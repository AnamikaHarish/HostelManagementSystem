package model;

public class Allocation {
    private int allocationId;
    private String studentId;
    private int roomId;
    private String allocationDate;
    private String checkoutDate;
    private String status; // "ACTIVE" or "INACTIVE"

    // Default Constructor
    public Allocation() {
    }

    // Parameterized Constructor
    public Allocation(int allocationId, String studentId, int roomId, String allocationDate, String checkoutDate, String status) {
        this.allocationId = allocationId;
        this.studentId = studentId;
        this.roomId = roomId;
        this.allocationDate = allocationDate;
        this.checkoutDate = checkoutDate;
        this.status = status;
    }

    // Getters and Setters
    public int getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(int allocationId) {
        this.allocationId = allocationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(String allocationDate) {
        this.allocationDate = allocationDate;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Allocation{" +
                "allocationId=" + allocationId +
                ", studentId='" + studentId + '\'' +
                ", roomId=" + roomId +
                ", allocationDate='" + allocationDate + '\'' +
                ", checkoutDate='" + checkoutDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}