package models;

public class PassengerPlane extends Plane {

    private boolean hasBusinessClass;
    private int businessSeats;

    public PassengerPlane(String model, int fuelConsumption, int flightRangeKm, double cruiseSpeedKmh, double capacity,
                          boolean hasBusinessClass, int businessSeats) { // Власні поля

        super(model, fuelConsumption, flightRangeKm, cruiseSpeedKmh, capacity);

        this.hasBusinessClass = hasBusinessClass;

        if (hasBusinessClass) {
            this.businessSeats = businessSeats;
        } else {
            this.businessSeats = 0;
        }
    }

    public boolean isHasBusinessClass() {
        return hasBusinessClass;
    }

    public int getBusinessSeats() {
        return businessSeats;
    }

    public void setHasBusinessClass(boolean hasBusinessClass) {
        this.hasBusinessClass = hasBusinessClass;
        if (!hasBusinessClass) {
            this.businessSeats = 0;
        }
    }

    public void setBusinessSeats(int businessSeats) {
        if (this.hasBusinessClass) {
            this.businessSeats = businessSeats;
        }
    }

    @Override
    public String toString() {
        return "PassengerPlane{" +
                "baseInfo=" + super.toString() +
                ", hasBusinessClass=" + hasBusinessClass +
                ", businessSeats=" + businessSeats +
                '}';
    }
}