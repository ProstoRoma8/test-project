package models;

public class Helicopter extends Plane {

    private int maxAltitudeM;       // Максимальна висота польоту (метри)
    private boolean hasHoist;       // Чи є рятувальна лебідка

    public Helicopter(int id, String model, int fuelConsumption, int flightRangeKm,
                      double cruiseSpeedKmh, int passengerCapacity,
                      int maxAltitudeM, boolean hasHoist) {
        super(id, model, fuelConsumption, flightRangeKm, cruiseSpeedKmh, passengerCapacity);
        this.maxAltitudeM = maxAltitudeM;
        this.hasHoist = hasHoist;
    }

    // --- Getters ---
    public int getMaxAltitudeM() { return maxAltitudeM; }
    public boolean isHasHoist() { return hasHoist; }

    // --- Setters ---
    public void setMaxAltitudeM(int maxAltitudeM) { this.maxAltitudeM = maxAltitudeM; }
    public void setHasHoist(boolean hasHoist) { this.hasHoist = hasHoist; }

    @Override
    public String toString() {
        return "Helicopter{" +
                "baseInfo=" + super.toString() +
                ", maxAltitude=" + maxAltitudeM + " m" +
                ", hasHoist=" + hasHoist +
                '}';
    }
}
