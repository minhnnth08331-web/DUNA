package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang employee_shifts trong CSDL DUNA_MensWear.
 */
public class EmployeeShift {
    private int employeeShiftId;
    private int employeeId;
    private int shiftId;
    private int storeId;
    private String workDate;

    public EmployeeShift() {
    }

    public int getEmployeeShiftId() {
        return employeeShiftId;
    }

    public void setEmployeeShiftId(int employeeShiftId) {
        this.employeeShiftId = employeeShiftId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }
}
