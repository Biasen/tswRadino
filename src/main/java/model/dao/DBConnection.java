package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL non trovato nel classpath", e);
        }
    }


    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/space_time";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Bj030902!";

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}

