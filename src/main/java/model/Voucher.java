package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Thực thể Khuyến mãi / Voucher (Coupon Entity) - DUNA Men's Wear
 */
public class Voucher implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id; // ID_Voucher
    private String maVoucher;
    private String tenChuongTrinh;
    private long giamGia;
    private long donToiThieu;
    private int soLuong;
    private int daSuDung;
    private String donHangSuDung;
    private int trangThai; // 1: Đang kích hoạt, 0: Ngừng kích hoạt
    private String loaiVoucher; // Thường, VIP
    private String ngayTao;

    public Voucher() {}

    public Voucher(int id, String maVoucher, String tenChuongTrinh, long giamGia,
                   long donToiThieu, int soLuong, int daSuDung, String donHangSuDung,
                   int trangThai, String loaiVoucher, String ngayTao) {
        this.id = id;
        this.maVoucher = maVoucher;
        this.tenChuongTrinh = tenChuongTrinh;
        this.giamGia = giamGia;
        this.donToiThieu = donToiThieu;
        this.soLuong = soLuong;
        this.daSuDung = daSuDung;
        this.donHangSuDung = donHangSuDung;
        this.trangThai = trangThai;
        this.loaiVoucher = loaiVoucher;
        this.ngayTao = ngayTao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdVoucher() {
        return id;
    }

    public void setIdVoucher(int idVoucher) {
        this.id = idVoucher;
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public String getTenChuongTrinh() {
        return tenChuongTrinh;
    }

    public void setTenChuongTrinh(String tenChuongTrinh) {
        this.tenChuongTrinh = tenChuongTrinh;
    }

    public long getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(long giamGia) {
        this.giamGia = giamGia;
    }

    public long getDonToiThieu() {
        return donToiThieu;
    }

    public void setDonToiThieu(long donToiThieu) {
        this.donToiThieu = donToiThieu;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getDaSuDung() {
        return daSuDung;
    }

    public void setDaSuDung(int daSuDung) {
        this.daSuDung = daSuDung;
    }

    public String getDonHangSuDung() {
        return donHangSuDung;
    }

    public void setDonHangSuDung(String donHangSuDung) {
        this.donHangSuDung = donHangSuDung;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getLoaiVoucher() {
        return loaiVoucher;
    }

    public void setLoaiVoucher(String loaiVoucher) {
        this.loaiVoucher = loaiVoucher;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public boolean isConHieuLuc() {
        return trangThai == 1 && (soLuong - daSuDung > 0);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", this.id);
        m.put("idVoucher", this.id);
        m.put("maVoucher", this.maVoucher != null ? this.maVoucher : "");
        m.put("tenChuongTrinh", this.tenChuongTrinh != null ? this.tenChuongTrinh : "");
        m.put("giamGia", this.giamGia);
        m.put("donToiThieu", this.donToiThieu);
        m.put("soLuong", this.soLuong);
        m.put("daSuDung", this.daSuDung);
        m.put("donHangSuDung", this.donHangSuDung != null ? this.donHangSuDung : "");
        m.put("trangThai", this.trangThai);
        m.put("loaiVoucher", this.loaiVoucher != null ? this.loaiVoucher : "Thường");
        m.put("ngayTao", this.ngayTao != null ? this.ngayTao : "");
        return m;
    }

    public static Voucher fromMap(Map<String, Object> map) {
        if (map == null) return null;
        Voucher v = new Voucher();
        if (map.get("id") != null) v.setId(Integer.parseInt(map.get("id").toString()));
        else if (map.get("idVoucher") != null) v.setId(Integer.parseInt(map.get("idVoucher").toString()));

        if (map.get("maVoucher") != null) v.setMaVoucher((String) map.get("maVoucher"));
        if (map.get("tenChuongTrinh") != null) v.setTenChuongTrinh((String) map.get("tenChuongTrinh"));
        if (map.get("giamGia") != null) v.setGiamGia(Long.parseLong(map.get("giamGia").toString()));
        if (map.get("donToiThieu") != null) v.setDonToiThieu(Long.parseLong(map.get("donToiThieu").toString()));
        if (map.get("soLuong") != null) v.setSoLuong(Integer.parseInt(map.get("soLuong").toString()));
        if (map.get("daSuDung") != null) v.setDaSuDung(Integer.parseInt(map.get("daSuDung").toString()));
        if (map.get("donHangSuDung") != null) v.setDonHangSuDung((String) map.get("donHangSuDung"));
        if (map.get("trangThai") != null) v.setTrangThai(Integer.parseInt(map.get("trangThai").toString()));
        if (map.get("loaiVoucher") != null) v.setLoaiVoucher((String) map.get("loaiVoucher"));
        if (map.get("ngayTao") != null) v.setNgayTao((String) map.get("ngayTao"));
        return v;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "id=" + id +
                ", maVoucher='" + maVoucher + '\'' +
                ", giamGia=" + giamGia +
                ", soLuong=" + soLuong +
                ", trangThai=" + trangThai +
                '}';
    }
}

