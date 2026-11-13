package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Thực thể Chi tiết đơn hàng (Order Item Entity) - DUNA Men's Wear
 */
public class ChiTietDonHang implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idChiTiet;
    private int idDonHang;
    private int idSanPham;
    private int idBienThe;
    private String maSP;
    private String tenSP;
    private int soLuong;
    private long donGia;
    private long thanhTien;
    private String mauSac;
    private String kichCo; // Size (trongLuong)

    public ChiTietDonHang() {}

    public ChiTietDonHang(int idChiTiet, int idDonHang, int idSanPham, int idBienThe,
                         String maSP, String tenSP, int soLuong, long donGia,
                         long thanhTien, String mauSac, String kichCo) {
        this.idChiTiet = idChiTiet;
        this.idDonHang = idDonHang;
        this.idSanPham = idSanPham;
        this.idBienThe = idBienThe;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
        this.mauSac = mauSac;
        this.kichCo = kichCo;
    }

    public int getIdChiTiet() {
        return idChiTiet;
    }

    public void setIdChiTiet(int idChiTiet) {
        this.idChiTiet = idChiTiet;
    }

    public int getIdDonHang() {
        return idDonHang;
    }

    public void setIdDonHang(int idDonHang) {
        this.idDonHang = idDonHang;
    }

    public int getIdSanPham() {
        return idSanPham;
    }

    public void setIdSanPham(int idSanPham) {
        this.idSanPham = idSanPham;
    }

    public int getIdBienThe() {
        return idBienThe;
    }

    public void setIdBienThe(int idBienThe) {
        this.idBienThe = idBienThe;
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

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public long getDonGia() {
        return donGia;
    }

    public void setDonGia(long donGia) {
        this.donGia = donGia;
    }

    public long getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(long thanhTien) {
        this.thanhTien = thanhTien;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public String getKichCo() {
        return kichCo;
    }

    public void setKichCo(String kichCo) {
        this.kichCo = kichCo;
    }

    public String getSize() {
        return kichCo;
    }

    public void setSize(String size) {
        this.kichCo = size;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("idChiTiet", this.idChiTiet);
        m.put("idDonHang", this.idDonHang);
        m.put("idSanPham", this.idSanPham);
        m.put("idBienThe", this.idBienThe);
        m.put("maSP", this.maSP != null ? this.maSP : "");
        m.put("tenSP", this.tenSP != null ? this.tenSP : "");
        m.put("soLuong", this.soLuong);
        m.put("donGia", this.donGia);
        m.put("thanhTien", this.thanhTien);
        m.put("mauSac", this.mauSac != null ? this.mauSac : "");
        m.put("kichCo", this.kichCo != null ? this.kichCo : "");
        m.put("size", this.kichCo != null ? this.kichCo : "");
        return m;
    }

    public static ChiTietDonHang fromMap(Map<String, Object> map) {
        if (map == null) return null;
        ChiTietDonHang item = new ChiTietDonHang();
        if (map.get("idChiTiet") != null) item.setIdChiTiet(Integer.parseInt(map.get("idChiTiet").toString()));
        if (map.get("idDonHang") != null) item.setIdDonHang(Integer.parseInt(map.get("idDonHang").toString()));
        if (map.get("idSanPham") != null) item.setIdSanPham(Integer.parseInt(map.get("idSanPham").toString()));
        if (map.get("idBienThe") != null) item.setIdBienThe(Integer.parseInt(map.get("idBienThe").toString()));
        if (map.get("maSP") != null) item.setMaSP((String) map.get("maSP"));
        if (map.get("tenSP") != null) item.setTenSP((String) map.get("tenSP"));
        if (map.get("soLuong") != null) item.setSoLuong(Integer.parseInt(map.get("soLuong").toString()));
        if (map.get("donGia") != null) item.setDonGia(Long.parseLong(map.get("donGia").toString()));
        if (map.get("thanhTien") != null) item.setThanhTien(Long.parseLong(map.get("thanhTien").toString()));
        if (map.get("mauSac") != null) item.setMauSac((String) map.get("mauSac"));
        if (map.get("kichCo") != null) item.setKichCo((String) map.get("kichCo"));
        else if (map.get("size") != null) item.setKichCo((String) map.get("size"));
        return item;
    }

    @Override
    public String toString() {
        return "ChiTietDonHang{" +
                "idChiTiet=" + idChiTiet +
                ", tenSP='" + tenSP + '\'' +
                ", soLuong=" + soLuong +
                ", thanhTien=" + thanhTien +
                '}';
    }
}

