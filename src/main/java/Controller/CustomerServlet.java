package Controller;

import connectService.connectService;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String search = request.getParameter("search");
        String searchType = request.getParameter("searchType");
        String rank   = request.getParameter("rank");
        String status = request.getParameter("status");

        HttpSession session = request.getSession();
        if (session.getAttribute("msgSuccess") != null) {
            request.setAttribute("msgSuccess", session.getAttribute("msgSuccess"));
            session.removeAttribute("msgSuccess");
        }

        List<Map<String, Object>> listKH = new ArrayList<>();
        connectService db = new connectService();
        DecimalFormat currencyFmt = new DecimalFormat("#,###");

        try (Connection conn = db.myConnection()) {
            if (conn != null) {
                // 1. Tự động kiểm tra và hạ về hạng Thường nếu quá 3 tháng (90 ngày) không mua hàng
                try (Statement stAutoReset = conn.createStatement()) {
                    stAutoReset.executeUpdate(
                        "UPDATE k " +
                        "SET k.HangKhachHang = N'Thường', " +
                        "    k.NgayBatDauChuKy = GETDATE() " +
                        "FROM KhachHang k " +
                        "INNER JOIN (" +
                        "    SELECT dh.ID_KhachHang, MAX(dh.NgayTao) AS NgayMuaCuoi " +
                        "    FROM DonHang dh " +
                        "    JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                        "    WHERE ttdh.TenTrangThai = N'Đã thanh toán' " +
                        "    GROUP BY dh.ID_KhachHang " +
                        ") LastOrder ON k.ID_KhachHang = LastOrder.ID_KhachHang " +
                        "WHERE DATEDIFF(day, LastOrder.NgayMuaCuoi, GETDATE()) > 90 " +
                        "  AND k.HangKhachHang <> N'Thường'"
                    );
                } catch (Exception exAuto) {
                    exAuto.printStackTrace();
                }

                // 2. Đồng bộ Hạng Khách Hàng theo Số Lượng Sản Phẩm trong chu kỳ tích lũy 3 tháng cuốn chiếu
                // Thường: 0-4 SP (0%) | Bạc: 5-8 SP (Giảm 2%) | Vàng: 9-12 SP (Giảm 4%) | VIP: >= 13 SP (Giảm 7%)
                try (Statement stSync = conn.createStatement()) {
                    stSync.executeUpdate(
                        "UPDATE k SET k.HangKhachHang = CASE " +
                        "    WHEN ISNULL(O.TongSP, 0) >= 13 THEN N'VIP' " +
                        "    WHEN ISNULL(O.TongSP, 0) >= 9  THEN N'Vàng' " +
                        "    WHEN ISNULL(O.TongSP, 0) >= 5  THEN N'Bạc' " +
                        "    ELSE N'Thường' " +
                        "END " +
                        "FROM KhachHang k " +
                        "LEFT JOIN (" +
                        "    SELECT dh.ID_KhachHang, SUM(ct.SoLuong) AS TongSP " +
                        "    FROM DonHang dh " +
                        "    JOIN ChiTietDonHang ct ON dh.ID_DonHang = ct.ID_DonHang " +
                        "    JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                        "    JOIN KhachHang k2 ON dh.ID_KhachHang = k2.ID_KhachHang " +
                        "    WHERE ttdh.TenTrangThai = N'Đã thanh toán' " +
                        "      AND (k2.NgayBatDauChuKy IS NULL OR dh.NgayTao >= k2.NgayBatDauChuKy) " +
                        "    GROUP BY dh.ID_KhachHang" +
                        ") O ON k.ID_KhachHang = O.ID_KhachHang " +
                        "WHERE k.ID_KhachHang NOT IN (" +
                        "    SELECT dh.ID_KhachHang FROM DonHang dh JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                        "    WHERE ttdh.TenTrangThai = N'Đã thanh toán' GROUP BY dh.ID_KhachHang HAVING DATEDIFF(day, MAX(dh.NgayTao), GETDATE()) > 90" +
                        ")"
                    );
                } catch (Exception exSync) {
                    exSync.printStackTrace();
                }

                // 3. Truy vấn danh sách khách hàng kèm Tổng chi tiêu, Tổng SP & Số đơn mua
                StringBuilder sql = new StringBuilder(
                    "SELECT k.ID_KhachHang, k.MaKH, k.HoTen, k.SoDienThoai, k.Email, k.GioiTinh, k.NgaySinh, k.HangKhachHang, k.DiaChi, k.TrangThai, k.NgayTao, k.NgayCapNhat, k.NgayBatDauChuKy, " +
                    "ISNULL(O.TongChiTieu, 0) AS TongChiTieu, " +
                    "ISNULL(O.SoDonDaMua, 0) AS SoDonDaMua, " +
                    "ISNULL(O.TongSP, 0) AS TongSP, " +
                    "O.NgayMuaCuoi " +
                    "FROM KhachHang k " +
                    "LEFT JOIN (" +
                    "    SELECT dh.ID_KhachHang, " +
                    "           SUM(dh.TongTien) AS TongChiTieu, " +
                    "           COUNT(DISTINCT dh.ID_DonHang) AS SoDonDaMua, " +
                    "           MAX(dh.NgayTao) AS NgayMuaCuoi, " +
                    "           ISNULL(( " +
                    "               SELECT SUM(ct.SoLuong) " +
                    "               FROM ChiTietDonHang ct " +
                    "               JOIN DonHang dh2 ON ct.ID_DonHang = dh2.ID_DonHang " +
                    "               JOIN TrangThaiDonHang ttdh2 ON dh2.TrangThaiDH_ID = ttdh2.TrangThaiDH_ID " +
                    "               WHERE dh2.ID_KhachHang = dh.ID_KhachHang " +
                    "                 AND ttdh2.TenTrangThai = N'Đã thanh toán' " +
                    "                 AND (k2.NgayBatDauChuKy IS NULL OR dh2.NgayTao >= k2.NgayBatDauChuKy) " +
                    "           ), 0) AS TongSP " +
                    "    FROM DonHang dh " +
                    "    JOIN TrangThaiDonHang ttdh ON dh.TrangThaiDH_ID = ttdh.TrangThaiDH_ID " +
                    "    JOIN KhachHang k2 ON dh.ID_KhachHang = k2.ID_KhachHang " +
                    "    WHERE ttdh.TenTrangThai = N'Đã thanh toán' " +
                    "      AND (k2.NgayBatDauChuKy IS NULL OR dh.NgayTao >= k2.NgayBatDauChuKy) " +
                    "    GROUP BY dh.ID_KhachHang, k2.NgayBatDauChuKy " +
                    ") O ON k.ID_KhachHang = O.ID_KhachHang " +
                    "WHERE 1=1 ");

                List<Object> params = new ArrayList<>();
                if (search != null && !search.trim().isEmpty()) {
                    String kw = "%" + search.trim() + "%";
                    if ("maKH".equals(searchType)) {
                        sql.append("AND k.MaKH LIKE ? ");
                        params.add(kw);
                    } else if ("hoTen".equals(searchType)) {
                        sql.append("AND k.HoTen LIKE ? ");
                        params.add(kw);
                    } else if ("email".equals(searchType)) {
                        sql.append("AND k.Email LIKE ? ");
                        params.add(kw);
                    } else if ("sdt".equals(searchType)) {
                        sql.append("AND k.SoDienThoai LIKE ? ");
                        params.add(kw);
                    } else {
                        sql.append("AND (k.HoTen LIKE ? OR k.SoDienThoai LIKE ? OR k.MaKH LIKE ? OR k.Email LIKE ?) ");
                        params.add(kw); params.add(kw); params.add(kw); params.add(kw);
                    }
                }
                if (rank != null && !rank.trim().isEmpty()) { sql.append("AND k.HangKhachHang = ? "); params.add(rank.trim()); }
                if (status != null && !status.trim().isEmpty()) { sql.append("AND k.TrangThai = ? "); params.add(status.trim()); }
                sql.append("ORDER BY k.ID_KhachHang DESC");

                try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                    for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Map<String, Object> m = new HashMap<>();
                            m.put("id",        rs.getInt("ID_KhachHang"));
                            m.put("maKH",      rs.getString("MaKH") != null ? rs.getString("MaKH") : "");
                            m.put("hoTen",     rs.getString("HoTen"));
                            m.put("sdt",       rs.getString("SoDienThoai"));
                            m.put("email",     rs.getString("Email") != null ? rs.getString("Email") : "");
                            m.put("gioiTinh",  rs.getString("GioiTinh") != null ? rs.getString("GioiTinh") : "Nam");
                            m.put("ngaySinh",  rs.getString("NgaySinh") != null ? rs.getString("NgaySinh") : "");
                            m.put("hang",      rs.getString("HangKhachHang"));
                            m.put("diaChi",    rs.getString("DiaChi") != null ? rs.getString("DiaChi") : "");
                            m.put("trangThai", rs.getString("TrangThai"));
                            
                            int tongSP = rs.getInt("TongSP");
                            m.put("tongSP", tongSP);
                            double tongChiTieu = rs.getDouble("TongChiTieu");
                            m.put("tongChiTieu", tongChiTieu);
                            m.put("tongChiTieuFormatted", currencyFmt.format(tongChiTieu) + " VNĐ");
                            m.put("soDonDaMua", rs.getInt("SoDonDaMua"));

                            String ntStr = "", ncnStr = "";
                            try {
                                java.sql.Timestamp ts1 = rs.getTimestamp("NgayTao");
                                if (ts1 != null) ntStr = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(ts1);
                                java.sql.Timestamp ts2 = rs.getTimestamp("NgayCapNhat");
                                if (ts2 != null) ncnStr = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(ts2);
                            } catch (Exception exDate) {
                                ntStr = rs.getString("NgayTao") != null ? rs.getString("NgayTao").replace(".000", "").replace(".0", "") : "";
                                ncnStr = rs.getString("NgayCapNhat") != null ? rs.getString("NgayCapNhat").replace(".000", "").replace(".0", "") : "";
                            }
                            m.put("ngayTao",   ntStr);
                            m.put("ngayCapNhat", ncnStr);

                            java.sql.Timestamp tsLast = rs.getTimestamp("NgayMuaCuoi");
                            String hanResetStr = "Chưa mua đơn";
                            if (tsLast != null) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(tsLast.getTime());
                                cal.add(Calendar.MONTH, 3);
                                java.util.Date expireDate = cal.getTime();
                                java.util.Date now = new java.util.Date();
                                long diffMillis = expireDate.getTime() - now.getTime();
                                long daysLeft = diffMillis / (24L * 60 * 60 * 1000);
                                
                                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                                if (daysLeft < 0) {
                                    hanResetStr = "<span style='color: var(--negative); font-weight:600;'>Đã hết hạn</span>";
                                } else {
                                    hanResetStr = sdf.format(expireDate) + " <span style='font-size:11.5px; color:var(--muted);'>(còn " + daysLeft + " ngày)</span>";
                                }
                            } else {
                                hanResetStr = "<span style='color: var(--muted);'>3 tháng từ đơn đầu</span>";
                            }
                            m.put("hanReset", hanResetStr);

                            listKH.add(m);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi CSDL: " + e.getMessage());
        }

        // Phân trang
        int pageSize = 10;
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try { page = Integer.parseInt(pageParam.trim()); } catch (Exception e) { page = 1; }
        }
        if (page < 1) page = 1;
        int totalItems = listKH.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (page > totalPages && totalPages > 0) page = totalPages;
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);
        List<Map<String, Object>> pagedList = (totalItems > 0 && fromIndex < totalItems) ? listKH.subList(fromIndex, toIndex) : new ArrayList<>();

        request.setAttribute("listKH", pagedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("p_search", search != null ? search : "");
        request.setAttribute("p_searchType", searchType != null ? searchType : "");
        request.setAttribute("p_rank",   rank   != null ? rank   : "");
        request.setAttribute("p_status", status != null ? status : "");
        request.getRequestDispatcher("quan-ly-khach-hang.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        boolean isManager = session != null && ("MANAGER".equals(session.getAttribute("role")) || (session.getAttribute("roleID") != null && "1".equals(session.getAttribute("roleID").toString())) || "admin".equalsIgnoreCase(String.valueOf(session.getAttribute("username"))));
        if (!isManager) {
            response.sendRedirect(request.getContextPath() + "/CustomerServlet?error=" + java.net.URLEncoder.encode("Nhân viên chỉ có quyền tra cứu và xem chi tiết khách hàng, không có quyền thêm mới hay chỉnh sửa!", "UTF-8"));
            return;
        }

        String sdtParam = request.getParameter("sdt");
        if (sdtParam != null && !sdtParam.trim().isEmpty()) {
            String cleanSdt = sdtParam.trim().replaceAll("[\\s\\-]", "");
            if (!cleanSdt.startsWith("0")) {
                sdtParam = "0" + cleanSdt;
            } else {
                sdtParam = cleanSdt;
            }
        }

        connectService db = new connectService();
        try (Connection conn = db.myConnection()) {
            if (conn != null) {
                if ("add".equals(action)) {
                    if (sdtParam == null || !sdtParam.matches("^0[0-9]{8,10}$")) {
                        response.sendRedirect(request.getContextPath() + "/CustomerServlet?error=" + java.net.URLEncoder.encode("Số điện thoại không hợp lệ! Vui lòng nhập từ 9-11 chữ số bắt đầu bằng 0.", "UTF-8"));
                        return;
                    }
                    try (PreparedStatement psCheck = conn.prepareStatement("SELECT ID_KhachHang FROM KhachHang WHERE SoDienThoai = ?")) {
                        psCheck.setString(1, sdtParam);
                        try (ResultSet rsCheck = psCheck.executeQuery()) {
                            if (rsCheck.next()) {
                                response.sendRedirect(request.getContextPath() + "/CustomerServlet?error=" + java.net.URLEncoder.encode("Số điện thoại này đã được đăng ký cho khách hàng khác!", "UTF-8"));
                                return;
                            }
                        }
                    }
                    String sql = "INSERT INTO KhachHang (HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayTao, NgayCapNhat, NgayBatDauChuKy) VALUES (?, ?, ?, ?, ?, N'Thường', ?, 'active', GETDATE(), GETDATE(), GETDATE())";
                    try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, request.getParameter("hoTen"));
                        ps.setString(2, sdtParam);
                        ps.setString(3, request.getParameter("email"));
                        ps.setString(4, request.getParameter("gioiTinh"));
                        String dob = request.getParameter("ngaySinh");
                        ps.setString(5, (dob != null && !dob.isEmpty()) ? dob : null);
                        ps.setString(6, request.getParameter("diaChi"));
                        ps.executeUpdate();

                        try (ResultSet rsKeys = ps.getGeneratedKeys()) {
                            if (rsKeys.next()) {
                                int newId = rsKeys.getInt(1);
                                String maKH = String.format("KH-%05d", newId);
                                try (PreparedStatement psUp = conn.prepareStatement("UPDATE KhachHang SET MaKH = ? WHERE ID_KhachHang = ?")) {
                                    psUp.setString(1, maKH);
                                    psUp.setInt(2, newId);
                                    psUp.executeUpdate();
                                }
                            }
                        }
                    }
                } else if ("edit".equals(action)) {
                    String idStr = request.getParameter("id");
                    int id = 0;
                    try {
                        if (idStr != null && !idStr.trim().isEmpty()) id = Integer.parseInt(idStr.trim());
                    } catch (NumberFormatException nfe) { id = 0; }
                    if (id <= 0) {
                        response.sendRedirect(request.getContextPath() + "/CustomerServlet");
                        return;
                    }
                    String sql = "UPDATE KhachHang SET HoTen=?, SoDienThoai=?, Email=?, GioiTinh=?, NgaySinh=?, DiaChi=?, NgayCapNhat=GETDATE() WHERE ID_KhachHang=?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, request.getParameter("hoTen"));
                        ps.setString(2, sdtParam);
                        ps.setString(3, request.getParameter("email"));
                        ps.setString(4, request.getParameter("gioiTinh"));
                        String dob = request.getParameter("ngaySinh");
                        ps.setString(5, (dob != null && !dob.isEmpty()) ? dob : null);
                        ps.setString(6, request.getParameter("diaChi"));
                        ps.setInt(7, id);
                        ps.executeUpdate();
                    }
                } else if ("lock".equals(action)) {
                    String idStr = request.getParameter("id");
                    int id = 0;
                    try {
                        if (idStr != null && !idStr.trim().isEmpty()) id = Integer.parseInt(idStr.trim());
                    } catch (NumberFormatException nfe) { id = 0; }
                    if (id <= 0) {
                        response.sendRedirect(request.getContextPath() + "/CustomerServlet");
                        return;
                    }
                    String cur = "active";
                    try (PreparedStatement psStatus = conn.prepareStatement("SELECT TrangThai FROM KhachHang WHERE ID_KhachHang=?")) {
                        psStatus.setInt(1, id);
                        try (ResultSet rs = psStatus.executeQuery()) {
                            if (rs.next()) cur = rs.getString(1);
                        }
                    }
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE KhachHang SET TrangThai=?, NgayCapNhat=GETDATE() WHERE ID_KhachHang=?")) {
                        ps.setString(1, "active".equals(cur) ? "locked" : "active");
                        ps.setInt(2, id);
                        ps.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/CustomerServlet");
    }
}
