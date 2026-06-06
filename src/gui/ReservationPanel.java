// ui tabel reservasi tamu menginap, serta formulir pemesanan check in tamu
package gui;

import exception.InvalidReservationException;
import exception.RoomUnavailableException;
import model.person.Guest;
import model.reservation.Reservation;
import model.room.Room;
import model.room.RoomStatus;
import service.HotelManager;
import service.payment.CashPayment;
import service.payment.CreditCardPayment;
import service.payment.PaymentStrategy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReservationPanel extends JPanel {
    private MainFrame parentFrame;
    private JTable reservationTable;
    private DefaultTableModel tableModel;

    public ReservationPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Reservasi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID Res", "Tamu", "Kamar", "Check-In", "Check-Out", "Total", "Pembayaran"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationTable = new JTable(tableModel);
        add(new JScrollPane(reservationTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton btnNewRes = new JButton("Reservasi Baru");
        JButton btnCheckout = new JButton("Check Out");
        bottomPanel.add(btnNewRes);
        bottomPanel.add(btnCheckout);
        add(bottomPanel, BorderLayout.SOUTH);

        btnNewRes.addActionListener(e -> showNewReservationDialog());
        btnCheckout.addActionListener(e -> processCheckout());
    }

    private void showNewReservationDialog() {
        JDialog dialog = new JDialog(parentFrame, "Reservasi Baru", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));
        dialog.setLocationRelativeTo(parentFrame);

        JTextField txtGuestName = new JTextField();
        JTextField txtGuestId = new JTextField(UUID.randomUUID().toString().substring(0,6));
        JTextField txtGuestPhone = new JTextField();
        
        HotelManager manager = HotelManager.getInstance();
        List<Room> availableRooms = manager.getRooms().stream()
                .filter(r -> r.getStatus() == RoomStatus.AVAILABLE)
                .collect(Collectors.toList());

        JComboBox<Room> cbRooms = new JComboBox<>(availableRooms.toArray(new Room[0]));
        JTextField txtCheckIn = new JTextField(LocalDate.now().toString());
        JTextField txtCheckOut = new JTextField(LocalDate.now().plusDays(1).toString());
        String[] paymentMethods = {"Tunai", "Kartu Kredit"};
        JComboBox<String> cbPayment = new JComboBox<>(paymentMethods);

        dialog.add(new JLabel("Nama Tamu:")); dialog.add(txtGuestName);
        dialog.add(new JLabel("ID Tamu (KTP/Paspor):")); dialog.add(txtGuestId);
        dialog.add(new JLabel("No. Telepon:")); dialog.add(txtGuestPhone);
        dialog.add(new JLabel("Pilih Kamar:")); dialog.add(cbRooms);
        dialog.add(new JLabel("Check-In (YYYY-MM-DD):")); dialog.add(txtCheckIn);
        dialog.add(new JLabel("Check-Out (YYYY-MM-DD):")); dialog.add(txtCheckOut);
        dialog.add(new JLabel("Metode Pembayaran:")); dialog.add(cbPayment);

        JButton btnSubmit = new JButton("Pesan Sekarang");
        dialog.add(new JLabel()); // empty space
        dialog.add(btnSubmit);

        btnSubmit.addActionListener(e -> {
            try {
                if (txtGuestName.getText().isEmpty()) throw new IllegalArgumentException("Nama harus diisi");
                
                Guest guest = new Guest(txtGuestId.getText(), txtGuestName.getText(), txtGuestPhone.getText(), "Tidak Disebutkan");
                Room selectedRoom = (Room) cbRooms.getSelectedItem();
                if (selectedRoom == null) throw new IllegalArgumentException("Belum ada kamar yang dipilih");

                LocalDate checkIn = LocalDate.parse(txtCheckIn.getText());
                LocalDate checkOut = LocalDate.parse(txtCheckOut.getText());

                PaymentStrategy payment;
                if (cbPayment.getSelectedItem().equals("Tunai")) {
                    payment = new CashPayment();
                } else {
                    String ccNum = JOptionPane.showInputDialog(dialog, "Masukkan 16 digit Nomor Kartu Kredit:");
                    payment = new CreditCardPayment(ccNum, txtGuestName.getText());
                }

                manager.bookRoom(guest, selectedRoom, checkIn, checkOut, payment);
                JOptionPane.showMessageDialog(dialog, "Reservasi Berhasil!");
                dialog.dispose();
                refreshData();
                parentFrame.navigateTo("Reservasi"); // to update dashboard numbers if needed

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Format tanggal salah. Gunakan YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException | RoomUnavailableException | InvalidReservationException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void processCheckout() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow >= 0) {
            String resId = (String) tableModel.getValueAt(selectedRow, 0);
            HotelManager manager = HotelManager.getInstance();
            Reservation toCheckout = null;
            for (Reservation r : manager.getReservations()) {
                if (r.getReservationId().equals(resId)) {
                    toCheckout = r;
                    break;
                }
            }
            if (toCheckout != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Check out tamu " + toCheckout.getGuest().getName() + "?", "Konfirmasi Check Out", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    manager.checkout(toCheckout);
                    refreshData();
                    JOptionPane.showMessageDialog(this, "Check out berhasil. Kamar sedang dibersihkan.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih reservasi untuk di-check out.");
        }
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        for (Reservation res : HotelManager.getInstance().getReservations()) {
            Object[] row = {
                    res.getReservationId(),
                    res.getGuest().getName(),
                    res.getRoom().getRoomNumber() + " (" + res.getRoom().getRoomType() + ")",
                    res.getCheckInDate(),
                    res.getCheckOutDate(),
                    String.format("$%.2f", res.getTotalAmount()),
                    res.getPaymentMethod().getPaymentMethodName()
            };
            tableModel.addRow(row);
        }
    }
}
