package FRAME;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import DATA.SinhVien;
import DATABASE.DatabaseConnection;
import DATABASE.SinhVienDAO;
import org.eclipse.wb.swing.FocusTraversalOnArray;

public class SinhVienFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<SinhVien> sV_List;
    private String currentUsername;

    public SinhVienFrame(String username) {
        this.currentUsername = username;
        sV_List = new ArrayList<>();
        
        setTitle("Quản lý sinh viên");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{	"STT", "ID", "Tên", "Lớp", "CCCD", "Email"	});
        table = new JTable(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportBorder(null);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        JPopupMenu settingMenu = new JPopupMenu();
        
        JPanel panel = new JPanel(new FlowLayout());
        panel.setPreferredSize(new Dimension(100, 100));
        getContentPane().add(panel, BorderLayout.NORTH);
        panel.setBackground(Color.WHITE);
        
        
        JButton addButton = new JButton("Thêm sinh viên");
        addButton.setForeground(Color.DARK_GRAY);
        addButton.setBackground(Color.CYAN);
        addButton.addActionListener(e -> showAddEditDialog(null));
        panel.add(addButton);
        
        JButton editButton = new JButton("Sửa sinh viên");
        editButton.setForeground(Color.DARK_GRAY);
        editButton.setBackground(Color.CYAN);
        editButton.addActionListener(e -> editSinhVien());
        panel.add(editButton);
        
        JButton deleteButton = new JButton("Xóa sinh viên");
        deleteButton.setForeground(Color.DARK_GRAY);
        deleteButton.setBackground(Color.CYAN);
        deleteButton.addActionListener(e -> deleteSinhVien());
        panel.add(deleteButton);
        
        JButton searchButton = new JButton("Tìm kiếm sinh viên");
        searchButton.setForeground(Color.DARK_GRAY);
        searchButton.setBackground(Color.CYAN);
        searchButton.addActionListener(e -> searchSinhVien());
        panel.add(searchButton);
        
        JButton btnNewButton = new JButton("Quay lại");
        btnNewButton.setForeground(Color.DARK_GRAY);
        btnNewButton.setBackground(Color.CYAN);
        btnNewButton.addActionListener(e -> { new MainFrame(username).setVisible(true);	dispose();	});
        panel.add(btnNewButton);       

        loadSV();
    }
    private void loadSV() {
        SinhVienDAO thiDAO = new SinhVienDAO();
        sV_List = thiDAO.getAllSV();
        model.setRowCount(0);

        for (SinhVien sV : sV_List) {
            model.addRow(new Object[]{
                sV_List.indexOf(sV) + 1, sV.getsV_ID(), sV.getsV_Name(), sV.getClass_Name(), sV.getcCCD(), sV.getEmail()
            });
        }
    }
    
    private void showAddEditDialog(SinhVien cur) {
        JTextField sV_IDField = new JTextField(20);
        JTextField sV_NameField = new JTextField(20);
        JTextField class_NameField = new JTextField(20);
        JTextField cCCDField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        
        if (cur != null) {
        	sV_IDField.setText(cur.getsV_ID());
        	sV_NameField.setText(cur.getsV_Name());
        	class_NameField.setText(cur.getClass_Name());
        	cCCDField.setText(cur.getcCCD());
        	emailField.setText(cur.getEmail());
        	sV_IDField.setEditable(false);
        }

        JPanel panel = new JPanel(new GridLayout(10, 2));
        panel.add(new JLabel("ID:"));
        panel.add(sV_IDField);
        panel.add(new JLabel("Tên sinh viên:"));
        panel.add(sV_NameField);
        panel.add(new JLabel("Lớp:"));
        panel.add(class_NameField);
        panel.add(new JLabel("Căn cước công dân:"));
        panel.add(cCCDField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int option = JOptionPane.showConfirmDialog(this, panel, cur == null ? "Thêm Sinh Viên" : "Sửa Sinh Viên", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String sV_ID = sV_IDField.getText().trim();
            String sV_Name = sV_NameField.getText().trim();
            String class_Name = class_NameField.getText().trim();
            String cCCD = cCCDField.getText().trim();
            String email = emailField.getText().trim();

            if (cur == null) {
                SinhVien newSV = new SinhVien(sV_ID, sV_Name, class_Name, cCCD, email);
                sV_List.add(newSV);
                model.addRow(newSV.toArray());
                saveDataToDatabase(newSV, true);
                JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công!");
            }else {
                SinhVien newSV = new SinhVien(sV_ID, sV_Name, class_Name, cCCD, email);
                int row = table.getSelectedRow();
                sV_List.set(row, newSV);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    model.setValueAt(newSV.toArray()[i], row, i);
                }
                saveDataToDatabase(newSV, false);
                JOptionPane.showMessageDialog(this, "Sửa sinh viên thành công!"); 
            }
            updateSTT();
        }
    }
    
    private void editSinhVien() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            showAddEditDialog(sV_List.get(row));
        } else {
            JOptionPane.showMessageDialog(this, "Chọn một sinh viên để sửa!");
        }
    }

    private void deleteSinhVien() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn sinh viên này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteDataFromDatabase(sV_List.get(row));
                sV_List.remove(row);
                model.removeRow(row);
                updateSTT();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chọn một nguyện vọng để xóa!");
        }
    }

    private void searchSinhVien() {
    String searchTerm = JOptionPane.showInputDialog("Nhập id cần tìm:");
    if (searchTerm != null) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Tên sinh viên");
        model.addColumn("Tên lớp");
        model.addColumn("CCCD");
        model.addColumn("Email");

        boolean found = false;

        for (SinhVien sv : sV_List) {
            if (sv.getsV_ID().equalsIgnoreCase(searchTerm)) {
                model.addRow(new Object[] {
                	sv.getsV_ID(),
                	sv.getsV_Name(),
                	sv.getClass_Name(),
                	sv.getcCCD(),
                	sv.getEmail()
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
            table.getColumnModel().getColumn(3).setPreferredWidth(100); 
            table.getColumnModel().getColumn(4).setPreferredWidth(100);  

            // Điều chỉnh kích thước bảng
            table.setPreferredScrollableViewportSize(new Dimension(800, 300)); // Kích thước bảng

            JScrollPane scrollPane = new JScrollPane(table);
            JOptionPane.showMessageDialog(this, scrollPane, "Kết quả tìm kiếm", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên với id: " + searchTerm);
        }
    }
}

    private void saveDataToDatabase(SinhVien sv, boolean isNew) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query;
            if (isNew) {query = "INSERT INTO SinhVien (sV_ID,sV_Name,class,cCCD,email) VALUES (?,?, ?, ?, ?)";
            }else {
                query = "UPDATE SinhVien " +
                    "SET sv_Name=?, class=?, cCCD=?, email=?" +
                    "WHERE sV_ID=?"; //AND so_bao_danh=?
            }

            PreparedStatement statement = connection.prepareStatement(query);
            if (isNew) {
            statement.setString(1, sv.getsV_ID());
            statement.setString(2, sv.getsV_Name());
            statement.setString(3, sv.getClass_Name());
            statement.setString(4, sv.getcCCD());
            statement.setString(5, sv.getEmail());
            } else {
            	
            statement.setString(5, sv.getsV_ID());
            statement.setString(1, sv.getsV_Name());
            statement.setString(2, sv.getClass_Name());
            statement.setString(3, sv.getcCCD());
            statement.setString(4, sv.getEmail());
        }
            statement.executeUpdate();
            loadSV();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu: " + ex.getMessage());
        }
        
    }

    private void deleteDataFromDatabase(SinhVien sv) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Kiểm tra giá trị trước khi xóa
            if (sv.getsV_ID() == null || sv.getsV_ID().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID sinh viên không hợp lệ");
                return;
            }

            // Xóa từ SinhVien_HocPhan
            try (PreparedStatement statement1 = connection.prepareStatement("DELETE FROM SinhVien_HocPhan WHERE sV_ID = ?")) {
                statement1.setString(1, sv.getsV_ID());
                int rowsAffected1 = statement1.executeUpdate(); 
            }

            // Xóa từ SinhVien
            try (PreparedStatement statement2 = connection.prepareStatement("DELETE FROM SinhVien WHERE sV_ID = ?")) {
                statement2.setString(1, sv.getsV_ID());
                int rowsAffected2 = statement2.executeUpdate();

                if (rowsAffected2 > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên cần xóa hoặc xảy ra lỗi.");
                }
            }
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

