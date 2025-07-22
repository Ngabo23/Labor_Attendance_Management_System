/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author USER
 */
import DBConnection.DBConnection;
import java.sql.*;
import java.util.*;
import java.sql.Date;

public class ReportDAO {
    public static class ReportRow {
        public String name;
        public String position;
        public double wagePerDay;
        public int daysPresent;
        public double totalWage;

        public ReportRow(String name, String position, double wagePerDay, int daysPresent, double totalWage) {
            this.name = name;
            this.position = position;
            this.wagePerDay = wagePerDay;
            this.daysPresent = daysPresent;
            this.totalWage = totalWage;
        }
    }
    
    public static List<ReportRow> getMonthlyWageReport(int month, int year) throws SQLException {
    List<ReportRow> report = new ArrayList<>();
    String sql = """
        SELECT 
            l.name,
            l.position,
            l.wage_per_day,
            COUNT(a.id) AS days_present,
            COUNT(a.id) * l.wage_per_day AS total_wage
        FROM 
            laborers l
        JOIN 
            attendance a ON l.id = a.laborer_id
        WHERE 
            a.status = 'Present' AND MONTH(a.date) = ? AND YEAR(a.date) = ?
        GROUP BY 
            l.id, l.name, l.position, l.wage_per_day
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, month);
        stmt.setInt(2, year);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            ReportRow row = new ReportRow(
                rs.getString("name"),
                rs.getString("position"),
                rs.getDouble("wage_per_day"),
                rs.getInt("days_present"),
                rs.getDouble("total_wage")
            );
            report.add(row);
        }
    }
    return report;
}

    
    public static List<ReportRow> getWageReport(Date start, Date end) throws SQLException {
        String sql = """
            SELECT l.name, l.position, l.wage_per_day, 
                   COUNT(a.id) AS days_present
            FROM laborers l
            LEFT JOIN attendance a ON l.id = a.laborer_id 
                AND a.date BETWEEN ? AND ? 
                AND a.status = 'Present'
            GROUP BY l.id
        """;

        List<ReportRow> rows = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(start.getTime()));
            stmt.setDate(2, new java.sql.Date(end.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rows.add(new ReportRow(
                    rs.getString("name"),
                    rs.getString("position"),
                    rs.getDouble("wage_per_day"),
                    rs.getInt("days_present"),
                    rs.getDouble("totalWage")    
                ));
            }
        }
        return rows;
    }
}

