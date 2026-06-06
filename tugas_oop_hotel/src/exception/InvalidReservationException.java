// exception jika tanggal reservasi tidak valid (check out lebih awal dari check in)
package exception;

public class InvalidReservationException extends Exception {
    public InvalidReservationException(String message) {
        super(message);
    }
}
