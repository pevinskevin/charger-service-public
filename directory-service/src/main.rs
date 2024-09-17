use std::{sync::Arc, time::SystemTime};

use axum::{
    extract::State,
    http::{header::ACCEPT, HeaderMap, StatusCode},
    response::{Html, IntoResponse, Response},
    routing::{get, post},
    Json, Router,
};
use serde::{Deserialize, Serialize};
use serde_json::json;
use tokio::sync::Mutex;

#[derive(Debug, Default, Serialize, Deserialize)]
struct Service {
    pub unique_name: String,
    pub network_address: String,
    pub service_type: String,
}

#[derive(Debug, Default, Serialize, Deserialize)]
struct ServiceRegistration {
    pub service: Service,
    pub registered: chrono::DateTime<chrono::Utc>,
}

impl ServiceRegistration {
    pub fn register_now(service: Service) -> ServiceRegistration {
        ServiceRegistration {
            service,
            registered: SystemTime::now().into(),
        }
    }
}

#[derive(Debug, Default)]
struct AppState {
    registry: Mutex<Vec<ServiceRegistration>>,
}

impl AppState {
    pub fn with_self(service: Service) -> Self {
        Self {
            registry: Mutex::new(vec![ServiceRegistration::register_now(service)]),
        }
    }
}

#[tokio::main]
async fn main() {
    const DEFAULT_TCP_BIND_ADDR: &str = "0.0.0.0:8000";
    let bind_addr =
        std::env::var("TCP_BIND_ADDR").unwrap_or_else(|_| DEFAULT_TCP_BIND_ADDR.to_string());

    let app_state = AppState::with_self(Service {
        unique_name: "directory-1".to_string(),
        network_address: bind_addr.clone(),
        service_type: "directory".to_string(),
    });
    let shared_state = Arc::new(app_state);

    let app = Router::new()
        .route("/", get(get_welcome))
        .route("/ping", get(get_ping))
        .route("/directory", get(get_services_list))
        .route("/directory/register", post(post_register_service))
        .with_state(shared_state);

    let listener = tokio::net::TcpListener::bind(&bind_addr).await.unwrap();

    println!("Listening on: http://{}", bind_addr);
    axum::serve(listener, app).await.unwrap();
}

async fn get_welcome() -> Response {
    Html(
        "
        <h1>Welcome to directory-service!</h1>
        <p>This endpoint is unused.</p>
        <p>You may be looking for:</p>
        <ul>
            <li>GET <a href=\"/ping\">/ping</a></li>
            <li>GET <a href=\"/directory\">/directory</a></li>
            <li>POST /directory/register with an object like:</li>
        </ul>
  <pre>  {
    \"unique_name\": \"\",
    \"network_address\": \"\",
    \"service_type\": \"\",
  }</pre>",
    )
    .into_response()
}

async fn get_ping() -> Response {
    let timestamp: chrono::DateTime<chrono::Utc> = SystemTime::now().into();
    let pong = json!({ "timestamp": timestamp });
    Json(pong).into_response()
}

async fn get_services_list(State(state): State<Arc<AppState>>, headers: HeaderMap) -> Response {
    let registry = &*state.registry.lock().await;

    match headers.get(ACCEPT).map(|x| x.as_bytes()) {
        Some(b"text/html") => String::from("Hello!").into_response(),
        Some(b"application/json") => Json(registry).into_response(),
        _otherwise => Json(registry).into_response(),
    }
}

async fn post_register_service(
    State(state): State<Arc<AppState>>,
    Json(new_service): Json<Service>,
) -> Response {
    let mut registry = state.registry.lock().await;
    if registry
        .iter()
        .any(|service| service.service.unique_name == new_service.unique_name)
    {
        return StatusCode::CONFLICT.into_response();
    }

    let new_registration = ServiceRegistration::register_now(new_service);
    println!("Registered: {:?}", new_registration);
    registry.push(new_registration);

    StatusCode::CREATED.into_response()
}
