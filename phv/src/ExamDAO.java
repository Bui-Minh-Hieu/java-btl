
import java.sql.*;
import java.util.ArrayList;

public class ExamDAO {
    public void addExam(Exam thi) {
        String query = "INSERT INTO Thi (exam_ID, subject_ID, exam_Date) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, thi.getExam_ID());
            statement.setString(2, thi.getSubject_ID());
            statement.setDate(3, new java.sql.Date(thi.getExam_Date().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteExam(String exam_ID) {
        String query = "DELETE FROM Thi WHERE exam_ID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, exam_ID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateExam(Exam thi) {
        String query = "UPDATE Thi SET subject_ID = ?, exam_Date = ? WHERE exam_ID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, thi.getSubject_ID());
            statement.setDate(2, new java.sql.Date(thi.getExam_Date().getTime()));
            statement.setString(3, thi.getExam_ID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
    public Exam getExam(String exam_ID) {
        Exam exam = null;
        String query = "SELECT * FROM Thi WHERE exam_ID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, exam_ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                exam = new Exam(
                        resultSet.getString("exam_ID"),
                        resultSet.getString("subject_ID"),
                        resultSet.getDate("exam_Date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exam;
    }

}