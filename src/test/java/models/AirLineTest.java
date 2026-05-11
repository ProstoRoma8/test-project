package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AirLineTest {

    private AirLine airline;

    @BeforeEach
    void setUp() {
        // Створюємо нову авіакомпанію перед кожним тестом
        airline = new AirLine("Ukraine International");
    }

    @Test
    void testConstructorAndGetName() {
        assertEquals("Ukraine International", airline.getName());
        assertNotNull(airline.getFleet());
        assertTrue(airline.getFleet().isEmpty(), "Спочатку флот має бути порожнім");
    }

    @Test
    void testAddPlane() {
        Plane p1 = new PassengerPlane("Boeing 737", 2500, 5000, 850, 180, false, 0);
        airline.addPlane(p1);

        assertEquals(1, airline.getFleet().size());
        assertEquals(p1, airline.getFleet().get(0));

        // Перевірка на додавання null (не має впасти і не має додати)
        airline.addPlane(null);
        assertEquals(1, airline.getFleet().size());
    }

    @Test
    void testGetPlaneByIndex() {
        Plane p1 = new PassengerPlane("Plane 1", 100, 1000, 500, 100, false, 0);
        Plane p2 = new CargoPlane("Plane 2", 200, 2000, 600, 50);
        airline.addPlane(p1);
        airline.addPlane(p2);

        // Позитивні сценарії
        assertEquals(p1, airline.getPlaneByIndex(0));
        assertEquals(p2, airline.getPlaneByIndex(1));

        // Негативні сценарії (індекси за межами)
        assertNull(airline.getPlaneByIndex(-1));
        assertNull(airline.getPlaneByIndex(100));
        assertNull(airline.getPlaneByIndex(2)); // Індекс дорівнює розміру
    }

    @Test
    void testCalculations() {
        // Додаємо:
        // 1. Пасажирський (100 місць)
        airline.addPlane(new PassengerPlane("Pass 1", 100, 1000, 500, 100, false, 0));
        // 2. Пасажирський (200 місць)
        airline.addPlane(new PassengerPlane("Pass 2", 100, 1000, 500, 200, false, 0));
        // 3. Вантажний (50 тонн)
        airline.addPlane(new CargoPlane("Cargo 1", 200, 2000, 600, 50));

        // Перевірка кількості літаків певного типу
        assertEquals(2, airline.getTotalPassengerPlanes());
        assertEquals(1, airline.getTotalCargoPlanes());

        // Перевірка загальної місткості (лише пасажирські)
        assertEquals(300.0, airline.getTotalCapacity(), 0.01);

        // Перевірка загальної вантажопідйомності (лише вантажні)
        assertEquals(50.0, airline.getTotalPayload(), 0.01);
    }

    @Test
    void testPrintFleetShort() {
        // Цей тест перевіряє, чи не падає метод printFleetShort при виклику.
        // 1. Порожній список
        assertDoesNotThrow(() -> airline.printFleetShort());

        // 2. Заповнений список
        airline.addPlane(new CargoPlane("Test", 100, 1000, 500, 50));
        assertDoesNotThrow(() -> airline.printFleetShort());
    }
}