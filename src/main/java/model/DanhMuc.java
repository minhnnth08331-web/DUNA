package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Thực thể Danh mục sản phẩm (Category Entity) - DUNA Men's Wear
 */
public class DanhMuc implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idDanhMuc;
    private String tenDanhMuc;
    private String moTa;

    public DanhMuc() {}

    public DanhMuc(int idDanhMuc, String tenDanhMuc, String moTa) {
        this.idDanhMuc = idDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
        this.moTa = moTa;
    }

    public int getIdDanhMuc() {
        return idDanhMuc;
    }

    public void setIdDanhMuc(int idDanhMuc) {
        this.idDanhMuc = idDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("idDanhMuc", this.idDanhMuc);
        map.put("tenDanhMuc", this.tenDanhMuc);
        map.put("moTa", this.moTa != null ? this.moTa : "");
        return map;
    }

    public static DanhMuc fromMap(Map<String, Object> map) {
        if (map == null) return null;
        DanhMuc dm = new DanhMuc();
        if (map.get("idDanhMuc") != null) {
            dm.setIdDanhMuc(Integer.parseInt(map.get("idDanhMuc").toString()));
        }
        if (map.get("tenDanhMuc") != null) {
            dm.setTenDanhMuc((String) map.get("tenDanhMuc"));
        }
        if (map.get("moTa") != null) {
            dm.setMoTa((String) map.get("moTa"));
        }
        return dm;
    }

    @Override
    public String toString() {
        return "DanhMuc{" +
                "idDanhMuc=" + idDanhMuc +
                ", tenDanhMuc='" + tenDanhMuc + '\'' +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}

