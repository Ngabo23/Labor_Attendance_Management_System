/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

import DAO.LaborerDAO;
import MODEL.Laborer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class LaborerManagementFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    public LaborerManagementFrame() {
        setTitle("👷 Laborer Management");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        // Table model & JTable setup
        model = new DefaultTableModel(new Object[]{"ID", "Name", "Position", "Wage/Day","National ID"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make table read-only here
            }
        };
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 15, 10, 15));
        add(scrollPane, BorderLayout.CENTER);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBorder(new EmptyBorder(5, 15, 0, 15));
        filterPanel.add(new JLabel("Filter: "));
        JTextField filterText = new JTextField(25);
        filterPanel.add(filterText);
        add(filterPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Buttons panel
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
        addButton.setFont(btnFont);
        editButton.setFont(btnFont);
        deleteButton.setFont(btnFont);

        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(new EmptyBorder(10, 0, 15, 0));
        btnPanel.add(addButton);
        btnPanel.add(editButton);
        btnPanel.add(deleteButton);
        add(btnPanel, BorderLayout.SOUTH);

        // Load data initially
        loadLaborers();

        // Button actions
        addButton.addActionListener(e -> addLaborer());
        editButton.addActionListener(e -> editLaborer());
        deleteButton.addActionListener(e -> deleteLaborer());

        // Filter logic
        filterText.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String text = filterText.getText();
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // case-insensitive
                }
            }
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });
    }

    private void loadLaborers() {
        try {
            List<Laborer> list = LaborerDAO.getAllLaborers();
            model.setRowCount(0);
            for (Laborer l : list) {model.addRow(new Object[]{
                  l.getId(), l.getName(), l.getPosition(), l.getWagePerDay(), l.getNationalId()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addLaborer() {
        JTextField nameField = new JTextField();
        JTextField positionField = new JTextField();
        JTextField wageField = new JTextField();
        JTextField national_idField = new JTextField();
        Object[] fields = {
                "Name:", nameField,
                "Position:", positionField,
                "Wage/Day:", wageField,
                "National ID:", national_idField
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Add Laborer", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Laborer l;
                l = new Laborer(
                        nameField.getText().trim(),
                        positionField.getText().trim(),
                        Double.parseDouble(wageField.getText().trim()),
                        national_idField.getText().trim()
                );
                
                LaborerDAO.addLaborer(l);
                loadLaborers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding laborer: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editLaborer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a laborer to edit.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Convert view row to model row (for sorting/filtering)
        int modelRow = table.convertRowIndexToModel(row);
        int id = (int) model.getValueAt(modelRow, 0);

        JTextField nameField = new JTextField((String) model.getValueAt(modelRow, 1));
        JTextField positionField = new JTextField((String) model.getValueAt(modelRow, 2));
        JTextField wageField = new JTextField(String.valueOf(model.getValueAt(modelRow, 3)));
        JTextField national_idField = new JTextField((String) model.getValueAt(modelRow, 4));
        Object[] fields = {
                "Name:", nameField,
                "Position:", positionField,
                "Wage/Day:", wageField,
                "National ID:", national_idField
        };
        int result = JOptionPane.showConfirmDialog(this, fields, "Edit Laborer", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Laborer l = new Laborer(id, nameField.getText().trim(), positionField.getText().trim(),
                        Double.parseDouble(wageField.getText().trim()), national_idField.getText().trim());
                LaborerDAO.updateLaborer(l);
                loadLaborers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating laborer: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteLaborer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a laborer to delete.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int id = (int) model.getValueAt(modelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the selected laborer?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                LaborerDAO.deleteLaborer(id);
                loadLaborers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting laborer: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        UIUtils.setModernLook();
        SwingUtilities.invokeLater(() -> new LaborerManagementFrame().setVisible(true));
    }
}


