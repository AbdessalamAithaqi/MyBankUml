package bank.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Database class for managing SQLite database operations.
 * Provides CRUD operations for User, Customer, Account, and Transaction entities.
 */
public class Database {
    private static Database instance;
    private Connection connection;
    private final String url;
    
    // Private constructor to prevent external instantiation
    private Database() {
        // Default database location - can be overridden
        this.url = "jdbc:sqlite:bank.db";
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
     * Authenticate a user.
     * 
     * @param username the username
     * @param password the password
     * @return the user's role if authentication succeeds, null otherwise
     */
    public String authenticateUser(String username, String password) {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                logAudit(username, "LOGIN_SUCCESS", "User logged in successfully");
                return role;
            } else {
                logAudit(username, "LOGIN_FAILED", "Failed login attempt");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get user role.
     * 
     * @param username the username
     * @return the user's role or null if not found
     */
    public String getUserRole(String username) {
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
        String sql = "INSERT INTO audit_log (username, action, timestamp, details) " +
                     "VALUES (?, ?, datetime('now'), ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, action);
            pstmt.setString(3, details);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error logging audit: " + e.getMessage());
        }
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