import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.*;

// =============== Colors Theme ===============
class ThemeColors {
    static final Color PRIMARY = new Color(26, 115, 232);
    static final Color PRIMARY_DARK = new Color(25, 103, 210);
    static final Color PRIMARY_LIGHT = new Color(227, 242, 253);
    static final Color SECONDARY = new Color(52, 168, 83);
    static final Color SUCCESS = new Color(52, 168, 83);
    static final Color WARNING = new Color(251, 188, 4);
    static final Color DANGER = new Color(234, 67, 54);
    static final Color INFO = new Color(3, 155, 229);
    static final Color ADMIN = new Color(156, 39, 172);
    
    static final Color WHITE = new Color(255, 255, 255);
    static final Color LIGHT_GRAY = new Color(248, 249, 250);
    static final Color GRAY = new Color(189, 189, 189);
    static final Color DARK_GRAY = new Color(97, 97, 97);
    static final Color TEXT_DARK = new Color(32, 33, 36);
    static final Color BG_LIGHT = new Color(245, 245, 245);
    static final Color CARD_BG = new Color(255, 255, 255);
}

// =============== Rounded Border ===============
class RoundedBorder extends AbstractBorder {
    private int radius;
    private Color color;
    private int thickness;

    public RoundedBorder(int radius, Color color, int thickness) {
        this.radius = radius;
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }
}

// =============== Rounded Panel ===============
class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Color backgroundColor;

    public RoundedPanel(int radius) {
        this.cornerRadius = radius;
        this.backgroundColor = ThemeColors.CARD_BG;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        
        g2d.setColor(new Color(0, 0, 0, 15));
        g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, cornerRadius, cornerRadius);
        
        super.paintComponent(g);
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }
}

// =============== Gradient Panel ===============
class GradientPanel extends JPanel {
    private Color color1;
    private Color color2;

    public GradientPanel(Color c1, Color c2) {
        this.color1 = c1;
        this.color2 = c2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}

// =============== Modern Button ===============
class ModernButton extends JButton {
    private Color defaultColor;
    private Color hoverColor;
    private Color pressedColor;
    private int radius = 10;

    public ModernButton(String text, Color color) {
        super(text);
        this.defaultColor = color;
        this.hoverColor = color.darker();
        this.pressedColor = color.darker().darker();
        
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(defaultColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(140, 45));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(defaultColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
    }
}

// =============== User Class ===============
class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private long createdAt;
    private boolean isAdmin;

    public User(String username, String password, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = System.currentTimeMillis();
        this.isAdmin = false;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public long getCreatedAt() { return createdAt; }
    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setPassword(String password) { this.password = password; }
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
    public void setType(String type) { this.type = type; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

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
    private static final String ROOMS_FILE = "data/rooms.dat";
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

    public static void saveRooms(ArrayList<Room> rooms) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ROOMS_FILE))) {
            oos.writeObject(rooms);
        } catch (IOException e) {
            System.err.println("Error saving rooms: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Room> loadRooms() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ROOMS_FILE))) {
            return (ArrayList<Room>) ois.readObject();
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
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        if (!users.containsKey("admin")) {
            User adminUser = new User("admin", "admin123", "admin@hotel.com", "9999999999");
            adminUser.setAdmin(true);
            users.put("admin", adminUser);
            saveUsers();
        }
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
        this.rooms = DatabaseManager.loadRooms();
        this.bookings = DatabaseManager.loadBookings();
        
        if (rooms.isEmpty()) {
            initializeRooms();
        }
        
        this.bookingCounter = bookings.isEmpty() ? 1 : bookings.get(bookings.size() - 1).getBookingId() + 1;
    }

    private void initializeRooms() {
        rooms.add(new Room(101, "Standard", 3000, "Comfortable room with basic amenities", 2));
        rooms.add(new Room(102, "Standard", 3000, "Comfortable room with basic amenities", 2));
        rooms.add(new Room(201, "Deluxe", 5000, "Spacious room with premium amenities", 4));
        rooms.add(new Room(202, "Deluxe", 5000, "Spacious room with premium amenities", 4));
        rooms.add(new Room(301, "Suite", 8000, "Luxury suite with separate living area", 6));
        rooms.add(new Room(302, "Suite", 8000, "Luxury suite with separate living area", 6));
        saveRooms();
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

    public void saveRooms() {
        DatabaseManager.saveRooms(rooms);
    }

    public boolean addRoom(Room room) {
        for (Room r : rooms) {
            if (r.getId() == room.getId()) return false;
        }
        rooms.add(room);
        saveRooms();
        return true;
    }

    public boolean removeRoom(int roomId) {
        for (Booking b : bookings) {
            if (b.getRoomId() == roomId && b.getStatus().equals("CONFIRMED")) {
                return false;
            }
        }
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId() == roomId) {
                rooms.remove(i);
                saveRooms();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Booking> searchBookings(String query) {
        ArrayList<Booking> results = new ArrayList<>();
        query = query.toLowerCase().trim();

        for (Booking b : bookings) {
            if (b.getUsername().toLowerCase().contains(query) ||
                String.valueOf(b.getBookingId()).contains(query) ||
                String.valueOf(b.getRoomId()).contains(query) ||
                b.getRoomType().toLowerCase().contains(query) ||
                b.getStatus().toLowerCase().contains(query)) {
                results.add(b);
            }
        }
        return results;
    }

    public int getTotalBookings() {
        return bookings.size();
    }

    public int getConfirmedBookings() {
        int count = 0;
        for (Booking b : bookings) {
            if (b.getStatus().equals("CONFIRMED")) count++;
        }
        return count;
    }

    public double getTotalRevenue() {
        double total = 0;
        for (Booking b : bookings) {
            if (b.getStatus().equals("CONFIRMED")) total += b.getTotalPrice();
        }
        return total;
    }

    public double getOccupancyRate() {
        if (rooms.isEmpty()) return 0;
        int bookedRooms = 0;
        for (Room r : rooms) {
            if (!r.isAvailable()) bookedRooms++;
        }
        return (bookedRooms * 100.0) / rooms.size();
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
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setResizable(true);
        setBackground(ThemeColors.BG_LIGHT);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(ThemeColors.BG_LIGHT);

        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createSignUpPanel(), "SIGNUP");
        mainPanel.add(createDashboardPanel(), "DASHBOARD");
        mainPanel.add(createViewRoomsPanel(), "VIEW_ROOMS");
        mainPanel.add(createBookRoomPanel(), "BOOK_ROOM");
        mainPanel.add(createMyBookingsPanel(), "MY_BOOKINGS");
        mainPanel.add(createSpecialOffersPanel(), "SPECIAL_OFFERS");
        mainPanel.add(createProfilePanel(), "PROFILE");
        mainPanel.add(createAdminDashboard(), "ADMIN_DASHBOARD");
        mainPanel.add(createManageRoomsPanel(), "MANAGE_ROOMS");
        mainPanel.add(createViewAllBookingsPanel(), "VIEW_ALL_BOOKINGS");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColors.BG_LIGHT);

        JPanel headerPanel = new GradientPanel(ThemeColors.PRIMARY, ThemeColors.PRIMARY_DARK);
        headerPanel.setPreferredSize(new Dimension(1400, 150));
        headerPanel.setLayout(new BorderLayout());

        JLabel logoLabel = new JLabel("🏨 HOTEL HUB");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 52));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setOpaque(false);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 50, 5, 0));

        JLabel taglineLabel = new JLabel("Premium Hotel Reservation System");
        taglineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        taglineLabel.setForeground(new Color(240, 240, 240));
        taglineLabel.setOpaque(false);
        taglineLabel.setBorder(BorderFactory.createEmptyBorder(0, 50, 30, 0));

        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(logoLabel);
        titlePanel.add(taglineLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(ThemeColors.BG_LIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);

        RoundedPanel cardPanel = new RoundedPanel(20);
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setPreferredSize(new Dimension(450, 420));

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(15, 30, 15, 30);
        cardGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginLabel = new JLabel("👤 Login");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        loginLabel.setForeground(ThemeColors.PRIMARY);
        cardGbc.gridx = 0;
        cardGbc.gridy = 0;
        cardGbc.gridwidth = 2;
        cardGbc.insets = new Insets(20, 30, 30, 30);
        cardPanel.add(loginLabel, cardGbc);

        JTextField usernameField = createModernTextField("Username");
        cardGbc.gridx = 0;
        cardGbc.gridy = 1;
        cardGbc.gridwidth = 2;
        cardGbc.insets = new Insets(10, 30, 10, 30);
        cardPanel.add(usernameField, cardGbc);

        JPasswordField passwordField = createModernPasswordField("Password");
        cardGbc.gridy = 2;
        cardPanel.add(passwordField, cardGbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(ThemeColors.CARD_BG);

        ModernButton loginButton = new ModernButton("Login", ThemeColors.PRIMARY);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                showCustomMessage("Please fill all fields!", "Warning");
                return;
            }

            User user = authController.login(username, password);
            if (user != null) {
                currentUser = user;
                updateAllPanels();
                if (user.isAdmin()) {
                    cardLayout.show(mainPanel, "ADMIN_DASHBOARD");
                } else {
                    cardLayout.show(mainPanel, "DASHBOARD");
                }
                showCustomMessage("Welcome, " + username + "!", "Success");
            } else {
                showCustomMessage("Invalid credentials!", "Error");
                passwordField.setText("");
            }
        });

        ModernButton signUpButton = new ModernButton("Sign Up", ThemeColors.SUCCESS);
        signUpButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            cardLayout.show(mainPanel, "SIGNUP");
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        cardGbc.gridx = 0;
        cardGbc.gridy = 3;
        cardGbc.gridwidth = 2;
        cardGbc.insets = new Insets(30, 30, 20, 30);
        cardPanel.add(buttonPanel, cardGbc);

        contentPanel.add(cardPanel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColors.BG_LIGHT);

        JPanel headerPanel = new GradientPanel(ThemeColors.SUCCESS, new Color(76, 175, 83));
        headerPanel.setPreferredSize(new Dimension(1400, 120));
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("✏️ Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 44));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 0));

        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(ThemeColors.BG_LIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);

        RoundedPanel cardPanel = new RoundedPanel(20);
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setPreferredSize(new Dimension(500, 500));

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(10, 30, 10, 30);
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        cardGbc.gridwidth = 2;

        JTextField usernameField = createModernTextField("Username");
        JPasswordField passwordField = createModernPasswordField("Password");
        JPasswordField confirmPasswordField = createModernPasswordField("Confirm Password");
        JTextField emailField = createModernTextField("Email");
        JTextField phoneField = createModernTextField("Phone (10 digits)");

        cardGbc.gridy = 0;
        cardPanel.add(usernameField, cardGbc);
        cardGbc.gridy = 1;
        cardPanel.add(passwordField, cardGbc);
        cardGbc.gridy = 2;
        cardPanel.add(confirmPasswordField, cardGbc);
        cardGbc.gridy = 3;
        cardPanel.add(emailField, cardGbc);
        cardGbc.gridy = 4;
        cardPanel.add(phoneField, cardGbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(ThemeColors.CARD_BG);

        ModernButton signUpButton = new ModernButton("Sign Up", ThemeColors.SUCCESS);
        signUpButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                showCustomMessage("Please fill all fields!", "Warning");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showCustomMessage("Passwords don't match!", "Warning");
                return;
            }

            if (authController.register(username, password, confirmPassword, email, phone)) {
                showCustomMessage("Account created! You can now login.", "Success");
                usernameField.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                emailField.setText("");
                phoneField.setText("");
                cardLayout.show(mainPanel, "LOGIN");
            } else {
                showCustomMessage("Sign up failed! Check inputs or username exists.", "Error");
            }
        });

        ModernButton backButton = new ModernButton("← Back", ThemeColors.GRAY);
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
        cardGbc.insets = new Insets(25, 30, 20, 30);
        cardPanel.add(buttonPanel, cardGbc);

        contentPanel.add(cardPanel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColors.BG_LIGHT);

        JPanel headerPanel = new GradientPanel(ThemeColors.PRIMARY, ThemeColors.PRIMARY_DARK);
        headerPanel.setPreferredSize(new Dimension(1400, 160));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("📊 Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);

        JLabel userLabel = new JLabel();
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(240, 240, 240));
        userLabel.setOpaque(false);

        JPanel textPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(userLabel);

        headerPanel.add(textPanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(2, 3, 25, 25));
        contentPanel.setBackground(ThemeColors.BG_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel viewRoomsCard = createDashboardCard("🛏️ View Rooms", "Browse Available Rooms", ThemeColors.SECONDARY, e -> {
            updateAllPanels();
            cardLayout.show(mainPanel, "VIEW_ROOMS");
        });
        JPanel bookRoomCard = createDashboardCard("📅 Book Room", "Reserve Your Room", ThemeColors.WARNING, e -> cardLayout.show(mainPanel, "BOOK_ROOM"));
        JPanel myBookingsCard = createDashboardCard("📝 My Bookings", "View Active Bookings", ThemeColors.PRIMARY, e -> {
            updateAllPanels();
            cardLayout.show(mainPanel, "MY_BOOKINGS");
        });
        JPanel offersCard = createDashboardCard("🎁 Special Offers", "View Discounts & Deals", ThemeColors.WARNING, e -> {
            updateAllPanels();
            cardLayout.show(mainPanel, "SPECIAL_OFFERS");
        });
        JPanel profileCard = createDashboardCard("👤 My Profile", "Edit Profile Info", ThemeColors.INFO, e -> {
            updateAllPanels();
            cardLayout.show(mainPanel, "PROFILE");
        });
        JPanel helpCard = createDashboardCard("❓ Help & Support", "Contact Us", new Color(233, 30, 99), e -> {
            JOptionPane.showMessageDialog(this, 
                "📞 Support Contact:\n\n" +
                "Email: support@hotelHub.com\n" +
                "Phone: +91-800-HOTEL-HUB\n" +
                "WhatsApp: +91-9876543210\n\n" +
                "Hours: 24/7 Customer Support",
                "Help & Support",
                JOptionPane.INFORMATION_MESSAGE);
        });

        contentPanel.add(viewRoomsCard);
        contentPanel.add(bookRoomCard);
        contentPanel.add(myBookingsCard);
        contentPanel.add(offersCard);
        contentPanel.add(profileCard);
        contentPanel.add(helpCard);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        footerPanel.setBackground(ThemeColors.BG_LIGHT);

        ModernButton logoutButton = new ModernButton("🚪 Logout", ThemeColors.DANGER);
        logoutButton.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "LOGIN");
        });

        footerPanel.add(logoutButton);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(ThemeColors.BG_LIGHT);
        mainContent.add(contentPanel, BorderLayout.CENTER);
        mainContent.add(footerPanel, BorderLayout.SOUTH);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(mainContent, BorderLayout.CENTER);

        panel.putClientProperty("userLabel", userLabel);

        return panel;
    }

    private JPanel createAdminDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColors.BG_LIGHT);

        JPanel headerPanel = new GradientPanel(ThemeColors.ADMIN, new Color(142, 36, 170));
        headerPanel.setPreferredSize(new Dimension(1400, 200));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("👑 Admin Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);

        JLabel subtitleLabel = new JLabel("System Administration & Analytics");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(240, 240, 240));
        subtitleLabel.setOpaque(false);

        JPanel textPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        headerPanel.add(textPanel, BorderLayout.WEST);

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        statsPanel.add(createStatCard("📊 Total Bookings", "0", ThemeColors.INFO));
        statsPanel.add(createStatCard("✅ Confirmed", "0", ThemeColors.SUCCESS));
        statsPanel.add(createStatCard("💰 Revenue", "Rs.0", ThemeColors.WARNING));
        statsPanel.add(createStatCard("🏨 Occupancy", "0%", ThemeColors.DANGER));

        headerPanel.add(statsPanel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(1, 2, 25, 25));
        contentPanel.setBackground(ThemeColors.BG_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel manageRoomsCard = createDashboardCard("🔧 Manage Rooms", "Add/Remove Rooms", ThemeColors.SECONDARY, e -> {
            updateAllPanels();
            cardLayout.show(mainPanel, "MANAGE_ROOMS");
        });
        JPanel viewBookingsCard = createDashboardCard("📋 All Bookings", "System Bookings", ThemeColors.PRIMARY, e -> {
            updateAllPanels();
            cardLayout.show(mainPanel, "VIEW_ALL_BOOKINGS");
        });

        contentPanel.add(manageRoomsCard);
        contentPanel.add(viewBookingsCard);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        footerPanel.setBackground(ThemeColors.BG_LIGHT);

        ModernButton logoutButton = new ModernButton("🚪 Logout", ThemeColors.DANGER);
        logoutButton.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "LOGIN");
        });

        footerPanel.add(logoutButton);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(ThemeColors.BG_LIGHT);
        mainContent.add(contentPanel, BorderLayout.CENTER);
        mainContent.add(footerPanel, BorderLayout.SOUTH);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(mainContent, BorderLayout.CENTER);

        panel.putClientProperty("statsPanel", statsPanel);

        return panel;
    }

    private JPanel createViewRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColors.BG_LIGHT);

        JPanel headerPanel = new GradientPanel(ThemeColors.PRIMARY, ThemeColors.PRIMARY_DARK);
        headerPanel.setPreferredSize(new Dimension(1400, 90));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("🛏️ Available Rooms");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        ModernButton backButton = new ModernButton("← Back", ThemeColors.PRIMARY);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ThemeColors.BG_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(ThemeColors.BG_LIGHT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.putClientProperty("roomsContent", contentPanel);

        return panel;
    }

    private JPanel createBookRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColors.BG_LIGHT);

        JPanel headerPanel = new GradientPanel(ThemeColors.WARNING, new Color(245, 180, 0));
        headerPanel.setPreferredSize(new Dimension(1400, 90));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("📅 Book Your Room");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        ModernButton backButton = new ModernButton("← Back", ThemeColors.WARNING);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(ThemeColors.BG_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        RoundedPanel cardPanel = new RoundedPanel(20);
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setPreferredSize(new Dimension(650, 550));

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(15, 30, 15, 30);
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        cardGbc.gridwidth = 2;

        JComboBox<String> roomComboBox = createModernComboBox();
        JSpinner checkInSpinner = createModernDateSpinner();
        JSpinner checkOutSpinner = createModernDateSpinner();
        JSpinner guestsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        guestsSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));

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
        buttonPanel.setBackground(ThemeColors.CARD_BG);

        ModernButton bookButton = new ModernButton("✅ Book Now", ThemeColors.SUCCESS);
        bookButton.addActionListener(e -> {
            if (roomComboBox.getSelectedIndex() == -1) {
                showCustomMessage("Please select a room!", "Error");
                return;
            }

            ArrayList<Room> availableRooms = hotelManager.getAvailableRoomsByDatesAndGuests(
                    convertToLocalDate((java.util.Date) checkInSpinner.getValue()),
                    convertToLocalDate((java.util.Date) checkOutSpinner.getValue()),
                    (Integer) guestsSpinner.getValue()
            );
            
            if (availableRooms.isEmpty()) {
                showCustomMessage("No rooms available!", "Error");
                return;
            }
            
            Room selectedRoom = availableRooms.get(roomComboBox.getSelectedIndex());

            java.util.Date checkInDate = (java.util.Date) checkInSpinner.getValue();
            java.util.Date checkOutDate = (java.util.Date) checkOutSpinner.getValue();

            LocalDate checkIn = convertToLocalDate(checkInDate);
            LocalDate checkOut = convertToLocalDate(checkOutDate);

            if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
                showCustomMessage("Check-out date must be after check-in!", "Error");
                return;
            }

            int numberOfGuests = (Integer) guestsSpinner.getValue();

            Booking booking = hotelManager.bookRoom(currentUser.getUsername(), selectedRoom.getId(), checkIn, checkOut, numberOfGuests);

            if (booking != null) {
                showCustomMessage("✅ Room booked successfully!", "Success");
                roomComboBox.removeAllItems();
                cardLayout.show(mainPanel, "DASHBOARD");
            } else {
                showCustomMessage("Booking failed!", "Error");
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

    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColors.BG_LIGHT);

        JPanel headerPanel = new GradientPanel(ThemeColors.PRIMARY, ThemeColors.PRIMARY_DARK);
        headerPanel.setPreferredSize(new Dimension(1400, 90));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("📝 My Bookings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        ModernButton backButton = new ModernButton("← Back", ThemeColors.PRIMARY);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ThemeColors.BG_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(ThemeColors.BG_LIGHT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.putClientProperty("bookingsContent", contentPanel);

        return panel;
    }

    private JPanel createSpecialOffersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColors.BG_LIGHT);

        JPanel headerPanel = new GradientPanel(ThemeColors.WARNING, new Color(245, 180, 0));
        headerPanel.setPreferredSize(new Dimension(1400, 90));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("🎁 Special Offers & Deals");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        ModernButton backButton = new ModernButton("← Back", ThemeColors.WARNING);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(2, 2, 25, 25));
        contentPanel.setBackground(ThemeColors.BG_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        contentPanel.add(createOfferCard("🎉 Summer Discount", "Get 30% off on all rooms", "Valid till 31st Aug", ThemeColors.DANGER));
        contentPanel.add(createOfferCard("👨‍👩‍👧‍👦 Family Package", "Book 3+ rooms, get 20% off", "Valid till 30th Sept", ThemeColors.INFO));
        contentPanel.add(createOfferCard("💑 Couple's Retreat", "Get complimentary breakfast", "Valid till 15th Dec", ThemeColors.PRIMARY));
        contentPanel.add(createOfferCard("🎓 Student Special", "Valid ID required - 25% off", "Valid till 31st Oct", ThemeColors.SECONDARY));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColors.BG_LIGHT);

        JPanel headerPanel = new GradientPanel(ThemeColors.INFO, new Color(0, 150, 220));
        headerPanel.setPreferredSize(new Dimension(1400, 90));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("👤 My Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        ModernButton backButton = new ModernButton("← Back", ThemeColors.INFO);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(ThemeColors.BG_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        RoundedPanel cardPanel = new RoundedPanel(20);
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setPreferredSize(new Dimension(500, 450));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 30, 15, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        JLabel profileTitle = new JLabel("👤 Profile Information");
        profileTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        profileTitle.setForeground(ThemeColors.PRIMARY);
        gbc.gridy = 0;
        cardPanel.add(profileTitle, gbc);

        JLabel usernameValue = new JLabel();
        usernameValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 1;
        cardPanel.add(createLabeledComponent("Username:", usernameValue), gbc);

        JLabel emailValue = new JLabel();
        emailValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 2;
        cardPanel.add(createLabeledComponent("Email:", emailValue), gbc);

        JLabel phoneValue = new JLabel();
        phoneValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 3;
        cardPanel.add(createLabeledComponent("Phone:", phoneValue), gbc);

        JLabel memberSince = new JLabel();
        memberSince.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 4;
        cardPanel.add(createLabeledComponent("Member Since:", memberSince), gbc);

        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        editPanel.setBackground(ThemeColors.CARD_BG);

        ModernButton editButton = new ModernButton("✏️ Edit Profile", ThemeColors.INFO);
        editButton.addActionListener(e -> showEditProfileDialog());

        ModernButton changePassButton = new ModernButton("🔐 Change Password", ThemeColors.WARNING);
        changePassButton.addActionListener(e -> showChangePasswordDialog());

        editPanel.add(editButton);
        editPanel.add(changePassButton);

        gbc.gridy = 5;
        gbc.insets = new Insets(30, 30, 20, 30);
        cardPanel.add(editPanel, gbc);

        contentPanel.add(cardPanel);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        panel.putClientProperty("usernameValue", usernameValue);
        panel.putClientProperty("emailValue", emailValue);
        panel.putClientProperty("phoneValue", phoneValue);
        panel.putClientProperty("memberSince", memberSince);

        return panel;
    }

    private JPanel createManageRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColors.BG_LIGHT);

        JPanel headerPanel = new GradientPanel(ThemeColors.SECONDARY, new Color(65, 160, 75));
        headerPanel.setPreferredSize(new Dimension(1400, 90));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("🔧 Manage Rooms");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        ModernButton backButton = new ModernButton("← Back", ThemeColors.SECONDARY);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "ADMIN_DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ThemeColors.BG_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBackground(ThemeColors.BG_LIGHT);

        ModernButton addRoomButton = new ModernButton("➕ Add Room", ThemeColors.SECONDARY);
        addRoomButton.addActionListener(e -> {
            showAddRoomDialog();
            updateAllPanels();
        });

        topPanel.add(addRoomButton);
        contentPanel.add(topPanel, BorderLayout.NORTH);

        JPanel roomsPanel = new JPanel();
        roomsPanel.setLayout(new BoxLayout(roomsPanel, BoxLayout.Y_AXIS));
        roomsPanel.setBackground(ThemeColors.BG_LIGHT);

        JScrollPane scrollPane = new JScrollPane(roomsPanel);
        scrollPane.setBackground(ThemeColors.BG_LIGHT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        panel.putClientProperty("roomsContent", roomsPanel);

        return panel;
    }

    private JPanel createViewAllBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ThemeColors.BG_LIGHT);

        JPanel headerPanel = new GradientPanel(ThemeColors.PRIMARY, ThemeColors.PRIMARY_DARK);
        headerPanel.setPreferredSize(new Dimension(1400, 90));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel titleLabel = new JLabel("📋 All Bookings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        ModernButton backButton = new ModernButton("← Back", ThemeColors.PRIMARY);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "ADMIN_DASHBOARD"));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ThemeColors.BG_LIGHT);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(ThemeColors.BG_LIGHT);

        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextField searchField = createModernTextField("Search by username, booking ID, room...");
        searchField.setPreferredSize(new Dimension(350, 40));

        ModernButton searchButton = new ModernButton("Search", ThemeColors.PRIMARY);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        contentPanel.add(searchPanel, BorderLayout.NORTH);

        JPanel bookingsPanel = new JPanel();
        bookingsPanel.setLayout(new BoxLayout(bookingsPanel, BoxLayout.Y_AXIS));
        bookingsPanel.setBackground(ThemeColors.BG_LIGHT);

        JScrollPane scrollPane = new JScrollPane(bookingsPanel);
        scrollPane.setBackground(ThemeColors.BG_LIGHT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            bookingsPanel.removeAll();

            ArrayList<Booking> results;
            if (query.isEmpty()) {
                results = hotelManager.getAllBookings();
            } else {
                results = hotelManager.searchBookings(query);
            }

            if (results.isEmpty()) {
                JLabel noResultsLabel = new JLabel("❌ No bookings found!");
                noResultsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                noResultsLabel.setForeground(ThemeColors.DARK_GRAY);
                bookingsPanel.add(noResultsLabel);
            } else {
                for (Booking booking : results) {
                    JPanel bookingCard = createAdminBookingCard(booking);
                    bookingsPanel.add(bookingCard);
                    bookingsPanel.add(Box.createVerticalStrut(12));
                }
            }
            bookingsPanel.revalidate();
            bookingsPanel.repaint();
        });

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        panel.putClientProperty("bookingsPanel", bookingsPanel);

        return panel;
    }

    // =============== Helper Methods ===============
    private JTextField createModernTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setText(placeholder);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(300, 40));
        field.setBorder(new RoundedBorder(10, ThemeColors.GRAY, 1));
        field.setForeground(ThemeColors.DARK_GRAY);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(ThemeColors.TEXT_DARK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(ThemeColors.DARK_GRAY);
                }
            }
        });
        
        return field;
    }

    private JPasswordField createModernPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(300, 40));
        field.setBorder(new RoundedBorder(10, ThemeColors.GRAY, 1));
        return field;
    }

    private JComboBox<String> createModernComboBox() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(400, 40));
        combo.setMaximumRowCount(10);
        return combo;
    }

    private JSpinner createModernDateSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(editor);
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return spinner;
    }

    private JPanel createLabeledComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(ThemeColors.CARD_BG);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(ThemeColors.TEXT_DARK);
        label.setPreferredSize(new Dimension(160, 40));
        panel.add(label, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDashboardCard(String title, String description, Color color, ActionListener action) {
        RoundedPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout());
        card.setBorder(new RoundedBorder(20, color, 3));
        card.setPreferredSize(new Dimension(400, 200));
        card.setMaximumSize(new Dimension(400, 200));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(color);
        headerPanel.setPreferredSize(new Dimension(400, 80));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(Color.WHITE);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(color);
        textPanel.add(titleLabel);
        textPanel.add(descLabel);

        headerPanel.add(textPanel, BorderLayout.WEST);

        ModernButton actionButton = new ModernButton("➜ Access", color);
        actionButton.addActionListener(action);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(ThemeColors.CARD_BG);
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        buttonPanel.add(actionButton, BorderLayout.CENTER);

        card.add(headerPanel, BorderLayout.NORTH);
        card.add(buttonPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createOfferCard(String title, String description, String validity, Color color) {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new GridBagLayout());
        card.setBorder(new RoundedBorder(15, color, 2));
        card.setPreferredSize(new Dimension(350, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(color);
        gbc.gridy = 0;
        card.add(titleLabel, gbc);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(ThemeColors.TEXT_DARK);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 15, 5, 15);
        card.add(descLabel, gbc);

        JLabel validLabel = new JLabel(validity);
        validLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        validLabel.setForeground(ThemeColors.DARK_GRAY);
        gbc.gridy = 2;
        card.add(validLabel, gbc);

        return card;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new GridBagLayout());
        card.setBorder(new RoundedBorder(15, color, 2));
        card.setPreferredSize(new Dimension(180, 100));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLabel.setForeground(ThemeColors.DARK_GRAY);
        gbc.gridy = 0;
        card.add(titleLabel, gbc);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(color);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 0);
        card.add(valueLabel, gbc);

        card.putClientProperty("valueLabel", valueLabel);

        return card;
    }

    private JPanel createRoomCard(Room room) {
        RoundedPanel panel = new RoundedPanel(15);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new RoundedBorder(15, ThemeColors.GRAY, 1));
        panel.setPreferredSize(new Dimension(1000, 110));
        panel.setMaximumSize(new Dimension(1000, 110));

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(ThemeColors.CARD_BG);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JLabel roomLabel = new JLabel("🛏️ " + room.toString());
        roomLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        infoPanel.add(roomLabel);

        JLabel statusLabel = new JLabel(room.isAvailable() ? "✅ Available" : "🚫 Booked");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(room.isAvailable() ? ThemeColors.SUCCESS : ThemeColors.DANGER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        infoPanel.add(statusLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAdminRoomCard(Room room) {
        RoundedPanel panel = new RoundedPanel(15);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new RoundedBorder(15, ThemeColors.GRAY, 1));
        panel.setPreferredSize(new Dimension(1000, 120));
        panel.setMaximumSize(new Dimension(1000, 120));

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(ThemeColors.CARD_BG);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JLabel roomLabel = new JLabel("🛏️ " + room.toString());
        roomLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        infoPanel.add(roomLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        ModernButton removeButton = new ModernButton("❌ Remove", ThemeColors.DANGER);
        removeButton.setPreferredSize(new Dimension(120, 35));
        removeButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this,
                    "Remove room #" + room.getId() + "?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                if (hotelManager.removeRoom(room.getId())) {
                    showCustomMessage("✅ Room removed!", "Success");
                    updateAllPanels();
                } else {
                    showCustomMessage("❌ Cannot remove! Active bookings exist.", "Error");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(ThemeColors.CARD_BG);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(removeButton);

        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createBookingCard(Booking booking) {
        RoundedPanel panel = new RoundedPanel(15);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new RoundedBorder(15, ThemeColors.GRAY, 1));
        panel.setPreferredSize(new Dimension(1000, 130));
        panel.setMaximumSize(new Dimension(1000, 130));

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(ThemeColors.CARD_BG);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JLabel bookingLabel = new JLabel("📋 " + booking.toString());
        bookingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel statusLabel = new JLabel("Status: " + booking.getStatus());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(booking.getStatus().equals("CONFIRMED") ? ThemeColors.SUCCESS : ThemeColors.DANGER);

        infoPanel.add(bookingLabel);
        infoPanel.add(statusLabel);

        ModernButton cancelButton = new ModernButton("❌ Cancel", ThemeColors.DANGER);
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this,
                    "Cancel booking #" + booking.getBookingId() + "?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                if (hotelManager.cancelBooking(booking.getBookingId(), currentUser.getUsername())) {
                    showCustomMessage("✅ Booking cancelled!", "Success");
                    updateAllPanels();
                } else {
                    showCustomMessage("❌ Failed to cancel!", "Error");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(ThemeColors.CARD_BG);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(cancelButton);

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createAdminBookingCard(Booking booking) {
        RoundedPanel panel = new RoundedPanel(15);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new RoundedBorder(15, ThemeColors.GRAY, 1));
        panel.setPreferredSize(new Dimension(1000, 120));
        panel.setMaximumSize(new Dimension(1000, 120));

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(ThemeColors.CARD_BG);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JLabel bookingLabel = new JLabel("📋 " + booking.toString());
        bookingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel statusLabel = new JLabel("Status: " + booking.getStatus());
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(booking.getStatus().equals("CONFIRMED") ? ThemeColors.SUCCESS : ThemeColors.DANGER);

        infoPanel.add(bookingLabel);
        infoPanel.add(statusLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    private void showAddRoomDialog() {
        JDialog dialog = new JDialog(this, "Add New Room", true);
        dialog.setSize(450, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        RoundedPanel panel = new RoundedPanel(15);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = createModernTextField("Room ID");
        JTextField typeField = createModernTextField("Room Type");
        JTextField priceField = createModernTextField("Price");
        JSpinner capacitySpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionArea.setBorder(new RoundedBorder(10, ThemeColors.GRAY, 1));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(createLabeledComponent("Room ID:", idField), gbc);

        gbc.gridy = 1;
        panel.add(createLabeledComponent("Room Type:", typeField), gbc);

        gbc.gridy = 2;
        panel.add(createLabeledComponent("Price:", priceField), gbc);

        gbc.gridy = 3;
        panel.add(createLabeledComponent("Capacity:", capacitySpinner), gbc);

        gbc.gridy = 4;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(ThemeColors.TEXT_DARK);
        panel.add(descLabel, gbc);

        gbc.gridy = 5;
        panel.add(new JScrollPane(descriptionArea), gbc);

        ModernButton addButton = new ModernButton("✅ Add Room", ThemeColors.SUCCESS);
        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String type = typeField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int capacity = (Integer) capacitySpinner.getValue();
                String description = descriptionArea.getText().trim();

                if (type.isEmpty() || description.isEmpty()) {
                    showCustomMessage("❌ Please fill all fields!", "Error");
                    return;
                }

                Room newRoom = new Room(id, type, price, description, capacity);
                if (hotelManager.addRoom(newRoom)) {
                    showCustomMessage("✅ Room added successfully!", "Success");
                    updateAllPanels();
                    dialog.dispose();
                } else {
                    showCustomMessage("❌ Room ID already exists!", "Error");
                }
            } catch (NumberFormatException ex) {
                showCustomMessage("❌ Please enter valid values!", "Error");
            }
        });

        ModernButton cancelButton = new ModernButton("❌ Cancel", ThemeColors.DANGER);
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(ThemeColors.CARD_BG);
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.insets = new Insets(25, 12, 12, 12);
        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showEditProfileDialog() {
        JDialog dialog = new JDialog(this, "Edit Profile", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        RoundedPanel panel = new RoundedPanel(15);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField emailField = createModernTextField(currentUser.getEmail());
        JTextField phoneField = createModernTextField(currentUser.getPhoneNumber());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(createLabeledComponent("Email:", emailField), gbc);

        gbc.gridy = 1;
        panel.add(createLabeledComponent("Phone:", phoneField), gbc);

        ModernButton saveButton = new ModernButton("✅ Save Changes", ThemeColors.SUCCESS);
        saveButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (email.isEmpty() || phone.isEmpty()) {
                showCustomMessage("❌ Please fill all fields!", "Error");
                return;
            }

            currentUser.setEmail(email);
            currentUser.setPhoneNumber(phone);
            authController.saveUsers();
            showCustomMessage("✅ Profile updated!", "Success");
            updateAllPanels();
            dialog.dispose();
        });

        ModernButton cancelButton = new ModernButton("❌ Cancel", ThemeColors.DANGER);
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(ThemeColors.CARD_BG);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 12, 12, 12);
        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showChangePasswordDialog() {
        JDialog dialog = new JDialog(this, "Change Password", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        RoundedPanel panel = new RoundedPanel(15);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPasswordField oldPassField = createModernPasswordField("Old Password");
        JPasswordField newPassField = createModernPasswordField("New Password");
        JPasswordField confirmPassField = createModernPasswordField("Confirm Password");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(oldPassField, gbc);

        gbc.gridy = 1;
        panel.add(newPassField, gbc);

        gbc.gridy = 2;
        panel.add(confirmPassField, gbc);

        ModernButton changeButton = new ModernButton("✅ Change Password", ThemeColors.SUCCESS);
        changeButton.addActionListener(e -> {
            String oldPass = new String(oldPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                showCustomMessage("❌ Please fill all fields!", "Error");
                return;
            }

            if (!oldPass.equals(currentUser.getPassword())) {
                showCustomMessage("❌ Old password is incorrect!", "Error");
                return;
            }

            if (!newPass.equals(confirmPass)) {
                showCustomMessage("❌ Passwords don't match!", "Error");
                return;
            }

            if (newPass.length() < 6) {
                showCustomMessage("❌ Password must be at least 6 characters!", "Error");
                return;
            }

            currentUser.setPassword(newPass);
            authController.saveUsers();
            showCustomMessage("✅ Password changed successfully!", "Success");
            dialog.dispose();
        });

        ModernButton cancelButton = new ModernButton("❌ Cancel", ThemeColors.DANGER);
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(ThemeColors.CARD_BG);
        buttonPanel.add(changeButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 12, 12, 12);
        panel.add(buttonPanel, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showCustomMessage(String message, String type) {
        JOptionPane.showMessageDialog(this, message, type, 
            type.equals("Error") ? JOptionPane.ERROR_MESSAGE :
            type.equals("Warning") ? JOptionPane.WARNING_MESSAGE :
            JOptionPane.INFORMATION_MESSAGE);
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

        // Update Profile Panel
        for (Component c : mainPanel.getComponents()) {
            JPanel p = (JPanel) c;
            if (p.getClientProperty("usernameValue") != null) {
                JLabel usernameValue = (JLabel) p.getClientProperty("usernameValue");
                JLabel emailValue = (JLabel) p.getClientProperty("emailValue");
                JLabel phoneValue = (JLabel) p.getClientProperty("phoneValue");
                JLabel memberSince = (JLabel) p.getClientProperty("memberSince");

                usernameValue.setText(currentUser.getUsername());
                emailValue.setText(currentUser.getEmail());
                phoneValue.setText(currentUser.getPhoneNumber());
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
                memberSince.setText(sdf.format(new java.util.Date(currentUser.getCreatedAt())));
                break;
            }
        }

        // Update Admin Dashboard Stats
        for (Component c : mainPanel.getComponents()) {
            JPanel p = (JPanel) c;
            if (p.getClientProperty("statsPanel") != null && currentUser.isAdmin()) {
                JPanel statsPanel = (JPanel) p.getClientProperty("statsPanel");
                Component[] components = statsPanel.getComponents();
                
                int totalBookings = hotelManager.getTotalBookings();
                int confirmedBookings = hotelManager.getConfirmedBookings();
                double revenue = hotelManager.getTotalRevenue();
                double occupancy = hotelManager.getOccupancyRate();

                if (components.length >= 4) {
                    JPanel stat1 = (JPanel) components[0];
                    JPanel stat2 = (JPanel) components[1];
                    JPanel stat3 = (JPanel) components[2];
                    JPanel stat4 = (JPanel) components[3];

                    updateStatCard(stat1, String.valueOf(totalBookings));
                    updateStatCard(stat2, String.valueOf(confirmedBookings));
                    updateStatCard(stat3, "Rs." + String.format("%.2f", revenue));
                    updateStatCard(stat4, String.format("%.1f%%", occupancy));
                }
                break;
            }
        }

        // Update View Rooms
        for (Component c : mainPanel.getComponents()) {
            JPanel p = (JPanel) c;
            if (p.getClientProperty("roomsContent") != null && p.getClientProperty("bookingsContent") == null && !currentUser.isAdmin()) {
                JPanel roomsContent = (JPanel) p.getClientProperty("roomsContent");
                roomsContent.removeAll();
                for (Room room : hotelManager.getAllRooms()) {
                    JPanel roomCard = createRoomCard(room);
                    roomsContent.add(roomCard);
                    roomsContent.add(Box.createVerticalStrut(12));
                }
                roomsContent.revalidate();
                roomsContent.repaint();
                break;
            }
        }

        // Update Manage Rooms (Admin)
        for (Component c : mainPanel.getComponents()) {
            JPanel p = (JPanel) c;
            if (p.getClientProperty("roomsContent") != null && currentUser.isAdmin()) {
                JPanel manageRoomsContent = (JPanel) p.getClientProperty("roomsContent");
                if (manageRoomsContent != null) {
                    manageRoomsContent.removeAll();
                    for (Room room : hotelManager.getAllRooms()) {
                        JPanel roomCard = createAdminRoomCard(room);
                        manageRoomsContent.add(roomCard);
                        manageRoomsContent.add(Box.createVerticalStrut(12));
                    }
                    manageRoomsContent.revalidate();
                    manageRoomsContent.repaint();
                }
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
                    roomCombo.addItem("🛏️ Room #" + room.getId() + " | " + room.getType() + " | Capacity: " + room.getCapacity() + " | Rs." + room.getPrice() + "/night");
                }
                break;
            }
        }

        // Update My Bookings
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
                    JLabel noBookingsLabel = new JLabel("📭 No active bookings!");
                    noBookingsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                    noBookingsLabel.setForeground(ThemeColors.DARK_GRAY);
                    bookingsContent.add(noBookingsLabel);
                } else {
                    for (Booking booking : activeBookings) {
                        JPanel bookingCard = createBookingCard(booking);
                        bookingsContent.add(bookingCard);
                        bookingsContent.add(Box.createVerticalStrut(12));
                    }
                }
                bookingsContent.revalidate();
                bookingsContent.repaint();
                break;
            }
        }
    }

    private void updateStatCard(JPanel statCard, String value) {
        for (Component comp : statCard.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getFont().getSize() > 15) {
                    label.setText(value);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelReservationSystem());
    }
}