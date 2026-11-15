package com.duna.common.entities;

import java.io.Serializable;

/**
 * Entity ánh xạ bảng BangTuVanSize (Ma trận tư vấn Size theo Chiều cao & Cân nặng)
 */
public class BangTuVanSize implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private Integer idDanhMuc;
    private int chieuCaoMin;
    private int chieuCaoMax;
    private int canNangMin;
    private int canNangMax;
    private String sizeGoiY;

    public BangTuVanSize() {}

    public BangTuVanSize(int id, Integer idDanhMuc, int chieuCaoMin, int chieuCaoMax, int canNangMin, int canNangMax, String sizeGoiY) {
        this.id = id;
        this.idDanhMuc = idDanhMuc;
        this.chieuCaoMin = chieuCaoMin;
        this.chieuCaoMax = chieuCaoMax;
        this.canNangMin = canNangMin;
        this.canNangMax = canNangMax;
        this.sizeGoiY = sizeGoiY;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getIdDanhMuc() { return idDanhMuc; }
    public void setIdDanhMuc(Integer idDanhMuc) { this.idDanhMuc = idDanhMuc; }

    public int getChieuCaoMin() { return chieuCaoMin; }
    public void setChieuCaoMin(int chieuCaoMin) { this.chieuCaoMin = chieuCaoMin; }

    public int getChieuCaoMax() { return chieuCaoMax; }
    public void setChieuCaoMax(int chieuCaoMax) { this.chieuCaoMax = chieuCaoMax; }

    public int getCanNangMin() { return canNangMin; }
    public void setCanNangMin(int canNangMin) { this.canNangMin = canNangMin; }

    public int getCanNangMax() { return canNangMax; }
    public void setCanNangMax(int canNangMax) { this.canNangMax = canNangMax; }

    public String getSizeGoiY() { return sizeGoiY; }
    public void setSizeGoiY(String sizeGoiY) { this.sizeGoiY = sizeGoiY; }
}