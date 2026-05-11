package models;

public class CargoPlane extends Plane {

    public CargoPlane(String model, int fuelConsumption, int flightRangeKm,
                      double cruiseSpeedKmh, double capacity) {

        super(model, fuelConsumption, flightRangeKm, cruiseSpeedKmh, capacity);
    }

    public double getPayloadCapacity() {
        return getCapacity();
    }

    @Override
    public String toString() {
        return "CargoPlane{" +
                "baseInfo=" + super.toString() +
                ", payload=" + getCapacity() + " tons" +
                '}';
    }
}