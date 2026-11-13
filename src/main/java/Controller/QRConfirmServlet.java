package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/QRConfirmServlet")
public class QRConfirmServlet extends HttpServlet {

    public static class QRData {
        private String token;
        private String amount;
        private String bank;
        private String customer;
        private volatile String status; // PENDING or CONFIRMED
        private long createdAt;

        public QRData(String token, String amount, String bank, String customer, String status) {
            this.token = token;
            this.amount = amount;
            this.bank = bank;
            this.customer = customer;
            this.status = status;
            this.createdAt = System.currentTimeMillis();
        }

        public String getToken() { return token; }
        public String getAmount() { return amount; }
        public String getBank() { return bank; }
        public String getCustomer() { return customer; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public long getCreatedAt() { return createdAt; }
    }

    private static final Map<String, QRData> qrSessions = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) action = "check";

        switch (action) {
            case "init":
                handleInit(request, response);
                break;
            case "view":
                handleView(request, response);
                break;
            case "confirm":
                handleConfirm(request, response);
                break;
            case "check":
            default:
                handleCheck(request, response);
                break;
        }
    }

    private void handleInit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String amount = request.getParameter("amount");
        String bank = request.getParameter("bank");
        String customer = request.getParameter("customer");

        if (amount == null) amount = "0";
        if (bank == null) bank = "Vietcombank";
        if (customer == null || customer.trim().isEmpty()) customer = "Khách vãng lai";

        String token = "QR_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 9000 + 1000);
        QRData data = new QRData(token, amount, bank, customer, "PENDING");
        qrSessions.put(token, data);

        String lanIP = getLocalLANIP(request);
        int port = request.getServerPort();
        String scheme = request.getScheme();
        String contextPath = request.getContextPath();

        String url = scheme + "://" + lanIP + (port != 80 && port != 443 ? ":" + port : "") + contextPath + "/QRConfirmServlet?action=view&token=" + token;

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write("{\"token\": \"" + token + "\", \"url\": \"" + url + "\", \"lanIP\": \"" + lanIP + "\"}");
        out.flush();
    }

    private void handleView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        QRData data = (token != null) ? qrSessions.get(token) : null;

        request.setAttribute("qrData", data);
        request.setAttribute("token", token);
        request.getRequestDispatcher("/qr-mobile.jsp").forward(request, response);
    }

    private void handleConfirm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getParameter("token");
        QRData data = (token != null && !token.trim().isEmpty()) ? qrSessions.get(token.trim()) : null;
        boolean success = false;
        String message = "Thao tác thành công";

        if (data != null) {
            long now = System.currentTimeMillis();
            // Mã QR chỉ có hiệu lực tối đa trong 15 phút (900.000 ms)
            if (now - data.getCreatedAt() > 15 * 60 * 1000) {
                data.setStatus("EXPIRED");
                message = "Mã QR đã hết hạn hiệu lực!";
            } else {
                data.setStatus("CONFIRMED");
                success = true;
            }
        } else {
            message = "Không tìm thấy phiên giao dịch QR hợp lệ!";
        }

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write("{\"status\": \"" + (data != null ? data.getStatus() : "INVALID") + "\", \"success\": " + success + ", \"message\": \"" + message + "\"}");
        out.flush();
    }

    private void handleCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getParameter("token");
        QRData data = (token != null) ? qrSessions.get(token) : null;
        String st = (data != null) ? data.getStatus() : "NOT_FOUND";

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write("{\"status\": \"" + st + "\"}");
        out.flush();
    }

    private String getLocalLANIP(HttpServletRequest request) {
        String serverName = request.getServerName();
        if (!"localhost".equalsIgnoreCase(serverName) && !"127.0.0.1".equals(serverName) && !"0:0:0:0:0:0:0:1".equals(serverName)) {
            return serverName;
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual()) continue;
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        String ip = addr.getHostAddress();
                        if (ip.startsWith("192.168.") || ip.startsWith("10.") || ip.startsWith("172.")) {
                            return ip;
                        }
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return serverName;
        }
    }
}
