package com.duna.tv3_le_minh_bach.model;

/**
 * Entity anh xa bang payment_transactions trong CSDL DUNA_MensWear.
 */
public class PaymentTransaction {
    private long txnId;
    private long paymentId;
    private String gateway;
    private String gatewayTxnNo;
    private String responseCode;
    private double amount;
    private String rawResponse;
    private String createdAt;

    public PaymentTransaction() {
    }

    public long getTxnId() {
        return txnId;
    }

    public void setTxnId(long txnId) {
        this.txnId = txnId;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getGatewayTxnNo() {
        return gatewayTxnNo;
    }

    public void setGatewayTxnNo(String gatewayTxnNo) {
        this.gatewayTxnNo = gatewayTxnNo;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
