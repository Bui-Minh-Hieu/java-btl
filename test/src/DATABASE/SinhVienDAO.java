package DATABASE;

import java.sql.*;
import java.util.ArrayList;

import DATA.SinhVien;

public class SinhVienDAO {
	public ArrayList<SinhVien> getAllSV() {
        ArrayList<SinhVien> danhSach = new ArrayList<>();
        String query = "SELECT * FROM SinhVien";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                SinhVien sv = new SinhVien(
                        rs.getString("sV_ID"),
                        rs.getString("sV_Name"),           
                        rs.getString("class"),             
                        rs.getString("cCCD"),                 
                        rs.getString("email")
                );
                danhSach.add(sv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }
}
