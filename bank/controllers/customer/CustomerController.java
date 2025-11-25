public class CustomerController {
    
    //private final CustomerView view;
    private final Customer customer;
    private final Database db;

    public CustomerController(Bank bank, CustomerView view, String username) {
        this.db = Database.getInstance();
        db.getConnection();
        
        this.customer = db.getCustomerByUsername(username);
        
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        /**
        view.addDepositListener(e -> handleDeposit());
        view.addWithdrawListener(e -> handleWithdraw());
        view.addGetAccountsListener(e -> handleGetAccounts());
        view.addViewTransactionsListener(e -> handleViewTransactions());
        view.addCreateTransactionListener(e -> handleCreateTransaction());
        */   
    }

    private void showLogin() {
        view.show();
    }

    private void handleDeposit() {
        // Initialize DepositController object which shows deposit GUI
    }

    private void handleWithdraw() {
        // Initialize WithdrawController object which shows withdraw GUI
    }

    private void handleGetAccounts() {
        // ...
    }

    private void handleViewTransactions() {
        // ...
    }

    private void handleCreateTransaction() {
        // ...
    }

}
