package com.duna.common.entities;

import java.io.Serializable;

/**
 * Entity ánh xạ bảng KichCo (Kích cỡ trang phục: S, M, L, XL, XXL)
 */
public class KichCo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String maKichCo;
    private String tenKichCo;
    private int thuTuSapXep;

    public KichCo() {}

    public KichCo(int id, String maKichCo, String tenKichCo, int thuTuSapXep) {
        this.id = id;
        this.maKichCo = maKichCo;
        this.tenKichCo = tenKichCo;
        this.thuTuSapXep = thuTuSapXep;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaKichCo() { return maKichCo; }
    public void setMaKichCo(String maKichCo) { this.maKichCo = maKichCo; }

    public String getTenKichCo() { return tenKichCo; }
    public void setTenKichCo(String tenKichCo) { this.tenKichCo = tenKichCo; }

    public int getThuTuSapXep() { return thuTuSapXep; }
    public void setThuTuSapXep(int thuTuSapXep) { this.thuTuSapXep = thuTuSapXep; }
}