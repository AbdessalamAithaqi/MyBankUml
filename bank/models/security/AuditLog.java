package bank.models.security;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuditLog {
    private Integer auditId;
    private Integer userId;
    private String action;
    private String resource;
    private String resourceId;
    private String oldValue;
    private String newValue;
    private String ipAddress;
    private LocalDateTime createdAt;

    public AuditLog(
            Integer auditId,
            Integer userId,
            String action,
            String resource,
            String resourceId,
            String oldValue,
            String newValue,
            String ipAddress,
            LocalDateTime createdAt
    ) {
        this.auditId = auditId;
        this.userId = userId;
        this.action = action;
        this.resource = resource;
        this.resourceId = resourceId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }
}
