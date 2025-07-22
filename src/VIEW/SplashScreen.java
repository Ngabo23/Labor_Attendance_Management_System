/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import javax.swing.*;
import java.awt.*;

/**
 * Simple splash screen with a progress bar for your app startup.
 */
public class SplashScreen extends JWindow {

    private final JProgressBar progressBar;

    public SplashScreen() {
        // Set size and center on screen
        int width = 400;
        int height = 200;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screen.width - width) / 2, (screen.height - height) / 2, width, height);

        // Create content panel with background color
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(45, 45, 45)); // Dark background
        content.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setContentPane(content);

        // App title label
        JLabel title = new JLabel("Labor Attendance System", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        content.add(title, BorderLayout.NORTH);

        // Status label
        JLabel statusLabel = new JLabel("Starting application...", JLabel.CENTER);
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.add(statusLabel, BorderLayout.CENTER);

        // Progress bar setup
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(new Color(0, 120, 215)); // Windows blue accent
        progressBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        content.add(progressBar, BorderLayout.SOUTH);
    }

    // Show splash screen
    public void showSplash() {
        setVisible(true);
    }

    // Close splash screen
    public void closeSplash() {
        setVisible(false);
        dispose();
    }

    // Optional: You can add methods to update status or progress here if needed
}
