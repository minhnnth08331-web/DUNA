package com.duna.tv3_le_minh_bach.model;

/**
 * Entity anh xa bang cod_reconciliations trong CSDL DUNA_MensWear.
 */
public class CodReconciliation {
    private int reconciliationId;
    private int carrierId;
    private String periodFrom;
    private String periodTo;
    private double totalCod;
    private double totalFee;
    private double netAmount;
    private String status;
    private String reconciledAt;
    private int createdBy;

    public CodReconciliation() {
    }

    public int getReconciliationId() {
        return reconciliationId;
    }

    public void setReconciliationId(int reconciliationId) {
        this.reconciliationId = reconciliationId;
    }

    public int getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(int carrierId) {
        this.carrierId = carrierId;
    }

    public String getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(String periodFrom) {
        this.periodFrom = periodFrom;
    }

    public String getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(String periodTo) {
        this.periodTo = periodTo;
    }

    public double getTotalCod() {
        return totalCod;
    }

    public void setTotalCod(double totalCod) {
        this.totalCod = totalCod;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReconciledAt() {
        return reconciledAt;
    }

    public void setReconciledAt(String reconciledAt) {
        this.reconciledAt = reconciledAt;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}
