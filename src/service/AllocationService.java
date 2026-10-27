package service;

import dao.AllocationDAO;
import dao.RoomDAO;
import dao.StudentDAO;
import exception.DuplicateStudentException;
import exception.RoomFullException;
import exception.StudentNotFoundException;
import model.Allocation;
import model.Room;
import model.Student;

import java.sql.SQLException;
import java.util.List;

public class AllocationService {

    private StudentDAO studentDAO;
    private RoomDAO roomDAO;
    private AllocationDAO allocationDAO;

    public AllocationService() {
        this.studentDAO = new StudentDAO();
        this.roomDAO = new RoomDAO();
        this.allocationDAO = new AllocationDAO();
    }

    // Register a new student with validation
    public void registerStudent(Student student) throws DuplicateStudentException, SQLException {
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty() ||
            student.getName() == null || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID and Name cannot be empty.");
        }
        studentDAO.addStudent(student);
    }

    // Register a new room with capacity check
    public void addRoom(Room room) throws SQLException {
        if (room.getCapacity() <= 0) {
            throw new IllegalArgumentException("Room capacity must be greater than zero.");
        }
        roomDAO.addRoom(room);
    }

    // Allocate a student to a room
    public void allocateStudent(String studentId, int roomId, String allocationDate)
            throws StudentNotFoundException, RoomFullException, DuplicateStudentException, SQLException {

        // Rule 1: Check if student exists
        Student student = studentDAO.getStudentById(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student with ID " + studentId + " does not exist.");
        }

        // Rule 2: Check if student already has an active allocation
        Allocation activeAlloc = allocationDAO.getActiveAllocationByStudentId(studentId);
        if (activeAlloc != null) {
            throw new DuplicateStudentException("Student " + studentId + " already has an active room allocation.");
        }

        // Rule 3: Check if room exists
        Room room = roomDAO.getRoomById(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Room ID " + roomId + " does not exist.");
        }

        // Rule 4: Check if room is full
        if (room.isFull()) {
            throw new RoomFullException("Room " + room.getRoomNumber() + " (Block " + room.getBlock() + ") is full.");
        }

        // Create new active allocation
        Allocation allocation = new Allocation(0, studentId, roomId, allocationDate, null, "ACTIVE");
        allocationDAO.addAllocation(allocation);

        // Update room occupancy (+1)
        roomDAO.updateOccupiedBeds(roomId, room.getOccupiedBeds() + 1);
    }

    // De-allocate a student from a room
    public void deallocateStudent(String studentId, String checkoutDate)
            throws StudentNotFoundException, SQLException {

        // Find active allocation for the student
        Allocation activeAlloc = allocationDAO.getActiveAllocationByStudentId(studentId);
        if (activeAlloc == null) {
            throw new StudentNotFoundException("No active allocation found for student ID " + studentId);
        }

        // Update allocation record to INACTIVE
        allocationDAO.deallocateStudent(activeAlloc.getAllocationId(), checkoutDate);

        // Decrement room occupancy (-1)
        Room room = roomDAO.getRoomById(activeAlloc.getRoomId());
        if (room != null && room.getOccupiedBeds() > 0) {
            roomDAO.updateOccupiedBeds(room.getRoomId(), room.getOccupiedBeds() - 1);
        }
    }

    // Getter methods for GUI views
    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms();
    }

    public List<Room> getAvailableRooms() throws SQLException {
        return roomDAO.getAvailableRooms();
    }

    public List<Allocation> getAllAllocations() throws SQLException {
        return allocationDAO.getAllAllocations();
    }

    // Search student by ID
    public Student searchStudent(String studentId) throws SQLException {
        return studentDAO.getStudentById(studentId);
    }

    // Delete student record (only if no active allocation)
    public void deleteStudent(String studentId) throws SQLException, IllegalStateException {
        Allocation activeAlloc = allocationDAO.getActiveAllocationByStudentId(studentId);
        if (activeAlloc != null) {
            throw new IllegalStateException("Cannot delete student. Student currently has an active room allocation.");
        }
        studentDAO.deleteStudent(studentId);
    }

    // Delete room record (only if occupied_beds == 0)
    public void deleteRoom(int roomId) throws SQLException, IllegalStateException {
        Room room = roomDAO.getRoomById(roomId);
        if (room != null && room.getOccupiedBeds() > 0) {
            throw new IllegalStateException("Cannot delete room while it has active occupants.");
        }
        roomDAO.deleteRoom(roomId);
    }
}