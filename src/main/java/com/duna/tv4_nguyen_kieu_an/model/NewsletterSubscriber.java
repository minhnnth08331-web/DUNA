package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang newsletter_subscribers trong CSDL DUNA_MensWear.
 */
public class NewsletterSubscriber {
    private int subscriberId;
    private String email;
    private boolean isActive;
    private String subscribedAt;

    public NewsletterSubscriber() {
    }

    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getSubscribedAt() {
        return subscribedAt;
    }

    public void setSubscribedAt(String subscribedAt) {
        this.subscribedAt = subscribedAt;
    }
}
