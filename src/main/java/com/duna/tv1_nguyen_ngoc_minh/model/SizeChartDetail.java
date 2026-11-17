package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang size_chart_details trong CSDL DUNA_MensWear.
 */
public class SizeChartDetail {
    private int chartDetailId;
    private int categoryId;
    private int sizeId;
    private double minHeightCm;
    private double maxHeightCm;
    private double minWeightKg;
    private double maxWeightKg;
    private double minChestCm;
    private double maxChestCm;
    private double minWaistCm;
    private double maxWaistCm;

    public SizeChartDetail() {
    }

    public int getChartDetailId() {
        return chartDetailId;
    }

    public void setChartDetailId(int chartDetailId) {
        this.chartDetailId = chartDetailId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public double getMinHeightCm() {
        return minHeightCm;
    }

    public void setMinHeightCm(double minHeightCm) {
        this.minHeightCm = minHeightCm;
    }

    public double getMaxHeightCm() {
        return maxHeightCm;
    }

    public void setMaxHeightCm(double maxHeightCm) {
        this.maxHeightCm = maxHeightCm;
    }

    public double getMinWeightKg() {
        return minWeightKg;
    }

    public void setMinWeightKg(double minWeightKg) {
        this.minWeightKg = minWeightKg;
    }

    public double getMaxWeightKg() {
        return maxWeightKg;
    }

    public void setMaxWeightKg(double maxWeightKg) {
        this.maxWeightKg = maxWeightKg;
    }

    public double getMinChestCm() {
        return minChestCm;
    }

    public void setMinChestCm(double minChestCm) {
        this.minChestCm = minChestCm;
    }

    public double getMaxChestCm() {
        return maxChestCm;
    }

    public void setMaxChestCm(double maxChestCm) {
        this.maxChestCm = maxChestCm;
    }

    public double getMinWaistCm() {
        return minWaistCm;
    }

    public void setMinWaistCm(double minWaistCm) {
        this.minWaistCm = minWaistCm;
    }

    public double getMaxWaistCm() {
        return maxWaistCm;
    }

    public void setMaxWaistCm(double maxWaistCm) {
        this.maxWaistCm = maxWaistCm;
    }
}
