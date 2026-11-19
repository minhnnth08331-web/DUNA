<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký Tài Khoản — DUNA Men's Wear</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/img/duna-logo.png" type="image/png">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme.css?v=duna_v4">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css?v=duna_v4">
    <script>if (localStorage.getItem('duna_theme') === 'dark') document.documentElement.classList.add('dark-mode');</script>
</head>
<body>
    <div class="login-page-container">
        <div class="login-card">
            <img src="${pageContext.request.contextPath}/img/duna-logo.png" alt="DUNA Logo" class="login-brand-logo" width="64" height="64" style="width: 64px !important; height: 64px !important; border-radius: 8px; object-fit: contain; margin: 0 auto 16px auto;">
            <div class="login-brand-title">DU<span>NA</span></div>
            <div class="login-brand-subtitle">Đăng Ký Tài Khoản Khách Hàng (UC014)</div>

            <form action="${pageContext.request.contextPath}/auth" method="POST" class="login-form">
                <input type="hidden" name="action" value="register">
                <div class="form-group">
                    <label for="fullName">Họ và tên</label>
                    <input type="text" id="fullName" name="fullName" class="form-control" required placeholder="Nguyễn Văn An">
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" class="form-control" required placeholder="email@example.com">
                </div>
                <div class="form-group">
                    <label for="phone">Số điện thoại</label>
                    <input type="tel" id="phone" name="phone" class="form-control" required placeholder="0912345678" maxlength="10" pattern="0[35789][0-9]{8}" title="Số điện thoại bắt đầu bằng số 0, gồm đúng 10 chữ số (03, 05, 07, 08, 09)" oninput="this.value=this.value.replace(/[^0-9]/g,'').slice(0,10)">
                </div>
                <div class="form-group">
                    <label for="password">Mật khẩu (>= 6 ký tự, có chữ và số)</label>
                    <input type="password" id="password" name="password" class="form-control" required placeholder="Ví dụ: Khach123">
                </div>
                <c:if test="${not empty requestScope.errorMessage}">
                    <div class="alert alert-danger">${requestScope.errorMessage}</div>
                </c:if>
                <div class="login-btn-container">
                    <button type="submit" class="btn btn-primary">ĐĂNG KÝ TÀI KHOẢN</button>
                </div>
                <div style="margin-top: 12px; text-align: center; font-size: 13px;">
                    Đã có tài khoản? <a href="${pageContext.request.contextPath}/login.jsp" style="color: var(--brand-accent, #c5a059); font-weight: 600;">Quay lại đăng nhập</a>
                </div>
            </form>
        </div>
    </div>
    <jsp:include page="/footer.jsp" />
</body>
</html>
