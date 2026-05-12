package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AirLineTest {

    private AirLine airline;
    private PassengerPlane boeing737;
    private PassengerPlane airbusA320;
    private CargoPlane antonov;
    private Helicopter mi8;

    @BeforeEach
    void setUp() {
        airline = new AirLine("Ukraine International");

        boeing737  = new PassengerPlane(1, "Boeing 737",  2500, 5000, 850,  180, false, 0);
        airbusA320 = new PassengerPlane(2, "Airbus A320", 2400, 4800, 840,  150, true, 20);
        antonov    = new CargoPlane(3, "AN-124", 15000, 5400, 865, 120.0);
        mi8        = new Helicopter(4, "Mi-8", 400, 800, 250, 24, 6000, true);
    }

    // ── Конструктор ──

    @Test
    void constructor_setsNameAndEmptyFleet() {
        assertEquals("Ukraine International", airline.getName());
        assertNotNull(airline.getFleet());
        assertTrue(airline.getFleet().isEmpty());
    }

    // ── addPlane ──

    @Test
    void addPlane_addsPlaneToFleet() {
        airline.addPlane(boeing737);
        assertEquals(1, airline.getFleet().size());
        assertSame(boeing737, airline.getFleet().get(0));
    }

    @Test
    void addPlane_nullIsIgnored() {
        airline.addPlane(null);
        assertTrue(airline.getFleet().isEmpty());
    }

    @Test
    void addPlane_multipleTypesAreAdded() {
        airline.addPlane(boeing737);
        airline.addPlane(antonov);
        airline.addPlane(mi8);
        assertEquals(3, airline.getFleet().size());
    }

    // ── deletePlane ──

    @Test
    void deletePlane_removesCorrectPlane() {
        airline.addPlane(boeing737);
        airline.addPlane(airbusA320);

        boolean result = airline.deletePlane(0);

        assertTrue(result);
        assertEquals(1, airline.getFleet().size());
        assertSame(airbusA320, airline.getFleet().get(0));
    }

    @Test
    void deletePlane_negativeIndex_returnsFalse() {
        airline.addPlane(boeing737);
        assertFalse(airline.deletePlane(-1));
        assertEquals(1, airline.getFleet().size());
    }

    @Test
    void deletePlane_outOfBoundIndex_returnsFalse() {
        airline.addPlane(boeing737);
        assertFalse(airline.deletePlane(5));
        assertEquals(1, airline.getFleet().size());
    }

    @Test
    void deletePlane_indexEqualToSize_returnsFalse() {
        airline.addPlane(boeing737);
        assertFalse(airline.deletePlane(1)); // розмір = 1, індекс 1 — поза межами
    }

    @Test
    void deletePlane_emptyFleet_returnsFalse() {
        assertFalse(airline.deletePlane(0));
    }

    // ── getPlaneByIndex ──

    @Test
    void getPlaneByIndex_returnsCorrectPlane() {
        airline.addPlane(boeing737);
        airline.addPlane(airbusA320);
        assertSame(boeing737,  airline.getPlaneByIndex(0));
        assertSame(airbusA320, airline.getPlaneByIndex(1));
    }

    @Test
    void getPlaneByIndex_negativeIndex_returnsNull() {
        airline.addPlane(boeing737);
        assertNull(airline.getPlaneByIndex(-1));
    }

    @Test
    void getPlaneByIndex_outOfBound_returnsNull() {
        airline.addPlane(boeing737);
        assertNull(airline.getPlaneByIndex(1));
        assertNull(airline.getPlaneByIndex(100));
    }

    // ── getSortedByRange ──

    @Test
    void getSortedByRange_returnsSortedList() {
        // mi8: 800, airbusA320: 4800, boeing737: 5000, antonov: 5400
        airline.addPlane(boeing737);
        airline.addPlane(antonov);
        airline.addPlane(mi8);
        airline.addPlane(airbusA320);

        List<Plane> sorted = airline.getSortedByRange();

        assertEquals(4, sorted.size());
        assertEquals(800,  sorted.get(0).getFlightRangeKm());
        assertEquals(4800, sorted.get(1).getFlightRangeKm());
        assertEquals(5000, sorted.get(2).getFlightRangeKm());
        assertEquals(5400, sorted.get(3).getFlightRangeKm());
    }

    @Test
    void getSortedByRange_doesNotMutateOriginalFleet() {
        airline.addPlane(boeing737);
        airline.addPlane(mi8);  // менша дальність — буде першим після сортування

        List<Plane> sorted = airline.getSortedByRange();

        // оригінальний порядок не змінився
        assertSame(boeing737, airline.getFleet().get(0));
        assertSame(mi8,       airline.getFleet().get(1));
        // відсортований: mi8 перший
        assertSame(mi8,      sorted.get(0));
        assertSame(boeing737, sorted.get(1));
    }

    @Test
    void getSortedByRange_emptyFleet_returnsEmptyList() {
        assertTrue(airline.getSortedByRange().isEmpty());
    }

    // ── getSortedByFuelConsumption ──

    @Test
    void getSortedByFuelConsumption_returnsSortedList() {
        // mi8: 400, airbusA320: 2400, boeing737: 2500, antonov: 15000
        airline.addPlane(boeing737);
        airline.addPlane(antonov);
        airline.addPlane(mi8);
        airline.addPlane(airbusA320);

        List<Plane> sorted = airline.getSortedByFuelConsumption();

        assertEquals(400,   sorted.get(0).getFuelConsumption());
        assertEquals(2400,  sorted.get(1).getFuelConsumption());
        assertEquals(2500,  sorted.get(2).getFuelConsumption());
        assertEquals(15000, sorted.get(3).getFuelConsumption());
    }

    // ── filterByFuelConsumption ──

    @Test
    void filterByFuelConsumption_returnsMatchingPlanes() {
        airline.addPlane(boeing737);   // 2500
        airline.addPlane(airbusA320);  // 2400
        airline.addPlane(antonov);     // 15000
        airline.addPlane(mi8);         // 400

        List<Plane> result = airline.filterByFuelConsumption(2000, 3000);

        assertEquals(2, result.size());
        assertTrue(result.contains(boeing737));
        assertTrue(result.contains(airbusA320));
    }

    @Test
    void filterByFuelConsumption_exactBoundary_isIncluded() {
        airline.addPlane(boeing737); // 2500

        List<Plane> result = airline.filterByFuelConsumption(2500, 2500);

        assertEquals(1, result.size());
        assertSame(boeing737, result.get(0));
    }

    @Test
    void filterByFuelConsumption_noMatch_returnsEmpty() {
        airline.addPlane(boeing737); // 2500
        assertTrue(airline.filterByFuelConsumption(9000, 10000).isEmpty());
    }

    // ── filterByMinRange ──

    @Test
    void filterByMinRange_returnsOnlyPlanesAboveMin() {
        airline.addPlane(mi8);         // 800
        airline.addPlane(airbusA320);  // 4800
        airline.addPlane(boeing737);   // 5000

        List<Plane> result = airline.filterByMinRange(4800);

        assertEquals(2, result.size());
        assertTrue(result.contains(airbusA320));
        assertTrue(result.contains(boeing737));
        assertFalse(result.contains(mi8));
    }

    @Test
    void filterByMinRange_zeroMin_returnsAll() {
        airline.addPlane(boeing737);
        airline.addPlane(mi8);

        assertEquals(2, airline.filterByMinRange(0).size());
    }

    // ── Аналітика ──

    @Test
    void getTotalPassengerPlanes_countsCorrectly() {
        airline.addPlane(boeing737);
        airline.addPlane(airbusA320);
        airline.addPlane(antonov);
        airline.addPlane(mi8);

        assertEquals(2, airline.getTotalPassengerPlanes());
    }

    @Test
    void getTotalCargoPlanes_countsCorrectly() {
        airline.addPlane(antonov);
        airline.addPlane(boeing737);

        assertEquals(1, airline.getTotalCargoPlanes());
    }

    @Test
    void getTotalHelicopters_countsCorrectly() {
        airline.addPlane(mi8);
        airline.addPlane(new Helicopter(5, "Mi-26", 2000, 800, 295, 82, 4600, false));

        assertEquals(2, airline.getTotalHelicopters());
    }

    @Test
    void getTotalCapacity_sumOnlyPassengerPlanes() {
        airline.addPlane(boeing737);   // 180
        airline.addPlane(airbusA320);  // 150
        airline.addPlane(antonov);     // не рахується
        airline.addPlane(mi8);         // не рахується

        assertEquals(330.0, airline.getTotalCapacity(), 0.01);
    }

    @Test
    void getTotalPayload_sumOnlyCargoPlanes() {
        airline.addPlane(antonov);     // 120.0 тонн
        airline.addPlane(boeing737);   // не рахується

        assertEquals(120.0, airline.getTotalPayload(), 0.01);
    }

    @Test
    void analytics_emptyFleet_returnsZero() {
        assertEquals(0, airline.getTotalPassengerPlanes());
        assertEquals(0, airline.getTotalCargoPlanes());
        assertEquals(0, airline.getTotalHelicopters());
        assertEquals(0.0, airline.getTotalCapacity(), 0.01);
        assertEquals(0.0, airline.getTotalPayload(),  0.01);
    }

    // ── printFleetShort ──

    @Test
    void printFleetShort_doesNotThrow_onEmptyFleet() {
        assertDoesNotThrow(() -> airline.printFleetShort());
    }

    @Test
    void printFleetShort_doesNotThrow_withAllTypes() {
        airline.addPlane(boeing737);
        airline.addPlane(antonov);
        airline.addPlane(mi8);
        assertDoesNotThrow(() -> airline.printFleetShort());
    }
}
