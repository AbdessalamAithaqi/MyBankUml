package bank.models.users;

import bank.models.enums.AccessLevel;
import bank.models.enums.UserType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Administrator extends User {
    private Integer adminId;
    private String firstName;
    private String lastName;
    private AccessLevel accessLevel;

    public Administrator(
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

            Integer adminId,
            String firstName,
            String lastName,
            AccessLevel accessLevel
    ) {
        super(userId, username, passwordHash, email, userType, isActive, lastLogin,
              failedLoginAttempts, accountLockedUntil, userCreatedAt, userUpdatedAt);
        
        this.adminId = adminId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessLevel = accessLevel;
    }
}
