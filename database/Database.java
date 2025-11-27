package bank.database;

import java.sql.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Singleton Database class for managing SQLite database operations.
 * Provides CRUD operations for User, Customer, Account, and Transaction entities.
 */
public class Database {
    private static Database instance;
    private Connection connection;
    private Boolean auditLogSupportsUsername; // lazily detected
    private final String url;
    
    // Private constructor to prevent external instantiation
    private Database() {
        // Always use the populated database under /database/bank.db
        Path dbPath = Paths.get("database", "bank.db").toAbsolutePath();
        if (!Files.exists(dbPath)) {
            throw new IllegalStateException("Expected database at " + dbPath + " but it was not found.");
        }
        this.url = "jdbc:sqlite:" + dbPath;
        connect();
    }
    
    /**
     * Get the singleton instance of Database.
     * Thread-safe implementation.
     * 
     * @return the singleton Database instance
     */
    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    
    /**
     * Establish database connection.
     * Creates necessary tables if they don't exist.
     */
    private void connect() {
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Database connection established.");
            createTablesIfNotExist();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create all necessary tables if they don't exist for some dumb reason
     */
    private void createTablesIfNotExist() {
        try {
            Statement stmt = connection.createStatement();
            
            // Users table (for authentication and role-based access etc)
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "username TEXT PRIMARY KEY, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL CHECK(role IN ('CUSTOMER', 'TELLER', 'ADMIN'))" +
                ")"
            );
            
            // Customers table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS customers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "name TEXT NOT NULL, " +
                "place_of_birth TEXT, " +
                "date_of_birth TEXT, " +
                "FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE" +
                ")"
            );
            
            // Accounts table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_number INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customer_id INTEGER NOT NULL, " +
                "account_type TEXT NOT NULL CHECK(account_type IN ('CHECKING', 'SAVINGS')), " +
                "balance REAL NOT NULL DEFAULT 0.0, " +
                "interest_rate REAL, " +
                "status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE', 'INACTIVE', 'CLOSED')), " +
                "card_number TEXT, " +
                "card_expiry TEXT, " +
                "card_cvc TEXT, " +
                "FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Transactions table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS transactions (" +
                "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "account_number INTEGER NOT NULL, " +
                "transaction_type TEXT NOT NULL CHECK(transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER')), " +
                "amount REAL NOT NULL, " +
                "timestamp TEXT NOT NULL, " +
                "description TEXT, " +
                "FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE" +
                ")"
            );
            
            // Branches table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS branches (" +
                "branch_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "branch_name TEXT NOT NULL, " +
                "address TEXT NOT NULL" +
                ")"
            );
            
            //Audit log table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS audit_log (" +
                "log_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "action TEXT NOT NULL, " +
                "timestamp TEXT NOT NULL, " +
                "details TEXT" +
                ")"
            );
            
            stmt.close();
            System.out.println("Database tables verified/created.");
        } catch (SQLException e) {
            System.err.println("Failed to create tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get the database connection.
     * 
     * @return the active Connection object
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
            connect();
        }
        return connection;
    }
    
    /**
     * Close the database connection.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ==================== USER OPERATIONS ====================
    // Super well doucmented so you just hover over the methods to see what they do please please please dont change this
    /**
     * Create a new user in the database.
     * 
     * @param username the username
     * @param password the password (should be hashed before calling)
     * @param role the user role (CUSTOMER, TELLER, ADMIN)
     * @return true if user was created successfully
     */
    public boolean createUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role.toUpperCase());
            pstmt.executeUpdate();
            logAudit(username, "CREATE_USER", "User created with role: " + role);
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Create a new user in the primary USER table (capitalized schema).
     * For this project, password_hash stores the provided password directly.
     *
     * @param username the username
     * @param password the password (stored as-is)
     * @param role the user role (CUSTOMER, TELLER, ADMIN)
     * @return true if user was created successfully
     */
    public boolean createUserPrimary(String username, String password, String role) {
        String sql = "INSERT INTO USER (username, password_hash, email, user_type, is_active) VALUES (?, ?, ?, ?, 1)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            // Store bcrypt hash
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            pstmt.setString(2, hash);
            // placeholder email to satisfy NOT NULL/UNIQUE without collecting PII
            pstmt.setString(3, username + "@placeholder.local");
            pstmt.setString(4, role.toUpperCase());
            pstmt.executeUpdate();
            logAudit(username, "CREATE_USER", "User created with role: " + role);
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating user in USER table: " + e.getMessage());
            return false;
        }
    }

    // ========= Primary (capitalized) schema helpers =========

    public static class PrimaryAccount {
        public final int accountId;
        public final String accountNumber;
        public final String accountType;
        public final double balance;
        public final String ownerDisplay;
        public final String birthplace;
        public final String address;
        public final String dateOfBirth;
        public final String createdAt;

        public PrimaryAccount(int accountId, String accountNumber, String accountType, double balance) {
            this(accountId, accountNumber, accountType, balance, null, null, null, null, null);
        }

        public PrimaryAccount(int accountId, String accountNumber, String accountType, double balance, String ownerDisplay,
                              String birthplace, String address, String dateOfBirth, String createdAt) {
            this.accountId = accountId;
            this.accountNumber = accountNumber;
            this.accountType = accountType;
            this.balance = balance;
            this.ownerDisplay = ownerDisplay;
            this.birthplace = birthplace;
            this.address = address;
            this.dateOfBirth = dateOfBirth;
            this.createdAt = createdAt;
        }
    }

    public static class PrimaryTransaction {
        public final int id;
        public final Integer fromAccountId;
        public final Integer toAccountId;
        public final double amount;
        public final String type;
        public final String timestamp;
        public final String description;

        public PrimaryTransaction(int id, Integer fromAccountId, Integer toAccountId, double amount,
                                  String type, String timestamp, String description) {
            this.id = id;
            this.fromAccountId = fromAccountId;
            this.toAccountId = toAccountId;
            this.amount = amount;
            this.type = type;
            this.timestamp = timestamp;
            this.description = description;
        }
    }

    public PrimaryAccount getPrimaryAccountById(int accountId) {
        String sql = "SELECT a.account_id, a.account_number, a.account_type, a.balance, u.username, c.first_name, c.last_name, " +
                     "c.birthplace, c.address, c.date_of_birth, u.created_at " +
                     "FROM ACCOUNT a JOIN CUSTOMER c ON a.customer_id = c.customer_id " +
                     "JOIN USER u ON c.user_id = u.user_id " +
                     "WHERE a.account_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapPrimaryAccount(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching primary account by id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get CUSTOMER.customer_id for a USER.username.
     */
    public Integer getPrimaryCustomerIdByUsername(String username) {
        String sql = "SELECT c.customer_id FROM CUSTOMER c JOIN USER u ON c.user_id = u.user_id WHERE u.username = ? LIMIT 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("customer_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching primary customer_id: " + e.getMessage());
        }
        return null;
    }

    /**
     * Fetch accounts from ACCOUNT for a given customer_id.
     */
    public List<PrimaryAccount> getPrimaryAccountsByCustomerId(int customerId) {
        String sql = "SELECT a.account_id, a.account_number, a.account_type, a.balance, u.username, c.first_name, c.last_name, " +
                     "c.birthplace, c.address, c.date_of_birth, u.created_at " +
                     "FROM ACCOUNT a JOIN CUSTOMER c ON a.customer_id = c.customer_id " +
                     "JOIN USER u ON c.user_id = u.user_id " +
                     "WHERE a.customer_id = ?";
        List<PrimaryAccount> accounts = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapPrimaryAccount(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching primary accounts: " + e.getMessage());
        }
        return accounts;
    }

    public List<PrimaryAccount> getPrimaryAccountsByType(String accountType) {
        String sql = "SELECT a.account_id, a.account_number, a.account_type, a.balance, u.username, c.first_name, c.last_name, " +
                     "c.birthplace, c.address, c.date_of_birth, u.created_at " +
                     "FROM ACCOUNT a JOIN CUSTOMER c ON a.customer_id = c.customer_id " +
                     "JOIN USER u ON c.user_id = u.user_id " +
                     "WHERE a.account_type = ?";
        List<PrimaryAccount> accounts = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accountType.toUpperCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapPrimaryAccount(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching primary accounts by type: " + e.getMessage());
        }
        return accounts;
    }

    /**
     * Fetch accounts by customer last name (partial, case-insensitive).
     */
    public List<PrimaryAccount> getPrimaryAccountsByCustomerLastName(String lastName) {
        String sql = "SELECT a.account_id, a.account_number, a.account_type, a.balance, u.username, c.first_name, c.last_name, " +
                     "c.birthplace, c.address, c.date_of_birth, u.created_at " +
                     "FROM ACCOUNT a JOIN CUSTOMER c ON a.customer_id = c.customer_id " +
                     "JOIN USER u ON c.user_id = u.user_id " +
                     "WHERE LOWER(c.last_name) LIKE LOWER(?)";
        List<PrimaryAccount> accounts = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + lastName + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapPrimaryAccount(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching primary accounts by customer last name: " + e.getMessage());
        }
        return accounts;
    }

    /**
     * Filter accounts by optional customer fields.
     */
    public List<PrimaryAccount> getPrimaryAccountsByCustomerFilters(String lastName, String birthplace, String address,
                                                                    String createdAfter, String dobAfter) {
        StringBuilder sb = new StringBuilder(
            "SELECT a.account_id, a.account_number, a.account_type, a.balance, u.username, c.first_name, c.last_name, " +
            "c.birthplace, c.address, c.date_of_birth, u.created_at " +
            "FROM ACCOUNT a JOIN CUSTOMER c ON a.customer_id = c.customer_id " +
            "JOIN USER u ON c.user_id = u.user_id WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();
        if (lastName != null && !lastName.isBlank()) {
            sb.append("AND LOWER(c.last_name) LIKE LOWER(?) ");
            params.add("%" + lastName + "%");
        }
        if (birthplace != null && !birthplace.isBlank()) {
            sb.append("AND LOWER(c.birthplace) LIKE LOWER(?) ");
            params.add("%" + birthplace + "%");
        }
        if (address != null && !address.isBlank()) {
            sb.append("AND LOWER(c.address) LIKE LOWER(?) ");
            params.add("%" + address + "%");
        }
        if (createdAfter != null && !createdAfter.isBlank()) {
            sb.append("AND date(u.created_at) >= date(?) ");
            params.add(createdAfter);
        }
        if (dobAfter != null && !dobAfter.isBlank()) {
            sb.append("AND date(c.date_of_birth) >= date(?) ");
            params.add(dobAfter);
        }
        sb.append("ORDER BY a.account_id");

        List<PrimaryAccount> accounts = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapPrimaryAccount(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching primary accounts with filters: " + e.getMessage());
        }
        return accounts;
    }

    /**
     * Fetch accounts by customer last name (case-insensitive, partial match).
     */
    public Double getPrimaryAccountBalance(int accountId) {
        String sql = "SELECT balance FROM ACCOUNT WHERE account_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting primary account balance: " + e.getMessage());
        }
        return null;
    }

    public boolean updatePrimaryAccountBalance(int accountId, double newBalance) {
        String sql = "UPDATE ACCOUNT SET balance = ? WHERE account_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, accountId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating primary account balance: " + e.getMessage());
            return false;
        }
    }

    /**
     * Insert into TRANSACTION (capitalized) table.
     * For deposits: to_account_id set, from_account_id null.
     * For withdrawals: from_account_id set, to_account_id null.
     */
    public int createPrimaryTransaction(int accountId, String transactionType, double amount, String description) {
        Integer fromId = null;
        Integer toId = null;
        String normalizedType = transactionType.toUpperCase();
        if ("DEPOSIT".equals(normalizedType)) {
            toId = accountId;
        } else if ("WITHDRAWAL".equals(normalizedType)) {
            fromId = accountId;
        }

        String sql = "INSERT INTO \"TRANSACTION\" (from_account_id, to_account_id, amount, transaction_type, status, description, transaction_date) " +
                     "VALUES (?, ?, ?, ?, 'COMPLETED', ?, datetime('now'))";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (fromId == null) {
                pstmt.setNull(1, Types.INTEGER);
            } else {
                pstmt.setInt(1, fromId);
            }
            if (toId == null) {
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, toId);
            }
            pstmt.setDouble(3, amount);
            pstmt.setString(4, normalizedType);
            pstmt.setString(5, description);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int transactionId = rs.getInt(1);
                    logAudit("SYSTEM", "CREATE_TRANSACTION", "Primary transaction " + transactionId + " " + normalizedType + " $" + amount);
                    return transactionId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating primary transaction: " + e.getMessage());
        }
        return -1;
    }

    public List<PrimaryTransaction> getPrimaryTransactionsForAccount(int accountId) {
        String sql = "SELECT transaction_id, from_account_id, to_account_id, amount, transaction_type, transaction_date, description " +
                     "FROM \"TRANSACTION\" " +
                     "WHERE from_account_id = ? OR to_account_id = ? " +
                     "ORDER BY transaction_date DESC, transaction_id DESC";
        List<PrimaryTransaction> list = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new PrimaryTransaction(
                        rs.getInt("transaction_id"),
                        rs.getObject("from_account_id") != null ? rs.getInt("from_account_id") : null,
                        rs.getObject("to_account_id") != null ? rs.getInt("to_account_id") : null,
                        rs.getDouble("amount"),
                        rs.getString("transaction_type"),
                        rs.getString("transaction_date"),
                        rs.getString("description")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching primary transactions: " + e.getMessage());
        }
        return list;
    }

    /**
     * Delete a user from the primary USER table.
     */
    public boolean deletePrimaryUser(String username) {
        // Identify role to cascade delete from role-specific tables.
        String role = getUserRole(username);
        if (role != null) {
            deleteRoleRecord(username, role);
        }

        String sql = "DELETE FROM USER WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user from USER: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update a user in the primary USER table.
     * Null parameters are ignored.
     */
    public boolean updatePrimaryUser(String username, String newPassword, String newRole) {
        StringBuilder sb = new StringBuilder("UPDATE USER SET ");
        List<Object> params = new ArrayList<>();
        if (newPassword != null) {
            sb.append("password_hash = ?, ");
            params.add(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        }
        if (newRole != null) {
            sb.append("user_type = ?, ");
            params.add(newRole.toUpperCase());
        }
        if (params.isEmpty()) {
            return false;
        }
        sb.append("updated_at = datetime('now') WHERE username = ?");
        params.add(username);

        try (PreparedStatement pstmt = connection.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user in USER: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePrimaryAccountStatus(int accountId, String status) {
        String normalized = status.toUpperCase();
        if (!normalized.equals("ACTIVE") && !normalized.equals("FROZEN") && !normalized.equals("CLOSED")) {
            System.err.println("Invalid account status: " + status);
            return false;
        }
        String sql = "UPDATE ACCOUNT SET status = ?, updated_at = datetime('now') WHERE account_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, normalized);
            pstmt.setInt(2, accountId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating primary account status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Create a new account in the primary ACCOUNT table.
     */
    public int createPrimaryAccount(int customerId, String accountType, double balance, int branchId) {
        String normalizedType = accountType.toUpperCase().startsWith("SAV") ? "SAVING" : "CHECK";
        String accountNumber = generatePrimaryAccountNumber(normalizedType, customerId);
        String sql = "INSERT INTO ACCOUNT (account_number, customer_id, account_type, balance, status, branch_id, opened_date) " +
                     "VALUES (?, ?, ?, ?, 'ACTIVE', ?, date('now'))";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, accountNumber);
            pstmt.setInt(2, customerId);
            pstmt.setString(3, normalizedType);
            pstmt.setDouble(4, balance);
            pstmt.setInt(5, branchId);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int accountId = rs.getInt(1);
                    logAudit("SYSTEM", "CREATE_ACCOUNT_PRIMARY", "Account " + accountNumber + " (" + normalizedType + ") created for customer_id " + customerId);
                    return accountId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating primary account: " + e.getMessage());
        }
        return -1;
    }

    private String generatePrimaryAccountNumber(String accountType, int customerId) {
        String prefix = accountType.startsWith("SAV") ? "SAV" : "CHK";
        long ts = System.currentTimeMillis() % 1000000;
        return prefix + "-" + customerId + "-" + ts;
    }

    /**
     * Create a minimal EMPLOYEE row for a teller.
     */
    public boolean createPrimaryEmployee(String username, String firstName, String lastName) {
        Integer userId = getPrimaryUserId(username);
        if (userId == null) {
            System.err.println("Cannot create employee; user not found: " + username);
            return false;
        }
        String sql = "INSERT INTO EMPLOYEE (user_id, branch_id, first_name, last_name, position, department, hire_date, is_teller, is_manager) " +
                     "VALUES (?, 1, ?, ?, 'Teller', 'Customer Service', date('now'), 1, 0)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, firstName == null ? "" : firstName);
            pstmt.setString(3, lastName == null ? "" : lastName);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating employee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Create a minimal ADMINISTRATOR row.
     */
    public boolean createPrimaryAdministrator(String username, String firstName, String lastName) {
        Integer userId = getPrimaryUserId(username);
        if (userId == null) {
            System.err.println("Cannot create admin; user not found: " + username);
            return false;
        }
        String sql = "INSERT INTO ADMINISTRATOR (user_id, first_name, last_name, access_level) VALUES (?, ?, ?, 'FULL')";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, firstName == null ? "" : firstName);
            pstmt.setString(3, lastName == null ? "" : lastName);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating administrator: " + e.getMessage());
            return false;
        }
    }

    private PrimaryAccount mapPrimaryAccount(ResultSet rs) throws SQLException {
        String owner = rs.getString("first_name");
        String ln = rs.getString("last_name");
        String username = rs.getString("username");
        String display;
        if (owner != null && !owner.isBlank()) {
            display = owner + (ln != null && !ln.isBlank() ? (" " + ln) : "");
        } else {
            display = username != null ? username : "Unknown";
        }
        return new PrimaryAccount(
            rs.getInt("account_id"),
            rs.getString("account_number"),
            rs.getString("account_type"),
            rs.getDouble("balance"),
            display,
            rs.getString("birthplace"),
            rs.getString("address"),
            rs.getString("date_of_birth"),
            rs.getString("created_at")
        );
    }

    /**
     * Delete a row from CUSTOMER/EMPLOYEE/ADMINISTRATOR based on username+role.
     */
    private void deleteRoleRecord(String username, String role) {
        String table;
        switch (role.toUpperCase()) {
            case "CUSTOMER":
                table = "CUSTOMER";
                break;
            case "TELLER":
                table = "EMPLOYEE";
                break;
            case "ADMIN":
                table = "ADMINISTRATOR";
                break;
            default:
                return;
        }
        String sql = "DELETE FROM " + table + " WHERE user_id = (SELECT user_id FROM USER WHERE username = ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting from " + table + ": " + e.getMessage());
        }
    }

    /**
     * Create a transfer record in TRANSACTION (capitalized) table.
     * Both from_account_id and to_account_id are populated.
     */
    public int createPrimaryTransfer(int fromAccountId, int toAccountId, double amount, String description) {
        String sql = "INSERT INTO \"TRANSACTION\" (from_account_id, to_account_id, amount, transaction_type, status, description, transaction_date) " +
                     "VALUES (?, ?, ?, 'TRANSFER', 'COMPLETED', ?, datetime('now'))";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, fromAccountId);
            pstmt.setInt(2, toAccountId);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, description);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int transactionId = rs.getInt(1);
                    logAudit("SYSTEM", "CREATE_TRANSFER", "Transfer " + transactionId + " $" + amount + " from " + fromAccountId + " to " + toAccountId);
                    return transactionId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating primary transfer: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Create a skeletal customer row in CUSTOMER for the given USER (personal fields empty by design).
     *
     * @param username the username already inserted in USER
     * @return true if row was created
     */
    public boolean createCustomerPrimary(String username, String firstName, String lastName) {
        return createCustomerPrimary(username, firstName, lastName, null, null, null);
    }

    /**
     * Create customer with optional details.
     */
    public boolean createCustomerPrimary(String username, String firstName, String lastName, String placeOfBirth, String dateOfBirth, String address) {
        Integer userId = getPrimaryUserId(username);
        if (userId == null) {
            return false;
        }
        String dob = (dateOfBirth == null || dateOfBirth.isBlank()) ? "1970-01-01" : dateOfBirth;
        String sql = "INSERT INTO CUSTOMER (user_id, first_name, last_name, date_of_birth, birthplace, address) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, firstName == null ? "" : firstName);
            pstmt.setString(3, lastName == null ? "" : lastName);
            pstmt.setString(4, dob);
            pstmt.setString(5, placeOfBirth);
            pstmt.setString(6, address);
            pstmt.executeUpdate();
            logAudit(username, "CREATE_CUSTOMER", "Customer created for user_id " + userId);
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating customer in CUSTOMER table: " + e.getMessage());
            return false;
        }
    }

    private Integer getPrimaryUserId(String username) {
        String sql = "SELECT user_id FROM USER WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user_id from USER: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Authenticate a user.
     * 
     * @param username the username
     * @param password the password
     * @return the user's role if authentication succeeds, null otherwise
     */
    public String authenticateUser(String username, String password) {
        // First attempt to authenticate against the populated USER table using bcrypt hashes.
        String role = authenticateAgainstPrimaryUserTable(username, password);
        if (role != null) {
            logAudit(username, "LOGIN_SUCCESS", "User logged in successfully");
            return role;
        }

        // Fallback: support legacy lowercase users table with plain-text passwords.
        role = authenticateAgainstLegacyUsersTable(username, password);
        if (role != null) {
            logAudit(username, "LOGIN_SUCCESS", "User logged in successfully (legacy table)");
            return role;
        }

        logAudit(username, "LOGIN_FAILED", "Failed login attempt");
        return null;
    }
    
    /**
     * Get user role.
     * 
     * @param username the username
     * @return the user's role or null if not found
     */
    public String getUserRole(String username) {
        // Check primary USER table first.
        String sqlPrimary = "SELECT user_type FROM USER WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sqlPrimary)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("user_type");
            }
        } catch (SQLException e) {
            System.err.println("Error getting user role from USER table: " + e.getMessage());
        }

        // Fallback to legacy users table.
        String sql = "SELECT role FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            System.err.println("Error getting user role: " + e.getMessage());
        }
        return null;
    }

    private String authenticateAgainstPrimaryUserTable(String username, String password) {
        String sql = "SELECT password_hash, user_type FROM USER WHERE username = ? AND is_active = 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    String role = rs.getString("user_type");
                    if (storedHash != null && storedHash.equals(password)) {
                        // Legacy plaintext match: rehash and update so future logins use bcrypt.
                        updatePrimaryUser(username, password, null);
                        return role;
                    }
                    // Optional: bcrypt hashes (preferred).
                    if (storedHash != null && isBcryptHash(storedHash)) {
                        try {
                            if (BCrypt.checkpw(password, storedHash)) {
                                return role;
                            }
                        } catch (IllegalArgumentException ex) {
                            System.err.println("Invalid bcrypt hash for user " + username + ": " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user against USER table: " + e.getMessage());
        }
        return null;
    }

    private String authenticateAgainstLegacyUsersTable(String username, String password) {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user against legacy users table: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update user password.
     * 
     * @param username the username
     * @param newPassword the new password (should be hashed)
     * @return true if password was updated successfully
     */
    public boolean updateUserPassword(String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                logAudit(username, "UPDATE_PASSWORD", "Password updated");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete a user.
     * 
     * @param username the username
     * @return true if user was deleted successfully
     */
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                logAudit("ADMIN", "DELETE_USER", "Deleted user: " + username);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get all users (Admin only).
     * 
     * @return list of usernames
     */
    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        String sql = "SELECT username FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        }
        return users;
    }
    
    // ==================== CUSTOMER OPERATIONS ====================
    
    /**
     * Create a new customer.
     * 
     * @param username the username (must exist in users table)
     * @param name the customer's name
     * @param placeOfBirth the place of birth
     * @param dateOfBirth the date of birth (format: YYYY-MM-DD)
     * @return the customer ID if created successfully, -1 otherwise
     */
    public int createCustomer(String username, String name, String placeOfBirth, String dateOfBirth) {
        String sql = "INSERT INTO customers (username, name, place_of_birth, date_of_birth) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, placeOfBirth);
            pstmt.setString(4, dateOfBirth);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int customerId = rs.getInt(1);
                logAudit(username, "CREATE_CUSTOMER", "Customer created with ID: " + customerId);
                return customerId;
            }
        } catch (SQLException e) {
            System.err.println("Error creating customer: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Get customer by username.
     * 
     * @param username the username
     * @return ResultSet containing customer data, or null
     */
    public ResultSet getCustomerByUsername(String username) {
        String sql = "SELECT * FROM customers WHERE username = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error getting customer: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get customer by ID.
     * 
     * @param customerId the customer ID
     * @return ResultSet containing customer data, or null
     */
    public ResultSet getCustomerById(int customerId) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error getting customer: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Search customers by place of birth.
     * 
     * @param placeOfBirth the place of birth
     * @return ResultSet containing matching customers
     */
    public ResultSet searchCustomersByBirthplace(String placeOfBirth) {
        String sql = "SELECT * FROM customers WHERE place_of_birth LIKE ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, "%" + placeOfBirth + "%");
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error searching customers: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Update customer information.
     * 
     * @param customerId the customer ID
     * @param name the new name
     * @param placeOfBirth the new place of birth
     * @param dateOfBirth the new date of birth
     * @return true if updated successfully
     */
    public boolean updateCustomer(int customerId, String name, String placeOfBirth, String dateOfBirth) {
        String sql = "UPDATE customers SET name = ?, place_of_birth = ?, date_of_birth = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, placeOfBirth);
            pstmt.setString(3, dateOfBirth);
            pstmt.setInt(4, customerId);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                logAudit("ADMIN", "UPDATE_CUSTOMER", "Updated customer ID: " + customerId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete a customer.
     * 
     * @param customerId the customer ID
     * @return true if deleted successfully
     */
    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                logAudit("ADMIN", "DELETE_CUSTOMER", "Deleted customer ID: " + customerId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get all customers.
     * 
     * @return ResultSet containing all customers
     */
    public ResultSet getAllCustomers() {
        String sql = "SELECT * FROM customers";
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error getting all customers: " + e.getMessage());
            return null;
        }
    }
    
    // ==================== ACCOUNT OPERATIONS ====================
    
    /**
     * Create a new account.
     * 
     * @param customerId the customer ID
     * @param accountType the account type (CHECKING or SAVINGS)
     * @param initialBalance the initial balance
     * @param interestRate the interest rate (for savings accounts)
     * @return the account number if created successfully, -1 otherwise
     */
    public int createAccount(int customerId, String accountType, double initialBalance, Double interestRate) {
        String sql = "INSERT INTO accounts (customer_id, account_type, balance, interest_rate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, customerId);
            pstmt.setString(2, accountType.toUpperCase());
            pstmt.setDouble(3, initialBalance);
            if (interestRate != null) {
                pstmt.setDouble(4, interestRate);
            } else {
                pstmt.setNull(4, Types.REAL);
            }
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int accountNumber = rs.getInt(1);
                logAudit("SYSTEM", "CREATE_ACCOUNT", "Account created: " + accountNumber + " for customer: " + customerId);
                return accountNumber;
            }
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Add card information to a checking account.
     * 
     * @param accountNumber the account number
     * @param cardNumber the card number
     * @param expiryDate the expiry date
     * @param cvc the CVC
     * @return true if card was added successfully
     */
    public boolean addCardToAccount(int accountNumber, String cardNumber, String expiryDate, String cvc) {
        String sql = "UPDATE accounts SET card_number = ?, card_expiry = ?, card_cvc = ? WHERE account_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, expiryDate);
            pstmt.setString(3, cvc);
            pstmt.setInt(4, accountNumber);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                logAudit("SYSTEM", "ADD_CARD", "Card added to account: " + accountNumber);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding card: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get account by account number.
     * 
     * @param accountNumber the account number
     * @return ResultSet containing account data, or null
     */
    public ResultSet getAccountByNumber(int accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error getting account: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get all accounts for a customer.
     * 
     * @param customerId the customer ID
     * @return ResultSet containing all accounts for the customer
     */
    public ResultSet getAccountsByCustomerId(int customerId) {
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error getting accounts: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get all accounts of a specific type.
     * 
     * @param accountType the account type (CHECKING or SAVINGS)
     * @return ResultSet containing all accounts of the specified type
     */
    public ResultSet getAccountsByType(String accountType) {
        String sql = "SELECT * FROM accounts WHERE account_type = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, accountType.toUpperCase());
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error getting accounts by type: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get all accounts in the system.
     * 
     * @return ResultSet containing all accounts
     */
    public ResultSet getAllAccounts() {
        String sql = "SELECT * FROM accounts";
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error getting all accounts: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Update account balance.
     * 
     * @param accountNumber the account number
     * @param newBalance the new balance
     * @return true if balance was updated successfully
     */
    public boolean updateAccountBalance(int accountNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, accountNumber);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating balance: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update account status.
     * 
     * @param accountNumber the account number
     * @param status the new status (ACTIVE, INACTIVE, CLOSED)
     * @return true if status was updated successfully
     */
    public boolean updateAccountStatus(int accountNumber, String status) {
        String sql = "UPDATE accounts SET status = ? WHERE account_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status.toUpperCase());
            pstmt.setInt(2, accountNumber);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                logAudit("ADMIN", "UPDATE_ACCOUNT_STATUS", "Account " + accountNumber + " status changed to: " + status);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating account status: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete an account.
     * 
     * @param accountNumber the account number
     * @return true if account was deleted successfully
     */
    public boolean deleteAccount(int accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, accountNumber);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                logAudit("ADMIN", "DELETE_ACCOUNT", "Deleted account: " + accountNumber);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting account: " + e.getMessage());
        }
        return false;
    }
    
    // ==================== TRANSACTION OPERATIONS ====================
    
    /**
     * Create a new transaction.
     * 
     * @param accountNumber the account number
     * @param transactionType the transaction type (DEPOSIT, WITHDRAWAL, TRANSFER)
     * @param amount the transaction amount
     * @param description optional description
     * @return the transaction ID if created successfully, -1 otherwise
     */
    public int createTransaction(int accountNumber, String transactionType, double amount, String description) {
        String sql = "INSERT INTO transactions (account_number, transaction_type, amount, timestamp, description) " +
                     "VALUES (?, ?, ?, datetime('now'), ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, accountNumber);
            pstmt.setString(2, transactionType.toUpperCase());
            pstmt.setDouble(3, amount);
            pstmt.setString(4, description);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int transactionId = rs.getInt(1);
                logAudit("SYSTEM", "CREATE_TRANSACTION", 
                    "Transaction " + transactionId + " created for account " + accountNumber + ": " + transactionType + " $" + amount);
                return transactionId;
            }
        } catch (SQLException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Get all transactions for an account.
     * 
     * @param accountNumber the account number
     * @return ResultSet containing all transactions for the account
     */
    public ResultSet getTransactionsByAccountNumber(int accountNumber) {
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY timestamp DESC";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountNumber);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error getting transactions: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get transaction by ID.
     * 
     * @param transactionId the transaction ID
     * @return ResultSet containing transaction data, or null
     */
    public ResultSet getTransactionById(int transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, transactionId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error getting transaction: " + e.getMessage());
            return null;
        }
    }
    
    // ==================== BRANCH OPERATIONS ====================
    
    /**
     * Create a new branch.
     * 
     * @param branchName the branch name
     * @param address the branch address
     * @return the branch ID if created successfully, -1 otherwise
     */
    public int createBranch(String branchName, String address) {
        String sql = "INSERT INTO branches (branch_name, address) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, branchName);
            pstmt.setString(2, address);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int branchId = rs.getInt(1);
                logAudit("ADMIN", "CREATE_BRANCH", "Branch created: " + branchName);
                return branchId;
            }
        } catch (SQLException e) {
            System.err.println("Error creating branch: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Get all branches.
     * 
     * @return ResultSet containing all branches
     */
    public ResultSet getAllBranches() {
        String sql = "SELECT * FROM branches";
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Error getting branches: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Update branch information.
     * 
     * @param branchId the branch ID
     * @param branchName the new branch name
     * @param address the new address
     * @return true if updated successfully
     */
    public boolean updateBranch(int branchId, String branchName, String address) {
        String sql = "UPDATE branches SET branch_name = ?, address = ? WHERE branch_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, branchName);
            pstmt.setString(2, address);
            pstmt.setInt(3, branchId);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                logAudit("ADMIN", "UPDATE_BRANCH", "Updated branch ID: " + branchId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating branch: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete a branch.
     * 
     * @param branchId the branch ID
     * @return true if deleted successfully
     */
    public boolean deleteBranch(int branchId) {
        String sql = "DELETE FROM branches WHERE branch_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, branchId);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                logAudit("ADMIN", "DELETE_BRANCH", "Deleted branch ID: " + branchId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting branch: " + e.getMessage());
        }
        return false;
    }
    
    // ==================== AUDIT LOG ====================
    
    /**
     * Log an action to the audit log.
     * 
     * @param username the username performing the action
     * @param action the action performed
     * @param details additional details
     */
    private void logAudit(String username, String action, String details) {
        // The populated database uses the AUDIT_LOG schema with user_id/resource columns.
        // Skip logging if that schema is not available.
        if (!isAuditLogSupported()) {
            return;
        }

        String sql = "INSERT INTO AUDIT_LOG (action, resource, resource_id, created_at) " +
                     "VALUES (?, 'AUTH', ?, datetime('now'))";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, action);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error logging audit: " + e.getMessage());
        }
    }

    private boolean isAuditLogSupported() {
        if (auditLogSupportsUsername != null) {
            return auditLogSupportsUsername;
        }
        String sql = "PRAGMA table_info('AUDIT_LOG')";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            boolean hasAction = false;
            boolean hasResource = false;
            boolean hasResourceId = false;
            while (rs.next()) {
                String col = rs.getString("name");
                if ("action".equalsIgnoreCase(col)) hasAction = true;
                if ("resource".equalsIgnoreCase(col)) hasResource = true;
                if ("resource_id".equalsIgnoreCase(col)) hasResourceId = true;
            }
            auditLogSupportsUsername = hasAction && hasResource && hasResourceId;
        } catch (SQLException e) {
            System.err.println("Error inspecting AUDIT_LOG schema: " + e.getMessage());
            auditLogSupportsUsername = false;
        }
        return auditLogSupportsUsername;
    }

    private boolean isBcryptHash(String value) {
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }
    
    /**
     * Get audit log entries.
     * 
     * @param username optional username filter (null for all)
     * @param limit maximum number of entries to return
     * @return ResultSet containing audit log entries
     */
    public ResultSet getAuditLog(String username, int limit) {
        String sql;
        if (username != null) {
            sql = "SELECT * FROM audit_log WHERE username = ? ORDER BY timestamp DESC LIMIT ?";
        } else {
            sql = "SELECT * FROM audit_log ORDER BY timestamp DESC LIMIT ?";
        }
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            if (username != null) {
                pstmt.setString(1, username);
                pstmt.setInt(2, limit);
            } else {
                pstmt.setInt(1, limit);
            }
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error getting audit log: " + e.getMessage());
            return null;
        }
    }
}
