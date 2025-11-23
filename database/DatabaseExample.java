package bank.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Example demonstrating how to use the Database singleton.
 */
public class DatabaseExample {
    public static void main(String[] args) {
        // Get the singleton instance
        Database db = Database.getInstance();
        
        System.out.println("=== Database Singleton Example ===\n");
        
        // Example 1: Create a user and customer
        System.out.println("1. Creating user and customer...");
        boolean userCreated = db.createUser("john_doe", "hashed_password123", "CUSTOMER");
        if (userCreated) {
            System.out.println("✓ User created successfully");
            
            int customerId = db.createCustomer("john_doe", "John Doe", "Montreal", "1990-05-15");
            if (customerId != -1) {
                System.out.println("✓ Customer created with ID: " + customerId);
                
                // Example 2: Create accounts
                System.out.println("\n2. Creating accounts...");
                int checkingAccount = db.createAccount(customerId, "CHECKING", 1000.0, null);
                int savingsAccount = db.createAccount(customerId, "SAVINGS", 5000.0, 2.5);
                
                System.out.println("✓ Checking account created: " + checkingAccount);
                System.out.println("✓ Savings account created: " + savingsAccount);
                
                // Add card to checking account
                db.addCardToAccount(checkingAccount, "4532-1234-5678-9010", "12/25", "123");
                System.out.println("✓ Card added to checking account");
                
                // Example 3: Create transactions
                System.out.println("\n3. Creating transactions...");
                int depositId = db.createTransaction(checkingAccount, "DEPOSIT", 500.0, "Initial deposit");
                int withdrawalId = db.createTransaction(savingsAccount, "WITHDRAWAL", 200.0, "ATM withdrawal");
                
                System.out.println("✓ Deposit transaction created: " + depositId);
                System.out.println("✓ Withdrawal transaction created: " + withdrawalId);
                
                // Update balances
                db.updateAccountBalance(checkingAccount, 1500.0);
                db.updateAccountBalance(savingsAccount, 4800.0);
                
                // Example 4: Query account information
                System.out.println("\n4. Querying account information...");
                try (ResultSet accountRs = db.getAccountsByCustomerId(customerId)) {
                    while (accountRs.next()) {
                        System.out.println("Account #" + accountRs.getInt("account_number") + 
                                         " | Type: " + accountRs.getString("account_type") +
                                         " | Balance: $" + accountRs.getDouble("balance"));
                    }
                } catch (SQLException e) {
                    System.err.println("Error querying accounts: " + e.getMessage());
                }
                
                // Example 5: Query transactions
                System.out.println("\n5. Querying transactions...");
                try (ResultSet transRs = db.getTransactionsByAccountNumber(checkingAccount)) {
                    while (transRs.next()) {
                        System.out.println("Transaction #" + transRs.getInt("transaction_id") +
                                         " | Type: " + transRs.getString("transaction_type") +
                                         " | Amount: $" + transRs.getDouble("amount") +
                                         " | Time: " + transRs.getString("timestamp"));
                    }
                } catch (SQLException e) {
                    System.err.println("Error querying transactions: " + e.getMessage());
                }
            }
        }
        
        // Example 6: Authentication
        System.out.println("\n6. Testing authentication...");
        String role = db.authenticateUser("john_doe", "hashed_password123");
        if (role != null) {
            System.out.println("✓ Authentication successful! User role: " + role);
        } else {
            System.out.println("✗ Authentication failed");
        }
        
        // Example 7: Teller operations - Get all accounts
        System.out.println("\n7. Teller operation - Getting all accounts...");
        try (ResultSet allAccounts = db.getAllAccounts()) {
            int count = 0;
            while (allAccounts.next()) {
                count++;
            }
            System.out.println("✓ Total accounts in system: " + count);
        } catch (SQLException e) {
            System.err.println("Error getting all accounts: " + e.getMessage());
        }
        
        // Example 8: Search functionality
        System.out.println("\n8. Searching customers by birthplace...");
        try (ResultSet searchResults = db.searchCustomersByBirthplace("Montreal")) {
            while (searchResults.next()) {
                System.out.println("Found customer: " + searchResults.getString("name") +
                                 " from " + searchResults.getString("place_of_birth"));
            }
        } catch (SQLException e) {
            System.err.println("Error searching customers: " + e.getMessage());
        }
        
        // Example 9: Create a teller user
        System.out.println("\n9. Creating a teller user...");
        db.createUser("teller_jane", "hashed_password456", "TELLER");
        System.out.println("✓ Teller user created");
        
        // Example 10: View audit log
        System.out.println("\n10. Viewing recent audit log entries...");
        try (ResultSet auditLog = db.getAuditLog(null, 5)) {
            while (auditLog.next()) {
                System.out.println("[" + auditLog.getString("timestamp") + "] " +
                                 auditLog.getString("username") + " - " +
                                 auditLog.getString("action") + ": " +
                                 auditLog.getString("details"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting audit log: " + e.getMessage());
        }
        
        System.out.println("\n=== Example Complete ===");
        
        // Close the database connection when done
        db.close();
    }
}