package bank.models.org;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Bank {
    private Integer bankId;
    private String name;
    private String headquartersAddress;
    private String swiftCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Bank(
            Integer bankId,
            String name,
            String headquartersAddress,
            String swiftCode,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.bankId = bankId;
        this.name = name;
        this.headquartersAddress = headquartersAddress;
        this.swiftCode = swiftCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
