package bank.models.users;

import bank.models.enums.UserType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class User {
    private Integer userId;
    private String username;
    private String passwordHash;
    private String email;
    private UserType userType;
    private Boolean isActive;
    private LocalDateTime lastLogin;
    private Integer failedLoginAttempts;
    private LocalDateTime accountLockedUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Explicit getter to ensure availability even if Lombok is not processed.
    public String getUsername() {
        return username;
    }

    public User(
            Integer userId,
            String username,
            String passwordHash,
            String email,
            UserType userType,
            Boolean isActive,
            LocalDateTime lastLogin,
            Integer failedLoginAttempts,
            LocalDateTime accountLockedUntil,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.userType = userType;
        this.isActive = isActive;
        this.lastLogin = lastLogin;
        this.failedLoginAttempts = failedLoginAttempts;
        this.accountLockedUntil = accountLockedUntil;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
