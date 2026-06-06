// pengontrol utama (singleton) untuk kelola daftar data dan logika book kamar
package service;

import exception.InvalidReservationException;
import exception.RoomUnavailableException;
import model.person.Guest;
import model.reservation.Reservation;
import model.room.Room;
import model.room.RoomStatus;
import service.payment.PaymentStrategy;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HotelManager implements Serializable {
    private static transient HotelManager instance;

    private List<Room> rooms;
    private List<Guest> guests;
    private List<Reservation> reservations;
    private List<model.person.Employee> employees;

    private HotelManager() {
        rooms = new ArrayList<>();
        guests = new ArrayList<>();
        reservations = new ArrayList<>();
        employees = new ArrayList<>();
    }

    public static HotelManager getInstance() {
        if (instance == null) {
            instance = DataStorage.loadHotelManager();
            if (instance == null) {
                instance = new HotelManager();
            }
        }
        return instance;
    }

    // crud
    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void addGuest(Guest guest) {
        guests.add(guest);
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void addEmployee(model.person.Employee employee) {
        employees.add(employee);
    }

    public List<model.person.Employee> getEmployees() {
        return employees;
    }

    // logika bisnis utama untuk validasi dan memproses penyewaan kamar tamu
    public Reservation bookRoom(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut, PaymentStrategy paymentMethod) throws RoomUnavailableException, InvalidReservationException {
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new RoomUnavailableException("Kamar " + room.getRoomNumber() + " tidak tersedia.");
        }
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new InvalidReservationException("Tanggal Check-Out harus setelah Check-In.");
        }

        // Simpan data guest jika belum ada
        if (!guests.contains(guest)) {
            guests.add(guest);
        }

        String reservationId = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Reservation reservation = new Reservation(reservationId, guest, room, checkIn, checkOut, paymentMethod);
        
        // proses bayar menggunakan pola desain strategy (penerapan polimorfisme)
        reservation.processPayment();
        
        // Ubah status kamar
        room.setStatus(RoomStatus.OCCUPIED);
        
        reservations.add(reservation);
        return reservation;
    }

    public void checkout(Reservation reservation) {
        Room room = reservation.getRoom();
        if (room != null) {
            room.setStatus(RoomStatus.CLEANING); // Set ke cleaning setelah checkout
        }
        reservations.remove(reservation);
    }
    
    public void saveData() {
        DataStorage.saveHotelManager(this);
    }
    
    // Method untuk mengatur ulang instance (hanya digunakan secara internal untuk memulihkan dari file)
    public static void setInstance(HotelManager loadedInstance) {
        instance = loadedInstance;
    }
}
