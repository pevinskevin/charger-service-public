package dk.kea.chargers.chargerservice;

public class ChargePoint {
    public int id;
    public int maxEffectKilowatt;
    public ChargePointStatus status;

    public ChargePoint(int id, int maxEffectKilowatt, ChargePointStatus initialStatus) {
        this.id = id;
        this.maxEffectKilowatt = maxEffectKilowatt;
        this.status = initialStatus;
    }
}
