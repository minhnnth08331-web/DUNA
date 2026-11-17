package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang cash_drawer_transactions trong CSDL DUNA_MensWear.
 */
public class CashDrawerTransaction {
    private int drawerTxnId;
    private int posSessionId;
    private String txnType;
    private double amount;
    private long orderId;
    private String note;
    private String createdAt;

    public CashDrawerTransaction() {
    }

    public int getDrawerTxnId() {
        return drawerTxnId;
    }

    public void setDrawerTxnId(int drawerTxnId) {
        this.drawerTxnId = drawerTxnId;
    }

    public int getPosSessionId() {
        return posSessionId;
    }

    public void setPosSessionId(int posSessionId) {
        this.posSessionId = posSessionId;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
