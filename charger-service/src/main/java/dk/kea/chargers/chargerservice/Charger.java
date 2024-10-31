package dk.kea.chargers.chargerservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

public class Charger {
    public List<ChargePoint> charge_points;

    private Charger(List<ChargePoint> charge_points) {
        this.charge_points = charge_points;
    }

    public static Charger initializeFromEnv() {
        // FIXME: Use ChargerEnv methods to read `NUM_CHARGE_POINTS` and `MAX_EFFECT_KILOWATT` from environment variable.
        // FIXME: Environment variables are defined in compose.yaml, so update both here and there!
        // HINT: Look in the file ChargerEnv.java for useful helper functions!
        var numChargePoints = ChargerEnv.withDefaultInt("NUM_CHARGE_POINTS", 42);
        var maxEffectKilowatt = ChargerEnv.withDefaultInt("MAX_EFFEKT_KILOWATT", 42);
        var charge_points = new ArrayList<ChargePoint>(numChargePoints);
        for (int cpid = 1; cpid <= numChargePoints; cpid++) {
            charge_points.add(new ChargePoint(cpid, maxEffectKilowatt, ChargePointStatus.StateU));
        }

        return new Charger(charge_points);
    }

    public ChargePoint getChargePoint(int charge_point_id) throws ResponseStatusException {
        var charge_point = this.charge_points.get(charge_point_id);
        if (charge_point == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such Charge Point: " + charge_point_id);
        }
        return charge_point;
    }
}
