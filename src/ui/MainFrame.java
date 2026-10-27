package ui;

import model.Allocation;
import model.Room;
import model.Student;
import service.AllocationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {

    private AllocationService service;

    // Student Tab UI Components
    private JTextField txtStudentId, txtStudentName, txtDepartment, txtSemester, txtPhone, txtSearchId;
    private JTable studentTable;
    private DefaultTableModel studentTableModel;

    // Room Tab UI Components
    private JTextField txtBlock, txtRoomNum, txtCapacity;
    private JTable roomTable;
    private DefaultTableModel roomTableModel;
    private JCheckBox chkAvailableOnly;

    // Allocation Tab UI Components
    private JTextField txtAllocStudentId, txtAllocRoomId, txtAllocDate, txtDeallocStudentId, txtCheckoutDate;
    private JTable allocTable;
    private DefaultTableModel allocTableModel;

    public MainFrame() {
        service = new AllocationService();

        setTitle("Hostel Room Allocation & Student Management System");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Student Management", createStudentPanel());
        tabbedPane.addTab("Room Management", createRoomPanel());
        tabbedPane.addTab("Allocation & Checkout", createAllocationPanel());

        add(tabbedPane);

        // Load initial database records
        loadStudentData();
        loadRoomData();
        loadAllocationData();
    }

    // --- STUDENT PANEL ---
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Register Student"));

        formPanel.add(new JLabel("Student ID:"));
        txtStudentId = new JTextField();
        formPanel.add(txtStudentId);

        formPanel.add(new JLabel("Name:"));
        txtStudentName = new JTextField();
        formPanel.add(txtStudentName);

        formPanel.add(new JLabel("Department:"));
        txtDepartment = new JTextField();
        formPanel.add(txtDepartment);

        formPanel.add(new JLabel("Semester:"));
        txtSemester = new JTextField();
        formPanel.add(txtSemester);

        formPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        JButton btnAddStudent = new JButton("Add Student");
        btnAddStudent.addActionListener(e -> addStudentAction());
        formPanel.add(btnAddStudent);

        // Search & Delete Action Bar
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionBar.add(new JLabel("Search ID:"));
        txtSearchId = new JTextField(10);
        actionBar.add(txtSearchId);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchStudentAction());
        actionBar.add(btnSearch);

        JButton btnReset = new JButton("Show All");
        btnReset.addActionListener(e -> loadStudentData());
        actionBar.add(btnReset);

        JButton btnDelete = new JButton("Delete Selected Student");
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> deleteStudentAction());
        actionBar.add(btnDelete);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(formPanel, BorderLayout.CENTER);
        topContainer.add(actionBar, BorderLayout.SOUTH);

        // Table Panel
        studentTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Department", "Semester", "Phone"}, 0);
        studentTable = new JTable(studentTableModel);

        panel.add(topContainer, BorderLayout.NORTH);
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        return panel;
    }

    private void addStudentAction() {
        try {
            String id = txtStudentId.getText().trim();
            String name = txtStudentName.getText().trim();
            String dept = txtDepartment.getText().trim();
            int sem = Integer.parseInt(txtSemester.getText().trim());
            String phone = txtPhone.getText().trim();

            Student student = new Student(id, name, dept, sem, phone);
            service.registerStudent(student);

            JOptionPane.showMessageDialog(this, "Student registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearStudentFields();
            loadStudentData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Semester must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchStudentAction() {
        String id = txtSearchId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a Student ID to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Student s = service.searchStudent(id);
            studentTableModel.setRowCount(0);
            if (s != null) {
                studentTableModel.addRow(new Object[]{s.getStudentId(), s.getName(), s.getDepartment(), s.getSemester(), s.getPhone()});
            } else {
                JOptionPane.showMessageDialog(this, "No student found with ID: " + id, "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudentAction() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String studentId = (String) studentTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete student " + studentId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.deleteStudent(studentId);
                JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStudentData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Delete Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadStudentData() {
        studentTableModel.setRowCount(0);
        try {
            List<Student> list = service.getAllStudents();
            for (Student s : list) {
                studentTableModel.addRow(new Object[]{s.getStudentId(), s.getName(), s.getDepartment(), s.getSemester(), s.getPhone()});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void clearStudentFields() {
        txtStudentId.setText("");
        txtStudentName.setText("");
        txtDepartment.setText("");
        txtSemester.setText("");
        txtPhone.setText("");
    }

    // --- ROOM PANEL ---
    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Hostel Room"));

        formPanel.add(new JLabel("Block:"));
        txtBlock = new JTextField();
        formPanel.add(txtBlock);

        formPanel.add(new JLabel("Room Number:"));
        txtRoomNum = new JTextField();
        formPanel.add(txtRoomNum);

        formPanel.add(new JLabel("Capacity:"));
        txtCapacity = new JTextField();
        formPanel.add(txtCapacity);

        JButton btnAddRoom = new JButton("Add Room");
        btnAddRoom.addActionListener(e -> addRoomAction());
        formPanel.add(btnAddRoom);

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkAvailableOnly = new JCheckBox("Show Available Rooms Only");
        chkAvailableOnly.addActionListener(e -> loadRoomData());
        actionBar.add(chkAvailableOnly);

        JButton btnDeleteRoom = new JButton("Delete Selected Room");
        btnDeleteRoom.setBackground(new Color(220, 53, 69));
        btnDeleteRoom.setForeground(Color.WHITE);
        btnDeleteRoom.addActionListener(e -> deleteRoomAction());
        actionBar.add(btnDeleteRoom);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(formPanel, BorderLayout.CENTER);
        topContainer.add(actionBar, BorderLayout.SOUTH);

        roomTableModel = new DefaultTableModel(new String[]{"Room ID", "Block", "Room No", "Capacity", "Occupied Beds"}, 0);
        roomTable = new JTable(roomTableModel);

        panel.add(topContainer, BorderLayout.NORTH);
        panel.add(new JScrollPane(roomTable), BorderLayout.CENTER);
        return panel;
    }

    private void addRoomAction() {
        try {
            String block = txtBlock.getText().trim();
            String roomNum = txtRoomNum.getText().trim();
            int cap = Integer.parseInt(txtCapacity.getText().trim());

            Room room = new Room(0, block, roomNum, cap, 0);
            service.addRoom(room);

            JOptionPane.showMessageDialog(this, "Room added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            txtBlock.setText("");
            txtRoomNum.setText("");
            txtCapacity.setText("");
            loadRoomData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRoomAction() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int roomId = (int) roomTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Room ID " + roomId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.deleteRoom(roomId);
                JOptionPane.showMessageDialog(this, "Room deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadRoomData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Delete Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadRoomData() {
        roomTableModel.setRowCount(0);
        try {
            List<Room> list = chkAvailableOnly.isSelected() ? service.getAvailableRooms() : service.getAllRooms();
            for (Room r : list) {
                roomTableModel.addRow(new Object[]{r.getRoomId(), r.getBlock(), r.getRoomNumber(), r.getCapacity(), r.getOccupiedBeds()});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // --- ALLOCATION PANEL ---
    private JPanel createAllocationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel formsContainer = new JPanel(new GridLayout(1, 2, 10, 10));

        // Allocation Form
        JPanel allocForm = new JPanel(new GridLayout(4, 2, 5, 5));
        allocForm.setBorder(BorderFactory.createTitledBorder("Allocate Room"));

        allocForm.add(new JLabel("Student ID:"));
        txtAllocStudentId = new JTextField();
        allocForm.add(txtAllocStudentId);

        allocForm.add(new JLabel("Room ID:"));
        txtAllocRoomId = new JTextField();
        allocForm.add(txtAllocRoomId);

        allocForm.add(new JLabel("Date (YYYY-MM-DD):"));
        txtAllocDate = new JTextField();
        allocForm.add(txtAllocDate);

        JButton btnAllocate = new JButton("Allocate Student");
        btnAllocate.addActionListener(e -> allocateAction());
        allocForm.add(btnAllocate);

        // Checkout Form
        JPanel checkoutForm = new JPanel(new GridLayout(3, 2, 5, 5));
        checkoutForm.setBorder(BorderFactory.createTitledBorder("De-allocate / Check Out"));

        checkoutForm.add(new JLabel("Student ID:"));
        txtDeallocStudentId = new JTextField();
        checkoutForm.add(txtDeallocStudentId);

        checkoutForm.add(new JLabel("Checkout Date:"));
        txtCheckoutDate = new JTextField();
        checkoutForm.add(txtCheckoutDate);

        JButton btnDeallocate = new JButton("De-allocate");
        btnDeallocate.addActionListener(e -> deallocateAction());
        checkoutForm.add(btnDeallocate);

        formsContainer.add(allocForm);
        formsContainer.add(checkoutForm);

        allocTableModel = new DefaultTableModel(new String[]{"Alloc ID", "Student ID", "Room ID", "Alloc Date", "Checkout Date", "Status"}, 0);
        allocTable = new JTable(allocTableModel);

        panel.add(formsContainer, BorderLayout.NORTH);
        panel.add(new JScrollPane(allocTable), BorderLayout.CENTER);
        return panel;
    }

    private void allocateAction() {
        try {
            String sId = txtAllocStudentId.getText().trim();
            int rId = Integer.parseInt(txtAllocRoomId.getText().trim());
            String date = txtAllocDate.getText().trim();

            service.allocateStudent(sId, rId, date);

            JOptionPane.showMessageDialog(this, "Student allocated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            txtAllocStudentId.setText("");
            txtAllocRoomId.setText("");
            txtAllocDate.setText("");
            loadAllocationData();
            loadRoomData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Room ID must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deallocateAction() {
        try {
            String sId = txtDeallocStudentId.getText().trim();
            String date = txtCheckoutDate.getText().trim();

            service.deallocateStudent(sId, date);

            JOptionPane.showMessageDialog(this, "Student de-allocated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            txtDeallocStudentId.setText("");
            txtCheckoutDate.setText("");
            loadAllocationData();
            loadRoomData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllocationData() {
        allocTableModel.setRowCount(0);
        try {
            List<Allocation> list = service.getAllAllocations();
            for (Allocation a : list) {
                allocTableModel.addRow(new Object[]{a.getAllocationId(), a.getStudentId(), a.getRoomId(), a.getAllocationDate(), a.getCheckoutDate(), a.getStatus()});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}