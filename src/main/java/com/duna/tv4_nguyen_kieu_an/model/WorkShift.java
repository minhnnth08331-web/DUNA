package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang work_shifts trong CSDL DUNA_MensWear.
 */
public class WorkShift {
    private int shiftId;
    private String shiftName;
    private String startTime;
    private String endTime;

    public WorkShift() {
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
