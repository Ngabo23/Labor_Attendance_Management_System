/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBConnection.DBConnection;
import MODEL.AttendanceRecord;
import MODEL.Laborer;

import java.sql.*;
import java.util.*;

public class AttendanceDAO {

    // Save attendance for the given date and shift
    public static void saveAttendance(Map<Integer, String> attendanceMap, java.sql.Date date, String shift) throws SQLException {
        String sql = "INSERT INTO attendance (laborer_id, date, status, shift) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Map.Entry<Integer, String> entry : attendanceMap.entrySet()) {
                stmt.setInt(1, entry.getKey());
                stmt.setDate(2, date);
                stmt.setString(3, entry.getValue());
                stmt.setString(4, shift);
                stmt.addBatch();
            }

            stmt.executeBatch();
        }
    }

    // Check if attendance already marked for this date and shift
    public static boolean isAlreadyMarked(java.sql.Date date, String shift) throws SQLException {
        String sql = "SELECT COUNT(*) FROM attendance WHERE date = ? AND shift = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, date);
            stmt.setString(2, shift);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }

        return false;
    }

    // ✅ Updated to include shift in the WHERE clause
    public static void updateAttendanceStatus(int laborerId, java.sql.Date date, String shift, String status) throws SQLException {
        String sql = "UPDATE attendance SET status = ? WHERE laborer_id = ? AND date = ? AND shift = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, laborerId);
            stmt.setDate(3, date);
            stmt.setString(4, shift);
            stmt.executeUpdate();
        }
    }

    // Get attendance status for a laborer on a specific date
    public static String getAttendanceStatus(int laborerId, java.sql.Date date) throws SQLException {
        String sql = "SELECT status FROM attendance WHERE laborer_id = ? AND date = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, laborerId);
            stmt.setDate(2, date);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("status");
            }
        }

        return null;
    }

    // List all attendance records
    public static List<Object[]> getAllAttendanceRecords() throws SQLException {
        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT a.laborer_id, l.name, a.date, a.status, a.shift " +
                     "FROM attendance a JOIN laborers l ON a.laborer_id = l.id " +
                     "ORDER BY a.date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("laborer_id"),
                    rs.getString("name"),
                    rs.getDate("date"),
                    rs.getString("status"),
                    rs.getString("shift")
                });
            }
        }

        return list;
    }

    // View attendance by date and shift
    public static List<AttendanceRecord> getAttendanceByDateAndShift(java.sql.Date date, String shift) throws SQLException {
        List<AttendanceRecord> list = new ArrayList<>();
        String sql = "SELECT l.name, a.status " +
                     "FROM attendance a JOIN laborers l ON a.laborer_id = l.id " +
                     "WHERE a.date = ? AND a.shift = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, date);
            stmt.setString(2, shift);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new AttendanceRecord(rs.getString("name"), rs.getString("status")));
            }
        }

        return list;
    }

    // Stub methods if still needed by your code (can be removed)
    public static List<Laborer> getAllAttendance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void updateStatus(int id, String trim) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

