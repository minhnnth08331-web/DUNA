package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang customer_groups trong CSDL DUNA_MensWear.
 */
public class CustomerGroup {
    private int groupId;
    private String groupName;
    private String description;

    public CustomerGroup() {
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
