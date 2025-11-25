package bank.models.security;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Authentication {
    private Integer authId;
    private Integer userId;
    private String sessionToken;
    private LocalDateTime expiresAt;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;

    public Authentication(
            Integer authId,
            Integer userId,
            String sessionToken,
            LocalDateTime expiresAt,
            String ipAddress,
            String userAgent,
            LocalDateTime createdAt
    ) {
        this.authId = authId;
        this.userId = userId;
        this.sessionToken = sessionToken;
        this.expiresAt = expiresAt;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.createdAt = createdAt;
    }
}
