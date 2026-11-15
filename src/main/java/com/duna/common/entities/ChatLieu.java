package com.duna.common.entities;

import java.io.Serializable;

/**
 * Entity ánh xạ bảng ChatLieu (Chất liệu vải thời trang nam: Cotton, Kaki, Denim, Linen...)
 */
public class ChatLieu implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String maChatLieu;
    private String tenChatLieu;
    private String dacTinh;
    private String huongDanBaoQuan;

    public ChatLieu() {}

    public ChatLieu(int id, String maChatLieu, String tenChatLieu, String dacTinh, String huongDanBaoQuan) {
        this.id = id;
        this.maChatLieu = maChatLieu;
        this.tenChatLieu = tenChatLieu;
        this.dacTinh = dacTinh;
        this.huongDanBaoQuan = huongDanBaoQuan;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaChatLieu() { return maChatLieu; }
    public void setMaChatLieu(String maChatLieu) { this.maChatLieu = maChatLieu; }

    public String getTenChatLieu() { return tenChatLieu; }
    public void setTenChatLieu(String tenChatLieu) { this.tenChatLieu = tenChatLieu; }

    public String getDacTinh() { return dacTinh; }
    public void setDacTinh(String dacTinh) { this.dacTinh = dacTinh; }

    public String getHuongDanBaoQuan() { return huongDanBaoQuan; }
    public void setHuongDanBaoQuan(String huongDanBaoQuan) { this.huongDanBaoQuan = huongDanBaoQuan; }
}