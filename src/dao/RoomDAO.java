package dao;

import database.DatabaseManager;
import model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    // Add a new room
    public void addRoom(Room room) throws SQLException {
        String query = "INSERT INTO room (block, room_number, capacity, occupied_beds) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, room.getBlock());
            pstmt.setString(2, room.getRoomNumber());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setInt(4, room.getOccupiedBeds());
            pstmt.executeUpdate();
        }
    }

    // Get a single room by ID
    public Room getRoomById(int roomId) throws SQLException {
        String query = "SELECT * FROM room WHERE room_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Room(
                        rs.getInt("room_id"),
                        rs.getString("block"),
                        rs.getString("room_number"),
                        rs.getInt("capacity"),
                        rs.getInt("occupied_beds")
                );
            }
        }
        return null;
    }

    // Fetch all rooms
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM room";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("room_id"),
                        rs.getString("block"),
                        rs.getString("room_number"),
                        rs.getInt("capacity"),
                        rs.getInt("occupied_beds")
                ));
            }
        }
        return rooms;
    }

    // Fetch rooms that have available capacity (occupied_beds < capacity)
    public List<Room> getAvailableRooms() throws SQLException {
        List<Room> availableRooms = new ArrayList<>();
        String query = "SELECT * FROM room WHERE occupied_beds < capacity";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                availableRooms.add(new Room(
                        rs.getInt("room_id"),
                        rs.getString("block"),
                        rs.getString("room_number"),
                        rs.getInt("capacity"),
                        rs.getInt("occupied_beds")
                ));
            }
        }
        return availableRooms;
    }

    // Update occupied beds count for a room
    public void updateOccupiedBeds(int roomId, int occupiedBeds) throws SQLException {
        String query = "UPDATE room SET occupied_beds = ? WHERE room_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, occupiedBeds);
            pstmt.setInt(2, roomId);
            pstmt.executeUpdate();
        }
    }

    // Delete a room
    public void deleteRoom(int roomId) throws SQLException {
        String query = "DELETE FROM room WHERE room_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, roomId);
            pstmt.executeUpdate();
        }
    }
}