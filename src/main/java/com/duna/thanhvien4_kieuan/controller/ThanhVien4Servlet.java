package com.duna.thanhvien4_kieuan.controller;

import com.duna.thanhvien4_kieuan.repository.*;
import com.duna.common.connection.connectService;
import com.duna.common.entities.KhachHang;
import com.duna.common.entities.NhanVien;
import com.duna.common.entities.Voucher;
import com.duna.common.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * THÀNH VIÊN 4: NGUYỄN KIỀU AN - MSSV: TH08490
 * Phân hệ: Quản trị Khách hàng CRM (Hạng 3 tháng), Voucher Khuyến mãi & Nhân sự DUNA
 * Điều phối chính xác 16 Repository riêng biệt (UC49 -> UC64).
 * Đảm bảo 100% usages và hỗ trợ giao diện Khách hàng/Voucher/Nhân sự.
 */
@WebServlet(name = "ThanhVien4Servlet", urlPatterns = {"/ThanhVien4Servlet", "/thanhvien4", "/CustomerServlet", "/VoucherServlet", "/EmployeeManagementServlet", "/AddEmployeeServlet", "/EmployeeDetailServlet", "/ToggleEmployeeStatusServlet", "/UpdateEmployeeServlet"})
public class ThanhVien4Servlet extends HttpServlet {
    private final UC49_DanhSachKhachHangVIPRepo uc49Repo = new UC49_DanhSachKhachHangVIPRepo();
    private final UC50_TimKiemKhachHangVIPRepo uc50Repo = new UC50_TimKiemKhachHangVIPRepo();
    private final UC51_ThemMoiKhachHangRepo uc51Repo = new UC51_ThemMoiKhachHangRepo();
    private final UC52_CapNhatKhachHangRepo uc52Repo = new UC52_CapNhatKhachHangRepo();
    private final UC53_LichSuMuaSamKhachRepo uc53Repo = new UC53_LichSuMuaSamKhachRepo();
    private final UC54_TichLuyThangHangVIPRepo uc54Repo = new UC54_TichLuyThangHangVIPRepo();
    private final UC55_ResetHang3ThangJobRepo uc55Repo = new UC55_ResetHang3ThangJobRepo();
    private final UC56_ThongKe4ChiSoVoucherRepo uc56Repo = new UC56_ThongKe4ChiSoVoucherRepo();
    private final UC57_TaoVoucherVIPLeRepo uc57Repo = new UC57_TaoVoucherVIPLeRepo();
    private final UC58_CauHinhDieuKienVoucherRepo uc58Repo = new UC58_CauHinhDieuKienVoucherRepo();
    private final UC59_TaoLoVoucherHangLoatRepo uc59Repo = new UC59_TaoLoVoucherHangLoatRepo();
    private final UC60_DanhSachNhanVienRepo uc60Repo = new UC60_DanhSachNhanVienRepo();
    private final UC61_XemChiTietHoSoNhanSuRepo uc61Repo = new UC61_XemChiTietHoSoNhanSuRepo();
    private final UC62_ThemNhanVienTuSinhAccountRepo uc62Repo = new UC62_ThemNhanVienTuSinhAccountRepo();
    private final UC63_KhoaTaiKhoanNhanVienRepo uc63Repo = new UC63_KhoaTaiKhoanNhanVienRepo();
    private final UC64_XuatBaoCaoTaiChinhExcelRepo uc64Repo = new UC64_XuatBaoCaoTaiChinhExcelRepo();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String servletPath = req.getServletPath();
        String action = req.getParameter("action");

        // Xử lý các thao tác Form / Link trên màn hình Quản lý Voucher
        if ("/VoucherServlet".equalsIgnoreCase(servletPath)) {
            String vAction = req.getParameter("action");
            if ("toggleStatus".equalsIgnoreCase(vAction)) {
                int id = parseInt(req.getParameter("id"), 0);
                if (id > 0) {
                    connectService db = new connectService();
                    try (Connection conn = db.myConnection();
                         PreparedStatement ps = conn.prepareStatement("UPDATE Vouchers SET TrangThai = CASE WHEN TrangThai = 1 THEN 0 ELSE 1 END WHERE ID_Voucher = ?")) {
                        ps.setInt(1, id);
                        ps.executeUpdate();
                    } catch (Exception e) { e.printStackTrace(); }
                }
                resp.sendRedirect(req.getContextPath() + "/VoucherServlet");
                return;
            } else if ("addCustom".equalsIgnoreCase(vAction)) {
                String vipNumber = req.getParameter("vipNumber");
                String code = "VIP" + (vipNumber != null ? vipNumber.trim().toUpperCase() : (System.currentTimeMillis() % 10000));
                String ten = req.getParameter("tenChuongTrinh");
                long giamGia = parseLong(req.getParameter("giamGia"), 500000L);
                long donToiThieu = parseLong(req.getParameter("donToiThieu"), 2000000L);
                int soLuong = parseInt(req.getParameter("soLuong"), 1);

                Voucher v = new Voucher();
                v.setMaVoucher(code);
                v.setTenChuongTrinh(ten != null ? ten : "Voucher đặc quyền VIP");
                v.setGiamGia(giamGia);
                v.setDonToiThieu(donToiThieu);
                v.setSoLuong(soLuong);
                boolean ok = uc57Repo.taoVoucherVIPLe(v);
                resp.sendRedirect(req.getContextPath() + "/VoucherServlet?success=" + (ok ? "added" : "error"));
                return;
            } else if ("generateBatch".equalsIgnoreCase(vAction)) {
                String prefix = req.getParameter("batchPrefix");
                long amount = parseLong(req.getParameter("batchAmount"), 100000L);
                int qty = parseInt(req.getParameter("batchQty"), 50);
                long donToiThieu = amount > 100 ? (amount * 4) : 500000L;
                int count = uc59Repo.generateBatchVouchers(prefix != null ? prefix : "DUNA", qty, amount, donToiThieu);
                resp.sendRedirect(req.getContextPath() + "/VoucherServlet?success=" + (count > 0 ? "batch" : "error"));
                return;
            }
        }

        // Hỗ trợ điều hướng giao diện JSP nếu truy cập từ Navbar/UI
        if (action == null || action.trim().isEmpty()) {
            if ("/CustomerServlet".equalsIgnoreCase(servletPath)) {
                List<Map<String, Object>> listKH = new ArrayList<>();
                connectService db = new connectService();
                try (Connection conn = db.myConnection()) {
                    if (conn != null) {
                        String sql = "SELECT k.ID_KhachHang, k.MaKH, k.HoTen, k.SoDienThoai, k.Email, k.GioiTinh, k.NgaySinh, k.HangKhachHang, k.DiaChi, k.TrangThai, ISNULL(k.TongTienChiTieu, 0) AS TongChiTieu FROM KhachHang k ORDER BY k.ID_KhachHang DESC";
                        try (PreparedStatement ps = conn.prepareStatement(sql);
                             ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                Map<String, Object> m = new HashMap<>();
                                m.put("id", rs.getInt("ID_KhachHang"));
                                m.put("maKH", rs.getString("MaKH") != null ? rs.getString("MaKH") : "KH" + rs.getInt("ID_KhachHang"));
                                m.put("hoTen", rs.getString("HoTen"));
                                m.put("sdt", rs.getString("SoDienThoai"));
                                m.put("email", rs.getString("Email") != null ? rs.getString("Email") : "");
                                m.put("gioiTinh", rs.getString("GioiTinh") != null ? rs.getString("GioiTinh") : "Nam");
                                m.put("ngaySinh", rs.getString("NgaySinh") != null ? rs.getString("NgaySinh") : "");
                                m.put("diaChi", rs.getString("DiaChi") != null ? rs.getString("DiaChi") : "");
                                m.put("hang", rs.getString("HangKhachHang") != null ? rs.getString("HangKhachHang") : "Thường");
                                m.put("tongChiTieu", rs.getLong("TongChiTieu"));
                                m.put("trangThai", rs.getString("TrangThai") != null ? rs.getString("TrangThai") : "active");
                                m.put("hanReset", "3 tháng");
                                listKH.add(m);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                req.setAttribute("listKH", listKH);
                req.setAttribute("currentPage", 1);
                req.setAttribute("totalPages", 1);
                req.getRequestDispatcher("/quan-ly-khach-hang.jsp").forward(req, resp);
                return;
            } else if ("/VoucherServlet".equalsIgnoreCase(servletPath)) {
                List<Map<String, Object>> vouchers = new ArrayList<>();
                connectService db = new connectService();
                int total = 0, available = 0, used = 0;
                long totalDiscount = 0;

                String search = req.getParameter("search");
                String giamGiaFilter = req.getParameter("giamGia");
                String trangThaiFilter = req.getParameter("trangThai");

                try (Connection conn = db.myConnection()) {
                    if (conn != null) {
                        StringBuilder sql = new StringBuilder("SELECT * FROM Vouchers WHERE 1=1 ");
                        List<Object> pList = new ArrayList<>();
                        if (search != null && !search.trim().isEmpty()) {
                            sql.append("AND (MaVoucher LIKE ? OR TenChuongTrinh LIKE ? OR DonHangSuDung LIKE ?) ");
                            String kw = "%" + search.trim() + "%";
                            pList.add(kw); pList.add(kw); pList.add(kw);
                        }
                        if (giamGiaFilter != null && !giamGiaFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(giamGiaFilter)) {
                            sql.append("AND GiamGia = ? ");
                            pList.add(parseLong(giamGiaFilter, 0));
                        }
                        if ("available".equalsIgnoreCase(trangThaiFilter)) {
                            sql.append("AND TrangThai = 1 AND SoLuong > DaSuDung ");
                        } else if ("used".equalsIgnoreCase(trangThaiFilter)) {
                            sql.append("AND (SoLuong <= DaSuDung OR TrangThai = 0) ");
                        } else if ("locked".equalsIgnoreCase(trangThaiFilter)) {
                            sql.append("AND TrangThai = 0 ");
                        }
                        sql.append("ORDER BY ID_Voucher DESC");

                        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                            for (int i = 0; i < pList.size(); i++) {
                                ps.setObject(i + 1, pList.get(i));
                            }
                            try (ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                    Map<String, Object> v = new HashMap<>();
                                    int id = rs.getInt("ID_Voucher");
                                    String ma = rs.getString("MaVoucher");
                                    String ten = rs.getString("TenChuongTrinh");
                                    long giam = rs.getLong("GiamGia");
                                    long toiThieu = rs.getLong("DonToiThieu");
                                    int soLuong = rs.getInt("SoLuong");
                                    int daDung = rs.getInt("DaSuDung");
                                    int trangThai = rs.getInt("TrangThai");
                                    String donHang = rs.getString("DonHangSuDung");

                                    v.put("ID_Voucher", id);
                                    v.put("id", id);
                                    v.put("MaVoucher", ma);
                                    v.put("maVoucher", ma);
                                    v.put("TenChuongTrinh", ten);
                                    v.put("tenChuongTrinh", ten);
                                    v.put("GiamGia", giam);
                                    v.put("giamGia", giam);
                                    v.put("DonToiThieu", toiThieu);
                                    v.put("donToiThieu", toiThieu);
                                    v.put("SoLuong", soLuong);
                                    v.put("soLuong", soLuong);
                                    v.put("DaSuDung", daDung);
                                    v.put("daSuDung", daDung);
                                    v.put("DonHangSuDung", donHang != null ? donHang : "");
                                    v.put("TrangThai", trangThai);
                                    v.put("trangThai", trangThai == 1 ? "Hoạt động" : "Đã khóa");

                                    vouchers.add(v);
                                    total += soLuong;
                                    if (trangThai == 1 && soLuong > daDung) {
                                        available += (soLuong - daDung);
                                    }
                                    used += daDung;
                                    totalDiscount += (giam * (daDung > 0 ? daDung : 1));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                req.setAttribute("vouchers", vouchers);
                req.setAttribute("stat_total", total);
                req.setAttribute("stat_available", available);
                req.setAttribute("stat_used", used);
                req.setAttribute("stat_discount", totalDiscount);
                req.setAttribute("search", search != null ? search : "");
                req.setAttribute("giamGia", giamGiaFilter != null ? giamGiaFilter : "ALL");
                req.setAttribute("trangThai", trangThaiFilter != null ? trangThaiFilter : "");

                req.getRequestDispatcher("/quan-ly-voucher.jsp").forward(req, resp);
                return;
            } else if ("/EmployeeManagementServlet".equalsIgnoreCase(servletPath)) {
                List<Map<String, String>> listQuanLy = new ArrayList<>();
                List<Map<String, String>> listNhanVien = new ArrayList<>();
                connectService db = new connectService();
                try (Connection conn = db.myConnection()) {
                    if (conn != null) {
                        String sql = "SELECT n.*, tt.TenTrangThai, tk.Username, tk.Role_ID FROM NhanVien n " +
                                     "INNER JOIN TrangThaiNhanVien tt ON n.TrangThaiNV_ID = tt.TrangThaiNV_ID " +
                                     "LEFT JOIN TaiKhoan tk ON n.ID_NhanVien = tk.NhanVienID ORDER BY n.ID_NhanVien ASC";
                        try (PreparedStatement ps = conn.prepareStatement(sql);
                             ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                Map<String, String> m = new HashMap<>();
                                String id = rs.getString("ID_NhanVien");
                                String username = rs.getString("Username") != null ? rs.getString("Username") : "";
                                String hoVaTen = rs.getString("HoVaTen") != null ? rs.getString("HoVaTen") : "";
                                int roleID = 0;
                                try { roleID = rs.getInt("Role_ID"); } catch (Exception ignored) {}
                                m.put("id", id);
                                m.put("ten", hoVaTen);
                                m.put("username", username);
                                String maNV = rs.getString("MaNhanVien");
                                boolean isAdmin = (roleID == 1) || "admin".equalsIgnoreCase(username);
                                if (maNV == null || maNV.trim().isEmpty()) {
                                    maNV = isAdmin ? "QL" + id : "NV" + id;
                                }
                                m.put("maNV", maNV);
                                m.put("ngaySinh", rs.getString("NgaySinh") != null ? rs.getString("NgaySinh") : "Chưa cập nhật");
                                m.put("trangThai", rs.getString("TenTrangThai") != null ? rs.getString("TenTrangThai") : "Hoạt động");
                                m.put("lyDo", "-");
                                if (isAdmin) {
                                    listQuanLy.add(m);
                                } else {
                                    listNhanVien.add(m);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                req.setAttribute("listQuanLy", listQuanLy);
                req.setAttribute("listNV", listNhanVien);
                req.setAttribute("currentPage", 1);
                req.setAttribute("totalPages", 1);
                req.getRequestDispatcher("/employee-management.jsp").forward(req, resp);
                return;
            } else if ("/EmployeeDetailServlet".equalsIgnoreCase(servletPath)) {
                int id = parseInt(req.getParameter("id"), 1);
                NhanVien nv = uc61Repo.getChiTietHoSoCCCD(id);
                req.setAttribute("employee", nv);
                req.getRequestDispatcher("/employee-detail.jsp").forward(req, resp);
                return;
            } else if ("/AddEmployeeServlet".equalsIgnoreCase(servletPath)) {
                req.getRequestDispatcher("/add-employee.jsp").forward(req, resp);
                return;
            }
            action = "uc49"; // Default: xem danh sách khách hàng
        }

        resp.setContentType("application/json;charset=UTF-8");
        action = action.trim().toLowerCase();

        try {
            switch (action) {
                // UC 49: Danh sách khách hàng & phân hạng
                case "uc49": {
                    List<KhachHang> list = uc49Repo.getDanhSachKhachHangVaHang();
                    resp.getWriter().write(JsonUtil.toJson(list));
                    break;
                }

                // UC 50: Tìm kiếm & lọc khách hàng VIP
                case "uc50": {
                    String q = req.getParameter("query");
                    String vip = req.getParameter("hangVIP");
                    List<KhachHang> list = uc50Repo.searchKhachHang(q, vip);
                    resp.getWriter().write(JsonUtil.toJson(list));
                    break;
                }

                // UC 51: Thêm mới khách hàng chuẩn hóa (+84)
                case "uc51": {
                    KhachHang kh = new KhachHang();
                    kh.setHoVaTen(req.getParameter("hoTen"));
                    kh.setSoDienThoai(req.getParameter("phone"));
                    kh.setEmail(req.getParameter("email"));
                    kh.setDiaChi(req.getParameter("diaChi"));
                    kh.setGioiTinh(req.getParameter("gioiTinh"));
                    boolean ok = uc51Repo.themKhachHangChuanHoa(kh);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 52: Cập nhật thông tin khách hàng
                case "uc52": {
                    KhachHang kh = new KhachHang();
                    kh.setId(parseInt(req.getParameter("id"), 0));
                    kh.setHoVaTen(req.getParameter("hoTen"));
                    kh.setSoDienThoai(req.getParameter("phone"));
                    kh.setEmail(req.getParameter("email"));
                    kh.setDiaChi(req.getParameter("diaChi"));
                    kh.setGioiTinh(req.getParameter("gioiTinh"));
                    boolean ok = uc52Repo.capNhatThongTinKhachHang(kh);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 53: Lịch sử mua sắm khách hàng
                case "uc53": {
                    int idKH = parseInt(req.getParameter("id"), 0);
                    List<Map<String, Object>> history = uc53Repo.getLichSuMuaSam(idKH);
                    resp.getWriter().write(JsonUtil.toJson(history));
                    break;
                }

                // UC 54: Tích lũy chi tiêu & thăng hạng VIP
                case "uc54": {
                    int idKH = parseInt(req.getParameter("id"), 0);
                    long amount = parseLong(req.getParameter("amount"), 0);
                    String rank = uc54Repo.tichLuyChiTieuVaThangHang(idKH, amount);
                    Map<String, Object> res = new HashMap<>();
                    res.put("newRank", rank);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 55: Job reset hạng 3 tháng cuốn chiếu
                case "uc55": {
                    int affected = uc55Repo.jobResetHang3ThangCuonChieu();
                    Map<String, Object> res = new HashMap<>();
                    res.put("resetCount", affected);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 56: Bảng thống kê 4 chỉ số Voucher
                case "uc56": {
                    Map<String, Integer> kpi = uc56Repo.getThongKeVoucher();
                    resp.getWriter().write(JsonUtil.toJson(kpi));
                    break;
                }

                // UC 57: Tạo Voucher VIP lẻ
                case "uc57": {
                    Voucher v = new Voucher();
                    v.setMaVoucher(req.getParameter("code"));
                    v.setTenChuongTrinh(req.getParameter("ten"));
                    v.setGiamGia(parseLong(req.getParameter("giamGia"), 50000L));
                    v.setDonToiThieu(parseLong(req.getParameter("donToiThieu"), 200000L));
                    v.setSoLuong(parseInt(req.getParameter("soLuong"), 1));
                    boolean ok = uc57Repo.taoVoucherVIPLe(v);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 58: Cấu hình điều kiện kích hoạt Voucher
                case "uc58": {
                    int idV = parseInt(req.getParameter("id"), 0);
                    long minOrder = parseLong(req.getParameter("donToiThieu"), 0);
                    int qty = parseInt(req.getParameter("soLuong"), 1);
                    String hanDung = req.getParameter("hanDung");
                    boolean ok = (hanDung != null) ?
                        uc58Repo.cauHinhDieuKienVoucher(idV, minOrder, qty, hanDung) :
                        uc58Repo.cauHinhDieuKienVoucher(idV, minOrder, qty);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 59: Sinh lô Voucher hàng loạt
                case "uc59": {
                    String prefix = req.getParameter("prefix");
                    int qty = parseInt(req.getParameter("quantity"), 10);
                    long discount = parseLong(req.getParameter("giamGia"), 50000L);
                    long minOrder = parseLong(req.getParameter("donToiThieu"), 300000L);
                    int count = uc59Repo.generateBatchVouchers(prefix, qty, discount, minOrder);
                    Map<String, Object> res = new HashMap<>();
                    res.put("createdCount", count);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 60: Danh sách nhân viên & trạng thái hoạt động
                case "uc60": {
                    List<NhanVien> list = uc60Repo.getDanhSachNhanVienVaTrangThai();
                    resp.getWriter().write(JsonUtil.toJson(list));
                    break;
                }
                case "uc60_status": {
                    int idNV = parseInt(req.getParameter("id"), 0);
                    int status = parseInt(req.getParameter("status"), 1);
                    boolean ok = uc60Repo.capNhatTrangThaiNhanVien(idNV, status);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 61: Xem chi tiết hồ sơ CCCD
                case "uc61": {
                    int idNV = parseInt(req.getParameter("id"), 0);
                    NhanVien nv = uc61Repo.getChiTietHoSoCCCD(idNV);
                    resp.getWriter().write(JsonUtil.toJson(nv));
                    break;
                }

                // UC 62: Thêm mới nhân viên, check CCCD
                case "uc62": {
                    NhanVien nv = new NhanVien();
                    nv.setHoVaTen(req.getParameter("hoTen"));
                    nv.setSoDienThoai(req.getParameter("phone"));
                    nv.setEmail(req.getParameter("email"));
                    nv.setMaSoCCCD(req.getParameter("cccd"));
                    nv.setDiaChi(req.getParameter("diaChi"));
                    boolean isQL = "true".equalsIgnoreCase(req.getParameter("isQuanLy"));
                    boolean ok = uc62Repo.themNhanVienKiemTraCCCD(nv, isQL);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 63: Khóa tài khoản nhân viên kèm lý do / Mở khóa
                case "uc63": {
                    int idNV = parseInt(req.getParameter("id"), 0);
                    String lyDo = req.getParameter("lyDo");
                    boolean ok = uc63Repo.khoaTaiKhoanNhanVien(idNV, lyDo);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }
                case "uc63_unlock": {
                    int idNV = parseInt(req.getParameter("id"), 0);
                    boolean ok = uc63Repo.moKhoaTaiKhoanNhanVien(idNV);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 64: Báo cáo tài chính & Biểu đồ xuất Excel POI
                case "uc64": {
                    List<Map<String, Object>> report = uc64Repo.getFinancialReportData();
                    resp.getWriter().write(JsonUtil.toJson(report));
                    break;
                }

                default: {
                    Map<String, String> err = new HashMap<>();
                    err.put("error", "Action không hợp lệ: " + action);
                    resp.getWriter().write(JsonUtil.toJson(err));
                    break;
                }
            }
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            resp.getWriter().write(JsonUtil.toJson(err));
        }
    }

    private int parseInt(String val, int defaultVal) {
        try {
            return (val != null && !val.trim().isEmpty()) ? Integer.parseInt(val.trim()) : defaultVal;
        } catch (Exception e) {
            return defaultVal;
        }
    }

    private long parseLong(String val, long defaultVal) {
        try {
            return (val != null && !val.trim().isEmpty()) ? Long.parseLong(val.trim()) : defaultVal;
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
