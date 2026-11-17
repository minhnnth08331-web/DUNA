package com.duna.common.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Quan ly ket noi JDBC SQL Server cho CSDL DUNA_MensWear (100 bang / 300 UC).
 */
public final class DBConnection {
    private static final String DEFAULT_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DEFAULT_URL =
            "jdbc:sqlserver://localhost:1433;databaseName=DUNA_MensWear;encrypt=true;trustServerCertificate=true;sendStringParametersAsUnicode=true;loginTimeout=2";
    private static final String DEFAULT_USER = "sa";
    private static final String DEFAULT_PASSWORD = "123";

    private static final String DRIVER;
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        try (InputStream is = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (Exception ignored) {
        }
        DRIVER = props.getProperty("db.driver", DEFAULT_DRIVER);
        URL = props.getProperty("db.url", DEFAULT_URL);
        USER = props.getProperty("db.user", DEFAULT_USER);
        PASSWORD = props.getProperty("db.password", DEFAULT_PASSWORD);
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Khong tim thay SQL Server JDBC Driver: " + DRIVER, e);
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static String getDatabaseName() {
        return "DUNA_MensWear";
    }
}
