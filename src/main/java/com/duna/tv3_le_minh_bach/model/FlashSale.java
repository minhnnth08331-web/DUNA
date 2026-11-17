package com.duna.tv3_le_minh_bach.model;

/**
 * Entity anh xa bang flash_sales trong CSDL DUNA_MensWear.
 */
public class FlashSale {
    private int flashSaleId;
    private String title;
    private String startTime;
    private String endTime;
    private String status;

    public FlashSale() {
    }

    public int getFlashSaleId() {
        return flashSaleId;
    }

    public void setFlashSaleId(int flashSaleId) {
        this.flashSaleId = flashSaleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
