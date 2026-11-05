package connectService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectService {
    // Cấu hình kết nối cơ sở dữ liệu Microsoft SQL Server (QuanLyBanHang)
    private final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyBanHangDUNA;encrypt=true;trustServerCertificate=true;sendStringParametersAsUnicode=true";
    private final String USER = "sa";
    private final String PASSWORD = "123";

    public connectService() {
    }

    public Connection myConnection() {
        Connection conn = null;
        try {
            // Nạp JDBC Driver cho SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Connect to SQL Server QuanLyBanHang failed: " + e.getMessage());
        }
        return conn;
    }
}
