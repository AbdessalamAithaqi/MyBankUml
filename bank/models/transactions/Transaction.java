package bank.models.transactions;

import bank.models.enums.TransactionStatus;
import bank.models.enums.TransactionType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Transaction {
    private Integer transactionId;
    private Integer fromAccountId;
    private Integer toAccountId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String description;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Transaction(
            Integer transactionId,
            Integer fromAccountId,
            Integer toAccountId,
            BigDecimal amount,
            TransactionType transactionType,
            TransactionStatus status,
            String description,
            LocalDateTime transactionDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.description = description;
        this.transactionDate = transactionDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
