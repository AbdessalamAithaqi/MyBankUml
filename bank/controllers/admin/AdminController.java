public class AdminController {
    //private final AdminView view;
    //private final Admininstrator admin;
    private final Database db;

    public AdminController(Bank bank, AdminView view, String username) {
        this.db = Database.getInstance();
        db.getConnection();
        
        //this.admin = db.(...);
        
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
        
        view.addModifyAccountListener();
        view.addUserListener();
        view.deleteUserListener();
        view.modifyUserListener()l
        */
    }

    private void showLogin() {
        view.show();
    }
}
