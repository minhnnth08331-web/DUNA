package com.duna.common.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectService {
    // Kết nối DUY NHẤT tới cơ sở dữ liệu QUANLYBANHANGQUANAONAMDUNA
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QUANLYBANHANGQUANAONAMDUNA;encrypt=true;trustServerCertificate=true;sendStringParametersAsUnicode=true";
    private final String USER = "sa";
    private final String PASSWORD = "123";

    private static connectService instance;

    public connectService() {
    }

    public static synchronized connectService getInstance() {
        if (instance == null) {
            instance = new connectService();
        }
        return instance;
    }

    public static Connection getConnection() {
        return getInstance().myConnection();
    }

    public Connection myConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Connect to SQL Server failed: " + e.getMessage());
        }
        return conn;
    }
}
