package dk.kea.chargers.chargerservice;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ChargerController {
    private Charger charger = Charger.initializeFromEnv();

    @GetMapping("/charger")
    @ResponseBody
    public Charger getChargePoint() {
        return this.charger;
    }

    @PostMapping("/charger/stop")
    @ResponseBody
    public boolean stopChargePoint(@RequestParam int charge_point_id) {
        var chargePoint = charger.getChargePoint(charge_point_id);
        if (chargePoint.status == ChargePointStatus.StateC) {
            chargePoint.status = ChargePointStatus.StateB;
            return true;
        }
        return false;
    }

    @PostMapping("/charger/status")
    @ResponseBody
    public ChargePoint setChargePointStatus(@RequestParam int charge_point_id, @RequestParam ChargePointStatus status) {
        var chargePoint = charger.getChargePoint(charge_point_id);
        chargePoint.status = status;
        return chargePoint;
    }
}
