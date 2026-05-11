package commands.AdminCommands;

import commands.Command;
import main.AppContext;
import models.CargoPlane;
import models.Helicopter;
import models.PassengerPlane;
import models.Plane;

import java.util.Scanner;

public class EditPlaneCommand implements Command {

    private final Scanner sc = new Scanner(System.in);

    @Override
    public void execute() {
        System.out.println("=== РЕДАГУВАННЯ ЛІТАЛЬНОГО АПАРАТУ ===");

        if (AppContext.airline.getFleet().isEmpty()) {
            System.out.println("⚠️  Флот порожній.");
            return;
        }

        AppContext.airline.printFleetShort();
        int idx = getValidInt("\nНомер для редагування: ",
                0, AppContext.airline.getFleet().size() - 1);

        Plane plane = AppContext.airline.getPlaneByIndex(idx);
        System.out.println("Редагуємо: " + plane.getModel());
        System.out.println("(Enter = залишити поточне значення)");

        System.out.print("Модель [" + plane.getModel() + "]: ");
        String s = sc.nextLine().trim();
        if (!s.isEmpty()) plane.setModel(s);

        System.out.print("Витрата пального [" + plane.getFuelConsumption() + "]: ");
        s = sc.nextLine().trim();
        if (!s.isEmpty()) trySetInt(s, plane::setFuelConsumption);

        System.out.print("Дальність (км) [" + plane.getFlightRangeKm() + "]: ");
        s = sc.nextLine().trim();
        if (!s.isEmpty()) trySetInt(s, plane::setFlightRangeKm);

        // Специфічні поля за типом
        if (plane instanceof PassengerPlane pp) {
            System.out.print("Місць [" + (int) pp.getCapacity() + "]: ");
            s = sc.nextLine().trim();
            if (!s.isEmpty()) trySetDouble(s, pp::setCapacity);

        } else if (plane instanceof CargoPlane cp) {
            System.out.print("Вантажопідйомність (тонн) [" + cp.getCapacity() + "]: ");
            s = sc.nextLine().trim();
            if (!s.isEmpty()) trySetDouble(s, cp::setCapacity);

        } else if (plane instanceof Helicopter h) {
            System.out.print("Місць [" + (int) h.getCapacity() + "]: ");
            s = sc.nextLine().trim();
            if (!s.isEmpty()) trySetDouble(s, h::setCapacity);

            System.out.print("Макс. висота (м) [" + h.getMaxAltitudeM() + "]: ");
            s = sc.nextLine().trim();
            if (!s.isEmpty()) trySetInt(s, h::setMaxAltitudeM);

            System.out.print("Рятувальна лебідка (y/n) [" + (h.isHasHoist() ? "y" : "n") + "]: ");
            s = sc.nextLine().trim().toLowerCase();
            if (s.equals("y") || s.equals("так")) h.setHasHoist(true);
            else if (s.equals("n") || s.equals("ні")) h.setHasHoist(false);
        }

        System.out.println("✅ «" + plane.getModel() + "» оновлено.");
        AppContext.logger.info("Відредаговано апарат на позиції " + idx + ": " + plane.getModel());
    }

    @Override
    public String getName() { return "Редагувати апарат"; }

    // Лямбда-хелпери щоб не дублювати try-catch
    private void trySetInt(String s, java.util.function.IntConsumer setter) {
        try { setter.accept(Integer.parseInt(s)); }
        catch (NumberFormatException e) { System.out.println("⚠️  Невірний формат — пропущено."); }
    }

    private void trySetDouble(String s, java.util.function.DoubleConsumer setter) {
        try { setter.accept(Double.parseDouble(s)); }
        catch (NumberFormatException e) { System.out.println("⚠️  Невірний формат — пропущено."); }
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
