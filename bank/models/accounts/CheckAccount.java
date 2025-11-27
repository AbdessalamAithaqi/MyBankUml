package bank.models.accounts;

import bank.models.enums.AccountStatus;
import bank.models.enums.AccountType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CheckAccount extends Account {
    private Integer checkAccountId;
    private BigDecimal overdraftLimit;
    private BigDecimal monthlyFee;
 
    public CheckAccount(
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
            LocalDateTime updatedAt,

            Integer checkAccountId,
            BigDecimal overdraftLimit,
            BigDecimal monthlyFee
    ) {
        super(accountId, accountNumber, customerId, accountType, balance, status,
              branchId, openedDate, closedDate, createdAt, updatedAt);
              
        this.checkAccountId = checkAccountId;
        this.overdraftLimit = overdraftLimit;
        this.monthlyFee = monthlyFee;
    }
}
