package dao;

import database.DatabaseManager;
import model.Allocation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllocationDAO {

    // Record a new room allocation
    public void addAllocation(Allocation allocation) throws SQLException {
        String query = "INSERT INTO allocation (student_id, room_id, allocation_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, allocation.getStudentId());
            pstmt.setInt(2, allocation.getRoomId());
            pstmt.setString(3, allocation.getAllocationDate());
            pstmt.setString(4, allocation.getStatus());
            pstmt.executeUpdate();
        }
    }

    // Check if a student already has an active allocation
    public Allocation getActiveAllocationByStudentId(String studentId) throws SQLException {
        String query = "SELECT * FROM allocation WHERE student_id = ? AND status = 'ACTIVE'";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Allocation(
                        rs.getInt("allocation_id"),
                        rs.getString("student_id"),
                        rs.getInt("room_id"),
                        rs.getString("allocation_date"),
                        rs.getString("checkout_date"),
                        rs.getString("status")
                );
            }
        }
        return null;
    }

    // De-allocate a student (update status to INACTIVE and set checkout_date)
    public void deallocateStudent(int allocationId, String checkoutDate) throws SQLException {
        String query = "UPDATE allocation SET status = 'INACTIVE', checkout_date = ? WHERE allocation_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, checkoutDate);
            pstmt.setInt(2, allocationId);
            pstmt.executeUpdate();
        }
    }

    // Fetch all allocation records (both active and inactive)
    public List<Allocation> getAllAllocations() throws SQLException {
        List<Allocation> allocations = new ArrayList<>();
        String query = "SELECT * FROM allocation";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                allocations.add(new Allocation(
                        rs.getInt("allocation_id"),
                        rs.getString("student_id"),
                        rs.getInt("room_id"),
                        rs.getString("allocation_date"),
                        rs.getString("checkout_date"),
                        rs.getString("status")
                ));
            }
        }
        return allocations;
    }
}