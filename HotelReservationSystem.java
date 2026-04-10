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
    private int capacity;

    public Room(int id, String type, double price, String description, int capacity) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.description = description;
        this.capacity = capacity;
        this.available = true;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }
    public String getDescription() { return description; }
    public int getCapacity() { return capacity; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("Room #%d | Type: %s | Price: Rs.%.2f | Capacity: %d | Status: %s | %s",
                id, type, price, capacity, (available ? "Available" : "Booked"), description);
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
    private int numberOfGuests;

    public Booking(int bookingId, String username, int roomId, String roomType,
                   LocalDate checkInDate, LocalDate checkOutDate, double totalPrice, int numberOfGuests) {
        this.bookingId = bookingId;
        this.username = username;
        this.roomId = roomId;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.numberOfGuests = numberOfGuests;
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
    public int getNumberOfGuests() { return numberOfGuests; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return String.format("Booking #%d | User: %s | Room: %s (#%d) | Guests: %d | Check-in: %s | Check-out: %s | Total: Rs.%.2f | Status: %s",
                bookingId, username, roomType, roomId, numberOfGuests,
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
        rooms.add(new Room(101, "Standard", 3000, "Comfortable room with basic amenities", 2));
        rooms.add(new Room(102, "Standard", 3000, "Comfortable room with basic amenities", 2));
        rooms.add(new Room(201, "Deluxe", 5000, "Spacious room with premium amenities", 4));
        rooms.add(new Room(202, "Deluxe", 5000, "Spacious room with premium amenities", 4));
        rooms.add(new Room(301, "Suite", 8000, "Luxury suite with separate living area", 6));
        rooms.add(new Room(302, "Suite", 8000, "Luxury suite with separate living area", 6));
    }

    public ArrayList<Room> getAvailableRooms() {
        ArrayList<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (r.isAvailable()) available.add(r);
        }
        return available;
    }

    public ArrayList<Room> getAvailableRoomsByDatesAndGuests(LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        ArrayList<Room> available = new ArrayList<>();
        for (Room r : rooms) {
            if (r.getCapacity() >= numberOfGuests && isRoomAvailableForDates(r.getId(), checkInDate, checkOutDate)) {
                available.add(r);
            }
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

    private boolean isRoomAvailableForDates(int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        for (Booking b : bookings) {
            if (b.getRoomId() == roomId && b.getStatus().equals("CONFIRMED")) {
                if (!(checkOutDate.isBefore(b.getCheckInDate()) || checkInDate.isAfter(b.getCheckOutDate()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public Booking bookRoom(String username, int roomId, LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        Room room = getRoomById(roomId);
        if (room == null) return null;

        if (room.getCapacity() < numberOfGuests) return null;

        if (!isRoomAvailableForDates(roomId, checkInDate, checkOutDate)) return null;

        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (nights <= 0) return null;

        double totalPrice = room.getPrice() * nights;
        Booking booking = new Booking(bookingCounter++, username, roomId, room.getType(), checkInDate, checkOutDate, totalPrice, numberOfGuests);
        bookings.add(booking);
        saveBookings();

        return booking;
    }

    public boolean cancelBooking(int bookingId, String username) {
        for (Booking b : bookings) {
            if (b.getBookingId() == bookingId && b.getUsername().equals(username)) {
                b.setStatus("CANCELLED");
                saveBookings();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Booking> getUserBookings(String username) {
        ArrayList<Booking> userBookings = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getUsername().equals(username)) {
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
    private static final Color PRIMARY_COLOR = new Color(25, 118, 211);
    private static final Color SECONDARY_COLOR = new Color(76, 175, 80);
    private static final Color ACCENT_COLOR = new Color(255, 152, 0);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color CARD_COLOR = Color.WHITE;

    public HotelReservationSystem() {
        authController = new AuthenticationController();
        hotelManager = new HotelManager();

        setTitle("Hotel Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(BACKGROUND_COLOR);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createSignUpPanel(), "SIGNUP");
        mainPanel.add(createDashboardPanel(), "DASHBOARD");
        mainPanel.add(createViewRoomsPanel(), "VIEW_ROOMS");
        mainPanel.add(createBookRoomPanel(), "BOOK_ROOM");
        mainPanel.add(createViewBookingsPanel(), "VIEW_BOOKINGS");
        mainPanel.add(createBookingHistoryPanel(), "BOOKING_HISTORY");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
        setVisible(true);
    }

    // =============== LOGIN PANEL ===============
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1200, 100));
        JLabel titleLabel = new JLabel("Hotel Reservation System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel cardPanel = createCard(400, 300);
        cardPanel.setLayout(new GridBagLayout());

        JLabel loginLabel = new JLabel("Login to Your Account");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 20, 20, 20);
        cardPanel.add(loginLabel, gbc);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 20, 10, 20);
        cardPanel.add(createLabeledField("Username:", usernameField), gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        gbc.gridy = 2;
        cardPanel.add(createLabeledField("Password:", passwordField), gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(CARD_COLOR);

        JButton loginButton = createStyledButton("Login", PRIMARY_COLOR);
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
                updateAllPanels();
                cardLayout.show(mainPanel, "DASHBOARD");
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + username, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });

        JButton signUpButton = createStyledButton("Sign Up", SECONDARY_COLOR);
        signUpButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            cardLayout.show(mainPanel, "SIGNUP");
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 20, 15, 20);
        cardPanel.add(buttonPanel, gbc);

        contentPanel.add(cardPanel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // =============== SIGNUP PANEL ===============
    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(SECONDARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1200, 100));
        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JPanel cardPanel = createCard(450, 400);
        cardPanel.setLayout(new GridBagLayout());
        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(10, 20, 10, 20);
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        cardGbc.gridwidth = 2;

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameField.setPreferredSize(new Dimension(350, 35));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setPreferredSize(new Dimension(350, 35));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
        confirmPasswordField.setPreferredSize(new Dimension(350, 35));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 12));
        emailField.setPreferredSize(new Dimension(350, 35));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JTextField phoneField = new JTextField();
        phoneField.setFont(new Font("Arial", Font.PLAIN, 12));
        phoneField.setPreferredSize(new Dimension(350, 35));
        phoneField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        cardGbc.gridy = 0;
        cardPanel.add(createLabeledField("Username:", usernameField), cardGbc);
        cardGbc.gridy = 1;
        cardPanel.add(createLabeledField("Password:", passwordField), cardGbc);
        cardGbc.gridy = 2;
        cardPanel.add(createLabeledField("Confirm Password:", confirmPasswordField), cardGbc);
        cardGbc.gridy = 3;
        cardPanel.add(createLabeledField("Email:", emailField), cardGbc);
        cardGbc.gridy = 4;
        cardPanel.add(createLabeledField("Phone (10 digits):", phoneField), cardGbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(CARD_COLOR);

        JButton signUpButton = createStyledButton("Sign Up", SECONDARY_COLOR);
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

        JButton backButton = createStyledButton("Back to Login", new Color(158, 158, 158));
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

        cardGbc.gridy = 5;
        cardGbc.insets = new Insets(25, 20, 15, 20);
        cardPanel.add(buttonPanel, cardGbc);

        contentPanel.add(cardPanel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // =============== DASHBOARD PANEL ===============
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1200, 120));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel();
        userLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        userLabel.setForeground(new Color(220, 220, 220));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(PRIMARY_COLOR);
        textPanel.add(titleLabel);
        textPanel.add(userLabel);

        headerPanel.add(textPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel viewRoomsCard = createDashboardCard("View Available Rooms", SECONDARY_COLOR, e -> {
            updateAllPanels();
            cardLayout.show(mainPanel, "VIEW_ROOMS");
        });
        JPanel bookRoomCard = createDashboardCard("Book a Room", ACCENT_COLOR, e -> cardLayout.show(mainPanel, "BOOK_ROOM"));
        JPanel viewBookingsCard = createDashboardCard("View My Bookings", PRIMARY_COLOR, e -> {
            updateAllPanels();
            cardLayout.show(mainPanel, "VIEW_BOOKINGS");
        });
        JPanel bookingHistoryCard = createDashboardCard("Booking History", new Color(103, 58, 183), e -> {
            updateAllPanels();
            cardLayout.show(mainPanel, "BOOKING_HISTORY");
        });

        contentPanel.add(viewRoomsCard);
        contentPanel.add(bookRoomCard);
        contentPanel.add(viewBookingsCard);
        contentPanel.add(bookingHistoryCard);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        footerPanel.setBackground(BACKGROUND_COLOR);

        JButton logoutButton = createStyledButton("Logout", DANGER_COLOR);
        logoutButton.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "LOGIN");
        });

        footerPanel.add(logoutButton);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.add(contentPanel, BorderLayout.CENTER);
        mainContent.add(footerPanel, BorderLayout.SOUTH);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(mainContent, BorderLayout.CENTER);

        panel.putClientProperty("userLabel", userLabel);

        return panel;
    }

    // =============== VIEW ROOMS PANEL ===============
    private JPanel createViewRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1200, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Available Rooms");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        JButton backButton = createStyledButton("← Back", PRIMARY_COLOR);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.putClientProperty("roomsContent", contentPanel);

        return panel;
    }

    // =============== BOOK ROOM PANEL ===============
    private JPanel createBookRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ACCENT_COLOR);
        headerPanel.setPreferredSize(new Dimension(1200, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Book a Room");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        JButton backButton = createStyledButton("← Back", ACCENT_COLOR);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel cardPanel = createCard(600, 500);
        cardPanel.setLayout(new GridBagLayout());
        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(15, 30, 15, 30);
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        cardGbc.gridwidth = 2;

        JComboBox<String> roomComboBox = new JComboBox<>();
        roomComboBox.setFont(new Font("Arial", Font.PLAIN, 13));
        roomComboBox.setPreferredSize(new Dimension(400, 40));
        roomComboBox.setMaximumRowCount(10);

        JSpinner checkInSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInSpinner, "yyyy-MM-dd");
        checkInSpinner.setEditor(checkInEditor);

        JSpinner checkOutSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutSpinner, "yyyy-MM-dd");
        checkOutSpinner.setEditor(checkOutEditor);

        JSpinner guestsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        guestsSpinner.setFont(new Font("Arial", Font.PLAIN, 13));

        cardGbc.gridy = 0;
        cardPanel.add(createLabeledComponent("Select Room:", roomComboBox), cardGbc);
        cardGbc.gridy = 1;
        cardPanel.add(createLabeledComponent("Check-in Date:", checkInSpinner), cardGbc);
        cardGbc.gridy = 2;
        cardPanel.add(createLabeledComponent("Check-out Date:", checkOutSpinner), cardGbc);
        cardGbc.gridy = 3;
        cardPanel.add(createLabeledComponent("Number of Guests:", guestsSpinner), cardGbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(CARD_COLOR);

        JButton bookButton = createStyledButton("Book Now", SECONDARY_COLOR);
        bookButton.addActionListener(e -> {
            if (roomComboBox.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Please select a room!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<Room> availableRooms = hotelManager.getAvailableRoomsByDatesAndGuests(
                    convertToLocalDate((java.util.Date) checkInSpinner.getValue()),
                    convertToLocalDate((java.util.Date) checkOutSpinner.getValue()),
                    (Integer) guestsSpinner.getValue()
            );
            Room selectedRoom = availableRooms.get(roomComboBox.getSelectedIndex());

            java.util.Date checkInDate = (java.util.Date) checkInSpinner.getValue();
            java.util.Date checkOutDate = (java.util.Date) checkOutSpinner.getValue();

            LocalDate checkIn = convertToLocalDate(checkInDate);
            LocalDate checkOut = convertToLocalDate(checkOutDate);

            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int numberOfGuests = (Integer) guestsSpinner.getValue();

            Booking booking = hotelManager.bookRoom(currentUser.getUsername(), selectedRoom.getId(), checkIn, checkOut, numberOfGuests);

            if (booking != null) {
                JOptionPane.showMessageDialog(this, "Room booked successfully!\n\n" + booking.toString(), "Success", JOptionPane.INFORMATION_MESSAGE);
                roomComboBox.removeAllItems();
                cardLayout.show(mainPanel, "DASHBOARD");
            } else {
                JOptionPane.showMessageDialog(this, "Booking failed! Room may not be available for selected dates or capacity exceeded.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(bookButton);

        cardGbc.gridy = 4;
        cardGbc.insets = new Insets(30, 30, 15, 30);
        cardPanel.add(buttonPanel, cardGbc);

        contentPanel.add(cardPanel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        panel.putClientProperty("roomCombo", roomComboBox);

        return panel;
    }

    // =============== VIEW BOOKINGS PANEL ===============
    private JPanel createViewBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(1200, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("My Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        JButton backButton = createStyledButton("← Back", PRIMARY_COLOR);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.putClientProperty("bookingsContent", contentPanel);

        return panel;
    }

    // =============== BOOKING HISTORY PANEL ===============
    private JPanel createBookingHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(103, 58, 183));
        headerPanel.setPreferredSize(new Dimension(1200, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Booking History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);

        JButton backButton = createStyledButton("← Back", new Color(103, 58, 183));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.putClientProperty("historyContent", contentPanel);

        return panel;
    }

    // =============== Helper Methods ===============
    private JPanel createCard(int width, int height) {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        panel.setMaximumSize(new Dimension(width, height));
        return panel;
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(CARD_COLOR);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setPreferredSize(new Dimension(120, 35));
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLabeledComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(CARD_COLOR);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setPreferredSize(new Dimension(150, 40));
        panel.add(label, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(140, 40));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        return button;
    }

    private JPanel createDashboardCard(String title, Color color, ActionListener action) {
        JPanel card = createCard(400, 180);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(color, 2));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(color);
        titlePanel.setPreferredSize(new Dimension(400, 60));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        JButton actionButton = new JButton("Access");
        actionButton.setFont(new Font("Arial", Font.BOLD, 14));
        actionButton.setBackground(color);
        actionButton.setForeground(Color.WHITE);
        actionButton.setPreferredSize(new Dimension(120, 40));
        actionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionButton.setFocusPainted(false);
        actionButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        actionButton.addActionListener(action);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        buttonPanel.add(actionButton, BorderLayout.CENTER);

        card.add(titlePanel, BorderLayout.NORTH);
        card.add(buttonPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createRoomCard(Room room) {
        JPanel panel = createCard(900, 100);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(CARD_COLOR);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JLabel roomLabel = new JLabel(room.toString());
        roomLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        infoPanel.add(roomLabel);

        JLabel statusLabel = new JLabel(room.isAvailable() ? "✓ Available" : "✗ Booked");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 13));
        statusLabel.setForeground(room.isAvailable() ? SECONDARY_COLOR : DANGER_COLOR);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        infoPanel.add(statusLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBookingCard(Booking booking) {
        JPanel panel = createCard(900, 120);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(CARD_COLOR);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JLabel bookingLabel = new JLabel(booking.toString());
        bookingLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel statusLabel = new JLabel("Status: " + booking.getStatus());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(booking.getStatus().equals("CONFIRMED") ? SECONDARY_COLOR : DANGER_COLOR);

        infoPanel.add(bookingLabel);
        infoPanel.add(statusLabel);

        JButton cancelButton = createStyledButton("Cancel", DANGER_COLOR);
        cancelButton.setPreferredSize(new Dimension(120, 35));
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(cancelButton);

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private LocalDate convertToLocalDate(java.util.Date date) {
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }

    private void updateAllPanels() {
        if (currentUser == null) return;

        // Update Dashboard
        for (Component c : mainPanel.getComponents()) {
            JPanel p = (JPanel) c;
            if (p.getClientProperty("userLabel") != null) {
                JLabel userLabel = (JLabel) p.getClientProperty("userLabel");
                userLabel.setText("Email: " + currentUser.getEmail() + " | Phone: " + currentUser.getPhoneNumber());
                break;
            }
        }

        // Update View Rooms
        for (Component c : mainPanel.getComponents()) {
            JPanel p = (JPanel) c;
            if (p.getClientProperty("roomsContent") != null) {
                JPanel roomsContent = (JPanel) p.getClientProperty("roomsContent");
                roomsContent.removeAll();
                for (Room room : hotelManager.getAllRooms()) {
                    JPanel roomCard = createRoomCard(room);
                    roomsContent.add(roomCard);
                    roomsContent.add(Box.createVerticalStrut(10));
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
                    roomCombo.addItem("Room #" + room.getId() + " - " + room.getType() + " - Capacity: " + room.getCapacity() + " | Rs." + room.getPrice() + "/night");
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

                ArrayList<Booking> userBookings = hotelManager.getUserBookings(currentUser.getUsername());
                ArrayList<Booking> activeBookings = new ArrayList<>();
                for (Booking b : userBookings) {
                    if (b.getStatus().equals("CONFIRMED")) {
                        activeBookings.add(b);
                    }
                }

                if (activeBookings.isEmpty()) {
                    JLabel noBookingsLabel = new JLabel("You have no active bookings!");
                    noBookingsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
                    noBookingsLabel.setForeground(new Color(100, 100, 100));
                    bookingsContent.add(noBookingsLabel);
                } else {
                    for (Booking booking : activeBookings) {
                        JPanel bookingCard = createBookingCard(booking);
                        bookingsContent.add(bookingCard);
                        bookingsContent.add(Box.createVerticalStrut(10));
                    }
                }
                bookingsContent.revalidate();
                bookingsContent.repaint();
                break;
            }
        }

        // Update Booking History
        for (Component c : mainPanel.getComponents()) {
            JPanel p = (JPanel) c;
            if (p.getClientProperty("historyContent") != null) {
                JPanel historyContent = (JPanel) p.getClientProperty("historyContent");
                historyContent.removeAll();

                ArrayList<Booking> allUserBookings = hotelManager.getUserBookings(currentUser.getUsername());
                ArrayList<Booking> allBookings = new ArrayList<>(allUserBookings);
                for (Booking b : hotelManager.getAllBookings()) {
                    if (b.getUsername().equals(currentUser.getUsername()) && b.getStatus().equals("CANCELLED")) {
                        allBookings.add(b);
                    }
                }

                if (allBookings.isEmpty()) {
                    JLabel noHistoryLabel = new JLabel("You have no booking history!");
                    noHistoryLabel.setFont(new Font("Arial", Font.ITALIC, 16));
                    noHistoryLabel.setForeground(new Color(100, 100, 100));
                    historyContent.add(noHistoryLabel);
                } else {
                    for (Booking booking : allBookings) {
                        JPanel bookingCard = createBookingHistoryCard(booking);
                        historyContent.add(bookingCard);
                        historyContent.add(Box.createVerticalStrut(10));
                    }
                }
                historyContent.revalidate();
                historyContent.repaint();
                break;
            }
        }
    }

    private JPanel createBookingHistoryCard(Booking booking) {
        JPanel panel = createCard(900, 110);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(CARD_COLOR);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JLabel bookingLabel = new JLabel(booking.toString());
        bookingLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel statusLabel = new JLabel("Status: " + booking.getStatus());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(booking.getStatus().equals("CONFIRMED") ? SECONDARY_COLOR : DANGER_COLOR);

        infoPanel.add(bookingLabel);
        infoPanel.add(statusLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelReservationSystem());
    }
}