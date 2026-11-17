package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang role_permissions trong CSDL DUNA_MensWear.
 */
public class RolePermission {
    private int rolePermissionId;
    private int roleId;
    private int permissionId;

    public RolePermission() {
    }

    public int getRolePermissionId() {
        return rolePermissionId;
    }

    public void setRolePermissionId(int rolePermissionId) {
        this.rolePermissionId = rolePermissionId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }
}
