package ui;

import javafx.beans.property.*;
import models.CargoPlane;
import models.Helicopter;
import models.PassengerPlane;
import models.Plane;

public class PlaneRow {

    private final Plane plane;

    private final StringProperty  model    = new SimpleStringProperty();
    private final StringProperty  type     = new SimpleStringProperty();
    private final IntegerProperty range    = new SimpleIntegerProperty();
    private final IntegerProperty fuel     = new SimpleIntegerProperty();
    private final DoubleProperty  speed    = new SimpleDoubleProperty();
    private final StringProperty  capacity = new SimpleStringProperty();

    public PlaneRow(Plane plane) {
        this.plane = plane;
        model.set(plane.getModel());
        range.set(plane.getFlightRangeKm());
        fuel.set(plane.getFuelConsumption());
        speed.set(plane.getCruiseSpeedKmh());

        if (plane instanceof PassengerPlane) {
            type.set("Пасажирський");
            capacity.set((int) plane.getCapacity() + " осіб");
        } else if (plane instanceof CargoPlane) {
            type.set("Вантажний");
            capacity.set(plane.getCapacity() + " тонн");
        } else if (plane instanceof Helicopter) {
            type.set("Гелікоптер");
            capacity.set((int) plane.getCapacity() + " осіб");
        } else {
            type.set("Невідомий");
            capacity.set("—");
        }
    }

    public Plane getPlane() { return plane; }

    public StringProperty  modelProperty()    { return model; }
    public StringProperty  typeProperty()     { return type; }
    public IntegerProperty rangeProperty()    { return range; }
    public IntegerProperty fuelProperty()     { return fuel; }
    public DoubleProperty  speedProperty()    { return speed; }
    public StringProperty  capacityProperty() { return capacity; }
}
