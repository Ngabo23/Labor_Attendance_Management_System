/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VIEW;

/**
 *
 * @author DONOVAN
 */
import DAO.ReportDAO;
import MODEL.ExcelExporter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.List;

public class WageReportFrame extends JFrame {
    private final JComboBox<String> monthCombo;
    private final JComboBox<Integer> yearCombo;
    private final JTable table;
    private final DefaultTableModel model;
    private final JLabel totalWageLabel;

    public WageReportFrame() {
        setTitle("📊 Monthly Wage Report");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // === Table Model Setup ===
        model = new DefaultTableModel(new Object[]{"Name", "Position", "Wage/Day", "Days Present", "Total Wage"}, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // === Top Panel: Month & Year Selectors ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        String[] months = new String[12];
for (int i = 0; i < 12; i++) {
    java.time.Month month = java.time.Month.of(i + 1);
    months[i] = String.format("%02d - %s", month.getValue(), month.name());
}
monthCombo = new JComboBox<>(months);


        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearCombo = new JComboBox<>();
        for (int y = currentYear - 5; y <= currentYear + 2; y++) {
            yearCombo.addItem(y);
        }
        yearCombo.setSelectedItem(currentYear);

        JButton generateBtn = new JButton("Generate");
        generateBtn.addActionListener(e -> loadMonthlyReport());

        JButton exportBtn = new JButton("Export to Excel");
        exportBtn.addActionListener(e -> exportToExcel());

        topPanel.add(new JLabel("Select Month:"));
        topPanel.add(monthCombo);
        topPanel.add(new JLabel("Year:"));
        topPanel.add(yearCombo);
        topPanel.add(generateBtn);
        topPanel.add(exportBtn);
        add(topPanel, BorderLayout.NORTH);

        // === Bottom Panel: Total Summary ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        totalWageLabel = new JLabel("Total Wages: 0 RWF");
        totalWageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        bottomPanel.add(totalWageLabel);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadMonthlyReport() {
        model.setRowCount(0);
        int selectedMonth = monthCombo.getSelectedIndex() + 1;
        int selectedYear = (int) yearCombo.getSelectedItem();

        try {
            List<ReportDAO.ReportRow> data = ReportDAO.getMonthlyWageReport(selectedMonth, selectedYear);

            double totalWages = 0;
            for (ReportDAO.ReportRow row : data) {
                model.addRow(new Object[]{
                    row.name, row.position, row.wagePerDay,
                    row.daysPresent, row.totalWage
                });
                totalWages += row.totalWage;
            }
            totalWageLabel.setText("Total Wages: " + totalWages + " RWF");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to load report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToExcel() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Excel File");
            int choice = fileChooser.showSaveDialog(this);
            if (choice == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (!path.endsWith(".xlsx")) path += ".xlsx";

                int selectedMonth = monthCombo.getSelectedIndex() + 1;
                int selectedYear = (int) yearCombo.getSelectedItem();
                List<ReportDAO.ReportRow> data = ReportDAO.getMonthlyWageReport(selectedMonth, selectedYear);

                ExcelExporter.exportWageReportToExcel(data, path);
                JOptionPane.showMessageDialog(this, "✅ Excel report exported successfully!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to export Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        UIUtils.setModernLook(); // optional styling
        SwingUtilities.invokeLater(() -> new MonthlyReportFrame().setVisible(true));
    }
}

