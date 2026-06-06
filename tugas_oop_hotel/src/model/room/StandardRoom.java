// tipe kamar standar, turunan dari room tanpa biaya tambahan
package model.room;

public class StandardRoom extends Room {
    public StandardRoom(String roomNumber, double basePrice) {
        super(roomNumber, basePrice);
    }

    @Override
    public double calculateDailyRate() {
        // Standard room doesn't have additional multipliers
        return getBasePrice();
    }

    @Override
    public String getRoomType() {
        return "Standard";
    }
}
