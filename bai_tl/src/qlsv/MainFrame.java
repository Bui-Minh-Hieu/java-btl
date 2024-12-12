package qlsv;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class MainFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<SinhVien> sV_List;
    private String currentUsername;

    public MainFrame(String username) {
        this.currentUsername = username;
        setTitle("Quản lý sinh viên");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        sV_List = new ArrayList<>();
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{
                "STT", "ID", "Tên", "Lớp", 
                "CCCD", "Email"
        });

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel(new FlowLayout());
        JPanel settingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPopupMenu settingMenu = new JPopupMenu();
        JMenuItem logoutItem = new JMenuItem("Đăng xuất");
        //JMenuItem showAdminItem = new JMenuItem("Xem thông tin Admin");
        
        JButton addButton = new JButton("Thêm sinh viên");
        JButton editButton = new JButton("Sửa sinh viên");
        JButton deleteButton = new JButton("Xóa sinh viên");
        JButton searchButton = new JButton("Tìm kiếm sinh viên");
        JButton settingButton = new JButton("Setting");
        
        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(searchButton);
        settingPanel.add(settingButton);
        //settingMenu.add(showAdminItem);
        settingMenu.add(logoutItem);
        JLabel titleLabel = new JLabel("Danh sách thông tin của sinh viên: ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        
        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(titleLabel, BorderLayout.CENTER);
        northPanel.add(settingPanel, BorderLayout.EAST);

        add(northPanel, BorderLayout.NORTH);
        //addPopupMenu();
        // Action listeners
        addButton.addActionListener(e -> showAddEditDialog(null));
        editButton.addActionListener(e -> editNguyenVong());
        deleteButton.addActionListener(e -> deleteNguyenVong());
        searchButton.addActionListener(e -> searchNguyenVong());
        settingButton.addActionListener(e -> {
    // Hiển thị menu setting
        settingMenu.show(settingButton, 0, settingButton.getHeight());
        });
       /*
        showAdminItem.addActionListener(e -> {
                    showAdminInfo();
        });*/
        logoutItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); // Đóng cửa sổ hiện tại
                new LoginFrame().setVisible(true); // Hiển thị lại màn hình đăng nhập
            }
        });
        
        loadDataFromDatabase();
    }

    private MainFrame() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    private void loadDataFromDatabase() {
    try (Connection connection = DatabaseConnection.getConnection()) {
        // Truy vấn kết hợp hai bảng
        String query = """
                SELECT sv.sV_ID, sv.sV_Name, sv.class, sv.cCCD, sv.email
                FROM SinhVien sv
               
                """;// JOIN thi_sinh ts ON nv.so_bao_danh = ts.so_bao_danh;

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        sV_List.clear();
        model.setRowCount(0);

        // Chuẩn bị câu lệnh UPDATE để cập nhật trạng thái
        /*
        String updateQuery = """
                UPDATE SinhVien
                SET trang_thai = ?
                WHERE id = ?;
                """;
          
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
		 */
        while (resultSet.next()) {
            String sV_ID = resultSet.getString("sV_ID");
            String sV_Name = resultSet.getString("sV_Name");
            String class_Name = resultSet.getString("class");
            String cCCD = resultSet.getString("cCCD");
            String email = resultSet.getString("email");

            // So sánh điểm thi với điểm chuẩn
            //String trangThai = diemThi >= diemChuan ? "Đỗ" : "Trượt";

            /* Cập nhật trạng thái vào cơ sở dữ liệu
            updateStatement.setString(1, trangThai);
            updateStatement.setString(2, id);
            updateStatement.executeUpdate();
			*/
            // Tạo đối tượng NguyenVong và thêm vào danh sách
            SinhVien cur = new SinhVien( sV_ID, sV_Name, class_Name,cCCD, email);
            sV_List.add(cur);

            // Thêm dữ liệu vào bảng hiển thị
            model.addRow(cur.toArray());
        }

        // Cập nhật số thứ tự
        updateSTT();

        // Đóng statement cập nhật
        // updateStatement.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Lỗi khi tải và cập nhật dữ liệu từ cơ sở dữ liệu: " + ex.getMessage());
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

        int option = JOptionPane.showConfirmDialog(this, panel, cur == null ? "Thêm Nguyện Vọng" : "Sửa Nguyện Vọng", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String sV_ID = sV_IDField.getText().trim();
            String sV_Name = sV_NameField.getText().trim();
            String class_Name = class_NameField.getText().trim();
            String cCCD = cCCDField.getText().trim();
            String email = emailField.getText().trim();

            if (cur == null) {
                String id;
                Random random = new Random();
                do {
                    id = String.valueOf(random.nextInt(100));
                } while (idDaTonTai(id));

                SinhVien newSV = new SinhVien(id, sV_Name, class_Name, cCCD, email);
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
    private boolean idDaTonTai(String id) {
	    for (SinhVien sv : sV_List) {
	        if (sv.getsV_ID().equals(id)) {
	            return true;
	        }
	    }
	    return false;
    }
    
    private void editNguyenVong() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            showAddEditDialog(sV_List.get(row));
        } else {
            JOptionPane.showMessageDialog(this, "Chọn một sinh viên để sửa!");
        }
    }

    private void deleteNguyenVong() {
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

    private void searchNguyenVong() {
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
            loadDataFromDatabase();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu: " + ex.getMessage());
        }
        
    }

    private void deleteDataFromDatabase(SinhVien sv) {
    try (Connection connection = DatabaseConnection.getConnection()) {
        String query = "DELETE FROM SinhVien WHERE sV_ID = ? ";//AND so_bao_danh = ?
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, sv.getsV_ID());
        //statement.setString(2, nv.so_bao_danh);
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
    /*
    private void addPopupMenu() {
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem viewInfoItem = new JMenuItem("Xem Thông Tin");

    viewInfoItem.addActionListener(e -> {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String soBaoDanh = (String) model.getValueAt(row, 2); // Cột "Số báo danh"
            new ThongTinSinhVien(this, soBaoDanh).setVisible(true);
        }
    });
	
    popupMenu.add(viewInfoItem);

    table.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                int row = table.rowAtPoint(e.getPoint());
                table.setRowSelectionInterval(row, row); // Chọn hàng khi nhấp chuột phải
                popupMenu.show(table, e.getX(), e.getY());
            }
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                int row = table.rowAtPoint(e.getPoint());
                table.setRowSelectionInterval(row, row);
                popupMenu.show(table, e.getX(), e.getY());
            }
        }
    });
    } 
    */
    private Admin getAdminInfo() {
    Admin admin = null;
    try (Connection connection = DatabaseConnection.getConnection()) {
        String query = "SELECT username, email, password FROM admin WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, currentUsername); // Sử dụng currentUsername
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String username = resultSet.getString("username");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            admin = new Admin(username, email, password);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin admin: " + ex.getMessage());
    }
    return admin;
}

    private void showAdminInfo() {
    Admin ad = getAdminInfo();
    if (ad != null) {
        // Tạo một hộp thoại hiển thị thông tin admin
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        panel.add(new JTextField(ad.getAdmin_ID()));
        panel.add(new JLabel("Tên:"));
        panel.add(new JTextField(ad.getAdmin_Name()));
        panel.add(new JLabel("Password:"));
        panel.add(new JTextField(ad.getPass())); // Mật khẩu không cần hiển thị

        // Không cho phép chỉnh sửa mật khẩu
        ((JTextField) panel.getComponent(5)).setEditable(false);

        JOptionPane.showMessageDialog(this, panel, "Thông tin Admin", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin admin.");
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}

