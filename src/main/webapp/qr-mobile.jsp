<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>Xác Nhận Thanh Toán Chuyển Khoản — DUNA Men's Wear</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme.css?v=duna_v4">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css?v=duna_v4">
    <script>if (localStorage.getItem('duna_theme') === 'dark') document.documentElement.classList.add('dark-mode');</script>
</head>
<body class="qr-mobile-canvas">

<div class="qr-mobile-card">
    <c:choose>
        <c:when test="${empty qrData}">
            <div style="padding: 24px 10px;">
                <div style="font-size: 40px; margin-bottom: 12px;">⚠️</div>
                <h3 style="font-size: 18px; margin-bottom: 8px; color: var(--negative);">Phiên xác nhận không hợp lệ</h3>
                <p style="font-size: 13.5px; color: var(--muted); line-height: 1.5;">Mã QR đã hết hạn hoặc không tồn tại trong hệ thống. Vui lòng quét lại mã QR tại quầy thu ngân DUNA Men's Wear.</p>
            </div>
        </c:when>
        <c:otherwise>
            <!-- Phần hiển thị xác nhận -->
            <div id="confirmSection" style="${qrData.status == 'CONFIRMED' ? 'display:none;' : ''}">
                <div class="qr-mobile-badge">DUNA &bull; Xác Nhận Chuyển Khoản</div>
                <div style="font-family: var(--font-brand); font-size: 22px; font-weight: 800; letter-spacing: 0.05em; color: var(--text-primary); margin-bottom: 4px;">
                    ${qrData.bank}
                </div>
                <p style="font-size: 13px; color: var(--muted); margin-bottom: 16px;">Vui lòng kiểm tra thông tin thanh toán đơn hàng</p>

                <div class="qr-mobile-amount-box">
                    <div style="font-size: 11.5px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.05em; color: var(--muted); margin-bottom: 4px;">Số tiền thanh toán</div>
                    <div class="qr-mobile-amount-val" id="dispAmount">${qrData.amount} VNĐ</div>
                </div>

                <div class="qr-mobile-info-list">
                    <div class="qr-mobile-info-row">
                        <span style="color: var(--muted); font-weight: 500;">Khách hàng:</span>
                        <span style="font-weight: 700; color: var(--text-primary);">${qrData.customer}</span>
                    </div>
                    <div class="qr-mobile-info-row">
                        <span style="color: var(--muted); font-weight: 500;">Nội dung CK:</span>
                        <span style="font-weight: 600; color: var(--brand-accent);">Thanh toan don hang</span>
                    </div>
                    <div class="qr-mobile-info-row">
                        <span style="color: var(--muted); font-weight: 500;">Trạng thái:</span>
                        <span class="badge badge-warning">Chờ xác nhận</span>
                    </div>
                </div>

                <button type="button" class="btn btn-primary" id="btnConfirm" onclick="doConfirm()" style="width: 100%; height: 48px; font-size: 14px; font-weight: 700;">
                    XÁC NHẬN ĐÃ CHUYỂN KHOẢN
                </button>
                <div style="font-size: 12px; color: var(--muted); margin-top: 14px;">
                    Hệ thống thu ngân sẽ tự động in hóa đơn ngay sau khi xác nhận.
                </div>
            </div>

            <!-- Phần hiển thị thành công -->
            <div id="successSection" style="${qrData.status == 'CONFIRMED' ? 'display:block;' : 'display:none;'} padding: 16px 0;">
                <div style="font-size: 54px; margin-bottom: 12px;">✅</div>
                <h2 style="font-size: 20px; font-weight: 800; color: var(--positive); margin-bottom: 8px;">THANH TOÁN THÀNH CÔNG</h2>
                <p style="font-size: 14px; color: var(--muted); line-height: 1.6; margin-bottom: 20px;">
                    Hệ thống tại quầy thu ngân đã tiếp nhận xác nhận và đang in hóa đơn cho quý khách.<br><br>
                    Cảm ơn quý khách đã mua sắm tại <strong>DUNA Men's Wear</strong>!
                </p>
                <div style="padding-top: 16px; border-top: 1px solid var(--border); color: var(--muted); font-size: 12px;">
                    Quý khách có thể đóng cửa sổ trình duyệt này.
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
    // Định dạng lại tiền VNĐ
    document.addEventListener("DOMContentLoaded", function() {
        var el = document.getElementById("dispAmount");
        if (el) {
            var raw = "${qrData.amount}";
            var num = parseInt(raw, 10);
            if (!isNaN(num)) {
                el.textContent = num.toLocaleString("vi-VN") + " VNĐ";
            }
        }
    });

    function doConfirm() {
        var btn = document.getElementById("btnConfirm");
        btn.disabled = true;
        btn.innerHTML = "Đang gửi xác nhận...";

        fetch("${pageContext.request.contextPath}/QRConfirmServlet?action=confirm&token=${token}")
            .then(function(res) { return res.json(); })
            .then(function(data) {
                if (data && data.status === "CONFIRMED") {
                    document.getElementById("confirmSection").style.display = "none";
                    document.getElementById("successSection").style.display = "block";
                } else {
                    alert("Có lỗi xảy ra khi xác nhận. Vui lòng thử lại!");
                    btn.disabled = false;
                    btn.innerHTML = "XÁC NHẬN ĐÃ CHUYỂN KHOẢN";
                }
            })
            .catch(function(err) {
                alert("Lỗi kết nối mạng: " + err);
                btn.disabled = false;
                btn.innerHTML = "XÁC NHẬN ĐÃ CHUYỂN KHOẢN";
            });
    }
</script>

</body>
</html>
