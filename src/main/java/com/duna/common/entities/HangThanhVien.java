package com.duna.common.entities;

import java.io.Serializable;

/**
 * Entity ánh xạ bảng HangThanhVien (Thường, Bạc, Vàng, VIP theo chu kỳ 3 tháng)
 */
public class HangThanhVien implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String maHang;
    private String tenHang;
    private long mocChiTieuToiThieu;
    private int phanTramGiamGia;
    private String dacQuyen;

    public HangThanhVien() {}

    public HangThanhVien(int id, String maHang, String tenHang, long mocChiTieuToiThieu, int phanTramGiamGia, String dacQuyen) {
        this.id = id;
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.mocChiTieuToiThieu = mocChiTieuToiThieu;
        this.phanTramGiamGia = phanTramGiamGia;
        this.dacQuyen = dacQuyen;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaHang() { return maHang; }
    public void setMaHang(String maHang) { this.maHang = maHang; }

    public String getTenHang() { return tenHang; }
    public void setTenHang(String tenHang) { this.tenHang = tenHang; }

    public long getMocChiTieuToiThieu() { return mocChiTieuToiThieu; }
    public void setMocChiTieuToiThieu(long mocChiTieuToiThieu) { this.mocChiTieuToiThieu = mocChiTieuToiThieu; }

    public int getPhanTramGiamGia() { return phanTramGiamGia; }
    public void setPhanTramGiamGia(int phanTramGiamGia) { this.phanTramGiamGia = phanTramGiamGia; }

    public String getDacQuyen() { return dacQuyen; }
    public void setDacQuyen(String dacQuyen) { this.dacQuyen = dacQuyen; }
}