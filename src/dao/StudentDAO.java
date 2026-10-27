package dao;

import database.DatabaseManager;
import exception.DuplicateStudentException;
import model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Add a new student record
    public void addStudent(Student student) throws DuplicateStudentException, SQLException {
        String query = "INSERT INTO student (student_id, name, department, semester, phone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment());
            pstmt.setInt(4, student.getSemester());
            pstmt.setString(5, student.getPhone());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateStudentException("Student with ID " + student.getStudentId() + " already exists.");
            }
            throw e;
        }
    }

    // Get a single student by ID
    public Student getStudentById(String studentId) throws SQLException {
        String query = "SELECT * FROM student WHERE student_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getString("student_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("semester"),
                        rs.getString("phone")
                );
            }
        }
        return null;
    }

    // Fetch all student records
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM student";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getString("student_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("semester"),
                        rs.getString("phone")
                ));
            }
        }
        return students;
    }

    // Update existing student details
    public void updateStudent(Student student) throws SQLException {
        String query = "UPDATE student SET name = ?, department = ?, semester = ?, phone = ? WHERE student_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getDepartment());
            pstmt.setInt(3, student.getSemester());
            pstmt.setString(4, student.getPhone());
            pstmt.setString(5, student.getStudentId());
            pstmt.executeUpdate();
        }
    }

    // Delete student record
    public void deleteStudent(String studentId) throws SQLException {
        String query = "DELETE FROM student WHERE student_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            pstmt.executeUpdate();
        }
    }
}