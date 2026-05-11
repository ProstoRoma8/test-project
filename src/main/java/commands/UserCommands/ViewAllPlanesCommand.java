package commands.UserCommands;

import commands.Command;
import main.AppContext;
import models.AirLine;

public class ViewAllPlanesCommand implements Command {

    @Override
    public void execute() {
        AirLine airline = AppContext.airline;

        System.out.println("=== ФЛОТ АВІАКОМПАНІЇ «" + airline.getName().toUpperCase() + "» ===");
        System.out.println();

        airline.printFleetShort();

        System.out.println();
        System.out.printf("  Всього: %d  (пасажирських: %d, вантажних: %d, гелікоптерів: %d)%n",
                airline.getFleet().size(),
                airline.getTotalPassengerPlanes(),
                airline.getTotalCargoPlanes(),
                airline.getTotalHelicopters());
        System.out.printf("  Загальна пасажиромісткість : %.0f місць%n", airline.getTotalCapacity());
        System.out.printf("  Загальна вантажопідйомність: %.1f тонн%n",  airline.getTotalPayload());

        AppContext.logger.info("Перегляд флоту: " + airline.getFleet().size() + " апаратів.");
    }

    @Override
    public String getName() {
        return "Переглянути всі літальні апарати";
    }
}
