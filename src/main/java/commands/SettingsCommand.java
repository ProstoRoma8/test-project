package commands;
public class SettingsCommand implements Command {
    @Override
    public void execute() {
        System.out.println(">>> [Користувацькі налаштування] — функціонал ще не реалізований.");
    }

    @Override
    public String getName() {
        return "Користувацькі налаштування";
    }
}
