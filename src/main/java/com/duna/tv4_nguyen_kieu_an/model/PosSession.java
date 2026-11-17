package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang pos_sessions trong CSDL DUNA_MensWear.
 */
public class PosSession {
    private int posSessionId;
    private int storeId;
    private int cashierId;
    private double openingCash;
    private double closingCash;
    private double expectedCash;
    private String status;
    private String openedAt;
    private String closedAt;

    public PosSession() {
    }

    public int getPosSessionId() {
        return posSessionId;
    }

    public void setPosSessionId(int posSessionId) {
        this.posSessionId = posSessionId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getCashierId() {
        return cashierId;
    }

    public void setCashierId(int cashierId) {
        this.cashierId = cashierId;
    }

    public double getOpeningCash() {
        return openingCash;
    }

    public void setOpeningCash(double openingCash) {
        this.openingCash = openingCash;
    }

    public double getClosingCash() {
        return closingCash;
    }

    public void setClosingCash(double closingCash) {
        this.closingCash = closingCash;
    }

    public double getExpectedCash() {
        return expectedCash;
    }

    public void setExpectedCash(double expectedCash) {
        this.expectedCash = expectedCash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(String openedAt) {
        this.openedAt = openedAt;
    }

    public String getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }
}
