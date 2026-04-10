import java.util.*;
import java.io.*;

class Room {
    int id;
    String type;
    double price;
    boolean available;

    Room(int id, String type, double price) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.available = true;
    }

    public String toString() {
        return "Room ID: " + id + " | Type: " + type + " | Price: " + price + " | Available: " + available;
    }
}

class Booking {
    int bookingId;
    int roomId;
    String userName;

    Booking(int bookingId, int roomId, String userName) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.userName = userName;
    }

    public String toString() {
        return "Booking ID: " + bookingId + " | Room ID: " + roomId + " | User: " + userName;
    }
}

public class HotelReservation {

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int bookingCounter = 1;

    public static void main(String[] args) {

        // Sample Rooms
        rooms.add(new Room(1, "Standard", 3000));
        rooms.add(new Room(2, "Deluxe", 5000));
        rooms.add(new Room(3, "Suite", 8000));

        while (true) {
            System.out.println("\n===== HOTEL SYSTEM =====");
            System.out.println("1. View Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Bookings");
            System.out.println("5. Exit");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    viewRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    viewBookings();
                    break;
                case 5:
                    saveData();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    static void viewRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room r : rooms) {
            System.out.println(r);
        }
    }

    static void bookRoom() {
        System.out.print("Enter your name: ");
        String name = sc.next();

        System.out.print("Enter Room ID: ");
        int id = sc.nextInt();

        for (Room r : rooms) {
            if (r.id == id && r.available) {
                r.available = false;

                Booking b = new Booking(bookingCounter++, id, name);
                bookings.add(b);

                System.out.println("Room booked successfully!");
                return;
            }
        }

        System.out.println("Room not available!");
    }

    static void cancelBooking() {
        System.out.print("Enter Booking ID: ");
        int id = sc.nextInt();

        Iterator<Booking> it = bookings.iterator();

        while (it.hasNext()) {
            Booking b = it.next();

            if (b.bookingId == id) {

                for (Room r : rooms) {
                    if (r.id == b.roomId) {
                        r.available = true;
                    }
                }

                it.remove();
                System.out.println("Booking cancelled!");
                return;
            }
        }

        System.out.println("Booking not found!");
    }

    static void viewBookings() {
        System.out.println("\nAll Bookings:");
        for (Booking b : bookings) {
            System.out.println(b);
        }
    }

    static void saveData() {
        try {
            FileWriter fw = new FileWriter("bookings.txt");

            for (Booking b : bookings) {
                fw.write(b.toString() + "\n");
            }

            fw.close();
        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }
}