package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang customer_measurements trong CSDL DUNA_MensWear.
 */
public class CustomerMeasurement {
    private int measurementId;
    private int customerId;
    private double heightCm;
    private double weightKg;
    private double chestCm;
    private double waistCm;
    private double hipCm;
    private double shoulderCm;
    private String measuredAt;

    public CustomerMeasurement() {
    }

    public int getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(int measurementId) {
        this.measurementId = measurementId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(double heightCm) {
        this.heightCm = heightCm;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public double getChestCm() {
        return chestCm;
    }

    public void setChestCm(double chestCm) {
        this.chestCm = chestCm;
    }

    public double getWaistCm() {
        return waistCm;
    }

    public void setWaistCm(double waistCm) {
        this.waistCm = waistCm;
    }

    public double getHipCm() {
        return hipCm;
    }

    public void setHipCm(double hipCm) {
        this.hipCm = hipCm;
    }

    public double getShoulderCm() {
        return shoulderCm;
    }

    public void setShoulderCm(double shoulderCm) {
        this.shoulderCm = shoulderCm;
    }

    public String getMeasuredAt() {
        return measuredAt;
    }

    public void setMeasuredAt(String measuredAt) {
        this.measuredAt = measuredAt;
    }
}
