package models;

public class CargoPlane extends Plane {

    public CargoPlane(int id, String model, int fuelConsumption, int flightRangeKm, double cruiseSpeedKmh, double capacity)
    {
        super(id, model, fuelConsumption, flightRangeKm, cruiseSpeedKmh, capacity);
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