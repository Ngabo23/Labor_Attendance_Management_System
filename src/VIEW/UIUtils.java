/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

public class UIUtils {
    public static void setModernLook() {
        try {
            FlatLightLaf.setup();  // Set FlatLaf light theme
            // Optional: Set UI defaults
            UIManager.put("Button.arc", 999);
            UIManager.put("Component.arc", 999);
        } catch (Exception e) {
            System.err.println("Failed to set FlatLaf");
            e.printStackTrace();
        }
    }
}

