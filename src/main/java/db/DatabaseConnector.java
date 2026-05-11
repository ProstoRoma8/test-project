package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    // Адреса сервера: localhost (ваш ПК), порт за замовчуванням 1433, назва бази AirlineDB
    // Параметри encrypt та trustServerCertificate потрібні для успішного підключення до локального сервера
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=AirlineDB;encrypt=true;trustServerCertificate=true;";

    // Якщо ви використовуєте Windows Authentication, ці поля можуть бути порожніми,
    // але краще створити окремого користувача в SQL Server (SQL Authentication)
    private static final String USER = "roma";
    private static final String PASSWORD = "76626845";

    public static Connection getConnection() throws SQLException {
        try {
            // Завантаження драйвера (в сучасних версіях Java це необов'язково, але корисно для перевірки)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server Driver not found", e);
        }
    }
}