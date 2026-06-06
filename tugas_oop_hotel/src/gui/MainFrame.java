// jendela ui besar untuk menu utama dashboard yang gonta-ganti isi menu cardnya
package gui;

import service.HotelManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private DashboardPanel dashboardPanel;
    private RoomPanel roomPanel;
    private ReservationPanel reservationPanel;

    public MainFrame() {
        setTitle("Sistem Manajemen Hotel OOP");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Handle application exit to save data
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int confirm = JOptionPane.showOptionDialog(MainFrame.this,
                        "Apakah Anda yakin ingin keluar? Data akan disimpan.",
                        "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION) {
                    HotelManager.getInstance().saveData();
                    System.exit(0);
                }
            }
        });

        // Initialize UI components
        initComponents();
    }

    private void initComponents() {
        // Setup Navigation Bar
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(4, 1, 10, 10));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        navPanel.setBackground(new Color(44, 62, 80));

        JButton btnDashboard = createNavButton("Dasbor");
        JButton btnRooms = createNavButton("Kelola Kamar");
        JButton btnReservations = createNavButton("Reservasi");
        JButton btnExit = createNavButton("Simpan & Keluar");

        navPanel.add(btnDashboard);
        navPanel.add(btnRooms);
        navPanel.add(btnReservations);
        navPanel.add(btnExit);

        // Setup Main Content Area with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        dashboardPanel = new DashboardPanel();
        roomPanel = new RoomPanel();
        reservationPanel = new ReservationPanel(this);

        mainPanel.add(dashboardPanel, "Dasbor");
        mainPanel.add(roomPanel, "Kamar");
        mainPanel.add(reservationPanel, "Reservasi");

        // Action Listeners for Navigation
        btnDashboard.addActionListener(e -> {
            dashboardPanel.refreshData();
            cardLayout.show(mainPanel, "Dasbor");
        });
        btnRooms.addActionListener(e -> {
            roomPanel.refreshData();
            cardLayout.show(mainPanel, "Kamar");
        });
        btnReservations.addActionListener(e -> {
            reservationPanel.refreshData();
            cardLayout.show(mainPanel, "Reservasi");
        });
        btnExit.addActionListener(e -> {
            HotelManager.getInstance().saveData();
            System.exit(0);
        });

        // Add to Frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(navPanel, BorderLayout.WEST);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }
    
    public void navigateTo(String panelName) {
        if(panelName.equals("Dasbor")) dashboardPanel.refreshData();
        else if(panelName.equals("Kamar")) roomPanel.refreshData();
        else if(panelName.equals("Reservasi")) reservationPanel.refreshData();
        cardLayout.show(mainPanel, panelName);
    }
}
