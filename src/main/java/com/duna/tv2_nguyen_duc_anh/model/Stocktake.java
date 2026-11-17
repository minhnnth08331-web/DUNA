package com.duna.tv2_nguyen_duc_anh.model;

/**
 * Entity anh xa bang stocktakes trong CSDL DUNA_MensWear.
 */
public class Stocktake {
    private int stocktakeId;
    private String stocktakeCode;
    private int warehouseId;
    private String status;
    private int createdBy;
    private String startedAt;
    private String completedAt;

    public Stocktake() {
    }

    public int getStocktakeId() {
        return stocktakeId;
    }

    public void setStocktakeId(int stocktakeId) {
        this.stocktakeId = stocktakeId;
    }

    public String getStocktakeCode() {
        return stocktakeCode;
    }

    public void setStocktakeCode(String stocktakeCode) {
        this.stocktakeCode = stocktakeCode;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
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

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}
