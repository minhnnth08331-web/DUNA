package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Thực thể Biến thể sản phẩm (Product Variant Entity - Màu sắc & Kích cỡ)
 * DUNA Men's Wear
 */
public class BienTheSanPham implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idBienThe;
    private int idSanPham;
    private String mauSac;
    private String trongLuong; // Kích cỡ size (S, M, L, XL, XXL) hoặc Trọng lượng
    private long giaNhap;
    private long giaBan;
    private int soLuong;
    private String barcode;
    private String maBienThe;

    public BienTheSanPham() {}

    public BienTheSanPham(int idBienThe, int idSanPham, String mauSac, String trongLuong,
                          long giaNhap, long giaBan, int soLuong, String barcode, String maBienThe) {
        this.idBienThe = idBienThe;
        this.idSanPham = idSanPham;
        this.mauSac = mauSac;
        this.trongLuong = trongLuong;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.barcode = barcode;
        this.maBienThe = maBienThe;
    }

    public int getIdBienThe() {
        return idBienThe;
    }

    public void setIdBienThe(int idBienThe) {
        this.idBienThe = idBienThe;
    }

    public int getId() {
        return idBienThe;
    }

    public void setId(int id) {
        this.idBienThe = id;
    }

    public int getIdSanPham() {
        return idSanPham;
    }

    public void setIdSanPham(int idSanPham) {
        this.idSanPham = idSanPham;
    }

    public String getMauSac() {
        return mauSac;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public String getTrongLuong() {
        return trongLuong;
    }

    public void setTrongLuong(String trongLuong) {
        this.trongLuong = trongLuong;
    }

    public String getSize() {
        return trongLuong;
    }

    public void setSize(String size) {
        this.trongLuong = size;
    }

    public long getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(long giaNhap) {
        this.giaNhap = giaNhap;
    }

    public long getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(long giaBan) {
        this.giaBan = giaBan;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getMaBienThe() {
        return maBienThe;
    }

    public void setMaBienThe(String maBienThe) {
        this.maBienThe = maBienThe;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.idBienThe);
        map.put("idBienThe", this.idBienThe);
        map.put("idSanPham", this.idSanPham);
        map.put("mauSac", this.mauSac != null ? this.mauSac : "");
        map.put("trongLuong", this.trongLuong != null ? this.trongLuong : "");
        map.put("size", this.trongLuong != null ? this.trongLuong : "");
        map.put("giaNhap", this.giaNhap);
        map.put("giaBan", this.giaBan);
        map.put("soLuong", this.soLuong);
        map.put("barcode", this.barcode != null ? this.barcode : "");
        map.put("maBienThe", this.maBienThe != null ? this.maBienThe : "");
        return map;
    }

    public static BienTheSanPham fromMap(Map<String, Object> map) {
        if (map == null) return null;
        BienTheSanPham bt = new BienTheSanPham();
        if (map.get("idBienThe") != null) bt.setIdBienThe(Integer.parseInt(map.get("idBienThe").toString()));
        else if (map.get("id") != null) bt.setIdBienThe(Integer.parseInt(map.get("id").toString()));

        if (map.get("idSanPham") != null) bt.setIdSanPham(Integer.parseInt(map.get("idSanPham").toString()));
        if (map.get("mauSac") != null) bt.setMauSac((String) map.get("mauSac"));
        if (map.get("trongLuong") != null) bt.setTrongLuong((String) map.get("trongLuong"));
        else if (map.get("size") != null) bt.setTrongLuong((String) map.get("size"));

        if (map.get("giaNhap") != null) bt.setGiaNhap(Long.parseLong(map.get("giaNhap").toString()));
        if (map.get("giaBan") != null) bt.setGiaBan(Long.parseLong(map.get("giaBan").toString()));
        if (map.get("soLuong") != null) bt.setSoLuong(Integer.parseInt(map.get("soLuong").toString()));
        if (map.get("barcode") != null) bt.setBarcode((String) map.get("barcode"));
        if (map.get("maBienThe") != null) bt.setMaBienThe((String) map.get("maBienThe"));

        return bt;
    }

    @Override
    public String toString() {
        return "BienTheSanPham{" +
                "idBienThe=" + idBienThe +
                ", idSanPham=" + idSanPham +
                ", mauSac='" + mauSac + '\'' +
                ", trongLuong='" + trongLuong + '\'' +
                ", giaBan=" + giaBan +
                ", soLuong=" + soLuong +
                '}';
    }
}

