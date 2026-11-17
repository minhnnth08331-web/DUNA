package com.duna.tv4_nguyen_kieu_an.model;

/**
 * Entity anh xa bang faqs trong CSDL DUNA_MensWear.
 */
public class Faq {
    private int faqId;
    private String faqGroup;
    private String question;
    private String answer;
    private int displayOrder;
    private boolean isActive;

    public Faq() {
    }

    public int getFaqId() {
        return faqId;
    }

    public void setFaqId(int faqId) {
        this.faqId = faqId;
    }

    public String getFaqGroup() {
        return faqGroup;
    }

    public void setFaqGroup(String faqGroup) {
        this.faqGroup = faqGroup;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
