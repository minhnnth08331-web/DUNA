package com.duna.common.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity ánh xạ bảng HoaDon (Đồng nhất hóa với DonHang trong POS DUNA)
 */
public class HoaDon implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String maHoaDon;
    private Integer idKhachHang;
    private String tenKhachHang;
    private String sdtKhachHang;
    private Integer idNhanVien;
    private String tenNhanVien;
    private long tongTienHang;
    private long giamGiaVIP;
    private long giamGiaVoucher;
    private long thueVAT;
    private long tongThanhToan;
    private String phuongThucThanhToan;
    private String trangThai;
    private String ghiChu;
    private String ngayTao;
    private List<ChiTietHoaDon> danhSachChiTiet = new ArrayList<>();

    public HoaDon() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public Integer getIdKhachHang() { return idKhachHang; }
    public void setIdKhachHang(Integer idKhachHang) { this.idKhachHang = idKhachHang; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getSdtKhachHang() { return sdtKhachHang; }
    public void setSdtKhachHang(String sdtKhachHang) { this.sdtKhachHang = sdtKhachHang; }

    public Integer getIdNhanVien() { return idNhanVien; }
    public void setIdNhanVien(Integer idNhanVien) { this.idNhanVien = idNhanVien; }

    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }

    public long getTongTienHang() { return tongTienHang; }
    public void setTongTienHang(long tongTienHang) { this.tongTienHang = tongTienHang; }

    public long getGiamGiaVIP() { return giamGiaVIP; }
    public void setGiamGiaVIP(long giamGiaVIP) { this.giamGiaVIP = giamGiaVIP; }

    public long getGiamGiaVoucher() { return giamGiaVoucher; }
    public void setGiamGiaVoucher(long giamGiaVoucher) { this.giamGiaVoucher = giamGiaVoucher; }

    public long getThueVAT() { return thueVAT; }
    public void setThueVAT(long thueVAT) { this.thueVAT = thueVAT; }

    public long getTongThanhToan() { return tongThanhToan; }
    public void setTongThanhToan(long tongThanhToan) { this.tongThanhToan = tongThanhToan; }

    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getNgayTao() { return ngayTao; }
    public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }

    public List<ChiTietHoaDon> getDanhSachChiTiet() { return danhSachChiTiet; }
    public void setDanhSachChiTiet(List<ChiTietHoaDon> danhSachChiTiet) { this.danhSachChiTiet = danhSachChiTiet; }
}