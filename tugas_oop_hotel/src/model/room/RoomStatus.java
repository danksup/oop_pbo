// enum untuk menyimpan daftar pilihan status kamar
package model.room;

public enum RoomStatus {
    AVAILABLE("Tersedia"),
    OCCUPIED("Terisi"),
    CLEANING("Dibersihkan"),
    MAINTENANCE("Perawatan");

    private final String displayName;

    RoomStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
