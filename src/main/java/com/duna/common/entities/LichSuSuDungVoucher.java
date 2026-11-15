package com.duna.common.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity ánh xạ bảng LichSuSuDungVoucher (Ghi nhận lịch sử áp dụng voucher theo đơn hàng)
 */
public class LichSuSuDungVoucher implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int idVoucher;
    private Integer idKhachHang;
    private int idHoaDon;
    private long soTienGiam;
    private Date thoiGianSuDung;

    public LichSuSuDungVoucher() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdVoucher() { return idVoucher; }
    public void setIdVoucher(int idVoucher) { this.idVoucher = idVoucher; }

    public Integer getIdKhachHang() { return idKhachHang; }
    public void setIdKhachHang(Integer idKhachHang) { this.idKhachHang = idKhachHang; }

    public int getIdHoaDon() { return idHoaDon; }
    public void setIdHoaDon(int idHoaDon) { this.idHoaDon = idHoaDon; }

    public long getSoTienGiam() { return soTienGiam; }
    public void setSoTienGiam(long soTienGiam) { this.soTienGiam = soTienGiam; }

    public Date getThoiGianSuDung() { return thoiGianSuDung; }
    public void setThoiGianSuDung(Date thoiGianSuDung) { this.thoiGianSuDung = thoiGianSuDung; }
}