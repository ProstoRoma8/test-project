package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестуємо спільну логіку абстрактного Plane через конкретні підкласи.
 */
class PlaneTest {

    private PassengerPlane createPlane() {
        return new PassengerPlane(1, "TestJet", 1000, 2000, 800, 100, false, 0);
    }

    // ── Getters ──

    @Test
    void getters_returnConstructorValues() {
        PassengerPlane p = new PassengerPlane(42, "Boeing 737", 2500, 5000, 850, 180, true, 20);

        assertEquals(42,     p.getId());
        assertEquals("Boeing 737", p.getModel());
        assertEquals(2500,   p.getFuelConsumption());
        assertEquals(5000,   p.getFlightRangeKm());
        assertEquals(850.0,  p.getCruiseSpeedKmh(), 0.01);
        assertEquals(180.0,  p.getCapacity(),        0.01);
    }

    // ── Setters ──

    @Test
    void setId_updatesId() {
        PassengerPlane p = createPlane();
        p.setId(99);
        assertEquals(99, p.getId());
    }

    @Test
    void setModel_updatesModel() {
        PassengerPlane p = createPlane();
        p.setModel("Airbus A380");
        assertEquals("Airbus A380", p.getModel());
    }

    @Test
    void setFuelConsumption_updatesValue() {
        PassengerPlane p = createPlane();
        p.setFuelConsumption(3000);
        assertEquals(3000, p.getFuelConsumption());
    }

    @Test
    void setFlightRangeKm_updatesValue() {
        PassengerPlane p = createPlane();
        p.setFlightRangeKm(7500);
        assertEquals(7500, p.getFlightRangeKm());
    }

    @Test
    void setCruiseSpeedKmh_updatesValue() {
        PassengerPlane p = createPlane();
        p.setCruiseSpeedKmh(920.5);
        assertEquals(920.5, p.getCruiseSpeedKmh(), 0.01);
    }

    @Test
    void setCapacity_updatesValue() {
        PassengerPlane p = createPlane();
        p.setCapacity(250.0);
        assertEquals(250.0, p.getCapacity(), 0.01);
    }

    // ── toString ──

    @Test
    void toString_containsKeyFields() {
        PassengerPlane p = new PassengerPlane(1, "Boeing", 1000, 2000, 800, 100, false, 0);
        String s = p.toString();

        assertNotNull(s);
        assertTrue(s.contains("Boeing"));
        assertTrue(s.contains("1000"));
        assertTrue(s.contains("2000"));
    }

    // ── instanceof hierarchy ──

    @Test
    void passengerPlane_isInstanceOfPlane() {
        assertInstanceOf(Plane.class, createPlane());
    }

    @Test
    void cargoPlane_isInstanceOfPlane() {
        assertInstanceOf(Plane.class, new CargoPlane(1, "AN-124", 15000, 5400, 865, 120));
    }

    @Test
    void helicopter_isInstanceOfPlane() {
        assertInstanceOf(Plane.class, new Helicopter(1, "Mi-8", 400, 800, 250, 24, 6000, true));
    }
}
