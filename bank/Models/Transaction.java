package Models;

import lombok.Getter;

@Getter
public class Transaction {
    
    private int amount;
    private String senderID;
    private String receiverID;

    public Transaction(int amount, String senderID, String receiverID){
        this.amount = amount;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    public void receipt() {
        System.out.println("Transaction receipt.");
    }
}
