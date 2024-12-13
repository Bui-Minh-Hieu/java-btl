package FRAME;

import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class MainFrame extends JFrame {
    private String currentUsername;

    public MainFrame(String username) {
    	this.currentUsername = username;
        setTitle("Quản Lý Sinh Viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(430, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        JButton btnSinhVien = new JButton("Quản lý Sinh Viên");
        btnSinhVien.setBackground(Color.CYAN);
        btnSinhVien.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               SinhVienFrame svFrame = new SinhVienFrame(username);
               svFrame.setVisible(true);
               dispose();
            }
        });
        
        JButton btnThi = new JButton("Quản lý Thi");
        btnThi.setBackground(Color.CYAN);
        btnThi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               ExamFrame thiFrame = new ExamFrame(username);
               thiFrame.setVisible(true);
            	dispose();
            }
        });
        
        JButton btnHocPhan = new JButton("Quản lý Học Phần");
        btnHocPhan.setBackground(Color.CYAN);
        btnHocPhan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //HocPhanFrame hocPhanFrame = new HocPhanFrame();
                //hocPhanFrame.setVisible(true);
            	dispose();
            }
        });

        getContentPane().add(panel, BorderLayout.SOUTH);
        
        JButton btnNewButton_2 = new JButton("Thống kê");
        btnNewButton_2.setBackground(Color.CYAN);
        btnNewButton_2.setForeground(Color.BLACK);
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
                
        JButton btnDangXuat = new JButton("Thoát");
        btnDangXuat.setBackground(Color.CYAN);
        btnDangXuat.setForeground(Color.BLACK);
        btnDangXuat.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		LoginFrame lgFrame = new LoginFrame();
        		lgFrame.setVisible(true);
        		dispose();
        	}
        });
        
        
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(32)
        					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        						.addComponent(btnThi, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
        						.addComponent(btnSinhVien, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
        					.addGap(48)
        					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        						.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
        						.addComponent(btnHocPhan, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
        				.addGroup(gl_panel.createSequentialGroup()
        					.addGap(129)
        					.addComponent(btnDangXuat, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(290, Short.MAX_VALUE))
        );
        gl_panel.setVerticalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addGap(30)
        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnSinhVien, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnHocPhan, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        			.addGap(26)
        			.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnThi, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addComponent(btnDangXuat, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        			.addGap(111))
        );
        panel.setLayout(gl_panel);
    }

    private MainFrame() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));        
    }
}
