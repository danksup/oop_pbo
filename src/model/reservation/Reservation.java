// class yang menyimpan keseluruhan data transaksi penyewaan kamar oleh tamu
package model.reservation;

import model.person.Guest;
import model.room.Room;
import service.payment.PaymentStrategy;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation implements Serializable {
    private String reservationId;
    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private PaymentStrategy paymentMethod;
    private double totalAmount;

    public Reservation(String reservationId, Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate, PaymentStrategy paymentMethod) {
        this.reservationId = reservationId;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.paymentMethod = paymentMethod;
        this.totalAmount = calculateTotalAmount();
    }

    // fungsi internal private untuk hitung total biaya durasi
    private double calculateTotalAmount() {
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (days <= 0) days = 1; // Minimum 1 day
        return days * room.calculateDailyRate();
    }

    // enkapsulasi baca data (getter) untuk kelas lain
    public String getReservationId() {
        return reservationId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public PaymentStrategy getPaymentMethod() {
        return paymentMethod;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void processPayment() {
        if (paymentMethod != null) {
            paymentMethod.pay(totalAmount);
        }
    }
    
    @Override
    public String toString() {
        return "ResID: " + reservationId + " | Guest: " + guest.getName() + " | Room: " + room.getRoomNumber() + " | In: " + checkInDate + " | Out: " + checkOutDate + " | Total: $" + String.format("%.2f", totalAmount);
    }
}
