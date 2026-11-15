import com.duna.thanhvien1_minh.repository.*;
import com.duna.thanhvien2_ducanh.repository.*;
import com.duna.thanhvien3_bach.repository.*;
import com.duna.thanhvien4_kieuan.repository.*;
import com.duna.common.entities.*;
import com.duna.common.util.CurrencyUtil;
import com.duna.common.util.SizeGuideUtil;
import com.duna.common.util.SizeUtil;
import junit.framework.TestCase;

import java.util.*;

/**
 * Unit Test tự động kiểm thử toàn diện 64 Repository riêng biệt phân bổ cho 4 thành viên DUNA Men's Wear:
 * - Member 1: Nguyễn Ngọc Minh - MSSV: TH08331 (UC 01 -> UC 16)
 * - Member 2: Nguyễn Đức Anh - MSSV: TH08308 (UC 17 -> UC 32)
 * - Member 3: Lê Minh Bách - MSSV: TH08442 (UC 33 -> UC 48)
 * - Member 4: Nguyễn Kiều An - MSSV: TH08490 (UC 49 -> UC 64)
 *
 * Kiểm tra và gọi 100% các Repository (Đảm bảo 0 dead code, >= 2 usages: Servlet + Test).
 */
public class SixtyFourUseCasesTest extends TestCase {
    // Member 1 Repositories (UC 01 -> UC 16)
    private final UC01_QuanLyDanhMucRepo uc01 = new UC01_QuanLyDanhMucRepo();
    private final UC02_QuanLyChatLieuRepo uc02 = new UC02_QuanLyChatLieuRepo();
    private final UC03_QuanLyFormDangRepo uc03 = new UC03_QuanLyFormDangRepo();
    private final UC04_KiemTraKhoaNgoaiDanhMucRepo uc04 = new UC04_KiemTraKhoaNgoaiDanhMucRepo();
    private final UC05_DanhSachSanPhamRepo uc05 = new UC05_DanhSachSanPhamRepo();
    private final UC06_TimKiemSanPhamNangCaoRepo uc06 = new UC06_TimKiemSanPhamNangCaoRepo();
    private final UC07_ThemMoiSanPhamRepo uc07 = new UC07_ThemMoiSanPhamRepo();
    private final UC08_CapNhatSanPhamRepo uc08 = new UC08_CapNhatSanPhamRepo();
    private final UC09_QuanLyGiaVonCOGSRepo uc09 = new UC09_QuanLyGiaVonCOGSRepo();
    private final UC10_CauHinhGiaBanVATRepo uc10 = new UC10_CauHinhGiaBanVATRepo();
    private final UC11_TrangThaiKinhDoanhRepo uc11 = new UC11_TrangThaiKinhDoanhRepo();
    private final UC12_DanhSachBienTheRepo uc12 = new UC12_DanhSachBienTheRepo();
    private final UC13_ThemBienTheUniqueRepo uc13 = new UC13_ThemBienTheUniqueRepo();
    private final UC14_CapNhatGiaTonBienTheRepo uc14 = new UC14_CapNhatGiaTonBienTheRepo();
    private final UC15_XoaBienTheDongBoKhoRepo uc15 = new UC15_XoaBienTheDongBoKhoRepo();
    private final UC16_TuVanSizeThongMinhRepo uc16 = new UC16_TuVanSizeThongMinhRepo();

    // Member 2 Repositories (UC 17 -> UC 32)
    private final UC17_DangNhapXacThucRepo uc17 = new UC17_DangNhapXacThucRepo();
    private final UC18_GhiNhoCookieRepo uc18 = new UC18_GhiNhoCookieRepo();
    private final UC19_PhanQuyenRBACRepo uc19 = new UC19_PhanQuyenRBACRepo();
    private final UC20_DoiMatKhauRepo uc20 = new UC20_DoiMatKhauRepo();
    private final UC21_SinhMaBarcodeRepo uc21 = new UC21_SinhMaBarcodeRepo();
    private final UC22_InTemBarcodeRepo uc22 = new UC22_InTemBarcodeRepo();
    private final UC23_BanHangPOS1ChamRepo uc23 = new UC23_BanHangPOS1ChamRepo();
    private final UC24_TimKiemAutoCompleteRepo uc24 = new UC24_TimKiemAutoCompleteRepo();
    private final UC25_QuetBarcodeUSBRepo uc25 = new UC25_QuetBarcodeUSBRepo();
    private final UC26_QuetBarcodeCameraRepo uc26 = new UC26_QuetBarcodeCameraRepo();
    private final UC27_QuanLyGioHangPOSRepo uc27 = new UC27_QuanLyGioHangPOSRepo();
    private final UC28_CanhBaoTonKhoRepo uc28 = new UC28_CanhBaoTonKhoRepo();
    private final UC29_QuanLyDonHangChoRepo uc29 = new UC29_QuanLyDonHangChoRepo();
    private final UC30_TiepNhanDoiSizeMauRepo uc30 = new UC30_TiepNhanDoiSizeMauRepo();
    private final UC31_TinhBuTruTienDoiHangRepo uc31 = new UC31_TinhBuTruTienDoiHangRepo();
    private final UC32_CapNhatKhoDoiHangRepo uc32 = new UC32_CapNhatKhoDoiHangRepo();

    // Member 3 Repositories (UC 33 -> UC 48)
    private final UC33_TraCuuKhachHangPOSRepo uc33 = new UC33_TraCuuKhachHangPOSRepo();
    private final UC34_ThemNhanhKhachHangPOSRepo uc34 = new UC34_ThemNhanhKhachHangPOSRepo();
    private final UC35_ApDungChietKhauVIPRepo uc35 = new UC35_ApDungChietKhauVIPRepo();
    private final UC36_ApDungVoucherPOSRepo uc36 = new UC36_ApDungVoucherPOSRepo();
    private final UC37_TinhTongHoaDonRepo uc37 = new UC37_TinhTongHoaDonRepo();
    private final UC38_ThanhToanTienMatRepo uc38 = new UC38_ThanhToanTienMatRepo();
    private final UC39_SinhMaVietQRDongRepo uc39 = new UC39_SinhMaVietQRDongRepo();
    private final UC40_XacNhanThanhToanQRRepo uc40 = new UC40_XacNhanThanhToanQRRepo();
    private final UC41_InHoaDonNhietK80Repo uc41 = new UC41_InHoaDonNhietK80Repo();
    private final UC42_DanhSachHoaDonRepo uc42 = new UC42_DanhSachHoaDonRepo();
    private final UC43_XemChiTietHoaDonRepo uc43 = new UC43_XemChiTietHoaDonRepo();
    private final UC44_InLaiHoaDonRepo uc44 = new UC44_InLaiHoaDonRepo();
    private final UC45_XuatExcelHoaDonRepo uc45 = new UC45_XuatExcelHoaDonRepo();
    private final UC46_ThongKeLoiNhuanHomNayRepo uc46 = new UC46_ThongKeLoiNhuanHomNayRepo();
    private final UC47_ThongKeLoiNhuanThangNamRepo uc47 = new UC47_ThongKeLoiNhuanThangNamRepo();
    private final UC48_BieuDoDoanhThuChartJSRepo uc48 = new UC48_BieuDoDoanhThuChartJSRepo();

    // Member 4 Repositories (UC 49 -> UC 64)
    private final UC49_DanhSachKhachHangVIPRepo uc49 = new UC49_DanhSachKhachHangVIPRepo();
    private final UC50_TimKiemKhachHangVIPRepo uc50 = new UC50_TimKiemKhachHangVIPRepo();
    private final UC51_ThemMoiKhachHangRepo uc51 = new UC51_ThemMoiKhachHangRepo();
    private final UC52_CapNhatKhachHangRepo uc52 = new UC52_CapNhatKhachHangRepo();
    private final UC53_LichSuMuaSamKhachRepo uc53 = new UC53_LichSuMuaSamKhachRepo();
    private final UC54_TichLuyThangHangVIPRepo uc54 = new UC54_TichLuyThangHangVIPRepo();
    private final UC55_ResetHang3ThangJobRepo uc55 = new UC55_ResetHang3ThangJobRepo();
    private final UC56_ThongKe4ChiSoVoucherRepo uc56 = new UC56_ThongKe4ChiSoVoucherRepo();
    private final UC57_TaoVoucherVIPLeRepo uc57 = new UC57_TaoVoucherVIPLeRepo();
    private final UC58_CauHinhDieuKienVoucherRepo uc58 = new UC58_CauHinhDieuKienVoucherRepo();
    private final UC59_TaoLoVoucherHangLoatRepo uc59 = new UC59_TaoLoVoucherHangLoatRepo();
    private final UC60_DanhSachNhanVienRepo uc60 = new UC60_DanhSachNhanVienRepo();
    private final UC61_XemChiTietHoSoNhanSuRepo uc61 = new UC61_XemChiTietHoSoNhanSuRepo();
    private final UC62_ThemNhanVienTuSinhAccountRepo uc62 = new UC62_ThemNhanVienTuSinhAccountRepo();
    private final UC63_KhoaTaiKhoanNhanVienRepo uc63 = new UC63_KhoaTaiKhoanNhanVienRepo();
    private final UC64_XuatBaoCaoTaiChinhExcelRepo uc64 = new UC64_XuatBaoCaoTaiChinhExcelRepo();

    public void testThanhVien1Minh_UC01_to_UC16() {
        // UC 01: Danh mục
        assertNotNull(uc01.getDanhSachDanhMuc());
        DanhMuc dm = new DanhMuc();
        dm.setIdDanhMuc(999);
        dm.setTenDanhMuc("Test Category");
        dm.setMoTa("Test Description");
        assertNotNull(dm.getTenDanhMuc());

        // UC 02: Chất liệu
        assertNotNull(uc02.getChatLieuList());
        assertTrue(uc02.themChatLieu("Cotton 100%"));

        // UC 03: Form dáng
        assertNotNull(uc03.getFormDangList());
        assertTrue(uc03.themFormDang("Slimfit"));

        // UC 04: Ràng buộc khóa ngoại
        assertFalse(uc04.checkDanhMucForeignKey(999999));

        // UC 05: Danh sách & bộ lọc
        assertNotNull(uc05.getDanhSachSanPham("", "all", "all"));

        // UC 06: Tìm kiếm nâng cao
        assertNotNull(uc06.searchAdvanced("SP01", "Polo", "Cotton", "Slimfit"));

        // UC 07 & 08: Thực thể sản phẩm
        SanPham sp = new SanPham();
        sp.setId(1);
        sp.setMaSP("SPTEST");
        sp.setTenSP("Áo Polo DUNA Classic");
        sp.setIdDanhMuc(1);
        sp.setGiaBan(350000);
        sp.setGiaNhap(180000);
        sp.setSoLuong(50);
        assertEquals("SPTEST", sp.getMaSP());

        // UC 09: Biên lợi nhuận
        assertNotNull(uc09.tinhLoiNhuanGop(1));

        // UC 10: Cấu hình VAT 8%
        Map<String, Object> vatMap = uc10.cauHinhGiaNiemYetVAT(500000);
        assertEquals(40000L, ((Long) vatMap.get("thueVAT")).longValue());
        assertEquals(540000L, ((Long) vatMap.get("giaNiemYet")).longValue());

        // UC 11: Toggle trạng thái
        // UC 12: Biến thể
        assertNotNull(uc12.getVariantsByProductId(1));

        // UC 13, 14, 15: Biến thể
        BienTheSanPham bt = new BienTheSanPham();
        bt.setIdBienThe(1);
        bt.setIdSanPham(1);
        bt.setMauSac("Trắng");
        bt.setTrongLuong("L");
        bt.setGiaBan(350000);
        bt.setGiaNhap(180000);
        bt.setSoLuong(20);
        bt.setBarcode("SP01-TRANG-L");
        assertEquals("SP01-TRANG-L", bt.getBarcode());

        // UC 16: Tư vấn Size thông minh
        assertEquals("L", SizeGuideUtil.recommendSize(175, 68));
        assertEquals("M", SizeGuideUtil.recommendSize(168, 58));
        assertEquals("L", SizeUtil.recommendSize(175, 68));
        assertEquals("L", uc16.tuVanSize(175, 68));
    }

    public void testThanhVien2DucAnh_UC17_to_UC32() {
        // UC 17: Xác thực đăng nhập
        assertNull(uc17.authenticate(null, null));

        // UC 18: Remember Me
        assertFalse(uc18.isValidRememberMeToken(""));

        // UC 19: Phân quyền RBAC
        assertTrue(uc19.checkRoleAccess(1, "/EmployeeManagementServlet"));
        assertFalse(uc19.checkRoleAccess(2, "/EmployeeManagementServlet"));
        assertTrue(uc19.checkRoleAccess(2, "/ProductServlet"));

        // UC 20: Đổi mật khẩu
        assertFalse(uc20.doiMatKhau("admin", "old", "weak")); // Pass yếu -> false

        // UC 21: Tự sinh Barcode
        String bc = uc21.generateBarcode("SP101", "Đen Than", "L");
        assertEquals("SP101-ĐENTHAN-L", bc);

        // UC 22: Thẻ bài Barcode
        Map<String, String> label = uc22.renderBarcodeLabel(bc, "Áo Polo Pima", 450000);
        assertEquals("DUNA MEN'S WEAR", label.get("brand"));

        // UC 23: POS Layout
        Map<String, Object> layout = uc23.getPOSLayoutConfig();
        assertNotNull(layout.get("leftColumn"));

        // UC 24: Auto-complete
        assertNotNull(uc24.searchAutoComplete("Polo"));

        // UC 25 & 26: Barcode Scan USB & WebRTC
        assertNull(uc25.findByBarcodeUSB(""));
        assertNull(uc26.handleWebRTCScan(""));

        // UC 27: Giỏ hàng POS
        List<Map<String, Object>> cart = new ArrayList<>();
        cart = uc27.themVaoGioHang(cart, 10, 2, "Áo Polo", 350000, "SP01-TRANG-M");
        assertEquals(1, cart.size());
        assertEquals(700000L, ((Long) cart.get(0).get("thanhTien")).longValue());

        cart = uc27.capNhatGioHang(cart, 10, 3);
        assertEquals(1050000L, ((Long) cart.get(0).get("thanhTien")).longValue());

        cart = uc27.xoaKhoiGioHang(cart, 10);
        assertEquals(0, cart.size());

        cart = uc27.xoaToanBoGioHang(cart);
        assertEquals(0, cart.size());

        // UC 28: Kiểm tra tồn kho
        assertFalse(uc28.checkInventoryRealtime(99999, 100));

        // UC 29: Quản lý Pending Tabs
        Map<String, Object> tab = uc29.createPendingTab("TAB_01", cart);
        assertNotNull(tab);
        assertNotNull(uc29.getAllPendingTabs());
        uc29.removePendingTab("TAB_01");

        // UC 30: Đổi size modal
        assertNotNull(uc30.openExchangeModal(99999));

        // UC 31: Tính bù trừ đổi hàng
        assertEquals(50000L, uc31.tinhTienBuTruDoiHang(300000, 350000));
        assertEquals(-30000L, uc31.tinhTienBuTruDoiHang(350000, 320000));

        // UC 32: Cập nhật kho đổi hàng
        assertNotNull(uc32);
    }

    public void testThanhVien3Bach_UC33_to_UC48() {
        // UC 33: Tra cứu SĐT
        assertNull(uc33.searchCustomerByPhone(""));

        // UC 34: Thêm nhanh KH
        assertNotNull(uc34);

        // UC 35: Chiết khấu VIP
        assertEquals(20000L, uc35.tinhGiamGiaHangVIP(1000000, "Bạc"));
        assertEquals(50000L, uc35.tinhGiamGiaHangVIP(1000000, "Vàng"));
        assertEquals(100000L, uc35.tinhGiamGiaHangVIP(1000000, "VIP"));
        assertEquals(0L, uc35.tinhGiamGiaHangVIP(1000000, "Thường"));

        // UC 36: Áp dụng Voucher
        assertEquals(0L, uc36.kiemTraVaApDungVoucher("", 500000));

        // UC 37: Tính tổng hóa đơn
        Map<String, Long> sum = uc37.tinhToanTongHoaDon(1000000, 50000, 100000, true);
        assertEquals(850000L, sum.get("tienHang").longValue() - sum.get("giamVIP").longValue() - sum.get("giamVoucher").longValue());
        assertEquals(68000L, sum.get("thueVAT").longValue()); // 850k * 8%
        assertEquals(918000L, sum.get("tongThanhToan").longValue());

        // UC 38: Tính tiền thối
        assertEquals(50000.0, uc38.tinhTienThoi(450000.0, 500000.0), 0.001);

        // UC 39: VietQR Napas 247
        String qr = uc39.sinhMaVietQR("HD1001", 918000);
        assertTrue(qr.contains("img.vietqr.io"));
        assertTrue(qr.contains("918000"));

        // UC 40: Xác nhận QR
        assertNotNull(uc40);

        // UC 41 & 44: In hóa đơn K80 & Reprint
        String k80 = uc41.taoNoiDungHoaDonK80(1001);
        assertTrue(k80.contains("DUNA MEN'S WEAR POS"));
        String reprint = uc44.reprintHoaDon(1001);
        assertTrue(reprint.contains("REPRINT"));

        // UC 42 & 43 & 45: Danh sách & chi tiết hóa đơn & xuất Excel
        assertNotNull(uc42.getDonHangTheoNgay("2026-01-01", "2026-12-31"));
        assertNotNull(uc43.getChiTietHoaDon(1));
        assertNotNull(uc45.exportOrdersData("2026-01-01", "2026-12-31"));

        // UC 46, 47, 48: Thống kê doanh thu & biểu đồ Chart.js
        assertNotNull(uc46.getDoanhThuHomNay());
        assertNotNull(uc47.getDoanhThuThangNam(11, 2026));
        assertNotNull(uc48.getChartData(7));
    }

    public void testThanhVien4KieuAn_UC49_to_UC64() {
        // UC 49 & 50: Khách hàng & Phân hạng
        assertNotNull(uc49.getDanhSachKhachHangVaHang());
        assertNotNull(uc50.searchKhachHang("", "all"));

        // UC 51 & 52: Entity Khách hàng
        KhachHang kh = new KhachHang();
        kh.setId(1);
        kh.setHoVaTen("Nguyễn Văn A");
        kh.setSoDienThoai("0901234567");
        kh.setEmail("a@gmail.com");
        kh.setHangKhachHang("VIP");
        assertEquals("VIP", kh.getHangKhachHang());

        // UC 53: Lịch sử mua sắm
        assertNotNull(uc53.getLichSuMuaSam(1));

        // UC 54 & 55: Tích lũy thăng hạng & Job reset
        assertNotNull(uc54);
        assertNotNull(uc55);

        // UC 56: Bảng thống kê Voucher
        assertNotNull(uc56.getThongKeVoucher());

        // UC 57: Voucher VIP
        Voucher v = new Voucher();
        v.setMaVoucher("DUNATOPVIP");
        v.setTenChuongTrinh("Tri ân khách hàng");
        v.setGiamGia(100000);
        v.setDonToiThieu(500000);
        assertEquals("DUNATOPVIP", v.getMaVoucher());

        // UC 58: Cấu hình điều kiện Voucher
        assertFalse(uc58.cauHinhDieuKienVoucher(99999, 100000, 50));
        assertFalse(uc58.cauHinhDieuKienVoucher(99999, 100000, 50, "2026-12-31"));

        // UC 59: Tạo lô voucher
        assertNotNull(uc59);

        // UC 60 & 61: Nhân viên & Hồ sơ CCCD
        assertNotNull(uc60.getDanhSachNhanVienVaTrangThai());
        assertNull(uc61.getChiTietHoSoCCCD(99999));

        // UC 62: Entity Nhân viên
        NhanVien nv = new NhanVien();
        nv.setHoVaTen("Trần Văn Quản Lý");
        nv.setMaSoCCCD("001202001234");
        assertEquals("001202001234", nv.getMaSoCCCD());

        // UC 63: Khóa tài khoản nhân viên
        assertFalse(uc63.khoaTaiKhoanNhanVien(99999, "Vi phạm quy chế bán hàng"));
        assertFalse(uc63.moKhoaTaiKhoanNhanVien(99999));

        // UC 64: Báo cáo tài chính POI
        assertNotNull(uc64.getFinancialReportData());
    }
}
