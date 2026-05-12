package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CargoPlaneTest {

    // ── Конструктор ──

    @Test
    void constructor_setsAllFieldsCorrectly() {
        CargoPlane cp = new CargoPlane(1, "AN-124", 15000, 5400, 865, 120.0);

        assertEquals(1,        cp.getId());
        assertEquals("AN-124", cp.getModel());
        assertEquals(15000,    cp.getFuelConsumption());
        assertEquals(5400,     cp.getFlightRangeKm());
        assertEquals(865.0,    cp.getCruiseSpeedKmh(), 0.01);
        assertEquals(120.0,    cp.getCapacity(),       0.01);
    }

    // ── getPayloadCapacity ──

    @Test
    void getPayloadCapacity_returnsSameAsCapacity() {
        CargoPlane cp = new CargoPlane(1, "Cargo", 5000, 3000, 700, 75.5);

        assertEquals(cp.getCapacity(), cp.getPayloadCapacity(), 0.01);
    }

    @Test
    void getPayloadCapacity_afterSetCapacity_updatesCorrectly() {
        CargoPlane cp = new CargoPlane(1, "Cargo", 5000, 3000, 700, 50.0);
        cp.setCapacity(100.0);

        assertEquals(100.0, cp.getPayloadCapacity(), 0.01);
    }

    // ── toString ──

    @Test
    void toString_containsCargoPlaneAndPayload() {
        CargoPlane cp = new CargoPlane(1, "AN-124", 15000, 5400, 865, 120.0);
        String s = cp.toString();

        assertNotNull(s);
        assertTrue(s.contains("CargoPlane"));
        assertTrue(s.contains("120.0"));
    }

    // ── Спадковість і setters ──

    @Test
    void isInstanceOfPlane() {
        assertInstanceOf(Plane.class, new CargoPlane(1, "Cargo", 5000, 3000, 700, 50));
    }

    @Test
    void setters_fromParent_workCorrectly() {
        CargoPlane cp = new CargoPlane(1, "Old", 5000, 3000, 700, 50);

        cp.setModel("New Cargo");
        cp.setFuelConsumption(8000);
        cp.setFlightRangeKm(4000);
        cp.setCruiseSpeedKmh(750.0);
        cp.setCapacity(200.0);

        assertEquals("New Cargo", cp.getModel());
        assertEquals(8000,  cp.getFuelConsumption());
        assertEquals(4000,  cp.getFlightRangeKm());
        assertEquals(750.0, cp.getCruiseSpeedKmh(), 0.01);
        assertEquals(200.0, cp.getCapacity(),       0.01);
    }

    // ── Граничні значення ──

    @Test
    void zeroPayload_isAllowed() {
        CargoPlane cp = new CargoPlane(1, "EmptyCargo", 1000, 1000, 500, 0.0);
        assertEquals(0.0, cp.getPayloadCapacity(), 0.01);
    }

    @Test
    void largePayload_isStored() {
        CargoPlane cp = new CargoPlane(1, "HugeCargo", 20000, 10000, 900, 500.0);
        assertEquals(500.0, cp.getPayloadCapacity(), 0.01);
    }
}
