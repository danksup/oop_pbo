// tipe kamar termewah, harga tambahan lebih besar dan dihitung per kasur
package model.room;

public class SuiteRoom extends Room {
    private int numberOfBedrooms;

    public SuiteRoom(String roomNumber, double basePrice, int numberOfBedrooms) {
        super(roomNumber, basePrice); // memanggil properti kelas induk
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    // polimorfisme: cara hitung tarif khusus tipe suite (ditimpa dari parent)
    @Override
    public double calculateDailyRate() {
        return getBasePrice() * 1.5 + (numberOfBedrooms * 100.0); // 50% more base + per bedroom charge
    }

    @Override
    public String getRoomType() {
        return "Suite";
    }
}
