package com.pattern.food_ordering_system.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*== DP >> Singleton Pattern ==*/
public class DBConnection {
    private static Connection conn;
    private static final String URL = "jdbc:mysql://localhost:3306/food_ordering_system";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    private DBConnection() {}

    public static Connection getConnection() {
        try {
            if (conn == null|| conn.isClosed()) conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

