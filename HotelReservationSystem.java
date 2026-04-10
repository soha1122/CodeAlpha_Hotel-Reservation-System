import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.*;

// =============== User Class ===============
class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private long createdAt;

    public User(String username, String password, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = System.currentTimeMillis();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public long getCreatedAt() { return createdAt; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' + ", createdAt=" + createdAt + '}';
    }
}

// =============== Room Class ===============
class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String type;
    private double price;
    private boolean available;
    private String description;

    public Room(int id, String type, double price, String description) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.description = description;
        this.available = true;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }
    public String getDescription() { return description; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("Room #%d | Type: %s | Price: Rs.%.2f | Status: %s | %s",
                id, type, price, (available ? "Available" : "Booked"), description);
    }
}

// =============== Booking Class ===============
class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    private int bookingId;
    private String username;
    private int roomId;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    private String status;
    private long bookingTime;

    public Booking(int bookingId, String username, int roomId, String roomType,
                   LocalDate checkInDate, LocalDate checkOutDate, double totalPrice) {
        this.bookingId = bookingId;
        this.username = username;
        this.roomId = roomId;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.status = "CONFIRMED";
        this.bookingTime = System.currentTimeMillis();
    }

    public int getBookingId() { return bookingId; }
    public String getUsername() { return username; }
    public int getRoomId() { return roomId; }
    public String getRoomType() { return roomType; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public long getBookingTime() { return bookingTime; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return String.format("Booking #%d | User: %s | Room: %s (#%d) | Check-in: %s | Check-out: %s | Total: Rs.%.2f | Status: %s",
                bookingId, username, roomType, roomId,
                checkInDate.format(formatter), checkOutDate.format(formatter), totalPrice, status);
    }
}

// =============== Database Manager ===============
class DatabaseManager {
    private static final String USERS_FILE = "data/users.dat";
    private static final String BOOKINGS_FILE = "data/bookings.dat";
    private static final String DATA_DIR = "data";

    static {
        new File(DATA_DIR).mkdirs();
    }

    public static void saveUsers(HashMap<String, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            return (HashMap<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    public static void saveBookings(ArrayList<Booking> bookings) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKINGS_FILE))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Booking> loadBookings() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKINGS_FILE))) {
            return (ArrayList<Booking>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}

// =============== Authentication Controller ===============
class AuthenticationController {
    private HashMap<String, User> users;

    public AuthenticationController() {
        this.users = DatabaseManager.loadUsers();
    }

    public boolean register(String username, String password, String confirmPassword, String email, String phoneNumber) {
        if (username == null || username.trim().isEmpty()) return false;
        if (username.length() < 3 || username.length() > 20) return false;
        if (password == null || password.length() < 6) return false;
        if (!password.equals(confirmPassword)) return false;
        if (!isValidEmail(email)) return false;
        if (!isValidPhoneNumber(phoneNumber)) return false;
        if (users.containsKey(username)) return false;

        User newUser = new User(username, password, email, phoneNumber);
        users.put(username, newUser);
        saveUsers();
        return true;
    }

    public User login(String username, String password) {
        if (!users.containsKey(username)) return null;
        User user = users.get(username);
        if (user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public void saveUsers() {
        DatabaseManager.saveUsers(users);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{10}");
    }
}

// =============== Hotel Manager ===============
class HotelManager {
    private ArrayList<Room> rooms;
    private ArrayList<Booking> bookings;
    private int bookingCounter;

    public HotelManager() {
        this.rooms = new ArrayList<>();
        this.bookings = DatabaseManager.loadBookings();
        initializeRooms();
        this.bookingCounter = bookings.isEmpty() ? 1 : bookings.get(bookings.size() - 1).getBookingId() + 1;
    }

    private void initializeRooms() {
        rooms.add(new Room(101, "Standard", 3000, "Comfortable room with basic amenities"));
        rooms.add(new Room(102, "Standard", 3000, "Comfortable room with basic amenities"));
        rooms.add(new Room(201, "Deluxe", 5000, "Spacious room with premium amenities"));
        rooms.add(new Room(202, "Deluxe", 5000, "Spacious room with premium amenities"));
        rooms.add(new Room(301, "Suite", 8000, "Luxury suite with separate living area"));
        rooms.add(new Room(302, "Suite", 8000, "Luxury suite with separate living area"));
    }

    public ArrayList<Room> getAvailableRooms() {
        ArrayList<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (r.isAvailable()) available.add(r);
        }
        return available;
    }

    public ArrayList<Room> getAllRooms() {
        return rooms;
    }

    public Room getRoomById(int roomId) {
        for (Room r : rooms) {
            if (r.getId() == roomId) return r;
        }
        return null;
    }

    public Booking bookRoom(String username, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        Room room = getRoomById(roomId);
        if (room == null || !room.isAvailable()) return null;

        for (Booking b : bookings) {
            if (b.getRoomId() == roomId && b.getStatus().equals("CONFIRMED")) {
                if (!(checkOutDate.isBefore(b.getCheckInDate()) || checkInDate.isAfter(b.getCheckOutDate()))) {
                    return null;
                }
            }
        }

        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (nights <= 0) return null;

        double totalPrice = room.getPrice() * nights;
        Booking booking = new Booking(bookingCounter++, username, roomId, room.getType(), checkInDate, checkOutDate, totalPrice);
        bookings.add(booking);
        room.setAvailable(false);
        saveBookings();
        return booking;
    }

    public boolean cancelBooking(int bookingId, String username) {
        for (Booking b : bookings) {
            if (b.getBookingId() == bookingId && b.getUsername().equals(username)) {
                b.setStatus("CANCELLED");
                Room room = getRoomById(b.getRoomId());
                if (room != null) room.setAvailable(true);
                saveBookings();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Booking> getUserBookings(String username) {
        ArrayList<Booking> userBookings = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getUsername().equals(username) && b.getStatus().equals("CONFIRMED")) {
                userBookings.add(b);
            }
        }
        return userBookings;
    }

    public ArrayList<Booking> getAllBookings() {
        return bookings;
    }

    public void saveBookings() {
        DatabaseManager.saveBookings(bookings);
    }
}

// =============== Main GUI Application ===============
public class HotelReservationSystem extends JFrame {
    private AuthenticationController authController;
    private HotelManager hotelManager;
    private User currentUser;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public HotelReservationSystem() {
        authController = new AuthenticationController();
        hotelManager = new HotelManager();

        setTitle("Hotel Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createSignUpPanel(), "SIGNUP");
        mainPanel.add(createDashboardPanel(), "DASHBOARD");
        mainPanel.add(createViewRoomsPanel(), "VIEW_ROOMS");
        mainPanel.add(createBookRoomPanel(), "BOOK_ROOM");
        mainPanel.add(createViewBookingsPanel(), "VIEW_BOOKINGS");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
        setVisible(true);
    }

    // =============== LOGIN PANEL ===============
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(25, 118, 211));
        headerPanel.setPreferredSize(new Dimension(1000, 80));
        JLabel titleLabel = new JLabel("Hotel Reservation System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(300, 35));
        gbc.gridx = 1;
        contentPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        contentPanel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBackground(new Color(25, 118, 211));
        loginButton.setForeground(Color.WHITE);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = authController.login(username, password);
            if (user != null) {
                currentUser = user;
                cardLayout.show(mainPanel, "DASHBOARD");
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + username, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));
        signUpButton.setPreferredSize(new Dimension(120, 40));
        signUpButton.setBackground(new Color(76, 175, 80));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            cardLayout.show(mainPanel, "SIGNUP");
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        contentPanel.add(buttonPanel, gbc);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // =============== SIGNUP PANEL ===============
    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(76, 175, 80));
        headerPanel.setPreferredSize(new Dimension(1000, 80));
        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameField.setPreferredSize(new Dimension(300, 30));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));

        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 12));

        JTextField phoneField = new JTextField();
        phoneField.setFont(new Font("Arial", Font.PLAIN, 12));

        addLabelAndField(contentPanel, gbc, "Username:", usernameField, 0);
        addLabelAndField(contentPanel, gbc, "Password:", passwordField, 1);
        addLabelAndField(contentPanel, gbc, "Confirm Password:", confirmPasswordField, 2);
        addLabelAndField(contentPanel, gbc, "Email:", emailField, 3);
        addLabelAndField(contentPanel, gbc, "Phone (10 digits):", phoneField, 4);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 14));
        signUpButton.setPreferredSize(new Dimension(120, 40));
        signUpButton.setBackground(new Color(76, 175, 80));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords don't match!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (authController.register(username, password, confirmPassword, email, phone)) {
                JOptionPane.showMessageDialog(this, "Account created successfully! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                emailField.setText("");
                phoneField.setText("");
                cardLayout.show(mainPanel, "LOGIN");
            } else {
                JOptionPane.showMessageDialog(this, "Sign up failed! Please check your inputs or username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(140, 40));
        backButton.setBackground(new Color(158, 158, 158));
        backButton.setForeground(Color.WHITE);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            emailField.setText("");
            phoneField.setText("");
            cardLayout.show(mainPanel, "LOGIN");
        });

        buttonPanel.add(signUpButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 15, 15, 15);
        contentPanel.add(buttonPanel, gbc);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // =============== DASHBOARD PANEL ===============
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 118, 211));
        headerPanel.setPreferredSize(new Dimension(1000, 100));

        JLabel titleLabel = new JLabel("Welcome to Hotel Reservation System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));

        JLabel userLabel = new JLabel();
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userLabel.setForeground(new Color(220, 220, 220));
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(25, 118, 211));
        textPanel.add(titleLabel);
        textPanel.add(userLabel);

        headerPanel.add(textPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JButton viewRoomsButton = createDashboardButton("View Available Rooms", new Color(76, 175, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(viewRoomsButton, gbc);
        viewRoomsButton.addActionListener(e -> cardLayout.show(mainPanel, "VIEW_ROOMS"));

        JButton bookRoomButton = createDashboardButton("Book a Room", new Color(255, 152, 0));
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPanel.add(bookRoomButton, gbc);
        bookRoomButton.addActionListener(e -> cardLayout.show(mainPanel, "BOOK_ROOM"));

        JButton viewBookingsButton = createDashboardButton("View My Bookings", new Color(33, 150, 243));
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(viewBookingsButton, gbc);
        viewBookingsButton.addActionListener(e -> cardLayout.show(mainPanel, "VIEW_BOOKINGS"));

        JButton logoutButton = createDashboardButton("Logout", new Color(244, 67, 54));
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(logoutButton, gbc);
        logoutButton.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "LOGIN");
        });

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        // Store userLabel reference for updating
        panel.putClientProperty("userLabel", userLabel);

        return panel;
    }

    // =============== VIEW ROOMS PANEL ===============
    private JPanel createViewRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 118, 211));
        headerPanel.setPreferredSize(new Dimension(1000, 60));

        JLabel titleLabel = new JLabel("Available Rooms");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBackground(new Color(25, 118, 211));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(new Color(245, 245, 245));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.putClientProperty("roomsContent", contentPanel);

        return panel;
    }

    // =============== BOOK ROOM PANEL ===============
    private JPanel createBookRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 152, 0));
        headerPanel.setPreferredSize(new Dimension(1000, 60));

        JLabel titleLabel = new JLabel("Book a Room");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBackground(new Color(255, 152, 0));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> roomComboBox = new JComboBox<>();
        roomComboBox.setFont(new Font("Arial", Font.PLAIN, 13));
        roomComboBox.setPreferredSize(new Dimension(300, 35));

        JSpinner checkInSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInSpinner, "yyyy-MM-dd");
        checkInSpinner.setEditor(checkInEditor);

        JSpinner checkOutSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutSpinner, "yyyy-MM-dd");
        checkOutSpinner.setEditor(checkOutEditor);

        addLabelAndComponent(contentPanel, gbc, "Select Room:", roomComboBox, 0);
        addLabelAndComponent(contentPanel, gbc, "Check-in Date:", checkInSpinner, 1);
        addLabelAndComponent(contentPanel, gbc, "Check-out Date:", checkOutSpinner, 2);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton bookButton = new JButton("Book Now");
        bookButton.setFont(new Font("Arial", Font.BOLD, 14));
        bookButton.setPreferredSize(new Dimension(120, 40));
        bookButton.setBackground(new Color(76, 175, 80));
        bookButton.setForeground(Color.WHITE);
        bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bookButton.addActionListener(e -> {
            if (roomComboBox.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Please select a room!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<Room> availableRooms = hotelManager.getAvailableRooms();
            Room selectedRoom = availableRooms.get(roomComboBox.getSelectedIndex());

            java.util.Date checkInDate = (java.util.Date) checkInSpinner.getValue();
            java.util.Date checkOutDate = (java.util.Date) checkOutSpinner.getValue();

            LocalDate checkIn = checkInDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOut = checkOutDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Booking booking = hotelManager.bookRoom(currentUser.getUsername(), selectedRoom.getId(), checkIn, checkOut);

            if (booking != null) {
                JOptionPane.showMessageDialog(this, "Room booked successfully!\n\n" + booking.toString(), "Success", JOptionPane.INFORMATION_MESSAGE);
                roomComboBox.removeAllItems();
                cardLayout.show(mainPanel, "DASHBOARD");
            } else {
                JOptionPane.showMessageDialog(this, "Booking failed! Room may not be available for selected dates.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(bookButton);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        contentPanel.add(buttonPanel, gbc);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        panel.putClientProperty("roomCombo", roomComboBox);

        return panel;
    }

    // =============== VIEW BOOKINGS PANEL ===============
    private JPanel createViewBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(1000, 60));

        JLabel titleLabel = new JLabel("My Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setBackground(new Color(33, 150, 243));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(new Color(245, 245, 245));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.putClientProperty("bookingsContent", contentPanel);

        return panel;
    }

    // =============== Helper Methods ===============
    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void addLabelAndComponent(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private JButton createDashboardButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        return button;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            updateAllPanels();
        }
    }

    private void updateAllPanels() {
        // Update Dashboard
        Component dashboardPanel = null;
        for (Component c : mainPanel.getComponents()) {
            if (((JPanel) c).getClientProperty("userLabel") != null) {
                dashboardPanel = c;
                break;
            }
        }
        if (dashboardPanel != null && currentUser != null) {
            JLabel userLabel = (JLabel) ((JPanel) dashboardPanel).getClientProperty("userLabel");
            userLabel.setText("Email: " + currentUser.getEmail() + " | Phone: " + currentUser.getPhoneNumber());
        }

        // Update View Rooms
        for (Component c : mainPanel.getComponents()) {
            JPanel p = (JPanel) c;
            if (p.getClientProperty("roomsContent") != null) {
                JPanel roomsContent = (JPanel) p.getClientProperty("roomsContent");
                roomsContent.removeAll();
                for (Room room : hotelManager.getAllRooms()) {
                    JPanel roomPanel = createRoomPanel(room);
                    roomsContent.add(roomPanel);
                }
                roomsContent.revalidate();
                roomsContent.repaint();
                break;
            }
        }

        // Update Book Room ComboBox
        for (Component c : mainPanel.getComponents()) {
            JPanel p = (JPanel) c;
            if (p.getClientProperty("roomCombo") != null) {
                JComboBox<String> roomCombo = (JComboBox<String>) p.getClientProperty("roomCombo");
                roomCombo.removeAllItems();
                ArrayList<Room> availableRooms = hotelManager.getAvailableRooms();
                for (Room room : availableRooms) {
                    roomCombo.addItem("Room #" + room.getId() + " - " + room.getType() + " (Rs." + room.getPrice() + "/night)");
                }
                break;
            }
        }

        // Update View Bookings
        for (Component c : mainPanel.getComponents()) {
            JPanel p = (JPanel) c;
            if (p.getClientProperty("bookingsContent") != null) {
                JPanel bookingsContent = (JPanel) p.getClientProperty("bookingsContent");
                bookingsContent.removeAll();

                if (currentUser != null) {
                    ArrayList<Booking> userBookings = hotelManager.getUserBookings(currentUser.getUsername());
                    if (userBookings.isEmpty()) {
                        JLabel noBookingsLabel = new JLabel("You have no bookings yet!");
                        noBookingsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
                        noBookingsLabel.setForeground(new Color(100, 100, 100));
                        bookingsContent.add(noBookingsLabel);
                    } else {
                        for (Booking booking : userBookings) {
                            JPanel bookingPanel = createBookingPanel(booking);
                            bookingsContent.add(bookingPanel);
                            bookingsContent.add(Box.createVerticalStrut(10));
                        }
                    }
                }
                bookingsContent.revalidate();
                bookingsContent.repaint();
                break;
            }
        }
    }

    private JPanel createRoomPanel(Room room) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(189, 189, 189), 1));
        panel.setPreferredSize(new Dimension(900, 80));
        panel.setMaximumSize(new Dimension(900, 80));

        JLabel infoLabel = new JLabel(room.toString());
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JLabel statusLabel = new JLabel(room.isAvailable() ? "✓ Available" : "✗ Booked");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 13));
        statusLabel.setForeground(room.isAvailable() ? new Color(76, 175, 80) : new Color(244, 67, 54));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15));

        panel.add(infoLabel, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createBookingPanel(Booking booking) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(189, 189, 189), 1));
        panel.setPreferredSize(new Dimension(900, 100));
        panel.setMaximumSize(new Dimension(900, 100));

        JLabel infoLabel = new JLabel(booking.toString());
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JButton cancelButton = new JButton("Cancel Booking");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setBackground(new Color(244, 67, 54));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setPreferredSize(new Dimension(130, 35));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to cancel booking #" + booking.getBookingId() + "?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                if (hotelManager.cancelBooking(booking.getBookingId(), currentUser.getUsername())) {
                    JOptionPane.showMessageDialog(this, "Booking cancelled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    updateAllPanels();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to cancel booking!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        panel.add(infoLabel, BorderLayout.CENTER);
        panel.add(cancelButton, BorderLayout.EAST);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelReservationSystem());
    }
}