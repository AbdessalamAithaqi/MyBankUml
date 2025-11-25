package bank.models.accounts;

import bank.models.enums.AccountStatus;
import bank.models.enums.AccountType;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public abstract class Account {
    private Integer accountId;
    private String accountNumber;
    private Integer customerId;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status;
    private Integer branchId;
    private LocalDate openedDate;
    private LocalDate closedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Account(
            Integer accountId, 
            String accountNumber, 
            Integer customerId, 
            AccountType accountType,
            BigDecimal balance, 
            AccountStatus status, 
            Integer branchId, 
            LocalDate openedDate, 
            LocalDate closedDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
        this.branchId = branchId;
        this.openedDate = openedDate;
        this.closedDate = closedDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
