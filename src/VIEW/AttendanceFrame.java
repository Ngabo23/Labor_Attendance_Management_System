/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package VIEW;

import DAO.AttendanceDAO;
import DAO.LaborerDAO;
import MODEL.AttendanceRecord;
import MODEL.Laborer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AttendanceFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JSpinner dateSpinner;
    private JComboBox<String> shiftCombo;


    public AttendanceFrame() {
        setTitle("🗓️ Mark Attendance");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add spacing

        // === Table Setup ===
        model = new DefaultTableModel(new Object[]{"ID", "Name", "Position", "Present?"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 3) ? Boolean.class : String.class;
            }
            
 

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // === Load Data ===
        loadLaborers();

        // === Top Panel: Date and shift Selection  ===
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);
        dateSpinner.setValue(new java.util.Date()); // Default to today
        
        shiftCombo = new JComboBox<>(new String[]{"Day", "Night"}); // night shift initialition 

        JLabel dateLabel = new JLabel("📅 Select Date:");
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.add(dateLabel);
        topPanel.add(dateSpinner);
       // topPanel.add(new JLabel("Shift:"));
        topPanel.add(shiftCombo); // addition of shift combo 
        topPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        add(topPanel, BorderLayout.NORTH);
        
        topPanel.add(new JLabel("Shift:"));
        topPanel.add(shiftCombo);

        
        // === Bottom Panel: Buttons ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton submitButton = new JButton("✅ Submit Attendance");
        JButton editAttendanceButton = new JButton("✏️ Edit Attendance");
        JButton viewButton = new JButton("View Attendance");
        


        submitButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        editAttendanceButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        viewButton.setFont(new Font("SansSerif", Font.BOLD, 13));

        submitButton.setToolTipText("Save today's attendance");
        editAttendanceButton.setToolTipText("Open edit attendance window");
        viewButton.setToolTipText(" view who was present or absent on a specific date");

        submitButton.setPreferredSize(new Dimension(180, 35));
        editAttendanceButton.setPreferredSize(new Dimension(180, 35));
        viewButton.setPreferredSize(new Dimension(180, 35));

        submitButton.addActionListener(e -> submitAttendance());
        editAttendanceButton.addActionListener(e -> new EditAttendanceFrame());
        viewButton.addActionListener(e -> viewAttendance());


        buttonPanel.add(submitButton);
        buttonPanel.add(editAttendanceButton);
        add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(viewButton);
    }

    private void loadLaborers() {
        try {
            List<Laborer> laborers = LaborerDAO.getAllLaborers();
            model.setRowCount(0);
            for (Laborer l : laborers) {
                model.addRow(new Object[]{l.getId(), l.getName(), l.getPosition(), true});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "⚠️ Failed to load laborers:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void submitAttendance() {
        java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
        java.sql.Date selectedDate = new java.sql.Date(utilDate.getTime());
        String shift = (String) shiftCombo.getSelectedItem(); //  Get selected shift

        try {
            if (AttendanceDAO.isAlreadyMarked(selectedDate, shift)) {
                JOptionPane.showMessageDialog(this, "⚠️ Attendance already marked for this date!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Map<Integer, String> attendanceMap = new HashMap<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                int id = (int) model.getValueAt(i, 0);
                boolean present = (boolean) model.getValueAt(i, 3);
                attendanceMap.put(id, present ? "Present" : "Absent");
            }

            AttendanceDAO.saveAttendance(attendanceMap, selectedDate, shift); // include shift in DAO
            JOptionPane.showMessageDialog(this, "✅ Attendance saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Error saving attendance:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
     
private void viewAttendance() {
    try {
        java.util.Date utilDate = (java.util.Date) dateSpinner.getValue();
        java.sql.Date selectedDate = new java.sql.Date(utilDate.getTime());
        String selectedShift = shiftCombo.getSelectedItem().toString();


        List<AttendanceRecord> records = AttendanceDAO.getAttendanceByDateAndShift(selectedDate, selectedShift);

        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No attendance records found for the selected date and shift.");
        } else {
            StringBuilder sb = new StringBuilder("📅 Attendance for " + selectedDate + " (" + selectedShift + "):\n\n");
            for (AttendanceRecord record : records) {
                sb.append("👷 ").append(record.getName())
                  .append(" - ")
                  .append(record.getStatus())
                  .append("\n");
            }

            // Show attendance in a scrollable text area
            JTextArea textArea = new JTextArea(sb.toString(), 15, 40);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JOptionPane.showMessageDialog(this, scrollPane, "Attendance Results", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "❌ Error retrieving attendance: " + ex.getMessage());
        ex.printStackTrace();
    }
}




    public static void main(String[] args) {
        UIUtils.setModernLook(); // Optional: set your own look & feel
        SwingUtilities.invokeLater(() -> new AttendanceFrame().setVisible(true));
    }
}
