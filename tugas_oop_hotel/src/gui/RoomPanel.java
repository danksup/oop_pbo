// panel ui untuk tabel info kamar hotel, ganti status dan tambah entri kamar
package gui;

import model.room.Room;
import model.room.RoomStatus;
import service.HotelManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoomPanel extends JPanel {
    private JTable roomTable;
    private DefaultTableModel tableModel;

    public RoomPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Manajemen Kamar");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Nomor Kamar", "Tipe", "Harga Dasar", "Tarif Harian", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(roomTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton btnAddRoom = new JButton("Tambah Kamar Baru");
        JButton btnChangeStatus = new JButton("Ubah Status");
        bottomPanel.add(btnAddRoom);
        bottomPanel.add(btnChangeStatus);
        add(bottomPanel, BorderLayout.SOUTH);

        btnAddRoom.addActionListener(e -> showAddRoomDialog());

        btnChangeStatus.addActionListener(e -> {
            int selectedRow = roomTable.getSelectedRow();
            if (selectedRow >= 0) {
                String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
                changeRoomStatus(roomNumber);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih kamar terlebih dahulu.");
            }
        });
    }

    // logika fungsional untuk mencari dan mengubah tipe status kamar tertentu
    private void changeRoomStatus(String roomNumber) {
        HotelManager manager = HotelManager.getInstance();
        Room roomToUpdate = null;
        for (Room r : manager.getRooms()) {
            if (r.getRoomNumber().equals(roomNumber)) {
                roomToUpdate = r;
                break;
            }
        }

        if (roomToUpdate != null) {
            RoomStatus[] statuses = RoomStatus.values();
            RoomStatus newStatus = (RoomStatus) JOptionPane.showInputDialog(this,
                    "Pilih status baru untuk Kamar " + roomNumber,
                    "Ubah Status",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    statuses,
                    roomToUpdate.getStatus());

            if (newStatus != null) {
                roomToUpdate.setStatus(newStatus);
                refreshData();
                JOptionPane.showMessageDialog(this, "Status berhasil diperbarui.");
            }
        }
    }

    private void showAddRoomDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tambah Kamar Baru", true);
        dialog.setSize(350, 250);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField txtRoomNumber = new JTextField();
        JTextField txtBasePrice = new JTextField();
        String[] types = {"Standard", "Deluxe", "Suite"};
        JComboBox<String> cbType = new JComboBox<>(types);

        JLabel lblExtra = new JLabel("-");
        JTextField txtExtra = new JTextField();
        txtExtra.setEnabled(false);

        cbType.addActionListener(e -> {
            String selected = (String) cbType.getSelectedItem();
            if ("Standard".equals(selected)) {
                lblExtra.setText("-");
                txtExtra.setEnabled(false);
                txtExtra.setText("");
            } else if ("Deluxe".equals(selected)) {
                lblExtra.setText("Balkon (true/false):");
                txtExtra.setEnabled(true);
            } else if ("Suite".equals(selected)) {
                lblExtra.setText("Jumlah Kasur (angka):");
                txtExtra.setEnabled(true);
            }
        });

        dialog.add(new JLabel("Nomor Kamar:")); dialog.add(txtRoomNumber);
        dialog.add(new JLabel("Tipe Kamar:")); dialog.add(cbType);
        dialog.add(new JLabel("Harga Dasar ($):")); dialog.add(txtBasePrice);
        dialog.add(lblExtra); dialog.add(txtExtra);

        JButton btnSave = new JButton("Simpan");
        dialog.add(new JLabel());
        dialog.add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                String roomNum = txtRoomNumber.getText().trim();
                if (roomNum.isEmpty()) throw new IllegalArgumentException("Nomor kamar tidak boleh kosong!");
                
                double price = Double.parseDouble(txtBasePrice.getText().trim());
                String type = (String) cbType.getSelectedItem();

                HotelManager manager = HotelManager.getInstance();
                for (Room r : manager.getRooms()) {
                    if (r.getRoomNumber().equals(roomNum)) {
                        throw new IllegalArgumentException("Nomor kamar sudah ada!");
                    }
                }

                Room newRoom;
                if ("Standard".equals(type)) {
                    newRoom = new model.room.StandardRoom(roomNum, price);
                } else if ("Deluxe".equals(type)) {
                    boolean balcony = Boolean.parseBoolean(txtExtra.getText().trim());
                    newRoom = new model.room.DeluxeRoom(roomNum, price, balcony);
                } else {
                    int beds = Integer.parseInt(txtExtra.getText().trim());
                    newRoom = new model.room.SuiteRoom(roomNum, price, beds);
                }

                manager.addRoom(newRoom);
                refreshData();
                JOptionPane.showMessageDialog(dialog, "Kamar baru berhasil ditambahkan!");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Format angka harga / kasur salah!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    // perbarui ulang tampilan tabel dengan menarik data terbaru dari manager
    public void refreshData() {
        tableModel.setRowCount(0); // Clear table
        List<Room> rooms = HotelManager.getInstance().getRooms();
        for (Room room : rooms) {
            Object[] rowData = {
                    room.getRoomNumber(),
                    room.getRoomType(),
                    String.format("$%.2f", room.getBasePrice()),
                    String.format("$%.2f", room.calculateDailyRate()), // Polymorphism in action
                    room.getStatus().getDisplayName()
            };
            tableModel.addRow(rowData);
        }
    }
}
