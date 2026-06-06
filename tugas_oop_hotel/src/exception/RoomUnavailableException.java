// exception khusus saat kamar yang dipesan ternyata tidak tersedia
package exception;

public class RoomUnavailableException extends Exception {
    public RoomUnavailableException(String message) {
        super(message);
    }
}
