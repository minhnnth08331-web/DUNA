package com.duna.common.entities;

import java.io.Serializable;

/**
 * Entity ánh xạ bảng FormDang (Form dáng trang phục: Slimfit, Regular, Oversize, Classic...)
 */
public class FormDang implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String maFormDang;
    private String tenFormDang;
    private String moTaForm;

    public FormDang() {}

    public FormDang(int id, String maFormDang, String tenFormDang, String moTaForm) {
        this.id = id;
        this.maFormDang = maFormDang;
        this.tenFormDang = tenFormDang;
        this.moTaForm = moTaForm;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaFormDang() { return maFormDang; }
    public void setMaFormDang(String maFormDang) { this.maFormDang = maFormDang; }

    public String getTenFormDang() { return tenFormDang; }
    public void setTenFormDang(String tenFormDang) { this.tenFormDang = tenFormDang; }

    public String getMoTaForm() { return moTaForm; }
    public void setMoTaForm(String moTaForm) { this.moTaForm = moTaForm; }
}