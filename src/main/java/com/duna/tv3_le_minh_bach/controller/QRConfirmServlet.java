package com.duna.tv3_le_minh_bach.controller;

import com.duna.common.util.BaseServlet;
import com.duna.common.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller xac nhan thanh toan VietQR tren thiet bi di dong (UC152 / POS QR Polling).
 */
@WebServlet(name = "QRConfirmServlet", urlPatterns = {"/QRConfirmServlet"})
public class QRConfirmServlet extends BaseServlet {
    private static final Map<String, Map<String, Object>> QR_TOKENS = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = RequestUtil.getString(req, "action", "");
        String token = RequestUtil.getString(req, "token", "");

        if ("init".equalsIgnoreCase(action)) {
            String newToken = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("token", newToken);
            entry.put("bank", RequestUtil.getString(req, "bank", "MB Bank"));
            entry.put("amount", RequestUtil.getString(req, "amount", "0"));
            entry.put("customer", RequestUtil.getString(req, "customer", "Khach hang DUNA"));
            entry.put("status", "PENDING");
            QR_TOKENS.put(newToken, entry);

            String lanIp = resolveLanIp(req.getServerName());
            String baseUrl = req.getScheme() + "://" + lanIp + ":" + req.getServerPort() + req.getContextPath();
            Map<String, Object> out = new LinkedHashMap<>();
            out.put("ok", Boolean.TRUE);
            out.put("token", newToken);
            out.put("status", "PENDING");
            out.put("lanIP", lanIp);
            out.put("url", baseUrl + "/QRConfirmServlet?token=" + newToken);
            writeJson(resp, HttpServletResponse.SC_OK, out);
            return;
        }

        if ("check".equalsIgnoreCase(action)) {
            Map<String, Object> entry = QR_TOKENS.get(token);
            Map<String, Object> out = new LinkedHashMap<>();
            out.put("ok", entry != null);
            out.put("token", token);
            out.put("status", entry != null ? entry.get("status") : "EXPIRED");
            writeJson(resp, HttpServletResponse.SC_OK, out);
            return;
        }

        if ("confirm".equalsIgnoreCase(action)) {
            Map<String, Object> entry = QR_TOKENS.get(token);
            if (entry != null) {
                entry.put("status", "CONFIRMED");
            }
            Map<String, Object> out = new LinkedHashMap<>();
            out.put("ok", entry != null);
            out.put("token", token);
            out.put("status", entry != null ? "CONFIRMED" : "EXPIRED");
            writeJson(resp, HttpServletResponse.SC_OK, out);
            return;
        }

        // Mobile browser opens /QRConfirmServlet?token=...
        req.setAttribute("token", token);
        req.setAttribute("qrData", QR_TOKENS.get(token));
        forwardView(req, resp, "/qr-mobile.jsp");
    }

    private String resolveLanIp(String fallbackHost) {
        try {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements()) {
                NetworkInterface iface = ifaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) continue;
                Enumeration<InetAddress> addrs = iface.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    if (addr instanceof Inet4Address && addr.isSiteLocalAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return fallbackHost;
    }
}
