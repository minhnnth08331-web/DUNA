package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Thực thể Sản phẩm (Product Entity) - Chuỗi thời trang nam DUNA
 */
public class SanPham implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id; // ID_SanPham
    private String maSP;
    private String tenSP;
    private int idDanhMuc;
    private String danhMuc; // TenDanhMuc
    private String thuongHieu;
    private long giaBan;
    private long giaNhap;
    private int soLuong; // TongSoLuong
    private String trangThai;
    private String chatLieu;
    private String formDang;
    private String hinhAnh;
    private String moTa;
    private String ngayTao;
    private String ngayCapNhat;

    private List<BienTheSanPham> danhSachBienThe = new ArrayList<>();

    public SanPham() {}

    public SanPham(int id, String maSP, String tenSP, int idDanhMuc, String danhMuc,
                   String thuongHieu, long giaBan, long giaNhap, int soLuong,
                   String trangThai, String chatLieu, String formDang, String hinhAnh,
                   String moTa, String ngayTao, String ngayCapNhat) {
        this.id = id;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.idDanhMuc = idDanhMuc;
        this.danhMuc = danhMuc;
        this.thuongHieu = thuongHieu;
        this.giaBan = giaBan;
        this.giaNhap = giaNhap;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
        this.chatLieu = chatLieu;
        this.formDang = formDang;
        this.hinhAnh = hinhAnh;
        this.moTa = moTa;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSanPham() {
        return id;
    }

    public void setIdSanPham(int idSanPham) {
        this.id = idSanPham;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getIdDanhMuc() {
        return idDanhMuc;
    }

    public void setIdDanhMuc(int idDanhMuc) {
        this.idDanhMuc = idDanhMuc;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    public String getTenDanhMuc() {
        return danhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.danhMuc = tenDanhMuc;
    }

    public String getThuongHieu() {
        return thuongHieu;
    }

    public void setThuongHieu(String thuongHieu) {
        this.thuongHieu = thuongHieu;
    }

    public long getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(long giaBan) {
        this.giaBan = giaBan;
    }

    public long getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(long giaNhap) {
        this.giaNhap = giaNhap;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getTongSoLuong() {
        return soLuong;
    }

    public void setTongSoLuong(int tongSoLuong) {
        this.soLuong = tongSoLuong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public void setChatLieu(String chatLieu) {
        this.chatLieu = chatLieu;
    }

    public String getFormDang() {
        return formDang;
    }

    public void setFormDang(String formDang) {
        this.formDang = formDang;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(String ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    public List<BienTheSanPham> getDanhSachBienThe() {
        return danhSachBienThe;
    }

    public void setDanhSachBienThe(List<BienTheSanPham> danhSachBienThe) {
        this.danhSachBienThe = danhSachBienThe;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", this.id);
        m.put("idSanPham", this.id);
        m.put("maSP", this.maSP != null ? this.maSP : "");
        m.put("tenSP", this.tenSP != null ? this.tenSP : "");
        m.put("danhMuc", this.danhMuc != null ? this.danhMuc : "");
        m.put("tenDanhMuc", this.danhMuc != null ? this.danhMuc : "");
        m.put("thuongHieu", this.thuongHieu != null ? this.thuongHieu : "");
        m.put("giaBan", this.giaBan);
        m.put("giaNhap", this.giaNhap);
        m.put("soLuong", this.soLuong);
        m.put("tongSoLuong", this.soLuong);
        m.put("trangThai", this.trangThai != null ? this.trangThai : "");
        m.put("chatLieu", this.chatLieu != null ? this.chatLieu : "");
        m.put("formDang", this.formDang != null ? this.formDang : "");
        m.put("hinhAnh", this.hinhAnh != null ? this.hinhAnh : "");
        m.put("moTa", this.moTa != null ? this.moTa : "");
        m.put("ngayTao", this.ngayTao != null ? this.ngayTao : "");
        m.put("ngayCapNhat", this.ngayCapNhat != null ? this.ngayCapNhat : "");
        return m;
    }

    public static SanPham fromMap(Map<String, Object> map) {
        if (map == null) return null;
        SanPham sp = new SanPham();
        if (map.get("id") != null) sp.setId(Integer.parseInt(map.get("id").toString()));
        else if (map.get("idSanPham") != null) sp.setId(Integer.parseInt(map.get("idSanPham").toString()));

        if (map.get("maSP") != null) sp.setMaSP((String) map.get("maSP"));
        if (map.get("tenSP") != null) sp.setTenSP((String) map.get("tenSP"));
        if (map.get("danhMuc") != null) sp.setDanhMuc((String) map.get("danhMuc"));
        else if (map.get("tenDanhMuc") != null) sp.setDanhMuc((String) map.get("tenDanhMuc"));

        if (map.get("thuongHieu") != null) sp.setThuongHieu((String) map.get("thuongHieu"));
        if (map.get("giaBan") != null) sp.setGiaBan(Long.parseLong(map.get("giaBan").toString()));
        if (map.get("giaNhap") != null) sp.setGiaNhap(Long.parseLong(map.get("giaNhap").toString()));
        if (map.get("soLuong") != null) sp.setSoLuong(Integer.parseInt(map.get("soLuong").toString()));
        else if (map.get("tongSoLuong") != null) sp.setSoLuong(Integer.parseInt(map.get("tongSoLuong").toString()));

        if (map.get("trangThai") != null) sp.setTrangThai((String) map.get("trangThai"));
        if (map.get("chatLieu") != null) sp.setChatLieu((String) map.get("chatLieu"));
        if (map.get("formDang") != null) sp.setFormDang((String) map.get("formDang"));
        if (map.get("hinhAnh") != null) sp.setHinhAnh((String) map.get("hinhAnh"));
        if (map.get("moTa") != null) sp.setMoTa((String) map.get("moTa"));
        if (map.get("ngayTao") != null) sp.setNgayTao((String) map.get("ngayTao"));
        if (map.get("ngayCapNhat") != null) sp.setNgayCapNhat((String) map.get("ngayCapNhat"));

        return sp;
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "id=" + id +
                ", maSP='" + maSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", danhMuc='" + danhMuc + '\'' +
                ", giaBan=" + giaBan +
                ", soLuong=" + soLuong +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}

