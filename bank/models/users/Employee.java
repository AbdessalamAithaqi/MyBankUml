package bank.models.users;

import bank.models.enums.UserType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class Employee extends User {
    private Integer employeeId;
    private Integer branchId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private LocalDate hireDate;
    private Boolean isTeller;
    private Boolean isManager;

    public Employee(
            Integer userId,
            String username,
            String passwordHash,
            String email,
            UserType userType,
            Boolean isActive,
            LocalDateTime lastLogin,
            Integer failedLoginAttempts,
            LocalDateTime accountLockedUntil,
            LocalDateTime userCreatedAt,
            LocalDateTime userUpdatedAt,

            Integer employeeId,
            Integer branchId,
            String firstName,
            String lastName,
            String position,
            String department,
            LocalDate hireDate,
            Boolean isTeller,
            Boolean isManager
    ) {
        super(userId, username, passwordHash, email, userType, isActive, lastLogin,
              failedLoginAttempts, accountLockedUntil, userCreatedAt, userUpdatedAt);
        
        this.employeeId = employeeId;
        this.branchId = branchId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = department;
        this.hireDate = hireDate;
        this.isTeller = isTeller;
        this.isManager = isManager;
    }
}
