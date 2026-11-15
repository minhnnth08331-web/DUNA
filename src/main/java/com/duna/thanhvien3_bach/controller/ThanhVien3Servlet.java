package com.duna.thanhvien3_bach.controller;

import com.duna.thanhvien3_bach.repository.*;
import com.duna.common.connection.connectService;
import com.duna.common.entities.ChiTietDonHang;
import com.duna.common.entities.DonHang;
import com.duna.common.entities.KhachHang;
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
import java.sql.Statement;
import java.util.*;

/**
 * THÀNH VIÊN 3: LÊ MINH BÁCH - MSSV: TH08442
 * Phân hệ: POS Giai đoạn 2 (Thanh toán VietQR, In hóa đơn) & Báo cáo Thống kê Doanh thu DUNA
 * Điều phối chính xác 16 Repository riêng biệt (UC33 -> UC48).
 * Đảm bảo 100% usages và hỗ trợ giao diện Bán hàng/Hóa đơn/Báo cáo.
 */
@WebServlet(name = "ThanhVien3Servlet", urlPatterns = {"/ThanhVien3Servlet", "/thanhvien3", "/OrderServlet", "/QRConfirmServlet"})
public class ThanhVien3Servlet extends HttpServlet {
    private final UC33_TraCuuKhachHangPOSRepo uc33Repo = new UC33_TraCuuKhachHangPOSRepo();
    private final UC34_ThemNhanhKhachHangPOSRepo uc34Repo = new UC34_ThemNhanhKhachHangPOSRepo();
    private final UC35_ApDungChietKhauVIPRepo uc35Repo = new UC35_ApDungChietKhauVIPRepo();
    private final UC36_ApDungVoucherPOSRepo uc36Repo = new UC36_ApDungVoucherPOSRepo();
    private final UC37_TinhTongHoaDonRepo uc37Repo = new UC37_TinhTongHoaDonRepo();
    private final UC38_ThanhToanTienMatRepo uc38Repo = new UC38_ThanhToanTienMatRepo();
    private final UC39_SinhMaVietQRDongRepo uc39Repo = new UC39_SinhMaVietQRDongRepo();
    private final UC40_XacNhanThanhToanQRRepo uc40Repo = new UC40_XacNhanThanhToanQRRepo();
    private final UC41_InHoaDonNhietK80Repo uc41Repo = new UC41_InHoaDonNhietK80Repo();
    private final UC42_DanhSachHoaDonRepo uc42Repo = new UC42_DanhSachHoaDonRepo();
    private final UC43_XemChiTietHoaDonRepo uc43Repo = new UC43_XemChiTietHoaDonRepo();
    private final UC44_InLaiHoaDonRepo uc44Repo = new UC44_InLaiHoaDonRepo();
    private final UC45_XuatExcelHoaDonRepo uc45Repo = new UC45_XuatExcelHoaDonRepo();
    private final UC46_ThongKeLoiNhuanHomNayRepo uc46Repo = new UC46_ThongKeLoiNhuanHomNayRepo();
    private final UC47_ThongKeLoiNhuanThangNamRepo uc47Repo = new UC47_ThongKeLoiNhuanThangNamRepo();
    private final UC48_BieuDoDoanhThuChartJSRepo uc48Repo = new UC48_BieuDoDoanhThuChartJSRepo();

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

        // 1. AJAX Kiểm tra tính hợp lệ của Voucher tại quầy
        if ("1".equals(req.getParameter("ajaxCheckVoucher"))) {
            resp.setContentType("application/json;charset=UTF-8");
            String code = req.getParameter("code");
            long tongTien = parseLong(req.getParameter("tongTien"), 0);
            Map<String, Object> res = new HashMap<>();
            if (code == null || code.trim().isEmpty()) {
                res.put("valid", false);
                res.put("message", "Vui lòng nhập mã voucher!");
            } else {
                String cleanCode = code.trim();
                connectService db = new connectService();
                try (Connection con = db.myConnection();
                     PreparedStatement ps = con.prepareStatement("SELECT * FROM Vouchers WHERE MaVoucher = ?")) {
                    ps.setString(1, cleanCode);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            int trangThai = rs.getInt("TrangThai");
                            int soLuong = rs.getInt("SoLuong");
                            int daSuDung = rs.getInt("DaSuDung");
                            long donToiThieu = rs.getLong("DonToiThieu");
                            long giamGia = rs.getLong("GiamGia");

                            if (trangThai != 1) {
                                res.put("valid", false);
                                res.put("message", "Mã voucher này đã bị khóa hoặc tạm dừng!");
                            } else if (daSuDung >= soLuong) {
                                res.put("valid", false);
                                res.put("message", "Mã voucher đã hết lượt sử dụng!");
                            } else if (tongTien < donToiThieu) {
                                res.put("valid", false);
                                res.put("minOrder", donToiThieu);
                                res.put("message", "Đơn hàng chưa đạt giá trị tối thiểu " + String.format("%,d", donToiThieu).replace(',', '.') + " đ để áp dụng voucher này!");
                            } else {
                                long discountAmount = giamGia;
                                if (giamGia <= 100) {
                                    discountAmount = Math.round(tongTien * giamGia / 100.0);
                                }
                                res.put("valid", true);
                                res.put("amount", discountAmount);
                                res.put("minOrder", donToiThieu);
                                res.put("message", "Áp dụng thành công mã " + cleanCode + " (Giảm " + String.format("%,d", discountAmount).replace(',', '.') + " đ)");
                            }
                        } else {
                            res.put("valid", false);
                            res.put("message", "Mã voucher không tồn tại trong hệ thống DUNA!");
                        }
                    }
                } catch (Exception e) {
                    res.put("valid", false);
                    res.put("message", "Lỗi kiểm tra voucher: " + e.getMessage());
                }
            }
            resp.getWriter().write(JsonUtil.toJson(res));
            return;
        }

        // 2. AJAX Tra cứu danh sách voucher khả dụng (Modal tra cứu voucher)
        if ("1".equals(req.getParameter("ajaxLookupVouchers"))) {
            resp.setContentType("application/json;charset=UTF-8");
            Map<String, Object> res = new HashMap<>();
            List<Map<String, Object>> vList = new ArrayList<>();
            connectService db = new connectService();
            try (Connection con = db.myConnection();
                 PreparedStatement ps = con.prepareStatement("SELECT * FROM Vouchers ORDER BY ID_Voucher DESC");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> v = new HashMap<>();
                    String c = rs.getString("MaVoucher");
                    v.put("code", c);
                    v.put("title", rs.getString("TenChuongTrinh"));
                    long giam = rs.getLong("GiamGia");
                    v.put("discount", giam <= 100 ? ("Giảm " + giam + "%") : ("Giảm " + String.format("%,d", giam).replace(',', '.') + " đ"));
                    v.put("minOrder", rs.getLong("DonToiThieu"));
                    int sl = rs.getInt("SoLuong");
                    int du = rs.getInt("DaSuDung");
                    v.put("stock", sl);
                    v.put("used", du);
                    int st = rs.getInt("TrangThai");
                    String stStr = (st != 1) ? "locked" : ((du >= sl) ? "used" : "ready");
                    v.put("status", stStr);
                    vList.add(v);
                }
                res.put("success", true);
                res.put("vouchers", vList);
            } catch (Exception e) {
                res.put("success", false);
                res.put("message", e.getMessage());
            }
            resp.getWriter().write(JsonUtil.toJson(res));
            return;
        }

        // 3. AJAX Quét mã vạch Barcode (USB Scanner hoặc WebRTC Camera)
        if ("1".equals(req.getParameter("ajaxScanBarcode"))) {
            resp.setContentType("application/json;charset=UTF-8");
            String barcode = req.getParameter("barcode");
            Map<String, Object> res = new HashMap<>();
            if (barcode == null || barcode.trim().isEmpty()) {
                res.put("found", false);
            } else {
                String cleanBc = barcode.trim();
                connectService db = new connectService();
                try (Connection con = db.myConnection()) {
                    boolean found = false;
                    String sqlBt = "SELECT bt.*, sp.TenSP, sp.MaSP FROM BienTheSanPham bt INNER JOIN SanPham sp ON bt.ID_SanPham = sp.ID_SanPham WHERE bt.Barcode = ? OR bt.MaBienThe = ?";
                    try (PreparedStatement ps = con.prepareStatement(sqlBt)) {
                        ps.setString(1, cleanBc);
                        ps.setString(2, cleanBc);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                res.put("found", true);
                                res.put("id", rs.getInt("ID_SanPham"));
                                res.put("idBT", rs.getInt("ID_BienThe"));
                                res.put("maSP", rs.getString("MaSP"));
                                res.put("tenSP", rs.getString("TenSP"));
                                res.put("giaBan", rs.getLong("GiaBan"));
                                res.put("soLuong", rs.getInt("SoLuong"));
                                res.put("mauSac", rs.getString("MauSac"));
                                res.put("trongLuong", rs.getString("TrongLuong"));
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        String sqlSp = "SELECT * FROM SanPham WHERE MaSP = ? OR CAST(ID_SanPham AS VARCHAR) = ?";
                        try (PreparedStatement ps = con.prepareStatement(sqlSp)) {
                            ps.setString(1, cleanBc);
                            ps.setString(2, cleanBc);
                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                    res.put("found", true);
                                    res.put("id", rs.getInt("ID_SanPham"));
                                    res.put("idBT", 0);
                                    res.put("maSP", rs.getString("MaSP"));
                                    res.put("tenSP", rs.getString("TenSP"));
                                    res.put("giaBan", rs.getLong("GiaBan"));
                                    res.put("soLuong", rs.getInt("TongSoLuong"));
                                    res.put("mauSac", "Tiêu chuẩn");
                                    res.put("trongLuong", "M");
                                    found = true;
                                }
                            }
                        }
                    }
                    if (!found) {
                        res.put("found", false);
                    }
                } catch (Exception e) {
                    res.put("found", false);
                    res.put("error", e.getMessage());
                }
            }
            resp.getWriter().write(JsonUtil.toJson(res));
            return;
        }

        // 4. AJAX Thêm nhanh khách hàng tại quầy (Quick Add Customer)
        if ("1".equals(req.getParameter("ajaxAddKH"))) {
            resp.setContentType("application/json;charset=UTF-8");
            String hoTen = req.getParameter("hoTen");
            String sdt = req.getParameter("sdt");
            String email = req.getParameter("email");
            Map<String, Object> res = new HashMap<>();
            if (hoTen == null || hoTen.trim().isEmpty() || sdt == null || sdt.trim().isEmpty()) {
                res.put("success", false);
                res.put("message", "Vui lòng nhập họ tên và số điện thoại!");
            } else {
                connectService db = new connectService();
                try (Connection con = db.myConnection()) {
                    boolean existed = false;
                    int khId = 0;
                    String checkSql = "SELECT ID_KhachHang, HoTen, SoDienThoai FROM KhachHang WHERE SoDienThoai = ?";
                    try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                        ps.setString(1, sdt.trim());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                existed = true;
                                khId = rs.getInt("ID_KhachHang");
                                hoTen = rs.getString("HoTen");
                            }
                        }
                    }
                    if (!existed) {
                        String insertSql = "INSERT INTO KhachHang (HoTen, SoDienThoai, Email, HangKhachHang, TongTienChiTieu, TrangThai, NgayTao) VALUES (?, ?, ?, 'Thường', 0, 'active', GETDATE())";
                        try (PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                            ps.setString(1, hoTen.trim());
                            ps.setString(2, sdt.trim());
                            ps.setString(3, email != null ? email.trim() : "");
                            ps.executeUpdate();
                            try (ResultSet rs = ps.getGeneratedKeys()) {
                                if (rs.next()) khId = rs.getInt(1);
                            }
                        }
                    }
                    res.put("success", true);
                    res.put("id", khId);
                    res.put("ten", hoTen.trim());
                    res.put("sdt", sdt.trim());
                    res.put("existed", existed);
                } catch (Exception e) {
                    res.put("success", false);
                    res.put("message", "Lỗi thêm khách hàng: " + e.getMessage());
                }
            }
            resp.getWriter().write(JsonUtil.toJson(res));
            return;
        }

        // 5. AJAX In hóa đơn bán lẻ (Print Invoice)
        if ("1".equals(req.getParameter("ajaxDetail"))) {
            resp.setContentType("application/json;charset=UTF-8");
            int idDH = parseInt(req.getParameter("id"), 0);
            Map<String, Object> res = new HashMap<>();
            connectService db = new connectService();
            try (Connection con = db.myConnection()) {
                String sqlOrder = "SELECT d.*, kh.HoTen AS TenKH, kh.SoDienThoai AS SdtKH, kh.DiaChi AS DiaChiKH, nv.HoVaTen AS TenNV " +
                                  "FROM DonHang d " +
                                  "LEFT JOIN KhachHang kh ON d.ID_KhachHang = kh.ID_KhachHang " +
                                  "LEFT JOIN NhanVien nv ON d.ID_NhanVien = nv.ID_NhanVien " +
                                  "WHERE d.ID_DonHang = ?";
                try (PreparedStatement ps = con.prepareStatement(sqlOrder)) {
                    ps.setInt(1, idDH);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            res.put("maDH", rs.getString("MaDonHang"));
                            res.put("ngayTao", rs.getTimestamp("NgayTao") != null ? rs.getTimestamp("NgayTao").toString() : "");
                            res.put("tenKH", rs.getString("TenKH") != null ? rs.getString("TenKH") : "Khách vãng lai");
                            res.put("sdtKH", rs.getString("SdtKH") != null ? rs.getString("SdtKH") : "-");
                            res.put("diaChiKH", rs.getString("DiaChiKH") != null ? rs.getString("DiaChiKH") : "Tại cửa hàng DUNA");
                            res.put("thanhToan", rs.getString("PhuongThucThanhToan"));
                            res.put("trangThai", rs.getString("TrangThaiDonHang"));
                            res.put("tenNV", rs.getString("TenNV") != null ? rs.getString("TenNV") : "Thu ngân DUNA");
                            res.put("ghiChu", rs.getString("GhiChu") != null ? rs.getString("GhiChu") : "");
                            res.put("tongTien", rs.getLong("TongTien"));
                        }
                    }
                }
                List<ChiTietDonHang> items = uc43Repo.getChiTietHoaDon(idDH);
                res.put("items", items);
            } catch (Exception e) {
                res.put("error", e.getMessage());
            }
            resp.getWriter().write(JsonUtil.toJson(res));
            return;
        }

        // 6. AJAX Cập nhật thuế suất VAT
        if ("updateVAT".equalsIgnoreCase(req.getParameter("action"))) {
            resp.setContentType("application/json;charset=UTF-8");
            int newVat = parseInt(req.getParameter("vatPct"), 8);
            req.getSession().setAttribute("currentVat", newVat);
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("newVat", newVat);
            res.put("message", "Đã lưu mức thuế VAT " + newVat + "% vào hệ thống thành công!");
            resp.getWriter().write(JsonUtil.toJson(res));
            return;
        }

        String servletPath = req.getServletPath();
        String view = req.getParameter("view");
        String action = req.getParameter("action");

        // Hỗ trợ điều hướng giao diện JSP Quản lý hóa đơn / Bán hàng / Báo cáo
        if (action == null || action.trim().isEmpty()) {
            if ("/QRConfirmServlet".equalsIgnoreCase(servletPath)) {
                req.getRequestDispatcher("/qr-mobile.jsp").forward(req, resp);
                return;
            }
            if ("sell".equalsIgnoreCase(view)) {
                List<Map<String, Object>> listKH = new ArrayList<>();
                List<Map<String, Object>> listSP = new ArrayList<>();
                connectService db = new connectService();
                try (Connection conn = db.myConnection()) {
                    if (conn != null) {
                        try (PreparedStatement ps = conn.prepareStatement("SELECT ID_KhachHang, HoTen, SoDienThoai, HangKhachHang FROM KhachHang WHERE TrangThai = 'active' ORDER BY HoTen");
                             ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                Map<String, Object> k = new HashMap<>();
                                k.put("id", rs.getInt("ID_KhachHang"));
                                k.put("ten", rs.getString("HoTen"));
                                k.put("sdt", rs.getString("SoDienThoai"));
                                String hang = rs.getString("HangKhachHang");
                                k.put("hang", hang != null ? hang : "Thường");
                                int discount = 0;
                                if ("VIP".equalsIgnoreCase(hang)) discount = 10;
                                else if ("Vàng".equalsIgnoreCase(hang)) discount = 5;
                                else if ("Bạc".equalsIgnoreCase(hang)) discount = 2;
                                k.put("discountPct", discount);
                                listKH.add(k);
                            }
                        }
                        try (PreparedStatement ps = conn.prepareStatement("SELECT s.ID_SanPham, s.MaSP, s.TenSP, s.GiaBan, s.TongSoLuong FROM SanPham s WHERE s.TongSoLuong > 0 ORDER BY s.TenSP");
                             ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                Map<String, Object> sp = new HashMap<>();
                                sp.put("id", rs.getInt("ID_SanPham"));
                                sp.put("maSP", rs.getString("MaSP"));
                                sp.put("tenSP", rs.getString("TenSP"));
                                sp.put("thuongHieu", "DUNA");
                                sp.put("giaBan", rs.getLong("GiaBan"));
                                sp.put("soLuong", rs.getInt("TongSoLuong"));
                                sp.put("phanCap", "Tiêu chuẩn");
                                sp.put("mauSacList", Arrays.asList("Đen", "Trắng", "Xanh navy", "Xám"));
                                sp.put("trongLuongList", Arrays.asList("S", "M", "L", "XL", "2XL"));
                                listSP.add(sp);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                req.setAttribute("listKH", listKH);
                req.setAttribute("listSP", listSP);
                req.setAttribute("currentVat", 8);
                req.getRequestDispatcher("/ban-hang.jsp").forward(req, resp);
                return;
            } else if ("report".equalsIgnoreCase(view)) {
                handleReportView(req, resp);
                return;
            } else if ("order".equalsIgnoreCase(view) || "/OrderServlet".equalsIgnoreCase(servletPath)) {
                handleOrderView(req, resp);
                return;
            }
            action = "uc46";
        }

        // Xử lý hủy đơn / cập nhật trạng thái đơn hàng từ Form
        if ("changeStatus".equalsIgnoreCase(action)) {
            int id = parseInt(req.getParameter("id"), 0);
            String trangThai = req.getParameter("trangThai");
            int statusId = "Đã hủy".equalsIgnoreCase(trangThai) ? 3 : ("Đã thanh toán".equalsIgnoreCase(trangThai) ? 1 : 2);
            connectService db = new connectService();
            try (Connection conn = db.myConnection();
                 PreparedStatement ps = conn.prepareStatement("UPDATE DonHang SET TrangThaiDH_ID = ? WHERE ID_DonHang = ?")) {
                ps.setInt(1, statusId);
                ps.setInt(2, id);
                ps.executeUpdate();
            } catch (Exception e) { e.printStackTrace(); }
            resp.sendRedirect(req.getContextPath() + "/OrderServlet");
            return;
        }

        resp.setContentType("application/json;charset=UTF-8");
        action = action.trim().toLowerCase();

        try {
            switch (action) {
                // UC 33: Tra cứu khách hàng theo SĐT
                case "uc33": {
                    String phone = req.getParameter("phone");
                    KhachHang kh = uc33Repo.searchCustomerByPhone(phone);
                    resp.getWriter().write(JsonUtil.toJson(kh));
                    break;
                }

                // UC 34: Thêm nhanh khách hàng tại quầy
                case "uc34": {
                    String hoTen = req.getParameter("hoTen");
                    String sdt = req.getParameter("phone");
                    KhachHang kh = uc34Repo.quickAddCustomer(hoTen, sdt);
                    resp.getWriter().write(JsonUtil.toJson(kh));
                    break;
                }

                // UC 35: Chiết khấu hạng VIP
                case "uc35": {
                    long subtotal = parseLong(req.getParameter("subtotal"), 0);
                    String hangVIP = req.getParameter("hangVIP");
                    long discount = uc35Repo.tinhGiamGiaHangVIP(subtotal, hangVIP);
                    Map<String, Object> res = new HashMap<>();
                    res.put("discountVIP", discount);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 36: Kiểm tra & áp dụng Voucher
                case "uc36": {
                    String code = req.getParameter("code");
                    long subtotal = parseLong(req.getParameter("subtotal"), 0);
                    long voucherDiscount = uc36Repo.kiemTraVaApDungVoucher(code, subtotal);
                    Map<String, Object> res = new HashMap<>();
                    res.put("discountVoucher", voucherDiscount);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 37: Tính toán tổng hóa đơn
                case "uc37": {
                    long subtotal = parseLong(req.getParameter("subtotal"), 0);
                    long vip = parseLong(req.getParameter("vipDiscount"), 0);
                    long voucher = parseLong(req.getParameter("voucherDiscount"), 0);
                    boolean vat = "true".equalsIgnoreCase(req.getParameter("vat"));
                    Map<String, Long> summary = uc37Repo.tinhToanTongHoaDon(subtotal, vip, voucher, vat);
                    resp.getWriter().write(JsonUtil.toJson(summary));
                    break;
                }

                // UC 38: Tính tiền thối
                case "uc38": {
                    double total = parseDouble(req.getParameter("total"), 0);
                    double cash = parseDouble(req.getParameter("cash"), 0);
                    double change = uc38Repo.tinhTienThoi(total, cash);
                    Map<String, Object> res = new HashMap<>();
                    res.put("change", change);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 39: Sinh mã VietQR
                case "uc39": {
                    String maDH = req.getParameter("maDH");
                    long total = parseLong(req.getParameter("total"), 0);
                    String qrUrl = uc39Repo.sinhMaVietQR(maDH, total);
                    Map<String, Object> res = new HashMap<>();
                    res.put("qrUrl", qrUrl);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 40: Xác nhận chuyển khoản tức thì
                case "uc40": {
                    int idDH = parseInt(req.getParameter("idDonHang"), 0);
                    boolean ok = uc40Repo.xacNhanChuyenKhoanQRTucThi(idDH);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 41: In hóa đơn K80
                case "uc41": {
                    int idDH = parseInt(req.getParameter("idDonHang"), 0);
                    String k80 = uc41Repo.taoNoiDungHoaDonK80(idDH);
                    Map<String, Object> res = new HashMap<>();
                    res.put("contentK80", k80);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 42: Danh sách đơn hàng theo ngày
                case "uc42": {
                    String from = req.getParameter("from");
                    String to = req.getParameter("to");
                    List<DonHang> list = uc42Repo.getDonHangTheoNgay(from, to);
                    resp.getWriter().write(JsonUtil.toJson(list));
                    break;
                }

                // UC 43: Chi tiết hóa đơn
                case "uc43": {
                    int idDH = parseInt(req.getParameter("idDonHang"), 0);
                    List<ChiTietDonHang> items = uc43Repo.getChiTietHoaDon(idDH);
                    resp.getWriter().write(JsonUtil.toJson(items));
                    break;
                }

                // UC 44: In lại hóa đơn
                case "uc44": {
                    int idDH = parseInt(req.getParameter("idDonHang"), 0);
                    String reprint = uc44Repo.reprintHoaDon(idDH);
                    Map<String, Object> res = new HashMap<>();
                    res.put("reprintContent", reprint);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 45: Xuất Excel POI
                case "uc45": {
                    String from = req.getParameter("from");
                    String to = req.getParameter("to");
                    List<Map<String, Object>> exportData = uc45Repo.exportOrdersData(from, to);
                    resp.getWriter().write(JsonUtil.toJson(exportData));
                    break;
                }

                // UC 46: Doanh thu hôm nay
                case "uc46": {
                    Map<String, Long> stats = uc46Repo.getDoanhThuHomNay();
                    resp.getWriter().write(JsonUtil.toJson(stats));
                    break;
                }

                // UC 47: Doanh thu theo tháng/năm
                case "uc47": {
                    int month = parseInt(req.getParameter("month"), 11);
                    int year = parseInt(req.getParameter("year"), 2026);
                    Map<String, Long> stats = uc47Repo.getDoanhThuThangNam(month, year);
                    resp.getWriter().write(JsonUtil.toJson(stats));
                    break;
                }

                // UC 48: Biểu đồ Chart.js
                case "uc48": {
                    int days = parseInt(req.getParameter("days"), 7);
                    List<Map<String, Object>> chart = uc48Repo.getChartData(days);
                    resp.getWriter().write(JsonUtil.toJson(chart));
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

    private void handleReportView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int filterYear = parseInt(req.getParameter("nam"), currentYear);
        int filterYearTop5 = parseInt(req.getParameter("namTop5"), filterYear);
        int thangTop5 = parseInt(req.getParameter("thangTop5"), 7);
        int filterYearDaBan = parseInt(req.getParameter("namDaBan"), filterYear);
        int thangDaBan = parseInt(req.getParameter("thangDaBan"), 7);

        if ("excel".equalsIgnoreCase(req.getParameter("export"))) {
            resp.setContentType("text/csv;charset=UTF-8");
            resp.setHeader("Content-Disposition", "attachment; filename=\"BaoCaoDoanhThu_DUNA_" + filterYear + ".csv\"");
            StringBuilder csv = new StringBuilder();
            csv.append("Năm,Tháng,Số đơn hàng,Doanh thu thuần,Giá vốn hàng bán,Lợi nhuận gộp\n");
            connectService db = new connectService();
            try (Connection conn = db.myConnection()) {
                if (conn != null) {
                    String sql = "SELECT MONTH(dh.NgayTao) AS Thang, COUNT(DISTINCT dh.ID_DonHang) AS SoDon, " +
                                 "SUM(dh.TongTien) AS DoanhThu, SUM(ct.SoLuong * ISNULL(ct.GiaNhapLucBan, 0)) AS GiaVon " +
                                 "FROM DonHang dh LEFT JOIN ChiTietDonHang ct ON dh.ID_DonHang = ct.ID_DonHang " +
                                 "WHERE YEAR(dh.NgayTao) = ? AND dh.TrangThaiDH_ID = 1 GROUP BY MONTH(dh.NgayTao) ORDER BY Thang";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, filterYear);
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                int m = rs.getInt("Thang");
                                int count = rs.getInt("SoDon");
                                long dt = rs.getLong("DoanhThu");
                                long gv = rs.getLong("GiaVon");
                                csv.append(filterYear).append(",").append(m).append(",").append(count).append(",")
                                   .append(dt).append(",").append(gv).append(",").append(dt - gv).append("\n");
                            }
                        }
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
            resp.getOutputStream().write(csv.toString().getBytes("UTF-8"));
            return;
        }

        // 1. KPI Hôm nay
        Map<String, Long> statsToday = uc46Repo.getDoanhThuHomNay();
        long revToday = statsToday != null && statsToday.containsKey("doanhThu") ? statsToday.get("doanhThu") : 0L;
        long profitToday = statsToday != null && statsToday.containsKey("loiNhuanGop") ? statsToday.get("loiNhuanGop") : 0L;

        // 2. Doanh thu, vốn, số lượng bán
        long revYear = 0L, cogsYear = 0L, soldYear = 0L;
        long revAllTime = 0L, soldAllTime = 0L;
        long totalInventory = 0L;

        // 3. Mảng 12 tháng Chart.js
        long[] mRevenue = new long[12];
        long[] mProfit = new long[12];
        long[] mSold = new long[12];

        List<Map<String, Object>> topProducts = new ArrayList<>();
        List<Map<String, Object>> categoryStock = new ArrayList<>();
        List<Map<String, Object>> soldProducts = new ArrayList<>();

        connectService db = new connectService();
        try (Connection conn = db.myConnection()) {
            if (conn != null) {
                // Doanh thu theo tháng trong năm filterYear
                String sqlYear = "SELECT MONTH(dh.NgayTao) AS Thang, SUM(dh.TongTien) AS DoanhThu, " +
                                 "SUM(ct.SoLuong * ISNULL(ct.GiaNhapLucBan, 0)) AS GiaVon, SUM(ct.SoLuong) AS SoLuongBan " +
                                 "FROM DonHang dh LEFT JOIN ChiTietDonHang ct ON dh.ID_DonHang = ct.ID_DonHang " +
                                 "WHERE YEAR(dh.NgayTao) = ? AND dh.TrangThaiDH_ID = 1 " +
                                 "GROUP BY MONTH(dh.NgayTao)";
                try (PreparedStatement ps = conn.prepareStatement(sqlYear)) {
                    ps.setInt(1, filterYear);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int month = rs.getInt("Thang");
                            long dt = rs.getLong("DoanhThu");
                            long gv = rs.getLong("GiaVon");
                            long sl = rs.getLong("SoLuongBan");
                            revYear += dt;
                            cogsYear += gv;
                            soldYear += sl;
                            if (month >= 1 && month <= 12) {
                                mRevenue[month - 1] = dt;
                                mProfit[month - 1] = (dt - gv);
                                mSold[month - 1] = sl;
                            }
                        }
                    }
                }

                // Toàn thời gian
                String sqlAll = "SELECT SUM(dh.TongTien) AS DoanhThu, SUM(ct.SoLuong) AS SoLuongBan " +
                                "FROM DonHang dh LEFT JOIN ChiTietDonHang ct ON dh.ID_DonHang = ct.ID_DonHang " +
                                "WHERE dh.TrangThaiDH_ID = 1";
                try (PreparedStatement ps = conn.prepareStatement(sqlAll);
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        revAllTime = rs.getLong("DoanhThu");
                        soldAllTime = rs.getLong("SoLuongBan");
                    }
                }

                // Tồn kho hiện tại
                String sqlInv = "SELECT ISNULL(SUM(SoLuong), 0) FROM BienTheSanPham";
                try (PreparedStatement ps = conn.prepareStatement(sqlInv);
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) totalInventory = rs.getLong(1);
                }

                // Top 10 sản phẩm bán chạy theo kỳ
                String sqlTop = "SELECT TOP 10 sp.TenSP, SUM(ct.SoLuong) AS DaBan, SUM(ct.ThanhTien) AS DoanhThu " +
                                "FROM ChiTietDonHang ct " +
                                "INNER JOIN SanPham sp ON ct.ID_SanPham = sp.ID_SanPham " +
                                "INNER JOIN DonHang dh ON ct.ID_DonHang = dh.ID_DonHang " +
                                "WHERE MONTH(dh.NgayTao) = ? AND YEAR(dh.NgayTao) = ? AND dh.TrangThaiDH_ID = 1 " +
                                "GROUP BY sp.ID_SanPham, sp.TenSP ORDER BY DaBan DESC";
                try (PreparedStatement ps = conn.prepareStatement(sqlTop)) {
                    ps.setInt(1, thangTop5);
                    ps.setInt(2, filterYearTop5);
                    try (ResultSet rs = ps.executeQuery()) {
                        int stt = 1;
                        while (rs.next()) {
                            Map<String, Object> p = new HashMap<>();
                            p.put("stt", stt++);
                            p.put("tenSP", rs.getString("TenSP"));
                            p.put("thang", thangTop5);
                            p.put("nam", filterYearTop5);
                            p.put("daBan", rs.getInt("DaBan"));
                            p.put("doanhThu", rs.getLong("DoanhThu"));
                            topProducts.add(p);
                        }
                    }
                }

                // Tồn kho theo danh mục
                String sqlCat = "SELECT dm.TenDanhMuc, ISNULL(SUM(bt.SoLuong), 0) AS SoLuongTon " +
                                "FROM DanhMuc dm " +
                                "LEFT JOIN SanPham sp ON dm.ID_DanhMuc = sp.ID_DanhMuc " +
                                "LEFT JOIN BienTheSanPham bt ON sp.ID_SanPham = bt.ID_SanPham " +
                                "GROUP BY dm.ID_DanhMuc, dm.TenDanhMuc ORDER BY SoLuongTon DESC";
                try (PreparedStatement ps = conn.prepareStatement(sqlCat);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> c = new HashMap<>();
                        c.put("danhMuc", rs.getString("TenDanhMuc"));
                        c.put("soLuongTon", rs.getInt("SoLuongTon"));
                        categoryStock.add(c);
                    }
                }

                // Sổ kê sản phẩm đã bán theo kỳ
                String sqlSold = "SELECT ct.ID_ChiTiet, sp.MaSP, ct.TenSP, sp.ChatLieu, dh.NgayTao, ct.SoLuong, ct.ThanhTien " +
                                 "FROM ChiTietDonHang ct " +
                                 "INNER JOIN SanPham sp ON ct.ID_SanPham = sp.ID_SanPham " +
                                 "INNER JOIN DonHang dh ON ct.ID_DonHang = dh.ID_DonHang " +
                                 "WHERE MONTH(dh.NgayTao) = ? AND YEAR(dh.NgayTao) = ? AND dh.TrangThaiDH_ID = 1 " +
                                 "ORDER BY ct.ID_ChiTiet DESC";
                try (PreparedStatement ps = conn.prepareStatement(sqlSold)) {
                    ps.setInt(1, thangDaBan);
                    ps.setInt(2, filterYearDaBan);
                    try (ResultSet rs = ps.executeQuery()) {
                        int stt = 1;
                        while (rs.next()) {
                            Map<String, Object> s = new HashMap<>();
                            s.put("stt", stt++);
                            s.put("maSP", rs.getString("MaSP") != null ? rs.getString("MaSP") : "SP" + rs.getInt("ID_ChiTiet"));
                            s.put("tenSP", rs.getString("TenSP"));
                            s.put("phanLoai", rs.getString("ChatLieu") != null ? rs.getString("ChatLieu") : "Thời trang nam DUNA");
                            s.put("thoiGian", rs.getString("NgayTao"));
                            s.put("soLuong", rs.getInt("SoLuong"));
                            s.put("thanhTien", rs.getLong("ThanhTien"));
                            soldProducts.add(s);
                        }
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        long profitYear = revYear - cogsYear;
        int profitMarginYear = revYear > 0 ? (int) Math.round((profitYear * 100.0) / revYear) : 0;

        req.setAttribute("filterYear", filterYear);
        req.setAttribute("filterYearTop5", filterYearTop5);
        req.setAttribute("thangTop5", thangTop5);
        req.setAttribute("filterYearDaBan", filterYearDaBan);
        req.setAttribute("thangDaBan", thangDaBan);

        req.setAttribute("stat_totalRevenueToday", revToday);
        req.setAttribute("stat_grossProfitToday", profitToday);
        req.setAttribute("stat_totalRevenueYear", revYear);
        req.setAttribute("stat_grossProfitYear", profitYear);
        req.setAttribute("stat_profitMarginYear", profitMarginYear);
        req.setAttribute("stat_totalRevenueAllTime", revAllTime);
        req.setAttribute("stat_totalSoldYear", soldYear);
        req.setAttribute("stat_totalSold", soldAllTime);
        req.setAttribute("stat_totalInventory", totalInventory);

        req.setAttribute("stat_monthlyRevenue", Arrays.toString(mRevenue));
        req.setAttribute("stat_monthlyProfit", Arrays.toString(mProfit));
        req.setAttribute("stat_monthlySold", Arrays.toString(mSold));

        req.setAttribute("topProducts", topProducts);
        req.setAttribute("categoryStock", categoryStock);
        req.setAttribute("soldProducts", soldProducts);

        req.getRequestDispatcher("/bao-cao-thong-ke.jsp").forward(req, resp);
    }

    private void handleOrderView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = req.getParameter("search");
        String trangThai = req.getParameter("trangThai");
        String payment = req.getParameter("paymentMethod");
        String fromDate = req.getParameter("fromDate");
        String toDate = req.getParameter("toDate");

        List<DonHang> allOrders = uc42Repo.getDonHangTheoNgay(fromDate, toDate);
        List<DonHang> filtered = new ArrayList<>();

        int totalOrders = 0;
        int completedOrders = 0;
        int cancelledOrders = 0;

        if (allOrders != null) {
            for (DonHang d : allOrders) {
                String tt = d.getTenTrangThai() != null ? d.getTenTrangThai() : "";
                totalOrders++;
                if ("Đã thanh toán".equalsIgnoreCase(tt) || "Hoàn thành".equalsIgnoreCase(tt)) {
                    completedOrders++;
                } else if ("Đã hủy".equalsIgnoreCase(tt)) {
                    cancelledOrders++;
                }

                boolean match = true;
                if (search != null && !search.trim().isEmpty()) {
                    String kw = search.trim().toLowerCase();
                    String ma = d.getMaDonHang() != null ? d.getMaDonHang().toLowerCase() : "";
                    String kh = d.getTenKhachHang() != null ? d.getTenKhachHang().toLowerCase() : "";
                    if (!ma.contains(kw) && !kh.contains(kw)) {
                        match = false;
                    }
                }
                if (match && trangThai != null && !trangThai.trim().isEmpty() && !"ALL".equalsIgnoreCase(trangThai)) {
                    if (!trangThai.equalsIgnoreCase(tt)) {
                        match = false;
                    }
                }
                if (match && payment != null && !payment.trim().isEmpty() && !"ALL".equalsIgnoreCase(payment)) {
                    String pt = d.getPhuongThucTT() != null ? d.getPhuongThucTT() : "";
                    if (!payment.equalsIgnoreCase(pt)) {
                        match = false;
                    }
                }
                if (match) {
                    filtered.add(d);
                }
            }
        }

        req.setAttribute("listDH", filtered);
        req.setAttribute("stat_totalOrders", totalOrders);
        req.setAttribute("stat_completedOrders", completedOrders);
        req.setAttribute("stat_cancelledOrders", cancelledOrders);
        req.setAttribute("currentVat", 8);
        req.setAttribute("currentPage", 1);
        req.setAttribute("totalPages", 1);
        req.setAttribute("p_search", search != null ? search : "");
        req.setAttribute("p_trangThai", trangThai != null ? trangThai : "");
        req.setAttribute("p_paymentMethod", payment != null ? payment : "");
        req.setAttribute("p_fromDate", fromDate != null ? fromDate : "");
        req.setAttribute("p_toDate", toDate != null ? toDate : "");

        req.getRequestDispatcher("/quan-ly-hoa-don.jsp").forward(req, resp);
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

    private double parseDouble(String val, double defaultVal) {
        try {
            return (val != null && !val.trim().isEmpty()) ? Double.parseDouble(val.trim()) : defaultVal;
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
