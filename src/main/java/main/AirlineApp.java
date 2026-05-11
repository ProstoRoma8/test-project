package main;

import commands.*;
import commands.AdminCommands.*;
import commands.UserCommands.*;
import java.util.Scanner;
import java.util.logging.Level;

public class AirlineApp {

    public static void main(String[] args) {
        AppContext.logger.info("=== Запуск додатка AirlineApp ===");

        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║  Система управління авіафлотом     ║");
            System.out.println("╚════════════════════════════════════╝");

            // ФІКС: безпечне зчитування ролі з валідацією
            int roleInput = 0;
            while (roleInput != 1 && roleInput != 2) {
                System.out.print("Увійдіть як (1 - Адміністратор, 2 - Користувач): ");
                if (scanner.hasNextInt()) {
                    roleInput = scanner.nextInt();
                    if (roleInput != 1 && roleInput != 2) {
                        System.out.println("⚠️  Оберіть 1 або 2.");
                    }
                } else {
                    System.out.println("⚠️  Введіть число 1 або 2.");
                    scanner.next();
                }
            }
            scanner.nextLine();

            UserRole role = (roleInput == 1) ? UserRole.ADMIN : UserRole.USER;
            AppContext.logger.info("Авторизація: обрано роль " + role);
            System.out.println("✅ Вхід як: " + (role == UserRole.ADMIN ? "Адміністратор" : "Користувач"));

            Menu menu = new Menu();

            // Спільні команди (для всіх ролей)
            menu.addCommand(1, new ViewAllPlanesCommand());
            menu.addCommand(2, new FlightCalculatorCommand());
            menu.addCommand(3, new SettingsCommand());

            // Команди тільки для адміністратора
            if (role == UserRole.ADMIN) {
                menu.addCommand(4, new AddPlaneCommand());
                menu.addCommand(5, new EditPlaneCommand());
                menu.addCommand(6, new DeletePlaneCommand());
                menu.addCommand(7, new SortPlanesCommand());
            }

            menu.addCommand(0, new ExitCommand());
            menu.handleUserInput();

        } catch (Exception e) {
            AppContext.logger.log(Level.SEVERE, "Критичний збій програми!", e);
            System.out.println("\n❌ Сталася критична помилка. Деталі збережено у файлі app.log");
        }
    }
}
