package ui;

import javafx.beans.property.*;
import models.CargoPlane;
import models.Helicopter;
import models.PassengerPlane;
import models.Plane;

/**
 * ViewModel-обгортка над Plane для відображення у TableView.
 *
 * ФІКС: PropertyValueFactory шукає геттери за конвенцією JavaBeans:
 *   getValue("model") → getModel()
 * Тому додані явні геттери getModel(), getType(), getRange(), getFuel(),
 * getSpeed(), getCapacity() поряд із *Property()-методами.
 */
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

    // ── Довідка до реального об'єкту ──
    public Plane getPlane() { return plane; }

    // ── JavaBeans геттери (потрібні PropertyValueFactory) ──
    public String  getModel()    { return model.get(); }
    public String  getType()     { return type.get(); }
    public int     getRange()    { return range.get(); }
    public int     getFuel()     { return fuel.get(); }
    public double  getSpeed()    { return speed.get(); }
    public String  getCapacity() { return capacity.get(); }

    // ── Property-методи (для прив'язок / слухачів) ──
    public StringProperty  modelProperty()    { return model; }
    public StringProperty  typeProperty()     { return type; }
    public IntegerProperty rangeProperty()    { return range; }
    public IntegerProperty fuelProperty()     { return fuel; }
    public DoubleProperty  speedProperty()    { return speed; }
    public StringProperty  capacityProperty() { return capacity; }
}
