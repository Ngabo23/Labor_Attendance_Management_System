/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

/**
 *
 * @author USER
 */
import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    public enum Theme {
        LIGHT, DARK
    }

    private static Theme currentTheme = Theme.LIGHT;

    public static void applyTheme(JFrame frame) {
        Color bgColor, fgColor, btnColor;

        if (currentTheme == Theme.DARK) {
            bgColor = new Color(45, 45, 45);
            fgColor = Color.WHITE;
            btnColor = new Color(70, 130, 180);
        } else {
            bgColor = Color.WHITE;
            fgColor = Color.BLACK;
            btnColor = new Color(51, 153, 255);
        }

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        frame.getContentPane().setBackground(bgColor);
        updateComponents(frame.getContentPane(), bgColor, fgColor, font, btnColor);
    }

    private static void updateComponents(Container container, Color bg, Color fg, Font font, Color btnColor) {
        for (Component comp : container.getComponents()) {
            comp.setFont(font);
            if (comp instanceof JPanel) {
                comp.setBackground(bg);
                updateComponents((Container) comp, bg, fg, font, btnColor);
            } else if (comp instanceof JButton) {
                comp.setBackground(btnColor);
                comp.setForeground(Color.WHITE);
            } else if (comp instanceof JLabel || comp instanceof JTable || comp instanceof JTextField || comp instanceof JPasswordField) {
                comp.setForeground(fg);
                if (!(comp instanceof JTable)) {
                    comp.setBackground(bg);
                }
            }
        }
    }

    public static void toggleTheme(JFrame frame) {
        currentTheme = (currentTheme == Theme.LIGHT) ? Theme.DARK : Theme.LIGHT;
        applyTheme(frame);
        frame.repaint();
    }

    public static Theme getCurrentTheme() {
        return currentTheme;
    }
}
