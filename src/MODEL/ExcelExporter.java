/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author Donovsn
 */


import DAO.ReportDAO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelExporter {

    public static void exportWageReportToExcel(List<ReportDAO.ReportRow> data, String filePath) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Wage Report");

        // === Styles ===
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

        Font totalFont = workbook.createFont();
        totalFont.setBold(true);
        CellStyle totalStyle = workbook.createCellStyle();
        totalStyle.setFont(totalFont);
        totalStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

        // === Header Row ===
        String[] columns = {"Name", "Position", "Wage/Day", "Days Present", "Total Wage"};
        Row header = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // === Data Rows ===
        double totalWagesSum = 0.0;
        int rowNum = 1;

        for (ReportDAO.ReportRow row : data) {
            Row excelRow = sheet.createRow(rowNum++);
            excelRow.createCell(0).setCellValue(row.name);
            excelRow.createCell(1).setCellValue(row.position);

            Cell wageCell = excelRow.createCell(2);
            wageCell.setCellValue(row.wagePerDay);
            wageCell.setCellStyle(currencyStyle);

            excelRow.createCell(3).setCellValue(row.daysPresent);

            Cell totalWageCell = excelRow.createCell(4);
            totalWageCell.setCellValue(row.totalWage);
            totalWageCell.setCellStyle(currencyStyle);

            totalWagesSum += row.totalWage;
        }

        // === Empty row for spacing ===
        sheet.createRow(rowNum++);

        // === Total Row ===
        Row totalRow = sheet.createRow(rowNum);
        totalRow.createCell(3).setCellValue("TOTAL");

        Cell totalCell = totalRow.createCell(4);
        totalCell.setCellValue(totalWagesSum);
        totalCell.setCellStyle(totalStyle);

        // Bold label
        CellStyle totalLabelStyle = workbook.createCellStyle();
        totalLabelStyle.setFont(totalFont);
        totalRow.getCell(3).setCellStyle(totalLabelStyle);

        // === Autosize Columns ===
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // === Write File ===
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}
