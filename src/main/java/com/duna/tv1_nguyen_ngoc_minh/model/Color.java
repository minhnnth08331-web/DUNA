package com.duna.tv1_nguyen_ngoc_minh.model;

/**
 * Entity anh xa bang colors trong CSDL DUNA_MensWear.
 */
public class Color {
    private int colorId;
    private String colorName;
    private String hexCode;
    private String colorGroup;

    public Color() {
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getColorGroup() {
        return colorGroup;
    }

    public void setColorGroup(String colorGroup) {
        this.colorGroup = colorGroup;
    }
}
