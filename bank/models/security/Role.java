package bank.models.security;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Role {
    private Integer roleId;
    private String roleName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Role(
            Integer roleId,
            String roleName,
            String description,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
