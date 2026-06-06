// tipe kamar deluxe, ada harga tambahan dan opsi balkon
package model.room;

public class DeluxeRoom extends Room {
    private boolean hasBalcony;

    public DeluxeRoom(String roomNumber, double basePrice, boolean hasBalcony) {
        super(roomNumber, basePrice);
        this.hasBalcony = hasBalcony;
    }

    public boolean isHasBalcony() {
        return hasBalcony;
    }

    @Override
    public double calculateDailyRate() {
        double rate = getBasePrice() * 1.2; // 20% more expensive
        if (hasBalcony) {
            rate += 50.0; // Additional flat fee for balcony
        }
        return rate;
    }

    @Override
    public String getRoomType() {
        return "Deluxe";
    }
}
