package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=AirlineDB;encrypt=true;trustServerCertificate=true;";

    private static final String USER = "roma";
    private static final String PASSWORD = "76626845";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server Driver not found", e);
        }
    }
}