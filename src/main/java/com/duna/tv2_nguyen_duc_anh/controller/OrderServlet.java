package com.duna.tv2_nguyen_duc_anh.controller;

import com.duna.common.uc.Tv2UcDispatcher;
import com.duna.common.uc.UcRegistry;
import com.duna.common.util.BaseServlet;
import com.duna.common.util.ExcelExportUtil;
import com.duna.common.util.RequestUtil;
import com.duna.tv1_nguyen_ngoc_minh.service.CustomerService;
import com.duna.tv2_nguyen_duc_anh.dao.OrderDAO;
import com.duna.tv2_nguyen_duc_anh.service.OrderReturnService;
import com.duna.tv3_le_minh_bach.service.PromotionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller M06 - Ban hang POS, So Hoa don & Bao cao thong ke DUNA (UC126 - UC146).
 * Tuong thich 100% giao dien ban-hang.jsp, quan-ly-hoa-don.jsp, bao-cao-thong-ke.jsp.
 * Tac gia: Nguyen Duc Anh - TH08308
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/OrderServlet"})
public class OrderServlet extends BaseServlet {
    private final OrderReturnService orderService = new OrderReturnService();
    private final OrderDAO orderDAO = new OrderDAO();
    private final CustomerService customerService = new CustomerService();
    private final PromotionService promotionService = new PromotionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = RequestUtil.getString(req, "action", "");
        String view = RequestUtil.getString(req, "view", "");

        if ("1".equals(req.getParameter("ajaxDetail"))) {
            long oid = RequestUtil.getLong(req, "id", 0L);
            Map<String, Object> detail = orderDAO.getClassicOrderDetailJson(oid);
            if (detail == null) {
                detail = new LinkedHashMap<>();
                detail.put("error", "Khong tim thay don hang #" + oid);
            }
            writeJson(resp, 200, detail);
            return;
        }

        if ("uc34".equalsIgnoreCase(action) || "1".equals(req.getParameter("ajaxAddKH"))) {
            String name = RequestUtil.getString(req, "name", "");
            String rawPhone = RequestUtil.getString(req, "phone", "");
            String email = RequestUtil.getString(req, "email", "");
            String phone = RequestUtil.normalizePhone(rawPhone);
            Map<String, Object> res = new LinkedHashMap<>();
            if (name.isEmpty() || rawPhone.isEmpty()) {
                res.put("success", Boolean.FALSE);
                res.put("message", "Vui lòng nhập họ tên và số điện thoại!");
                writeJson(resp, 200, res);
                return;
            }
            if (!RequestUtil.isValidVnPhone(phone)) {
                res.put("success", Boolean.FALSE);
                res.put("message", "Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0 và gồm đúng 10 chữ số (đầu số 03, 05, 07, 08, 09).");
                writeJson(resp, 200, res);
                return;
            }
            Map<String, Object> existedRow = customerService.findByExactPhone(phone);
            boolean existed = existedRow != null;
            if (!existed) {
                int created = customerService.addCustomerFull(name, phone, email, "MALE", "", "");
                if (created <= 0) {
                    res.put("success", Boolean.FALSE);
                    res.put("message", "Không thể thêm khách hàng (SĐT không hợp lệ hoặc đã tồn tại).");
                    writeJson(resp, 200, res);
                    return;
                }
                existedRow = customerService.findByExactPhone(phone);
            }
            int cid = existedRow == null ? 0 : ((Number) existedRow.get("customer_id")).intValue();
            String finalTen = existedRow == null ? name : String.valueOf(existedRow.get("full_name"));
            String finalHang = existedRow == null ? "Thành Viên" : String.valueOf(existedRow.get("tier_name"));
            int finalDisc = (existedRow != null && existedRow.get("discount_percent") instanceof Number)
                    ? ((Number) existedRow.get("discount_percent")).intValue() : 0;
            res.put("success", Boolean.TRUE);
            res.put("id", cid);
            res.put("idKhachHang", cid);
            res.put("ten", finalTen);
            res.put("hoVaTen", finalTen);
            res.put("sdt", phone);
            res.put("soDienThoai", phone);
            res.put("hang", finalHang);
            res.put("discountPct", finalDisc);
            res.put("existed", existed);
            writeJson(resp, 200, res);
            return;
        }

        if ("uc36".equalsIgnoreCase(action) || "1".equals(req.getParameter("ajaxCheckVoucher"))) {
            String code = RequestUtil.getString(req, "code", "").toUpperCase();
            double orderAmt = RequestUtil.getDouble(req, "subtotal",
                    RequestUtil.getDouble(req, "amount", RequestUtil.getDouble(req, "orderAmount", 0)));
            int customerId = RequestUtil.getInt(req, "customerId", RequestUtil.getInt(req, "idKH", 0));
            Integer customerUserId = null;
            if (customerId > 0) {
                Map<String, Object> cDetail = customerService.getCustomerDetail(customerId);
                if (cDetail != null && cDetail.get("user_id") instanceof Number) {
                    customerUserId = ((Number) cDetail.get("user_id")).intValue();
                }
            }
            Map<String, Object> res = promotionService.validateVoucherDetailed(code, orderAmt, customerUserId);
            writeJson(resp, 200, res);
            return;
        }

        if ("1".equals(req.getParameter("ajaxLookupVouchers"))) {
            String vSearch = RequestUtil.getString(req, "search", "");
            String vStatus = RequestUtil.getString(req, "status", "");
            List<Map<String, Object>> vRaw = promotionService.listClassicVouchers(vSearch, "", "ALL".equalsIgnoreCase(vStatus) ? "" : vStatus);
            List<Map<String, Object>> outList = new ArrayList<>();
            for (Map<String, Object> v : vRaw) {
                String loai = String.valueOf(v.get("LoaiGiam"));
                long giamVal = v.get("GiamGia") instanceof Number ? ((Number) v.get("GiamGia")).longValue() : 0L;
                long maxCap = v.get("GiamToiDa") instanceof Number ? ((Number) v.get("GiamToiDa")).longValue() : 0L;
                long minOrder = v.get("DonToiThieu") instanceof Number ? ((Number) v.get("DonToiThieu")).longValue() : 0L;
                int stock = v.get("SoLuong") instanceof Number ? ((Number) v.get("SoLuong")).intValue() : 0;
                int used = v.get("DaSuDung") instanceof Number ? ((Number) v.get("DaSuDung")).intValue() : 0;
                int remaining = v.get("ConLai") instanceof Number ? ((Number) v.get("ConLai")).intValue() : 0;
                String statusCode = v.get("TrangThaiCode") != null ? String.valueOf(v.get("TrangThaiCode")) : "available";

                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", v.get("ID_Voucher"));
                item.put("code", v.get("MaVoucher"));
                item.put("title", v.get("TenChuongTrinh"));
                item.put("discountType", loai);
                item.put("amount", giamVal);
                item.put("maxCap", "PERCENT".equalsIgnoreCase(loai) ? maxCap : 0L);
                item.put("discount", "PERCENT".equalsIgnoreCase(loai)
                        ? "Giảm " + giamVal + "%"
                        : "Giảm " + giamVal + " đ");
                item.put("minOrder", minOrder);
                item.put("stock", stock);
                item.put("used", used);
                item.put("remaining", remaining);
                item.put("status", statusCode);
                item.put("startDate", v.get("NgayBatDau"));
                item.put("endDate", v.get("NgayKetThuc"));
                outList.add(item);
            }
            Map<String, Object> res = new LinkedHashMap<>();
            res.put("success", Boolean.TRUE);
            res.put("vouchers", outList);
            writeJson(resp, 200, res);
            return;
        }

        if ("sell".equalsIgnoreCase(view)) {
            req.setAttribute("listKH", orderDAO.listPosCustomers());
            req.setAttribute("listSP", orderDAO.listPosProducts());
            req.setAttribute("currentVat", orderDAO.getVatRate());
            long holdId = RequestUtil.getLong(req, "holdId", 0L);
            if (holdId > 0) {
                req.setAttribute("holdId", holdId);
                req.setAttribute("holdInfo", orderDAO.getHoldOrderInfo(holdId));
            }
            forwardView(req, resp, "/ban-hang.jsp");
            return;
        }

        if ("report".equalsIgnoreCase(view)) {
            int curYear = Calendar.getInstance().get(Calendar.YEAR);
            int curMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int filterYear = RequestUtil.getInt(req, "nam", curYear);
            int filterYearTop5 = RequestUtil.getInt(req, "namTop5", curYear);
            int filterYearDaBan = RequestUtil.getInt(req, "namDaBan", curYear);
            int thangTop5 = RequestUtil.getInt(req, "thangTop5", curMonth);
            int thangDaBan = RequestUtil.getInt(req, "thangDaBan", curMonth);

            Map<String, Object> rpt = orderDAO.buildClassicReportData(filterYear, thangTop5, filterYearTop5, thangDaBan, filterYearDaBan);
            if ("excel".equalsIgnoreCase(req.getParameter("export"))) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> soldRows = (List<Map<String, Object>>) rpt.get("soldProducts");
                ExcelExportUtil.writeExcel(resp, "DUNA_BaoCao_DoanhThu_" + filterYear + ".xlsx", "BaoCao", soldRows);
                return;
            }
            for (Map.Entry<String, Object> entry : rpt.entrySet()) {
                req.setAttribute(entry.getKey(), entry.getValue());
            }
            req.setAttribute("filterYear", filterYear);
            req.setAttribute("filterYearTop5", filterYearTop5);
            req.setAttribute("filterYearDaBan", filterYearDaBan);
            req.setAttribute("thangTop5", thangTop5);
            req.setAttribute("thangDaBan", thangDaBan);
            req.setAttribute("currentPage", 1);
            req.setAttribute("totalPages", 1);
            forwardView(req, resp, "/bao-cao-thong-ke.jsp");
            return;
        }

        String search = RequestUtil.getString(req, "search", RequestUtil.getString(req, "keyword", ""));
        if ("/orders".equals(req.getServletPath()) && view.isEmpty()) {
            String uc = RequestUtil.getString(req, "uc", "UC126").toUpperCase();
            UcRegistry.UcMeta meta = UcRegistry.findByCode(uc);
            List<Map<String, Object>> rows = Tv2UcDispatcher.execute(uc, req);
            if (wantsJson(req)) {
                writeOkJson(resp, uc, "Thanh cong", rows);
                return;
            }
            setResultRows(req, uc, meta != null ? uc + " — " + meta.getTitle() : "Nghiệp vụ Đơn hàng & Hóa đơn VAT M06", rows);
            forwardView(req, resp, "/WEB-INF/views/tv2_nguyen_duc_anh/orders.jsp");
            return;
        }
        String trangThai = RequestUtil.getString(req, "status", RequestUtil.getString(req, "trangThai", ""));
        String payMethod = RequestUtil.getString(req, "pay", RequestUtil.getString(req, "paymentMethod", ""));
        String fromDate = RequestUtil.getSafeDate(req, "fromDate", "");
        String toDate = RequestUtil.getSafeDate(req, "toDate", "");
        int page = Math.max(1, RequestUtil.getInt(req, "page", 1));
        int pageSize = 20;

        List<Map<String, Object>> listDH = orderDAO.listClassicOrders(search, trangThai, payMethod, fromDate, toDate, (page - 1) * pageSize, pageSize);
        int total = orderDAO.countClassicOrders(search, trangThai, payMethod, fromDate, toDate);
        int totalPages = Math.max(1, (int) Math.ceil(total / (double) pageSize));

        if (wantsJson(req)) {
            writeOkJson(resp, "UC126", "Thanh cong", listDH);
            return;
        }
        Map<String, Object> kpi = orderDAO.getClassicOrderKpiCounts();
        req.setAttribute("stat_totalOrders", kpi.get("stat_totalOrders"));
        req.setAttribute("stat_completedOrders", kpi.get("stat_completedOrders"));
        req.setAttribute("stat_cancelledOrders", kpi.get("stat_cancelledOrders"));
        req.setAttribute("listDH", listDH);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("p_search", search);
        req.setAttribute("p_status", trangThai);
        req.setAttribute("p_trangThai", trangThai);
        req.setAttribute("p_pay", payMethod);
        req.setAttribute("p_paymentMethod", payMethod);
        req.setAttribute("p_fromDate", fromDate);
        req.setAttribute("p_toDate", toDate);
        forwardView(req, resp, "/quan-ly-hoa-don.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int userId = getSessionUserId(req, 1);

        if ("updateVAT".equalsIgnoreCase(action)) {
            double vatPct = RequestUtil.getDouble(req, "vatPct", 8.0);
            orderDAO.updateVatRate(vatPct);
            Map<String, Object> res = new LinkedHashMap<>();
            res.put("success", Boolean.TRUE);
            res.put("newVat", vatPct);
            res.put("message", "Đã lưu mức thuế VAT " + (int) vatPct + "% vào hệ thống thành công!");
            writeJson(resp, 200, res);
            return;
        }

        if ("createPhoneOrder".equalsIgnoreCase(action)) {
            long oid = orderService.createPhoneOrder(
                    RequestUtil.getInt(req, "customerId", 1),
                    userId,
                    RequestUtil.getString(req, "receiverName", "Khách gọi hotline"),
                    RequestUtil.getString(req, "receiverPhone", "0912345678"),
                    RequestUtil.getString(req, "shippingAddress", "Hà Nội"),
                    RequestUtil.getDouble(req, "subtotal", 390000),
                    RequestUtil.getDouble(req, "shipFee", 30000),
                    "Đơn đặt qua điện thoại (UC133)");
            resp.sendRedirect(req.getContextPath() + "/orders?success=" + (oid > 0 ? "1" : "0"));
            return;
        }

        if ("create".equalsIgnoreCase(action)) {
            String[] spIds = req.getParameterValues("idSP[]");
            String[] qtys = req.getParameterValues("qty[]");
            String[] prices = req.getParameterValues("donGia[]");
            String[] colors = req.getParameterValues("mauSac[]");
            String[] sizes = req.getParameterValues("trongLuong[]");
            if (spIds == null || spIds.length == 0 || qtys == null || prices == null) {
                if (wantsJson(req)) {
                    writeErrJson(resp, 400, "Gio hang khong hop le hoac dang trong");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/OrderServlet?view=sell&error=emptyCart");
                }
                return;
            }
            boolean isPaid = !"hold".equalsIgnoreCase(RequestUtil.getString(req, "invoiceType", "paid"));
            int custId = RequestUtil.getInt(req, "idKH", 0);
            long holdId = RequestUtil.getLong(req, "holdId", 0L);
            String payMethod = RequestUtil.getString(req, "thanhToan", "Tiền mặt");
            String vCode = RequestUtil.getString(req, "voucherCode", "");
            double vipDisc = RequestUtil.getDouble(req, "discountAmount", 0);
            double voucherDisc = RequestUtil.getDouble(req, "voucherAmount", 0);
            double vat = RequestUtil.getDouble(req, "vatAmount", 0);
            String note = RequestUtil.getString(req, "ghiChu", "");
            long orderId = orderDAO.saveClassicPosOrder(holdId, custId > 0 ? custId : null, userId, isPaid,
                    payMethod, vCode, vipDisc, voucherDisc, vat, note, spIds, qtys, prices, colors, sizes);
            if (wantsJson(req)) {
                if (orderId <= 0) {
                    writeErrJson(resp, 400, "Khong the tao don hang: du lieu san pham khong hop le");
                } else {
                    writeOkJson(resp, "UC262", "Tao don POS thanh cong", orderId);
                }
                return;
            }
            if (orderId <= 0) {
                resp.sendRedirect(req.getContextPath() + "/OrderServlet?view=sell&error=emptyCart");
            } else {
                resp.sendRedirect(req.getContextPath() + "/OrderServlet?view=sell&success=" + (isPaid ? "1" : "hold") + "&id=" + orderId);
            }
            return;
        }

        if ("changeStatus".equalsIgnoreCase(action) || "cancel".equalsIgnoreCase(action)) {
            long id = RequestUtil.getLong(req, "id", RequestUtil.getLong(req, "orderId", 0L));
            String st = RequestUtil.getString(req, "status", RequestUtil.getString(req, "trangThai", ""));
            String stLower = st.toLowerCase();
            int affected;
            if ("cancel".equalsIgnoreCase(action) || stLower.contains("hủy") || stLower.contains("huy") || "CANCELLED".equalsIgnoreCase(st)) {
                affected = orderService.cancelOrder(id, "STAFF_CANCEL", "Hóa đơn chờ bị hủy từ sổ hóa đơn", userId);
            } else {
                int delivered = orderService.deliverOrder(id, userId);
                int completed = orderService.completeOrder(id);
                affected = Math.max(delivered, completed);
            }
            if (wantsJson(req) || "1".equals(req.getParameter("ajax"))) {
                Map<String, Object> out = new LinkedHashMap<>();
                out.put("ok", affected > 0);
                out.put("success", affected > 0);
                out.put("affected", affected);
                out.put("message", affected > 0 ? "Cập nhật trạng thái đơn hàng thành công" : "Đơn hàng không ở trạng thái cho phép cập nhật");
                writeJson(resp, affected > 0 ? 200 : 400, out);
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/OrderServlet?" + (affected > 0 ? "success=1" : "error=status_update_failed"));
            return;
        }

        if ("exchangeItem".equalsIgnoreCase(action)) {
            long idDH = RequestUtil.getLong(req, "idDH", 0L);
            long idCT = RequestUtil.getLong(req, "idCT", 0L);
            int idBienTheMoi = RequestUtil.getInt(req, "idBienTheMoi", 0);
            int exRes = orderDAO.exchangeOrderItem(idDH, idCT, idBienTheMoi, 0);
            if (exRes > 0) {
                resp.sendRedirect(req.getContextPath() + "/OrderServlet?success=exchange");
            } else {
                resp.sendRedirect(req.getContextPath() + "/OrderServlet?error=" + (exRes == -1 ? "exchange_out_of_stock" : "exchange_failed"));
            }
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/OrderServlet");
    }
}
