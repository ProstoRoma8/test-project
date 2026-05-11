package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PassengerPlaneTest {

    @Test
    void testConstructorWithBusinessClass() {
        // Сценарій: Створюємо літак, де Є бізнес-клас
        PassengerPlane plane = new PassengerPlane(
                "Boeing 737", 2500, 5000, 850, 180,
                true, 12 // hasBusiness = true, 12 місць
        );

        // Перевіряємо батьківські поля
        assertEquals("Boeing 737", plane.getModel());
        assertEquals(180, plane.getCapacity());

        // Перевіряємо власні поля
        assertTrue(plane.isHasBusinessClass(), "Має бути true");
        assertEquals(12, plane.getBusinessSeats(), "Має зберегтися 12 місць");
    }

    @Test
    void testConstructorWithoutBusinessClass() {
        // Сценарій: Передаємо кількість місць (50), але прапорець hasBusiness = false.
        // Конструктор має примусово встановити businessSeats = 0.
        PassengerPlane plane = new PassengerPlane(
                "Airbus A320", 2400, 4800, 840, 150,
                false, 50
        );

        assertFalse(plane.isHasBusinessClass());
        assertEquals(0, plane.getBusinessSeats(), "Кількість місць має обнулитися, бо бізнес-класу немає");
    }

    @Test
    void testSetHasBusinessClass_Disabling() {
        // Сценарій: Спочатку бізнес-клас є, потім ми його вимикаємо
        PassengerPlane plane = new PassengerPlane("TestJet", 100, 1000, 500, 100, true, 20);

        // Вимикаємо
        plane.setHasBusinessClass(false);

        // Перевіряємо, чи обнулилися місця (згідно з логікою твого сеттера)
        assertFalse(plane.isHasBusinessClass());
        assertEquals(0, plane.getBusinessSeats(), "Місця мають скинутися на 0 при вимкненні бізнес-класу");
    }

    @Test
    void testSetHasBusinessClass_Enabling() {
        // Сценарій: Вмикаємо бізнес-клас
        PassengerPlane plane = new PassengerPlane("TestJet", 100, 1000, 500, 100, false, 0);

        plane.setHasBusinessClass(true);
        assertTrue(plane.isHasBusinessClass());
        // Місця залишаються 0, поки ми їх не задамо явно, але прапорець змінився
        assertEquals(0, plane.getBusinessSeats());
    }

    @Test
    void testSetBusinessSeats_Logic() {
        // 1. Спробуємо встановити місця, коли бізнес-клас ВИМКНЕНО
        PassengerPlane planeNoBiz = new PassengerPlane("SimpleJet", 100, 1000, 500, 100, false, 0);
        planeNoBiz.setBusinessSeats(10);
        assertEquals(0, planeNoBiz.getBusinessSeats(), "Не можна встановити місця, якщо hasBusinessClass = false");

        // 2. Спробуємо встановити місця, коли бізнес-клас УВІМКНЕНО
        PassengerPlane planeWithBiz = new PassengerPlane("LuxJet", 100, 1000, 500, 100, true, 5);
        planeWithBiz.setBusinessSeats(25);
        assertEquals(25, planeWithBiz.getBusinessSeats(), "Місця мають оновитися успішно");
    }

    @Test
    void testToString() {
        PassengerPlane plane = new PassengerPlane("Boeing", 1000, 2000, 800, 100, true, 10);
        String result = plane.toString();

        assertNotNull(result);
        assertTrue(result.contains("PassengerPlane"));
        assertTrue(result.contains("hasBusinessClass=true"));
        assertTrue(result.contains("businessSeats=10"));
    }
}