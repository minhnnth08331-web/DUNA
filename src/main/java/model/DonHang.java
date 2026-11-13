package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Thực thể Đơn hàng / Hóa đơn (Order Entity) - DUNA Men's Wear POS
 */
public class DonHang implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id; // ID_DonHang
    private String maDonHang;
    private Integer idKhachHang;
    private String tenKhachHang;
    private String sdtKhachHang;
    private Integer idNhanVien;
    private String tenNhanVien;
    private long tongTien;
    private long giamGia;
    private long thanhTien;
    private int trangThaiDH_ID;
    private String tenTrangThai; // Chờ thanh toán, Đã thanh toán, Đã hủy
    private String phuongThucTT; // Tiền mặt, Chuyển khoản, Thẻ
    private String ghiChu;
    private String loaiDonHang; // Tại quầy (POS), Giao hàng
    private String ngayTao;
    private int soLuongSP;

    private List<ChiTietDonHang> danhSachChiTiet = new ArrayList<>();

    public DonHang() {}

    public DonHang(int id, String maDonHang, Integer idKhachHang, String tenKhachHang,
                   Integer idNhanVien, long tongTien, long giamGia, long thanhTien,
                   int trangThaiDH_ID, String tenTrangThai, String phuongThucTT, String ngayTao) {
        this.id = id;
        this.maDonHang = maDonHang;
        this.idKhachHang = idKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.idNhanVien = idNhanVien;
        this.tongTien = tongTien;
        this.giamGia = giamGia;
        this.thanhTien = thanhTien;
        this.trangThaiDH_ID = trangThaiDH_ID;
        this.tenTrangThai = tenTrangThai;
        this.phuongThucTT = phuongThucTT;
        this.ngayTao = ngayTao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDonHang() {
        return id;
    }

    public void setIdDonHang(int idDonHang) {
        this.id = idDonHang;
    }

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public Integer getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(Integer idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSdtKhachHang() {
        return sdtKhachHang;
    }

    public void setSdtKhachHang(String sdtKhachHang) {
        this.sdtKhachHang = sdtKhachHang;
    }

    public Integer getIdNhanVien() {
        return idNhanVien;
    }

    public void setIdNhanVien(Integer idNhanVien) {
        this.idNhanVien = idNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public long getTongTien() {
        return tongTien;
    }

    public void setTongTien(long tongTien) {
        this.tongTien = tongTien;
    }

    public long getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(long giamGia) {
        this.giamGia = giamGia;
    }

    public long getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(long thanhTien) {
        this.thanhTien = thanhTien;
    }

    public int getTrangThaiDH_ID() {
        return trangThaiDH_ID;
    }

    public void setTrangThaiDH_ID(int trangThaiDH_ID) {
        this.trangThaiDH_ID = trangThaiDH_ID;
    }

    public String getTenTrangThai() {
        return tenTrangThai;
    }

    public void setTenTrangThai(String tenTrangThai) {
        this.tenTrangThai = tenTrangThai;
    }

    public String getPhuongThucTT() {
        return phuongThucTT;
    }

    public void setPhuongThucTT(String phuongThucTT) {
        this.phuongThucTT = phuongThucTT;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getLoaiDonHang() {
        return loaiDonHang;
    }

    public void setLoaiDonHang(String loaiDonHang) {
        this.loaiDonHang = loaiDonHang;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public int getSoLuongSP() {
        return soLuongSP;
    }

    public void setSoLuongSP(int soLuongSP) {
        this.soLuongSP = soLuongSP;
    }

    public List<ChiTietDonHang> getDanhSachChiTiet() {
        return danhSachChiTiet;
    }

    public void setDanhSachChiTiet(List<ChiTietDonHang> danhSachChiTiet) {
        this.danhSachChiTiet = danhSachChiTiet;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", this.id);
        m.put("idDonHang", this.id);
        m.put("maDonHang", this.maDonHang != null ? this.maDonHang : "");
        m.put("idKhachHang", this.idKhachHang);
        m.put("tenKhachHang", this.tenKhachHang != null ? this.tenKhachHang : "Khách lẻ tại quầy");
        m.put("sdtKhachHang", this.sdtKhachHang != null ? this.sdtKhachHang : "");
        m.put("idNhanVien", this.idNhanVien);
        m.put("tenNhanVien", this.tenNhanVien != null ? this.tenNhanVien : "");
        m.put("tongTien", this.tongTien);
        m.put("giamGia", this.giamGia);
        m.put("thanhTien", this.thanhTien);
        m.put("trangThaiDH_ID", this.trangThaiDH_ID);
        m.put("tenTrangThai", this.tenTrangThai != null ? this.tenTrangThai : "");
        m.put("phuongThucTT", this.phuongThucTT != null ? this.phuongThucTT : "");
        m.put("ghiChu", this.ghiChu != null ? this.ghiChu : "");
        m.put("loaiDonHang", this.loaiDonHang != null ? this.loaiDonHang : "Tại quầy");
        m.put("ngayTao", this.ngayTao != null ? this.ngayTao : "");
        m.put("soLuongSP", this.soLuongSP);
        return m;
    }

    public static DonHang fromMap(Map<String, Object> map) {
        if (map == null) return null;
        DonHang dh = new DonHang();
        if (map.get("id") != null) dh.setId(Integer.parseInt(map.get("id").toString()));
        else if (map.get("idDonHang") != null) dh.setId(Integer.parseInt(map.get("idDonHang").toString()));

        if (map.get("maDonHang") != null) dh.setMaDonHang((String) map.get("maDonHang"));
        if (map.get("idKhachHang") != null) dh.setIdKhachHang(Integer.parseInt(map.get("idKhachHang").toString()));
        if (map.get("tenKhachHang") != null) dh.setTenKhachHang((String) map.get("tenKhachHang"));
        if (map.get("sdtKhachHang") != null) dh.setSdtKhachHang((String) map.get("sdtKhachHang"));
        if (map.get("idNhanVien") != null) dh.setIdNhanVien(Integer.parseInt(map.get("idNhanVien").toString()));
        if (map.get("tenNhanVien") != null) dh.setTenNhanVien((String) map.get("tenNhanVien"));
        if (map.get("tongTien") != null) dh.setTongTien(Long.parseLong(map.get("tongTien").toString()));
        if (map.get("giamGia") != null) dh.setGiamGia(Long.parseLong(map.get("giamGia").toString()));
        if (map.get("thanhTien") != null) dh.setThanhTien(Long.parseLong(map.get("thanhTien").toString()));
        if (map.get("trangThaiDH_ID") != null) dh.setTrangThaiDH_ID(Integer.parseInt(map.get("trangThaiDH_ID").toString()));
        if (map.get("tenTrangThai") != null) dh.setTenTrangThai((String) map.get("tenTrangThai"));
        if (map.get("phuongThucTT") != null) dh.setPhuongThucTT((String) map.get("phuongThucTT"));
        if (map.get("ghiChu") != null) dh.setGhiChu((String) map.get("ghiChu"));
        if (map.get("loaiDonHang") != null) dh.setLoaiDonHang((String) map.get("loaiDonHang"));
        if (map.get("ngayTao") != null) dh.setNgayTao((String) map.get("ngayTao"));
        if (map.get("soLuongSP") != null) dh.setSoLuongSP(Integer.parseInt(map.get("soLuongSP").toString()));
        return dh;
    }

    @Override
    public String toString() {
        return "DonHang{" +
                "id=" + id +
                ", maDonHang='" + maDonHang + '\'' +
                ", tenKhachHang='" + tenKhachHang + '\'' +
                ", thanhTien=" + thanhTien +
                ", tenTrangThai='" + tenTrangThai + '\'' +
                '}';
    }
}

