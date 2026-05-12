package commands.UserCommands;

import models.CargoPlane;
import models.Helicopter;
import models.PassengerPlane;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестуємо математичну логіку калькулятора рейсу без Scanner/AppContext.
 * Формули: flightHours = distance / speed; fuelNeeded = hours * consumption.
 */
class FlightCalculatorLogicTest {

    // ── Час польоту ──

    @Test
    void flightTime_basicCalculation() {
        double speed    = 850.0;  // км/год
        int    distance = 1700;   // км
        double expected = 2.0;    // год

        double actual = distance / speed;

        assertEquals(expected, actual, 0.001);
    }

    @Test
    void flightTime_fractionalResult() {
        double speed    = 800.0;
        int    distance = 1000;
        double expected = 1.25; // год

        assertEquals(expected, distance / speed, 0.001);
    }

    // ── Витрата пального ──

    @Test
    void fuelNeeded_exactValue() {
        double flightHours  = 2.0;
        int    consumption  = 2500; // кг/год
        double expectedFuel = 5000.0; // кг

        assertEquals(expectedFuel, flightHours * consumption, 0.1);
    }

    @Test
    void fuelNeeded_fractionalHours() {
        double flightHours = 1.5;
        int    consumption = 1000;

        assertEquals(1500.0, flightHours * consumption, 0.1);
    }

    // ── Перевірка допустимості рейсу ──

    @Test
    void flightFeasible_whenDistanceLessThanRange() {
        int distance = 3000;
        int maxRange = 5000;
        assertTrue(distance <= maxRange);
    }

    @Test
    void flightFeasible_whenDistanceEqualsRange() {
        int distance = 5000;
        int maxRange = 5000;
        assertTrue(distance <= maxRange);
    }

    @Test
    void flightNotFeasible_whenDistanceExceedsRange() {
        int distance = 6000;
        int maxRange = 5000;
        assertFalse(distance <= maxRange);
    }

    // ── Вартість пального ──

    @Test
    void fuelCost_basicCalculation() {
        double fuelKg       = 5000.0;
        double pricePerKg   = 0.85;
        double expectedCost = 4250.0;

        assertEquals(expectedCost, fuelKg * pricePerKg, 0.01);
    }

    @Test
    void fuelCost_zeroPriceResultsInZeroCost() {
        double fuelKg     = 5000.0;
        double pricePerKg = 0.0;

        assertEquals(0.0, fuelKg * pricePerKg, 0.001);
    }

    // ── Інтеграція: повний розрахунок для реального літака ──

    @Test
    void fullCalculation_boeing737_shortRoute() {
        PassengerPlane plane = new PassengerPlane(1, "Boeing 737", 2500, 5000, 850, 180, false, 0);
        int distance = 1700; // км

        assertTrue(distance <= plane.getFlightRangeKm(), "Рейс має бути можливим");

        double hours     = (double) distance / plane.getCruiseSpeedKmh();
        double fuelNeeded = hours * plane.getFuelConsumption();

        assertEquals(2.0,    hours,     0.001);
        assertEquals(5000.0, fuelNeeded, 0.1);
    }

    @Test
    void fullCalculation_helicopter_shortDistance() {
        Helicopter h = new Helicopter(1, "Mi-8", 400, 800, 250, 24, 6000, true);
        int distance = 500;

        assertTrue(distance <= h.getFlightRangeKm());

        double hours      = (double) distance / h.getCruiseSpeedKmh();
        double fuelNeeded = hours * h.getFuelConsumption();

        assertEquals(2.0,   hours,     0.001);
        assertEquals(800.0, fuelNeeded, 0.1);
    }

    @Test
    void fullCalculation_cargoPlane_longRoute() {
        CargoPlane cargo = new CargoPlane(1, "AN-124", 15000, 5400, 865, 120);
        int distance = 4330; // точно 5 годин

        assertTrue(distance <= cargo.getFlightRangeKm());

        double hours      = (double) distance / cargo.getCruiseSpeedKmh();
        double fuelNeeded = hours * cargo.getFuelConsumption();

        assertEquals(5.0,     hours,     0.001);
        assertEquals(75000.0, fuelNeeded, 1.0);
    }
}
