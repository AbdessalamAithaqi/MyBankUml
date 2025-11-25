package bank.models.security;

import bank.models.enums.PermissionAction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Permission {
    private Integer permissionId;
    private Integer roleId;
    private String resource;
    private PermissionAction action;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Permission(
            Integer permissionId,
            Integer roleId,
            String resource,
            PermissionAction action,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.permissionId = permissionId;
        this.roleId = roleId;
        this.resource = resource;
        this.action = action;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
