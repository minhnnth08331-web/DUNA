package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Thực thể Khách hàng & Hạng hội viên (Customer Entity)
 * DUNA Men's Wear - Quản lý chu kỳ tích lũy cuốn chiếu 3 tháng
 */
public class KhachHang implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id; // ID_KhachHang
    private String maKhachHang;
    private String hoVaTen;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private String gioiTinh;
    private String hangKhachHang; // Thường, Bạc, Vàng, VIP
    private long tongTienChiTieu;
    private int tongSPMua; // Số lượng SP đã mua trong chu kỳ 3 tháng
    private String trangThai;
    private String ngayBatDauChuKy;
    private String hanGiuHang; // Chuỗi hiển thị: dd/MM/yyyy (còn X ngày)
    private int soNgayConLai;
    private int phanTramGiam; // 0, 2, 4, 7%
    private String ngayTao;

    public KhachHang() {}

    public KhachHang(int id, String maKhachHang, String hoVaTen, String soDienThoai,
                     String email, String diaChi, String gioiTinh, String hangKhachHang,
                     long tongTienChiTieu, int tongSPMua, String trangThai) {
        this.id = id;
        this.maKhachHang = maKhachHang;
        this.hoVaTen = hoVaTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
        this.gioiTinh = gioiTinh;
        this.hangKhachHang = hangKhachHang;
        this.tongTienChiTieu = tongTienChiTieu;
        this.tongSPMua = tongSPMua;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdKhachHang() {
        return id;
    }

    public void setIdKhachHang(int idKhachHang) {
        this.id = idKhachHang;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
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

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getHangKhachHang() {
        return hangKhachHang;
    }

    public void setHangKhachHang(String hangKhachHang) {
        this.hangKhachHang = hangKhachHang;
    }

    public long getTongTienChiTieu() {
        return tongTienChiTieu;
    }

    public void setTongTienChiTieu(long tongTienChiTieu) {
        this.tongTienChiTieu = tongTienChiTieu;
    }

    public int getTongSPMua() {
        return tongSPMua;
    }

    public void setTongSPMua(int tongSPMua) {
        this.tongSPMua = tongSPMua;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getNgayBatDauChuKy() {
        return ngayBatDauChuKy;
    }

    public void setNgayBatDauChuKy(String ngayBatDauChuKy) {
        this.ngayBatDauChuKy = ngayBatDauChuKy;
    }

    public String getHanGiuHang() {
        return hanGiuHang;
    }

    public void setHanGiuHang(String hanGiuHang) {
        this.hanGiuHang = hanGiuHang;
    }

    public int getSoNgayConLai() {
        return soNgayConLai;
    }

    public void setSoNgayConLai(int soNgayConLai) {
        this.soNgayConLai = soNgayConLai;
    }

    public int getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(int phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", this.id);
        m.put("idKhachHang", this.id);
        m.put("maKhachHang", this.maKhachHang != null ? this.maKhachHang : "");
        m.put("hoVaTen", this.hoVaTen != null ? this.hoVaTen : "");
        m.put("soDienThoai", this.soDienThoai != null ? this.soDienThoai : "");
        m.put("email", this.email != null ? this.email : "");
        m.put("diaChi", this.diaChi != null ? this.diaChi : "");
        m.put("gioiTinh", this.gioiTinh != null ? this.gioiTinh : "");
        m.put("hangKhachHang", this.hangKhachHang != null ? this.hangKhachHang : "Thường");
        m.put("tongTienChiTieu", this.tongTienChiTieu);
        m.put("tongSPMua", this.tongSPMua);
        m.put("trangThai", this.trangThai != null ? this.trangThai : "Hoạt động");
        m.put("hanGiuHang", this.hanGiuHang != null ? this.hanGiuHang : "");
        m.put("phanTramGiam", this.phanTramGiam);
        m.put("ngayTao", this.ngayTao != null ? this.ngayTao : "");
        return m;
    }

    public static KhachHang fromMap(Map<String, Object> map) {
        if (map == null) return null;
        KhachHang kh = new KhachHang();
        if (map.get("id") != null) kh.setId(Integer.parseInt(map.get("id").toString()));
        else if (map.get("idKhachHang") != null) kh.setId(Integer.parseInt(map.get("idKhachHang").toString()));

        if (map.get("maKhachHang") != null) kh.setMaKhachHang((String) map.get("maKhachHang"));
        if (map.get("hoVaTen") != null) kh.setHoVaTen((String) map.get("hoVaTen"));
        if (map.get("soDienThoai") != null) kh.setSoDienThoai((String) map.get("soDienThoai"));
        if (map.get("email") != null) kh.setEmail((String) map.get("email"));
        if (map.get("diaChi") != null) kh.setDiaChi((String) map.get("diaChi"));
        if (map.get("gioiTinh") != null) kh.setGioiTinh((String) map.get("gioiTinh"));
        if (map.get("hangKhachHang") != null) kh.setHangKhachHang((String) map.get("hangKhachHang"));
        if (map.get("tongTienChiTieu") != null) kh.setTongTienChiTieu(Long.parseLong(map.get("tongTienChiTieu").toString()));
        if (map.get("tongSPMua") != null) kh.setTongSPMua(Integer.parseInt(map.get("tongSPMua").toString()));
        if (map.get("trangThai") != null) kh.setTrangThai((String) map.get("trangThai"));
        if (map.get("hanGiuHang") != null) kh.setHanGiuHang((String) map.get("hanGiuHang"));
        if (map.get("phanTramGiam") != null) kh.setPhanTramGiam(Integer.parseInt(map.get("phanTramGiam").toString()));
        if (map.get("ngayTao") != null) kh.setNgayTao((String) map.get("ngayTao"));

        return kh;
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "id=" + id +
                ", hoVaTen='" + hoVaTen + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", hangKhachHang='" + hangKhachHang + '\'' +
                ", tongSPMua=" + tongSPMua +
                '}';
    }
}

