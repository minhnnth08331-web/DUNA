package com.duna.thanhvien2_ducanh.repository;

/**
 * MEMBER 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * UC 31: Tự động tính bù trừ tiền chênh lệch khi khách đổi Size/Mẫu khác
 */
public class UC31_TinhBuTruTienDoiHangRepo {

    public long tinhTienBuTruDoiHang(long giaMonCu, long giaMonMoi) {
        return giaMonMoi - giaMonCu;
    }
}
