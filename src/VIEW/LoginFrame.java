/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

/**
 *
 * @author Donovan
 */
import DAO.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Login - Labor Attendance System");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("LOGIN");
        loginButton.addActionListener(e -> handleLogin());

        panel.add(new JLabel()); // empty space
        panel.add(loginButton);

        add(panel);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (UserDAO.validateLogin(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                new DashboardFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Login error: " + e.getMessage());
        }
    }

   public static void main(String[] args) {
    // Initialize FlatLaf immediately on EDT
    SwingUtilities.invokeLater(() -> {
        UIUtils.setModernLook();
        
        // Show splash screen
        SplashScreen splash = new SplashScreen();
        splash.showSplash();
        
        // Simulate loading in background
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(1500); // Simulate loading
                return null;
            }
            
            @Override
            protected void done() {
                splash.closeSplash();
                new LoginFrame().setVisible(true);
            }
        }.execute();
    });
}

}
