package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AirLine {

    private String name;
    private List<Plane> fleet;

    public AirLine(String name) {
        this.name = name;
        this.fleet = new ArrayList<>();
    }

    // --- CRUD ---

    public void addPlane(Plane plane) {
        if (plane != null) fleet.add(plane);
    }

    public boolean deletePlane(int index) {
        if (index < 0 || index >= fleet.size()) return false;
        fleet.remove(index);
        return true;
    }

    public Plane getPlaneByIndex(int index) {
        if (index < 0 || index >= fleet.size()) return null;
        return fleet.get(index);
    }

    // --- ВИВЕДЕННЯ ---

    public void printFleetShort() {
        if (fleet.isEmpty()) {
            System.out.println("  Парк порожній.");
            return;
        }
        System.out.println(String.format("  %-3s %-20s %-14s %-16s %-10s %-12s",
                "№", "Модель", "Тип", "Дальність (км)", "Пальне", "Місткість"));
        System.out.println("  " + "-".repeat(78));
        for (int i = 0; i < fleet.size(); i++) {
            Plane p = fleet.get(i);
            System.out.println(String.format("  %-3d %-20s %-14s %-16d %-10d %-12s",
                    i, p.getModel(), getTypeName(p),
                    p.getFlightRangeKm(), p.getFuelConsumption(), getCapacityLabel(p)));
        }
    }

    private String getTypeName(Plane p) {
        if (p instanceof PassengerPlane) return "Пасажирський";
        if (p instanceof CargoPlane)     return "Вантажний";
        if (p instanceof Helicopter)     return "Гелікоптер";
        return "Невідомий";
    }

    private String getCapacityLabel(Plane p) {
        if (p instanceof CargoPlane) return p.getCapacity() + " тонн";
        return (int) p.getCapacity() + " осіб";
    }

    // --- СОРТУВАННЯ ---

    public List<Plane> getSortedByRange() {
        return fleet.stream()
                .sorted(Comparator.comparingInt(Plane::getFlightRangeKm))
                .collect(Collectors.toList());
    }

    public List<Plane> getSortedByFuelConsumption() {
        return fleet.stream()
                .sorted(Comparator.comparingInt(Plane::getFuelConsumption))
                .collect(Collectors.toList());
    }

    // --- ФІЛЬТРАЦІЯ ---

    public List<Plane> filterByFuelConsumption(int min, int max) {
        return fleet.stream()
                .filter(p -> p.getFuelConsumption() >= min && p.getFuelConsumption() <= max)
                .collect(Collectors.toList());
    }

    public List<Plane> filterByMinRange(int minRange) {
        return fleet.stream()
                .filter(p -> p.getFlightRangeKm() >= minRange)
                .collect(Collectors.toList());
    }

    // --- АНАЛІТИКА ---

    public int getTotalPassengerPlanes() {
        return (int) fleet.stream().filter(p -> p instanceof PassengerPlane).count();
    }

    public int getTotalCargoPlanes() {
        return (int) fleet.stream().filter(p -> p instanceof CargoPlane).count();
    }

    public int getTotalHelicopters() {
        return (int) fleet.stream().filter(p -> p instanceof Helicopter).count();
    }

    public double getTotalCapacity() {
        return fleet.stream()
                .filter(p -> p instanceof PassengerPlane)
                .mapToDouble(Plane::getCapacity).sum();
    }

    public double getTotalPayload() {
        return fleet.stream()
                .filter(p -> p instanceof CargoPlane)
                .mapToDouble(Plane::getCapacity).sum();
    }

    public String getName() { return name; }
    public List<Plane> getFleet() { return fleet; }
}
