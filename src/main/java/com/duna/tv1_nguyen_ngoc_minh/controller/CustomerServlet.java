package com.duna.tv1_nguyen_ngoc_minh.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv1_nguyen_ngoc_minh.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller M02 - Khach hang, Hang thanh vien, Nhom & So do (UC028 - UC043).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
@WebServlet(name = "CustomerServlet", urlPatterns = {"/customers", "/CustomerServlet"})
public class CustomerServlet extends BaseServlet {
    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uc = RequestUtil.getString(req, "uc", "UC028");
        String keyword = RequestUtil.getString(req, "keyword", RequestUtil.getString(req, "search", ""));
        int page = Math.max(1, RequestUtil.getInt(req, "page", 1));
        int customerId = RequestUtil.getInt(req, "customerId", 1);

        if ("UC030".equalsIgnoreCase(uc)) {
            Map<String, Object> detail = customerService.getCustomerDetail(customerId);
            if (wantsJson(req)) {
                writeOkJson(resp, uc, "Ho so khach hang", detail);
                return;
            }
            req.setAttribute("customerDetail", detail);
        }

        List<Map<String, Object>> rows;
        if ("UC029".equalsIgnoreCase(uc) || !keyword.isEmpty()) {
            rows = customerService.searchCustomers(keyword);
        } else if ("UC035".equalsIgnoreCase(uc)) {
            rows = customerService.listTiers(keyword);
        } else if ("UC039".equalsIgnoreCase(uc)) {
            rows = customerService.listGroups(keyword);
        } else {
            rows = customerService.listCustomers(keyword, (page - 1) * 20, 20);
        }
        if (wantsJson(req)) {
            writeOkJson(resp, uc, "Thanh cong", rows);
            return;
        }
        req.setAttribute("tiers", customerService.listTiers(""));
        req.setAttribute("groups", customerService.listGroups(""));
        setResultRows(req, uc, "Quan ly Khach hang & Hang VIP (" + uc + ")", rows);
        forwardView(req, resp, "/WEB-INF/views/tv1_nguyen_ngoc_minh/customers.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = RequestUtil.getString(req, "action", "");
        int affected = 0;
        if ("add".equals(action) || "addCustomer".equals(action)) {
            affected = customerService.addWalkInCustomer(
                    RequestUtil.getString(req, "fullName", ""),
                    RequestUtil.getString(req, "phone", ""),
                    RequestUtil.getString(req, "gender", "MALE"));
        } else if ("update".equals(action)) {
            affected = customerService.updateCustomer(
                    RequestUtil.getInt(req, "customerId", 0),
                    RequestUtil.getString(req, "phone", ""),
                    RequestUtil.getString(req, "email", ""),
                    RequestUtil.getString(req, "birthDate", ""));
        } else if ("autoUpgrade".equals(action)) {
            affected = customerService.autoUpgradeTiers();
        } else if ("addGroup".equals(action)) {
            affected = customerService.addGroup(RequestUtil.getString(req, "groupName", ""),
                    RequestUtil.getString(req, "description", ""));
        } else if ("saveMeasurement".equals(action)) {
            affected = customerService.saveMeasurement(
                    RequestUtil.getInt(req, "customerId", 1),
                    RequestUtil.getDouble(req, "heightCm", 170),
                    RequestUtil.getDouble(req, "weightKg", 65),
                    RequestUtil.getDouble(req, "chestCm", 92),
                    RequestUtil.getDouble(req, "waistCm", 78),
                    RequestUtil.getDouble(req, "hipCm", 94),
                    RequestUtil.getDouble(req, "shoulderCm", 44));
        }
        if (wantsJson(req)) {
            writeOkJson(resp, action, "Thanh cong", affected);
        } else {
            resp.sendRedirect(req.getContextPath() + "/customers?msg=" + (affected > 0 ? "ok" : "skip"));
        }
    }
}
