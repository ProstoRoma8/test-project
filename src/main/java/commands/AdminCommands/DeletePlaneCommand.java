package commands.AdminCommands;

import commands.Command;
import main.AppContext;
import models.AirLine;
import models.Plane;

import java.util.Scanner;

public class DeletePlaneCommand implements Command {

    private final Scanner sc = new Scanner(System.in);

    @Override
    public void execute() {
        AirLine airline = AppContext.airline;

        System.out.println("=== ВИДАЛЕННЯ ЛІТАКА ===");

        if (airline.getFleet().isEmpty()) {
            System.out.println("⚠️  Флот порожній — нічого видаляти.");
            return;
        }

        airline.printFleetShort();

        int idx = getValidInt("\nВведіть номер літака для видалення: ",
                0, airline.getFleet().size() - 1);

        Plane plane = airline.getPlaneByIndex(idx);
        System.out.print("❓ Видалити «" + plane.getModel() + "»? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("так")) {
            airline.deletePlane(idx);
            System.out.println("✅ Літак «" + plane.getModel() + "» видалено.");
            AppContext.logger.info("Видалено літак: " + plane.getModel());
        } else {
            System.out.println("❌ Видалення скасовано.");
        }
    }

    @Override
    public String getName() {
        return "Видалити літак";
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
