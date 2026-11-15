package com.duna.thanhvien2_ducanh.repository;

import java.util.*;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 23: Giao diện bán hàng POS 1 chạm (Thiết kế 2 cột chuẩn UX DUNA)
 */
public class UC23_BanHangPOS1ChamRepo {

    public Map<String, Object> getPOSLayoutConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("leftColumn", "Danh sách sản phẩm & Phím tắt chọn nhanh");
        config.put("rightColumn", "Giỏ hàng quầy, Thông tin khách hàng & VietQR");
        config.put("theme", "Modern DUNA Menswear");
        return config;
    }
}
