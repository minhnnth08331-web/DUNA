package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang membership_tiers trong CSDL DUNA_MensWear.
 */
public class MembershipTier {
    private int tierId;
    private String tierName;
    private double minSpend;
    private double discountPercent;
    private double pointMultiplier;
    private String benefits;

    public MembershipTier() {
    }

    public int getTierId() {
        return tierId;
    }

    public void setTierId(int tierId) {
        this.tierId = tierId;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public double getMinSpend() {
        return minSpend;
    }

    public void setMinSpend(double minSpend) {
        this.minSpend = minSpend;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double getPointMultiplier() {
        return pointMultiplier;
    }

    public void setPointMultiplier(double pointMultiplier) {
        this.pointMultiplier = pointMultiplier;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }
}
