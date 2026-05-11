package main;

import commands.Command;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {

    private final Map<Integer, Command> commands = new LinkedHashMap<>();

    public void addCommand(int number, Command command) {
        commands.put(number, command);
    }

    public void display() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║       ГОЛОВНЕ МЕНЮ           ║");
        System.out.println("╠══════════════════════════════╣");
        for (Map.Entry<Integer, Command> entry : commands.entrySet()) {
            System.out.printf("║  %-2d. %-25s║%n", entry.getKey(), entry.getValue().getName());
        }
        System.out.println("╚══════════════════════════════╝");
    }

    public void handleUserInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            display();
            System.out.print("Оберіть дію: ");

            // ФІКС: захист від нечислового введення (раніше падало з InputMismatchException)
            if (!scanner.hasNextInt()) {
                System.out.println("⚠️  Введіть число з меню.");
                scanner.nextLine(); // очищаємо буфер
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // очищаємо буфер після nextInt

            Command command = commands.get(choice);
            if (command != null) {
                System.out.println();
                command.execute();
            } else {
                System.out.println("⚠️  Невірний вибір. Спробуйте ще раз.");
            }
        }
    }
}
