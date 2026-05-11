package commands.UserCommands;

import commands.Command;
import main.AppContext;
import models.AirLine;
import models.Plane;

import java.util.List;
import java.util.Scanner;

public class FlightCalculatorCommand implements Command {

    private final Scanner sc = new Scanner(System.in);

    @Override
    public void execute() {
        AirLine airline = AppContext.airline;

        System.out.println("=== ✈️  АВІАЦІЙНИЙ КАЛЬКУЛЯТОР РЕЙСУ ===");

        List<Plane> fleet = airline.getFleet();
        if (fleet.isEmpty()) {
            System.out.println("⚠️  Флот порожній. Спочатку додайте літаки.");
            return;
        }

        // 1. Вибір літака
        System.out.println("\nОберіть літак для розрахунку:");
        airline.printFleetShort();
        int idx = getValidInt("\nНомер літака: ", 0, fleet.size() - 1);
        Plane plane = fleet.get(idx);

        System.out.println("\nОбрано: " + plane.getModel());
        System.out.println("  Витрата пального : " + plane.getFuelConsumption() + " кг/год");
        System.out.println("  Крейсерська швид.: " + plane.getCruiseSpeedKmh() + " км/год");
        System.out.println("  Макс. дальність  : " + plane.getFlightRangeKm() + " км");

        // 2. Введення дистанції
        int distance = getValidInt("\nВведіть дистанцію рейсу (км): ", 1, 20000);

        // 3. Розрахунок
        // Час польоту в годинах
        double flightHours = (double) distance / plane.getCruiseSpeedKmh();
        // Витрата пального: F = час_польоту × витрата_кг/год
        double fuelNeeded = flightHours * plane.getFuelConsumption();

        System.out.println("\n─────────────────────────────────────");
        System.out.printf("  Час польоту     : %.2f год (≈ %d хв)%n",
                flightHours, (int)(flightHours * 60));
        System.out.printf("  Пального потрібно: %.1f кг%n", fuelNeeded);

        // 4. Перевірка технічної можливості
        // (дальність літака вже задана — якщо дистанція > maxRange, блокуємо)
        if (distance > plane.getFlightRangeKm()) {
            System.out.println("\n🚫 РЕЙС НЕМОЖЛИВИЙ!");
            System.out.printf("   Дистанція %d км перевищує максимальну дальність літака %d км.%n",
                    distance, plane.getFlightRangeKm());
            AppContext.logger.warning("Калькулятор: рейс заблоковано — дистанція перевищує дальність (" +
                    plane.getModel() + ", " + distance + " км)");
            return;
        }

        System.out.println("  ✅ Технічно можливий (дальність у межах норми)");

        // 5. Економічний розрахунок
        System.out.print("\nВведіть ціну пального (USD за кг, або 0 для пропуску): ");
        double pricePerKg = getValidDouble("Ціна (USD/кг): ", 0, 10);
        if (pricePerKg > 0) {
            double totalCost = fuelNeeded * pricePerKg;
            System.out.printf("  💰 Вартість пального: $%.2f%n", totalCost);
        }

        System.out.println("─────────────────────────────────────");
        AppContext.logger.info("Калькулятор: рейс розраховано — " + plane.getModel() +
                ", дистанція=" + distance + " км, пальне=" + String.format("%.1f", fuelNeeded) + " кг");
    }

    @Override
    public String getName() {
        return "Калькулятор рейсу";
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

    private double getValidDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                double v = sc.nextDouble(); sc.nextLine();
                if (v >= min && v <= max) return v;
                System.out.println("⚠️  Число від " + min + " до " + max + ".");
            } else {
                System.out.println("⚠️  Введіть число.");
                sc.next();
            }
        }
    }
}
