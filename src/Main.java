// titik awal jalan aplikasi. buat data dummy jika awal lalu buka halaman login
import gui.LoginFrame;
import model.person.Employee;
import model.room.DeluxeRoom;
import model.room.StandardRoom;
import model.room.SuiteRoom;
import service.HotelManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // init kamar
        HotelManager manager = HotelManager.getInstance();
        if (manager.getRooms().isEmpty()) {
            manager.addRoom(new StandardRoom("101", 100.0));
            manager.addRoom(new StandardRoom("102", 100.0));
            manager.addRoom(new DeluxeRoom("201", 150.0, true));
            manager.addRoom(new DeluxeRoom("202", 150.0, false));
            manager.addRoom(new SuiteRoom("301", 300.0, 2));
            manager.saveData();
        }

        if (manager.getEmployees().isEmpty()) {
            Employee defaultAdmin = new Employee("EMP-001", "Administrator", "08123456789", "Manager", 5000.0, "admin", "admin123");
            manager.addEmployee(defaultAdmin);
            manager.saveData();
        }


        //gui
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
