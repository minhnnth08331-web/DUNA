import junit.framework.TestCase;
import java.util.Map;
import model.BienTheSanPham;
import model.ChiTietDonHang;
import model.DanhMuc;
import model.DonHang;
import model.KhachHang;
import model.NhanVien;
import model.SanPham;
import model.Voucher;

/**
 * Unit test kiểm thử các Thực thể (Entity) và cơ chế Mapping (Entity <-> Map)
 * Chuỗi thời trang nam DUNA
 */
public class EntityMapperTest extends TestCase {

    public void testSanPhamEntityAndMap() {
        SanPham sp = new SanPham();
        sp.setId(101);
        sp.setMaSP("SP-POLO-01");
        sp.setTenSP("Áo Polo Nam Classic DUNA");
        sp.setDanhMuc("Áo Polo");
        sp.setGiaBan(299000L);
        sp.setGiaNhap(150000L);
        sp.setSoLuong(50);
        sp.setTrangThai("Còn hàng");

        // Test toMap
        Map<String, Object> map = sp.toMap();
        assertEquals(101, map.get("id"));
        assertEquals("SP-POLO-01", map.get("maSP"));
        assertEquals("Áo Polo Nam Classic DUNA", map.get("tenSP"));
        assertEquals("Áo Polo", map.get("danhMuc"));
        assertEquals(299000L, map.get("giaBan"));
        assertEquals(50, map.get("soLuong"));

        // Test fromMap
        SanPham sp2 = SanPham.fromMap(map);
        assertEquals(sp.getId(), sp2.getId());
        assertEquals(sp.getMaSP(), sp2.getMaSP());
        assertEquals(sp.getTenSP(), sp2.getTenSP());
        assertEquals(sp.getGiaBan(), sp2.getGiaBan());
    }

    public void testBienTheSanPhamEntityAndMap() {
        BienTheSanPham bt = new BienTheSanPham();
        bt.setIdBienThe(501);
        bt.setIdSanPham(101);
        bt.setMauSac("Xanh Navy");
        bt.setTrongLuong("L");
        bt.setGiaBan(299000L);
        bt.setSoLuong(20);

        Map<String, Object> map = bt.toMap();
        assertEquals(501, map.get("id"));
        assertEquals("Xanh Navy", map.get("mauSac"));
        assertEquals("L", map.get("trongLuong"));
        assertEquals("L", map.get("size"));

        BienTheSanPham bt2 = BienTheSanPham.fromMap(map);
        assertEquals(bt.getIdBienThe(), bt2.getIdBienThe());
        assertEquals(bt.getMauSac(), bt2.getMauSac());
        assertEquals(bt.getSize(), bt2.getSize());
    }

    public void testKhachHangEntityAndTierMap() {
        KhachHang kh = new KhachHang();
        kh.setId(201);
        kh.setHoVaTen("Nguyễn Văn A");
        kh.setSoDienThoai("0988776655");
        kh.setHangKhachHang("VIP");
        kh.setTongSPMua(15);
        kh.setPhanTramGiam(7);

        Map<String, Object> map = kh.toMap();
        assertEquals("VIP", map.get("hangKhachHang"));
        assertEquals(15, map.get("tongSPMua"));
        assertEquals(7, map.get("phanTramGiam"));

        KhachHang kh2 = KhachHang.fromMap(map);
        assertEquals("Nguyễn Văn A", kh2.getHoVaTen());
        assertEquals("VIP", kh2.getHangKhachHang());
    }

    public void testDonHangAndChiTietEntityAndMap() {
        DonHang dh = new DonHang();
        dh.setId(301);
        dh.setMaDonHang("DH-20261113-001");
        dh.setTongTien(598000L);
        dh.setGiamGia(41860L);
        dh.setThanhTien(556140L);
        dh.setTenTrangThai("Đã thanh toán");

        ChiTietDonHang item = new ChiTietDonHang();
        item.setIdChiTiet(1);
        item.setIdDonHang(301);
        item.setTenSP("Áo Polo Nam Classic DUNA");
        item.setSoLuong(2);
        item.setDonGia(299000L);
        item.setThanhTien(598000L);
        dh.getDanhSachChiTiet().add(item);

        Map<String, Object> mapDH = dh.toMap();
        assertEquals("DH-20261113-001", mapDH.get("maDonHang"));
        assertEquals(556140L, mapDH.get("thanhTien"));

        Map<String, Object> mapItem = item.toMap();
        assertEquals(2, mapItem.get("soLuong"));
        assertEquals(598000L, mapItem.get("thanhTien"));
    }

    public void testVoucherEntityAndMap() {
        Voucher v = new Voucher();
        v.setId(401);
        v.setMaVoucher("DUNAVIP50");
        v.setTenChuongTrinh("Tri ân khách hàng VIP");
        v.setGiamGia(50000L);
        v.setDonToiThieu(300000L);
        v.setSoLuong(100);
        v.setDaSuDung(25);
        v.setTrangThai(1);
        v.setLoaiVoucher("VIP");

        assertTrue(v.isConHieuLuc());

        Map<String, Object> map = v.toMap();
        assertEquals("DUNAVIP50", map.get("maVoucher"));
        assertEquals(50000L, map.get("giamGia"));
        assertEquals("VIP", map.get("loaiVoucher"));
    }
}

