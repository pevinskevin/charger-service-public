package dk.kea.chargers.chargerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@SpringBootApplication
public class ChargerServiceApplication {

    public static void main(String[] args) {
        RegisterRequest.registerService();
        SpringApplication.run(ChargerServiceApplication.class, args);
    }
}
