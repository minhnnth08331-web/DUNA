package com.duna.tv3_le_minh_bach.model;

/**
 * Entity anh xa bang shipping_methods trong CSDL DUNA_MensWear.
 */
public class ShippingMethod {
    private int shippingMethodId;
    private int carrierId;
    private String methodName;
    private int estimatedDaysMin;
    private int estimatedDaysMax;
    private boolean isActive;

    public ShippingMethod() {
    }

    public int getShippingMethodId() {
        return shippingMethodId;
    }

    public void setShippingMethodId(int shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }

    public int getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(int carrierId) {
        this.carrierId = carrierId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getEstimatedDaysMin() {
        return estimatedDaysMin;
    }

    public void setEstimatedDaysMin(int estimatedDaysMin) {
        this.estimatedDaysMin = estimatedDaysMin;
    }

    public int getEstimatedDaysMax() {
        return estimatedDaysMax;
    }

    public void setEstimatedDaysMax(int estimatedDaysMax) {
        this.estimatedDaysMax = estimatedDaysMax;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
