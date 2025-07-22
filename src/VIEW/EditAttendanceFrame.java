
package VIEW;

import DAO.AttendanceDAO;
import DAO.LaborerDAO;
import MODEL.Laborer;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import static org.apache.commons.math3.analysis.polynomials.PolynomialsUtils.shift;

public class EditAttendanceFrame extends JFrame {
    private JComboBox<Laborer> laborerComboBox;
    private JDateChooser dateChooser;
    private JComboBox<String> statusComboBox;
    private JButton loadButton, updateButton;
    private JLabel currentStatusLabel;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> shiftComboBox;


    public EditAttendanceFrame() {
        setTitle("✏️ Edit Attendance");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0 - Laborer Label + ComboBox
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Select Laborer:"), gbc);

        gbc.gridx = 1;
        laborerComboBox = new JComboBox<>();
        laborerComboBox.setPreferredSize(new Dimension(250, 28));
        formPanel.add(laborerComboBox, gbc);

        // Row 1 - Date Label + JDateChooser
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Select Date:"), gbc);

        gbc.gridx = 1;
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(250, 28));
        dateChooser.setDate(new java.util.Date());
        formPanel.add(dateChooser, gbc);

        // Row 2 - Current Status Label + Display
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Current Status:"), gbc);

        gbc.gridx = 1;
        currentStatusLabel = new JLabel("-");
        currentStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(currentStatusLabel, gbc);

        // Row 3 - New Status Label + ComboBox
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("New Status:"), gbc);

        gbc.gridx = 1;
        statusComboBox = new JComboBox<>(new String[]{"Present", "Absent"});
        statusComboBox.setPreferredSize(new Dimension(250, 28));
        formPanel.add(statusComboBox, gbc);

        // Row 4 - Buttons (Load & Update)
        gbc.gridx = 0;
        gbc.gridy = 4;
        loadButton = new JButton("Load Status");
        loadButton.setPreferredSize(new Dimension(120, 35));
        loadButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(loadButton, gbc);

        gbc.gridx = 1;
        updateButton = new JButton("Update");
        updateButton.setPreferredSize(new Dimension(120, 35));
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(updateButton, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new DefaultTableModel(new Object[]{"Laborer ID", "Name", "Date", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        attendanceTable = new JTable(tableModel);
        attendanceTable.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        attendanceTable.setRowHeight(24);
        attendanceTable.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        JScrollPane tableScroll = new JScrollPane(attendanceTable);
        tableScroll.setBorder(new EmptyBorder(10, 15, 15, 15));
        add(tableScroll, BorderLayout.CENTER);

        // Load laborers into comboBox
        loadLaborers();

        // Load all attendance on open
        loadAllAttendance();

        // Listeners
        loadButton.addActionListener(e -> loadStatus());
        updateButton.addActionListener(e -> updateAttendance());

        setVisible(true);
    }

    private void loadLaborers() {
        try {
            List<Laborer> laborers = LaborerDAO.getAllLaborers();
            laborerComboBox.removeAllItems();
            for (Laborer l : laborers) {
                laborerComboBox.addItem(l);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Failed to load laborers.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadStatus() {
        try {
            Laborer selected = (Laborer) laborerComboBox.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a laborer.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Date date = new Date(dateChooser.getDate().getTime());
            String status = AttendanceDAO.getAttendanceStatus(selected.getId(), date);
            currentStatusLabel.setText(status != null ? status : "Not Found");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading status.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateAttendance() {
    try {
        Laborer selected = (Laborer) laborerComboBox.getSelectedItem();

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a laborer.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int laborerId = selected.getId(); // ✅ Declare laborerId
        java.sql.Date date = new java.sql.Date(dateChooser.getDate().getTime());
        String shift = (String) shiftComboBox.getSelectedItem(); // ✅ Declare shift
        String newStatus = (String) statusComboBox.getSelectedItem(); // ✅ Declare status

        AttendanceDAO.updateAttendanceStatus(laborerId, date, shift, newStatus); // ✅ All variables now valid
        JOptionPane.showMessageDialog(this, "✅ Attendance updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        currentStatusLabel.setText(newStatus);
        loadAllAttendance(); // Refresh table

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "❌ Failed to update attendance.", "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}


    private void loadAllAttendance() {
        try {
            tableModel.setRowCount(0); // Clear table
            List<Object[]> records = AttendanceDAO.getAllAttendanceRecords();
            for (Object[] row : records) {
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Failed to load attendance records.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EditAttendanceFrame::new);
    }
}

    
    
    

