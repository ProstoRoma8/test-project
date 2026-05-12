package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelicopterTest {

    // ── Конструктор ──

    @Test
    void constructor_setsAllFieldsCorrectly() {
        Helicopter h = new Helicopter(1, "Mi-8", 400, 800, 250, 24, 6000, true);

        assertEquals(1,       h.getId());
        assertEquals("Mi-8",  h.getModel());
        assertEquals(400,     h.getFuelConsumption());
        assertEquals(800,     h.getFlightRangeKm());
        assertEquals(250.0,   h.getCruiseSpeedKmh(), 0.01);
        assertEquals(24.0,    h.getCapacity(),        0.01);
        assertEquals(6000,    h.getMaxAltitudeM());
        assertTrue(h.isHasHoist());
    }

    @Test
    void constructor_withoutHoist_isStoredCorrectly() {
        Helicopter h = new Helicopter(2, "EC135", 200, 600, 260, 8, 3000, false);

        assertFalse(h.isHasHoist());
    }

    // ── Setters ──

    @Test
    void setMaxAltitudeM_updatesValue() {
        Helicopter h = new Helicopter(1, "TestHeli", 300, 500, 200, 10, 3000, false);
        h.setMaxAltitudeM(5000);
        assertEquals(5000, h.getMaxAltitudeM());
    }

    @Test
    void setHasHoist_trueToFalse() {
        Helicopter h = new Helicopter(1, "Rescue", 400, 800, 250, 24, 6000, true);
        h.setHasHoist(false);
        assertFalse(h.isHasHoist());
    }

    @Test
    void setHasHoist_falseToTrue() {
        Helicopter h = new Helicopter(1, "Civil", 300, 600, 220, 12, 4000, false);
        h.setHasHoist(true);
        assertTrue(h.isHasHoist());
    }

    @Test
    void parentSetters_workCorrectly() {
        Helicopter h = new Helicopter(1, "Old", 300, 500, 200, 10, 3000, false);

        h.setModel("Updated");
        h.setFuelConsumption(450);
        h.setFlightRangeKm(1000);
        h.setCruiseSpeedKmh(280.0);
        h.setCapacity(20.0);

        assertEquals("Updated", h.getModel());
        assertEquals(450,    h.getFuelConsumption());
        assertEquals(1000,   h.getFlightRangeKm());
        assertEquals(280.0,  h.getCruiseSpeedKmh(), 0.01);
        assertEquals(20.0,   h.getCapacity(),        0.01);
    }

    // ── toString ──

    @Test
    void toString_containsHelicopterClassName() {
        Helicopter h = new Helicopter(1, "Mi-26", 2000, 800, 295, 82, 4600, true);
        assertTrue(h.toString().contains("Helicopter"));
    }

    @Test
    void toString_containsModelName() {
        Helicopter h = new Helicopter(1, "Mi-26", 2000, 800, 295, 82, 4600, true);
        assertTrue(h.toString().contains("Mi-26"));
    }

    @Test
    void toString_containsAltitude() {
        Helicopter h = new Helicopter(1, "Mi-26", 2000, 800, 295, 82, 4600, true);
        assertTrue(h.toString().contains("4600"));
    }

    @Test
    void toString_containsHoistInfo() {
        Helicopter h = new Helicopter(1, "Mi-26", 2000, 800, 295, 82, 4600, true);
        assertTrue(h.toString().contains("hasHoist=true"));
    }

    @Test
    void toString_withoutHoist_containsFalse() {
        Helicopter h = new Helicopter(1, "Civil", 300, 600, 220, 12, 4000, false);
        assertTrue(h.toString().contains("hasHoist=false"));
    }

    // ── Спадковість ──

    @Test
    void isInstanceOfPlane() {
        Helicopter h = new Helicopter(1, "EC135", 200, 600, 260, 8, 3000, true);
        assertInstanceOf(Plane.class, h);
    }

    // ── Граничні значення ──

    @Test
    void minimalAltitude_isStored() {
        Helicopter h = new Helicopter(1, "Low", 200, 400, 180, 4, 100, false);
        assertEquals(100, h.getMaxAltitudeM());
    }

    @Test
    void maximalAltitude_isStored() {
        Helicopter h = new Helicopter(1, "High", 500, 900, 300, 6, 9000, true);
        assertEquals(9000, h.getMaxAltitudeM());
    }
}
