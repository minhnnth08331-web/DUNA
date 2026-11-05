package Controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import connectService.connectService;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String view   = request.getParameter("view");
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String pay    = request.getParameter("pay");

        // AJAX: trả JSON chi tiết đơn hàng để in hóa đơn
        String ajaxDetail = request.getParameter("ajaxDetail");
        if ("1".equals(ajaxDetail)) {
            String idParam = request.getParameter("id");
            response.setContentType("application/json;charset=UTF-8");
            StringBuilder json = new StringBuilder("{");
            try (Connection conn = new connectService().myConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                    "SELECT dh.ID_DonHang, dh.MaDonHang, kh.HoTen, kh.SoDienThoai, kh.DiaChi, " +
                    "dh.TongTien, dh.PhuongThucThanhToan, ttdh.TenTrangThai AS TrangThai, dh.GhiChu, dh.TenNhanVien, FORMAT(dh.NgayTao, 'dd/MM/yyyy HH:mm:ss') as NgayTao " +
                    "FROM DonHang dh JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID LEFT JOIN KhachHang kh ON dh.ID_KhachHang = kh.ID_KhachHang " +
                    "WHERE dh.ID_DonHang=?");
                ps.setInt(1, Integer.parseInt(idParam));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    json.append("\"maDH\":\"").append(escape(rs.getString("MaDonHang"))).append("\"");
                    json.append(",\"tenKH\":\"").append(escape(rs.getString("HoTen") != null ? rs.getString("HoTen") : "Khách lẻ")).append("\"");
                    json.append(",\"sdtKH\":\"").append(escape(rs.getString("SoDienThoai") != null ? rs.getString("SoDienThoai") : "")).append("\"");
                    json.append(",\"diaChiKH\":\"").append(escape(rs.getString("DiaChi") != null ? rs.getString("DiaChi") : "")).append("\"");
                    json.append(",\"tongTien\":").append(rs.getLong("TongTien"));
                    json.append(",\"thanhToan\":\"").append(escape(rs.getString("PhuongThucThanhToan"))).append("\"");
                    json.append(",\"trangThai\":\"").append(escape(rs.getString("TrangThai"))).append("\"");
                    json.append(",\"ghiChu\":\"").append(escape(rs.getString("GhiChu") != null ? rs.getString("GhiChu") : "")).append("\"");
                    json.append(",\"ngayTao\":\"").append(escape(rs.getString("NgayTao") != null ? rs.getString("NgayTao") : "")).append("\"");
                    json.append(",\"tenNV\":\"").append(escape(rs.getString("TenNhanVien") != null ? rs.getString("TenNhanVien") : "Admin")).append("\"");
                    String vCode = "";
                    String vName = "";
                    try (PreparedStatement psV = conn.prepareStatement("SELECT MaVoucher, TenChuongTrinh FROM Vouchers WHERE DonHangSuDung = ? OR ? LIKE '%Voucher (' + MaVoucher + '):%'")) {
                        psV.setString(1, "Đơn hàng #" + idParam);
                        psV.setString(2, rs.getString("GhiChu") != null ? rs.getString("GhiChu") : "");
                        ResultSet rsV = psV.executeQuery();
                        if (rsV.next()) {
                            vCode = rsV.getString("MaVoucher");
                            vName = rsV.getString("TenChuongTrinh");
                        }
                    } catch (Exception ignored) {}
                    json.append(",\"voucherCode\":\"").append(escape(vCode)).append("\"");
                    json.append(",\"voucherName\":\"").append(escape(vName != null ? vName : "")).append("\"");
                    // Chi tiết sản phẩm
                    json.append(",\"chiTiet\":[");
                    PreparedStatement ps2 = conn.prepareStatement("SELECT ID_ChiTiet, ID_SanPham, ID_BienThe, TenSP, SoLuong, DonGia, ThanhTien FROM ChiTietDonHang WHERE ID_DonHang=?");
                    ps2.setInt(1, Integer.parseInt(idParam));
                    ResultSet rs2 = ps2.executeQuery();
                    boolean first = true;
                    while (rs2.next()) {
                        if (!first) json.append(",");
                        json.append("{\"idChiTiet\":").append(rs2.getInt("ID_ChiTiet"));
                        json.append(",\"idSP\":").append(rs2.getInt("ID_SanPham"));
                        json.append(",\"idBT\":").append(rs2.getInt("ID_BienThe"));
                        json.append(",\"tenSP\":\"").append(escape(rs2.getString("TenSP"))).append("\"");
                        json.append(",\"soLuong\":").append(rs2.getInt("SoLuong"));
                        json.append(",\"donGia\":").append(rs2.getLong("DonGia"));
                        json.append(",\"thanhTien\":").append(rs2.getLong("ThanhTien")).append("}");
                        first = false;
                    }
                    json.append("]");
                }
            } catch (Exception ex) { ex.printStackTrace(); }
            json.append("}");
            response.getWriter().write(json.toString());
            return;
        }

                // AJAX: lấy danh sách biến thể để đổi hàng/size thời trang DUNA
        String ajaxVariantsExchange = request.getParameter("ajaxVariantsExchange");
        if ("1".equals(ajaxVariantsExchange)) {
            response.setContentType("application/json;charset=UTF-8");
            String idSP = request.getParameter("idSP");
            StringBuilder json = new StringBuilder("[");
            try (Connection cEx = new connectService().myConnection()) {
                String sqlEx = "SELECT bt.ID_BienThe, bt.ID_SanPham, sp.TenSP, bt.MauSac, bt.TrongLuong, bt.GiaBan, bt.SoLuong " +
                               "FROM BienTheSanPham bt JOIN SanPham sp ON bt.ID_SanPham = sp.ID_SanPham " +
                               (idSP != null && !idSP.isEmpty() ? "WHERE bt.ID_SanPham = ? AND bt.SoLuong > 0 " : "WHERE bt.SoLuong > 0 ") +
                               "ORDER BY sp.TenSP, bt.TrongLuong, bt.MauSac";
                PreparedStatement psEx = cEx.prepareStatement(sqlEx);
                if (idSP != null && !idSP.isEmpty()) psEx.setInt(1, Integer.parseInt(idSP));
                ResultSet rsEx = psEx.executeQuery();
                boolean first = true;
                while (rsEx.next()) {
                    if (!first) json.append(",");
                    json.append("{\"idBT\":").append(rsEx.getInt("ID_BienThe"))
                        .append(",\"idSP\":").append(rsEx.getInt("ID_SanPham"))
                        .append(",\"tenSP\":\"").append(escape(rsEx.getString("TenSP"))).append("\"")
                        .append(",\"mauSac\":\"").append(escape(rsEx.getString("MauSac"))).append("\"")
                        .append(",\"size\":\"").append(escape(rsEx.getString("TrongLuong"))).append("\"")
                        .append(",\"giaBan\":").append(rsEx.getLong("GiaBan"))
                        .append(",\"soLuong\":").append(rsEx.getInt("SoLuong")).append("}");
                    first = false;
                }
            } catch (Exception ex) { ex.printStackTrace(); }
            json.append("]");
            response.getWriter().write(json.toString());
            return;
        }

        // AJAX: tìm sản phẩm theo barcode (dùng cho quét mã vạch)
        String ajaxScanBarcode = request.getParameter("ajaxScanBarcode");
        if ("1".equals(ajaxScanBarcode)) {
            response.setContentType("application/json;charset=UTF-8");
            String barcode = request.getParameter("barcode");
            if (barcode == null || barcode.trim().isEmpty()) {
                response.getWriter().write("{\"found\":false,\"message\":\"Barcode rỗng\"}");
                return;
            }
            StringBuilder json = new StringBuilder();
            try (Connection connBC = new connectService().myConnection()) {
                String cleanCode = barcode.trim().toUpperCase().replaceAll("[^A-Z0-9]", "");
                // 1. Tìm trong bảng BienTheSanPham (theo Barcode, MaBienThe, hoặc MaSP + MaBienThe, bỏ qua dấu cách/gạch nối)
                PreparedStatement psBC = connBC.prepareStatement(
                    "SELECT bt.ID_BienThe, bt.ID_SanPham, bt.Barcode, bt.MauSac, bt.TrongLuong, bt.GiaBan, bt.SoLuong, " +
                    "sp.TenSP, sp.TongSoLuong, sp.MaSP " +
                    "FROM BienTheSanPham bt " +
                    "JOIN SanPham sp ON bt.ID_SanPham = sp.ID_SanPham " +
                    "WHERE REPLACE(REPLACE(UPPER(ISNULL(bt.Barcode,'')), ' ', ''), '-', '') = ? " +
                    "OR REPLACE(REPLACE(UPPER(ISNULL(sp.MaSP,'') + ISNULL(bt.MaBienThe,'')), ' ', ''), '-', '') = ? " +
                    "OR REPLACE(REPLACE(UPPER(ISNULL(sp.MaSP,'') + 'BT' + CAST(bt.ID_BienThe AS VARCHAR)), ' ', ''), '-', '') = ? " +
                    "OR REPLACE(REPLACE(UPPER(ISNULL(bt.MaBienThe,'')), ' ', ''), '-', '') = ?");
                psBC.setString(1, cleanCode);
                psBC.setString(2, cleanCode);
                psBC.setString(3, cleanCode);
                psBC.setString(4, cleanCode);
                ResultSet rsBC = psBC.executeQuery();
                if (rsBC.next()) {
                    json.append("{\"found\":true")
                        .append(",\"id\":").append(rsBC.getInt("ID_SanPham"))
                        .append(",\"idBT\":").append(rsBC.getInt("ID_BienThe"))
                        .append(",\"tenSP\":\"").append(escape(rsBC.getString("TenSP"))).append("\"")
                        .append(",\"maSP\":\"").append(escape(rsBC.getString("MaSP") != null ? rsBC.getString("MaSP") : "")).append("\"")
                        .append(",\"giaBan\":").append(rsBC.getLong("GiaBan"))
                        .append(",\"mauSac\":\"").append(escape(rsBC.getString("MauSac") != null ? rsBC.getString("MauSac") : "")).append("\"")
                        .append(",\"trongLuong\":\"").append(escape(rsBC.getString("TrongLuong") != null ? rsBC.getString("TrongLuong") : "")).append("\"")
                        .append(",\"soLuong\":").append(rsBC.getInt("SoLuong"))
                        .append(",\"tongSoLuong\":").append(rsBC.getInt("TongSoLuong"))
                        .append(",\"barcode\":\"").append(escape(rsBC.getString("Barcode") != null ? rsBC.getString("Barcode") : cleanCode)).append("\"")
                        .append("}");
                } else {
                    // 2. Fallback: tìm trực tiếp trong SanPham (khi quét mã chung SP001, SP002...)
                    PreparedStatement psSP = connBC.prepareStatement(
                        "SELECT ID_SanPham, MaSP, TenSP, GiaBan, TongSoLuong, " +
                        "(SELECT TOP 1 ID_BienThe FROM BienTheSanPham WHERE ID_SanPham = SanPham.ID_SanPham) AS ID_BienThe, " +
                        "(SELECT TOP 1 MauSac FROM BienTheSanPham WHERE ID_SanPham = SanPham.ID_SanPham) AS MauSac, " +
                        "(SELECT TOP 1 TrongLuong FROM BienTheSanPham WHERE ID_SanPham = SanPham.ID_SanPham) AS TrongLuong, " +
                        "(SELECT TOP 1 SoLuong FROM BienTheSanPham WHERE ID_SanPham = SanPham.ID_SanPham) AS SoLuong " +
                        "FROM SanPham WHERE REPLACE(REPLACE(UPPER(ISNULL(MaSP,'')), ' ', ''), '-', '') = ? OR ('SP' + CAST(ID_SanPham AS VARCHAR)) = ? OR CAST(ID_SanPham AS VARCHAR) = ?");
                    psSP.setString(1, cleanCode);
                    psSP.setString(2, cleanCode);
                    psSP.setString(3, cleanCode);
                    ResultSet rsSP = psSP.executeQuery();
                    if (rsSP.next()) {
                        int idSP = rsSP.getInt("ID_SanPham");
                        int idBT = rsSP.getInt("ID_BienThe");
                        int slBT = rsSP.getInt("SoLuong");
                        int tongSL = rsSP.getInt("TongSoLuong");
                        String ms = rsSP.getString("MauSac");
                        String tl = rsSP.getString("TrongLuong");
                        json.append("{\"found\":true")
                            .append(",\"id\":").append(idSP)
                            .append(",\"idBT\":").append(idBT > 0 ? idBT : 0)
                            .append(",\"tenSP\":\"").append(escape(rsSP.getString("TenSP"))).append("\"")
                            .append(",\"maSP\":\"").append(escape(rsSP.getString("MaSP") != null ? rsSP.getString("MaSP") : "SP" + idSP)).append("\"")
                            .append(",\"giaBan\":").append(rsSP.getLong("GiaBan"))
                            .append(",\"mauSac\":\"").append(escape(ms != null ? ms : "")).append("\"")
                            .append(",\"trongLuong\":\"").append(escape(tl != null ? tl : "")).append("\"")
                            .append(",\"soLuong\":").append(idBT > 0 ? slBT : tongSL)
                            .append(",\"tongSoLuong\":").append(tongSL)
                            .append(",\"barcode\":\"").append(escape(cleanCode)).append("\"")
                            .append("}");
                    } else {
                        json.append("{\"found\":false,\"message\":\"Không tìm thấy sản phẩm với mã: ").append(escape(barcode.trim())).append("\"}");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                json.append("{\"found\":false,\"message\":\"Lỗi CSDL: ").append(escape(ex.getMessage())).append("\"}");
            }
            response.getWriter().write(json.toString());
            return;
        }

        // AJAX: tìm kiếm khách hàng
        String ajaxSearchKH = request.getParameter("ajaxSearchKH");
        if ("1".equals(ajaxSearchKH)) {
            response.setContentType("application/json;charset=UTF-8");
            String kw = request.getParameter("kw");
            if (kw == null || kw.trim().isEmpty()) { response.getWriter().write("[]"); return; }
            String kwLike = "%" + kw.trim() + "%";
            StringBuilder json = new StringBuilder("[");
            try (Connection cKH = new connectService().myConnection()) {
                PreparedStatement ps = cKH.prepareStatement(
                    "SELECT TOP 15 ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, HangKhachHang FROM KhachHang WHERE TrangThai='active' AND (" +
                    "HoTen LIKE ? OR SoDienThoai LIKE ? OR Email LIKE ? OR MaKH LIKE ?) ORDER BY HoTen");
                ps.setString(1,kwLike); ps.setString(2,kwLike); ps.setString(3,kwLike); ps.setString(4,kwLike);
                ResultSet rs = ps.executeQuery();
                boolean first = true;
                while (rs.next()) {
                    if (!first) json.append(",");
                    String hang = rs.getString("HangKhachHang"); if (hang==null) hang="Th\u01b0\u1eddng";
                    int discPct = 0;
                    if (hang.equals("B\u1ea1c")) discPct=5; else if (hang.equals("V\u00e0ng")) discPct=10; else if (hang.equals("VIP")) discPct=15;
                    json.append("{\"id\":").append(rs.getInt("ID_KhachHang"))
                        .append(",\"maKH\":\"").append(escape(rs.getString("MaKH")!=null?rs.getString("MaKH"):"")).append("\"")
                        .append(",\"ten\":\"").append(escape(rs.getString("HoTen")!=null?rs.getString("HoTen"):"")).append("\"")
                        .append(",\"sdt\":\"").append(escape(rs.getString("SoDienThoai")!=null?rs.getString("SoDienThoai"):"")).append("\"")
                        .append(",\"hang\":\"").append(escape(hang)).append("\"")
                        .append(",\"discountPct\":").append(discPct).append("}");
                    first = false;
                }
            } catch (Exception ex) { ex.printStackTrace(); }
            json.append("]");
            response.getWriter().write(json.toString());
            return;
        }

        // AJAX: thêm nhanh khách hàng từ modal bán hàng
        String ajaxAddKH = request.getParameter("ajaxAddKH");
        if ("1".equals(ajaxAddKH)) {
            response.setContentType("application/json;charset=UTF-8");
            String hoTen = request.getParameter("hoTen");
            String sdt   = request.getParameter("sdt");
            String email = request.getParameter("email");
            if (hoTen == null || hoTen.trim().isEmpty() || sdt == null || sdt.trim().isEmpty()) {
                response.getWriter().write("{\"success\":false,\"message\":\"Vui l\u00f2ng nh\u1eadp H\u1ecd t\u00ean v\u00e0 S\u0110T!\"}");
                return;
            }
            String cleanSdt = sdt.trim().replaceAll("[\\s\\-]", "");
            if (!cleanSdt.startsWith("0")) cleanSdt = "0" + cleanSdt;
            try (Connection connKH = new connectService().myConnection()) {
                PreparedStatement psCheck = connKH.prepareStatement("SELECT ID_KhachHang, HoTen FROM KhachHang WHERE SoDienThoai = ?");
                psCheck.setString(1, cleanSdt);
                ResultSet rsCheck = psCheck.executeQuery();
                if (rsCheck.next()) {
                    int existId = rsCheck.getInt("ID_KhachHang");
                    String existTen = rsCheck.getString("HoTen");
                    response.getWriter().write("{\"success\":true,\"id\":" + existId + ",\"ten\":\"" + escape(existTen) + "\",\"sdt\":\"" + escape(cleanSdt) + "\",\"existed\":true}");
                    return;
                }
                String maKH = "KH-" + System.currentTimeMillis();
                try (Statement st = connKH.createStatement(); ResultSet rs = st.executeQuery("SELECT ISNULL(MAX(ID_KhachHang), 0) + 1 FROM KhachHang")) {
                    if (rs.next()) maKH = String.format("KH-%05d", rs.getInt(1));
                } catch (Exception ignored) {}
                String sqlInsert = "INSERT INTO KhachHang (MaKH, HoTen, SoDienThoai, Email, GioiTinh, HangKhachHang, TrangThai, NgayTao, NgayCapNhat) VALUES (?, ?, ?, ?, N'Nam', N'Th\u01b0\u1eddng', 'active', GETDATE(), GETDATE())";
                PreparedStatement psIns = connKH.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                psIns.setString(1, maKH);
                psIns.setString(2, hoTen.trim());
                psIns.setString(3, cleanSdt);
                psIns.setString(4, email != null ? email.trim() : "");
                psIns.executeUpdate();
                ResultSet keys = psIns.getGeneratedKeys();
                int newId = 0;
                if (keys.next()) newId = keys.getInt(1);
                response.getWriter().write("{\"success\":true,\"id\":" + newId + ",\"ten\":\"" + escape(hoTen.trim()) + "\",\"sdt\":\"" + escape(cleanSdt) + "\",\"existed\":false}");
            } catch (Exception ex) {
                ex.printStackTrace();
                response.getWriter().write("{\"success\":false,\"message\":\"L\u1ed7i CSDL: " + escape(ex.getMessage()) + "\"}");
            }
            return;
        }

        // AJAX: tra cứu toàn bộ danh sách voucher trong hệ thống cho POS (GET ?ajaxLookupVouchers=1)
        if ("1".equals(request.getParameter("ajaxLookupVouchers"))) {
            response.setContentType("application/json;charset=UTF-8");
            StringBuilder json = new StringBuilder("{\"success\":true,\"vouchers\":[");
            try (Connection connVC = new connectService().myConnection()) {
                syncVouchers(connVC);
                String sqlVC = "SELECT MaVoucher, ISNULL(TenChuongTrinh, N'Voucher trợ giá') AS TenChuongTrinh, GiamGia, ISNULL(DonToiThieu, GiamGia * 5) AS DonToiThieu, SoLuong, ISNULL(DaSuDung, 0) AS DaSuDung, ISNULL(TrangThai, 1) AS TrangThai " +
                               "FROM Vouchers ORDER BY GiamGia ASC, MaVoucher ASC";
                PreparedStatement psVC = connVC.prepareStatement(sqlVC);
                ResultSet rsVC = psVC.executeQuery();
                
                List<String> items = new ArrayList<>();
                while (rsVC.next()) {
                    String vcCode = rsVC.getString("MaVoucher").trim().toUpperCase();
                    String vcTitle = rsVC.getString("TenChuongTrinh");
                    long vcAmount = rsVC.getLong("GiamGia");
                    long vcMinOrder = rsVC.getLong("DonToiThieu");
                    int vcSoLuong = rsVC.getInt("SoLuong");
                    int vcDaSuDung = rsVC.getInt("DaSuDung");
                    int vcTrangThai = rsVC.getInt("TrangThai");
                    
                    String vcStatus = (vcTrangThai == 0) ? "locked" : (vcSoLuong <= 0 ? "used" : "available");
                    String vcStatusName = (vcTrangThai == 0) ? "Đã khóa" : (vcSoLuong <= 0 ? "Đã sử dụng" : "Sẵn sàng");
                    
                    long vcMaxCap = 0;
                    if (vcAmount <= 100) {
                        if (vcAmount == 50) vcMaxCap = 3200000;
                        else if (vcAmount == 45) vcMaxCap = 2600000;
                        else if (vcAmount == 40) vcMaxCap = 2200000;
                        else if (vcAmount == 35) vcMaxCap = 1800000;
                        else if (vcAmount == 30) vcMaxCap = 1500000;
                        else if (vcAmount == 25) vcMaxCap = 1000000;
                        else if (vcAmount == 20) vcMaxCap = 750000;
                        else if (vcAmount == 15) vcMaxCap = 500000;
                        else if (vcAmount == 10) vcMaxCap = 300000;
                        else if (vcAmount == 5) vcMaxCap = 100000;
                        else vcMaxCap = 1000000;
                    }

                    items.add(String.format("{\"code\":\"%s\",\"title\":\"%s\",\"amount\":%d,\"maxCap\":%d,\"minOrder\":%d,\"soLuong\":%d,\"daSuDung\":%d,\"status\":\"%s\",\"statusName\":\"%s\"}",
                        escape(vcCode), escape(vcTitle), vcAmount, vcMaxCap, vcMinOrder, vcSoLuong, vcDaSuDung, vcStatus, vcStatusName));
                }
                
                json.append(String.join(",", items));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            json.append("]}");
            response.getWriter().write(json.toString());
            return;
        }

        // AJAX: kiểm tra voucher (GET ?ajaxCheckVoucher=1&code=yn100)
        String ajaxCheckVoucher = request.getParameter("ajaxCheckVoucher");
        if ("1".equals(ajaxCheckVoucher)) {
            response.setContentType("application/json;charset=UTF-8");
            String code = request.getParameter("code");
            if (code == null || code.trim().isEmpty()) {
                response.getWriter().write("{\"valid\":false,\"message\":\"Mã voucher không được để trống\"}");
                return;
            }
            String cleanCode = code.trim().toLowerCase();
            try (Connection connVC = new connectService().myConnection()) {
                syncVouchers(connVC);
                PreparedStatement psVC = connVC.prepareStatement(
                    "SELECT ID_Voucher, MaVoucher, GiamGia, ISNULL(DonToiThieu, GiamGia * 5) AS DonToiThieu, SoLuong, DaSuDung, ISNULL(TrangThai, 1) AS TrangThai FROM Vouchers WHERE LOWER(MaVoucher) = ?");
                psVC.setString(1, cleanCode);
                ResultSet rsVC = psVC.executeQuery();
                if (rsVC.next()) {
                    int soLuongConLai = rsVC.getInt("SoLuong");
                    long giamGia = rsVC.getLong("GiamGia");
                    long donToiThieu = rsVC.getLong("DonToiThieu");
                    int daSuDung = rsVC.getInt("DaSuDung");
                    int trangThai = rsVC.getInt("TrangThai");
                    long tongTien = 0;
                    String ttStr = request.getParameter("tongTien");
                    if (ttStr != null && !ttStr.trim().isEmpty()) {
                        try { tongTien = Long.parseLong(ttStr.trim()); } catch(Exception e) {}
                    }
                    boolean isVipVoucher = code.trim().toUpperCase().startsWith("VIP");
                    boolean allowVip = false;
                    if (isVipVoucher) {
                        int khId = 0;
                        String khStr = request.getParameter("khId");
                        if (khStr != null && !khStr.trim().isEmpty()) {
                            try { khId = Integer.parseInt(khStr.trim()); } catch(Exception e) {}
                        }
                        if (khId > 0) {
                            try (PreparedStatement psKh = connVC.prepareStatement("SELECT HangKhachHang FROM KhachHang WHERE ID_KhachHang = ?")) {
                                psKh.setInt(1, khId);
                                ResultSet rsKh = psKh.executeQuery();
                                if (rsKh.next() && "VIP".equalsIgnoreCase(rsKh.getString("HangKhachHang"))) {
                                    allowVip = true;
                                }
                            }
                        }
                    }
                    if (trangThai == 0) {
                        response.getWriter().write("{\"valid\":false,\"message\":\"Voucher \\u0022" + escape(code.trim().toUpperCase()) + "\\u0022 hiện đang bị khóa không cho phép sử dụng!\"}");
                    } else if (isVipVoucher && !allowVip) {
                        response.getWriter().write("{\"valid\":false,\"message\":\"Mã đặc quyền VIP \\u0022" + escape(code.trim().toUpperCase()) + "\\u0022 chỉ có thể áp dụng với các tài khoản khách hàng có Hạng VIP! Vui lòng chọn khách hàng Hạng VIP.\"}");
                    } else if (soLuongConLai <= 0) {
                        response.getWriter().write("{\"valid\":false,\"message\":\"Voucher \\u0022" + escape(code.trim().toUpperCase()) + "\\u0022 đã được sử dụng (hết lượt)!\"}");
                    } else if (tongTien > 0 && tongTien < donToiThieu) {
                        response.getWriter().write("{\"valid\":false,\"message\":\"Mã voucher \\u0022" + escape(code.trim().toUpperCase()) + "\\u0022 chỉ có thể áp dụng cho đơn hàng có giá trị tối thiểu từ " + String.format("%,d", donToiThieu).replace(',', '.') + " VNĐ! (Đơn hiện tại chưa đủ điều kiện)\"}");
                    } else {
                        if (giamGia <= 100) {
                            long actualDisc = (tongTien > 0) ? Math.round((double)tongTien * (double)giamGia / 100.0) : 0;
                            long maxCap = 0;
                            if (giamGia == 50) maxCap = 3200000;
                            else if (giamGia == 45) maxCap = 2600000;
                            else if (giamGia == 40) maxCap = 2200000;
                            else if (giamGia == 35) maxCap = 1800000;
                            else if (giamGia == 30) maxCap = 1500000;
                            else if (giamGia == 25) maxCap = 1000000;
                            else if (giamGia == 20) maxCap = 750000;
                            else if (giamGia == 15) maxCap = 500000;
                            else if (giamGia == 10) maxCap = 300000;
                            else if (giamGia == 5) maxCap = 100000;
                            else maxCap = 1000000;

                            boolean capped = (actualDisc > maxCap);
                            if (capped) actualDisc = maxCap;

                            String msg = "Áp dụng thành công mã gi\u1ea3m " + giamGia + "% (Gi\u1ea3m " + String.format("%,d", actualDisc).replace(',', '.') + " VNĐ" + (capped ? " — Đã chạm trần tối đa" : "") + ")!";
                            response.getWriter().write("{\"valid\":true,\"amount\":" + actualDisc
                                + ",\"minOrder\":" + donToiThieu
                                + ",\"soLuongConLai\":" + soLuongConLai
                                + ",\"daSuDung\":" + daSuDung
                                + ",\"message\":\"" + msg + "\"}");
                        } else {
                            response.getWriter().write("{\"valid\":true,\"amount\":" + giamGia
                                + ",\"minOrder\":" + donToiThieu
                                + ",\"soLuongConLai\":" + soLuongConLai
                                + ",\"daSuDung\":" + daSuDung
                                + ",\"message\":\"Áp dụng thành công mã gi\u1ea3m " + String.format("%,d", giamGia).replace(',', '.') + " VNĐ!\"}");
                        }
                    }
                } else {
                    response.getWriter().write("{\"valid\":false,\"message\":\"Mã voucher \\u0022" + escape(code.trim().toUpperCase()) + "\\u0022 không tồn tại trong hệ thống!\"}");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                response.getWriter().write("{\"valid\":false,\"message\":\"L\u1ed7i ki\u1ec3m tra voucher: " + escape(ex.getMessage()) + "\"}");
            }
            return;
        }

        List<Map<String, Object>> listDH  = new ArrayList<>();
        List<Map<String, Object>> listKH  = new ArrayList<>();
        List<Map<String, Object>> listSP  = new ArrayList<>();
        connectService db = new connectService();

        // Trang Bán hàng (tạo đơn hàng)
        if ("sell".equals(view)) {
            String holdIdStr = request.getParameter("holdId");
            try (Connection conn = db.myConnection()) {
                syncAllCustomerRanks(conn);
                syncVouchers(conn);
                int currentVat = syncAndGetVAT(conn);
                request.setAttribute("currentVat", currentVat);
                ResultSet rsKH = conn.prepareStatement(
                    "SELECT ID_KhachHang, HoTen, SoDienThoai, HangKhachHang FROM KhachHang WHERE TrangThai='active' ORDER BY HoTen"
                ).executeQuery();
                while (rsKH.next()) {
                    Map<String, Object> k = new HashMap<>();
                    k.put("id",   rsKH.getInt("ID_KhachHang"));
                    k.put("ten",  rsKH.getString("HoTen"));
                    k.put("sdt",  rsKH.getString("SoDienThoai"));
                    String hang = rsKH.getString("HangKhachHang");
                    if (hang == null) hang = "Thường";
                    k.put("hang", hang);
                    // Discount % theo hạng
                    int discountPct = 0;
                    if (hang.equals("Bạc")) discountPct = 5;
                    else if (hang.equals("Vàng")) discountPct = 10;
                    else if (hang.equals("VIP")) discountPct = 15;
                    k.put("discountPct", discountPct);
                    listKH.add(k);
                }
                ResultSet rsSP = conn.prepareStatement(
                    "SELECT s.ID_SanPham, s.MaSP, s.TenSP, s.ThuongHieu, s.GiaBan, s.TongSoLuong, " +
                    "ts.DoCung AS ValDoCung, ts.LucCang AS ValLucCang, ts.VatLieuKhung AS ValVatLieu, ts.DoiTuong AS ValDoiTuong " +
                    "FROM SanPham s " +
                    "LEFT JOIN (SELECT ID_SanPham, MAX(DoCung) AS DoCung, MAX(LucCang) AS LucCang, MAX(VatLieuKhung) AS VatLieuKhung, MAX(DoiTuong) AS DoiTuong FROM ThongSoKyThuat GROUP BY ID_SanPham) ts ON s.ID_SanPham = ts.ID_SanPham " +
                    "WHERE s.TongSoLuong > 0 " +
                    "ORDER BY s.TenSP"
                ).executeQuery();
                while (rsSP.next()) {
                    Map<String, Object> sp = new HashMap<>();
                    int idSP = rsSP.getInt("ID_SanPham");
                    sp.put("id",       idSP);
                    sp.put("maSP",     rsSP.getString("MaSP"));
                    sp.put("tenSP",    rsSP.getString("TenSP"));
                    sp.put("thuongHieu", rsSP.getString("ThuongHieu"));
                    sp.put("giaBan",   rsSP.getLong("GiaBan"));
                    sp.put("soLuong",  rsSP.getInt("TongSoLuong"));
                    
                    int pDoCung = calcScoreDoCung(rsSP.getString("ValDoCung"));
                    int pLucCang = calcScoreLucCang(rsSP.getString("ValLucCang"));
                    int pVatLieu = calcScoreVatLieu(rsSP.getString("ValVatLieu"));
                    int pDoiTuong = calcScoreDoiTuong(rsSP.getString("ValDoiTuong"));
                    double diemTB = (pDoCung + pLucCang + pVatLieu + pDoiTuong) / 4.0;
                    String phanCap = (diemTB >= 88.0) ? "Cao cấp" : (diemTB >= 76.0 ? "Trung cấp" : "Thường");
                    sp.put("phanCap",  phanCap);
                    
                    List<String> mauSacList = new ArrayList<>();
                    List<String> trongLuongList = new ArrayList<>();
                    StringBuilder variantsJson = new StringBuilder("[");
                    try (PreparedStatement psBT = conn.prepareStatement("SELECT ID_BienThe, MauSac, TrongLuong, GiaBan, Barcode, SoLuong FROM BienTheSanPham WHERE ID_SanPham=? ORDER BY ID_BienThe")) {
                        psBT.setInt(1, idSP);
                        try (ResultSet rsBT = psBT.executeQuery()) {
                            Set<String> setMau = new LinkedHashSet<>();
                            Set<String> setTL = new LinkedHashSet<>();
                            boolean firstBT = true;
                            while (rsBT.next()) {
                                String ms = rsBT.getString("MauSac");
                                String tl = rsBT.getString("TrongLuong");
                                String bc = rsBT.getString("Barcode");
                                long giaBT = rsBT.getLong("GiaBan");
                                int sl = rsBT.getInt("SoLuong");
                                if (ms != null && !ms.trim().isEmpty()) setMau.add(ms.trim());
                                if (tl != null && !tl.trim().isEmpty()) setTL.add(tl.trim());
                                if (bc == null || bc.trim().isEmpty()) {
                                    String msp = rsSP.getString("MaSP");
                                    bc = ((msp != null ? msp : ("SP" + idSP)) + "BT" + rsBT.getInt("ID_BienThe")).toUpperCase().replaceAll("[^A-Z0-9]", "");
                                }

                                if (!firstBT) variantsJson.append(",");
                                variantsJson.append("{\"idBT\":").append(rsBT.getInt("ID_BienThe"))
                                            .append(",\"mau\":\"").append(escape(ms != null ? ms.trim() : "")).append("\"")
                                            .append(",\"tl\":\"").append(escape(tl != null ? tl.trim() : "")).append("\"")
                                            .append(",\"gia\":").append(giaBT > 0 ? giaBT : rsSP.getLong("GiaBan"))
                                            .append(",\"barcode\":\"").append(escape(bc)).append("\"")
                                            .append(",\"sl\":").append(sl).append("}");
                                firstBT = false;
                            }
                            mauSacList.addAll(setMau);
                            trongLuongList.addAll(setTL);
                        }
                    }
                    variantsJson.append("]");
                    sp.put("mauSacList", mauSacList);
                    sp.put("trongLuongList", trongLuongList);
                    sp.put("variantsJson", variantsJson.toString());
                    listSP.add(sp);
                }

                // Nếu mở từ hóa đơn chờ: load thông tin holdId để JSP pre-populate
                if (holdIdStr != null && !holdIdStr.trim().isEmpty()) {
                    try {
                        int holdId = Integer.parseInt(holdIdStr.trim());
                        PreparedStatement psHold = conn.prepareStatement(
                            "SELECT dh.ID_DonHang, dh.MaDonHang, dh.TongTien, dh.PhuongThucThanhToan, ttdh.TenTrangThai AS TrangThai, dh.TenNhanVien, " +
                            "dh.ID_KhachHang, kh.HoTen, kh.SoDienThoai, kh.HangKhachHang, dh.GhiChu " +
                            "FROM DonHang dh JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID LEFT JOIN KhachHang kh ON dh.ID_KhachHang = kh.ID_KhachHang " +
                            "WHERE dh.ID_DonHang=? AND ttdh.TenTrangThai=N'Chưa thanh toán'");
                        psHold.setInt(1, holdId);
                        ResultSet rsHold = psHold.executeQuery();
                        if (rsHold.next()) {
                            Map<String, Object> holdInfo = new HashMap<>();
                            holdInfo.put("idDH", rsHold.getInt("ID_DonHang"));
                            holdInfo.put("maDH", rsHold.getString("MaDonHang") != null ? rsHold.getString("MaDonHang") : "");
                            holdInfo.put("idKH", rsHold.getObject("ID_KhachHang") != null ? rsHold.getInt("ID_KhachHang") : 0);
                            holdInfo.put("tenKH", rsHold.getString("HoTen") != null ? rsHold.getString("HoTen") : "Khách vãng lai");
                            holdInfo.put("sdtKH", rsHold.getString("SoDienThoai") != null ? rsHold.getString("SoDienThoai") : "");
                            holdInfo.put("hang", rsHold.getString("HangKhachHang") != null ? rsHold.getString("HangKhachHang") : "Thường");
                            holdInfo.put("tenNV", rsHold.getString("TenNhanVien") != null ? rsHold.getString("TenNhanVien") : "Admin");
                            holdInfo.put("ghiChu", rsHold.getString("GhiChu") != null ? rsHold.getString("GhiChu") : "");
                            // Load chi tiết
                            List<Map<String, Object>> holdItems = new ArrayList<>();
                            PreparedStatement psCT = conn.prepareStatement(
                                "SELECT ct.ID_SanPham, ct.TenSP, ct.SoLuong, ct.DonGia, sp.TongSoLuong " +
                                "FROM ChiTietDonHang ct LEFT JOIN SanPham sp ON ct.ID_SanPham=sp.ID_SanPham " +
                                "WHERE ct.ID_DonHang=?");
                            psCT.setInt(1, holdId);
                            ResultSet rsCT = psCT.executeQuery();
                            while (rsCT.next()) {
                                Map<String, Object> ci = new HashMap<>();
                                ci.put("idSP", rsCT.getInt("ID_SanPham"));
                                ci.put("tenSP", rsCT.getString("TenSP") != null ? rsCT.getString("TenSP") : "");
                                ci.put("soLuong", rsCT.getInt("SoLuong"));
                                ci.put("donGia", rsCT.getLong("DonGia"));
                                ci.put("tonKho", rsCT.getInt("TongSoLuong"));
                                holdItems.add(ci);
                            }
                            holdInfo.put("items", holdItems);
                            request.setAttribute("holdInfo", holdInfo);
                            request.setAttribute("holdId", holdId);
                        }
                    } catch (Exception ex) { ex.printStackTrace(); }
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Lỗi CSDL: " + e.getMessage());
            }
            request.setAttribute("listKH", listKH);
            request.setAttribute("listSP", listSP);
            request.getRequestDispatcher("ban-hang.jsp").forward(request, response);
            return;
        }

        if ("report".equals(view)) {
            String thangTop5Str = request.getParameter("thangTop5");
            String namStr = request.getParameter("nam");        // Năm cho biểu đồ & tổng doanh thu năm
            String namTop5Str = request.getParameter("namTop5"); // Năm riêng cho TOP 5
            String thangDaBanStr = request.getParameter("thangDaBan"); // Tháng cho DS sản phẩm đã bán
            String namDaBanStr = request.getParameter("namDaBan");     // Năm cho DS sản phẩm đã bán

            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);
            int currentMonth = cal.get(Calendar.MONTH) + 1;

            int selectedThangTop5 = currentMonth;
            try {
                if (thangTop5Str != null && !thangTop5Str.trim().isEmpty()) {
                    selectedThangTop5 = Integer.parseInt(thangTop5Str.trim());
                }
            } catch (Exception e) {
                selectedThangTop5 = currentMonth;
            }

            int filterYear = currentYear;
            try {
                if (namStr != null && !namStr.trim().isEmpty()) {
                    filterYear = Integer.parseInt(namStr.trim());
                }
            } catch (Exception e) {}

            int filterYearTop5 = filterYear;
            try {
                if (namTop5Str != null && !namTop5Str.trim().isEmpty()) {
                    filterYearTop5 = Integer.parseInt(namTop5Str.trim());
                }
            } catch (Exception e) {}

            int selectedThangDaBan = currentMonth;
            try {
                if (thangDaBanStr != null && !thangDaBanStr.trim().isEmpty()) {
                    selectedThangDaBan = Integer.parseInt(thangDaBanStr.trim());
                }
            } catch (Exception e) {
                selectedThangDaBan = currentMonth;
            }

            int filterYearDaBan = filterYear;
            try {
                if (namDaBanStr != null && !namDaBanStr.trim().isEmpty()) {
                    filterYearDaBan = Integer.parseInt(namDaBanStr.trim());
                }
            } catch (Exception e) {}

            long totalRevenueToday = 0;
            long totalRevenueAllTime = 0;
            long totalRevenueYear = 0;
            long totalCostToday = 0;
            long totalCostAllTime = 0;
            long totalCostYear = 0;
            int totalSold = 0;
            int totalSoldYear = 0;
            int totalInventory = 0;
            List<Map<String, Object>> topProducts = new ArrayList<>();
            List<Map<String, Object>> categoryStock = new ArrayList<>();
            List<Map<String, Object>> soldProducts = new ArrayList<>();
            long[] monthlyRevenue = new long[12];
            long[] monthlyCost = new long[12];
            long[] monthlyProfit = new long[12];
            int[] monthlySold = new int[12];

            try (Connection conn = db.myConnection()) {
                // 1a. Tổng doanh thu hôm nay
                PreparedStatement ps1a = conn.prepareStatement(
                    "SELECT ISNULL(SUM(dh.TongTien), 0) FROM DonHang dh JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID=ttdh.TrangThaiDH_ID WHERE ttdh.TenTrangThai = ? AND CONVERT(date, dh.NgayTao) = CONVERT(date, GETDATE())"
                );
                ps1a.setString(1, "Đã thanh toán");
                ResultSet rs1a = ps1a.executeQuery();
                if (rs1a.next()) totalRevenueToday = rs1a.getLong(1);

                // 1b. Tổng giá vốn hôm nay
                PreparedStatement psCostToday = conn.prepareStatement(
                    "SELECT ISNULL(SUM(ISNULL(ct.GiaNhapLucBan, 0) * ct.SoLuong), 0) FROM ChiTietDonHang ct JOIN DonHang dh ON ct.ID_DonHang = dh.ID_DonHang JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID=ttdh.TrangThaiDH_ID WHERE ttdh.TenTrangThai = ? AND CONVERT(date, dh.NgayTao) = CONVERT(date, GETDATE())"
                );
                psCostToday.setString(1, "Đã thanh toán");
                ResultSet rsCostToday = psCostToday.executeQuery();
                if (rsCostToday.next()) totalCostToday = rsCostToday.getLong(1);

                // 1. Tổng doanh thu toàn thời gian (Đã thanh toán)
                PreparedStatement ps1 = conn.prepareStatement(
                    "SELECT ISNULL(SUM(dh.TongTien), 0) FROM DonHang dh JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID=ttdh.TrangThaiDH_ID WHERE ttdh.TenTrangThai = ?"
                );
                ps1.setString(1, "Đã thanh toán");
                ResultSet rs1 = ps1.executeQuery();
                if (rs1.next()) totalRevenueAllTime = rs1.getLong(1);

                // Giá vốn toàn thời gian
                PreparedStatement psCostAll = conn.prepareStatement(
                    "SELECT ISNULL(SUM(ISNULL(ct.GiaNhapLucBan, 0) * ct.SoLuong), 0) FROM ChiTietDonHang ct JOIN DonHang dh ON ct.ID_DonHang = dh.ID_DonHang JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID=ttdh.TrangThaiDH_ID WHERE ttdh.TenTrangThai = ?"
                );
                psCostAll.setString(1, "Đã thanh toán");
                ResultSet rsCostAll = psCostAll.executeQuery();
                if (rsCostAll.next()) totalCostAllTime = rsCostAll.getLong(1);

                // 2. Tổng số lượng tồn kho
                PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT ISNULL(SUM(TongSoLuong), 0) FROM SanPham"
                );
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next()) totalInventory = rs2.getInt(1);

                // 3. Tổng số lượng sản phẩm đã bán (trong các đơn hàng đã thanh toán)
                PreparedStatement psSold = conn.prepareStatement(
                    "SELECT ISNULL(SUM(ct.SoLuong), 0) FROM ChiTietDonHang ct JOIN DonHang dh ON ct.ID_DonHang = dh.ID_DonHang JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID=ttdh.TrangThaiDH_ID WHERE ttdh.TenTrangThai = ?"
                );
                psSold.setString(1, "Đã thanh toán");
                ResultSet rsSold = psSold.executeQuery();
                if (rsSold.next()) totalSold = rsSold.getInt(1);

                // 4. Biểu đồ doanh thu theo tháng (trong năm được chọn) & Tổng doanh thu năm đó
                PreparedStatement ps3 = conn.prepareStatement(
                    "SELECT MONTH(dh.NgayTao) AS Thang, SUM(dh.TongTien) AS DoanhThu " +
                    "FROM DonHang dh JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID=ttdh.TrangThaiDH_ID WHERE ttdh.TenTrangThai = ? AND YEAR(dh.NgayTao) = ? " +
                    "GROUP BY MONTH(dh.NgayTao)"
                );
                ps3.setString(1, "Đã thanh toán");
                ps3.setInt(2, filterYear);
                ResultSet rs3 = ps3.executeQuery();
                while (rs3.next()) {
                    int m = rs3.getInt("Thang");
                    long rev = rs3.getLong("DoanhThu");
                    if (m >= 1 && m <= 12) {
                        monthlyRevenue[m - 1] = rev;
                        totalRevenueYear += rev;
                    }
                }

                // Giá vốn theo tháng trong năm
                PreparedStatement psCostYear = conn.prepareStatement(
                    "SELECT MONTH(dh.NgayTao) AS Thang, ISNULL(SUM(ISNULL(ct.GiaNhapLucBan, 0) * ct.SoLuong), 0) AS GiaVon " +
                    "FROM ChiTietDonHang ct JOIN DonHang dh ON ct.ID_DonHang = dh.ID_DonHang JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID=ttdh.TrangThaiDH_ID " +
                    "WHERE ttdh.TenTrangThai = ? AND YEAR(dh.NgayTao) = ? GROUP BY MONTH(dh.NgayTao)"
                );
                psCostYear.setString(1, "Đã thanh toán");
                psCostYear.setInt(2, filterYear);
                ResultSet rsCostYear = psCostYear.executeQuery();
                while (rsCostYear.next()) {
                    int m = rsCostYear.getInt("Thang");
                    long cost = rsCostYear.getLong("GiaVon");
                    if (m >= 1 && m <= 12) {
                        monthlyCost[m - 1] = cost;
                        totalCostYear += cost;
                    }
                }
                for (int i = 0; i < 12; i++) {
                    monthlyProfit[i] = monthlyRevenue[i] - monthlyCost[i];
                }

                long grossProfitToday = totalRevenueToday - totalCostToday;
                long grossProfitYear = totalRevenueYear - totalCostYear;
                long grossProfitAllTime = totalRevenueAllTime - totalCostAllTime;
                double profitMarginYear = totalRevenueYear > 0 ? (grossProfitYear * 100.0 / totalRevenueYear) : 0.0;

                // 3B. Số lượng sản phẩm đã bán theo từng tháng trong năm & Tổng sản phẩm đã bán năm đó
                PreparedStatement psSoldYear = conn.prepareStatement(
                    "SELECT MONTH(dh.NgayTao) AS Thang, ISNULL(SUM(ct.SoLuong), 0) AS SoLuong " +
                    "FROM ChiTietDonHang ct JOIN DonHang dh ON ct.ID_DonHang = dh.ID_DonHang " +
                    "JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID=ttdh.TrangThaiDH_ID " +
                    "WHERE ttdh.TenTrangThai = ? AND YEAR(dh.NgayTao) = ? " +
                    "GROUP BY MONTH(dh.NgayTao)"
                );
                psSoldYear.setString(1, "Đã thanh toán");
                psSoldYear.setInt(2, filterYear);
                ResultSet rsSoldYear = psSoldYear.executeQuery();
                while (rsSoldYear.next()) {
                    int m = rsSoldYear.getInt("Thang");
                    int qty = rsSoldYear.getInt("SoLuong");
                    if (m >= 1 && m <= 12) {
                        monthlySold[m - 1] = qty;
                        totalSoldYear += qty;
                    }
                }

                // 4. TOP 10 sản phẩm bán chạy lọc theo tháng được chọn
                PreparedStatement ps4 = conn.prepareStatement(
                    "SELECT TOP 10 COALESCE(ct.TenSP, spd.TenSP, spv.TenSP, N'Sản phẩm') AS TenSP, " +
                    "SUM(ct.SoLuong) AS DaBan, SUM(ct.ThanhTien) AS DoanhThu " +
                    "FROM ChiTietDonHang ct " +
                    "JOIN DonHang dh ON ct.ID_DonHang = dh.ID_DonHang " +
                    "JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                    "LEFT JOIN SanPham spd ON ct.ID_SanPham = spd.ID_SanPham " +
                    "LEFT JOIN BienTheSanPham bt ON ct.ID_BienThe = bt.ID_BienThe " +
                    "LEFT JOIN SanPham spv ON bt.ID_SanPham = spv.ID_SanPham " +
                    "WHERE ttdh.TenTrangThai = ? AND MONTH(dh.NgayTao) = ? AND YEAR(dh.NgayTao) = ? " +
                    "GROUP BY COALESCE(ct.TenSP, spd.TenSP, spv.TenSP, N'Sản phẩm') " +
                    "ORDER BY DaBan DESC, DoanhThu DESC"
                );
                ps4.setString(1, "Đã thanh toán");
                ps4.setInt(2, selectedThangTop5);
                ps4.setInt(3, filterYearTop5);
                ResultSet rs4 = ps4.executeQuery();
                int idx = 1;
                while (rs4.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("stt", idx++);
                    item.put("tenSP", rs4.getString("TenSP"));
                    item.put("thang", selectedThangTop5);
                    item.put("nam", filterYearTop5);
                    item.put("daBan", rs4.getInt("DaBan"));
                    item.put("doanhThu", rs4.getLong("DoanhThu"));
                    topProducts.add(item);
                }

                // 5. Thống kê tồn kho theo Danh Mục Thời Trang Nam DUNA
                PreparedStatement ps5 = conn.prepareStatement(
                    "SELECT dm.TenDanhMuc, ISNULL(SUM(bt.SoLuong), 0) AS TonKho " +
                    "FROM DanhMuc dm " +
                    "LEFT JOIN SanPham sp ON dm.ID_DanhMuc = sp.ID_DanhMuc " +
                    "LEFT JOIN BienTheSanPham bt ON sp.ID_SanPham = bt.ID_SanPham " +
                    "GROUP BY dm.TenDanhMuc ORDER BY TonKho DESC"
                );
                ResultSet rs5 = ps5.executeQuery();
                while (rs5.next()) {
                    Map<String, Object> cat = new HashMap<>();
                    cat.put("danhMuc", rs5.getString("TenDanhMuc"));
                    cat.put("soLuongTon", rs5.getInt("TonKho"));
                    categoryStock.add(cat);
                }

                // 6. Danh sách sản phẩm đã bán trong tháng/năm
                PreparedStatement ps6 = conn.prepareStatement(
                    "SELECT COALESCE(spd.MaSP, spv.MaSP, 'SP' + CAST(ISNULL(ct.ID_SanPham, ISNULL(bt.ID_SanPham, 0)) AS VARCHAR)) AS MaSP, " +
                    "COALESCE(ct.TenSP, spd.TenSP, spv.TenSP, N'Sản phẩm') AS TenSP, " +
                    "ct.SoLuong AS SoLuong, ct.ThanhTien AS ThanhTien, dh.NgayTao AS NgayTao, " +
                    "ISNULL(dm.TenDanhMuc, N'Thời trang') AS DanhMuc " +
                    "FROM ChiTietDonHang ct " +
                    "JOIN DonHang dh ON ct.ID_DonHang = dh.ID_DonHang " +
                    "JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                    "LEFT JOIN SanPham spd ON ct.ID_SanPham = spd.ID_SanPham " +
                    "LEFT JOIN BienTheSanPham bt ON ct.ID_BienThe = bt.ID_BienThe " +
                    "LEFT JOIN SanPham spv ON bt.ID_SanPham = spv.ID_SanPham " +
                    "LEFT JOIN DanhMuc dm ON COALESCE(spd.ID_DanhMuc, spv.ID_DanhMuc) = dm.ID_DanhMuc " +
                    "WHERE ttdh.TenTrangThai = ? AND MONTH(dh.NgayTao) = ? AND YEAR(dh.NgayTao) = ? " +
                    "ORDER BY dh.NgayTao DESC, dh.ID_DonHang DESC"
                );
                ps6.setString(1, "Đã thanh toán");
                ps6.setInt(2, selectedThangDaBan);
                ps6.setInt(3, filterYearDaBan);
                ResultSet rs6 = ps6.executeQuery();
                int idx6 = 1;
                java.text.SimpleDateFormat sdfDaBan = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                while (rs6.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("stt", idx6++);
                    item.put("maSP", rs6.getString("MaSP") != null ? rs6.getString("MaSP") : "");
                    item.put("tenSP", rs6.getString("TenSP") != null ? rs6.getString("TenSP") : "");
                    item.put("soLuong", rs6.getInt("SoLuong"));
                    item.put("thanhTien", rs6.getLong("ThanhTien"));
                    java.sql.Timestamp ts = rs6.getTimestamp("NgayTao");
                    item.put("thoiGian", (ts != null) ? sdfDaBan.format(ts) : "-");
                    item.put("phanLoai", rs6.getString("DanhMuc"));
                    soldProducts.add(item);
                }

                request.setAttribute("stat_totalCostToday", totalCostToday);
                request.setAttribute("stat_totalCostYear", totalCostYear);
                request.setAttribute("stat_totalCostAllTime", totalCostAllTime);
                request.setAttribute("stat_grossProfitToday", grossProfitToday);
                request.setAttribute("stat_grossProfitYear", grossProfitYear);
                request.setAttribute("stat_grossProfitAllTime", grossProfitAllTime);
                request.setAttribute("stat_profitMarginYear", String.format(Locale.US, "%.1f", profitMarginYear));

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Lỗi CSDL khi tải báo cáo: " + e.getMessage());
            }

            StringBuilder chartDataStr = new StringBuilder("[");
            for (int i = 0; i < 12; i++) {
                chartDataStr.append(monthlyRevenue[i]);
                if (i < 11) chartDataStr.append(",");
            }
            chartDataStr.append("]");

            StringBuilder chartProfitDataStr = new StringBuilder("[");
            for (int i = 0; i < 12; i++) {
                chartProfitDataStr.append(monthlyProfit[i]);
                if (i < 11) chartProfitDataStr.append(",");
            }
            chartProfitDataStr.append("]");

            StringBuilder chartSoldDataStr = new StringBuilder("[");
            for (int i = 0; i < 12; i++) {
                chartSoldDataStr.append(monthlySold[i]);
                if (i < 11) chartSoldDataStr.append(",");
            }
            chartSoldDataStr.append("]");

            if ("excel".equals(request.getParameter("export"))) {
                exportReportToExcel(response, filterYear, filterYearTop5, selectedThangTop5, filterYearDaBan, selectedThangDaBan, totalRevenueAllTime, totalRevenueYear, totalSold, totalSoldYear, totalInventory, monthlyRevenue, monthlySold, topProducts, categoryStock, soldProducts);
                return;
            }

            request.setAttribute("stat_totalRevenueToday", totalRevenueToday);
            request.setAttribute("stat_totalRevenueAllTime", totalRevenueAllTime);
            request.setAttribute("stat_totalRevenueYear", totalRevenueYear);
            request.setAttribute("stat_totalSold", totalSold);
            request.setAttribute("stat_totalSoldYear", totalSoldYear);
            request.setAttribute("stat_totalInventory", totalInventory);
            request.setAttribute("stat_monthlyRevenue", chartDataStr.toString());
            request.setAttribute("stat_monthlyProfit", chartProfitDataStr.toString());
            request.setAttribute("stat_monthlySold", chartSoldDataStr.toString());
            request.setAttribute("topProducts", topProducts);
            request.setAttribute("categoryStock", categoryStock);
            request.setAttribute("soldProducts", soldProducts);
            request.setAttribute("filterYear", filterYear);
            request.setAttribute("filterYearTop5", filterYearTop5);
            request.setAttribute("filterYearDaBan", filterYearDaBan);
            request.setAttribute("currentYear", currentYear);
            request.setAttribute("thangTop5", selectedThangTop5);
            request.setAttribute("thangDaBan", selectedThangDaBan);

            request.getRequestDispatcher("bao-cao-thong-ke.jsp").forward(request, response);
            return;
        }

        try (Connection conn = db.myConnection()) {
            // --- Load danh sách đơn hàng ---
            StringBuilder sql = new StringBuilder(
                "SELECT dh.ID_DonHang, dh.MaDonHang, kh.HoTen, kh.MaKH, " +
                "dh.TongTien, dh.PhuongThucThanhToan, ttdh.TenTrangThai AS TrangThai, dh.GhiChu, dh.TenNhanVien, dh.NgayTao " +
                "FROM DonHang dh JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID LEFT JOIN KhachHang kh ON dh.ID_KhachHang = kh.ID_KhachHang WHERE 1=1 ");
            List<Object> params = new ArrayList<>();
            if (search != null && !search.trim().isEmpty()) {
                sql.append("AND (dh.MaDonHang LIKE ? OR kh.HoTen LIKE ? OR CAST(dh.ID_DonHang AS VARCHAR) = ? OR dh.GhiChu LIKE ?) ");
                params.add("%" + search.trim() + "%"); 
                params.add("%" + search.trim() + "%");
                params.add(search.trim().replace("Đơn hàng #", "").replace("Don hang #", "").trim());
                params.add("%" + search.trim() + "%");
            }
            if (status != null && !status.trim().isEmpty()) { sql.append("AND ttdh.TenTrangThai = ? "); params.add(status.trim()); }
            if (pay    != null && !pay.trim().isEmpty())    { sql.append("AND dh.PhuongThucThanhToan = ? "); params.add(pay.trim()); }
            sql.append("ORDER BY dh.ID_DonHang DESC");
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id",         rs.getInt("ID_DonHang"));
                m.put("maDH",       rs.getString("MaDonHang"));
                m.put("tenKH",      rs.getString("HoTen") != null ? rs.getString("HoTen") : "Khách lẻ");
                m.put("tongTien",   rs.getLong("TongTien"));
                m.put("thanhToan",  rs.getString("PhuongThucThanhToan"));
                m.put("trangThai",  rs.getString("TrangThai"));
                m.put("ghiChu",     rs.getString("GhiChu") != null ? rs.getString("GhiChu") : "");
                m.put("tenNV",      rs.getString("TenNhanVien") != null ? rs.getString("TenNhanVien") : "Admin");
                String ntFmt = "";
                try {
                    java.sql.Timestamp ts = rs.getTimestamp("NgayTao");
                    if (ts != null) ntFmt = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(ts);
                } catch (Exception exDate) {
                    ntFmt = rs.getString("NgayTao") != null ? rs.getString("NgayTao").replace(".000", "").replace(".0", "") : "";
                }
                m.put("ngayTao",    ntFmt);
                // Load chi tiết
                List<Map<String, Object>> chiTiet = new ArrayList<>();
                PreparedStatement ps2 = conn.prepareStatement("SELECT ID_ChiTiet, ID_SanPham, ID_BienThe, TenSP, SoLuong, DonGia, ThanhTien FROM ChiTietDonHang WHERE ID_DonHang=?");
                ps2.setInt(1, rs.getInt("ID_DonHang"));
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    Map<String, Object> ct = new HashMap<>();
                    ct.put("idChiTiet", rs2.getInt("ID_ChiTiet"));
                    ct.put("idSP",      rs2.getInt("ID_SanPham"));
                    ct.put("idBT",      rs2.getInt("ID_BienThe"));
                    ct.put("tenSP",     rs2.getString("TenSP"));
                    ct.put("soLuong",   rs2.getInt("SoLuong"));
                    ct.put("donGia",    rs2.getLong("DonGia"));
                    ct.put("thanhTien", rs2.getLong("ThanhTien"));
                    chiTiet.add(ct);
                }
                m.put("chiTiet", chiTiet);
                listDH.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi CSDL: " + e.getMessage());
        }

        // --- Logic Phân trang cho danh sách đơn hàng (10 dòng/trang) ---
        int pageSize = 10;
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try { page = Integer.parseInt(pageParam.trim()); } catch (Exception e) { page = 1; }
        }
        if (page < 1) page = 1;
        int totalItems = listDH.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (page > totalPages && totalPages > 0) page = totalPages;
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);
        List<Map<String, Object>> pagedList = (totalItems > 0 && fromIndex < totalItems) ? listDH.subList(fromIndex, toIndex) : new ArrayList<>();

        // --- Tính tổng số liệu toàn bộ đơn hàng từ DATABASE ---
        long totalRevenue = 0;
        int totalOrders = 0;
        int cancelledOrders = 0;
        int completedOrders = 0;
        try (Connection connStat = db.myConnection()) {
            ResultSet rsTotalOrders = connStat.prepareStatement("SELECT COUNT(*) FROM DonHang").executeQuery();
            if (rsTotalOrders.next()) totalOrders = rsTotalOrders.getInt(1);

            PreparedStatement psRev = connStat.prepareStatement("SELECT ISNULL(SUM(dh.TongTien), 0) FROM DonHang dh JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID=ttdh.TrangThaiDH_ID WHERE ttdh.TenTrangThai = ?");
            psRev.setString(1, "Đã thanh toán");
            ResultSet rsRevenue = psRev.executeQuery();
            if (rsRevenue.next()) totalRevenue = rsRevenue.getLong(1);

            PreparedStatement psCompleted = connStat.prepareStatement("SELECT COUNT(*) FROM DonHang dh JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID=ttdh.TrangThaiDH_ID WHERE ttdh.TenTrangThai = ?");
            psCompleted.setString(1, "Đã thanh toán");
            ResultSet rsCompleted = psCompleted.executeQuery();
            if (rsCompleted.next()) completedOrders = rsCompleted.getInt(1);

            PreparedStatement psCancelled = connStat.prepareStatement("SELECT COUNT(*) FROM DonHang dh JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID=ttdh.TrangThaiDH_ID WHERE ttdh.TenTrangThai = ?");
            psCancelled.setString(1, "Đã hủy");
            ResultSet rsCancelled = psCancelled.executeQuery();
            if (rsCancelled.next()) cancelledOrders = rsCancelled.getInt(1);
            int currentVat = syncAndGetVAT(connStat);
            request.setAttribute("currentVat", currentVat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("stat_totalOrders", totalOrders);
        request.setAttribute("stat_totalRevenue", totalRevenue);
        request.setAttribute("stat_completedOrders", completedOrders);
        request.setAttribute("stat_cancelledOrders", cancelledOrders);

        request.setAttribute("listDH", pagedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("p_search", search != null ? search : "");
        request.setAttribute("p_status", status != null ? status : "");
        request.setAttribute("p_pay",    pay    != null ? pay    : "");
        request.getRequestDispatcher("quan-ly-hoa-don.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        connectService db = new connectService();
        try (Connection conn = db.myConnection()) {
            if ("updateVAT".equals(action)) {
                response.setContentType("application/json;charset=UTF-8");
                String role = (String) request.getSession().getAttribute("role");
                Integer rId = (Integer) request.getSession().getAttribute("roleID");
                String u = (String) request.getSession().getAttribute("username");
                boolean isAdmin = "MANAGER".equals(role) || (rId != null && rId == 1) || "admin".equalsIgnoreCase(u);
                if (!isAdmin) {
                    response.getWriter().write("{\"success\":false,\"message\":\"⚠️ Chỉ có tài khoản Quản lý (Admin) mới có quyền chỉnh sửa thuế suất VAT!\"}");
                    return;
                }
                String newVatStr = request.getParameter("vatPct");
                try {
                    int newVat = Integer.parseInt(newVatStr.trim());
                    if (newVat < 0 || newVat > 100) {
                        response.getWriter().write("{\"success\":false,\"message\":\"❌ Mức thuế suất VAT phải nằm trong khoảng 0% - 100%!\"}");
                        return;
                    }
                    syncAndGetVAT(conn);
                    PreparedStatement psUpdSP = conn.prepareStatement("UPDATE SanPham SET TyLeVat = ?");
                    psUpdSP.setInt(1, newVat);
                    psUpdSP.executeUpdate();
                    
                    PreparedStatement psUpdDM = conn.prepareStatement("UPDATE DanhMuc SET TyLeVat = ?");
                    psUpdDM.setInt(1, newVat);
                    psUpdDM.executeUpdate();
                    
                    response.getWriter().write("{\"success\":true,\"newVat\":" + newVat + ",\"message\":\"✅ Đã cập nhật thuế suất VAT cho toàn bộ Sản Phẩm & Danh Mục thành " + newVat + "%! (Theo Quy chuẩn 1)\"}");
                } catch (Exception ex) {
                    response.getWriter().write("{\"success\":false,\"message\":\"❌ Lỗi khi cập nhật VAT: " + escape(ex.getMessage()) + "\"}");
                }
                return;
            }

            if ("create".equals(action)) {
                // Tạo đơn hàng mới (hỗ trợ cả khách vãng lai và khách quen, cả paid và hold)
                String idKHStr     = request.getParameter("idKH");
                String invoiceType = request.getParameter("invoiceType"); // "paid" | "hold"
                if (invoiceType == null || invoiceType.trim().isEmpty()) invoiceType = "paid";
                String holdIdStr   = request.getParameter("holdId");

                int idKH = 0;
                try { if (idKHStr != null && !idKHStr.trim().isEmpty()) idKH = Integer.parseInt(idKHStr.trim()); } catch (Exception e) { idKH = 0; }

                int holdId = 0;
                try { if (holdIdStr != null && !holdIdStr.trim().isEmpty()) holdId = Integer.parseInt(holdIdStr.trim()); } catch (Exception e) { holdId = 0; }

                String pay  = request.getParameter("thanhToan");
                long discountAmount = parseMoney(request.getParameter("discountAmount"));
                long vatAmount = parseMoney(request.getParameter("vatAmount"));
                int vatPct = 8;
                try {
                    String vatPctStr = request.getParameter("vatPct");
                    if (vatPctStr != null && !vatPctStr.trim().isEmpty()) {
                        vatPct = Integer.parseInt(vatPctStr.trim());
                    } else {
                        vatPct = syncAndGetVAT(conn);
                    }
                } catch (Exception ignored) { vatPct = syncAndGetVAT(conn); }
                long customerPaid = parseMoney(request.getParameter("customerPaid"));
                // Voucher
                String voucherCode = request.getParameter("voucherCode");
                long voucherAmount = parseMoney(request.getParameter("voucherAmount"));
                if (voucherCode != null) voucherCode = voucherCode.trim().toUpperCase();
                String[] idSPs = request.getParameterValues("idSP[]");
                String[] qtys  = request.getParameterValues("qty[]");
                String[] mauSacs = request.getParameterValues("mauSac[]");
                String[] trongLuongs = request.getParameterValues("trongLuong[]");
                String[] donGias = request.getParameterValues("donGia[]");

                // --- CASE 1: Xử lý / Hoàn tất hóa đơn chờ ---
                if (holdId > 0) {
                    // Verify this invoice is actually pending
                    PreparedStatement psCheck = conn.prepareStatement("SELECT dh.ID_DonHang, dh.TongTien, dh.ID_KhachHang FROM DonHang dh JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID=ttdh.TrangThaiDH_ID WHERE dh.ID_DonHang=? AND ttdh.TenTrangThai=N'Chưa thanh toán'");
                    psCheck.setInt(1, holdId);
                    ResultSet rsCheck = psCheck.executeQuery();
                    if (!rsCheck.next() || idSPs == null || idSPs.length == 0) {
                        response.sendRedirect(request.getContextPath() + "/OrderServlet?view=sell&error=emptyCart");
                        return;
                    }
                    int holdKHId = rsCheck.getInt("ID_KhachHang");
                    if (idKH == 0 && holdKHId > 0) idKH = holdKHId;

                    conn.setAutoCommit(false);

                    // 1. Hoàn lại tồn kho của các chi tiết đơn hàng cũ trong hóa đơn chờ
                    PreparedStatement psRestoreCT = conn.prepareStatement("SELECT ID_SanPham, ID_BienThe, SoLuong FROM ChiTietDonHang WHERE ID_DonHang=?");
                    psRestoreCT.setInt(1, holdId);
                    ResultSet rsRestoreCT = psRestoreCT.executeQuery();
                    while (rsRestoreCT.next()) {
                        int idSPOld = rsRestoreCT.getInt("ID_SanPham");
                        int idBTOld = rsRestoreCT.getInt("ID_BienThe");
                        int slOld   = rsRestoreCT.getInt("SoLuong");
                        PreparedStatement psRestore = conn.prepareStatement(
                            "UPDATE SanPham SET TongSoLuong = TongSoLuong + ?, " +
                            "TrangThai = CASE WHEN TrangThai = N'Hết hàng' THEN N'Còn hàng' ELSE TrangThai END, " +
                            "NgayCapNhat = GETDATE() WHERE ID_SanPham = ?");
                        psRestore.setInt(1, slOld);
                        psRestore.setInt(2, idSPOld);
                        psRestore.executeUpdate();
                        if (idBTOld > 0) {
                            PreparedStatement psRestoreBT = conn.prepareStatement("UPDATE BienTheSanPham SET SoLuong = SoLuong + ? WHERE ID_BienThe = ?");
                            psRestoreBT.setInt(1, slOld);
                            psRestoreBT.setInt(2, idBTOld);
                            psRestoreBT.executeUpdate();
                        }
                    }

                    // 2. Xóa toàn bộ chi tiết cũ của hóa đơn chờ để ghi nhận lại danh sách giỏ hàng mới nhất
                    PreparedStatement psDelOld = conn.prepareStatement("DELETE FROM ChiTietDonHang WHERE ID_DonHang=?");
                    psDelOld.setInt(1, holdId);
                    psDelOld.executeUpdate();

                    // 3. Tính tổng tiền hàng mới từ giỏ hàng thực tế
                    long tongTienHang = 0;
                    long[] donGiaList = new long[idSPs.length];
                    for (int i = 0; i < idSPs.length; i++) {
                        int qty = Math.max(1, Integer.parseInt(qtys[i]));
                        int spId = Integer.parseInt(idSPs[i]);
                        long donGia = 0;
                        if (donGias != null && i < donGias.length && donGias[i] != null) {
                            donGia = parseMoney(donGias[i]);
                        }
                        String mau = (mauSacs != null && i < mauSacs.length && mauSacs[i] != null) ? mauSacs[i].trim() : "";
                        String tl  = (trongLuongs != null && i < trongLuongs.length && trongLuongs[i] != null) ? trongLuongs[i].trim() : "";
                        if (!mau.isEmpty() || !tl.isEmpty()) {
                            try (PreparedStatement psBT = conn.prepareStatement("SELECT GiaBan FROM BienTheSanPham WHERE ID_SanPham=? AND MauSac=? AND TrongLuong=?")) {
                                psBT.setInt(1, spId);
                                psBT.setString(2, mau);
                                psBT.setString(3, tl);
                                ResultSet rsBT = psBT.executeQuery();
                                if (rsBT.next() && rsBT.getLong("GiaBan") > 0) {
                                    donGia = rsBT.getLong("GiaBan");
                                }
                            }
                        }
                        PreparedStatement psSP = conn.prepareStatement("SELECT GiaBan, TongSoLuong FROM SanPham WHERE ID_SanPham=?");
                        psSP.setInt(1, spId);
                        ResultSet rsSP = psSP.executeQuery();
                        if (rsSP.next()) {
                            int tonKho = rsSP.getInt("TongSoLuong");
                            if (qty > tonKho) {
                                conn.rollback();
                                response.sendRedirect(request.getContextPath() + "/OrderServlet?view=sell&error=outOfStock");
                                return;
                            }
                            if (donGia <= 0) donGia = rsSP.getLong("GiaBan");
                            donGiaList[i] = donGia;
                            tongTienHang += donGia * qty;
                        }
                    }

                    discountAmount = Math.max(0, Math.min(discountAmount, tongTienHang));
                    voucherAmount  = Math.max(0, Math.min(voucherAmount,  tongTienHang - discountAmount));
                    long afterDiscount = tongTienHang - discountAmount - voucherAmount;
                    vatAmount = Math.max(0, vatAmount);
                    long thanhToan = afterDiscount + vatAmount;

                    if ("hold".equals(invoiceType)) {
                        customerPaid = 0;
                    } else if (customerPaid < thanhToan) {
                        customerPaid = thanhToan;
                    }
                    long tienThua = Math.max(0, customerPaid - thanhToan);
                    StringBuilder finalNote = new StringBuilder();
                    finalNote.append("paid".equals(invoiceType) ? "Hoàn tất hóa đơn chờ | " : "Hóa đơn chờ - Bán hàng tại quầy | ");
                    finalNote.append("Khách đưa: ").append(String.format("%,d", customerPaid).replace(',', '.')).append(" VNĐ");
                    finalNote.append(" | Tiền thừa: ").append(String.format("%,d", tienThua).replace(',', '.')).append(" VNĐ");
                    finalNote.append(" | Giảm giá theo hạng KH: ").append(String.format("%,d", discountAmount).replace(',', '.')).append(" VNĐ");
                    if (voucherAmount > 0 && voucherCode != null && !voucherCode.isEmpty()) {
                        finalNote.append(" | Voucher (").append(voucherCode).append("): ").append(String.format("%,d", voucherAmount).replace(',', '.')).append(" VNĐ");
                    }
                    finalNote.append(" | VAT (" + vatPct + "%): ").append(String.format("%,d", vatAmount).replace(',', '.')).append(" VNĐ");

                    // 4. Thêm danh sách sản phẩm (đầy đủ từ giỏ hàng) vào hóa đơn
                    String sqlCTNew = "INSERT INTO ChiTietDonHang (ID_DonHang, ID_SanPham, ID_BienThe, TenSP, SoLuong, DonGia, ThanhTien, GiaNhapLucBan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement psCTNew = conn.prepareStatement(sqlCTNew);
                    for (int i = 0; i < idSPs.length; i++) {
                        int spId = Integer.parseInt(idSPs[i]);
                        int qty = Math.max(1, Integer.parseInt(qtys[i]));
                        PreparedStatement rsSP2 = conn.prepareStatement("SELECT TenSP, GiaBan, GiaNhap FROM SanPham WHERE ID_SanPham=?");
                        rsSP2.setInt(1, spId);
                        ResultSet rsSPData = rsSP2.executeQuery();
                        if (rsSPData.next()) {
                            String tenSP = rsSPData.getString("TenSP");
                            long giaNhapSP = rsSPData.getLong("GiaNhap");
                            String mau = (mauSacs != null && i < mauSacs.length && mauSacs[i] != null) ? mauSacs[i].trim() : "";
                            String tl  = (trongLuongs != null && i < trongLuongs.length && trongLuongs[i] != null) ? trongLuongs[i].trim() : "";
                            Integer idBT = null;
                            long giaNhapLucBan = giaNhapSP;
                            if (!mau.isEmpty() || !tl.isEmpty()) {
                                try (PreparedStatement psGetBT = conn.prepareStatement("SELECT ID_BienThe, GiaNhap FROM BienTheSanPham WHERE ID_SanPham=? AND MauSac=? AND TrongLuong=?")) {
                                    psGetBT.setInt(1, spId); psGetBT.setString(2, mau); psGetBT.setString(3, tl);
                                    ResultSet rsGetBT = psGetBT.executeQuery();
                                    if (rsGetBT.next()) {
                                        idBT = rsGetBT.getInt("ID_BienThe");
                                        long gnBT = rsGetBT.getLong("GiaNhap");
                                        if (gnBT > 0) giaNhapLucBan = gnBT;
                                    }
                                }
                            }
                            if (!mau.isEmpty() && !tl.isEmpty()) tenSP += " (" + mau + " - " + tl + ")";
                            else if (!mau.isEmpty()) tenSP += " (" + mau + ")";
                            else if (!tl.isEmpty()) tenSP += " (" + tl + ")";
                            long donGia = donGiaList[i] > 0 ? donGiaList[i] : rsSPData.getLong("GiaBan");
                            if (giaNhapLucBan <= 0) giaNhapLucBan = (long)(donGia * 0.55);
                            psCTNew.setInt(1, holdId); psCTNew.setInt(2, spId);
                            if (idBT != null) psCTNew.setInt(3, idBT); else psCTNew.setNull(3, java.sql.Types.INTEGER);
                            psCTNew.setString(4, tenSP); psCTNew.setInt(5, qty);
                            psCTNew.setLong(6, donGia); psCTNew.setLong(7, donGia * qty);
                            psCTNew.setLong(8, giaNhapLucBan);
                            psCTNew.addBatch();
                        }
                        // Trừ lại tồn kho cho giỏ hàng mới
                        PreparedStatement psUpdate = conn.prepareStatement(
                            "UPDATE SanPham SET TongSoLuong = CASE WHEN TongSoLuong - ? < 0 THEN 0 ELSE TongSoLuong - ? END, " +
                            "TrangThai = CASE WHEN TongSoLuong - ? <= 0 THEN N'Hết hàng' ELSE TrangThai END, " +
                            "NgayCapNhat = GETDATE() WHERE ID_SanPham = ?");
                        psUpdate.setInt(1, qty); psUpdate.setInt(2, qty); psUpdate.setInt(3, qty); psUpdate.setInt(4, spId);
                        psUpdate.executeUpdate();
                        String mau2 = (mauSacs != null && i < mauSacs.length && mauSacs[i] != null) ? mauSacs[i].trim() : "";
                        String tl2  = (trongLuongs != null && i < trongLuongs.length && trongLuongs[i] != null) ? trongLuongs[i].trim() : "";
                        if (!mau2.isEmpty() || !tl2.isEmpty()) {
                            PreparedStatement psUpdateBT = conn.prepareStatement(
                                "UPDATE BienTheSanPham SET SoLuong = CASE WHEN SoLuong - ? < 0 THEN 0 ELSE SoLuong - ? END WHERE ID_SanPham = ? AND MauSac = ? AND TrongLuong = ?");
                            psUpdateBT.setInt(1, qty); psUpdateBT.setInt(2, qty); psUpdateBT.setInt(3, spId);
                            psUpdateBT.setString(4, mau2); psUpdateBT.setString(5, tl2);
                            psUpdateBT.executeUpdate();
                        }
                    }
                    psCTNew.executeBatch();

                    String loggedInEmp = (String) request.getSession().getAttribute("tenNV");
                    if (loggedInEmp == null || loggedInEmp.trim().isEmpty()) loggedInEmp = "Admin";
                    Integer loggedInEmpId = (Integer) request.getSession().getAttribute("nhanVienID");
                    if (loggedInEmpId == null || loggedInEmpId <= 0) loggedInEmpId = 1;
                    String trangThaiHold = "paid".equals(invoiceType) ? "Đã thanh toán" : "Chưa thanh toán";

                    // 5. Cập nhật hóa đơn
                    PreparedStatement psComplete = conn.prepareStatement("UPDATE DonHang SET TrangThaiDH_ID=(SELECT TrangThaiDH_ID FROM TrangThaiDonHang WHERE TenTrangThai=?), TongTien=?, PhuongThucThanhToan=?, GhiChu=?, TenNhanVien=?, ID_NhanVien=?, ID_KhachHang=? WHERE ID_DonHang=?");
                    psComplete.setString(1, trangThaiHold);
                    psComplete.setLong(2, thanhToan);
                    psComplete.setString(3, pay != null && !pay.trim().isEmpty() ? pay : ("paid".equals(invoiceType) ? "Tiền mặt" : "Không có"));
                    psComplete.setString(4, finalNote.toString());
                    psComplete.setString(5, loggedInEmp);
                    psComplete.setInt(6, loggedInEmpId);
                    if (idKH > 0) psComplete.setInt(7, idKH); else if (holdKHId > 0) psComplete.setInt(7, holdKHId); else psComplete.setNull(7, java.sql.Types.INTEGER);
                    psComplete.setInt(8, holdId);
                    psComplete.executeUpdate();

                    if ((idKH > 0 || holdKHId > 0) && "paid".equals(invoiceType)) {
                        updateCustomerRank(conn, idKH > 0 ? idKH : holdKHId);
                    }
                    conn.commit();
                    try (Connection connVC = new connectService().myConnection()) {
                        String oldVoucherCode = null;
                        try (PreparedStatement psGetOldVC = connVC.prepareStatement("SELECT v.MaVoucher FROM DonHang dh JOIN Vouchers v ON dh.ID_Voucher = v.ID_Voucher WHERE dh.ID_DonHang = ?")) {
                            psGetOldVC.setInt(1, holdId);
                            ResultSet rsOldVC = psGetOldVC.executeQuery();
                            if (rsOldVC.next()) {
                                oldVoucherCode = rsOldVC.getString("MaVoucher");
                            }
                        } catch (Exception e) {}

                        String newVoucherCode = (voucherAmount > 0 && voucherCode != null && !voucherCode.isEmpty()) ? voucherCode.trim().toLowerCase() : null;

                        if (oldVoucherCode != null && (newVoucherCode == null || !oldVoucherCode.toLowerCase().equals(newVoucherCode))) {
                            try (PreparedStatement psRestoreVC = connVC.prepareStatement("UPDATE Vouchers SET SoLuong = SoLuong + 1, DaSuDung = CASE WHEN DaSuDung > 0 THEN DaSuDung - 1 ELSE 0 END WHERE LOWER(MaVoucher) = ?")) {
                                psRestoreVC.setString(1, oldVoucherCode.toLowerCase());
                                psRestoreVC.executeUpdate();
                            } catch (Exception e) {}
                            try (PreparedStatement psUnlink = connVC.prepareStatement("UPDATE DonHang SET ID_Voucher = NULL WHERE ID_DonHang = ?")) {
                                psUnlink.setInt(1, holdId);
                                psUnlink.executeUpdate();
                            } catch (Exception e) {}
                        }

                        if (newVoucherCode != null && (oldVoucherCode == null || !oldVoucherCode.toLowerCase().equals(newVoucherCode))) {
                            try (PreparedStatement psVCUp = connVC.prepareStatement(
                                "UPDATE Vouchers SET SoLuong = CASE WHEN SoLuong > 0 THEN SoLuong - 1 ELSE 0 END, DaSuDung = DaSuDung + 1, DonHangSuDung = ISNULL(DonHangSuDung, '') + ? WHERE LOWER(MaVoucher) = ? AND SoLuong > 0")) {
                                psVCUp.setString(1, " Đơn hàng #" + holdId);
                                psVCUp.setString(2, newVoucherCode);
                                psVCUp.executeUpdate();
                                
                                try (PreparedStatement psLinkVoucher = connVC.prepareStatement("UPDATE DonHang SET ID_Voucher = (SELECT TOP 1 ID_Voucher FROM Vouchers WHERE LOWER(MaVoucher) = ?) WHERE ID_DonHang = ?")) {
                                    psLinkVoucher.setString(1, newVoucherCode);
                                    psLinkVoucher.setInt(2, holdId);
                                    psLinkVoucher.executeUpdate();
                                } catch (Exception ignored) {}
                            } catch (Exception exVC) { exVC.printStackTrace(); }
                        }
                    } catch (Exception exVC) { exVC.printStackTrace(); }
                    response.sendRedirect(request.getContextPath() + "/OrderServlet?view=sell&success=1&id=" + holdId);
                    return;
                }

                // --- CASE 2: Tạo đơn hàng mới ---
                if (idSPs == null || idSPs.length == 0) {
                    response.sendRedirect(request.getContextPath() + "/OrderServlet?view=sell&error=emptyCart");
                    return;
                }

                // Kiểm tra tồn kho và tính tổng tiền hàng
                long tongTienHang = 0;
                long[] donGiaList2 = new long[idSPs.length];
                for (int i = 0; i < idSPs.length; i++) {
                    int qty = Math.max(1, Integer.parseInt(qtys[i]));
                    int spId = Integer.parseInt(idSPs[i]);
                    long donGia = 0;
                    if (donGias != null && i < donGias.length && donGias[i] != null) {
                        donGia = parseMoney(donGias[i]);
                    }
                    String mau = (mauSacs != null && i < mauSacs.length && mauSacs[i] != null) ? mauSacs[i].trim() : "";
                    String tl  = (trongLuongs != null && i < trongLuongs.length && trongLuongs[i] != null) ? trongLuongs[i].trim() : "";
                    if (!mau.isEmpty() || !tl.isEmpty()) {
                        try (PreparedStatement psBT = conn.prepareStatement("SELECT GiaBan FROM BienTheSanPham WHERE ID_SanPham=? AND MauSac=? AND TrongLuong=?")) {
                            psBT.setInt(1, spId);
                            psBT.setString(2, mau);
                            psBT.setString(3, tl);
                            ResultSet rsBT = psBT.executeQuery();
                            if (rsBT.next() && rsBT.getLong("GiaBan") > 0) {
                                donGia = rsBT.getLong("GiaBan");
                            }
                        }
                    }
                    PreparedStatement psSP = conn.prepareStatement("SELECT GiaBan, TongSoLuong FROM SanPham WHERE ID_SanPham=?");
                    psSP.setInt(1, spId);
                    ResultSet rsSP = psSP.executeQuery();
                    if (rsSP.next()) {
                        int tonKho = rsSP.getInt("TongSoLuong");
                        if (qty > tonKho) {
                            response.sendRedirect(request.getContextPath() + "/OrderServlet?view=sell&error=outOfStock");
                            return;
                        }
                        if (donGia <= 0) donGia = rsSP.getLong("GiaBan");
                        donGiaList2[i] = donGia;
                        tongTienHang += donGia * qty;
                    }
                }

                // code cu
                // discountAmount = Math.max(0, Math.min(discountAmount, tongTienHang));
                // voucherAmount  = Math.max(0, Math.min(voucherAmount,  tongTienHang - discountAmount));
                // long afterDiscount = tongTienHang - discountAmount - voucherAmount;

                // // 👉 SAI Ở ĐÂY: Server tin tưởng và nhận thẳng vatAmount bị sai từ Client đẩy lên
                // vatAmount = Math.max(0, vatAmount); 

                // long thanhToan = afterDiscount + vatAmount;


                // code moi
                discountAmount = Math.max(0, Math.min(discountAmount, tongTienHang));
                voucherAmount  = Math.max(0, Math.min(voucherAmount,  tongTienHang - discountAmount));

                // 👉 BƯỚC 1: Tính lại số tiền chịu thuế thực tế trên Server
                long taxableBase = Math.max(0, tongTienHang - discountAmount - voucherAmount);

                // 👉 BƯỚC 2: Tính lại chính xác tiền VAT 8% trên Server (chống gian lận Client)
                vatAmount = Math.round(taxableBase * (vatPct / 100.0));

                // 👉 BƯỚC 3: Chốt tổng tiền thanh toán chuẩn lưu vào Database
                long thanhToan = taxableBase + vatAmount;




                if ("hold".equals(invoiceType)) {
                    customerPaid = 0; // Khi tạo hóa đơn chờ thì không nhận tiền Khách trả
                } else if (customerPaid < thanhToan) {
                    customerPaid = thanhToan;
                }
                long tienThua = Math.max(0, customerPaid - thanhToan);
                String customNote = request.getParameter("ghiChu");
                if (customNote != null) {
                    customNote = customNote.replaceAll(" \\| Tiền mặt", "").replaceAll(" \\| Chuyển khoản.*", "").replaceAll(" \\| Tiền thừa trả lại:.*", "");
                    customNote = customNote.replaceAll(" \\| Khách đưa:.*", "").replaceAll(" \\| VAT.*", "").replaceAll(" \\| Giảm giá.*", "").replaceAll(" \\| Voucher.*", "").trim();
                }
                if (customNote == null || customNote.isEmpty()) {
                    customNote = "paid".equals(invoiceType) ? "Bán hàng tại quầy" : "Hóa đơn chờ - Bán hàng tại quầy";
                }
                StringBuilder finalNote = new StringBuilder();
                finalNote.append(customNote).append(" | ");
                finalNote.append("Khách đưa: ").append(String.format("%,d", customerPaid).replace(',', '.')).append(" VNĐ");
                finalNote.append(" | Tiền thừa: ").append(String.format("%,d", tienThua).replace(',', '.')).append(" VNĐ");
                finalNote.append(" | Giảm giá theo hạng KH: ").append(String.format("%,d", discountAmount).replace(',', '.')).append(" VNĐ");
                if (voucherAmount > 0 && voucherCode != null && !voucherCode.isEmpty()) {
                    finalNote.append(" | Voucher (").append(voucherCode).append("): ").append(String.format("%,d", voucherAmount).replace(',', '.')).append(" VNĐ");
                }
                finalNote.append(" | VAT (" + vatPct + "%): ").append(String.format("%,d", vatAmount).replace(',', '.')).append(" VNĐ");

                // Trạng thái: hold -> "Chưa thanh toán", paid -> "Đã thanh toán"
                String trangThai = "paid".equals(invoiceType) ? "Đã thanh toán" : "Chưa thanh toán";
                String loggedInEmp = (String) request.getSession().getAttribute("tenNV");
                if (loggedInEmp == null || loggedInEmp.trim().isEmpty()) loggedInEmp = "Admin";
                Integer loggedInEmpId = (Integer) request.getSession().getAttribute("nhanVienID");
                if (loggedInEmpId == null || loggedInEmpId <= 0) loggedInEmpId = 1;

                conn.setAutoCommit(false);
                String maDH = "HD-" + System.currentTimeMillis();
                try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT ISNULL(MAX(ID_DonHang), 0) + 1 FROM DonHang")) {
                    if (rs.next()) maDH = String.format("HD-%03d", rs.getInt(1));
                } catch (Exception ignored) {}
                String sqlDH;
                PreparedStatement psDH;
                if (idKH > 0) {
                    sqlDH = "INSERT INTO DonHang (MaDonHang, ID_KhachHang, ID_NhanVien, TongTien, PhuongThucThanhToan, TrangThaiDH_ID, GhiChu, TenNhanVien, NgayTao) VALUES (?, ?, ?, ?, ?, (SELECT TrangThaiDH_ID FROM TrangThaiDonHang WHERE TenTrangThai=?), ?, ?, GETDATE())";
                    psDH = conn.prepareStatement(sqlDH, Statement.RETURN_GENERATED_KEYS);
                    psDH.setString(1, maDH); psDH.setInt(2, idKH); psDH.setInt(3, loggedInEmpId); psDH.setLong(4, thanhToan);
                    psDH.setString(5, pay != null && !pay.trim().isEmpty() ? pay : "Tiền mặt");
                    psDH.setString(6, trangThai); psDH.setString(7, finalNote.toString());
                    psDH.setString(8, loggedInEmp);
                } else {
                    // Khách vãng lai: không gán ID_KhachHang
                    sqlDH = "INSERT INTO DonHang (MaDonHang, ID_NhanVien, TongTien, PhuongThucThanhToan, TrangThaiDH_ID, GhiChu, TenNhanVien, NgayTao) VALUES (?, ?, ?, ?, (SELECT TrangThaiDH_ID FROM TrangThaiDonHang WHERE TenTrangThai=?), ?, ?, GETDATE())";
                    psDH = conn.prepareStatement(sqlDH, Statement.RETURN_GENERATED_KEYS);
                    psDH.setString(1, maDH); psDH.setInt(2, loggedInEmpId); psDH.setLong(3, thanhToan);
                    psDH.setString(4, pay != null && !pay.trim().isEmpty() ? pay : "Tiền mặt");
                    psDH.setString(5, trangThai); psDH.setString(6, finalNote.toString());
                    psDH.setString(7, loggedInEmp);
                }
                psDH.executeUpdate();

                ResultSet keys = psDH.getGeneratedKeys();
                if (!keys.next()) { conn.rollback(); throw new SQLException("Không tạo được mã hóa đơn."); }
                int idDH = keys.getInt(1);

                    String sqlCT = "INSERT INTO ChiTietDonHang (ID_DonHang, ID_SanPham, ID_BienThe, TenSP, SoLuong, DonGia, ThanhTien, GiaNhapLucBan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement psCT = conn.prepareStatement(sqlCT);
                    for (int i = 0; i < idSPs.length; i++) {
                        int spId = Integer.parseInt(idSPs[i]);
                        int qty = Math.max(1, Integer.parseInt(qtys[i]));
                        PreparedStatement rsSP2 = conn.prepareStatement("SELECT TenSP, GiaBan, GiaNhap FROM SanPham WHERE ID_SanPham=?");
                        rsSP2.setInt(1, spId);
                        ResultSet rsSPData = rsSP2.executeQuery();
                        if (rsSPData.next()) {
                            String tenSP = rsSPData.getString("TenSP");
                            long giaNhapSP = rsSPData.getLong("GiaNhap");
                            String mau = (mauSacs != null && i < mauSacs.length && mauSacs[i] != null) ? mauSacs[i].trim() : "";
                            String tl  = (trongLuongs != null && i < trongLuongs.length && trongLuongs[i] != null) ? trongLuongs[i].trim() : "";
                            Integer idBT = null;
                            long giaNhapLucBan = giaNhapSP;
                            if (!mau.isEmpty() || !tl.isEmpty()) {
                                try (PreparedStatement psGetBT = conn.prepareStatement("SELECT ID_BienThe, GiaNhap FROM BienTheSanPham WHERE ID_SanPham=? AND MauSac=? AND TrongLuong=?")) {
                                    psGetBT.setInt(1, spId); psGetBT.setString(2, mau); psGetBT.setString(3, tl);
                                    ResultSet rsGetBT = psGetBT.executeQuery();
                                    if (rsGetBT.next()) {
                                        idBT = rsGetBT.getInt("ID_BienThe");
                                        long gnBT = rsGetBT.getLong("GiaNhap");
                                        if (gnBT > 0) giaNhapLucBan = gnBT;
                                    }
                                }
                            }
                            if (!mau.isEmpty() && !tl.isEmpty()) tenSP += " (" + mau + " - " + tl + ")";
                            else if (!mau.isEmpty()) tenSP += " (" + mau + ")";
                            else if (!tl.isEmpty()) tenSP += " (" + tl + ")";
                            long donGia = donGiaList2[i] > 0 ? donGiaList2[i] : rsSPData.getLong("GiaBan");
                            if (giaNhapLucBan <= 0) giaNhapLucBan = (long)(donGia * 0.55);
                            psCT.setInt(1, idDH); psCT.setInt(2, spId);
                            if (idBT != null) psCT.setInt(3, idBT); else psCT.setNull(3, java.sql.Types.INTEGER);
                            psCT.setString(4, tenSP); psCT.setInt(5, qty);
                            psCT.setLong(6, donGia); psCT.setLong(7, donGia * qty);
                            psCT.setLong(8, giaNhapLucBan);
                            psCT.addBatch();
                        }
                        // Trừ tồn kho (kể cả hóa đơn chờ - dự trữ hàng)
                        PreparedStatement psUpdate = conn.prepareStatement(
                            "UPDATE SanPham SET TongSoLuong = CASE WHEN TongSoLuong - ? < 0 THEN 0 ELSE TongSoLuong - ? END, " +
                            "TrangThai = CASE WHEN TongSoLuong - ? <= 0 THEN N'Hết hàng' ELSE TrangThai END, " +
                            "NgayCapNhat = GETDATE() WHERE ID_SanPham = ?");
                        psUpdate.setInt(1, qty); psUpdate.setInt(2, qty); psUpdate.setInt(3, qty); psUpdate.setInt(4, spId);
                        psUpdate.executeUpdate();
                        String mau2 = (mauSacs != null && i < mauSacs.length && mauSacs[i] != null) ? mauSacs[i].trim() : "";
                        String tl2  = (trongLuongs != null && i < trongLuongs.length && trongLuongs[i] != null) ? trongLuongs[i].trim() : "";
                        if (!mau2.isEmpty() || !tl2.isEmpty()) {
                            PreparedStatement psUpdateBT = conn.prepareStatement(
                                "UPDATE BienTheSanPham SET SoLuong = CASE WHEN SoLuong - ? < 0 THEN 0 ELSE SoLuong - ? END WHERE ID_SanPham = ? AND MauSac = ? AND TrongLuong = ?");
                            psUpdateBT.setInt(1, qty); psUpdateBT.setInt(2, qty); psUpdateBT.setInt(3, spId);
                            psUpdateBT.setString(4, mau2); psUpdateBT.setString(5, tl2);
                            psUpdateBT.executeUpdate();
                        }
                    }
                    psCT.executeBatch();
                if (idKH > 0 && "paid".equals(invoiceType)) {
                    updateCustomerRank(conn, idKH);
                }
                conn.commit();

                // Trừ số lượng voucher sau khi commit thành công
                if (!"hold".equals(invoiceType) && voucherAmount > 0 && voucherCode != null && !voucherCode.isEmpty()) {
                    try (Connection connVC2 = new connectService().myConnection()) {
                        PreparedStatement psVCUp = connVC2.prepareStatement(
                            "UPDATE Vouchers SET SoLuong = CASE WHEN SoLuong > 0 THEN SoLuong - 1 ELSE 0 END, DaSuDung = DaSuDung + 1, DonHangSuDung = ? WHERE LOWER(MaVoucher) = ? AND SoLuong > 0");
                        psVCUp.setString(1, "Đơn hàng #" + idDH);
                        psVCUp.setString(2, voucherCode.trim().toLowerCase());
                        psVCUp.executeUpdate();
                        
                        try (PreparedStatement psLinkVoucher2 = connVC2.prepareStatement("UPDATE DonHang SET ID_Voucher = (SELECT TOP 1 ID_Voucher FROM Vouchers WHERE LOWER(MaVoucher) = ?) WHERE ID_DonHang = ?")) {
                            psLinkVoucher2.setString(1, voucherCode.trim().toLowerCase());
                            psLinkVoucher2.setInt(2, idDH);
                            psLinkVoucher2.executeUpdate();
                        } catch (Exception ignored) {}
                    } catch (Exception exVC) { exVC.printStackTrace(); }
                }

                if ("hold".equals(invoiceType)) {
                    response.sendRedirect(request.getContextPath() + "/OrderServlet?view=sell&success=hold&id=" + idDH);
                } else {
                    response.sendRedirect(request.getContextPath() + "/OrderServlet?view=sell&success=1&id=" + idDH);
                }
                return;



            } else if ("addCustomer".equals(action)) {
                // Thêm KH nhanh từ modal bán hàng
                String hoTen = request.getParameter("hoTen");
                String sdt   = request.getParameter("sdt");
                if (sdt != null && !sdt.trim().isEmpty()) {
                    String cleanSdt = sdt.trim().replaceAll("[\\s\\-]", "");
                    if (!cleanSdt.startsWith("0")) cleanSdt = "0" + cleanSdt;
                    sdt = cleanSdt;
                }
                String sql = "INSERT INTO KhachHang (MaKH, HoTen, SoDienThoai, Email, GioiTinh, HangKhachHang, TrangThai, NgayTao, NgayCapNhat) VALUES (?, ?, ?, ?, ?, ?, 'active', GETDATE(), GETDATE())";
                PreparedStatement ps = conn.prepareStatement(sql);
                String maKH = "KH-" + System.currentTimeMillis();
                try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT ISNULL(MAX(ID_KhachHang), 0) + 1 FROM KhachHang")) {
                    if (rs.next()) maKH = String.format("KH-%05d", rs.getInt(1));
                } catch (Exception ignored) {}
                ps.setString(1, maKH);
                ps.setString(2, hoTen);
                ps.setString(3, sdt);
                ps.setString(4, request.getParameter("email") != null ? request.getParameter("email") : "");
                ps.setString(5, "Nam");
                ps.setString(6, "Thường");
                ps.executeUpdate();
                response.sendRedirect(request.getContextPath() + "/OrderServlet?added=kh");
                return;

            } else if ("exchangeItem".equals(action)) {
                // Đổi trả hàng / Đổi size thời trang DUNA
                int idDH = Integer.parseInt(request.getParameter("idDH"));
                int idCT = Integer.parseInt(request.getParameter("idCT"));
                int idBienTheMoi = Integer.parseInt(request.getParameter("idBienTheMoi"));

                conn.setAutoCommit(false);
                try {
                    PreparedStatement psOldCT = conn.prepareStatement(
                        "SELECT ID_SanPham, ID_BienThe, TenSP, SoLuong, DonGia, ThanhTien FROM ChiTietDonHang WHERE ID_ChiTiet=? AND ID_DonHang=?");
                    psOldCT.setInt(1, idCT); psOldCT.setInt(2, idDH);
                    ResultSet rsOldCT = psOldCT.executeQuery();
                    if (!rsOldCT.next()) {
                        conn.rollback();
                        response.sendRedirect(request.getContextPath() + "/OrderServlet?error=notfound");
                        return;
                    }
                    int idSPCu = rsOldCT.getInt("ID_SanPham");
                    int idBTCu = rsOldCT.getInt("ID_BienThe");
                    String tenSPCu = rsOldCT.getString("TenSP");
                    long donGiaCu = rsOldCT.getLong("DonGia");

                    PreparedStatement psNewBT = conn.prepareStatement(
                        "SELECT bt.ID_SanPham, bt.MauSac, bt.TrongLuong, bt.GiaNhap, bt.GiaBan, bt.SoLuong, sp.TenSP " +
                        "FROM BienTheSanPham bt JOIN SanPham sp ON bt.ID_SanPham = sp.ID_SanPham WHERE bt.ID_BienThe=?");
                    psNewBT.setInt(1, idBienTheMoi);
                    ResultSet rsNewBT = psNewBT.executeQuery();
                    if (!rsNewBT.next()) {
                        conn.rollback();
                        response.sendRedirect(request.getContextPath() + "/OrderServlet?error=variantnotfound");
                        return;
                    }
                    int idSPMoi = rsNewBT.getInt("ID_SanPham");
                    String mauMoi = rsNewBT.getString("MauSac");
                    String sizeMoi = rsNewBT.getString("TrongLuong");
                    long giaNhapMoi = rsNewBT.getLong("GiaNhap");
                    long giaBanMoi = rsNewBT.getLong("GiaBan");
                    int tonKhoMoi = rsNewBT.getInt("SoLuong");
                    String baseTenMoi = rsNewBT.getString("TenSP");

                    if (tonKhoMoi < 1) {
                        conn.rollback();
                        response.sendRedirect(request.getContextPath() + "/OrderServlet?error=" + java.net.URLEncoder.encode("Biến thể size/màu mới đã hết hàng trong kho!", "UTF-8"));
                        return;
                    }

                    if (idBTCu > 0) {
                        try (PreparedStatement psInc = conn.prepareStatement("UPDATE BienTheSanPham SET SoLuong = SoLuong + 1 WHERE ID_BienThe=?")) {
                            psInc.setInt(1, idBTCu); psInc.executeUpdate();
                        }
                    }
                    if (idSPCu > 0) {
                        try (PreparedStatement psIncSP = conn.prepareStatement("UPDATE SanPham SET TongSoLuong = TongSoLuong + 1, TrangThai=CASE WHEN TrangThai=N'Hết hàng' THEN N'Còn hàng' ELSE TrangThai END WHERE ID_SanPham=?")) {
                            psIncSP.setInt(1, idSPCu); psIncSP.executeUpdate();
                        }
                    }

                    try (PreparedStatement psDec = conn.prepareStatement("UPDATE BienTheSanPham SET SoLuong = SoLuong - 1 WHERE ID_BienThe=?")) {
                        psDec.setInt(1, idBienTheMoi); psDec.executeUpdate();
                    }
                    try (PreparedStatement psDecSP = conn.prepareStatement("UPDATE SanPham SET TongSoLuong = CASE WHEN TongSoLuong - 1 < 0 THEN 0 ELSE TongSoLuong - 1 END WHERE ID_SanPham=?")) {
                        psDecSP.setInt(1, idSPMoi); psDecSP.executeUpdate();
                    }

                    String tenSPMoi = baseTenMoi + " (" + mauMoi + " - " + sizeMoi + ")";
                    long diffTien = (giaBanMoi - donGiaCu) * 1;

                    try (PreparedStatement psUpdCT = conn.prepareStatement(
                            "UPDATE ChiTietDonHang SET ID_SanPham=?, ID_BienThe=?, TenSP=?, DonGia=?, ThanhTien=(ThanhTien + ?), GiaNhapLucBan=? WHERE ID_ChiTiet=?")) {
                        psUpdCT.setInt(1, idSPMoi);
                        psUpdCT.setInt(2, idBienTheMoi);
                        psUpdCT.setString(3, tenSPMoi);
                        psUpdCT.setLong(4, giaBanMoi);
                        psUpdCT.setLong(5, diffTien);
                        psUpdCT.setLong(6, giaNhapMoi);
                        psUpdCT.setInt(7, idCT);
                        psUpdCT.executeUpdate();
                    }

                    java.text.SimpleDateFormat sdfEx = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String exNote = " | [Đổi hàng " + sdfEx.format(new java.util.Date()) + "]: Đổi từ " + tenSPCu + " sang " + tenSPMoi + (diffTien != 0 ? (". Chênh lệch: " + String.format("%,d", diffTien).replace(',', '.') + " VNĐ") : "");
                    try (PreparedStatement psUpdDH = conn.prepareStatement(
                            "UPDATE DonHang SET TongTien = TongTien + ?, GhiChu = ISNULL(GhiChu, '') + ? WHERE ID_DonHang=?")) {
                        psUpdDH.setLong(1, diffTien);
                        psUpdDH.setString(2, exNote);
                        psUpdDH.setInt(3, idDH);
                        psUpdDH.executeUpdate();
                    }

                    PreparedStatement psKHEx = conn.prepareStatement("SELECT ID_KhachHang FROM DonHang WHERE ID_DonHang=?");
                    psKHEx.setInt(1, idDH);
                    ResultSet rsKHEx = psKHEx.executeQuery();
                    if (rsKHEx.next()) {
                        int idKHEx = rsKHEx.getInt("ID_KhachHang");
                        if (idKHEx > 0) updateCustomerRank(conn, idKHEx);
                    }

                    conn.commit();
                    response.sendRedirect(request.getContextPath() + "/OrderServlet?success=exchanged");
                    return;
                } catch (Exception ex) {
                    conn.rollback();
                    ex.printStackTrace();
                    response.sendRedirect(request.getContextPath() + "/OrderServlet?error=" + java.net.URLEncoder.encode("Lỗi khi đổi hàng: " + ex.getMessage(), "UTF-8"));
                    return;
                }

            } else if ("changeStatus".equals(action)) {
                int id       = Integer.parseInt(request.getParameter("id"));
                String newSt = request.getParameter("trangThai");

                // Kiểm tra trạng thái hiện tại trong database
                String currentStatus = "";
                try (PreparedStatement psCur = conn.prepareStatement("SELECT ttdh.TenTrangThai FROM DonHang dh JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID WHERE dh.ID_DonHang=?")) {
                    psCur.setInt(1, id);
                    ResultSet rsCur = psCur.executeQuery();
                    if (rsCur.next()) currentStatus = rsCur.getString(1);
                }

                // Nếu đơn hàng đã hủy trước đó và lại nhận lệnh hủy -> bỏ qua tránh cộng dồn kho/voucher lần 2
                if ("Đã hủy".equals(currentStatus) && "Đã hủy".equals(newSt)) {
                    response.sendRedirect(request.getContextPath() + "/OrderServlet");
                    return;
                }

                // Nếu hủy đơn -> hoàn lại tồn kho (chỉ chạy khi đơn chưa bị hủy)
                if ("Đã hủy".equals(newSt) && !"Đã hủy".equals(currentStatus)) {
                    PreparedStatement psCT = conn.prepareStatement(
                        "SELECT ID_SanPham, ID_BienThe, SoLuong FROM ChiTietDonHang WHERE ID_DonHang=?");
                    psCT.setInt(1, id);
                    ResultSet rsCT = psCT.executeQuery();
                    while (rsCT.next()) {
                        int idSP = rsCT.getInt("ID_SanPham");
                        int idBT = rsCT.getInt("ID_BienThe");
                        int sl   = rsCT.getInt("SoLuong");
                        PreparedStatement psRestore = conn.prepareStatement(
                            "UPDATE SanPham SET TongSoLuong = TongSoLuong + ?" +
                            ", TrangThai = CASE WHEN TrangThai = N'Hết hàng' THEN N'Còn hàng' ELSE TrangThai END" +
                            ", NgayCapNhat = GETDATE() WHERE ID_SanPham = ?");
                        psRestore.setInt(1, sl);
                        psRestore.setInt(2, idSP);
                        psRestore.executeUpdate();
                        if (idBT > 0) {
                            PreparedStatement psRestoreBT = conn.prepareStatement("UPDATE BienTheSanPham SET SoLuong = SoLuong + ? WHERE ID_BienThe = ?");
                            psRestoreBT.setInt(1, sl);
                            psRestoreBT.setInt(2, idBT);
                            psRestoreBT.executeUpdate();
                        }
                    }
                    // Restore voucher nếu có
                    try (PreparedStatement psGetVC = conn.prepareStatement(
                            "SELECT v.MaVoucher FROM DonHang dh JOIN Vouchers v ON dh.ID_Voucher = v.ID_Voucher WHERE dh.ID_DonHang = ?")) {
                        psGetVC.setInt(1, id);
                        ResultSet rsVC = psGetVC.executeQuery();
                        if (rsVC.next()) {
                            String vcCode = rsVC.getString("MaVoucher");
                            if (vcCode != null && !vcCode.isEmpty()) {
                                try (PreparedStatement psRestVC = conn.prepareStatement(
                                        "UPDATE Vouchers SET SoLuong = SoLuong + 1, DaSuDung = CASE WHEN DaSuDung > 0 THEN DaSuDung - 1 ELSE 0 END WHERE LOWER(MaVoucher) = ?")) {
                                    psRestVC.setString(1, vcCode.trim().toLowerCase());
                                    psRestVC.executeUpdate();
                                }
                            }
                        }
                    } catch(Exception e){}
                    
                    try (PreparedStatement psUnlink = conn.prepareStatement("UPDATE DonHang SET ID_Voucher = NULL WHERE ID_DonHang = ?")) {
                        psUnlink.setInt(1, id);
                        psUnlink.executeUpdate();
                    } catch(Exception e){}
                }
                if ("Đã hủy".equals(newSt)) {
                    PreparedStatement ps = conn.prepareStatement("UPDATE DonHang SET TrangThaiDH_ID=(SELECT TrangThaiDH_ID FROM TrangThaiDonHang WHERE TenTrangThai=?), PhuongThucThanhToan=N'Không có', GhiChu=N'Hóa đơn chờ bị hủy do khách hàng hủy đơn hàng | Khách đưa: 0 VNĐ | Tiền thừa: 0 VNĐ | Giảm giá theo hạng KH: 0 VNĐ | VAT (" + syncAndGetVAT(conn) + "%): 0 VNĐ' WHERE ID_DonHang=?");
                    ps.setString(1, newSt); ps.setInt(2, id); ps.executeUpdate();
                } else {
                    PreparedStatement ps = conn.prepareStatement("UPDATE DonHang SET TrangThaiDH_ID=(SELECT TrangThaiDH_ID FROM TrangThaiDonHang WHERE TenTrangThai=?) WHERE ID_DonHang=?");
                    ps.setString(1, newSt); ps.setInt(2, id); ps.executeUpdate();
                }

                PreparedStatement psKH = conn.prepareStatement("SELECT ID_KhachHang FROM DonHang WHERE ID_DonHang=?");
                psKH.setInt(1, id);
                ResultSet rsKH = psKH.executeQuery();
                if (rsKH.next()) {
                    int idKH = rsKH.getInt("ID_KhachHang");
                    if (idKH > 0) {
                        updateCustomerRank(conn, idKH);
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        response.sendRedirect(request.getContextPath() + "/OrderServlet");
    }

    private long parseMoney(String value) {
        if (value == null || value.trim().isEmpty()) return 0;
        try {
            String cleanVal = value.trim().replaceAll("\\.", "").replaceAll(",", "");
            return Math.max(0, Math.round(Double.parseDouble(cleanVal)));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }

    private void updateCustomerRank(Connection conn, int idKH) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT ISNULL(SUM(ct.SoLuong), 0) AS TongSP " +
                "FROM ChiTietDonHang ct " +
                "JOIN DonHang dh ON ct.ID_DonHang = dh.ID_DonHang " +
                "JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                "WHERE dh.ID_KhachHang = ? AND ttdh.TenTrangThai = N'Đã thanh toán'"
            );
            ps.setInt(1, idKH);
            ResultSet rs = ps.executeQuery();
            int tongSP = 0;
            if (rs.next()) {
                tongSP = rs.getInt("TongSP");
            }
            
            String hangMoi = "Thường";
            if (tongSP >= 10) hangMoi = "VIP";
            else if (tongSP >= 5) hangMoi = "Vàng";
            else if (tongSP >= 2) hangMoi = "Bạc";
            
            PreparedStatement psUpd = conn.prepareStatement("UPDATE KhachHang SET HangKhachHang = ?, NgayCapNhat = GETDATE() WHERE ID_KhachHang = ?");
            psUpd.setString(1, hangMoi);
            psUpd.setInt(2, idKH);
            psUpd.executeUpdate();

            syncAllCustomerRanks(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncAllCustomerRanks(Connection conn) {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(
                "UPDATE k SET k.HangKhachHang = CASE " +
                "    WHEN ISNULL(O.TongSP, 0) >= 10 THEN N'VIP' " +
                "    WHEN ISNULL(O.TongSP, 0) >= 5  THEN N'Vàng' " +
                "    WHEN ISNULL(O.TongSP, 0) >= 2  THEN N'Bạc' " +
                "    ELSE N'Thường' " +
                "END " +
                "FROM KhachHang k " +
                "LEFT JOIN (" +
                "    SELECT dh.ID_KhachHang, SUM(ct.SoLuong) AS TongSP " +
                "    FROM DonHang dh " +
                "    JOIN ChiTietDonHang ct ON dh.ID_DonHang = ct.ID_DonHang " +
                "    JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                "    WHERE ttdh.TenTrangThai = N'Đã thanh toán' " +
                "    GROUP BY dh.ID_KhachHang" +
                ") O ON k.ID_KhachHang = O.ID_KhachHang"
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int syncAndGetVAT(Connection conn) {
        int vatPct = 8;
        try {
            conn.createStatement().execute("IF EXISTS (SELECT * FROM sys.tables WHERE name = 'CauHinhHeThong') DROP TABLE CauHinhHeThong");
            conn.createStatement().execute("IF EXISTS (SELECT * FROM sys.tables WHERE name = 'DanhGiaSanPham') DROP TABLE DanhGiaSanPham");
            conn.createStatement().execute("IF EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID('FK_NhanVien_TaiKhoan')) ALTER TABLE NhanVien DROP CONSTRAINT FK_NhanVien_TaiKhoan");
            conn.createStatement().execute("IF EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('NhanVien') AND name = 'TaiKhoanID') ALTER TABLE NhanVien DROP COLUMN TaiKhoanID");
            conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('SanPham') AND name = 'TyLeVat') ALTER TABLE SanPham ADD TyLeVat INT NOT NULL DEFAULT 8");
            conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('DanhMuc') AND name = 'TyLeVat') ALTER TABLE DanhMuc ADD TyLeVat INT NOT NULL DEFAULT 8");
            conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('DonHang') AND name = 'TienThueVAT') ALTER TABLE DonHang ADD TienThueVAT DECIMAL(18,2) NOT NULL DEFAULT 0");
            conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('DonHang') AND name = 'ID_Voucher') ALTER TABLE DonHang ADD ID_Voucher INT NULL");
            conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Vouchers') AND name = 'LoaiVoucher') ALTER TABLE Vouchers ADD LoaiVoucher NVARCHAR(20) NOT NULL DEFAULT N'Thường' CHECK (LoaiVoucher IN (N'Thường', N'VIP'))");
            
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT TOP 1 TyLeVat FROM SanPham")) {
                if (rs.next()) {
                    vatPct = rs.getInt("TyLeVat");
                }
            }
        } catch (Exception ignored) {}
        return vatPct;
    }

    private void syncVouchers(Connection conn) {
        try {
            try {
                conn.createStatement().execute("IF EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Vouchers') AND name = 'TenChungTrinh') EXEC sp_rename 'Vouchers.TenChungTrinh', 'TenChuongTrinh', 'COLUMN'");
                conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Vouchers') AND name = 'TenChuongTrinh') ALTER TABLE Vouchers ADD TenChuongTrinh NVARCHAR(100) DEFAULT N'Voucher trợ giá vợt & phụ kiện'");
                conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Vouchers') AND name = 'DonHangSuDung') ALTER TABLE Vouchers ADD DonHangSuDung NVARCHAR(100) NULL");
                conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Vouchers') AND name = 'TrangThai') ALTER TABLE Vouchers ADD TrangThai INT NOT NULL DEFAULT 1");
                conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Vouchers') AND name = 'DonToiThieu') ALTER TABLE Vouchers ADD DonToiThieu BIGINT NOT NULL DEFAULT 0");
                conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Vouchers') AND name = 'LoaiVoucher') ALTER TABLE Vouchers ADD LoaiVoucher NVARCHAR(20) NOT NULL DEFAULT N'Thường' CHECK (LoaiVoucher IN (N'Thường', N'VIP'))");
            } catch(Exception ignored) {}
            try {
                conn.createStatement().execute("DELETE FROM Vouchers WHERE UPPER(MaVoucher) IN ('YN100', 'YN200', 'YN300', 'YN400', 'YN500', 'SALEVIP')");
                conn.createStatement().execute("UPDATE Vouchers SET DonToiThieu = GiamGia * 5 WHERE ISNULL(DonToiThieu, 0) = 0 AND MaVoucher NOT LIKE 'VIP%'");
                conn.createStatement().execute("UPDATE Vouchers SET LoaiVoucher = N'VIP' WHERE MaVoucher LIKE 'VIP%'");
            } catch(Exception ignored) {}

            // Đã loại bỏ logic tự động sinh 250 mã voucher cũ YN100 -> YN500
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void exportReportToExcel(HttpServletResponse response, int filterYear, int filterYearTop5, int thangTop5, int filterYearDaBan, int thangDaBan,
            long totalRevenueAllTime, long totalRevenueYear, int totalSold, int totalSoldYear, int totalInventory, 
            long[] monthlyRevenue, int[] monthlySold, List<Map<String, Object>> topProducts, List<Map<String, Object>> categoryStock, List<Map<String, Object>> soldProducts) throws IOException {
        
        try (Workbook workbook = new XSSFWorkbook()) {
            DataFormat dataFormat = workbook.createDataFormat();
            
            // Font Tiêu đề chính (Title)
            Font titleFont = workbook.createFont();
            titleFont.setFontName("Arial");
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true);
            titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());

            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Font Subtitle
            Font subTitleFont = workbook.createFont();
            subTitleFont.setFontName("Arial");
            subTitleFont.setFontHeightInPoints((short) 11);
            subTitleFont.setItalic(true);
            subTitleFont.setColor(IndexedColors.GREY_80_PERCENT.getIndex());

            CellStyle subTitleStyle = workbook.createCellStyle();
            subTitleStyle.setFont(subTitleFont);
            subTitleStyle.setAlignment(HorizontalAlignment.CENTER);
            subTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Font Tiêu đề mục (Section Header)
            Font sectionFont = workbook.createFont();
            sectionFont.setFontName("Arial");
            sectionFont.setFontHeightInPoints((short) 13);
            sectionFont.setBold(true);
            sectionFont.setColor(IndexedColors.DARK_BLUE.getIndex());

            CellStyle sectionStyle = workbook.createCellStyle();
            sectionStyle.setFont(sectionFont);
            sectionStyle.setAlignment(HorizontalAlignment.LEFT);
            sectionStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Style cho Header Bảng (Table Header)
            Font headerFont = workbook.createFont();
            headerFont.setFontName("Arial");
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Style dữ liệu văn bản (Text Data)
            Font dataFont = workbook.createFont();
            dataFont.setFontName("Arial");
            dataFont.setFontHeightInPoints((short) 11);

            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setFont(dataFont);
            textStyle.setAlignment(HorizontalAlignment.LEFT);
            textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            textStyle.setBorderTop(BorderStyle.THIN);
            textStyle.setBorderBottom(BorderStyle.THIN);
            textStyle.setBorderLeft(BorderStyle.THIN);
            textStyle.setBorderRight(BorderStyle.THIN);

            // Style dữ liệu canh giữa (Center Data)
            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.cloneStyleFrom(textStyle);
            centerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle intCenterStyle = workbook.createCellStyle();
            intCenterStyle.cloneStyleFrom(centerStyle);
            intCenterStyle.setDataFormat(dataFormat.getFormat("0"));

            // Style dữ liệu số (Number Data)
            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.cloneStyleFrom(textStyle);
            numberStyle.setAlignment(HorizontalAlignment.RIGHT);
            numberStyle.setDataFormat(dataFormat.getFormat("#,##0"));

            // Style cho dòng Tổng (Total Row)
            Font totalFont = workbook.createFont();
            totalFont.setFontName("Arial");
            totalFont.setFontHeightInPoints((short) 11);
            totalFont.setBold(true);

            CellStyle totalTextStyle = workbook.createCellStyle();
            totalTextStyle.setFont(totalFont);
            totalTextStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            totalTextStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            totalTextStyle.setAlignment(HorizontalAlignment.LEFT);
            totalTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            totalTextStyle.setBorderTop(BorderStyle.THIN);
            totalTextStyle.setBorderBottom(BorderStyle.THIN);
            totalTextStyle.setBorderLeft(BorderStyle.THIN);
            totalTextStyle.setBorderRight(BorderStyle.THIN);

            CellStyle totalCenterStyle = workbook.createCellStyle();
            totalCenterStyle.cloneStyleFrom(totalTextStyle);
            totalCenterStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle totalNumberStyle = workbook.createCellStyle();
            totalNumberStyle.cloneStyleFrom(totalTextStyle);
            totalNumberStyle.setAlignment(HorizontalAlignment.RIGHT);
            totalNumberStyle.setDataFormat(dataFormat.getFormat("#,##0"));

            // =========================================================================
            // SHEET 1: TỔNG QUAN & DOANH THU
            // =========================================================================
            Sheet sheet1 = workbook.createSheet("Tổng Quan & Doanh Thu");
            sheet1.setDisplayGridlines(true);

            int rowIdx = 0;
            Row r0 = sheet1.createRow(rowIdx++);
            r0.setHeightInPoints(30);
            Cell c0_0 = r0.createCell(0);
            c0_0.setCellValue("BÁO CÁO THỐNG KÊ KINH DOANH");
            c0_0.setCellStyle(titleStyle);
            sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

            Row r1 = sheet1.createRow(rowIdx++);
            r1.setHeightInPoints(20);
            Cell c1_0 = r1.createCell(0);
            c1_0.setCellValue("Báo cáo thống kê hoạt động kinh doanh - Năm " + filterYear);
            c1_0.setCellStyle(subTitleStyle);
            sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));

            rowIdx++; // Dòng trống

            Row r3 = sheet1.createRow(rowIdx++);
            r3.setHeightInPoints(22);
            Cell c3_0 = r3.createCell(0);
            c3_0.setCellValue("1. TỔNG QUAN HOẠT ĐỘNG");
            c3_0.setCellStyle(sectionStyle);

            Row r4 = sheet1.createRow(rowIdx++);
            r4.setHeightInPoints(25);
            String[] headers1 = {"Chỉ tiêu thống kê", "Số liệu", "Đơn vị tính", "Ghi chú"};
            for (int i = 0; i < headers1.length; i++) {
                Cell c = r4.createCell(i);
                c.setCellValue(headers1[i]);
                c.setCellStyle(headerStyle);
            }

            Row r5 = sheet1.createRow(rowIdx++);
            r5.setHeightInPoints(22);
            createCell(r5, 0, "Tổng doanh thu toàn thời gian", textStyle);
            createLongCell(r5, 1, totalRevenueAllTime, numberStyle);
            createCell(r5, 2, "VNĐ", centerStyle);
            createCell(r5, 3, "Tính trên toàn bộ đơn hàng đã thanh toán", textStyle);

            Row r6 = sheet1.createRow(rowIdx++);
            r6.setHeightInPoints(22);
            createCell(r6, 0, "Tổng doanh thu năm " + filterYear, textStyle);
            createLongCell(r6, 1, totalRevenueYear, numberStyle);
            createCell(r6, 2, "VNĐ", centerStyle);
            createCell(r6, 3, "Trong năm thống kê đã chọn (" + filterYear + ")", textStyle);

            Row r7 = sheet1.createRow(rowIdx++);
            r7.setHeightInPoints(22);
            createCell(r7, 0, "Tổng sản phẩm đã bán toàn thời gian", textStyle);
            createIntegerCell(r7, 1, totalSold, numberStyle);
            createCell(r7, 2, "Sản phẩm", centerStyle);
            createCell(r7, 3, "Tính trên toàn bộ đơn hàng đã thanh toán", textStyle);

            Row r7_1 = sheet1.createRow(rowIdx++);
            r7_1.setHeightInPoints(22);
            createCell(r7_1, 0, "Tổng sản phẩm đã bán trong năm " + filterYear, textStyle);
            createIntegerCell(r7_1, 1, totalSoldYear, numberStyle);
            createCell(r7_1, 2, "Sản phẩm", centerStyle);
            createCell(r7_1, 3, "Trong năm thống kê đã chọn (" + filterYear + ")", textStyle);

            Row r8_0 = sheet1.createRow(rowIdx++);
            r8_0.setHeightInPoints(22);
            createCell(r8_0, 0, "Tổng số lượng tồn kho hiện tại", textStyle);
            createIntegerCell(r8_0, 1, totalInventory, numberStyle);
            createCell(r8_0, 2, "Sản phẩm", centerStyle);
            createCell(r8_0, 3, "Tính trên toàn bộ sản phẩm trong kho", textStyle);

            rowIdx++; // Dòng trống

            Row r8 = sheet1.createRow(rowIdx++);
            r8.setHeightInPoints(22);
            Cell c8_0 = r8.createCell(0);
            c8_0.setCellValue("2. DOANH THU & SỐ LƯỢNG BÁN THEO THÁNG TRONG NĂM " + filterYear);
            c8_0.setCellStyle(sectionStyle);

            Row r9 = sheet1.createRow(rowIdx++);
            r9.setHeightInPoints(25);
            String[] headers2 = {"Tháng", "Doanh thu (VNĐ)", "Số sản phẩm đã bán", "Tỷ trọng doanh thu (%)"};
            for (int i = 0; i < headers2.length; i++) {
                Cell c = r9.createCell(i);
                c.setCellValue(headers2[i]);
                c.setCellStyle(headerStyle);
            }

            long totalYearRevenue = 0;
            int totalYearQty = 0;
            for (int i = 0; i < 12; i++) {
                totalYearRevenue += monthlyRevenue[i];
                totalYearQty += monthlySold[i];
            }

            for (int i = 0; i < 12; i++) {
                Row r = sheet1.createRow(rowIdx++);
                r.setHeightInPoints(20);
                createCell(r, 0, "Tháng " + (i + 1), centerStyle);
                createLongCell(r, 1, monthlyRevenue[i], numberStyle);
                createIntegerCell(r, 2, monthlySold[i], numberStyle);
                double pct = (totalYearRevenue > 0) ? ((double) monthlyRevenue[i] * 100.0 / totalYearRevenue) : 0.0;
                createCell(r, 3, String.format(Locale.US, "%.2f%%", pct), centerStyle);
            }

            Row rTotal1 = sheet1.createRow(rowIdx++);
            rTotal1.setHeightInPoints(22);
            createCell(rTotal1, 0, "Tổng cộng năm " + filterYear, totalCenterStyle);
            createLongCell(rTotal1, 1, totalYearRevenue, totalNumberStyle);
            createIntegerCell(rTotal1, 2, totalYearQty, totalNumberStyle);
            createCell(rTotal1, 3, "100.00%", totalCenterStyle);

            for (int col = 0; col <= 3; col++) {
                try { sheet1.autoSizeColumn(col); } catch (Exception e) {}
                if (sheet1.getColumnWidth(col) < 4500) {
                    sheet1.setColumnWidth(col, 5500);
                }
            }

            // =========================================================================
            // SHEET 2: TOP 10 SẢN PHẨM BÁN CHẠY
            // =========================================================================
            Sheet sheet2 = workbook.createSheet("TOP 10 Sản Phẩm Bán Chạy");
            sheet2.setDisplayGridlines(true);

            int rowIdx2 = 0;
            Row s2_r0 = sheet2.createRow(rowIdx2++);
            s2_r0.setHeightInPoints(30);
            Cell s2_c0 = s2_r0.createCell(0);
            s2_c0.setCellValue("TOP 10 SẢN PHẨM BÁN CHẠY NHẤT");
            s2_c0.setCellStyle(titleStyle);
            sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            Row s2_r1 = sheet2.createRow(rowIdx2++);
            s2_r1.setHeightInPoints(20);
            Cell s2_c1 = s2_r1.createCell(0);
            s2_c1.setCellValue("Thống kê cho: Tháng " + thangTop5 + " / Năm " + filterYearTop5);
            s2_c1.setCellStyle(subTitleStyle);
            sheet2.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

            rowIdx2++; // Dòng trống

            Row s2_r3 = sheet2.createRow(rowIdx2++);
            s2_r3.setHeightInPoints(25);
            String[] headersTop5 = {"STT", "Tên sản phẩm", "Tháng/Năm", "Số lượng đã bán", "Doanh thu (VNĐ)"};
            for (int i = 0; i < headersTop5.length; i++) {
                Cell c = s2_r3.createCell(i);
                c.setCellValue(headersTop5[i]);
                c.setCellStyle(headerStyle);
            }

            int sumQtyTop5 = 0;
            long sumRevTop5 = 0;
            if (topProducts != null && !topProducts.isEmpty()) {
                for (Map<String, Object> item : topProducts) {
                    Row r = sheet2.createRow(rowIdx2++);
                    r.setHeightInPoints(22);
                    int stt = item.get("stt") != null ? ((Number) item.get("stt")).intValue() : 0;
                    String tenSP = item.get("tenSP") != null ? item.get("tenSP").toString() : "";
                    int daBan = item.get("daBan") != null ? ((Number) item.get("daBan")).intValue() : 0;
                    long doanhThu = item.get("doanhThu") != null ? ((Number) item.get("doanhThu")).longValue() : 0L;

                    createIntegerCell(r, 0, stt, intCenterStyle);
                    createCell(r, 1, tenSP, textStyle);
                    createCell(r, 2, thangTop5 + "/" + filterYearTop5, centerStyle);
                    createIntegerCell(r, 3, daBan, numberStyle);
                    createLongCell(r, 4, doanhThu, numberStyle);

                    sumQtyTop5 += daBan;
                    sumRevTop5 += doanhThu;
                }
                Row rTot2 = sheet2.createRow(rowIdx2++);
                rTot2.setHeightInPoints(22);
                createCell(rTot2, 0, "", totalTextStyle);
                createCell(rTot2, 1, "Tổng cộng TOP 5", totalTextStyle);
                createCell(rTot2, 2, "", totalCenterStyle);
                createIntegerCell(rTot2, 3, sumQtyTop5, totalNumberStyle);
                createLongCell(rTot2, 4, sumRevTop5, totalNumberStyle);
            } else {
                Row rEmpty = sheet2.createRow(rowIdx2++);
                rEmpty.setHeightInPoints(25);
                Cell cEmpty = rEmpty.createCell(0);
                cEmpty.setCellValue("Không có dữ liệu trong khoảng thời gian này");
                cEmpty.setCellStyle(centerStyle);
                sheet2.addMergedRegion(new CellRangeAddress(rEmpty.getRowNum(), rEmpty.getRowNum(), 0, 4));
            }

            for (int col = 0; col <= 4; col++) {
                try { sheet2.autoSizeColumn(col); } catch (Exception e) {}
                if (sheet2.getColumnWidth(col) < 3500) {
                    sheet2.setColumnWidth(col, 5000);
                }
            }

            // =========================================================================
            // SHEET 3: TỒN KHO THEO PHÂN LOẠI VỢT
            // =========================================================================
            Sheet sheet3 = workbook.createSheet("Tồn Kho Danh Mục Thời Trang");
            sheet3.setDisplayGridlines(true);

            int rowIdx3 = 0;
            Row s3_r0 = sheet3.createRow(rowIdx3++);
            s3_r0.setHeightInPoints(30);
            Cell s3_c0 = s3_r0.createCell(0);
            s3_c0.setCellValue("THỐNG KÊ TỒN KHO THEO DANH MỤC THỜI TRANG DUNA");
            s3_c0.setCellStyle(titleStyle);
            sheet3.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

            rowIdx3++; // Dòng trống

            Row s3_r2 = sheet3.createRow(rowIdx3++);
            s3_r2.setHeightInPoints(25);
            String[] headersStock = {"STT", "Danh mục thời trang", "Số lượng tồn (Sản phẩm)"};
            for (int i = 0; i < headersStock.length; i++) {
                Cell c = s3_r2.createCell(i);
                c.setCellValue(headersStock[i]);
                c.setCellStyle(headerStyle);
            }

            int idx3 = 1;
            int sumStock = 0;
            if (categoryStock != null && !categoryStock.isEmpty()) {
                for (Map<String, Object> cat : categoryStock) {
                    Row r = sheet3.createRow(rowIdx3++);
                    r.setHeightInPoints(22);
                    String danhMuc = cat.get("danhMuc") != null ? cat.get("danhMuc").toString() : "";
                    int soLuongTon = cat.get("soLuongTon") != null ? ((Number) cat.get("soLuongTon")).intValue() : 0;

                    createIntegerCell(r, 0, idx3++, intCenterStyle);
                    createCell(r, 1, danhMuc, textStyle);
                    createIntegerCell(r, 2, soLuongTon, numberStyle);

                    sumStock += soLuongTon;
                }
                Row rTot3 = sheet3.createRow(rowIdx3++);
                rTot3.setHeightInPoints(22);
                createCell(rTot3, 0, "", totalTextStyle);
                createCell(rTot3, 1, "Tổng cộng tồn kho", totalTextStyle);
                createIntegerCell(rTot3, 2, sumStock, totalNumberStyle);
            } else {
                Row rEmpty = sheet3.createRow(rowIdx3++);
                rEmpty.setHeightInPoints(25);
                Cell cEmpty = rEmpty.createCell(0);
                cEmpty.setCellValue("Không có dữ liệu tồn kho");
                cEmpty.setCellStyle(centerStyle);
                sheet3.addMergedRegion(new CellRangeAddress(rEmpty.getRowNum(), rEmpty.getRowNum(), 0, 2));
            }

            for (int col = 0; col <= 2; col++) {
                try { sheet3.autoSizeColumn(col); } catch (Exception e) {}
                if (sheet3.getColumnWidth(col) < 4500) {
                    sheet3.setColumnWidth(col, 6000);
                }
            }

            // =========================================================================
            // SHEET 4: DANH SÁCH SẢN PHẨM ĐÃ BÁN
            // =========================================================================
            Sheet sheet4 = workbook.createSheet("Danh Sách SP Đã Bán");
            sheet4.setDisplayGridlines(true);

            int rowIdx4 = 0;
            Row s4_r0 = sheet4.createRow(rowIdx4++);
            s4_r0.setHeightInPoints(30);
            Cell s4_c0 = s4_r0.createCell(0);
            s4_c0.setCellValue("DANH SÁCH TOÀN BỘ SẢN PHẨM ĐÃ BÁN - THÁNG " + thangDaBan + "/" + filterYearDaBan);
            s4_c0.setCellStyle(titleStyle);
            sheet4.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

            rowIdx4++; // Dòng trống

            Row s4_r2 = sheet4.createRow(rowIdx4++);
            s4_r2.setHeightInPoints(25);
            String[] headersSold = {"STT", "Mã sản phẩm", "Tên sản phẩm", "Phân loại", "Thời gian bán", "Số lượng", "Thành tiền (VNĐ)"};
            for (int i = 0; i < headersSold.length; i++) {
                Cell c = s4_r2.createCell(i);
                c.setCellValue(headersSold[i]);
                c.setCellStyle(headerStyle);
            }

            int sumQtySold = 0;
            long sumRevSold = 0;
            if (soldProducts != null && !soldProducts.isEmpty()) {
                for (Map<String, Object> item : soldProducts) {
                    Row r = sheet4.createRow(rowIdx4++);
                    r.setHeightInPoints(22);
                    int stt = item.get("stt") != null ? ((Number) item.get("stt")).intValue() : 0;
                    String maSP = item.get("maSP") != null ? item.get("maSP").toString() : "";
                    String tenSP = item.get("tenSP") != null ? item.get("tenSP").toString() : "";
                    String phanLoai = item.get("phanLoai") != null ? item.get("phanLoai").toString() : "Vợt cầu lông thường";
                    String thoiGian = item.get("thoiGian") != null ? item.get("thoiGian").toString() : "";
                    int soLuong = item.get("soLuong") != null ? ((Number) item.get("soLuong")).intValue() : 0;
                    long thanhTien = item.get("thanhTien") != null ? ((Number) item.get("thanhTien")).longValue() : 0L;

                    createIntegerCell(r, 0, stt, intCenterStyle);
                    createCell(r, 1, maSP, centerStyle);
                    createCell(r, 2, tenSP, textStyle);
                    createCell(r, 3, phanLoai, centerStyle);
                    createCell(r, 4, thoiGian, centerStyle);
                    createIntegerCell(r, 5, soLuong, numberStyle);
                    createLongCell(r, 6, thanhTien, numberStyle);

                    sumQtySold += soLuong;
                    sumRevSold += thanhTien;
                }
                Row rTot4 = sheet4.createRow(rowIdx4++);
                rTot4.setHeightInPoints(22);
                createCell(rTot4, 0, "", totalTextStyle);
                createCell(rTot4, 1, "", totalTextStyle);
                createCell(rTot4, 2, "Tổng cộng", totalTextStyle);
                createCell(rTot4, 3, "", totalCenterStyle);
                createCell(rTot4, 4, "", totalCenterStyle);
                createIntegerCell(rTot4, 5, sumQtySold, totalNumberStyle);
                createLongCell(rTot4, 6, sumRevSold, totalNumberStyle);
            } else {
                Row rEmpty = sheet4.createRow(rowIdx4++);
                rEmpty.setHeightInPoints(25);
                Cell cEmpty = rEmpty.createCell(0);
                cEmpty.setCellValue("Không có dữ liệu trong khoảng thời gian này");
                cEmpty.setCellStyle(centerStyle);
                sheet4.addMergedRegion(new CellRangeAddress(rEmpty.getRowNum(), rEmpty.getRowNum(), 0, 6));
            }

            for (int col = 0; col <= 6; col++) {
                try { sheet4.autoSizeColumn(col); } catch (Exception e) {}
                if (sheet4.getColumnWidth(col) < 4000) {
                    sheet4.setColumnWidth(col, 5500);
                }
            }

            // Ghi file ra HTTP Response
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String fileName = "Bao_Cao_Thong_Ke_" + System.currentTimeMillis() + ".xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            try (OutputStream out = response.getOutputStream()) {
                workbook.write(out);
                out.flush();
            }
        }
    }

    private void createCell(Row row, int colIdx, String value, CellStyle style) {
        Cell cell = row.createCell(colIdx);
        cell.setCellValue(value != null ? value : "");
        if (style != null) cell.setCellStyle(style);
    }

    private void createIntegerCell(Row row, int colIdx, int value, CellStyle style) {
        Cell cell = row.createCell(colIdx);
        cell.setCellValue(value);
        if (style != null) cell.setCellStyle(style);
    }

    private void createLongCell(Row row, int colIdx, long value, CellStyle style) {
        Cell cell = row.createCell(colIdx);
        cell.setCellValue((double) value);
        if (style != null) cell.setCellStyle(style);
    }

    private int calcScoreDoCung(String dc) {
        if (dc == null || dc.trim().isEmpty()) return 70;
        String s = dc.toLowerCase().trim();
        if (s.contains("siêu cứng")) return 100;
        if (s.contains("cứng trung bình") || s.contains("hơi cứng")) return 85;
        if (s.contains("cứng")) return 92;
        if (s.contains("dịu nhẹ")) return 72;
        if (s.contains("dẻo trung bình")) return 65;
        if (s.contains("dẻo")) return 60;
        return 78;
    }

    private int calcScoreLucCang(String lc) {
        if (lc == null || lc.trim().isEmpty()) return 70;
        String s = lc.toLowerCase().trim();
        if (s.contains("30") || s.contains("31") || s.contains("32") || s.contains("13.6")) return 100;
        if (s.contains("29") || s.contains("13.2")) return 94;
        if (s.contains("28") || s.contains("12.7")) return 86;
        if (s.contains("27") || s.contains("12.2")) return 78;
        if (s.contains("26") || s.contains("25") || s.contains("24") || s.contains("11")) return 60;
        return 72;
    }

    private int calcScoreVatLieu(String vl) {
        if (vl == null || vl.trim().isEmpty()) return 65;
        String s = vl.toLowerCase().trim();
        if (s.contains("namd + tungsten") || s.contains("ex-hyper") || s.contains("m40x") || s.contains("super hmg") || s.contains("copper") || s.contains("tungsten")) return 98;
        if (s.contains("namd") || s.contains("volume cut") || s.contains("ultra pe") || s.contains("pocketing")) return 90;
        if (s.contains("nanomesh") || s.contains("vibe-shield") || s.contains("ht graphite") || s.contains("rexis")) return 78;
        if (s.contains("hm graphite") || s.contains("high modulus")) return 72;
        return 60;
    }

    private int calcScoreDoiTuong(String dt) {
        if (dt == null || dt.trim().isEmpty()) return 70;
        String s = dt.toLowerCase().trim();
        if (s.contains("vđv") || (s.contains("chuyên nghiệp") && !s.contains("bán"))) return 100;
        if (s.contains("chuyên nghiệp")) return 94;
        if (s.contains("bán chuyên") && s.contains("phong trào")) return 84;
        if (s.contains("bán chuyên")) return 88;
        if (s.contains("mới chơi")) return 68;
        if (s.contains("mới tập") || s.contains("mọi")) return 65;
        return 74;
    }
}
