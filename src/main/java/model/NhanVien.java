package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Thực thể Nhân viên & Tài khoản (Employee Entity) - DUNA Men's Wear
 */
public class NhanVien implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id; // ID_NhanVien
    private String maNhanVien;
    private String hoVaTen;
    private String soDienThoai;
    private String email;
    private String maSoCCCD;
    private String ngaySinh;
    private String gioiTinh;
    private String diaChi;
    private String diaChiHienTai;
    private String ngayHetHanCCCD;
    private int trangThaiNV_ID;
    private String tenTrangThai;
    private int roleId; // 1: Quản lý / Admin, 2: Nhân viên bán hàng
    private String username;
    private String password;

    public NhanVien() {}

    public NhanVien(int id, String maNhanVien, String hoVaTen, String soDienThoai,
                   String email, String maSoCCCD, int trangThaiNV_ID, int roleId, String username) {
        this.id = id;
        this.maNhanVien = maNhanVien;
        this.hoVaTen = hoVaTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.maSoCCCD = maSoCCCD;
        this.trangThaiNV_ID = trangThaiNV_ID;
        this.roleId = roleId;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdNhanVien() {
        return id;
    }

    public void setIdNhanVien(int idNhanVien) {
        this.id = idNhanVien;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMaSoCCCD() {
        return maSoCCCD;
    }

    public void setMaSoCCCD(String maSoCCCD) {
        this.maSoCCCD = maSoCCCD;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getDiaChiHienTai() {
        return diaChiHienTai;
    }

    public void setDiaChiHienTai(String diaChiHienTai) {
        this.diaChiHienTai = diaChiHienTai;
    }

    public String getNgayHetHanCCCD() {
        return ngayHetHanCCCD;
    }

    public void setNgayHetHanCCCD(String ngayHetHanCCCD) {
        this.ngayHetHanCCCD = ngayHetHanCCCD;
    }

    public int getTrangThaiNV_ID() {
        return trangThaiNV_ID;
    }

    public void setTrangThaiNV_ID(int trangThaiNV_ID) {
        this.trangThaiNV_ID = trangThaiNV_ID;
    }

    public String getTenTrangThai() {
        return tenTrangThai;
    }

    public void setTenTrangThai(String tenTrangThai) {
        this.tenTrangThai = tenTrangThai;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return roleId == 1 || "admin".equalsIgnoreCase(username);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", this.id);
        m.put("idNhanVien", this.id);
        m.put("maNhanVien", this.maNhanVien != null ? this.maNhanVien : "");
        m.put("hoVaTen", this.hoVaTen != null ? this.hoVaTen : "");
        m.put("soDienThoai", this.soDienThoai != null ? this.soDienThoai : "");
        m.put("email", this.email != null ? this.email : "");
        m.put("maSoCCCD", this.maSoCCCD != null ? this.maSoCCCD : "");
        m.put("ngaySinh", this.ngaySinh != null ? this.ngaySinh : "");
        m.put("gioiTinh", this.gioiTinh != null ? this.gioiTinh : "");
        m.put("diaChi", this.diaChi != null ? this.diaChi : "");
        m.put("diaChiHienTai", this.diaChiHienTai != null ? this.diaChiHienTai : "");
        m.put("ngayHetHanCCCD", this.ngayHetHanCCCD != null ? this.ngayHetHanCCCD : "");
        m.put("trangThaiNV_ID", this.trangThaiNV_ID);
        m.put("tenTrangThai", this.tenTrangThai != null ? this.tenTrangThai : "Đang làm việc");
        m.put("roleId", this.roleId);
        m.put("username", this.username != null ? this.username : "");
        return m;
    }

    public static NhanVien fromMap(Map<String, Object> map) {
        if (map == null) return null;
        NhanVien nv = new NhanVien();
        if (map.get("id") != null) nv.setId(Integer.parseInt(map.get("id").toString()));
        else if (map.get("idNhanVien") != null) nv.setId(Integer.parseInt(map.get("idNhanVien").toString()));

        if (map.get("maNhanVien") != null) nv.setMaNhanVien((String) map.get("maNhanVien"));
        if (map.get("hoVaTen") != null) nv.setHoVaTen((String) map.get("hoVaTen"));
        if (map.get("soDienThoai") != null) nv.setSoDienThoai((String) map.get("soDienThoai"));
        if (map.get("email") != null) nv.setEmail((String) map.get("email"));
        if (map.get("maSoCCCD") != null) nv.setMaSoCCCD((String) map.get("maSoCCCD"));
        if (map.get("ngaySinh") != null) nv.setNgaySinh((String) map.get("ngaySinh"));
        if (map.get("gioiTinh") != null) nv.setGioiTinh((String) map.get("gioiTinh"));
        if (map.get("diaChi") != null) nv.setDiaChi((String) map.get("diaChi"));
        if (map.get("diaChiHienTai") != null) nv.setDiaChiHienTai((String) map.get("diaChiHienTai"));
        if (map.get("ngayHetHanCCCD") != null) nv.setNgayHetHanCCCD((String) map.get("ngayHetHanCCCD"));
        if (map.get("trangThaiNV_ID") != null) nv.setTrangThaiNV_ID(Integer.parseInt(map.get("trangThaiNV_ID").toString()));
        if (map.get("tenTrangThai") != null) nv.setTenTrangThai((String) map.get("tenTrangThai"));
        if (map.get("roleId") != null) nv.setRoleId(Integer.parseInt(map.get("roleId").toString()));
        if (map.get("username") != null) nv.setUsername((String) map.get("username"));
        return nv;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "id=" + id +
                ", maNhanVien='" + maNhanVien + '\'' +
                ", hoVaTen='" + hoVaTen + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}

