package dao;

import models.*;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Інтеграційні тести PlaneDao — потребують запущеного SQL Server
 * з базою AirlineDB та таблицею planes.
 *
 * Кожен тест додає тестовий запис і видаляє його після виконання,
 * щоб не засмічувати базу даних.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlaneDaoTest {

    private static final PlaneDao dao = new PlaneDao();

    // ── getAllPlanes ──────────────────────────────

    @Test
    @Order(1)
    void testGetAllPlanesReturnsList() {
        List<Plane> planes = dao.getAllPlanes();
        assertNotNull(planes);
        // Список може бути порожнім, але не null
    }

    // ── savePlane + deletePlane (PassengerPlane) ──

    @Test
    @Order(2)
    void testSaveAndDeletePassengerPlane() {
        PassengerPlane p = new PassengerPlane(0, "Test Boeing 737",
                2500, 5000, 850, 180, true, 12);

        int countBefore = dao.getAllPlanes().size();
        dao.savePlane(p);

        List<Plane> after = dao.getAllPlanes();
        assertEquals(countBefore + 1, after.size());

        // Знаходимо щойно збережений запис і видаляємо
        Plane saved = after.stream()
                .filter(pl -> "Test Boeing 737".equals(pl.getModel()))
                .findFirst()
                .orElse(null);
        assertNotNull(saved);
        assertInstanceOf(PassengerPlane.class, saved);

        PassengerPlane savedPP = (PassengerPlane) saved;
        assertEquals(2500, savedPP.getFuelConsumption());
        assertEquals(5000, savedPP.getFlightRangeKm());
        assertEquals(850.0, savedPP.getCruiseSpeedKmh(), 0.01);
        assertEquals(180.0, savedPP.getCapacity(), 0.01);
        assertTrue(savedPP.isHasBusinessClass());
        assertEquals(12, savedPP.getBusinessSeats());

        dao.deletePlane(saved.getId());
        assertEquals(countBefore, dao.getAllPlanes().size());
    }

    // ── savePlane + deletePlane (CargoPlane) ──────

    @Test
    @Order(3)
    void testSaveAndDeleteCargoPlane() {
        CargoPlane p = new CargoPlane(0, "Test AN-124",
                12000, 4500, 800, 150);

        int countBefore = dao.getAllPlanes().size();
        dao.savePlane(p);

        List<Plane> after = dao.getAllPlanes();
        assertEquals(countBefore + 1, after.size());

        Plane saved = after.stream()
                .filter(pl -> "Test AN-124".equals(pl.getModel()))
                .findFirst()
                .orElse(null);
        assertNotNull(saved);
        assertInstanceOf(CargoPlane.class, saved);
        assertEquals(150.0, saved.getCapacity(), 0.01);

        dao.deletePlane(saved.getId());
        assertEquals(countBefore, dao.getAllPlanes().size());
    }

    // ── savePlane + deletePlane (Helicopter) ──────

    @Test
    @Order(4)
    void testSaveAndDeleteHelicopter() {
        Helicopter h = new Helicopter(0, "Test Mi-8",
                400, 800, 250, 24, 6000, true);

        int countBefore = dao.getAllPlanes().size();
        dao.savePlane(h);

        List<Plane> after = dao.getAllPlanes();
        assertEquals(countBefore + 1, after.size());

        Plane saved = after.stream()
                .filter(pl -> "Test Mi-8".equals(pl.getModel()))
                .findFirst()
                .orElse(null);
        assertNotNull(saved);
        assertInstanceOf(Helicopter.class, saved);

        Helicopter savedH = (Helicopter) saved;
        assertEquals(6000, savedH.getMaxAltitudeM());
        assertTrue(savedH.isHasHoist());

        dao.deletePlane(saved.getId());
        assertEquals(countBefore, dao.getAllPlanes().size());
    }

    // ── updatePlane ───────────────────────────────

    @Test
    @Order(5)
    void testUpdatePassengerPlane() {
        // 1. Зберігаємо
        PassengerPlane p = new PassengerPlane(0, "Update Test Plane",
                2000, 4000, 800, 150, false, 0);
        dao.savePlane(p);

        Plane saved = dao.getAllPlanes().stream()
                .filter(pl -> "Update Test Plane".equals(pl.getModel()))
                .findFirst().orElse(null);
        assertNotNull(saved);

        // 2. Оновлюємо
        saved.setModel("Updated Plane Name");
        saved.setFuelConsumption(3000);
        ((PassengerPlane) saved).setHasBusinessClass(true);
        ((PassengerPlane) saved).setBusinessSeats(20);
        dao.updatePlane(saved);

        // 3. Перевіряємо
        Plane updated = dao.getAllPlanes().stream()
                .filter(pl -> "Updated Plane Name".equals(pl.getModel()))
                .findFirst().orElse(null);
        assertNotNull(updated);
        assertEquals(3000, updated.getFuelConsumption());
        assertTrue(((PassengerPlane) updated).isHasBusinessClass());
        assertEquals(20, ((PassengerPlane) updated).getBusinessSeats());

        // 4. Прибираємо
        dao.deletePlane(updated.getId());
    }

    @Test
    @Order(6)
    void testUpdateCargoPlane() {
        CargoPlane p = new CargoPlane(0, "Update Cargo Test", 5000, 3000, 700, 80);
        dao.savePlane(p);

        Plane saved = dao.getAllPlanes().stream()
                .filter(pl -> "Update Cargo Test".equals(pl.getModel()))
                .findFirst().orElse(null);
        assertNotNull(saved);

        saved.setCapacity(120.0);
        dao.updatePlane(saved);

        Plane updated = dao.getAllPlanes().stream()
                .filter(pl -> "Update Cargo Test".equals(pl.getModel()))
                .findFirst().orElse(null);
        assertNotNull(updated);
        assertEquals(120.0, updated.getCapacity(), 0.01);

        dao.deletePlane(updated.getId());
    }

    @Test
    @Order(7)
    void testUpdateHelicopter() {
        Helicopter h = new Helicopter(0, "Update Heli Test",
                300, 600, 240, 10, 3000, false);
        dao.savePlane(h);

        Plane saved = dao.getAllPlanes().stream()
                .filter(pl -> "Update Heli Test".equals(pl.getModel()))
                .findFirst().orElse(null);
        assertNotNull(saved);

        ((Helicopter) saved).setMaxAltitudeM(5000);
        ((Helicopter) saved).setHasHoist(true);
        dao.updatePlane(saved);

        Plane updated = dao.getAllPlanes().stream()
                .filter(pl -> "Update Heli Test".equals(pl.getModel()))
                .findFirst().orElse(null);
        assertNotNull(updated);
        assertEquals(5000, ((Helicopter) updated).getMaxAltitudeM());
        assertTrue(((Helicopter) updated).isHasHoist());

        dao.deletePlane(updated.getId());
    }
}
