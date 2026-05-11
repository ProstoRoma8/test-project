package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CargoPlaneTest {

    @Test
    void testCargoPlaneConstructorAndGetters() {
        // 1. Підготовка даних (Arrange)
        String model = "Antonov An-124";
        int fuel = 12000;      // кг/год
        int range = 4500;      // км
        double speed = 800.0;  // км/год
        double capacity = 150.0; // тонн

        // 2. Дія (Act)
        CargoPlane cargoPlane = new CargoPlane(model, fuel, range, speed, capacity);

        // 3. Перевірка (Assert)
        // Перевіряємо, чи конструктор правильно записав дані в поля батьківського класу Plane
        assertEquals("Antonov An-124", cargoPlane.getModel());
        assertEquals(12000, cargoPlane.getFuelConsumption());
        assertEquals(4500, cargoPlane.getFlightRangeKm());

        // Для double використовуємо третій параметр (delta) - допустиму похибку
        assertEquals(800.0, cargoPlane.getCruiseSpeedKmh(), 0.01);
        assertEquals(150.0, cargoPlane.getCapacity(), 0.01);
    }

    @Test
    void testGetPayloadCapacity() {
        // Перевіряємо специфічний метод для вантажних літаків
        CargoPlane cargoPlane = new CargoPlane("TestCargo", 100, 1000, 500, 55.5);

        // Метод getPayloadCapacity має повертати те саме, що й getCapacity
        assertEquals(55.5, cargoPlane.getPayloadCapacity(), 0.01);
    }

    @Test
    void testToString() {
        CargoPlane cargoPlane = new CargoPlane("Mriya", 5000, 10000, 850, 250);
        String result = cargoPlane.toString();

        // Перевіряємо, чи рядок містить ключові слова
        assertNotNull(result);
        assertTrue(result.contains("CargoPlane")); // Має бути назва класу
        assertTrue(result.contains("Mriya"));      // Має бути назва моделі
        assertTrue(result.contains("payload="));   // Має бути поле payload
        assertTrue(result.contains("250"));        // Має бути значення вантажопідйомності
    }
}