public class EmployeeController {
    //private final EmployeeView view;
    //private final Employee employee;
    private final Database db;
    //private final Customer customer; To be set when we perform an action on behalf of a Customer like when depositing, we take it from the Customer name input of the view.

    public EmployeeController(Bank bank, EmployeeView view, String username) {
        this.db = Database.getInstance();
        db.getConnection();
        
        //this.employee = db.(...);
        
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        /**
        view.addDepositListener(e -> func());
        view.addWithdrawListener(e -> func());
        view.addGetAccountsListener(e -> func());
        view.addViewTransactionsListener(e -> func());
        view.addCreateTransactionListener(e -> func());
        
        view.addGetAllAccountsListener(e -> func());
        view.addGetAccountByIdListener(e -> func());
        view.addGetAccountByTypeListener(e -> func());
        */
    }

    private void showLogin() {
        view.show();
    }
}
