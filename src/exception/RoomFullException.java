package exception;

public class RoomFullException extends Exception {
    public RoomFullException(String message) {
        super(message);
    }
}