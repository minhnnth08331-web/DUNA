package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang login_histories trong CSDL DUNA_MensWear.
 */
public class LoginHistory {
    private long loginId;
    private int userId;
    private String emailAttempt;
    private String ipAddress;
    private String deviceInfo;
    private boolean isSuccess;
    private String failReason;
    private String loginAt;

    public LoginHistory() {
    }

    public long getLoginId() {
        return loginId;
    }

    public void setLoginId(long loginId) {
        this.loginId = loginId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmailAttempt() {
        return emailAttempt;
    }

    public void setEmailAttempt(String emailAttempt) {
        this.emailAttempt = emailAttempt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(String loginAt) {
        this.loginAt = loginAt;
    }
}
