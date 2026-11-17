package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang email_campaigns trong CSDL DUNA_MensWear.
 */
public class EmailCampaign {
    private int campaignId;
    private String campaignName;
    private int templateId;
    private int targetGroupId;
    private String status;
    private String scheduledAt;
    private String sentAt;
    private int totalRecipients;
    private int createdBy;
    private String createdAt;

    public EmailCampaign() {
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getTargetGroupId() {
        return targetGroupId;
    }

    public void setTargetGroupId(int targetGroupId) {
        this.targetGroupId = targetGroupId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(String scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public int getTotalRecipients() {
        return totalRecipients;
    }

    public void setTotalRecipients(int totalRecipients) {
        this.totalRecipients = totalRecipients;
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
}
