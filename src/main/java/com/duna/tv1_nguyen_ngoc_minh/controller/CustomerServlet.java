package com.duna.tv1_nguyen_ngoc_minh.controller;

import com.duna.common.uc.Tv1UcDispatcher;
import com.duna.common.uc.UcRegistry;
import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv1_nguyen_ngoc_minh.dao.CustomerDAO;
import com.duna.tv1_nguyen_ngoc_minh.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Controller M02 - Khach hang, Hang thanh vien & Ho so CRM (UC028 - UC043).
 * Tuong thich 100% giao dien quan-ly-khach-hang.jsp va API JSON 300 UC.
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/customers", "/CustomerServlet"})
public class CustomerServlet extends BaseServlet {
    private final CustomerService customerService = new CustomerService();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "");
        String search = RequestUtil.getString(req, "search", RequestUtil.getString(req, "keyword", ""));
        String rank = RequestUtil.getString(req, "rank", "");
        String status = RequestUtil.getString(req, "status", "");
        int page = Math.max(1, RequestUtil.getInt(req, "page", 1));
        int pageSize = 20;

        if ("/customers".equals(req.getServletPath())) {
            String activeUc = uc.isEmpty() ? "UC028" : uc.toUpperCase();
            UcRegistry.UcMeta meta = UcRegistry.findByCode(activeUc);
            List<Map<String, Object>> rows = Tv1UcDispatcher.execute(activeUc, req);
            if (wantsJson(req)) {
                writeOkJson(resp, activeUc, "Thanh cong", rows);
                return;
            }
            setResultRows(req, activeUc, meta != null ? activeUc + " — " + meta.getTitle() : "Nghiệp vụ Khách hàng & Hạng VIP M02", rows);
            forwardView(req, resp, "/WEB-INF/views/tv1_nguyen_ngoc_minh/customers.jsp");
            return;
        }

        if ("UC035".equalsIgnoreCase(uc) || "UC039".equalsIgnoreCase(uc)) {
            List<Map<String, Object>> rows = "UC035".equalsIgnoreCase(uc)
                    ? customerService.listTiers(search)
                    : customerService.listGroups(search);
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }

        String searchType = RequestUtil.getString(req, "searchType", "");
        String expiring = RequestUtil.getString(req, "expiring", "");

        List<Map<String, Object>> listKH = customerDAO.listClassicCustomers(search, searchType, rank, status, expiring, (page - 1) * pageSize, pageSize);
        int total = customerDAO.countClassicCustomers(search, searchType, rank, status, expiring);
        int totalPages = Math.max(1, (int) Math.ceil(total / (double) pageSize));

        if (wantsJson(req)) {
            writeOkJson(resp, "UC028", "Thanh cong", listKH);
            return;
        }
        req.setAttribute("listKH", listKH);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("search", search);
        req.setAttribute("p_search", search);
        req.setAttribute("p_searchType", searchType);
        req.setAttribute("rank", rank);
        req.setAttribute("p_rank", rank);
        req.setAttribute("status", status);
        req.setAttribute("p_status", status);
        req.setAttribute("p_expiring", expiring);
        forwardView(req, resp, "/quan-ly-khach-hang.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        String baseTarget = "/customers".equals(req.getServletPath()) ? "/customers" : "/CustomerServlet";
        int affected = 0;
        String errorMsg = null;
        String successMsg = "1";

        if ("add".equals(action) || "addCustomer".equals(action)) {
            String fullName = RequestUtil.getString(req, "hoTen", RequestUtil.getString(req, "fullName", ""));
            String rawPhone = RequestUtil.getString(req, "sdt", RequestUtil.getString(req, "phone", ""));
            String email = RequestUtil.getString(req, "email", "");
            String gender = "Nữ".equalsIgnoreCase(RequestUtil.getString(req, "gioiTinh", RequestUtil.getString(req, "gender", "Nam")))
                    || "FEMALE".equalsIgnoreCase(RequestUtil.getString(req, "gender", "")) ? "FEMALE" : "MALE";
            String birthDate = RequestUtil.getString(req, "ngaySinh", RequestUtil.getString(req, "birthDate", ""));
            String address = RequestUtil.getString(req, "diaChi", "");

            if (fullName.isEmpty()) {
                errorMsg = "Vui lòng nhập họ và tên khách hàng!";
            } else if (!RequestUtil.isValidVnPhone(rawPhone)) {
                errorMsg = "Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0 và gồm đúng 10 chữ số (đầu số 03, 05, 07, 08, 09).";
            } else {
                affected = customerService.addCustomerFull(fullName, rawPhone, email, gender, birthDate, address);
                if (affected == -2) {
                    errorMsg = "Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0 và gồm đúng 10 chữ số (đầu số 03, 05, 07, 08, 09).";
                } else if (affected <= 0) {
                    errorMsg = "Số điện thoại " + RequestUtil.normalizePhone(rawPhone) + " đã tồn tại trong hệ thống!";
                } else {
                    successMsg = "added";
                }
            }
        } else if ("edit".equals(action) || "update".equals(action)) {
            int id = RequestUtil.getInt(req, "id", RequestUtil.getInt(req, "customerId", 0));
            String fullName = RequestUtil.getString(req, "hoTen", "");
            String rawPhone = RequestUtil.getString(req, "sdt", RequestUtil.getString(req, "phone", ""));
            if (!RequestUtil.isValidVnPhone(rawPhone)) {
                errorMsg = "Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0 và gồm đúng 10 chữ số (đầu số 03, 05, 07, 08, 09).";
            } else {
                affected = customerDAO.updateClassicCustomerFull(
                        id,
                        fullName,
                        rawPhone,
                        RequestUtil.getString(req, "email", ""),
                        RequestUtil.getString(req, "gioiTinh", "Nam"),
                        RequestUtil.getString(req, "ngaySinh", RequestUtil.getString(req, "birthDate", "")));
                if (affected == -2) {
                    errorMsg = "Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0 và gồm đúng 10 chữ số.";
                } else if (affected == -4) {
                    errorMsg = "Số điện thoại " + RequestUtil.normalizePhone(rawPhone) + " đã thuộc về khách hàng khác!";
                } else if (affected > 0) {
                    successMsg = "updated";
                }
            }
        } else if ("lock".equals(action)) {
            int id = RequestUtil.getInt(req, "id", 0);
            Map<String, Object> cur = customerService.getCustomerDetail(id);
            boolean activeNow = cur != null && Boolean.TRUE.equals(cur.get("is_active"));
            affected = customerService.setCustomerActive(id, !activeNow);
            successMsg = "locked";
        } else if ("autoUpgrade".equals(action)) {
            affected = customerService.autoUpgradeTiers();
        }
        if (wantsJson(req)) {
            if (errorMsg != null) {
                writeErrJson(resp, 400, errorMsg);
            } else {
                writeOkJson(resp, action, "Thanh cong", affected);
            }
        } else {
            if (errorMsg != null) {
                resp.sendRedirect(req.getContextPath() + baseTarget + "?error=" + URLEncoder.encode(errorMsg, "UTF-8"));
            } else {
                resp.sendRedirect(req.getContextPath() + baseTarget + "?success=" + successMsg);
            }
        }
    }
}
