// class utilitas untuk menyimpan dan memuat objek hotel manager ke file .dat
package service;

import java.io.*;

public class DataStorage {
    private static final String DATA_FILE = "hotel_data.dat";

    public static void saveHotelManager(HotelManager manager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(manager);
            System.out.println("Data successfully saved.");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public static HotelManager loadHotelManager() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                HotelManager manager = (HotelManager) ois.readObject();
                System.out.println("Data successfully loaded.");
                return manager;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data: " + e.getMessage());
            }
        }
        System.out.println("No previous data found. Starting fresh.");
        return null;
    }
}
