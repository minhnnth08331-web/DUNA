package Controller;

import connectService.connectService;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

@WebServlet("/VoucherServlet")
public class VoucherServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        Integer rId = (Integer) session.getAttribute("roleID");
        String u = (String) session.getAttribute("username");
        boolean isAdmin = "MANAGER".equals(role) || (rId != null && rId == 1) || "admin".equalsIgnoreCase(u);
        if (!isAdmin) {
            response.sendRedirect(request.getContextPath() + "/OrderServlet?view=sell&error=voucher_denied");
            return;
        }
        String action = request.getParameter("action");
        if (action == null) action = "";

        connectService db = new connectService();
        try (Connection conn = db.myConnection()) {
            syncVouchers(conn);

            if ("toggleStatus".equals(action)) {
                String idStr = request.getParameter("id");
                if (idStr != null && !idStr.trim().isEmpty()) {
                    int id = 0;
                    try { id = Integer.parseInt(idStr.trim()); } catch (NumberFormatException nfe) { id = 0; }
                    if (id > 0) {
                        try (PreparedStatement ps = conn.prepareStatement("UPDATE Vouchers SET TrangThai = CASE WHEN ISNULL(TrangThai, 1) = 1 THEN 0 ELSE 1 END WHERE ID_Voucher = ?")) {
                            ps.setInt(1, id);
                            ps.executeUpdate();
                        }
                    }
                }
                response.sendRedirect(request.getContextPath() + "/VoucherServlet?success=toggled");
                return;
            }

            // Đọc bộ lọc
            String search = request.getParameter("search");
            if (search == null) search = "";
            String giamGiaFilter = request.getParameter("giamGia");
            if (giamGiaFilter == null) giamGiaFilter = "";
            String trangThaiFilter = request.getParameter("trangThai");
            if (trangThaiFilter == null) trangThaiFilter = "";

            // Xây dựng truy vấn danh sách
            StringBuilder sql = new StringBuilder("SELECT ID_Voucher, MaVoucher, ISNULL(TenChuongTrinh, N'Voucher tri ân khách hàng DUNA Men') AS TenChuongTrinh, GiamGia, ISNULL(DonToiThieu, GiamGia * 5) AS DonToiThieu, SoLuong, DaSuDung, ISNULL(DonHangSuDung, '') AS DonHangSuDung, ISNULL(TrangThai, 1) AS TrangThai, NgayTao FROM Vouchers WHERE 1=1 ");
            List<Object> params = new ArrayList<>();

            if (!search.trim().isEmpty()) {
                sql.append(" AND (MaVoucher LIKE ? OR TenChuongTrinh LIKE ? OR DonHangSuDung LIKE ?) ");
                String pattern = "%" + search.trim() + "%";
                String orderPattern = pattern;
                
                // Nếu người dùng tìm kiếm theo mã hóa đơn HD-xxx, chuyển về định dạng lưu trong DB
                if (search.trim().toUpperCase().startsWith("HD-")) {
                    try {
                        int id = Integer.parseInt(search.trim().substring(3));
                        orderPattern = "%Đơn hàng #" + id + "%";
                    } catch (Exception ignored) {}
                }
                
                params.add(pattern);
                params.add(pattern);
                params.add(orderPattern);
            }

            if (!giamGiaFilter.isEmpty() && !"ALL".equals(giamGiaFilter)) {
                try {
                    long gg = Long.parseLong(giamGiaFilter);
                    sql.append(" AND GiamGia = ? ");
                    params.add(gg);
                } catch(Exception e) {}
            }

            if ("available".equals(trangThaiFilter)) {
                sql.append(" AND SoLuong > 0 AND ISNULL(TrangThai, 1) = 1 ");
            } else if ("used".equals(trangThaiFilter)) {
                sql.append(" AND (DaSuDung > 0 OR SoLuong = 0) ");
            } else if ("locked".equals(trangThaiFilter)) {
                sql.append(" AND ISNULL(TrangThai, 1) = 0 ");
            }

            sql.append(" ORDER BY GiamGia ASC, MaVoucher ASC ");

            List<Map<String, Object>> vouchers = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(i + 1, params.get(i));
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> v = new HashMap<>();
                        v.put("ID_Voucher", rs.getInt("ID_Voucher"));
                        v.put("MaVoucher", rs.getString("MaVoucher"));
                        v.put("TenChuongTrinh", rs.getString("TenChuongTrinh"));
                        v.put("GiamGia", rs.getLong("GiamGia"));
                        v.put("DonToiThieu", rs.getLong("DonToiThieu"));
                        v.put("SoLuong", rs.getInt("SoLuong"));
                        v.put("DaSuDung", rs.getInt("DaSuDung"));
                        String dhSuDung = rs.getString("DonHangSuDung");
                        if (dhSuDung != null && dhSuDung.startsWith("Đơn hàng #")) {
                            try {
                                int idDH = Integer.parseInt(dhSuDung.replace("Đơn hàng #", "").trim());
                                dhSuDung = String.format("HD-%03d", idDH);
                            } catch (Exception ignored) {}
                        }
                        v.put("DonHangSuDung", dhSuDung);
                        v.put("TrangThai", rs.getInt("TrangThai"));
                        v.put("NgayTao", rs.getTimestamp("NgayTao"));
                        vouchers.add(v);
                    }
                }
            }

            if ("excel".equals(request.getParameter("export"))) {
                exportExcel(response, vouchers);
                return;
            }

            // Thống kê tổng quan
            int totalCount = 0;
            int availableCount = 0;
            int usedCount = 0;
            long totalDiscountGranted = 0;
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) AS total, " +
                         "SUM(CASE WHEN SoLuong > 0 AND ISNULL(TrangThai,1)=1 THEN 1 ELSE 0 END) AS avail, " +
                         "SUM(CASE WHEN DaSuDung > 0 OR SoLuong = 0 THEN 1 ELSE 0 END) AS used FROM Vouchers")) {
                if (rs.next()) {
                    totalCount = rs.getInt("total");
                    availableCount = rs.getInt("avail");
                    usedCount = rs.getInt("used");
                }
            }

            // Tính chính xác 100% tổng số tiền thực tế đã trợ giá từ tất cả các hóa đơn trong hệ thống
            try (Statement stAll = conn.createStatement();
                 ResultSet rsAll = stAll.executeQuery("SELECT MaVoucher, GiamGia, DaSuDung FROM Vouchers WHERE DaSuDung > 0")) {
                while (rsAll.next()) {
                    int daSuDung = rsAll.getInt("DaSuDung");
                    long giamGia = rsAll.getLong("GiamGia");
                    String maVoucher = rsAll.getString("MaVoucher");
                    if (giamGia > 100) {
                        totalDiscountGranted += giamGia * daSuDung;
                    } else {
                        try (PreparedStatement psDisc = conn.prepareStatement(
                                "SELECT GhiChu FROM DonHang dh JOIN TrangThaiDonHang tt ON dh.TrangThaiDH_ID = tt.TrangThaiDH_ID " +
                                "WHERE tt.TenTrangThai = N'Đã thanh toán' AND dh.GhiChu LIKE ?")) {
                            psDisc.setString(1, "%Voucher (" + maVoucher + "):%");
                            try (ResultSet rsDisc = psDisc.executeQuery()) {
                                while (rsDisc.next()) {
                                    String gc = rsDisc.getString("GhiChu");
                                    if (gc != null) {
                                        String key = "Voucher (" + maVoucher + "): ";
                                        int idxKey = gc.indexOf(key);
                                        if (idxKey != -1) {
                                            int start = idxKey + key.length();
                                            int end = gc.indexOf(" VNĐ", start);
                                            if (end != -1) {
                                                String valStr = gc.substring(start, end).replace(".", "").replace(",", "").trim();
                                                try {
                                                    totalDiscountGranted += Long.parseLong(valStr);
                                                } catch (Exception ignored) {}
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception ignored) {}
                    }
                }
            }

            request.setAttribute("vouchers", vouchers);
            request.setAttribute("stat_total", totalCount);
            request.setAttribute("stat_available", availableCount);
            request.setAttribute("stat_used", usedCount);
            request.setAttribute("stat_discount", totalDiscountGranted);
            request.setAttribute("search", search);
            request.setAttribute("giamGia", giamGiaFilter);
            request.setAttribute("trangThai", trangThaiFilter);

            request.getRequestDispatcher("quan-ly-voucher.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi CSDL: " + e.getMessage());
            request.getRequestDispatcher("quan-ly-voucher.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        Integer rId = (Integer) session.getAttribute("roleID");
        String u = (String) session.getAttribute("username");
        boolean isAdmin = "MANAGER".equals(role) || (rId != null && rId == 1) || "admin".equalsIgnoreCase(u);
        if (!isAdmin) {
            response.sendRedirect(request.getContextPath() + "/OrderServlet?view=sell&error=voucher_denied");
            return;
        }
        String action = request.getParameter("action");
        if (action == null) action = "";

        connectService db = new connectService();
        try (Connection conn = db.myConnection()) {
            syncVouchers(conn);

            if ("addCustom".equals(action)) {
                String vipNumber = request.getParameter("vipNumber");
                String ma = request.getParameter("maVoucher");
                if (vipNumber != null && !vipNumber.trim().isEmpty()) {
                    ma = "VIP" + vipNumber.trim();
                }
                String ten = request.getParameter("tenChuongTrinh");
                String giamStr = request.getParameter("giamGia");

                if (ma == null || ma.trim().isEmpty() || giamStr == null || giamStr.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/VoucherServlet?error=empty");
                    return;
                }
                ma = ma.trim().toUpperCase();
                if (!ma.startsWith("VIP")) ma = "VIP" + ma; // Đảm bảo cấu trúc VIP + chuỗi ký tự/số
                if (ten == null || ten.trim().isEmpty()) ten = "Voucher đặc quyền dành riêng cho Khách Hạng VIP";
                long giamGia = Long.parseLong(giamStr.trim().replace(".", "").replace(",", ""));
                long donToiThieu = 0;
                String donStr = request.getParameter("donToiThieu");
                if (donStr != null && !donStr.trim().isEmpty()) {
                    try { donToiThieu = Long.parseLong(donStr.trim().replace(".", "").replace(",", "")); } catch (Exception e) {}
                }
                if (donToiThieu <= 0) {
                    if (giamGia <= 100) {
                        if (giamGia == 50) donToiThieu = 5000000;
                        else if (giamGia == 45) donToiThieu = 4700000;
                        else if (giamGia == 40) donToiThieu = 4400000;
                        else if (giamGia == 35) donToiThieu = 4000000;
                        else if (giamGia == 30) donToiThieu = 3600000;
                        else donToiThieu = 3600000;
                    } else {
                        // Chiến thuật định giá mồi nhử săn mã VIP: hoàn toàn vượt trội hơn mã thường (>500k), chiết khấu nhảy vọt lên 50%
                        if (giamGia == 1500000) donToiThieu = 3000000;      // Giảm 50% (Thực trả 1tr5 - Đơn hàng siêu lời)
                        else if (giamGia == 1200000) donToiThieu = 2800000; // Giảm ~43% (Thực trả 1tr6)
                        else if (giamGia == 950000) donToiThieu = 2600000;  // Giảm ~36% (Thực trả 1tr65)
                        else if (giamGia == 750000) donToiThieu = 2400000;  // Giảm ~31% (Thực trả 1tr65)
                        else if (giamGia == 600000) donToiThieu = 2200000;  // Giảm ~27% (Thực trả 1tr6)
                        else donToiThieu = giamGia * 2;
                    }
                }
                int soLuong = 1; // Cố định 1 lượt sử dụng cho mã VIP lẻ
                if (giamGia < 0) giamGia = 0; // Không được về số âm

                // Kiểm tra trùng
                try (PreparedStatement psChk = conn.prepareStatement("SELECT COUNT(*) FROM Vouchers WHERE UPPER(MaVoucher) = ?")) {
                    psChk.setString(1, ma);
                    ResultSet rsChk = psChk.executeQuery();
                    if (rsChk.next() && rsChk.getInt(1) > 0) {
                        response.sendRedirect(request.getContextPath() + "/VoucherServlet?error=exist");
                        return;
                    }
                }

                try (PreparedStatement psIns = conn.prepareStatement(
                        "INSERT INTO Vouchers (MaVoucher, TenChuongTrinh, LoaiVoucher, GiamGia, DonToiThieu, SoLuong, DaSuDung, TrangThai, NgayTao) VALUES (?, ?, N'VIP', ?, ?, 1, 0, 1, GETDATE())")) {
                    psIns.setString(1, ma);
                    psIns.setString(2, ten.trim());
                    psIns.setLong(3, giamGia);
                    psIns.setLong(4, donToiThieu); // Điều kiện tối thiểu theo bậc thang VIP
                    psIns.executeUpdate();
                }

                response.sendRedirect(request.getContextPath() + "/VoucherServlet?success=added");
                return;
            }

            if ("generateBatch".equals(action)) {
                String prefix = request.getParameter("batchPrefix");
                String amountStr = request.getParameter("batchAmount");
                String qtyStr = request.getParameter("batchQty");
                String ten = request.getParameter("batchTen");
                if (ten == null || ten.trim().isEmpty()) ten = "Lô voucher tự động";
                long amount = Long.parseLong(amountStr.trim().replace(".", "").replace(",", ""));
                int qty = Integer.parseInt(qtyStr.trim());
                if (amount < 0) amount = 0; // Không được về số âm
                if (qty < 1) qty = 1;       // Số lượng mã tối thiểu trong lô là 1
                long donToiThieu = 0;
                if (amount <= 100) {
                    prefix = "DUNA" + amount + "P"; // Ví dụ: DUNA5P, DUNA10P, DUNA25P (P = Phần trăm)
                    if (amount == 5) donToiThieu = 800000;
                    else if (amount == 10) donToiThieu = 1500000;
                    else if (amount == 15) donToiThieu = 2200000;
                    else if (amount == 20) donToiThieu = 2700000;
                    else if (amount == 25) donToiThieu = 3100000;
                    else donToiThieu = 1500000;
                    if (ten.equals("Lô voucher tự động") || ten.trim().isEmpty()) {
                        ten = "Voucher giảm giá " + amount + "% thời trang nam DUNA";
                    }
                } else {
                    prefix = "DUNA" + (amount / 1000);
                    donToiThieu = amount * 5;
                }
                prefix = prefix.trim().toUpperCase();

                Set<String> existingCodes = new HashSet<>();
                try (PreparedStatement psChk = conn.prepareStatement("SELECT MaVoucher FROM Vouchers WHERE UPPER(MaVoucher) LIKE ?")) {
                    psChk.setString(1, prefix + "%");
                    try (ResultSet rs = psChk.executeQuery()) {
                        while (rs.next()) {
                            existingCodes.add(rs.getString(1).trim().toUpperCase());
                        }
                    }
                }

                try (PreparedStatement psIns = conn.prepareStatement("INSERT INTO Vouchers (MaVoucher, TenChuongTrinh, GiamGia, DonToiThieu, SoLuong, DaSuDung, TrangThai, NgayTao) VALUES (?, ?, ?, ?, 1, 0, 1, GETDATE())")) {
                    Random rnd = new Random();
                    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                    int added = 0;
                    int attempts = 0;
                    int batchCount = 0;
                    while (added < qty && attempts < Integer.MAX_VALUE) {
                        attempts++;
                        StringBuilder sb = new StringBuilder(5);
                        for (int i = 0; i < 5; i++) {
                            sb.append(chars.charAt(rnd.nextInt(chars.length())));
                        }
                        String code = prefix + sb.toString();
                        if (!existingCodes.contains(code)) {
                            psIns.setString(1, code);
                            psIns.setString(2, ten);
                            psIns.setLong(3, amount);
                            psIns.setLong(4, donToiThieu);
                            psIns.addBatch();
                            existingCodes.add(code);
                            added++;
                            batchCount++;
                            if (batchCount % 500 == 0 || added == qty) {
                                psIns.executeBatch();
                            }
                        }
                    }
                    if (batchCount % 500 != 0 && batchCount > 0) {
                        psIns.executeBatch();
                    }
                }
                response.sendRedirect(request.getContextPath() + "/VoucherServlet?success=batch");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/VoucherServlet?error=" + java.net.URLEncoder.encode("Lỗi xử lý: " + e.getMessage(), "UTF-8"));
            return;
        }
        response.sendRedirect(request.getContextPath() + "/VoucherServlet");
    }

    private void exportExcel(HttpServletResponse response, List<Map<String, Object>> vouchers) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh Sách Voucher");
            DataFormat dataFormat = workbook.createDataFormat();

            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setBorderBottom(BorderStyle.THIN);
            textStyle.setBorderTop(BorderStyle.THIN);
            textStyle.setBorderLeft(BorderStyle.THIN);
            textStyle.setBorderRight(BorderStyle.THIN);

            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.cloneStyleFrom(textStyle);
            centerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.cloneStyleFrom(textStyle);
            numberStyle.setDataFormat(dataFormat.getFormat("#,##0"));
            numberStyle.setAlignment(HorizontalAlignment.RIGHT);

            Row rTitle = sheet.createRow(0);
            rTitle.setHeightInPoints(35);
            Cell cTitle = rTitle.createCell(0);
            cTitle.setCellValue("DANH SÁCH VOUCHER & MÃ GIẢM GIÁ HỆ THỐNG");
            cTitle.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

            Row rHeader = sheet.createRow(2);
            rHeader.setHeightInPoints(25);
            String[] headers = {"STT", "Mã Voucher", "Tên Chương Trình", "Mệnh Giá Giảm (VNĐ)", "Điều Kiện Đơn Tối Thiểu (VNĐ)", "Số Lượt Tối Đa", "Đã Sử Dụng", "Đơn Hàng Đã Xài", "Trạng Thái"};
            for (int i = 0; i < headers.length; i++) {
                Cell c = rHeader.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }

            int rowIdx = 3;
            int stt = 1;
            for (Map<String, Object> v : vouchers) {
                Row r = sheet.createRow(rowIdx++);
                r.setHeightInPoints(20);

                Cell c0 = r.createCell(0); c0.setCellValue(stt++); c0.setCellStyle(centerStyle);
                Cell c1 = r.createCell(1); c1.setCellValue(String.valueOf(v.get("MaVoucher"))); c1.setCellStyle(centerStyle);
                Cell c2 = r.createCell(2); c2.setCellValue(String.valueOf(v.get("TenChuongTrinh"))); c2.setCellStyle(textStyle);
                long ggVal = ((Number)v.get("GiamGia")).longValue();
                Cell c3 = r.createCell(3);
                if (ggVal <= 100) {
                    c3.setCellValue(ggVal + "%"); c3.setCellStyle(centerStyle);
                } else {
                    c3.setCellValue(ggVal); c3.setCellStyle(numberStyle);
                }
                Cell c4 = r.createCell(4); c4.setCellValue(((Number)v.get("DonToiThieu")).doubleValue()); c4.setCellStyle(numberStyle);
                Cell c5 = r.createCell(5); c5.setCellValue(((Number)v.get("SoLuong")).doubleValue()); c5.setCellStyle(numberStyle);
                Cell c6 = r.createCell(6); c6.setCellValue(((Number)v.get("DaSuDung")).doubleValue()); c6.setCellStyle(numberStyle);
                
                String dh = String.valueOf(v.get("DonHangSuDung"));
                Cell c7 = r.createCell(7); c7.setCellValue(dh.equals("null") ? "" : dh); c7.setCellStyle(centerStyle);
                
                int tt = (int) v.get("TrangThai");
                int sl = (int) v.get("SoLuong");
                String ttStr = (tt == 0) ? "Đã khóa" : ((sl <= 0) ? "Đã sử dụng (Hết)" : "Sẵn sàng (Chưa dùng)");
                Cell c8 = r.createCell(8); c8.setCellValue(ttStr); c8.setCellStyle(centerStyle);
            }

            for (int i = 0; i <= 8; i++) {
                try { sheet.autoSizeColumn(i); } catch(Exception e){}
                if (sheet.getColumnWidth(i) < 4000) sheet.setColumnWidth(i, 4500);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=Danh_Sach_Voucher_" + System.currentTimeMillis() + ".xlsx");
            try (OutputStream out = response.getOutputStream()) {
                workbook.write(out);
                out.flush();
            }
        }
    }

    private void syncVouchers(Connection conn) {
        // Cấu trúc bảng Vouchers đã được cố định chuẩn trong DATN.sql
    }
}
