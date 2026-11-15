package com.duna.thanhvien3_bach.repository;

/**
 * MEMBER 3: LÊ MINH BÁCH - MSSV: TH08442
 * UC 39: Sinh mã VietQR thanh toán động hiển thị chính xác số tiền đơn
 */
public class UC39_SinhMaVietQRDongRepo {

    public String sinhMaVietQR(String maDonHang, long tongTien) {
        String bankId = "MB";
        String accountNo = "0987654321";
        String memo = "DUNA " + (maDonHang != null ? maDonHang : "POS");
        return String.format("https://img.vietqr.io/image/%s-%s-compact2.png?amount=%d&addInfo=%s&accountName=DUNA%%20MENSWEAR",
                             bankId, accountNo, tongTien, memo.replace(" ", "%20"));
    }
}
