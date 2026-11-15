package com.duna.common.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity ánh xạ bảng LichSuTichDiem (Ghi log chi tiêu, cộng/trừ điểm thành viên)
 */
public class LichSuTichDiem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int idKhachHang;
    private Integer idHoaDon;
    private int soDiemThayDoi;
    private String loaiGiaoDich;
    private Date thoiGian;
    private String ghiChu;

    public LichSuTichDiem() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdKhachHang() { return idKhachHang; }
    public void setIdKhachHang(int idKhachHang) { this.idKhachHang = idKhachHang; }

    public Integer getIdHoaDon() { return idHoaDon; }
    public void setIdHoaDon(Integer idHoaDon) { this.idHoaDon = idHoaDon; }

    public int getSoDiemThayDoi() { return soDiemThayDoi; }
    public void setSoDiemThayDoi(int soDiemThayDoi) { this.soDiemThayDoi = soDiemThayDoi; }

    public String getLoaiGiaoDich() { return loaiGiaoDich; }
    public void setLoaiGiaoDich(String loaiGiaoDich) { this.loaiGiaoDich = loaiGiaoDich; }

    public Date getThoiGian() { return thoiGian; }
    public void setThoiGian(Date thoiGian) { this.thoiGian = thoiGian; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}