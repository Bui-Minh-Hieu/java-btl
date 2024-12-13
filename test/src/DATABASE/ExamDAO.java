package DATABASE;

import java.sql.*;
import java.util.ArrayList;

import DATA.Exam;

public class ExamDAO {
    public ArrayList<Exam> getAllExams() {
        ArrayList<Exam> exams = new ArrayList<>();
        String query = "SELECT * FROM Thi";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Exam thi = new Exam(
                        resultSet.getString("exam_ID"),
                        resultSet.getString("subject_ID"),
                        resultSet.getDate("exam_Date")
                );
                exams.add(thi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }
}