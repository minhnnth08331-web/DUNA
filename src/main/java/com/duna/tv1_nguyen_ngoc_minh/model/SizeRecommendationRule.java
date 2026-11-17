package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang size_recommendation_rules trong CSDL DUNA_MensWear.
 */
public class SizeRecommendationRule {
    private int ruleId;
    private int categoryId;
    private String formFit;
    private double heightMin;
    private double heightMax;
    private double weightMin;
    private double weightMax;
    private int recommendedSizeId;

    public SizeRecommendationRule() {
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getFormFit() {
        return formFit;
    }

    public void setFormFit(String formFit) {
        this.formFit = formFit;
    }

    public double getHeightMin() {
        return heightMin;
    }

    public void setHeightMin(double heightMin) {
        this.heightMin = heightMin;
    }

    public double getHeightMax() {
        return heightMax;
    }

    public void setHeightMax(double heightMax) {
        this.heightMax = heightMax;
    }

    public double getWeightMin() {
        return weightMin;
    }

    public void setWeightMin(double weightMin) {
        this.weightMin = weightMin;
    }

    public double getWeightMax() {
        return weightMax;
    }

    public void setWeightMax(double weightMax) {
        this.weightMax = weightMax;
    }

    public int getRecommendedSizeId() {
        return recommendedSizeId;
    }

    public void setRecommendedSizeId(int recommendedSizeId) {
        this.recommendedSizeId = recommendedSizeId;
    }
}
