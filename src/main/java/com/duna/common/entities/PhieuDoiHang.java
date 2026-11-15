package com.duna.common.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity ánh xạ bảng PhieuDoiHang (Đổi size, đổi màu tại quầy POS DUNA)
 */
public class PhieuDoiHang implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String maPhieu;
    private int idHoaDonGoc;
    private Integer idKhachHang;
    private int idNhanVien;
    private int idBienTheCu;
    private int idBienTheMoi;
    private int soLuong;
    private long tienChenhLech;
    private String lyDoDoi;
    private Date ngayTao;

    public PhieuDoiHang() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }

    public int getIdHoaDonGoc() { return idHoaDonGoc; }
    public void setIdHoaDonGoc(int idHoaDonGoc) { this.idHoaDonGoc = idHoaDonGoc; }

    public Integer getIdKhachHang() { return idKhachHang; }
    public void setIdKhachHang(Integer idKhachHang) { this.idKhachHang = idKhachHang; }

    public int getIdNhanVien() { return idNhanVien; }
    public void setIdNhanVien(int idNhanVien) { this.idNhanVien = idNhanVien; }

    public int getIdBienTheCu() { return idBienTheCu; }
    public void setIdBienTheCu(int idBienTheCu) { this.idBienTheCu = idBienTheCu; }

    public int getIdBienTheMoi() { return idBienTheMoi; }
    public void setIdBienTheMoi(int idBienTheMoi) { this.idBienTheMoi = idBienTheMoi; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public long getTienChenhLech() { return tienChenhLech; }
    public void setTienChenhLech(long tienChenhLech) { this.tienChenhLech = tienChenhLech; }

    public String getLyDoDoi() { return lyDoDoi; }
    public void setLyDoDoi(String lyDoDoi) { this.lyDoDoi = lyDoDoi; }

    public Date getNgayTao() { return ngayTao; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }
}