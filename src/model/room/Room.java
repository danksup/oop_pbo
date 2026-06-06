// abstract class untuk kamar hotel. ini adalah fondasi polimorfisme untuk tarif harian
package model.room;

import java.io.Serializable;

public abstract class Room implements Serializable {
    private String roomNumber;
    private double basePrice;
    private RoomStatus status;

    // konstruktor inisialisasi data kamar
    public Room(String roomNumber, double basePrice) {
        this.roomNumber = roomNumber;
        this.basePrice = basePrice;
        this.status = RoomStatus.AVAILABLE;
    }

    // enkapsulasi: fungsi getter setter
    public String getRoomNumber() {
        return roomNumber;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    // abstraksi: method dasar untuk perhitungan tarif dinamis (polimorfisme)
    public abstract double calculateDailyRate();

    public abstract String getRoomType();
    
    @Override
    public String toString() {
        return getRoomType() + " - " + roomNumber + " (" + status.getDisplayName() + ")";
    }
}
