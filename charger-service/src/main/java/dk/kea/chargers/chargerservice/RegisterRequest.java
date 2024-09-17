package dk.kea.chargers.chargerservice;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RegisterRequest {
    public String uniqueName;
    public String networkAddress;
    public String serviceType;

    public RegisterRequest(String uniqueName, String networkAddress, String serviceType) {
        this.uniqueName = uniqueName;
        this.networkAddress = networkAddress;
        this.serviceType = serviceType;
    }

    public static void registerService() {
        try {
            // FIXME: Load the charger's name from the environment variable `CHARGER_NAME`.
            var uniqueName = "charger-1";
            var chargerEndpoint = ChargerEnv.withDefaultStr("CHARGER_ENDPOINT", "http://127.0.0.1:8080/charger");
            var payload = new RegisterRequest(
                    uniqueName,
                    chargerEndpoint,
                    "charger"
            );
            var payloadSerialized = new ObjectMapper().writeValueAsString(payload);

            System.out.println(payloadSerialized);

            // Contact Directory Service and register this service:
            var client = HttpClient.newHttpClient();

            // Get the Directory Service endpoint from the environment:
            var directoryEndpoint = ChargerEnv
                    .withDefaultUri("DIRECTORY_ENDPOINT", URI.create("http://127.0.0.1:8000/directory"))
                    .resolve("/directory/register");

            // Build a registration request:
            var request = HttpRequest.newBuilder()
                    .uri(directoryEndpoint)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payloadSerialized))
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var success = response.statusCode() >= 200 && response.statusCode() < 300;
            if (success) {
                System.err.println("Successfully registered service!");
            } else {
                System.err.println("Failed to register service:");
                System.err.println("  " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}