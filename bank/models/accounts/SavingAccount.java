package bank.models.accounts;

import bank.models.enums.AccountStatus;
import bank.models.enums.AccountType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class SavingAccount extends Account {
    private Integer savingAccountId;
    private BigDecimal interestRate;
    private BigDecimal minimumBalance;

    public SavingAccount(
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

            Integer savingAccountId,
            BigDecimal interestRate,
            BigDecimal minimumBalance
    ) {
        super(accountId, accountNumber, customerId, accountType, balance, status,
              branchId, openedDate, closedDate, createdAt, updatedAt);
              
        this.savingAccountId = savingAccountId;
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }
}
