package commands.AdminCommands;

import commands.Command;
import main.AppContext;
import models.*;

import java.util.Scanner;

public class AddPlaneCommand implements Command {

    private final Scanner sc = new Scanner(System.in);
    private int id;

    @Override
    public void execute() {
        System.out.println("=== ДОДАВАННЯ НОВОГО ЛІТАЛЬНОГО АПАРАТУ ===");
        System.out.println("Оберіть тип:");
        System.out.println("1 - Пасажирський літак");
        System.out.println("2 - Вантажний літак");
        System.out.println("3 - Гелікоптер");

        int type = getValidInt("Ваш вибір: ", 1, 3);

        System.out.print("Введіть модель: ");
        String model = sc.nextLine();

        int fuel  = getValidInt("Витрати пального (кг/год): ", 1, 50000);
        int range = getValidInt("Дальність польоту (км): ", 10, 20000);
        double speed = getValidDouble("Крейсерська швидкість (км/год): ", 50, 3000);

        Plane plane;

        switch (type) {
            case 1 -> {
                int seats = getValidInt("Місткість (кількість пасажирів): ", 1, 853);
                boolean hasBusiness = askYesNo("Чи є бізнес-клас? (y/n): ");
                int businessSeats = 0;
                if (hasBusiness) {
                    businessSeats = getValidInt("Кількість місць бізнес-класу: ", 1, seats);
                }
                plane = new PassengerPlane(id, model, fuel, range, speed,
                        (double) seats, hasBusiness, businessSeats);
            }
            case 2 -> {
                double tons = getValidDouble("Вантажопідйомність (тонн): ", 0.1, 500);
                plane = new CargoPlane(0, model, fuel, range, speed, tons);
            }
            case 3 -> {
                int passengers = getValidInt("Місткість (осіб): ", 1, 50);
                int altitude   = getValidInt("Макс. висота польоту (м): ", 100, 9000);
                boolean hoist  = askYesNo("Чи є рятувальна лебідка? (y/n): ");
                plane = new Helicopter(id, model, fuel, range, speed,
                        passengers, altitude, hoist);
            }
            default -> { return; }
        }

        AppContext.airline.addPlane(plane);
        AppContext.logger.info("Додано: " + model + " (тип=" + type + ")");
        System.out.println("✅ «" + model + "» успішно додано до флоту!");
    }

    @Override
    public String getName() {
        return "Додати літальний апарат";
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
                System.out.println("⚠️  Введіть число (наприклад, 250.5).");
                sc.next();
            }
        }
    }

    private boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("y") || s.equals("yes") || s.equals("так") || s.equals("+")) return true;
            if (s.equals("n") || s.equals("no")  || s.equals("ні")  || s.equals("-")) return false;
            System.out.println("⚠️  Введіть 'y' або 'n'.");
        }
    }
}
