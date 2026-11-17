package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang positions trong CSDL DUNA_MensWear.
 */
public class Position {
    private int positionId;
    private String positionName;
    private double baseSalary;

    public Position() {
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }
}
