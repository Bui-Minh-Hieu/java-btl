package qlsv;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class ExamFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<Thi> thi_List; // Danh sách kỳ thi
    private String currentUsername;

    public ExamFrame(String username) {
        this.currentUsername = username;
        setTitle("Quản lý kỳ thi");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        thi_List = new ArrayList<>();
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"STT", "Exam ID", "Subject ID", "Exam Date"});

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel(new FlowLayout());
        JButton addExamButton = new JButton("Thêm kỳ thi");
        JButton editExamButton = new JButton("Sửa kỳ thi");
        JButton deleteExamButton = new JButton("Xóa kỳ thi");

        panel.add(addExamButton);
        panel.add(editExamButton);
        panel.add(deleteExamButton);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // Action listeners
        addExamButton.addActionListener(e -> showAddEditExamDialog(null));
        editExamButton.addActionListener(e -> editExam());
        deleteExamButton.addActionListener(e -> deleteExam());

        loadExams();
    }

    private void loadExams() {
        ThiDAO thiDAO = new ThiDAO();
        thi_List = thiDAO.getAllExams();
        model.setRowCount(0);

        for (Thi thi : thi_List) {
            model.addRow(new Object[]{
                thi_List.indexOf(thi) + 1, thi.getExam_ID(), thi.getSubject_ID(), thi.getExam_Date()
            });
        }
    }

    private void showAddEditExamDialog(Thi cur) {
        JTextField examIDField = new JTextField(20);
        JTextField subjectIDField = new JTextField(20);
        JTextField examDateField = new JTextField(20); // Bạn có thể sử dụng JDatePicker cho ngày

        if (cur != null) {
            examIDField.setText(cur.getExam_ID());
            subjectIDField.setText(cur.getSubject_ID());
            examDateField.setText(cur.getExam_Date().toString()); // Chuyển đổi thành chuỗi để hiển thị
        }

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Exam ID:"));
        panel.add(examIDField);
        panel.add(new JLabel("Subject ID:"));
        panel.add(subjectIDField);
        panel.add(new JLabel("Exam Date:"));
        panel.add(examDateField);

        int result = JOptionPane.showConfirmDialog(this, panel, cur == null ? "Thêm kỳ thi" : "Sửa kỳ thi", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String examID = examIDField.getText().trim();
            String subjectID = subjectIDField.getText().trim();
            Date examDate = null;

            try {
                examDate = Date.valueOf(examDateField.getText().trim());
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Ngày thi không hợp lệ");
                return;
            }

            Thi thi = new Thi(examID, subjectID, examDate);
            ThiDAO thiDAO = new ThiDAO();

            if (cur == null) {
                thiDAO.addExam(thi);
            } else {
                thiDAO.updateExam(thi);
            }

            loadExams();
        }
    }

    private void editExam() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String examID = (String) table.getValueAt(selectedRow, 1);
            ThiDAO thiDAO = new ThiDAO();
            Thi exam = thiDAO.getExam(examID);
            showAddEditExamDialog(exam);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kỳ thi cần sửa");
        }
    }

    private void deleteExam() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String examID = (String) table.getValueAt(selectedRow, 1);
            ThiDAO thiDAO = new ThiDAO();
            thiDAO.deleteExam(examID);
            loadExams();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kỳ thi cần xóa");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExamFrame frame = new ExamFrame("username");
            frame.setVisible(true);
        });
    }
}
