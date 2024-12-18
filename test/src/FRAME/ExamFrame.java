package FRAME;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import DATA.Exam;
import DATABASE.DatabaseConnection;
import DATABASE.ExamDAO;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ExamFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<Exam> thi_List;
    private String currentUsername;

    public ExamFrame(String username) {
        this.currentUsername = username;
        thi_List = new ArrayList<>();
        
        setTitle("Quản lý lịch thi ");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{	"STT", "Mã Kì Thi", "Mã Học Phần", "Ngày Thi"	});
        table = new JTable(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportBorder(null);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        JPopupMenu settingMenu = new JPopupMenu();
        
        JPanel panel = new JPanel(new FlowLayout());
        panel.setPreferredSize(new Dimension(100, 100));
        getContentPane().add(panel, BorderLayout.NORTH);
        panel.setBackground(Color.WHITE);
        
        JButton addButton = new JButton("Thêm Lịch Thi");
        addButton.setForeground(Color.DARK_GRAY);
        addButton.setBackground(Color.CYAN);
        addButton.addActionListener(e -> showAddEditDialog(null));
        panel.add(addButton);
        
        JButton editButton = new JButton("Sửa Lịch Thi");
        editButton.setForeground(Color.DARK_GRAY);
        editButton.setBackground(Color.CYAN);
        editButton.addActionListener(e -> editExam());
        panel.add(editButton);
        
        JButton deleteButton = new JButton("Xóa Lịch Thi");
        deleteButton.setForeground(Color.DARK_GRAY);
        deleteButton.setBackground(Color.CYAN);
        deleteButton.addActionListener(e -> deleteExam());
        panel.add(deleteButton);
        
        JButton searchButton = new JButton("Tìm kiếm Lịch Thi");
        searchButton.setForeground(Color.DARK_GRAY);
        searchButton.setBackground(Color.CYAN);
        searchButton.addActionListener(e -> searchExam());
        panel.add(searchButton);
        
        JButton btnNewButton = new JButton("Quay lại");
        btnNewButton.setForeground(Color.DARK_GRAY);
        btnNewButton.setBackground(Color.CYAN);
        btnNewButton.addActionListener(e -> { new MainFrame(username).setVisible(true);	dispose();	});
        panel.add(btnNewButton);       
        panel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{addButton, editButton, deleteButton, searchButton, btnNewButton}));

        loadExams();
    }

    private void loadDataFromDatabase() {
    try (Connection connection = DatabaseConnection.getConnection()) {
        String query = " SELECT Thi.exam_ID, Thi.subject_ID, Thi.exam_Date From Thi;";

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        thi_List.clear();
        model.setRowCount(0);

        while (resultSet.next()) {
            String exam_ID = resultSet.getString("exam_ID");
            String subject_ID = resultSet.getString("subject_ID");
            Date exam_Date = resultSet.getDate("exam_Date");
            Exam cur = new Exam( exam_ID, subject_ID,exam_Date);
            thi_List.add(cur);

            // Thêm dữ liệu vào bảng hiển thị
            model.addRow(cur.toArray());
        }

        // Cập nhật số thứ tự
        updateSTT();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Lỗi khi tải và cập nhật dữ liệu từ cơ sở dữ liệu: " + ex.getMessage());
    }
}
    private void loadExams() {
        ExamDAO thiDAO = new ExamDAO();
        thi_List = thiDAO.getAllExams();
        model.setRowCount(0);
        for (Exam thi : thi_List) {
            model.addRow(new Object[]{
                thi_List.indexOf(thi) + 1, thi.getExam_ID(), thi.getSubject_ID(), thi.getExam_Date()
            });
        }
    }
    private void showAddEditDialog(Exam cur) {
        JTextField exam_IDField = new JTextField(20);
        JTextField subject_IDField = new JTextField(20);
        JTextField exam_DateField = new JTextField(20);
        
        if (cur != null) {
        	exam_IDField.setText(cur.getExam_ID());
        	subject_IDField.setText(cur.getSubject_ID());
        	exam_DateField.setText(cur.getExam_Date().toString());
        	exam_IDField.setEditable(false);
        	subject_IDField.setEditable(false);
        }

        JPanel panel = new JPanel(new GridLayout(10, 2));
        panel.add(new JLabel("Mã Thi:"));
        panel.add(exam_IDField);
        panel.add(new JLabel("Mã Học Phần:"));
        panel.add(subject_IDField);
        panel.add(new JLabel("Ngày Thi:"));
        panel.add(exam_DateField);

        int option = JOptionPane.showConfirmDialog(this, panel, cur == null ? "Thêm Lich Thi" : "Sửa Lich Thi", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String exam_ID = exam_IDField.getText().trim();
            String subject_ID = subject_IDField.getText().trim();
            Date exam_Date;

            try {
            	exam_Date = Date.valueOf(exam_DateField.getText().trim());
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Ngày thi không hợp lệ");
                return;
            }

            if (cur == null) {
                Exam newEx = new Exam(exam_ID, subject_ID, exam_Date);
                thi_List.add(newEx);
                model.addRow(newEx.toArray());
                saveDataToDatabase(newEx, true);
                JOptionPane.showMessageDialog(this, "Thêm kì thi thành công!");
            }else {
                Exam newEx = new Exam(exam_ID, subject_ID, exam_Date);
                int row = table.getSelectedRow();
                thi_List.set(row, newEx);
                model.setValueAt(newEx.toArray()[2], row, 2);
               
                saveDataToDatabase(newEx, false);
                JOptionPane.showMessageDialog(this, "Sửa kì thi thành công!"); 
            }
            updateSTT();

        }
    }
    
    private void editExam() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            showAddEditDialog(thi_List.get(row));
        } else {
            JOptionPane.showMessageDialog(this, "Chọn một kì thi để sửa!");
        }
    }

    private void deleteExam() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn kì thi này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteDataFromDatabase(thi_List.get(row));
                thi_List.remove(row);
                model.removeRow(row);
                updateSTT();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chọn kì thi để xóa!");
        }
    }

    private void searchExam() {
    String searchTerm = JOptionPane.showInputDialog("Nhập id kì thi cần tìm:");
    if (searchTerm != null) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Mã Kì Thi");
        model.addColumn("Mã Học Phần");
        model.addColumn("Ngày Thi");

        boolean found = false;

        for (Exam ex : thi_List) {
            if (ex.getExam_ID().equalsIgnoreCase(searchTerm)) {
                model.addRow(new Object[] {
                	ex.getExam_ID(),
                	ex.getSubject_ID(),
                	ex.getExam_Date(),
                });
                found = true;
            }
        }

        if (found) {
            JTable table = new JTable(model);
            // Điều chỉnh chiều cao của các hàng
            table.setRowHeight(30); // Chiều cao mỗi hàng (mặc định là 16)
            
            // Điều chỉnh chiều rộng của các cột
            table.getColumnModel().getColumn(0).setPreferredWidth(100); 
            table.getColumnModel().getColumn(1).setPreferredWidth(150); 
            table.getColumnModel().getColumn(2).setPreferredWidth(150); 

            // Điều chỉnh kích thước bảng
            table.setPreferredScrollableViewportSize(new Dimension(800, 300)); // Kích thước bảng

            JScrollPane scrollPane = new JScrollPane(table);
            JOptionPane.showMessageDialog(this, scrollPane, "Kết quả tìm kiếm", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy kì thi với id: " + searchTerm);
        }
    }
}

    private void saveDataToDatabase(Exam ex, boolean isNew) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query;
            if (isNew) {query = "INSERT INTO Thi (exam_ID, subject_ID, exam_Date) VALUES (?, ?, ?);";
            }else {
                query = "UPDATE Thi SET exam_Date= ?  WHERE exam_ID= ? AND subject_ID = ?;";
            }

            PreparedStatement statement = connection.prepareStatement(query);
            if (isNew) {
            statement.setString(1, ex.getExam_ID());
            statement.setString(2, ex.getSubject_ID());
            statement.setDate(3, (Date) ex.getExam_Date());
            } else {  	
            statement.setString(2, ex.getExam_ID());
            statement.setString(3, ex.getSubject_ID());
            statement.setDate(1, (Date) ex.getExam_Date());
        }
            statement.executeUpdate();
            loadExams();
        } catch (SQLException ex1) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu: " + ex1.getMessage());
        }
        
    }

    private void deleteDataFromDatabase(Exam sv) {
    try (Connection connection = DatabaseConnection.getConnection()) {
        String query = "DELETE  FROM Thi WHERE Thi.exam_ID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, sv.getExam_ID());
        statement.executeUpdate();
        JOptionPane.showMessageDialog(this, "Xóa thành công!");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Lỗi khi xóa dữ liệu: " + ex.getMessage());
    }
    }
    
    private void updateSTT() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0);
        }
    }
}


