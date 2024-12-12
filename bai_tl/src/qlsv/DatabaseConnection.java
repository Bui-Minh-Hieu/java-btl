package qlsv;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	
	private static final String URL = "jdbc:sqlserver://SONICT\\MSSQLSERVER1:1433;databaseName=qlsv;encrypt=true;trustServerCertificate=true";
	private static final String USER = "sa";
	private static final String PASSWORD = "123"; 
    public static Connection getConnection() {
        try {
            // Kiểm tra driver của SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Trả về null nếu không kết nối được
        }
    }
}

