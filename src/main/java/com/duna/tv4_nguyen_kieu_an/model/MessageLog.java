package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang message_logs trong CSDL DUNA_MensWear.
 */
public class MessageLog {
    private long messageLogId;
    private String channel;
    private String recipient;
    private String subject;
    private String status;
    private String errorMessage;
    private String createdAt;

    public MessageLog() {
    }

    public long getMessageLogId() {
        return messageLogId;
    }

    public void setMessageLogId(long messageLogId) {
        this.messageLogId = messageLogId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
