package main;

import models.AirLine;
import java.io.IOException;
import java.util.logging.*;

public class AppContext {

    // 1. Створюємо глобальний об'єкт логера
    public static final Logger logger = Logger.getLogger("AirLineApp");

    public static final AirLine airline = new AirLine("My Airline");

    // 2. Статичний блок ініціалізації запускається один раз при старті програми
    static {
        try {
            // Налаштовуємо запис у файл "app.log"
            // true означає, що нові логи додаватимуться в кінець файлу (append), а не перезаписуватимуть його
            FileHandler fileHandler = new FileHandler("app.log", true);

            // Встановлюємо простий текстовий формат (замість XML)
            fileHandler.setFormatter(new SimpleFormatter());

            // Додаємо цей "обробник файлів" до нашого логера
            logger.addHandler(fileHandler);

            // Рівень логування: записувати все (INFO, WARNING, SEVERE тощо)
            logger.setLevel(Level.ALL);

            // Вимикаємо вивід логів у консоль (щоб не засмічувати меню), якщо хочеш тільки файл
            logger.setUseParentHandlers(false);

            logger.info("=== Запуск програми ===");

        } catch (IOException e) {
            System.err.println("Не вдалося налаштувати логування: " + e.getMessage());
        }
    }
}