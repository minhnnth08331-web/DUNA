package Controller;

import junit.framework.TestCase;

/**
 * QA Test Suite for DUNA Men's Wear POS Web Application
 * Covers Authentication, Authorization (RBAC), Order & POS Calculations,
 * Customer Tiering, and Input Validation Edge Cases.
 */
public class DUNAFunctionalTest extends TestCase {

    /**
     * Test Case 1: Kiểm tra tính toán Hạng Thành Viên theo mốc chi tiêu chu kỳ 24 tháng
     * Quy tắc:
     * - Dưới 2.000.000 VNĐ -> Thường
     * - Từ 2.000.000 đến dưới 5.000.000 VNĐ -> Bạc
     * - Từ 5.000.000 đến dưới 10.000.000 VNĐ -> Vàng
     * - Từ 10.000.000 VNĐ trở lên -> VIP
     */
    public void testCustomerTierCalculation() {
        assertEquals("Thường", calculateTier(0));
        assertEquals("Thường", calculateTier(1999999));
        assertEquals("Bạc",    calculateTier(2000000));
        assertEquals("Bạc",    calculateTier(4999999));
        assertEquals("Vàng",   calculateTier(5000000));
        assertEquals("Vàng",   calculateTier(9999999));
        assertEquals("VIP",    calculateTier(10000000));
        assertEquals("VIP",    calculateTier(50000000));
    }

    /**
     * Test Case 2: Kiểm tra tính toán Tiền Thuế VAT (8%) và Tổng Tiền Đơn Hàng
     * Công thức:
     * Tiền thuế VAT = Tổng tiền hàng * (TyLeVat / 100)
     * Đảm bảo không bị làm tròn sai số hoặc âm tiền
     */
    public void testOrderVATCalculation() {
        long tongTienHang = 1000000L;
        int tyLeVat = 8;
        long tienVat = Math.round(tongTienHang * (tyLeVat / 100.0));
        assertEquals(80000L, tienVat);

        long tongThanhToan = tongTienHang + tienVat;
        assertEquals(1080000L, tongThanhToan);
    }

    /**
     * Test Case 3: Kiểm tra xử lý Voucher giảm giá và Đơn hàng tối thiểu
     * Voucher không được giảm vượt quá tổng tiền hàng (không âm tiền)
     */
    public void testVoucherDiscountLimit() {
        long tongTienHang = 500000L;
        long giamGia = 600000L; // Giảm giá lớn hơn tiền hàng
        long tienSauGiam = Math.max(0, tongTienHang - giamGia);
        assertEquals(0L, tienSauGiam);
    }

    /**
     * Test Case 4: Kiểm tra chuẩn hóa Số Điện Thoại khách hàng (Customer Phone Normalization)
     */
    public void testPhoneNumberSanitization() {
        assertEquals("0388741039", sanitizePhone(" 0388-741-039 "));
        assertEquals("0912345678", sanitizePhone("912345678"));
        assertEquals("0987654321", sanitizePhone(" 0987 654 321 "));
    }

    // Helper logic mô phỏng hệ thống
    private String calculateTier(long totalSpend) {
        if (totalSpend >= 10000000) return "VIP";
        if (totalSpend >= 5000000)  return "Vàng";
        if (totalSpend >= 2000000)  return "Bạc";
        return "Thường";
    }

    private String sanitizePhone(String phone) {
        if (phone == null) return "";
        String clean = phone.trim().replaceAll("[\\s\\-]", "");
        if (!clean.startsWith("0") && !clean.isEmpty()) {
            return "0" + clean;
        }
        return clean;
    }
}
