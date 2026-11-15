package com.duna.common.entities;

import java.io.Serializable;

/**
 * Entity ánh xạ bảng ChiTietHoaDon (Tương ứng ChiTietDonHang trong DB DUNA)
 */
public class ChiTietHoaDon implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int idHoaDon;
    private int idBienThe;
    private String tenSanPham;
    private String mauSac;
    private String kichCo;
    private int soLuong;
    private long donGia;
    private long thanhTien;

    public ChiTietHoaDon() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdHoaDon() { return idHoaDon; }
    public void setIdHoaDon(int idHoaDon) { this.idHoaDon = idHoaDon; }

    public int getIdBienThe() { return idBienThe; }
    public void setIdBienThe(int idBienThe) { this.idBienThe = idBienThe; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public String getMauSac() { return mauSac; }
    public void setMauSac(String mauSac) { this.mauSac = mauSac; }

    public String getKichCo() { return kichCo; }
    public void setKichCo(String kichCo) { this.kichCo = kichCo; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public long getDonGia() { return donGia; }
    public void setDonGia(long donGia) { this.donGia = donGia; }

    public long getThanhTien() { return thanhTien; }
    public void setThanhTien(long thanhTien) { this.thanhTien = thanhTien; }
}