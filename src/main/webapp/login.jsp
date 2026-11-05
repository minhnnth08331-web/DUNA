<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập — DUNA Men's Wear</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme.css?v=duna_v4">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css?v=duna_v4">
    <script>if (localStorage.getItem('duna_theme') === 'dark') document.documentElement.classList.add('dark-mode');</script>
</head>
<body>
    <div class="login-page-container">
        <div class="login-card">
            <img src="${pageContext.request.contextPath}/img/duna-logo.png" alt="DUNA Logo" class="login-brand-logo" width="64" height="64" style="width: 64px !important; height: 64px !important; max-width: 64px !important; max-height: 64px !important; border-radius: 8px; object-fit: contain; margin: 0 auto 16px auto;">
            <div class="login-brand-title">DU<span>NA</span></div>
            <div class="login-brand-subtitle">Hệ Thống Quản Trị & Bán Lẻ Thời Trang Nam</div>
            
            <form action="LoginServlet" method="POST" class="login-form" id="loginForm" onsubmit="return handleLoginSubmit(event)">
                <div class="form-group">
                    <label for="username">Tên đăng nhập</label>
                    <input type="text" id="username" name="username" class="form-control" required placeholder="Nhập tên tài khoản..." autocomplete="username" value="${not empty cookie.yn_user ? cookie.yn_user.value : ''}">
                </div>
                
                <div class="form-group">
                    <label for="password">Mật khẩu</label>
                    <input type="password" id="password" name="password" class="form-control" required placeholder="Nhập mật khẩu..." autocomplete="current-password" value="${not empty cookie.yn_pass ? cookie.yn_pass.value : ''}">
                </div>

                <div class="form-group" style="flex-direction: row; align-items: center; justify-content: space-between; margin-top: 4px;">
                    <label style="display: flex; align-items: center; gap: 8px; font-size: 13px; font-weight: 500; color: var(--muted); cursor: pointer; text-transform: none; letter-spacing: normal;">
                        <input type="checkbox" name="rememberMe" value="true" id="rememberMe" ${not empty cookie.yn_user ? 'checked' : ''}>
                        Ghi nhớ tài khoản & mật khẩu
                    </label>
                </div>

                <c:if test="${not empty requestScope.errorMessage}">
                    <div class="alert alert-danger" style="margin-top: 12px; margin-bottom: 12px;">
                        ${requestScope.errorMessage}
                    </div>
                </c:if>

                <div class="login-btn-container">
                    <button type="submit" class="btn btn-primary">ĐĂNG NHẬP HỆ THỐNG</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function getCookie(name) {
            var v = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
            return v ? decodeURIComponent(v[2]) : null;
        }

        document.addEventListener("DOMContentLoaded", function() {
            var u = document.getElementById("username");
            var p = document.getElementById("password");
            var cb = document.getElementById("rememberMe");
            
            // Tự động điền tài khoản và mật khẩu nếu đã được ghi nhớ trước đó
            var savedUser = getCookie("yn_user") || localStorage.getItem("yn_user");
            var savedPass = getCookie("yn_pass") || localStorage.getItem("yn_pass");
            if (savedUser && !u.value) {
                u.value = savedUser;
                if (savedPass && !p.value) {
                    p.value = savedPass;
                }
                if (cb) cb.checked = true;
            }
        });

        function handleLoginSubmit(event) {
            var rememberCb = document.getElementById("rememberMe");
            var u = document.getElementById("username").value.trim();
            var p = document.getElementById("password").value;
            var form = document.getElementById("loginForm");

            if (rememberCb && rememberCb.checked) {
                localStorage.setItem("yn_user", u);
                localStorage.setItem("yn_pass", p);

                // Kích hoạt thông báo lưu mật khẩu gốc của trình duyệt web (Chrome, CocCoc, Edge,...)
                if (window.PasswordCredential && navigator.credentials && navigator.credentials.store) {
                    try {
                        var cred = new PasswordCredential(form);
                        navigator.credentials.store(cred).catch(function(){});
                    } catch(e) {
                        try {
                            var c = new PasswordCredential({ id: u, password: p, name: u });
                            navigator.credentials.store(c).catch(function(){});
                        } catch(ex){}
                    }
                }
            } else {
                localStorage.removeItem("yn_user");
                localStorage.removeItem("yn_pass");
            }
            return true;
        }
    </script>
    <jsp:include page="footer.jsp" />
</body>
</html>
