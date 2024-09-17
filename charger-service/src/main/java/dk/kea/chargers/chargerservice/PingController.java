package dk.kea.chargers.chargerservice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.*;
import java.util.TimeZone;

@Controller
public class PingController {
    @GetMapping("/ping")
    @ResponseBody
    public Pong ping() {
        return Pong.withSystemTime();
    }

    public record Pong (ZonedDateTime timestamp) {
        public static Pong withSystemTime() {
            return new Pong(ZonedDateTime.now(ZoneId.systemDefault()));
        }
    }
}
