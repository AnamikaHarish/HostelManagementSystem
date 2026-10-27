package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:database/hostel.db";

    // Establish and return a JDBC Connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Auto-create database file and tables if they do not exist
    public static void initializeDatabase() {
        String createStudentTable = "CREATE TABLE IF NOT EXISTS student (" +
                "student_id VARCHAR(50) PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "department VARCHAR(50) NOT NULL, " +
                "semester INTEGER NOT NULL, " +
                "phone VARCHAR(15) NOT NULL);";

        String createRoomTable = "CREATE TABLE IF NOT EXISTS room (" +
                "room_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "block VARCHAR(10) NOT NULL, " +
                "room_number VARCHAR(10) NOT NULL, " +
                "capacity INTEGER NOT NULL, " +
                "occupied_beds INTEGER DEFAULT 0);";

        String createAllocationTable = "CREATE TABLE IF NOT EXISTS allocation (" +
                "allocation_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id VARCHAR(50) NOT NULL, " +
                "room_id INTEGER NOT NULL, " +
                "allocation_date TEXT NOT NULL, " +
                "checkout_date TEXT, " +
                "status VARCHAR(20) DEFAULT 'ACTIVE', " +
                "FOREIGN KEY (student_id) REFERENCES student(student_id), " +
                "FOREIGN KEY (room_id) REFERENCES room(room_id));";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute(createStudentTable);
            stmt.execute(createRoomTable);
            stmt.execute(createAllocationTable);
            
            System.out.println("SQLite Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
}