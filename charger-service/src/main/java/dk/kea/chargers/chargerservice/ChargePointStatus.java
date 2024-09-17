package dk.kea.chargers.chargerservice;

public enum ChargePointStatus {
    // A charge point is available.
    StateA,

    // A car is connected to the charge point.
    StateB,

    // A car is actively charging.
    StateC,

    // An error has occurred on the charge point.
    StateF,

    // Unknown / Unavailable:
    //
    // The charge point's state is unknown, so we assume it's unavailable.
    StateU,
}
