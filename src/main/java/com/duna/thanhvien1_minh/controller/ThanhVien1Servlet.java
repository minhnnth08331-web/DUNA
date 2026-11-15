package com.duna.thanhvien1_minh.controller;

import com.duna.thanhvien1_minh.repository.*;
import com.duna.common.connection.connectService;
import com.duna.common.entities.BienTheSanPham;
import com.duna.common.entities.DanhMuc;
import com.duna.common.entities.SanPham;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * THÀNH VIÊN 1: NGUYỄN NGỌC MINH - MSSV: TH08331
 * Phân hệ: Quản trị Sản phẩm, Biến thể & Danh mục Thời trang Nam DUNA
 * Điều phối chính xác 16 Repository riêng biệt (UC01 -> UC16).
 * Đảm bảo 100% usages và tương thích giao diện JSP Quản lý sản phẩm.
 */
@WebServlet(name = "ThanhVien1Servlet", urlPatterns = {"/ThanhVien1Servlet", "/thanhvien1", "/ProductServlet"})
public class ThanhVien1Servlet extends HttpServlet {
    private final UC01_QuanLyDanhMucRepo uc01Repo = new UC01_QuanLyDanhMucRepo();
    private final UC02_QuanLyChatLieuRepo uc02Repo = new UC02_QuanLyChatLieuRepo();
    private final UC03_QuanLyFormDangRepo uc03Repo = new UC03_QuanLyFormDangRepo();
    private final UC04_KiemTraKhoaNgoaiDanhMucRepo uc04Repo = new UC04_KiemTraKhoaNgoaiDanhMucRepo();
    private final UC05_DanhSachSanPhamRepo uc05Repo = new UC05_DanhSachSanPhamRepo();
    private final UC06_TimKiemSanPhamNangCaoRepo uc06Repo = new UC06_TimKiemSanPhamNangCaoRepo();
    private final UC07_ThemMoiSanPhamRepo uc07Repo = new UC07_ThemMoiSanPhamRepo();
    private final UC08_CapNhatSanPhamRepo uc08Repo = new UC08_CapNhatSanPhamRepo();
    private final UC09_QuanLyGiaVonCOGSRepo uc09Repo = new UC09_QuanLyGiaVonCOGSRepo();
    private final UC10_CauHinhGiaBanVATRepo uc10Repo = new UC10_CauHinhGiaBanVATRepo();
    private final UC11_TrangThaiKinhDoanhRepo uc11Repo = new UC11_TrangThaiKinhDoanhRepo();
    private final UC12_DanhSachBienTheRepo uc12Repo = new UC12_DanhSachBienTheRepo();
    private final UC13_ThemBienTheUniqueRepo uc13Repo = new UC13_ThemBienTheUniqueRepo();
    private final UC14_CapNhatGiaTonBienTheRepo uc14Repo = new UC14_CapNhatGiaTonBienTheRepo();
    private final UC15_XoaBienTheDongBoKhoRepo uc15Repo = new UC15_XoaBienTheDongBoKhoRepo();
    private final UC16_TuVanSizeThongMinhRepo uc16Repo = new UC16_TuVanSizeThongMinhRepo();

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

        // Hỗ trợ AJAX lấy chi tiết biến thể và thông số kỹ thuật sản phẩm (Modal Chi tiết mặt hàng POS)
        if ("1".equals(req.getParameter("ajaxDetailAll"))) {
            resp.setContentType("application/json;charset=UTF-8");
            int idSP = parseInt(req.getParameter("idSP"), 0);
            List<BienTheSanPham> variants = uc12Repo.getVariantsByProductId(idSP);

            List<Map<String, Object>> specs = new ArrayList<>();
            connectService db = new connectService();
            try (Connection conn = db.myConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT * FROM SanPham WHERE ID_SanPham = ?")) {
                ps.setInt(1, idSP);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String chatLieu = rs.getString("ChatLieu");
                        String formDang = rs.getString("FormDang");
                        String thuongHieu = rs.getString("ThuongHieu");
                        String moTa = rs.getString("MoTa");
                        Map<String, Object> s1 = new HashMap<>(); s1.put("ten", "Chất liệu vải"); s1.put("giaTri", chatLieu != null ? chatLieu : "Cotton cao cấp DUNA"); specs.add(s1);
                        Map<String, Object> s2 = new HashMap<>(); s2.put("ten", "Form dáng"); s2.put("giaTri", formDang != null ? formDang : "Chuẩn Regular Fit"); specs.add(s2);
                        Map<String, Object> s3 = new HashMap<>(); s3.put("ten", "Thương hiệu"); s3.put("giaTri", thuongHieu != null ? thuongHieu : "DUNA Men's Wear (Việt Nam)"); specs.add(s3);
                        Map<String, Object> s4 = new HashMap<>(); s4.put("ten", "Hướng dẫn giặt ủi"); s4.put("giaTri", moTa != null && !moTa.trim().isEmpty() ? moTa : "Giặt máy chế độ nhẹ hoặc giặt tay, ủi nhiệt độ thấp dưới 150°C"); specs.add(s4);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Map<String, Object> res = new HashMap<>();
            res.put("variants", variants);
            res.put("specs", specs);
            resp.getWriter().write(JsonUtil.toJson(res));
            return;
        }

        String action = req.getParameter("action");

        // Hỗ trợ hiển thị trực tiếp giao diện JSP Quản lý sản phẩm nếu không có action
        if (action == null || action.trim().isEmpty()) {
            String search = req.getParameter("search");
            String cat = req.getParameter("cat");
            String status = req.getParameter("status");
            List<SanPham> list = uc05Repo.getDanhSachSanPham(search, cat, status);
            req.setAttribute("listSPEntities", list);
            req.setAttribute("listSP", list);
            req.getRequestDispatcher("/quan-ly-san-pham.jsp").forward(req, resp);
            return;
        }

        resp.setContentType("application/json;charset=UTF-8");
        action = action.trim().toLowerCase();

        try {
            switch (action) {
                // UC 01: Quản lý danh mục (Xem, Thêm, Sửa, Xóa)
                case "uc01": {
                    List<DanhMuc> list = uc01Repo.getDanhSachDanhMuc();
                    resp.getWriter().write(JsonUtil.toJson(list));
                    break;
                }
                case "uc01_add": {
                    DanhMuc dm = new DanhMuc();
                    dm.setTenDanhMuc(req.getParameter("tenDanhMuc"));
                    dm.setMoTa(req.getParameter("moTa"));
                    boolean ok = uc01Repo.themDanhMuc(dm);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }
                case "uc01_update": {
                    DanhMuc dm = new DanhMuc();
                    dm.setIdDanhMuc(parseInt(req.getParameter("idDanhMuc"), 0));
                    dm.setTenDanhMuc(req.getParameter("tenDanhMuc"));
                    dm.setMoTa(req.getParameter("moTa"));
                    boolean ok = uc01Repo.capNhatDanhMuc(dm);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }
                case "uc01_delete": {
                    int idDanhMuc = parseInt(req.getParameter("idDanhMuc"), 0);
                    boolean ok = uc01Repo.xoaDanhMuc(idDanhMuc);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 02: Quản lý thuộc tính chất liệu (Xem, Thêm, Xóa)
                case "uc02": {
                    List<String> list = uc02Repo.getChatLieuList();
                    resp.getWriter().write(JsonUtil.toJson(list));
                    break;
                }
                case "uc02_add": {
                    String chatLieu = req.getParameter("chatLieu");
                    boolean ok = uc02Repo.themChatLieu(chatLieu);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }
                case "uc02_delete": {
                    String chatLieu = req.getParameter("chatLieu");
                    boolean ok = uc02Repo.xoaChatLieu(chatLieu);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 03: Quản lý thuộc tính form dáng (Xem, Thêm, Xóa)
                case "uc03": {
                    List<String> list = uc03Repo.getFormDangList();
                    resp.getWriter().write(JsonUtil.toJson(list));
                    break;
                }
                case "uc03_add": {
                    String formDang = req.getParameter("formDang");
                    boolean ok = uc03Repo.themFormDang(formDang);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }
                case "uc03_delete": {
                    String formDang = req.getParameter("formDang");
                    boolean ok = uc03Repo.xoaFormDang(formDang);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 04: Ràng buộc toàn vẹn khóa ngoại danh mục
                case "uc04": {
                    int idDanhMuc = parseInt(req.getParameter("idDanhMuc"), 0);
                    boolean isBound = uc04Repo.checkDanhMucForeignKey(idDanhMuc);
                    Map<String, Object> map = new HashMap<>();
                    map.put("idDanhMuc", idDanhMuc);
                    map.put("hasConstraint", isBound);
                    resp.getWriter().write(JsonUtil.toJson(map));
                    break;
                }

                // UC 05: Danh sách sản phẩm & bộ lọc
                case "uc05": {
                    String keyword = req.getParameter("keyword");
                    String cat = req.getParameter("cat");
                    String status = req.getParameter("status");
                    List<SanPham> list = uc05Repo.getDanhSachSanPham(keyword, cat, status);
                    resp.getWriter().write(JsonUtil.toJson(list));
                    break;
                }

                // UC 06: Tìm kiếm nâng cao
                case "uc06": {
                    String maSP = req.getParameter("maSP");
                    String ten = req.getParameter("ten");
                    String chatLieu = req.getParameter("chatLieu");
                    String formDang = req.getParameter("formDang");
                    List<SanPham> list = uc06Repo.searchAdvanced(maSP, ten, chatLieu, formDang);
                    resp.getWriter().write(JsonUtil.toJson(list));
                    break;
                }

                // UC 07: Thêm mới sản phẩm
                case "uc07": {
                    SanPham sp = new SanPham();
                    sp.setMaSP(req.getParameter("maSP"));
                    sp.setTenSP(req.getParameter("tenSP"));
                    sp.setIdDanhMuc(parseInt(req.getParameter("idDanhMuc"), 1));
                    sp.setGiaBan(parseLong(req.getParameter("giaBan"), 0));
                    sp.setGiaNhap(parseLong(req.getParameter("giaNhap"), 0));
                    sp.setSoLuong(parseInt(req.getParameter("soLuong"), 0));
                    sp.setChatLieu(req.getParameter("chatLieu"));
                    sp.setFormDang(req.getParameter("formDang"));
                    sp.setHinhAnh(req.getParameter("hinhAnh"));
                    sp.setMoTa(req.getParameter("moTa"));
                    boolean success = uc07Repo.themSanPham(sp);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", success);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 08: Cập nhật sản phẩm
                case "uc08": {
                    SanPham sp = new SanPham();
                    sp.setId(parseInt(req.getParameter("id"), 0));
                    sp.setTenSP(req.getParameter("tenSP"));
                    sp.setIdDanhMuc(parseInt(req.getParameter("idDanhMuc"), 1));
                    sp.setGiaBan(parseLong(req.getParameter("giaBan"), 0));
                    sp.setGiaNhap(parseLong(req.getParameter("giaNhap"), 0));
                    sp.setChatLieu(req.getParameter("chatLieu"));
                    sp.setFormDang(req.getParameter("formDang"));
                    sp.setHinhAnh(req.getParameter("hinhAnh"));
                    sp.setMoTa(req.getParameter("moTa"));
                    boolean success = uc08Repo.capNhatSanPham(sp);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", success);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 09: Quản lý giá vốn nhập hàng & biên lợi nhuận
                case "uc09": {
                    int idSP = parseInt(req.getParameter("id"), 1);
                    Map<String, Object> margin = uc09Repo.tinhLoiNhuanGop(idSP);
                    resp.getWriter().write(JsonUtil.toJson(margin));
                    break;
                }
                case "uc09_update": {
                    int idSP = parseInt(req.getParameter("id"), 0);
                    long giaNhapMoi = parseLong(req.getParameter("giaNhap"), 0);
                    boolean ok = uc09Repo.capNhatGiaNhap(idSP, giaNhapMoi);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 10: Cấu hình VAT 8%
                case "uc10": {
                    long giaChuaThue = parseLong(req.getParameter("giaBan"), 500000L);
                    Map<String, Object> vatMap = uc10Repo.cauHinhGiaNiemYetVAT(giaChuaThue);
                    resp.getWriter().write(JsonUtil.toJson(vatMap));
                    break;
                }

                // UC 11: Quản lý trạng thái kinh doanh
                case "uc11": {
                    int idSP = parseInt(req.getParameter("id"), 0);
                    boolean ok = uc11Repo.toggleTrangThaiKinhDoanh(idSP);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }
                case "uc11_update": {
                    int idSP = parseInt(req.getParameter("id"), 0);
                    String trangThai = req.getParameter("trangThai");
                    boolean ok = uc11Repo.capNhatTrangThai(idSP, trangThai);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 12: Chi tiết biến thể
                case "uc12": {
                    int idSP = parseInt(req.getParameter("id"), 0);
                    List<BienTheSanPham> variants = uc12Repo.getVariantsByProductId(idSP);
                    resp.getWriter().write(JsonUtil.toJson(variants));
                    break;
                }

                // UC 13: Thêm biến thể duy nhất
                case "uc13": {
                    BienTheSanPham bt = new BienTheSanPham();
                    bt.setIdSanPham(parseInt(req.getParameter("idSanPham"), 0));
                    bt.setMaBienThe(req.getParameter("maBienThe"));
                    bt.setMauSac(req.getParameter("mauSac"));
                    bt.setTrongLuong(req.getParameter("kichThuoc"));
                    bt.setGiaNhap(parseLong(req.getParameter("giaNhap"), 0));
                    bt.setGiaBan(parseLong(req.getParameter("giaBan"), 0));
                    bt.setSoLuong(parseInt(req.getParameter("soLuong"), 0));
                    bt.setBarcode(req.getParameter("barcode"));
                    boolean ok = uc13Repo.themBienTheUnique(bt);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 14: Cập nhật giá & tồn kho biến thể
                case "uc14": {
                    int idBT = parseInt(req.getParameter("idBienThe"), 0);
                    long giaBanMoi = parseLong(req.getParameter("giaBan"), 0);
                    int soLuongMoi = parseInt(req.getParameter("soLuong"), 0);
                    boolean ok = uc14Repo.capNhatGiaVaTonKhoBienThe(idBT, giaBanMoi, soLuongMoi);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 15: Xóa biến thể & đồng bộ kho
                case "uc15": {
                    int idBT = parseInt(req.getParameter("idBienThe"), 0);
                    int idSP = parseInt(req.getParameter("idSanPham"), 0);
                    boolean ok = uc15Repo.xoaBienTheVaDongBoKho(idBT, idSP);
                    Map<String, Object> res = new HashMap<>();
                    res.put("success", ok);
                    resp.getWriter().write(JsonUtil.toJson(res));
                    break;
                }

                // UC 16: Tư vấn size thông minh
                case "uc16": {
                    int height = parseInt(req.getParameter("height"), 170);
                    int weight = parseInt(req.getParameter("weight"), 65);
                    String size = uc16Repo.tuVanSize(height, weight);
                    Map<String, Object> res = new HashMap<>();
                    res.put("height", height);
                    res.put("weight", weight);
                    res.put("recommendedSize", size);
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
