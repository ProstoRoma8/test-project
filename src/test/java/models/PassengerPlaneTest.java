package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PassengerPlaneTest {

    // ── Конструктор з бізнес-класом ──

    @Test
    void constructor_withBusinessClass_setsAllFields() {
        PassengerPlane p = new PassengerPlane(1, "Boeing 737", 2500, 5000, 850, 180, true, 12);

        assertEquals(1,          p.getId());
        assertEquals("Boeing 737", p.getModel());
        assertEquals(2500,       p.getFuelConsumption());
        assertEquals(5000,       p.getFlightRangeKm());
        assertEquals(850.0,      p.getCruiseSpeedKmh(), 0.01);
        assertEquals(180.0,      p.getCapacity(),       0.01);
        assertTrue(p.isHasBusinessClass());
        assertEquals(12, p.getBusinessSeats());
    }

    @Test
    void constructor_withoutBusinessClass_businessSeatsIsZero() {
        // Навіть якщо передати businessSeats > 0, але hasBusiness=false → має стати 0
        PassengerPlane p = new PassengerPlane(2, "Airbus A320", 2400, 4800, 840, 150, false, 50);

        assertFalse(p.isHasBusinessClass());
        assertEquals(0, p.getBusinessSeats());
    }

    @Test
    void constructor_businessClassWithZeroSeats_seatsRemainZero() {
        PassengerPlane p = new PassengerPlane(3, "SmallJet", 800, 1500, 600, 50, true, 0);

        assertTrue(p.isHasBusinessClass());
        assertEquals(0, p.getBusinessSeats());
    }

    // ── setHasBusinessClass ──

    @Test
    void setHasBusinessClass_disabling_resetsSeatsToZero() {
        PassengerPlane p = new PassengerPlane(1, "LuxJet", 1000, 2000, 800, 100, true, 20);

        p.setHasBusinessClass(false);

        assertFalse(p.isHasBusinessClass());
        assertEquals(0, p.getBusinessSeats());
    }

    @Test
    void setHasBusinessClass_enabling_flagChanges_seatsStayZero() {
        PassengerPlane p = new PassengerPlane(1, "EcoJet", 1000, 2000, 800, 100, false, 0);

        p.setHasBusinessClass(true);

        assertTrue(p.isHasBusinessClass());
        // Місця ще 0 — треба задати окремо
        assertEquals(0, p.getBusinessSeats());
    }

    @Test
    void setHasBusinessClass_trueToTrue_noChange() {
        PassengerPlane p = new PassengerPlane(1, "Jet", 1000, 2000, 800, 100, true, 15);

        p.setHasBusinessClass(true);

        assertTrue(p.isHasBusinessClass());
        assertEquals(15, p.getBusinessSeats()); // місця не скинулись
    }

    // ── setBusinessSeats ──

    @Test
    void setBusinessSeats_whenBusinessClassEnabled_updatesSeats() {
        PassengerPlane p = new PassengerPlane(1, "LuxJet", 1000, 2000, 800, 100, true, 5);

        p.setBusinessSeats(25);

        assertEquals(25, p.getBusinessSeats());
    }

    @Test
    void setBusinessSeats_whenBusinessClassDisabled_doesNothing() {
        PassengerPlane p = new PassengerPlane(1, "SimpleJet", 1000, 2000, 800, 100, false, 0);

        p.setBusinessSeats(10);

        assertEquals(0, p.getBusinessSeats()); // залишається 0
    }

    // ── toString ──

    @Test
    void toString_containsClassName() {
        PassengerPlane p = new PassengerPlane(1, "Boeing", 1000, 2000, 800, 100, true, 10);
        String s = p.toString();

        assertNotNull(s);
        assertTrue(s.contains("PassengerPlane"));
    }

    @Test
    void toString_containsBusinessClassInfo_whenEnabled() {
        PassengerPlane p = new PassengerPlane(1, "Boeing", 1000, 2000, 800, 100, true, 10);
        String s = p.toString();

        assertTrue(s.contains("hasBusinessClass=true"));
        assertTrue(s.contains("businessSeats=10"));
    }

    @Test
    void toString_containsBusinessClassInfo_whenDisabled() {
        PassengerPlane p = new PassengerPlane(1, "Airbus", 1000, 2000, 800, 100, false, 0);
        String s = p.toString();

        assertTrue(s.contains("hasBusinessClass=false"));
        assertTrue(s.contains("businessSeats=0"));
    }

    // ── Спадковість ──

    @Test
    void isInstanceOfPlane() {
        PassengerPlane p = new PassengerPlane(1, "Jet", 1000, 2000, 800, 100, false, 0);
        assertInstanceOf(Plane.class, p);
    }
}
