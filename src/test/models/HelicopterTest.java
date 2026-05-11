package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelicopterTest {

    @Test
    void testConstructorAndGetters() {
        Helicopter h = new Helicopter("Mi-8", 400, 800, 250, 24, 6000, true);

        assertEquals("Mi-8", h.getModel());
        assertEquals(400,   h.getFuelConsumption());
        assertEquals(800,   h.getFlightRangeKm());
        assertEquals(250.0, h.getCruiseSpeedKmh(), 0.01);
        assertEquals(24.0,  h.getCapacity(), 0.01);
        assertEquals(6000,  h.getMaxAltitudeM());
        assertTrue(h.isHasHoist());
    }

    @Test
    void testSetters() {
        Helicopter h = new Helicopter("TestHeli", 300, 500, 200, 10, 3000, false);

        h.setMaxAltitudeM(5000);
        h.setHasHoist(true);
        h.setModel("Updated");

        assertEquals(5000, h.getMaxAltitudeM());
        assertTrue(h.isHasHoist());
        assertEquals("Updated", h.getModel());
    }

    @Test
    void testToString() {
        Helicopter h = new Helicopter("Mi-26", 2000, 800, 295, 82, 4600, true);
        String result = h.toString();

        assertNotNull(result);
        assertTrue(result.contains("Helicopter"));
        assertTrue(result.contains("Mi-26"));
        assertTrue(result.contains("maxAltitude=4600"));
        assertTrue(result.contains("hasHoist=true"));
    }

    @Test
    void testIsHelicopterInstanceOfPlane() {
        Helicopter h = new Helicopter("EC135", 200, 600, 260, 8, 3000, true);
        assertInstanceOf(Plane.class, h);
    }
}
