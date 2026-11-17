package com.duna.tv2_nguyen_duc_anh.model;

/**
 * Entity anh xa bang stock_transfers trong CSDL DUNA_MensWear.
 */
public class StockTransfer {
    private int transferId;
    private String transferCode;
    private int fromWarehouseId;
    private int toWarehouseId;
    private String status;
    private int createdBy;
    private String createdAt;
    private String completedAt;

    public StockTransfer() {
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getTransferCode() {
        return transferCode;
    }

    public void setTransferCode(String transferCode) {
        this.transferCode = transferCode;
    }

    public int getFromWarehouseId() {
        return fromWarehouseId;
    }

    public void setFromWarehouseId(int fromWarehouseId) {
        this.fromWarehouseId = fromWarehouseId;
    }

    public int getToWarehouseId() {
        return toWarehouseId;
    }

    public void setToWarehouseId(int toWarehouseId) {
        this.toWarehouseId = toWarehouseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}
