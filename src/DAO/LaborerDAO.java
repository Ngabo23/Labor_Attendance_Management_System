
package DAO;

/**
 *
 * @author DONOVAN
 */
import DBConnection.DBConnection;
import MODEL.Laborer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LaborerDAO {

    // === Add Laborer ===
    public static void addLaborer(Laborer l) throws SQLException {
        String sql = "INSERT INTO laborers (name, position, wage_per_day, national_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, l.getName());
            stmt.setString(2, l.getPosition());
            stmt.setDouble(3, l.getWagePerDay());
            stmt.setString(4, l.getNationalId());
            stmt.executeUpdate();
        }
    }

    // === Update Laborer ===
    public static void updateLaborer(Laborer l) throws SQLException {
        String sql = "UPDATE laborers SET name=?, position=?, wage_per_day=?, national_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, l.getName());
            stmt.setString(2, l.getPosition());
            stmt.setDouble(3, l.getWagePerDay());
            stmt.setString(4, l.getNationalId());
            stmt.setInt(5, l.getId());
            stmt.executeUpdate();
        }
    }

    // === Delete Laborer ===
    public static void deleteLaborer(int id) throws SQLException {
        String sql = "DELETE FROM laborers WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // === Get All Laborers ===
    public static List<Laborer> getAllLaborers() throws SQLException {
        List<Laborer> list = new ArrayList<>();
        String sql = "SELECT * FROM laborers";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Laborer l = new Laborer(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("position"),
                    rs.getDouble("wage_per_day"),
                    rs.getString("national_id")  // Include National ID
                );
                list.add(l);
            }
        }
        return list;
    }
}