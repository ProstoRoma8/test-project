package models;

public abstract class Plane {

    protected int id;
    protected String model;
    protected int fuelConsumption;
    protected int flightRangeKm;
    protected double cruiseSpeedKmh;
    protected double capacity;

    public Plane(int id, String model, int fuelConsumption, int flightRangeKm,
                 double cruiseSpeedKmh, double capacity) {
        this.id = this.id;
        this.model = model;
        this.fuelConsumption = fuelConsumption;
        this.flightRangeKm = flightRangeKm;
        this.cruiseSpeedKmh = cruiseSpeedKmh;
        this.capacity = capacity;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getModel() { return model; }
    public int getFuelConsumption() { return fuelConsumption; }
    public int getFlightRangeKm() { return flightRangeKm; }
    public double getCruiseSpeedKmh() { return cruiseSpeedKmh; }
    public double getCapacity() { return capacity; }

    // --- Setters ---
    public void setId(int id) { this.id = id; }
    public void setModel(String model) { this.model = model; }
    public void setFuelConsumption(int fuelConsumption) { this.fuelConsumption = fuelConsumption; }
    public void setFlightRangeKm(int flightRangeKm) { this.flightRangeKm = flightRangeKm; }
    public void setCruiseSpeedKmh(double cruiseSpeedKmh) { this.cruiseSpeedKmh = cruiseSpeedKmh; }
    public void setCapacity(double capacity) { this.capacity = capacity; }

    @Override
    public String toString() {
        return "Plane{" +
                "id=" + id +
                "model='" + model + '\'' +
                ", fuelConsumption=" + fuelConsumption + " kg/h" +
                ", flightRange=" + flightRangeKm + " km" +
                ", cruiseSpeed=" + cruiseSpeedKmh + " km/h" +
                ", capacity=" + capacity +
                '}';
    }


}
