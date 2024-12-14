package qlhp;

import java.sql.*;
import java.util.ArrayList;

public class HocPhanDAO {
    public ArrayList<HocPhan> getAllHP() {
        ArrayList<HocPhan> danhSach = new ArrayList<>();
        String query = "SELECT * FROM HocPhan";  // Correct table name

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                HocPhan hp = new HocPhan(
                        rs.getString("subject_ID"),
                        rs.getString("subject_Name"),
                        rs.getInt("tin_chi"),
                        rs.getFloat("price")
                );
                danhSach.add(hp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }
}
