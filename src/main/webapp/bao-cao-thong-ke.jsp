<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="todayStr" value="${now}" pattern="dd/MM/yyyy" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Báo cáo thống kê & Doanh thu — DUNA Men's Wear</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@500;600;700;800&family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=JetBrains+Mono:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme.css?v=duna_v4">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css?v=duna_v4">
    <script>if (localStorage.getItem('duna_theme') === 'dark') document.documentElement.classList.add('dark-mode');</script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <div class="navbar">
        <a href="${pageContext.request.contextPath}/OrderServlet?view=sell" class="navbar-brand">
            <img src="${pageContext.request.contextPath}/img/duna-logo.png" alt="DUNA" class="navbar-brand-logo" width="36" height="36" style="width: 36px !important; height: 36px !important; max-width: 36px !important; max-height: 36px !important; border-radius: 6px; object-fit: contain; flex-shrink: 0;">
            <div class="navbar-brand-text">
                <span class="brand-monogram">DU<span>NA</span></span>
                <span class="brand-subtext">Men's Wear</span>
            </div>
        </a>
        <div class="nav-menu">
            <a href="${pageContext.request.contextPath}/ProductServlet">Quản lý sản phẩm</a>
            <a href="${pageContext.request.contextPath}/EmployeeManagementServlet">Quản lý nhân sự</a>
            <a href="${pageContext.request.contextPath}/OrderServlet?view=report" class="active">Báo cáo thống kê</a>
            <a href="${pageContext.request.contextPath}/CustomerServlet">Quản lý khách hàng</a>
            <a href="${pageContext.request.contextPath}/OrderServlet?view=sell">Bán hàng</a>
            <a href="${pageContext.request.contextPath}/OrderServlet">Quản lý hóa đơn</a>
            <a href="${pageContext.request.contextPath}/VoucherServlet">Quản lý Voucher</a>
        </div>
        <div class="nav-right-cluster">
            <div class="nav-user">
                <span>Xin chào</span>
                <a href="${pageContext.request.contextPath}/EmployeeDetailServlet?id=${not empty sessionScope.nhanVienID ? sessionScope.nhanVienID : 1}&mode=view" class="nav-user-name">
                    ${not empty sessionScope.tenNV ? sessionScope.tenNV : 'Admin'}
                </a>
                <span class="nav-divider">|</span>
                <a href="${pageContext.request.contextPath}/LogoutServlet" class="nav-logout-btn">Đăng xuất</a>
            </div>
        </div>
    </div>

<main class="container">
    <div class="page-header-row">
        <div>
            <div class="page-title-eyebrow">Phân tích kinh doanh & Sổ cái tài chính</div>
            <h2>Báo Cáo Thống Kê & Doanh Thu DUNA</h2>
            <div style="font-size: 13.5px; color: var(--color-text-muted); margin-top: 4px;">Phân tích chỉ số doanh thu thuần, tổng giá vốn, lợi nhuận gộp và biên lợi nhuận thời gian thực</div>
        </div>
        <div>
            <span class="badge badge-neutral" style="font-size: 13px; padding: 6px 14px;">Ngày lập: ${todayStr}</span>
        </div>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger" style="margin-bottom: 24px;">${error}</div>
    </c:if>

    <!-- BỘ LỌC TÌM KIẾM -->
    <div class="card" style="margin-bottom: 24px;">
        <form action="${pageContext.request.contextPath}/OrderServlet" method="get" class="filter-bar" id="filterReportForm" style="margin: 0; align-items: flex-end;">
            <input type="hidden" name="view" value="report">
            
            <div class="form-group" style="min-width: 220px; margin: 0;">
                <label for="nam" class="form-label">Chọn năm xem thống kê:</label>
                <select name="nam" id="nam" class="form-select" onchange="submitReportFilter()">
                    <c:forEach var="y" begin="2020" end="2120">
                        <option value="${y}" ${filterYear == y ? 'selected' : ''}>Năm ${y}</option>
                    </c:forEach>
                </select>
            </div>

            <div style="display: flex; gap: 8px;">
                <button type="button" class="btn btn-secondary" onclick="exportReportExcel()">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" style="vertical-align: middle; margin-right: 4px;">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m5.231 13.481L15 17.25m0 0-1.5-1.5M15 17.25l-1.5 1.5M15 17.25H9m6 0v-4.5M9 6v12a2.25 2.25 0 0 0 2.25 2.25h8.25a2.25 2.25 0 0 0 2.25-2.25V7.875c0-.621-.504-1.125-1.125-1.125H13.5V5.25c0-.621-.504-1.125-1.125-1.125H11.25a2.25 2.25 0 0 0-2.25 2.25Z" />
                    </svg>
                    Xuất Báo Cáo Excel
                </button>
            </div>
        </form>
    </div>

    <!-- STATS OVERVIEW CARDS (8 CHỈ SỐ THEO PHONG CÁCH TYPOGRAPHIC LUXURY) -->
    <div class="overview-grid" style="grid-template-columns: repeat(4, 1fr); margin-bottom: 24px;">
        <!-- Doanh thu Hôm nay -->
        <div class="stat-card">
            <span class="stat-label">Doanh thu hôm nay</span>
            <div class="stat-val font-mono" style="color: var(--color-success);">
                <fmt:formatNumber value="${not empty stat_totalRevenueToday ? stat_totalRevenueToday : 0}" type="number" groupingUsed="true"/> đ
            </div>
            <span class="stat-sub">Ghi nhận trong ngày giao dịch</span>
        </div>

        <!-- Lợi nhuận gộp Hôm nay -->
        <div class="stat-card">
            <span class="stat-label">Lợi nhuận gộp hôm nay</span>
            <div class="stat-val font-mono" style="color: var(--color-primary);">
                <fmt:formatNumber value="${not empty stat_grossProfitToday ? stat_grossProfitToday : 0}" type="number" groupingUsed="true"/> đ
            </div>
            <span class="stat-sub">Doanh thu trừ giá vốn hàng bán</span>
        </div>

        <!-- Doanh thu năm filterYear -->
        <div class="stat-card">
            <span class="stat-label">Doanh thu năm ${filterYear}</span>
            <div class="stat-val font-mono" style="color: var(--color-accent);">
                <fmt:formatNumber value="${not empty stat_totalRevenueYear ? stat_totalRevenueYear : 0}" type="number" groupingUsed="true"/> đ
            </div>
            <span class="stat-sub">Doanh thu thuần đã trừ ưu đãi</span>
        </div>

        <!-- Lợi nhuận gộp năm filterYear -->
        <div class="stat-card">
            <span class="stat-label">Lợi nhuận năm ${filterYear} (${stat_profitMarginYear}%)</span>
            <div class="stat-val font-mono" style="color: var(--color-success);">
                <fmt:formatNumber value="${not empty stat_grossProfitYear ? stat_grossProfitYear : 0}" type="number" groupingUsed="true"/> đ
            </div>
            <span class="stat-sub">Tỷ suất biên lợi nhuận ròng: ${stat_profitMarginYear}%</span>
        </div>

        <!-- Tổng doanh thu toàn thời gian -->
        <div class="stat-card">
            <span class="stat-label">Doanh thu toàn thời gian</span>
            <div class="stat-val font-mono">
                <fmt:formatNumber value="${not empty stat_totalRevenueAllTime ? stat_totalRevenueAllTime : 0}" type="number" groupingUsed="true"/> đ
            </div>
            <span class="stat-sub">Tích lũy từ tất cả đơn thanh toán</span>
        </div>

        <!-- Tổng sản phẩm đã bán trong năm filterYear -->
        <div class="stat-card">
            <span class="stat-label">Sản phẩm bán năm ${filterYear}</span>
            <div class="stat-val font-mono">
                <fmt:formatNumber value="${not empty stat_totalSoldYear ? stat_totalSoldYear : 0}" type="number" groupingUsed="true"/> SP
            </div>
            <span class="stat-sub">Số lượng mặt hàng xuất kho</span>
        </div>

        <!-- Tổng sản phẩm đã bán toàn thời gian -->
        <div class="stat-card">
            <span class="stat-label">Sản phẩm bán toàn thời gian</span>
            <div class="stat-val font-mono">
                <fmt:formatNumber value="${not empty stat_totalSold ? stat_totalSold : 0}" type="number" groupingUsed="true"/> SP
            </div>
            <span class="stat-sub">Tổng quy mô bán ra toàn hệ thống</span>
        </div>

        <!-- Tổng tồn kho -->
        <div class="stat-card">
            <span class="stat-label">Tổng tồn kho hiện tại</span>
            <div class="stat-val font-mono">
                <fmt:formatNumber value="${not empty stat_totalInventory ? stat_totalInventory : 0}" type="number" groupingUsed="true"/> SP
            </div>
            <span class="stat-sub">Tổng số biến thể sẵn sàng bán lẻ</span>
        </div>
    </div>

    <!-- BIỂU ĐỒ DOANH THU -->
    <div class="card" style="margin-bottom: 24px; padding: 24px;">
        <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 12px; margin-bottom: 20px; border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 14px;">
            <div>
                <h3 style="margin: 0; font-size: 16px; font-weight: 700; color: var(--color-text);">Biểu đồ tăng trưởng doanh thu theo tháng (Năm ${filterYear})</h3>
                <div style="font-size: 12.5px; color: var(--color-text-muted); margin-top: 2px;">Cột biểu diễn doanh thu thuần thực nhận | Đường nét biểu diễn lợi nhuận gộp</div>
            </div>
            <span class="badge badge-neutral" style="font-size: 12px;">Đơn vị: VNĐ</span>
        </div>
        <div style="height: 380px; position: relative;">
            <canvas id="revenueChart"></canvas>
        </div>
    </div>

    <!-- HÀNG CHỨA 2 BẢNG (TOP 10 SẢN PHẨM & TỒN KHO THEO DANH MỤC) -->
    <div style="display: grid; grid-template-columns: 1.4fr 1fr; gap: 24px; margin-bottom: 24px;">
        <!-- TOP 10 SẢN PHẨM BÁN CHẠY -->
        <div class="card" style="padding: 20px;">
            <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px; margin-bottom: 16px; border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 12px;">
                <div style="display: flex; align-items: center; gap: 8px;">
                    <h3 style="margin: 0; font-size: 15px; font-weight: 700; color: var(--color-text);">Top 10 sản phẩm bán chạy</h3>
                    <span class="badge badge-primary">Tháng ${thangTop5}/${filterYearTop5}</span>
                </div>
                <div style="display: flex; align-items: center; gap: 8px;">
                    <select name="thangTop5" id="thangTop5" form="filterReportForm" class="form-select" onchange="submitReportFilter()" style="padding: 4px 10px; font-size: 12.5px; height: 32px; width: auto;">
                        <c:forEach var="m" begin="1" end="12">
                            <option value="${m}" ${thangTop5 == m ? 'selected' : ''}>Tháng ${m}</option>
                        </c:forEach>
                    </select>
                    <select name="namTop5" id="namTop5" form="filterReportForm" class="form-select" onchange="submitReportFilter()" style="padding: 4px 10px; font-size: 12.5px; height: 32px; width: auto;">
                        <c:forEach var="y" begin="2020" end="2120">
                            <option value="${y}" ${filterYearTop5 == y ? 'selected' : ''}>Năm ${y}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th style="width: 50px; text-align: center;">STT</th>
                            <th>Tên sản phẩm</th>
                            <th style="width: 70px; text-align: center;">Kỳ</th>
                            <th style="width: 80px; text-align: center;">Đã bán</th>
                            <th class="th-money" style="width: 140px;">Doanh thu</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty topProducts}">
                                <c:forEach var="item" items="${topProducts}">
                                    <tr>
                                        <td style="text-align: center;" class="font-mono">${item.stt}</td>
                                        <td style="font-weight: 600;">${item.tenSP}</td>
                                        <td style="text-align: center; color: var(--color-text-muted); font-size: 12px;">${item.thang}/${item.nam}</td>
                                        <td style="text-align: center; font-weight: 700;" class="font-mono">${item.daBan}</td>
                                        <td class="money font-mono" style="font-weight: 700;">
                                            <fmt:formatNumber value="${item.doanhThu}" type="number" groupingUsed="true"/> đ
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="5" style="text-align: center; color: var(--color-text-muted); padding: 32px 16px;">Không có dữ liệu trong khoảng thời gian này</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- TỒN KHO THEO PHÂN LOẠI DANH MỤC -->
        <div class="card" style="padding: 20px;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 12px;">
                <h3 style="margin: 0; font-size: 15px; font-weight: 700; color: var(--color-text);">Tồn kho theo Danh mục DUNA</h3>
                <span class="badge badge-neutral">Kho hàng</span>
            </div>
            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Danh mục thời trang</th>
                            <th class="th-money" style="width: 130px;">Số lượng tồn</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty categoryStock}">
                                <c:forEach var="item" items="${categoryStock}">
                                    <tr>
                                        <td style="font-weight: 600;">
                                            <c:choose>
                                                <c:when test="${item.danhMuc == 'Thời trang nam cao cấp'}">
                                                    <span class="badge badge-primary">${item.danhMuc}</span>
                                                </c:when>
                                                <c:when test="${item.danhMuc == 'Thời trang nam trung cấp'}">
                                                    <span class="badge badge-warning">${item.danhMuc}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-neutral">${item.danhMuc}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="money font-mono" style="font-weight: 700; font-size: 14px;">
                                            <fmt:formatNumber value="${item.soLuongTon}" type="number" groupingUsed="true"/> SP
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="2" style="text-align: center; color: var(--color-text-muted); padding: 32px 16px;">Không có dữ liệu tồn kho</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- DANH SÁCH SẢN PHẨM ĐÃ BÁN -->
    <div class="card" style="padding: 20px;">
        <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px; margin-bottom: 16px; border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 12px;">
            <div style="display: flex; align-items: center; gap: 8px;">
                <h3 style="margin: 0; font-size: 16px; font-weight: 700; color: var(--color-text);">Sổ kê chi tiết sản phẩm đã bán</h3>
                <span class="badge badge-primary">Tháng ${thangDaBan}/${filterYearDaBan}</span>
            </div>
            <div style="display: flex; align-items: center; gap: 8px;">
                <label for="thangDaBan" style="font-size: 13px; color: var(--color-text-muted);">Lọc theo:</label>
                <select name="thangDaBan" id="thangDaBan" form="filterReportForm" class="form-select" onchange="submitReportFilter()" style="padding: 4px 10px; font-size: 12.5px; height: 32px; width: auto;">
                    <c:forEach var="m" begin="1" end="12">
                        <option value="${m}" ${thangDaBan == m ? 'selected' : ''}>Tháng ${m}</option>
                    </c:forEach>
                </select>
                <select name="namDaBan" id="namDaBan" form="filterReportForm" class="form-select" onchange="submitReportFilter()" style="padding: 4px 10px; font-size: 12.5px; height: 32px; width: auto;">
                    <c:forEach var="y" begin="2020" end="2120">
                        <option value="${y}" ${filterYearDaBan == y ? 'selected' : ''}>Năm ${y}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="table-wrap" style="max-height: 520px; overflow-y: auto;">
            <table class="data-table">
                <thead style="position: sticky; top: 0; z-index: 2;">
                    <tr>
                        <th style="width: 50px; text-align: center;">STT</th>
                        <th style="width: 140px; text-align: center;">Mã sản phẩm</th>
                        <th>Tên sản phẩm</th>
                        <th style="width: 180px; text-align: center;">Phân loại</th>
                        <th style="width: 160px; text-align: center;">Thời gian bán</th>
                        <th style="width: 90px; text-align: center;">Số lượng</th>
                        <th class="th-money" style="width: 160px;">Thành tiền</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty soldProducts}">
                            <c:forEach var="item" items="${soldProducts}">
                                <tr>
                                    <td style="text-align: center;" class="font-mono">${item.stt}</td>
                                    <td style="text-align: center;" class="font-mono" style="font-weight: 700; color: var(--color-primary);">${item.maSP}</td>
                                    <td style="font-weight: 600;">${item.tenSP}</td>
                                    <td style="text-align: center;">
                                        <c:choose>
                                            <c:when test="${item.phanLoai == 'Thời trang nam cao cấp'}">
                                                <span class="badge badge-primary">${item.phanLoai}</span>
                                            </c:when>
                                            <c:when test="${item.phanLoai == 'Thời trang nam trung cấp'}">
                                                <span class="badge badge-warning">${item.phanLoai}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-neutral">${not empty item.phanLoai ? item.phanLoai : 'Thời trang nam thường'}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="text-align: center; color: var(--color-text-muted); font-size: 12.5px;" class="font-mono">${item.thoiGian}</td>
                                    <td style="text-align: center; font-weight: 700;" class="font-mono">${item.soLuong}</td>
                                    <td class="money font-mono" style="font-weight: 700; color: var(--color-success);">
                                        <fmt:formatNumber value="${item.thanhTien}" type="number" groupingUsed="true"/> đ
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="7" style="text-align: center; color: var(--color-text-muted); padding: 36px 16px;">Không có dữ liệu bán hàng trong khoảng thời gian này</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</main>

<jsp:include page="footer.jsp" />

<script>
    if ('scrollRestoration' in history) {
        history.scrollRestoration = 'manual';
    }

    function submitReportFilter() {
        sessionStorage.setItem('reportScrollPos', window.scrollY || document.documentElement.scrollTop);
        document.getElementById('filterReportForm').submit();
    }

    function exportReportExcel() {
        const form = document.getElementById('filterReportForm');
        const params = new URLSearchParams(new FormData(form));
        params.set('export', 'excel');
        
        const thangTop5 = document.getElementById('thangTop5');
        const namTop5 = document.getElementById('namTop5');
        const thangDaBan = document.getElementById('thangDaBan');
        const namDaBan = document.getElementById('namDaBan');
        if (thangTop5) params.set('thangTop5', thangTop5.value);
        if (namTop5) params.set('namTop5', namTop5.value);
        if (thangDaBan) params.set('thangDaBan', thangDaBan.value);
        if (namDaBan) params.set('namDaBan', namDaBan.value);
        
        window.location.href = '${pageContext.request.contextPath}/OrderServlet?' + params.toString();
    }

    // Khôi phục vị trí cuộn ngay lập tức trước khi render xong toàn bộ
    (function restoreScrollEarly() {
        const savedScrollPos = sessionStorage.getItem('reportScrollPos');
        if (savedScrollPos !== null) {
            const pos = parseInt(savedScrollPos, 10);
            if (!isNaN(pos)) {
                document.documentElement.style.scrollBehavior = 'auto';
                window.scrollTo(0, pos);
            }
        }
    })();

    document.addEventListener("DOMContentLoaded", function() {
        const savedScrollPos = sessionStorage.getItem('reportScrollPos');
        if (savedScrollPos !== null) {
            const pos = parseInt(savedScrollPos, 10);
            if (!isNaN(pos)) {
                document.documentElement.style.scrollBehavior = 'auto';
                window.scrollTo(0, pos);
                let attempts = 0;
                const scrollInterval = setInterval(() => {
                    window.scrollTo(0, pos);
                    attempts++;
                    if (attempts >= 10) {
                        clearInterval(scrollInterval);
                        sessionStorage.removeItem('reportScrollPos');
                        document.documentElement.style.scrollBehavior = '';
                    }
                }, 40);
            } else {
                sessionStorage.removeItem('reportScrollPos');
            }
        }

        const ctx = document.getElementById('revenueChart').getContext('2d');

        // Parse monthly revenue, profit và sold từ Server
        const revenueData = ${not empty stat_monthlyRevenue ? stat_monthlyRevenue : '[]'};
        const profitData = ${not empty stat_monthlyProfit ? stat_monthlyProfit : '[]'};
        const soldData = ${not empty stat_monthlySold ? stat_monthlySold : '[]'};

        const isDark = document.documentElement.classList.contains('dark-mode');
        const gridColor = isDark ? '#262626' : '#e5e5e5';
        const labelColor = isDark ? '#a3a3a3' : '#737373';
        const barColor = isDark ? '#3b82f6' : '#0f172a';
        const lineColor = '#b45309';

        new Chart(ctx, {
            data: {
                labels: [
                    'Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 
                    'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'
                ],
                datasets: [
                    {
                        type: 'bar',
                        label: 'Doanh thu thuần (VNĐ)',
                        data: revenueData,
                        backgroundColor: barColor,
                        hoverBackgroundColor: '#2563eb',
                        borderWidth: 0,
                        borderRadius: 4,
                        barPercentage: 0.5,
                        categoryPercentage: 0.8,
                        order: 2
                    },
                    {
                        type: 'line',
                        label: 'Lợi nhuận gộp (VNĐ)',
                        data: profitData,
                        borderColor: lineColor,
                        backgroundColor: 'rgba(180, 83, 9, 0.08)',
                        borderWidth: 2.5,
                        pointBackgroundColor: lineColor,
                        pointBorderColor: '#fff',
                        pointBorderWidth: 1.5,
                        pointRadius: 4,
                        tension: 0.3,
                        fill: true,
                        order: 1
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                        labels: {
                            font: { family: "'Plus Jakarta Sans', sans-serif", size: 12, weight: '600' },
                            color: labelColor,
                            boxWidth: 14,
                            padding: 16
                        }
                    },
                    tooltip: {
                        backgroundColor: '#0f172a',
                        titleFont: { size: 13, family: "'Plus Jakarta Sans', sans-serif", weight: '700' },
                        bodyFont: { size: 13, family: "'Plus Jakarta Sans', sans-serif" },
                        padding: 12,
                        cornerRadius: 6,
                        displayColors: false,
                        callbacks: {
                            label: function(context) {
                                const idx = context.dataIndex;
                                const soldQty = soldData[idx] || 0;
                                return [
                                    context.dataset.label + ': ' + Number(context.raw || 0).toLocaleString('vi-VN') + ' VNĐ',
                                    'Số lượng bán: ' + Number(soldQty).toLocaleString('vi-VN') + ' SP'
                                ];
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        grid: { display: false },
                        ticks: {
                            font: { family: "'Plus Jakarta Sans', sans-serif", size: 12, weight: '500' },
                            color: labelColor
                        }
                    },
                    y: {
                        grid: { color: gridColor },
                        ticks: {
                            font: { family: "'JetBrains Mono', monospace", size: 11 },
                            color: labelColor,
                            callback: function(value) {
                                if (value >= 1000000) {
                                    return (value / 1000000) + 'M';
                                }
                                return value.toLocaleString('vi-VN');
                            }
                        },
                        border: { dash: [4, 4] }
                    }
                }
            }
        });
    });
</script>
</body>
</html>
