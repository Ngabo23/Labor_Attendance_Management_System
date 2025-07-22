/*
  Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("🏠 Dashboard - Labor Attendance System");
        setSize(420, 380); // Increased height for the logo
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- Logo Panel ---
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);

        // Load the logo image
        JLabel logoLabel;
        File logoFile = new File("logo.jpg"); // <-- Change filename if needed
        if (logoFile.exists()) {
            ImageIcon logoIcon = new ImageIcon("logo.jpg");
            // Optionally resize the icon for consistent display
            Image img = logoIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(img));
        } else {
            // Fallback: Emoji if logo not found
            logoLabel = new JLabel("🛠️", JLabel.CENTER);
            logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        }
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        logoPanel.add(logoLabel);

        // --- Title ---
        JLabel title = new JLabel("Labor Attendance System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));

        // Stack logo and title together
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(logoPanel, BorderLayout.NORTH);
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // --- Buttons ---
        JButton laborerBtn = createStyledButton("👷 Manage Laborers");
        JButton attendanceBtn = createStyledButton("📅 Mark Attendance");
        JButton reportBtn = createStyledButton("📊 Generate Wage Report");

        laborerBtn.addActionListener(e -> new LaborerManagementFrame().setVisible(true));
        attendanceBtn.addActionListener(e -> new AttendanceFrame().setVisible(true));
        reportBtn.addActionListener(e -> new WageReportFrame().setVisible(true));

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));
        centerPanel.add(laborerBtn);
        centerPanel.add(attendanceBtn);
        centerPanel.add(reportBtn);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(30, 144, 255));  // Dodger Blue
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        UIUtils.setModernLook();
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}
