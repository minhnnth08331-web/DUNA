package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang reward_redemptions trong CSDL DUNA_MensWear.
 */
public class RewardRedemption {
    private int redemptionId;
    private int accountId;
    private int rewardId;
    private int pointsUsed;
    private String status;
    private String redeemedAt;

    public RewardRedemption() {
    }

    public int getRedemptionId() {
        return redemptionId;
    }

    public void setRedemptionId(int redemptionId) {
        this.redemptionId = redemptionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(int pointsUsed) {
        this.pointsUsed = pointsUsed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRedeemedAt() {
        return redeemedAt;
    }

    public void setRedeemedAt(String redeemedAt) {
        this.redeemedAt = redeemedAt;
    }
}
