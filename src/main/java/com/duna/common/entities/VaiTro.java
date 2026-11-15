package com.duna.common.entities;

import java.io.Serializable;

/**
 * Entity ánh xạ bảng VaiTro (1: Quản trị viên / Quản lý, 2: Nhân viên bán hàng POS)
 */
public class VaiTro implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String maVaiTro;
    private String tenVaiTro;
    private String moTaQuyenHan;

    public VaiTro() {}

    public VaiTro(int id, String maVaiTro, String tenVaiTro, String moTaQuyenHan) {
        this.id = id;
        this.maVaiTro = maVaiTro;
        this.tenVaiTro = tenVaiTro;
        this.moTaQuyenHan = moTaQuyenHan;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaVaiTro() { return maVaiTro; }
    public void setMaVaiTro(String maVaiTro) { this.maVaiTro = maVaiTro; }

    public String getTenVaiTro() { return tenVaiTro; }
    public void setTenVaiTro(String tenVaiTro) { this.tenVaiTro = tenVaiTro; }

    public String getMoTaQuyenHan() { return moTaQuyenHan; }
    public void setMoTaQuyenHan(String moTaQuyenHan) { this.moTaQuyenHan = moTaQuyenHan; }
}