package com.duna.common.entities;

import java.io.Serializable;

/**
 * Entity ánh xạ bảng MauSac (Màu sắc sản phẩm thời trang nam: Đen Than, Xanh Navy, Trắng, Ghi Xám...)
 */
public class MauSac implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String maMau;
    private String tenMau;
    private String maHex;

    public MauSac() {}

    public MauSac(int id, String maMau, String tenMau, String maHex) {
        this.id = id;
        this.maMau = maMau;
        this.tenMau = tenMau;
        this.maHex = maHex;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaMau() { return maMau; }
    public void setMaMau(String maMau) { this.maMau = maMau; }

    public String getTenMau() { return tenMau; }
    public void setTenMau(String tenMau) { this.tenMau = tenMau; }

    public String getMaHex() { return maHex; }
    public void setMaHex(String maHex) { this.maHex = maHex; }
}