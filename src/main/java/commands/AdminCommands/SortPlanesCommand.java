package commands.AdminCommands;

import commands.Command;
import main.AppContext;
import models.AirLine;
import models.CargoPlane;
import models.PassengerPlane;
import models.Plane;

import java.util.List;
import java.util.Scanner;

public class SortPlanesCommand implements Command {

    private final Scanner sc = new Scanner(System.in);

    @Override
    public void execute() {
        AirLine airline = AppContext.airline;
        System.out.println("=== СОРТУВАННЯ ТА ФІЛЬТРАЦІЯ ФЛОТУ ===");

        if (airline.getFleet().isEmpty()) {
            System.out.println("⚠️  Флот порожній.");
            return;
        }

        System.out.println("1. Сортувати за дальністю польоту (зростання)");
        System.out.println("2. Сортувати за витратою пального (зростання)");
        System.out.println("3. Фільтрувати за діапазоном витрати пального");
        System.out.println("4. Фільтрувати за мінімальною дальністю");

        int choice = getValidInt("Ваш вибір: ", 1, 4);
        List<Plane> result;

        switch (choice) {
            case 1 -> {
                result = airline.getSortedByRange();
                System.out.println("\n📋 Відсортовано за дальністю польоту:");
            }
            case 2 -> {
                result = airline.getSortedByFuelConsumption();
                System.out.println("\n📋 Відсортовано за витратою пального:");
            }
            case 3 -> {
                int min = getValidInt("Мін. витрата пального (кг/год): ", 0, 50000);
                int max = getValidInt("Макс. витрата пального (кг/год): ", min, 50000);
                result = airline.filterByFuelConsumption(min, max);
                System.out.println("\n📋 Фільтр: витрата від " + min + " до " + max + " кг/год:");
            }
            case 4 -> {
                int minRange = getValidInt("Мінімальна дальність (км): ", 0, 20000);
                result = airline.filterByMinRange(minRange);
                System.out.println("\n📋 Фільтр: дальність >= " + minRange + " км:");
            }
            default -> { return; }
        }

        if (result.isEmpty()) {
            System.out.println("  (Жодного літака не знайдено за заданим критерієм)");
        } else {
            printPlaneList(result);
        }

        AppContext.logger.info("Сортування/фільтрація флоту: вибір=" + choice +
                ", результатів=" + result.size());
    }

    @Override
    public String getName() {
        return "Сортування / Фільтрація";
    }

    private void printPlaneList(List<Plane> planes) {
        System.out.println(String.format("  %-20s %-12s %-16s %-10s %-10s",
                "Модель", "Тип", "Дальність (км)", "Пальне", "Місткість"));
        System.out.println("  " + "-".repeat(70));
        for (Plane p : planes) {
            String type = (p instanceof PassengerPlane) ? "Пасажирський" : "Вантажний";
            String capacity = (p instanceof PassengerPlane)
                    ? (int) p.getCapacity() + " місць"
                    : p.getCapacity() + " тонн";
            System.out.println(String.format("  %-20s %-12s %-16d %-10d %-10s",
                    p.getModel(), type, p.getFlightRangeKm(),
                    p.getFuelConsumption(), capacity));
        }
        System.out.println("  Знайдено: " + planes.size() + " літаків.");
    }

    private int getValidInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int v = sc.nextInt(); sc.nextLine();
                if (v >= min && v <= max) return v;
                System.out.println("⚠️  Число від " + min + " до " + max + ".");
            } else {
                System.out.println("⚠️  Введіть ціле число.");
                sc.next();
            }
        }
    }
}
