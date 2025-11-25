package bank.models.org;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Branch {
    private Integer branchId;
    private Integer bankId;
    private String branchName;
    private String address;
    private String phone;
    private Integer managerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Branch(
            Integer branchId,
            Integer bankId,
            String branchName,
            String address,
            String phone,
            Integer managerId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.branchId = branchId;
        this.bankId = bankId;
        this.branchName = branchName;
        this.address = address;
        this.phone = phone;
        this.managerId = managerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
