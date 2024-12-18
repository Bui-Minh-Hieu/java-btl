package FRAME;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import DATA.HocPhan;
import DATABASE.DatabaseConnection;
import DATABASE.HocPhanDAO;
import DATABASE.SinhVienDAO;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class SubjectFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<HocPhan> sub_List;
    private String currentUsername;

    public SubjectFrame(String username) {
        this.currentUsername = username;
        sub_List = new ArrayList<>();
        
        setTitle("Quản lý học phần");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"STT", "ID", "Tên học phần", "Số Tín chỉ", "Giá"});
        table = new JTable(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportBorder(null);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        JPopupMenu settingMenu = new JPopupMenu();
        
        JPanel panel = new JPanel(new FlowLayout());
        panel.setPreferredSize(new Dimension(100, 100));
        getContentPane().add(panel, BorderLayout.NORTH);
        panel.setBackground(Color.WHITE);
        
        
        JButton addButton = new JButton("Thêm học phần");
        addButton.setForeground(Color.DARK_GRAY);
        addButton.setBackground(Color.CYAN);
        addButton.addActionListener(e -> showAddEditDialog(null));
        panel.add(addButton);
        
        JButton editButton = new JButton("Sửa học phần");
        editButton.setForeground(Color.DARK_GRAY);
        editButton.setBackground(Color.CYAN);
        editButton.addActionListener(e -> editNguyenVong());
        panel.add(editButton);
        
        JButton deleteButton = new JButton("Xóa học phần");
        deleteButton.setForeground(Color.DARK_GRAY);
        deleteButton.setBackground(Color.CYAN);
        deleteButton.addActionListener(e -> deleteNguyenVong());
        panel.add(deleteButton);
        
        JButton searchButton = new JButton("Tìm kiếm học phần");
        searchButton.setForeground(Color.DARK_GRAY);
        searchButton.setBackground(Color.CYAN);
        searchButton.addActionListener(e -> searchNguyenVong());
        panel.add(searchButton);
        
        JButton btnNewButton = new JButton("Quay lại");
        btnNewButton.setForeground(Color.DARK_GRAY);
        btnNewButton.setBackground(Color.CYAN);
        btnNewButton.addActionListener(e -> { new MainFrame(username).setVisible(true);	dispose();	});
        panel.add(btnNewButton);       

        loadSV();
    }

	private void loadSV() {
        HocPhanDAO hPDAO = new HocPhanDAO();
        sub_List = hPDAO.getAllHP();
        model.setRowCount(0);

        for (HocPhan hP : sub_List) {
            model.addRow(new Object[]{
            		sub_List.indexOf(hP) + 1, hP.getSubject_ID(), hP.getSubject_Name(), hP.getTin_chi(), hP.getPrice()
            });
        }
    }

    private void showAddEditDialog(HocPhan cur) {
        JTextField subject_IDField = new JTextField(20);
        JTextField subject_NameField = new JTextField(20);
        JTextField tin_chiField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        
        if (cur != null) {
        	subject_IDField.setText(cur.getSubject_ID());
        	subject_NameField.setText(cur.getSubject_Name());
        	tin_chiField.setText(String.valueOf(cur.getTin_chi()));
        	priceField.setText(String.valueOf(cur.getPrice()));
        	subject_IDField.setEditable(false);
        }

        JPanel panel = new JPanel(new GridLayout(10, 2));
        panel.add(new JLabel("ID:"));
        panel.add(subject_IDField);
        panel.add(new JLabel("Tên học phần:"));
        panel.add(subject_NameField);
        panel.add(new JLabel("Số tín chỉ:"));
        panel.add(tin_chiField);
        panel.add(new JLabel("giá:"));
        panel.add(priceField);

        int option = JOptionPane.showConfirmDialog(this, panel, cur == null ? "Thêm Nguyện Vọng" : "Sửa Nguyện Vọng", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String subject_ID = subject_IDField.getText().trim();
            String subject_Name = subject_NameField.getText().trim();
            String tin_chi = tin_chiField.getText().trim();
            String price = priceField.getText().trim();

            if (cur == null) {
                String id;
                Random random = new Random();
                do {
                    id = String.valueOf(random.nextInt(100));
                } while (idDaTonTai(id));
                int tin_chiValue = Integer.parseInt(tin_chi);
                float priceValue = Float.parseFloat(price);
                HocPhan newHP = new HocPhan(id, subject_Name, tin_chiValue ,priceValue);
                sub_List.add(newHP);
                model.addRow(newHP.toArray());
                saveDataToDatabase(newHP, true);
                JOptionPane.showMessageDialog(this, "Thêm học phần thành công!");
            }else {
            	try {
            		int tin_chiValue = Integer.parseInt(tin_chi);
                    float priceValue = Float.parseFloat(price);
            	    HocPhan newHP = new HocPhan(subject_ID, subject_Name, tin_chiValue, priceValue);
            	    int row = table.getSelectedRow();
            	    sub_List.set(row, newHP);

            	    for (int i = 0; i < model.getColumnCount(); i++) {
            	        model.setValueAt(newHP.toArray()[i], row, i);
            	    }
            	    saveDataToDatabase(newHP, false);
            	    JOptionPane.showMessageDialog(this, "Sửa học phần thành công!");
            	} catch (NumberFormatException e) {
            	    JOptionPane.showMessageDialog(this, "Số tín chỉ hoặc giá phải là số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            	}

            }
            updateSTT();
        }
    }
    private boolean idDaTonTai(String id) {
	    for (HocPhan hp : sub_List) {
	        if (hp.getSubject_ID().equals(id)) {
	            return true;
	        }
	    }
	    return false;
    }
    
    private void editNguyenVong() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            showAddEditDialog(sub_List.get(row));
        } else {
            JOptionPane.showMessageDialog(this, "Chọn một học phần để sửa!");
        }
    }

    private void deleteNguyenVong() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn học phần này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteDataFromDatabase(sub_List.get(row));
                sub_List.remove(row);
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
            model.addColumn("Tên học phần");
            model.addColumn("Số tín chỉ");
            model.addColumn("Giá");

            boolean found = false;

            for (HocPhan hp : sub_List) {
            	if (hp.getSubject_ID().equalsIgnoreCase(searchTerm)) {
            	    model.addRow(new Object[] {
            	        hp.getSubject_ID(),
            	        hp.getSubject_Name(),
            	        hp.getTin_chi(),
            	        hp.getPrice()
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

                // Điều chỉnh kích thước bảng
                table.setPreferredScrollableViewportSize(new Dimension(800, 300)); // Kích thước bảng

                JScrollPane scrollPane = new JScrollPane(table);
                JOptionPane.showMessageDialog(this, scrollPane, "Kết quả tìm kiếm", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy học phần với id: " + searchTerm);
            	}
        	}
        }

    private void saveDataToDatabase(HocPhan hp, boolean isNew) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query;
            if (isNew) {query = "INSERT INTO HocPhan (subject_ID, subject_Name, tin_chi, price) VALUES (?, ?, ?, ?)";
            }else {
                query = "UPDATE HocPhan " +
                    "SET subject_Name=?, tin_chi=?, price=?" +
                    "WHERE subject_ID=?"; 
            }

            PreparedStatement statement = connection.prepareStatement(query);
            if (isNew) {
            statement.setString(1, hp.getSubject_ID());
            statement.setString(2, hp.getSubject_Name());
            statement.setInt(3, hp.getTin_chi());
            statement.setFloat(4, hp.getPrice());
            } else {
            	
            statement.setString(4, hp.getSubject_ID());
            statement.setString(1, hp.getSubject_Name());
            statement.setInt(2, hp.getTin_chi());
            statement.setFloat(3, hp.getPrice());
        }
            statement.executeUpdate();
            loadSV();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu: " + ex.getMessage());
        }
    }

    private void deleteDataFromDatabase(HocPhan hp) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM HocPhan WHERE subject_ID = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, hp.getSubject_ID());
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