package dk.kea.chargers.chargerservice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class FrontendController {
    @GetMapping(value = "/", produces = "text/html")
    @ResponseBody
    public String index() {
        return readFileSafely("./charger-service/frontend/index.html");
    }

    @GetMapping(value = "/jquery-3.7.1.min.js", produces = "text/javascript")
    @ResponseBody
    public String jquery_js() {
        return readFileSafely("./charger-service/frontend/jquery-3.7.1.min.js");
    }

    @GetMapping(value = "/favicon.ico", produces = "image/png")
    public ResponseEntity<byte[]> favicon_ico() {
        return readBinaryFileSafely("./charger-service/frontend/favicon.ico");
    }

    public static String readFileSafely(String filename) {
        try {
            return Files.readString(Paths.get(filename));
        } catch (IOException e) {
            return "Kunne ikke finde " + filename + ":<br>\n" + e.toString() + "<br>\nKigger i mappen:<br>\n" + System.getProperty("user.dir");
        }
    }

    public static ResponseEntity<byte[]> readBinaryFileSafely(String filename) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/png");
            var content = Files.readAllBytes(Paths.get(filename));
            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
