package com.duna.tv2_nguyen_duc_anh.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;
import com.duna.tv2_nguyen_duc_anh.dao.OrderDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller ho tro quet ma vach Barcode USB/WebRTC tai quay POS (UC092, UC261).
 * Tac gia: Nguyen Duc Anh - TH08308
 */
@WebServlet(name = "BarcodeScanServlet", urlPatterns = {"/ThanhVien2Servlet", "/thanhvien2"})
public class BarcodeScanServlet extends BaseServlet {
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String barcode = RequestUtil.getString(req, "barcode", "");
        Map<String, Object> item = orderDAO.scanByBarcodeOrSku(barcode);
        if (item == null) {
            item = new LinkedHashMap<>();
            item.put("found", Boolean.FALSE);
        }
        writeJson(resp, 200, item);
    }
}
