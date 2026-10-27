package model;

public class Room {
    private int roomId;
    private String block;
    private String roomNumber;
    private int capacity;
    private int occupiedBeds;

    // Default Constructor
    public Room() {
    }

    // Parameterized Constructor
    public Room(int roomId, String block, String roomNumber, int capacity, int occupiedBeds) {
        this.roomId = roomId;
        this.block = block;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.occupiedBeds = occupiedBeds;
    }

    // Getters and Setters
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupiedBeds() {
        return occupiedBeds;
    }

    public void setOccupiedBeds(int occupiedBeds) {
        this.occupiedBeds = occupiedBeds;
    }

    // Helper method to check if room is full
    public boolean isFull() {
        return occupiedBeds >= capacity;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", block='" + block + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", capacity=" + capacity +
                ", occupiedBeds=" + occupiedBeds +
                '}';
    }
}