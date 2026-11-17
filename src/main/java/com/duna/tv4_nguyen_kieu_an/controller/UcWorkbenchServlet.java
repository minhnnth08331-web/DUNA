package com.duna.tv4_nguyen_kieu_an.controller;

import com.duna.common.util.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller dieu phoi ban do 300 Use Case & 17 Module (4 thanh vien).
 * Tac gia: Nguyen Kieu An - TH08490
 */
@WebServlet(name = "UcWorkbenchServlet", urlPatterns = {"/uc-hub"})
public class UcWorkbenchServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardView(req, resp, "/WEB-INF/views/common/uc-hub.jsp");
    }
}
