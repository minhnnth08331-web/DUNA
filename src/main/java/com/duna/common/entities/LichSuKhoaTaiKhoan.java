package com.duna.common.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity ánh xạ bảng LichSuKhoaTaiKhoan (Ghi nhận lý do khóa tài khoản nhân sự khẩn cấp)
 */
public class LichSuKhoaTaiKhoan implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int idNhanVien;
    private Integer idNguoiThucHien;
    private String lyDoKhoa;
    private Date thoiGianKhoa;
    private Date thoiGianMoKhoa;
    private String trangThai;

    public LichSuKhoaTaiKhoan() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdNhanVien() { return idNhanVien; }
    public void setIdNhanVien(int idNhanVien) { this.idNhanVien = idNhanVien; }

    public Integer getIdNguoiThucHien() { return idNguoiThucHien; }
    public void setIdNguoiThucHien(Integer idNguoiThucHien) { this.idNguoiThucHien = idNguoiThucHien; }

    public String getLyDoKhoa() { return lyDoKhoa; }
    public void setLyDoKhoa(String lyDoKhoa) { this.lyDoKhoa = lyDoKhoa; }

    public Date getThoiGianKhoa() { return thoiGianKhoa; }
    public void setThoiGianKhoa(Date thoiGianKhoa) { this.thoiGianKhoa = thoiGianKhoa; }

    public Date getThoiGianMoKhoa() { return thoiGianMoKhoa; }
    public void setThoiGianMoKhoa(Date thoiGianMoKhoa) { this.thoiGianMoKhoa = thoiGianMoKhoa; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}