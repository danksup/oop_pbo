// antarmuka ui panel laporan ringkasan isi hotel saat ini
package gui;

import service.HotelManager;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private JLabel totalRoomsLabel;
    private JLabel availableRoomsLabel;
    private JLabel activeReservationsLabel;

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Dasbor Hotel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        totalRoomsLabel = createStatCard("Total Kamar", "0");
        availableRoomsLabel = createStatCard("Kamar Tersedia", "0");
        activeReservationsLabel = createStatCard("Reservasi Aktif", "0");

        statsPanel.add(totalRoomsLabel);
        statsPanel.add(availableRoomsLabel);
        statsPanel.add(activeReservationsLabel);

        add(statsPanel, BorderLayout.CENTER);
    }

    private JLabel createStatCard(String title, String value) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + title + "<br><b style='font-size:20px'>" + value + "</b></div></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        label.setOpaque(true);
        label.setBackground(new Color(236, 240, 241));
        return label;
    }

    public void refreshData() {
        HotelManager manager = HotelManager.getInstance();
        int totalRooms = manager.getRooms().size();
        long availableRooms = manager.getRooms().stream()
                .filter(r -> r.getStatus() == model.room.RoomStatus.AVAILABLE)
                .count();
        int reservations = manager.getReservations().size();

        totalRoomsLabel.setText("<html><div style='text-align: center;'>Total Kamar<br><b style='font-size:20px'>" + totalRooms + "</b></div></html>");
        availableRoomsLabel.setText("<html><div style='text-align: center;'>Kamar Tersedia<br><b style='font-size:20px'>" + availableRooms + "</b></div></html>");
        activeReservationsLabel.setText("<html><div style='text-align: center;'>Reservasi Aktif<br><b style='font-size:20px'>" + reservations + "</b></div></html>");
    }
}
