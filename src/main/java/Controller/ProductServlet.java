package Controller;

import connectService.connectService;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String search = request.getParameter("search");
        String cat    = request.getParameter("cat");
        String status = request.getParameter("status");

        List<Map<String, Object>> listSP = new ArrayList<>();
        connectService db = new connectService();

        try (Connection conn = db.myConnection()) {
            StringBuilder sql = new StringBuilder(
                "SELECT sp.ID_SanPham, sp.MaSP, sp.TenSP, dm.TenDanhMuc, " +
                "sp.ThuongHieu, sp.GiaBan, sp.GiaNhap, " +
                "ISNULL((SELECT SUM(bt.SoLuong) FROM BienTheSanPham bt WHERE bt.ID_SanPham = sp.ID_SanPham), sp.TongSoLuong) AS TongSoLuong, " +
                "sp.TrangThai, sp.ChatLieu, sp.FormDang, sp.HinhAnh, sp.MoTa, sp.NgayTao, sp.NgayCapNhat " +
                "FROM SanPham sp " +
                "INNER JOIN DanhMuc dm ON sp.ID_DanhMuc = dm.ID_DanhMuc " +
                "WHERE 1=1 "
            );
            List<Object> params = new ArrayList<>();

            if (search != null && !search.trim().isEmpty()) {
                sql.append("AND (sp.TenSP LIKE ? OR sp.MaSP LIKE ? OR sp.ChatLieu LIKE ? OR sp.FormDang LIKE ?) ");
                String kw = "%" + search.trim() + "%";
                params.add(kw);
                params.add(kw);
                params.add(kw);
                params.add(kw);
            }
            if (cat != null && !cat.trim().isEmpty()) {
                sql.append("AND dm.TenDanhMuc = ? ");
                params.add(cat.trim());
            }
            if (status != null && !status.trim().isEmpty()) {
                sql.append("AND sp.TrangThai = ? ");
                params.add(status.trim());
            }
            sql.append("ORDER BY sp.ID_SanPham DESC");

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> m = new HashMap<>();
                m.put("id",          rs.getInt("ID_SanPham"));
                m.put("maSP",        rs.getString("MaSP"));
                m.put("tenSP",       rs.getString("TenSP"));
                m.put("danhMuc",     rs.getString("TenDanhMuc"));
                m.put("thuongHieu",  rs.getString("ThuongHieu"));
                m.put("giaBan",      rs.getLong("GiaBan"));
                m.put("giaNhap",     rs.getLong("GiaNhap"));
                m.put("soLuong",     rs.getInt("TongSoLuong"));
                m.put("trangThai",   rs.getString("TrangThai"));
                m.put("chatLieu",    rs.getString("ChatLieu") != null ? rs.getString("ChatLieu") : "");
                m.put("formDang",    rs.getString("FormDang") != null ? rs.getString("FormDang") : "");
                m.put("hinhAnh",     rs.getString("HinhAnh") != null ? rs.getString("HinhAnh") : "");
                m.put("moTa",        rs.getString("MoTa") != null ? rs.getString("MoTa") : "");

                String ntStr = "", ncnStr = "";
                try {
                    java.sql.Timestamp ts1 = rs.getTimestamp("NgayTao");
                    if (ts1 != null) ntStr = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts1);
                    java.sql.Timestamp ts2 = rs.getTimestamp("NgayCapNhat");
                    if (ts2 != null) ncnStr = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts2);
                } catch (Exception exDate) {
                    ntStr = rs.getString("NgayTao") != null ? rs.getString("NgayTao").replace(".000", "").replace(".0", "") : "";
                    ncnStr = rs.getString("NgayCapNhat") != null ? rs.getString("NgayCapNhat").replace(".000", "").replace(".0", "") : "";
                }
                m.put("ngayTao",     ntStr);
                m.put("ngayCapNhat", ncnStr);

                listSP.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi CSDL: " + e.getMessage());
        }

        // AJAX: trả JSON biến thể
        String ajaxVariant = request.getParameter("ajaxVariant");
        if ("1".equals(ajaxVariant)) {
            String idSP = request.getParameter("idSP");
            int parsedIdSP = parseInt(idSP);
            response.setContentType("application/json;charset=UTF-8");
            if (parsedIdSP <= 0) {
                response.getWriter().write("[]");
                return;
            }
            StringBuilder json = new StringBuilder("[");
            try (Connection conn2 = new connectService().myConnection()) {
                PreparedStatement psMaSP = conn2.prepareStatement("SELECT ISNULL(MaSP, 'SP' + CAST(ID_SanPham AS VARCHAR)) FROM SanPham WHERE ID_SanPham=?");
                psMaSP.setInt(1, parsedIdSP);
                ResultSet rsMaSP = psMaSP.executeQuery();
                String spCode = rsMaSP.next() ? rsMaSP.getString(1) : ("SP" + parsedIdSP);

                PreparedStatement psBT = conn2.prepareStatement("SELECT ID_BienThe, MauSac, TrongLuong, GiaNhap, GiaBan, SoLuong, Barcode, MaBienThe FROM BienTheSanPham WHERE ID_SanPham=? ORDER BY ID_BienThe");
                psBT.setInt(1, parsedIdSP);
                ResultSet rsBT = psBT.executeQuery();
                boolean first = true;
                while (rsBT.next()) {
                    if (!first) json.append(",");
                    int idBT = rsBT.getInt("ID_BienThe");
                    String bc = rsBT.getString("Barcode");
                    if (bc == null || bc.trim().isEmpty()) {
                        String maBT = rsBT.getString("MaBienThe");
                        bc = (spCode + (maBT != null && !maBT.trim().isEmpty() ? maBT.trim() : ("BT" + idBT))).toUpperCase().replaceAll("[^A-Z0-9]", "");
                        try (PreparedStatement psUpd = conn2.prepareStatement("UPDATE BienTheSanPham SET Barcode=? WHERE ID_BienThe=?")) {
                            psUpd.setString(1, bc); psUpd.setInt(2, idBT); psUpd.executeUpdate();
                        } catch (Exception ignored) {}
                    }
                    json.append("{\"id\":").append(idBT)
                        .append(",\"mauSac\":\"").append(escape(rsBT.getString("MauSac"))).append("\"")
                        .append(",\"trongLuong\":\"").append(escape(rsBT.getString("TrongLuong"))).append("\"")
                        .append(",\"giaNhap\":").append(rsBT.getLong("GiaNhap"))
                        .append(",\"giaBan\":").append(rsBT.getLong("GiaBan"))
                        .append(",\"soLuong\":").append(rsBT.getInt("SoLuong"))
                        .append(",\"barcode\":\"").append(escape(bc)).append("\"}");
                    first = false;
                }
            } catch (Exception ex) { ex.printStackTrace(); }
            json.append("]");
            response.getWriter().write(json.toString());
            return;
        }

        // AJAX: trả JSON cho chi tiết sản phẩm và danh sách biến thể
        String ajaxDetailAll = request.getParameter("ajaxDetailAll");
        if ("1".equals(ajaxDetailAll)) {
            String idSP = request.getParameter("idSP");
            int parsedIdSP = parseInt(idSP);
            response.setContentType("application/json;charset=UTF-8");
            if (parsedIdSP <= 0) {
                response.getWriter().write("{\"variants\":[],\"specs\":[]}");
                return;
            }
            StringBuilder json = new StringBuilder("{");
            try (Connection conn3 = new connectService().myConnection()) {
                json.append("\"variants\":[");
                PreparedStatement psMaSP = conn3.prepareStatement("SELECT ISNULL(MaSP, 'SP' + CAST(ID_SanPham AS VARCHAR)) FROM SanPham WHERE ID_SanPham=?");
                psMaSP.setInt(1, parsedIdSP);
                ResultSet rsMaSP = psMaSP.executeQuery();
                String spCode = rsMaSP.next() ? rsMaSP.getString(1) : ("SP" + parsedIdSP);

                PreparedStatement psBT = conn3.prepareStatement("SELECT ID_BienThe, MauSac, TrongLuong, GiaNhap, GiaBan, SoLuong, Barcode, MaBienThe FROM BienTheSanPham WHERE ID_SanPham=? ORDER BY ID_BienThe");
                psBT.setInt(1, parsedIdSP);
                ResultSet rsBT = psBT.executeQuery();
                boolean first = true;
                while (rsBT.next()) {
                    if (!first) json.append(",");
                    int idBT = rsBT.getInt("ID_BienThe");
                    String bc = rsBT.getString("Barcode");
                    if (bc == null || bc.trim().isEmpty()) {
                        String maBT = rsBT.getString("MaBienThe");
                        bc = (spCode + (maBT != null && !maBT.trim().isEmpty() ? maBT.trim() : ("BT" + idBT))).toUpperCase().replaceAll("[^A-Z0-9]", "");
                    }
                    json.append("{\"id\":").append(idBT)
                        .append(",\"mauSac\":\"").append(escape(rsBT.getString("MauSac"))).append("\"")
                        .append(",\"trongLuong\":\"").append(escape(rsBT.getString("TrongLuong"))).append("\"")
                        .append(",\"giaNhap\":").append(rsBT.getLong("GiaNhap"))
                        .append(",\"giaBan\":").append(rsBT.getLong("GiaBan"))
                        .append(",\"soLuong\":").append(rsBT.getInt("SoLuong"))
                        .append(",\"barcode\":\"").append(escape(bc)).append("\"}");
                    first = false;
                }
                json.append("],\"specs\":[]}");
            } catch (Exception ex) { 
                ex.printStackTrace(); 
                json.append("\"variants\":[],\"specs\":[]}");
            }
            response.getWriter().write(json.toString());
            return;
        }

        // Phân trang (10 dòng/trang)
        int pageSize = 10;
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try { page = Integer.parseInt(pageParam.trim()); } catch (Exception e) { page = 1; }
        }
        if (page < 1) page = 1;
        int totalItems = listSP.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (page > totalPages && totalPages > 0) page = totalPages;
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);
        List<Map<String, Object>> pagedList = (totalItems > 0 && fromIndex < totalItems) ? listSP.subList(fromIndex, toIndex) : new ArrayList<>();

        request.setAttribute("listSP", pagedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("p_search", search != null ? search : "");
        request.setAttribute("p_cat",    cat    != null ? cat    : "");
        request.setAttribute("p_status", status != null ? status : "");
        request.getRequestDispatcher("quan-ly-san-pham.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        boolean isManager = session != null && ("MANAGER".equals(session.getAttribute("role")) || (session.getAttribute("roleID") != null && "1".equals(session.getAttribute("roleID").toString())) || "admin".equalsIgnoreCase(String.valueOf(session.getAttribute("username"))));
        if (!isManager) {
            response.sendRedirect(request.getContextPath() + "/ProductServlet?error=" + java.net.URLEncoder.encode("Nhân viên bán hàng chỉ được tra cứu kho, không có quyền chỉnh sửa sản phẩm!", "UTF-8"));
            return;
        }
        String action = request.getParameter("action");
        connectService db = new connectService();

        try (Connection conn = db.myConnection()) {
            if ("add".equals(action)) {
                String catName = request.getParameter("cat");
                int idDM = getDanhMucId(conn, catName);

                int soLuongAdd = parseInt(request.getParameter("soLuong"));
                String trangThaiAdd = soLuongAdd <= 0 ? "Hết hàng" : "Còn hàng";
                long giaBan = parseLong(request.getParameter("giaBan"));
                long giaNhap = parseLong(request.getParameter("giaNhap"));
                if (giaNhap <= 0) giaNhap = (long) (giaBan * 0.55); // Mặc định biên lợi nhuận thời trang nếu chưa nhập

                String sql = "INSERT INTO SanPham (MaSP, TenSP, ID_DanhMuc, ThuongHieu, GiaBan, GiaNhap, TongSoLuong, TrangThai, ChatLieu, FormDang, HinhAnh, MoTa, NgayTao, NgayCapNhat) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
                PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
                String maSP = "SP" + System.currentTimeMillis();
                try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT ISNULL(MAX(ID_SanPham), 0) + 1 FROM SanPham")) {
                    if (rs.next()) maSP = String.format("SP%03d", rs.getInt(1));
                } catch (Exception ignored) {}
                ps.setString(1, maSP);
                ps.setString(2, request.getParameter("tenSP"));
                ps.setInt(3, idDM);
                ps.setString(4, request.getParameter("thuongHieu") != null && !request.getParameter("thuongHieu").trim().isEmpty() ? request.getParameter("thuongHieu") : "DUNA");
                ps.setLong(5, giaBan);
                ps.setLong(6, giaNhap);
                ps.setInt(7, soLuongAdd);
                ps.setString(8, trangThaiAdd);
                ps.setString(9, request.getParameter("chatLieu"));
                ps.setString(10, request.getParameter("formDang"));
                ps.setString(11, request.getParameter("hinhAnh"));
                ps.setString(12, request.getParameter("moTa"));
                ps.executeUpdate();

                ResultSet genKeys = ps.getGeneratedKeys();
                if (genKeys.next()) {
                    int newIdSP = genKeys.getInt(1);
                    // Lưu biến thể mặc định nếu có màu sắc hoặc size
                    String mauSacNew = request.getParameter("mauSac");
                    String sizeNew = request.getParameter("trongLuong"); // Size (S, M, L, XL, ...)
                    if (mauSacNew != null && !mauSacNew.trim().isEmpty()) {
                        if (sizeNew == null || sizeNew.trim().isEmpty()) sizeNew = "L";
                        String maBTNew = "BT-001";
                        String sqlBT = "INSERT INTO BienTheSanPham (MaBienThe, ID_SanPham, MauSac, TrongLuong, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (?,?,?,?,?,?,?,?)";
                        PreparedStatement psBTNew = conn.prepareStatement(sqlBT);
                        psBTNew.setString(1, maBTNew);
                        psBTNew.setInt(2, newIdSP);
                        psBTNew.setString(3, mauSacNew);
                        psBTNew.setString(4, sizeNew);
                        psBTNew.setLong(5, giaNhap);
                        psBTNew.setLong(6, giaBan);
                        psBTNew.setInt(7, soLuongAdd);
                        String barcode = (maSP + "BT01").toUpperCase().replaceAll("[^A-Z0-9]", "");
                        psBTNew.setString(8, barcode);
                        psBTNew.executeUpdate();
                    }
                }

            } else if ("edit".equals(action)) {
                String catName = request.getParameter("cat");
                int idDM = getDanhMucId(conn, catName);
                int id = parseInt(request.getParameter("id"));
                if (id <= 0) {
                    response.sendRedirect(request.getContextPath() + "/ProductServlet");
                    return;
                }

                long giaBan = parseLong(request.getParameter("giaBan"));
                long giaNhap = parseLong(request.getParameter("giaNhap"));

                int soLuongEdit = 0;
                try (PreparedStatement psCalc = conn.prepareStatement("SELECT ISNULL(SUM(SoLuong), 0) FROM BienTheSanPham WHERE ID_SanPham = ?")) {
                    psCalc.setInt(1, id);
                    ResultSet rsCalc = psCalc.executeQuery();
                    soLuongEdit = rsCalc.next() ? rsCalc.getInt(1) : 0;
                }
                String trangThaiParam = request.getParameter("trangThai");
                if (!"Ngừng bán".equals(trangThaiParam)) {
                    trangThaiParam = soLuongEdit <= 0 ? "Hết hàng" : "Còn hàng";
                }

                String sql = "UPDATE SanPham SET TenSP=?, ID_DanhMuc=?, ThuongHieu=?, GiaBan=?, GiaNhap=?, TongSoLuong=?, TrangThai=?, ChatLieu=?, FormDang=?, HinhAnh=?, MoTa=?, NgayCapNhat=GETDATE() WHERE ID_SanPham=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, request.getParameter("tenSP"));
                ps.setInt(2, idDM);
                ps.setString(3, request.getParameter("thuongHieu"));
                ps.setLong(4, giaBan);
                ps.setLong(5, giaNhap);
                ps.setInt(6, soLuongEdit);
                ps.setString(7, trangThaiParam);
                ps.setString(8, request.getParameter("chatLieu"));
                ps.setString(9, request.getParameter("formDang"));
                ps.setString(10, request.getParameter("hinhAnh"));
                ps.setString(11, request.getParameter("moTa"));
                ps.setInt(12, id);
                ps.executeUpdate();

            } else if ("delete".equals(action)) {
                // Soft delete nếu sản phẩm đã phát sinh đơn hàng, ngược lại hard delete
                int id = Integer.parseInt(request.getParameter("id"));
                boolean hasOrders = false;
                try (PreparedStatement psCheck = conn.prepareStatement("SELECT TOP 1 1 FROM ChiTietDonHang WHERE ID_SanPham=?")) {
                    psCheck.setInt(1, id);
                    ResultSet rsCheck = psCheck.executeQuery();
                    if (rsCheck.next()) hasOrders = true;
                }

                if (hasOrders) {
                    // Soft delete: chuyển sang trạng thái "Ngừng bán"
                    try (PreparedStatement psSoft = conn.prepareStatement("UPDATE SanPham SET TrangThai=N'Ngừng bán', NgayCapNhat=GETDATE() WHERE ID_SanPham=?")) {
                        psSoft.setInt(1, id);
                        psSoft.executeUpdate();
                    }
                } else {
                    // Hard delete: xoá các biến thể trước, rồi xoá sản phẩm
                    try (PreparedStatement psDelBT = conn.prepareStatement("DELETE FROM BienTheSanPham WHERE ID_SanPham=?")) {
                        psDelBT.setInt(1, id);
                        psDelBT.executeUpdate();
                    }
                    try (PreparedStatement psDelTS = conn.prepareStatement("DELETE FROM ThongSoKyThuat WHERE ID_SanPham=?")) {
                        psDelTS.setInt(1, id);
                        psDelTS.executeUpdate();
                    }
                    try (PreparedStatement psDelSP = conn.prepareStatement("DELETE FROM SanPham WHERE ID_SanPham=?")) {
                        psDelSP.setInt(1, id);
                        psDelSP.executeUpdate();
                    }
                }

            } else if ("addVariant".equals(action)) {
                int idSP = Integer.parseInt(request.getParameter("idSP"));
                String mauSac = request.getParameter("mauSac");
                String size = request.getParameter("trongLuong"); // Size: S, M, L, XL...
                long giaBan = parseLong(request.getParameter("giaBan"));
                long giaNhap = parseLong(request.getParameter("giaNhap"));
                int soLuong = parseInt(request.getParameter("soLuong"));

                if (giaNhap <= 0) {
                    try (PreparedStatement psGia = conn.prepareStatement("SELECT GiaNhap FROM SanPham WHERE ID_SanPham=?")) {
                        psGia.setInt(1, idSP);
                        ResultSet rsGia = psGia.executeQuery();
                        if (rsGia.next()) giaNhap = rsGia.getLong(1);
                    }
                }

                String maBT = "BT" + System.currentTimeMillis();
                try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT ISNULL(MAX(ID_BienThe), 0) + 1 FROM BienTheSanPham")) {
                    if (rs.next()) maBT = String.format("BT-%03d", rs.getInt(1));
                } catch (Exception ignored) {}

                String maSP = "SP" + idSP;
                try (PreparedStatement psSP = conn.prepareStatement("SELECT MaSP FROM SanPham WHERE ID_SanPham=?")) {
                    psSP.setInt(1, idSP);
                    ResultSet rsSP = psSP.executeQuery();
                    if (rsSP.next() && rsSP.getString(1) != null) maSP = rsSP.getString(1);
                }

                String barcode = (maSP + maBT).toUpperCase().replaceAll("[^A-Z0-9]", "");

                // Check trùng lặp theo ràng buộc UQ_SanPham_Mau_Size
                boolean exists = false;
                try (PreparedStatement psDup = conn.prepareStatement("SELECT ID_BienThe, SoLuong FROM BienTheSanPham WHERE ID_SanPham=? AND MauSac=? AND TrongLuong=?")) {
                    psDup.setInt(1, idSP);
                    psDup.setString(2, mauSac);
                    psDup.setString(3, size);
                    ResultSet rsDup = psDup.executeQuery();
                    if (rsDup.next()) {
                        exists = true;
                        int idExisting = rsDup.getInt(1);
                        int oldSL = rsDup.getInt(2);
                        try (PreparedStatement psUpdExisting = conn.prepareStatement("UPDATE BienTheSanPham SET SoLuong=?, GiaBan=?, GiaNhap=? WHERE ID_BienThe=?")) {
                            psUpdExisting.setInt(1, oldSL + soLuong);
                            psUpdExisting.setLong(2, giaBan);
                            psUpdExisting.setLong(3, giaNhap);
                            psUpdExisting.setInt(4, idExisting);
                            psUpdExisting.executeUpdate();
                        }
                    }
                }

                if (!exists) {
                    String sql = "INSERT INTO BienTheSanPham (MaBienThe, ID_SanPham, MauSac, TrongLuong, GiaNhap, GiaBan, SoLuong, Barcode) VALUES (?,?,?,?,?,?,?,?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, maBT);
                    ps.setInt(2, idSP);
                    ps.setString(3, mauSac);
                    ps.setString(4, size);
                    ps.setLong(5, giaNhap);
                    ps.setLong(6, giaBan);
                    ps.setInt(7, soLuong);
                    ps.setString(8, barcode);
                    ps.executeUpdate();
                }

                // Cập nhật lại tổng số lượng SanPham
                updateProductAggregateStock(conn, idSP);
                response.sendRedirect(request.getContextPath() + "/ProductServlet");
                return;

            } else if ("deleteVariant".equals(action)) {
                int idBT = parseInt(request.getParameter("idBT"));
                if (idBT <= 0) {
                    response.sendRedirect(request.getContextPath() + "/ProductServlet");
                    return;
                }
                int idSP = 0;
                try (PreparedStatement psSel = conn.prepareStatement("SELECT ID_SanPham FROM BienTheSanPham WHERE ID_BienThe=?")) {
                    psSel.setInt(1, idBT);
                    ResultSet rsSel = psSel.executeQuery();
                    if (rsSel.next()) idSP = rsSel.getInt(1);
                }

                // Kiểm tra xem biến thể đã có trong ChiTietDonHang chưa
                boolean hasOrder = false;
                try (PreparedStatement psCheck = conn.prepareStatement("SELECT TOP 1 1 FROM ChiTietDonHang WHERE ID_BienThe=?")) {
                    psCheck.setInt(1, idBT);
                    ResultSet rs = psCheck.executeQuery();
                    if (rs.next()) hasOrder = true;
                }

                if (hasOrder) {
                    // Set số lượng về 0 nếu đã có đơn
                    try (PreparedStatement psZero = conn.prepareStatement("UPDATE BienTheSanPham SET SoLuong=0 WHERE ID_BienThe=?")) {
                        psZero.setInt(1, idBT);
                        psZero.executeUpdate();
                    }
                } else {
                    try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM BienTheSanPham WHERE ID_BienThe=?")) {
                        psDel.setInt(1, idBT);
                        psDel.executeUpdate();
                    }
                }

                if (idSP > 0) updateProductAggregateStock(conn, idSP);
                response.sendRedirect(request.getContextPath() + "/ProductServlet");
                return;

            } else if ("editVariant".equals(action)) {
                int idBT = parseInt(request.getParameter("idBT"));
                int idSP = parseInt(request.getParameter("idSP"));
                if (idBT <= 0 || idSP <= 0) {
                    response.sendRedirect(request.getContextPath() + "/ProductServlet");
                    return;
                }
                String mauSac = request.getParameter("mauSac");
                String size = request.getParameter("trongLuong");
                long giaBan = parseLong(request.getParameter("giaBan"));
                long giaNhap = parseLong(request.getParameter("giaNhap"));
                int soLuong = parseInt(request.getParameter("soLuong"));

                String sql = "UPDATE BienTheSanPham SET MauSac=?, TrongLuong=?, GiaNhap=?, GiaBan=?, SoLuong=? WHERE ID_BienThe=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, mauSac);
                ps.setString(2, size);
                ps.setLong(3, giaNhap);
                ps.setLong(4, giaBan);
                ps.setInt(5, soLuong);
                ps.setInt(6, idBT);
                ps.executeUpdate();

                updateProductAggregateStock(conn, idSP);
                response.sendRedirect(request.getContextPath() + "/ProductServlet");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/ProductServlet");
    }

    private void updateProductAggregateStock(Connection conn, int idSP) throws SQLException {
        String sql = "UPDATE SanPham SET " +
                     "TongSoLuong = ISNULL((SELECT SUM(SoLuong) FROM BienTheSanPham WHERE ID_SanPham=?), 0), " +
                     "TrangThai = CASE " +
                     "  WHEN TrangThai = N'Ngừng bán' THEN N'Ngừng bán' " +
                     "  WHEN ISNULL((SELECT SUM(SoLuong) FROM BienTheSanPham WHERE ID_SanPham=?), 0) <= 0 THEN N'Hết hàng' " +
                     "  ELSE N'Còn hàng' " +
                     "END, " +
                     "NgayCapNhat = GETDATE() " +
                     "WHERE ID_SanPham = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSP);
            ps.setInt(2, idSP);
            ps.setInt(3, idSP);
            ps.executeUpdate();
        }
    }

    private int getDanhMucId(Connection conn, String tenDM) throws SQLException {
        if (tenDM == null || tenDM.trim().isEmpty()) return 1;
        PreparedStatement ps = conn.prepareStatement("SELECT ID_DanhMuc FROM DanhMuc WHERE TenDanhMuc=?");
        ps.setString(1, tenDM.trim());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);
        // Nếu danh mục chưa có, tạo mới
        PreparedStatement psIns = conn.prepareStatement("INSERT INTO DanhMuc (TenDanhMuc, TyLeVat) VALUES (?, 8)", java.sql.Statement.RETURN_GENERATED_KEYS);
        psIns.setString(1, tenDM.trim());
        psIns.executeUpdate();
        ResultSet rsGen = psIns.getGeneratedKeys();
        return rsGen.next() ? rsGen.getInt(1) : 1;
    }

    private long parseLong(String s) {
        try { return Math.max(0, Long.parseLong(s != null ? s.trim() : "0")); } catch (Exception e) { return 0; }
    }
    private int parseInt(String s) {
        try { return Math.max(0, Integer.parseInt(s != null ? s.trim() : "0")); } catch (Exception e) { return 0; }
    }
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", " ");
    }
}
