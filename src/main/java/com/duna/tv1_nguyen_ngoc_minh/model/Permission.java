package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang permissions trong CSDL DUNA_MensWear.
 */
public class Permission {
    private int permissionId;
    private String permissionCode;
    private String permissionName;
    private String moduleName;

    public Permission() {
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
