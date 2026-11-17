package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang departments trong CSDL DUNA_MensWear.
 */
public class Department {
    private int departmentId;
    private String departmentName;
    private String description;

    public Department() {
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
