package bank.models.users;

import bank.models.enums.UserType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Customer extends User {
    private Integer customerId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String birthplace;
    private String ssnMasked;
    private String phone;
    private String address;
    private Integer branchId;

    public Customer(
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
            
            Integer customerId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String birthplace,
            String ssnMasked,
            String phone,
            String address,
            Integer branchId
    ) {
        super(userId, username, passwordHash, email, userType, isActive, lastLogin,
              failedLoginAttempts, accountLockedUntil, userCreatedAt, userUpdatedAt);

        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.birthplace = birthplace;
        this.ssnMasked = ssnMasked;
        this.phone = phone;
        this.address = address;
        this.branchId = branchId;
    }

    // Convenience constructor for GUI/testing use when we only have a display name.
    public Customer(String displayName) {
        super(null, displayName, null, null, UserType.CUSTOMER, true,
              null, 0, null, LocalDateTime.now(), LocalDateTime.now());
        this.firstName = displayName;
        this.lastName = "";
    }

    public String getName() {
        if (firstName != null && !firstName.isBlank()) {
            if (lastName != null && !lastName.isBlank()) {
                return firstName + " " + lastName;
            }
            return firstName;
        }
        return Objects.requireNonNullElse(getUsername(), "Customer");
    }
}
