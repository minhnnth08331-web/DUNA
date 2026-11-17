package com.duna.tv3_le_minh_bach.model;

/**
 * Entity anh xa bang shipping_fees trong CSDL DUNA_MensWear.
 */
public class ShippingFee {
    private int feeId;
    private int shippingMethodId;
    private int zoneId;
    private int minWeightGram;
    private int maxWeightGram;
    private double baseFee;
    private double freeShipThreshold;

    public ShippingFee() {
    }

    public int getFeeId() {
        return feeId;
    }

    public void setFeeId(int feeId) {
        this.feeId = feeId;
    }

    public int getShippingMethodId() {
        return shippingMethodId;
    }

    public void setShippingMethodId(int shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getMinWeightGram() {
        return minWeightGram;
    }

    public void setMinWeightGram(int minWeightGram) {
        this.minWeightGram = minWeightGram;
    }

    public int getMaxWeightGram() {
        return maxWeightGram;
    }

    public void setMaxWeightGram(int maxWeightGram) {
        this.maxWeightGram = maxWeightGram;
    }

    public double getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(double baseFee) {
        this.baseFee = baseFee;
    }

    public double getFreeShipThreshold() {
        return freeShipThreshold;
    }

    public void setFreeShipThreshold(double freeShipThreshold) {
        this.freeShipThreshold = freeShipThreshold;
    }
}
