<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<div class="footer-wrapper no-print">
    <footer class="custom-footer">
        <div class="footer-container">
            <div class="footer-brand-box">
                <img src="${pageContext.request.contextPath}/img/duna-logo.png" alt="DUNA" class="footer-brand-logo" width="32" height="32" style="width: 32px !important; height: 32px !important; max-width: 32px !important; max-height: 32px !important; border-radius: 6px; object-fit: contain; flex-shrink: 0;">
                <div class="footer-brand-info">
                    <span class="footer-brand-title">DU<span>NA</span> MEN'S WEAR</span>
                </div>
            </div>
            <div class="footer-links-box">
                <div class="footer-item">
                    <span>Hotline:</span>
                    <a href="tel:0378885173">037 888 5173</a>
                </div>
                <span class="footer-divider">•</span>
                <div class="footer-item">
                    <span>Email:</span>
                    <a href="mailto:contact@duna.vn">contact@duna.vn</a>
                </div>
            </div>
            <div class="footer-copyright">
                &copy; 2026 DUNA Men's Wear &mdash; Hệ Thống Quản Lý Bán Lẻ Thời Trang Nam Tầm Trung
            </div>
        </div>
    </footer>
</div>

<script>
(function() {
    // 0. Tự động gắn Favicon DUNA Men's Wear nếu trang chưa có
    if (!document.querySelector('link[rel*="icon"]')) {
        var iconLink = document.createElement('link');
        iconLink.type = 'image/png';
        iconLink.rel = 'shortcut icon';
        iconLink.href = '${pageContext.request.contextPath}/img/duna-logo.png';
        document.head.appendChild(iconLink);
    }

    // 1. Khôi phục theme lưu trong localStorage
    var savedTheme = localStorage.getItem('duna_theme') || 'light';
    if (savedTheme === 'dark') {
        document.documentElement.classList.add('dark-mode');
    }

    // 2. Tự động gắn nút chuyển Light / Dark Mode trên thanh điều hướng
    function setupThemeToggle() {
        if (document.getElementById('ynThemeBtn')) return;

        var btn = document.createElement('button');
        btn.id = 'ynThemeBtn';
        btn.className = 'theme-toggle-btn no-print no-invert';
        btn.setAttribute('type', 'button');
        btn.title = 'Chuyển đổi giao diện Sáng / Tối';

        function updateStyle() {
            var isDark = document.documentElement.classList.contains('dark-mode');
            btn.innerHTML = isDark ? '🌙 <span>Dark</span>' : '☀️ <span>Light</span>';
        }

        btn.onclick = function(e) {
            e.preventDefault();
            document.documentElement.classList.toggle('dark-mode');
            var isDark = document.documentElement.classList.contains('dark-mode');
            localStorage.setItem('duna_theme', isDark ? 'dark' : 'light');
            updateStyle();
        };

        var navbar = document.querySelector('.navbar');
        if (navbar) {
            var rightCluster = navbar.querySelector('.nav-right-cluster');
            if (rightCluster) {
                rightCluster.insertBefore(btn, rightCluster.firstChild);
            } else {
                var userInfo = null;
                for (var i = 0; i < navbar.children.length; i++) {
                    var ch = navbar.children[i];
                    if (ch.tagName.toLowerCase() === 'span' || ch.classList.contains('nav-user')) {
                        userInfo = ch;
                        break;
                    }
                }
                if (userInfo && userInfo.parentNode === navbar) {
                    navbar.insertBefore(btn, userInfo);
                } else {
                    navbar.appendChild(btn);
                }
            }
        } else {
            btn.style.position = 'fixed';
            btn.style.top = '16px';
            btn.style.right = '16px';
            btn.style.zIndex = '9999';
            document.body.appendChild(btn);
        }
        updateStyle();
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', setupThemeToggle);
    } else {
        setupThemeToggle();
    }
})();

/* ========================================================
   TÍNH NĂNG CHUYỂN TRANG KHÔNG TẢI LẠI TRANG (AJAX/PJAX) & TÌM KIẾM TRANG
   ======================================================== */
function doAjaxPageLoad(targetUrl, isPopState) {
    var area = document.getElementById('ajax-content-area');
    if (!area) {
        window.location.href = targetUrl;
        return;
    }
    area.style.opacity = '0.4';
    area.style.pointerEvents = 'none';
    
    fetch(targetUrl, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(function(res) { return res.text(); })
        .then(function(html) {
            var parser = new DOMParser();
            var doc = parser.parseFromString(html, 'text/html');
            var newArea = doc.getElementById('ajax-content-area');
            if (newArea) {
                area.innerHTML = newArea.innerHTML;
                if (!isPopState) {
                    window.history.pushState({ path: targetUrl }, '', targetUrl);
                }
            } else {
                window.location.href = targetUrl;
            }
        })
        .catch(function() {
            window.location.href = targetUrl;
        })
        .finally(function() {
            area.style.opacity = '1';
            area.style.pointerEvents = 'auto';
            var rect = area.getBoundingClientRect();
            if (rect.top < -20) {
                area.scrollIntoView({ behavior: 'smooth' });
            }
        });
}

function jumpToPage(inputEl, urlParams, maxPage) {
    if (!inputEl) return;
    var val = parseInt(inputEl.value, 10);
    if (isNaN(val) || val < 1) {
        val = 1;
        inputEl.value = 1;
    }
    if (maxPage && val > maxPage) {
        val = maxPage;
        inputEl.value = maxPage;
    }
    var targetUrl = '?page=' + val + (urlParams || '');
    doAjaxPageLoad(targetUrl, false);
}

document.addEventListener('click', function(e) {
    var link = e.target.closest('.pagination-bar a');
    if (link && link.getAttribute('href') && !link.getAttribute('href').startsWith('#') && !link.classList.contains('active')) {
        e.preventDefault();
        doAjaxPageLoad(link.getAttribute('href'), false);
    }
});

window.addEventListener('popstate', function(e) {
    if (document.getElementById('ajax-content-area')) {
        doAjaxPageLoad(window.location.href, true);
    }
});
</script>
