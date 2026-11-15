package com.duna.thanhvien2_ducanh.controller;

import com.duna.thanhvien2_ducanh.repository.*;
import com.duna.common.entities.BienTheSanPham;
import com.duna.common.entities.NhanVien;
import com.duna.common.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * THÀNH VIÊN 2: NGUYỄN ĐỨC ANH - MSSV: TH08308
 * Phân hệ: Bảo mật, Xác thực & Bán hàng POS Quầy Giai đoạn 1 (Barcode, Giỏ hàng)
 * Điều phối chính xác 16 Repository riêng biệt (UC17 -> UC32).
 * Đảm bảo 100% usages và hỗ trợ giao diện Login/POS.
 */
@WebServlet(name = "ThanhVien2Servlet", urlPatterns = {"/ThanhVien2Servlet", "/thanhvien2", "/LoginServlet", "/LogoutServlet", "/ChangeCredentialsServlet"})
public class ThanhVien2Servlet extends HttpServlet {
    private final UC17_DangNhapXacThucRepo uc17Repo = new UC17_DangNhapXacThucRepo();
    private final UC18_GhiNhoCookieRepo uc18Repo = new UC18_GhiNhoCookieRepo();
    private final UC19_PhanQuyenRBACRepo uc19Repo = new UC19_PhanQuyenRBACRepo();
    private final UC20_DoiMatKhauRepo uc20Repo = new UC20_DoiMatKhauRepo();
    private final UC21_SinhMaBarcodeRepo uc21Repo = new UC21_SinhMaBarcodeRepo();
    private final UC22_InTemBarcodeRepo uc22Repo = new UC22_InTemBarcodeRepo();
    private final UC23_BanHangPOS1ChamRepo uc23Repo = new UC23_BanHangPOS1ChamRepo();
    private final UC24_TimKiemAutoCompleteRepo uc24Repo = new UC24_TimKiemAutoCompleteRepo();
    private final UC25_QuetBarcodeUSBRepo uc25Repo = new UC25_QuetBarcodeUSBRepo();
    private final UC26_QuetBarcodeCameraRepo uc26Repo = new UC26_QuetBarcodeCameraRepo();
    private final UC27_QuanLyGioHangPOSRepo uc27Repo = new UC27_QuanLyGioHangPOSRepo();
    private final UC28_CanhBaoTonKhoRepo uc28Repo = new UC28_CanhBaoTonKhoRepo();
    private final UC29_QuanLyDonHangChoRepo uc29Repo = new UC29_QuanLyDonHangChoRepo();
    private final UC30_TiepNhanDoiSizeMauRepo uc30Repo = new UC30_TiepNhanDoiSizeMauRepo();
    private final UC31_TinhBuTruTienDoiHangRepo uc31Repo = new UC31_TinhBuTruTienDoiHangRepo();
    private final UC32_CapNhatKhoDoiHangRepo uc32Repo = new UC32_CapNhatKhoDoiHangRepo();

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

        // Xử lý Logout trực tiếp từ Navbar/UI
        if ("/LogoutServlet".equalsIgnoreCase(servletPath)) {
            HttpSession session = req.getSession(false);
            if (session != null) session.invalidate();
            Cookie c = new Cookie("yn_user", "");
            c.setMaxAge(0);
            c.setPath("/");
            resp.addCookie(c);
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // Kiểm tra xem yêu cầu có phải là gọi API JSON hay là giao diện trình duyệt HTML thông thường
        boolean isJsonApi = "application/json".equalsIgnoreCase(req.getHeader("Content-Type"))
                || "XMLHttpRequest".equalsIgnoreCase(req.getHeader("X-Requested-With"))
                || "json".equalsIgnoreCase(req.getParameter("format"))
                || "1".equalsIgnoreCase(req.getParameter("ajax"));

        // Hỗ trợ hiển thị giao diện Login JSP nếu truy cập /LoginServlet từ trình duyệt (HTML)
        if ("/LoginServlet".equalsIgnoreCase(servletPath) && !isJsonApi) {
            if ("GET".equalsIgnoreCase(req.getMethod())) {
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            } else { // Form POST từ login.jsp
                String u = req.getParameter("username");
                String p = req.getParameter("password");
                NhanVien nv = uc17Repo.authenticate(u, p);
                if (nv != null) {
                    HttpSession session = req.getSession(true);
                    String roleStr = (nv.getRoleId() == 1 || "Quản lý".equalsIgnoreCase(nv.getTenTrangThai()) || "MANAGER".equalsIgnoreCase(nv.getTenTrangThai())) ? "MANAGER" : "EMPLOYEE";
                    session.setAttribute("role", roleStr);
                    session.setAttribute("roleID", nv.getRoleId());
                    session.setAttribute("roleId", nv.getRoleId());
                    session.setAttribute("nhanVienID", nv.getId());
                    session.setAttribute("idNhanVien", nv.getId());
                    session.setAttribute("tenNV", nv.getHoVaTen());
                    session.setAttribute("username", nv.getUsername() != null ? nv.getUsername() : u);

                    String rememberMe = req.getParameter("rememberMe");
                    if (rememberMe == null) rememberMe = req.getParameter("remember");
                    boolean isRemember = "true".equalsIgnoreCase(rememberMe) || "on".equalsIgnoreCase(rememberMe);

                    Cookie cUser = new Cookie("yn_user", isRemember ? (nv.getUsername() != null ? nv.getUsername() : u) : "");
                    cUser.setMaxAge(isRemember ? 30 * 24 * 60 * 60 : 0);
                    cUser.setPath("/");
                    cUser.setHttpOnly(true);
                    resp.addCookie(cUser);

                    Cookie cPassClear = new Cookie("yn_pass", "");
                    cPassClear.setMaxAge(0);
                    cPassClear.setPath("/");
                    resp.addCookie(cPassClear);

                    if ("MANAGER".equals(roleStr)) {
                        resp.sendRedirect(req.getContextPath() + "/EmployeeManagementServlet");
                    } else {
                        resp.sendRedirect(req.getContextPath() + "/OrderServlet?view=sell");
                    }
                    return;
                } else {
                    req.setAttribute("errorMessage", "Sai tài khoản, mật khẩu hoặc tài khoản bị khóa!");
                    req.getRequestDispatcher("/login.jsp").forward(req, resp);
                    return;
                }
            }
        }

        resp.setContentType("application/json;charset=UTF-8");
        String action = req.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            if ("/LoginServlet".equalsIgnoreCase(servletPath)) {
                action = "uc17";
            } else if ("/ChangeCredentialsServlet".equalsIgnoreCase(servletPath)) {
                action = "uc20";
            } else {
                String accept = req.getHeader("Accept");
                if (accept != null && accept.contains("text/html")) {
                    resp.sendRedirect(req.getContextPath() + "/OrderServlet?view=sell");
                    return;
                }
                action = "uc23"; // Default: Cấu hình POS cho API / Unit Test
            }
        }
        action = action.trim().toLowerCase();

        try {
            switch (action) {
                // UC 17: Xác thực đăng nhập
                case "uc17": {
                    String u = req.getParameter("username");
                    String p = req.getParameter("password");
                    NhanVien nv = uc17Repo.authenticate(u, p);
                    Map<String, Object> res = new HashMap<>();
                    if (nv != null) {
                        HttpSession s = req.getSession(true);
                        String roleStr = (nv.getRoleId() == 1 || "Quản lý".equalsIgnoreCase(nv.getTenTrangThai()) || "MANAGER".equalsIgnoreCase(nv.getTenTrangThai())) ? "MANAGER" : "EMPLOYEE";
                        s.setAttribute("role", roleStr);
                        s.setAttribute("roleID", nv.getRoleId());
                        s.setAttribute("roleId", nv.getRoleId());
                        s.setAttribute("nhanVienID", nv.getId());
                        s.setAttribute("idNhanVien", nv.getId());
                        s.setAttribute("tenNV", nv.getHoVaTen());
                        s.setAttribute("username", nv.getUsername() != null ? nv.getUsername() : u);
                        res.put("success", true);
                        res.put("user", nv);

                        // Hỗ trợ Remember Me an toàn (UC 18)
                        String remember = req.getParameter("rememberMe");
                        if (remember == null) remember = req.getParameter("remember");
                        if ("on".equalsIgnoreCase(remember) || "true".equalsIgnoreCase(remember)) {
                            Cookie c = new Cookie("yn_user", nv.getUsername() != null ? nv.getUsername() : u);
                            c.setHttpOnly(true);
                            c.setMaxAge(30 * 24 * 3600);
                            c.setPath("/");
                            resp.addCookie(c);
                        }
                    } else {
                        res.put("success", false);
                        res.put("message", "Sai tài khoản hoặc mật khẩu!");
                    }
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 18: Kiểm tra token Remember Me
                case "uc18": {
                    String u = req.getParameter("username");
                    boolean valid = uc18Repo.isValidRememberMeToken(u);
                    Map<String, Object> res = new HashMap<>();
                    res.put("valid", valid);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 19: Kiểm tra quyền RBAC
                case "uc19": {
                    int roleId = parseInt(req.getParameter("roleId"), 2);
                    String path = req.getParameter("path");
                    boolean allowed = uc19Repo.checkRoleAccess(roleId, path);
                    Map<String, Object> res = new HashMap<>();
                    res.put("allowed", allowed);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 20: Đổi mật khẩu
                case "uc20": {
                    String u = req.getParameter("username");
                    String oldP = req.getParameter("oldPass");
                    String newP = req.getParameter("newPass");
                    boolean ok = uc20Repo.doiMatKhau(u, oldP, newP);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 21: Tự sinh Barcode
                case "uc21": {
                    String maSP = req.getParameter("maSP");
                    String mau = req.getParameter("mauSac");
                    String size = req.getParameter("kichThuoc");
                    String barcode = uc21Repo.generateBarcode(maSP, mau, size);
                    Map<String, Object> res = new HashMap<>();
                    res.put("barcode", barcode);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 22: Thẻ bài & nhãn in tem Barcode
                case "uc22": {
                    String bc = req.getParameter("barcode");
                    String ten = req.getParameter("tenSP");
                    long gia = parseLong(req.getParameter("giaBan"), 0);
                    Map<String, String> label = uc22Repo.renderBarcodeLabel(bc, ten, gia);
                    resp.getWriter().write(JsonUtil.toJson(label));
                    break;
                }

                // UC 23: Cấu hình giao diện POS 2 cột
                case "uc23": {
                    Map<String, Object> config = uc23Repo.getPOSLayoutConfig();
                    resp.getWriter().write(JsonUtil.toJson(config));
                    break;
                }

                // UC 24: Tìm kiếm auto-complete tại quầy
                case "uc24": {
                    String kw = req.getParameter("keyword");
                    List<Map<String, Object>> list = uc24Repo.searchAutoComplete(kw);
                    resp.getWriter().write(JsonUtil.toJson(list));
                    break;
                }

                // UC 25: Quét bằng súng Barcode USB
                case "uc25": {
                    String bc = req.getParameter("barcode");
                    BienTheSanPham bt = uc25Repo.findByBarcodeUSB(bc);
                    resp.getWriter().write(JsonUtil.toJson(bt));
                    break;
                }

                // UC 26: Quét Camera WebRTC
                case "uc26": {
                    String bc = req.getParameter("barcode");
                    BienTheSanPham bt = uc26Repo.handleWebRTCScan(bc);
                    resp.getWriter().write(JsonUtil.toJson(bt));
                    break;
                }

                // UC 27: Quản lý giỏ hàng POS (Cập nhật, thêm, xóa, làm rỗng)
                case "uc27": {
                    int idBT = parseInt(req.getParameter("idBienThe"), 0);
                    int qty = parseInt(req.getParameter("soLuong"), 1);
                    List<Map<String, Object>> cart = getSessionCart(req);
                    cart = uc27Repo.capNhatGioHang(cart, idBT, qty);
                    req.getSession().setAttribute("pos_cart", cart);
                    resp.getWriter().write(JsonUtil.toJson(cart));
                    break;
                }
                case "uc27_add": {
                    int idBT = parseInt(req.getParameter("idBienThe"), 0);
                    int qty = parseInt(req.getParameter("soLuong"), 1);
                    String ten = req.getParameter("tenSP");
                    long donGia = parseLong(req.getParameter("donGia"), 0);
                    String bc = req.getParameter("barcode");
                    List<Map<String, Object>> cart = getSessionCart(req);
                    cart = uc27Repo.themVaoGioHang(cart, idBT, qty, ten, donGia, bc);
                    req.getSession().setAttribute("pos_cart", cart);
                    resp.getWriter().write(JsonUtil.toJson(cart));
                    break;
                }
                case "uc27_remove": {
                    int idBT = parseInt(req.getParameter("idBienThe"), 0);
                    List<Map<String, Object>> cart = getSessionCart(req);
                    cart = uc27Repo.xoaKhoiGioHang(cart, idBT);
                    req.getSession().setAttribute("pos_cart", cart);
                    resp.getWriter().write(JsonUtil.toJson(cart));
                    break;
                }
                case "uc27_clear": {
                    List<Map<String, Object>> cart = getSessionCart(req);
                    cart = uc27Repo.xoaToanBoGioHang(cart);
                    req.getSession().setAttribute("pos_cart", cart);
                    resp.getWriter().write(JsonUtil.toJson(cart));
                    break;
                }

                // UC 28: Kiểm tra tồn kho thời gian thực
                case "uc28": {
                    int idBT = parseInt(req.getParameter("idBienThe"), 0);
                    int qty = parseInt(req.getParameter("soLuong"), 1);
                    boolean available = uc28Repo.checkInventoryRealtime(idBT, qty);
                    Map<String, Object> res = new HashMap<>();
                    res.put("available", available);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 29: Quản lý nhiều đơn hàng chờ (Tạo, xem, xóa, danh sách)
                case "uc29": {
                    String tabId = req.getParameter("tabId");
                    List<Map<String, Object>> cart = getSessionCart(req);
                    Map<String, Object> tab = uc29Repo.createPendingTab(tabId, cart);
                    resp.getWriter().write(JsonUtil.toJson(tab));
                    break;
                }
                case "uc29_get": {
                    String tabId = req.getParameter("tabId");
                    List<Map<String, Object>> cart = uc29Repo.getPendingTab(tabId);
                    resp.getWriter().write(JsonUtil.toJson(cart));
                    break;
                }
                case "uc29_remove": {
                    String tabId = req.getParameter("tabId");
                    boolean ok = uc29Repo.removePendingTab(tabId);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }
                case "uc29_all": {
                    Map<String, List<Map<String, Object>>> tabs = uc29Repo.getAllPendingTabs();
                    resp.getWriter().write(JsonUtil.toJson(tabs));
                    break;
                }

                // UC 30: Mở modal đổi hàng
                case "uc30": {
                    int idCT = parseInt(req.getParameter("idChiTiet"), 0);
                    Map<String, Object> data = uc30Repo.openExchangeModal(idCT);
                    resp.getWriter().write(JsonUtil.toJson(data));
                    break;
                }

                // UC 31: Tính tiền bù trừ đổi hàng
                case "uc31": {
                    long giaCu = parseLong(req.getParameter("giaCu"), 0);
                    long giaMoi = parseLong(req.getParameter("giaMoi"), 0);
                    long buTru = uc31Repo.tinhTienBuTruDoiHang(giaCu, giaMoi);
                    Map<String, Object> res = new HashMap<>();
                    res.put("buTru", buTru);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 32: Cập nhật tồn kho khi đổi hàng
                case "uc32": {
                    int idCu = parseInt(req.getParameter("idBienTheCu"), 0);
                    int idMoi = parseInt(req.getParameter("idBienTheMoi"), 0);
                    int soLuong = parseInt(req.getParameter("soLuong"), 1);
                    boolean ok = uc32Repo.capNhatKhoKhiDoiHang(idCu, idMoi, soLuong);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
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

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getSessionCart(HttpServletRequest req) {
        HttpSession s = req.getSession(true);
        List<Map<String, Object>> cart = (List<Map<String, Object>>) s.getAttribute("pos_cart");
        if (cart == null) {
            cart = new ArrayList<>();
            s.setAttribute("pos_cart", cart);
        }
        return cart;
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
