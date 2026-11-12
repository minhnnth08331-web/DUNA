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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme.css?v=duna_v5">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css?v=duna_v5">
    <script>if (localStorage.getItem('duna_theme') === 'dark') document.documentElement.classList.add('dark-mode');</script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
    /* ==========================================================================
       DUNA MEN'S WEAR — ANALYTICS & FINANCIAL REPORT (OPERATE MODE)
       Elevated sartorial craftsmanship, generous spatial rhythm, zero slop
       ========================================================================== */
    .report-page-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-end;
        gap: 24px;
        margin-bottom: 28px;
        padding-bottom: 20px;
        border-bottom: 1px solid var(--border);
        flex-wrap: wrap;
    }
    .report-title-group h1 {
        font-size: 24px;
        font-weight: 800;
        letter-spacing: -0.02em;
        color: var(--text-primary);
        margin: 0 0 6px 0;
        line-height: 1.25;
    }
    .report-title-group p {
        font-size: 13.5px;
        color: var(--muted);
        margin: 0;
        line-height: 1.5;
    }
    .report-date-badge {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        padding: 6px 14px;
        background: var(--surface);
        border: 1px solid var(--control-border);
        border-radius: 8px;
        font-size: 12.5px;
        font-weight: 600;
        color: var(--muted);
        box-shadow: 0 1px 3px rgba(15, 23, 42, 0.04);
    }

    /* Year Filter Toolbar Card */
    .report-filter-card {
        background: var(--surface);
        border: 1px solid var(--border);
        border-radius: 12px;
        padding: 16px 24px;
        margin-bottom: 28px;
        box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
    }
    .report-filter-inner {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 16px;
        flex-wrap: wrap;
    }
    .year-selector-wrap {
        display: flex;
        align-items: center;
        gap: 12px;
    }
    .year-selector-wrap label {
        font-size: 12.5px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        color: var(--muted);
    }
    .year-select {
        height: 40px;
        border: 1px solid var(--control-border);
        border-radius: 8px;
        padding: 0 14px;
        font-size: 13.5px;
        font-weight: 600;
        color: var(--text);
        background: var(--surface);
        outline: none;
        min-width: 150px;
        transition: border-color 0.15s ease;
    }
    .year-select:focus {
        border-color: #0f172a;
    }
    .btn-export-excel {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        height: 40px;
        padding: 0 18px;
        background: var(--surface);
        color: #15803d;
        border: 1px solid #86efac;
        border-radius: 8px;
        font-size: 13px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.15s ease;
        box-shadow: 0 1px 2px rgba(15, 23, 42, 0.04);
    }
    .btn-export-excel:hover {
        background: #dcfce7;
        border-color: #4ade80;
        transform: translateY(-1px);
        box-shadow: 0 2px 6px rgba(21, 128, 61, 0.12);
    }

    /* 8 KPI Metrics Cards Grid */
    .report-kpi-grid {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 20px;
        margin-bottom: 28px;
    }
    @media (max-width: 1200px) {
        .report-kpi-grid {
            grid-template-columns: repeat(2, 1fr);
        }
    }
    @media (max-width: 680px) {
        .report-kpi-grid {
            grid-template-columns: 1fr;
        }
    }
    .kpi-card-luxury {
        background: var(--surface);
        border: 1px solid var(--border);
        border-radius: 12px;
        padding: 20px 22px;
        box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
        position: relative;
        overflow: hidden;
        transition: transform 0.2s ease, box-shadow 0.2s ease;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }
    .kpi-card-luxury:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(15, 23, 42, 0.07);
    }
    .kpi-accent-emerald {
        border-left: 4px solid #16a34a;
    }
    .kpi-accent-navy {
        border-left: 4px solid #0f172a;
    }
    .kpi-accent-gold {
        border-left: 4px solid #c5a059;
    }
    .kpi-accent-slate {
        border-left: 4px solid #64748b;
    }

    .kpi-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 8px;
    }
    .kpi-label {
        font-size: 11.5px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.06em;
        color: var(--muted);
    }
    .kpi-tag {
        font-size: 11px;
        font-weight: 600;
        padding: 2px 8px;
        border-radius: 9999px;
        background: #f1f5f9;
        color: #475569;
    }
    .kpi-val {
        font-size: 26px;
        font-weight: 800;
        line-height: 1.2;
        letter-spacing: -0.02em;
        margin-bottom: 6px;
    }
    .kpi-desc {
        font-size: 12px;
        color: var(--muted);
        line-height: 1.4;
    }

    /* Chart Container Card */
    .report-chart-card {
        background: var(--surface);
        border: 1px solid var(--border);
        border-radius: 12px;
        padding: 24px;
        margin-bottom: 28px;
        box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
    }
    .report-chart-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 16px;
        flex-wrap: wrap;
        margin-bottom: 20px;
        padding-bottom: 16px;
        border-bottom: 1px solid var(--border-subtle);
    }
    .chart-title-area h3 {
        margin: 0 0 4px 0;
        font-size: 17px;
        font-weight: 800;
        color: var(--text-primary);
        letter-spacing: -0.01em;
    }
    .chart-title-area p {
        margin: 0;
        font-size: 13px;
        color: var(--muted);
    }
    .chart-control-cluster {
        display: flex;
        align-items: center;
        gap: 12px;
    }
    .btn-explain-formula {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        background: #f8fafc;
        color: #0f172a;
        border: 1px solid #cbd5e1;
        padding: 7px 14px;
        border-radius: 8px;
        font-size: 12.5px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.15s ease;
        box-shadow: 0 1px 2px rgba(15, 23, 42, 0.04);
    }
    .btn-explain-formula:hover {
        background: #0f172a;
        color: #ffffff;
        border-color: #0f172a;
        transform: translateY(-1px);
        box-shadow: 0 3px 8px rgba(15, 23, 42, 0.12);
    }
    .badge-unit {
        font-size: 11.5px;
        font-weight: 600;
        color: var(--muted);
        background: #f1f5f9;
        border: 1px solid #e2e8f0;
        padding: 4px 10px;
        border-radius: 6px;
    }

    /* Two-column Tables Grid */
    .report-tables-grid {
        display: grid;
        grid-template-columns: 1.35fr 1fr;
        gap: 24px;
        margin-bottom: 28px;
    }
    @media (max-width: 1024px) {
        .report-tables-grid {
            grid-template-columns: 1fr;
        }
    }
    .report-section-card {
        background: var(--surface);
        border: 1px solid var(--border);
        border-radius: 12px;
        padding: 22px 24px;
        box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
    }
    .section-card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 12px;
        flex-wrap: wrap;
        margin-bottom: 16px;
        padding-bottom: 12px;
        border-bottom: 1px solid var(--border-subtle);
    }
    .section-card-title {
        margin: 0;
        font-size: 16px;
        font-weight: 800;
        color: var(--text-primary);
    }
    .table-filter-select {
        height: 34px;
        border: 1px solid var(--control-border);
        border-radius: 6px;
        padding: 0 28px 0 12px;
        min-width: 115px;
        font-size: 12.5px;
        font-weight: 600;
        color: var(--text);
        background: var(--surface);
        outline: none;
        box-sizing: border-box;
        cursor: pointer;
        transition: border-color 0.15s ease;
    }
    .table-filter-select:focus {
        border-color: #0f172a;
    }

    /* Modern Report Tables */
    .report-table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
        font-size: 13px;
    }
    .report-table th {
        background: #f8fafc;
        color: #475569;
        font-size: 11px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        padding: 12px 14px;
        border-bottom: 1px solid var(--border);
        text-align: left;
        white-space: nowrap;
    }
    .report-table th.th-money,
    .report-table td.th-money {
        text-align: right;
    }
    .report-table td {
        padding: 12px 14px;
        border-bottom: 1px solid var(--rule);
        vertical-align: middle;
        color: var(--text);
    }
    .report-table tr:last-child td {
        border-bottom: none;
    }
    .report-table tbody tr:hover {
        background-color: #f8fafc;
    }

    /* Formula Modal Special Styles */
    .formula-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 18px;
        margin: 18px 0;
    }
    @media (max-width: 640px) {
        .formula-grid {
            grid-template-columns: 1fr;
        }
    }
    .formula-card {
        background: #f8fafc;
        border: 1px solid #e2e8f0;
        border-radius: 10px;
        padding: 16px 18px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }
    .formula-card.formula-revenue {
        border-top: 3px solid #0f172a;
    }
    .formula-card.formula-cogs {
        border-top: 3px solid #64748b;
    }
    .formula-card.formula-profit {
        border-top: 3px solid #b45309;
        background: #fffbeb;
        border-color: #fde68a;
    }
    .formula-card.formula-margin {
        border-top: 3px solid #16a34a;
        background: #f0fdf4;
        border-color: #bbf7d0;
    }
    .formula-badge {
        font-size: 11px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        margin-bottom: 6px;
        display: inline-block;
    }
    .formula-math-box {
        background: #ffffff;
        border: 1px solid rgba(0,0,0,0.06);
        border-radius: 6px;
        padding: 10px 12px;
        font-family: 'JetBrains Mono', monospace;
        font-size: 12.5px;
        font-weight: 700;
        margin: 8px 0;
        color: #0f172a;
    }
    .formula-desc {
        font-size: 12px;
        color: #475569;
        line-height: 1.5;
    }

    /* Dark Mode Overrides */
    html.dark-mode .report-date-badge,
    html.dark-mode .year-select {
        background: var(--surface);
        border-color: var(--border);
        color: var(--text);
    }
    html.dark-mode .btn-explain-formula {
        background: #181d28;
        color: #f1f5f9;
        border-color: #334155;
    }
    html.dark-mode .btn-explain-formula:hover {
        background: #ffffff;
        color: #0f172a;
    }
    html.dark-mode .badge-unit {
        background: #1e293b;
        border-color: #334155;
        color: #94a3b8;
    }
    html.dark-mode .report-table th {
        background: #181d28;
        color: #94a3b8;
        border-bottom-color: var(--border);
    }
    html.dark-mode .report-table tbody tr:hover {
        background-color: #1a202c;
    }
    html.dark-mode .formula-card {
        background: #181d28;
        border-color: #334155;
    }
    html.dark-mode .formula-math-box {
        background: #12161f;
        color: #f8fafc;
        border-color: #272e3f;
    }
    html.dark-mode .formula-desc {
        color: #94a3b8;
    }
    html.dark-mode .formula-card.formula-profit {
        background: rgba(180, 83, 9, 0.12);
        border-color: rgba(251, 191, 36, 0.3);
    }
    html.dark-mode .formula-card.formula-margin {
        background: rgba(22, 163, 74, 0.12);
        border-color: rgba(74, 222, 128, 0.3);
    }
    </style>
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
    <!-- PAGE HEADER: Flex layout, generous spatial rhythm, no banned kickers -->
    <div class="report-page-header">
        <div class="report-title-group">
            <h1>Báo Cáo Thống Kê & Phân Tích Tài Chính DUNA</h1>
            <p>Phân tích chỉ số doanh thu thuần, tổng giá vốn hàng bán (COGS), lợi nhuận gộp và biên lợi nhuận thời gian thực</p>
        </div>
        <div>
            <div class="report-date-badge">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>
                <span>Ngày lập: ${todayStr}</span>
            </div>
        </div>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger" style="margin-bottom: 28px;">${error}</div>
    </c:if>

    <!-- YEAR FILTER TOOLBAR CARD -->
    <div class="report-filter-card">
        <form action="${pageContext.request.contextPath}/OrderServlet" method="get" id="filterReportForm" style="margin: 0; width: 100%;">
            <input type="hidden" name="view" value="report">
            <div class="report-filter-inner">
                <div class="year-selector-wrap">
                    <label for="nam">Chọn năm xem thống kê:</label>
                    <select name="nam" id="nam" class="year-select" onchange="submitReportFilter()">
                        <c:forEach var="y" begin="2020" end="2120">
                            <option value="${y}" ${filterYear == y ? 'selected' : ''}>Năm ${y}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <button type="button" class="btn-export-excel" onclick="exportReportExcel()">
                        <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m5.231 13.481L15 17.25m0 0-1.5-1.5M15 17.25l-1.5 1.5M15 17.25H9m6 0v-4.5M9 6v12a2.25 2.25 0 0 0 2.25 2.25h8.25a2.25 2.25 0 0 0 2.25-2.25V7.875c0-.621-.504-1.125-1.125-1.125H13.5V5.25c0-.621-.504-1.125-1.125-1.125H11.25a2.25 2.25 0 0 0-2.25 2.25Z" />
                        </svg>
                        <span>Xuất Báo Cáo Excel</span>
                    </button>
                </div>
            </div>
        </form>
    </div>

    <!-- STATS OVERVIEW CARDS (8 CHỈ SỐ THEO PHONG CÁCH SARTORIAL LUXURY) -->
    <div class="report-kpi-grid">
        <!-- 1. Doanh thu Hôm nay -->
        <div class="kpi-card-luxury kpi-accent-emerald">
            <div class="kpi-header">
                <span class="kpi-label">Doanh thu hôm nay</span>
                <span class="kpi-tag" style="background: #dcfce7; color: #15803d;">Hôm nay</span>
            </div>
            <div class="kpi-val font-mono" style="color: #15803d;">
                <fmt:formatNumber value="${not empty stat_totalRevenueToday ? stat_totalRevenueToday : 0}" type="number" groupingUsed="true"/> đ
            </div>
            <div class="kpi-desc">Ghi nhận trong ngày giao dịch hiện tại</div>
        </div>

        <!-- 2. Lợi nhuận gộp Hôm nay -->
        <div class="kpi-card-luxury kpi-accent-navy">
            <div class="kpi-header">
                <span class="kpi-label">Lợi nhuận gộp hôm nay</span>
                <span class="kpi-tag">Lãi gộp</span>
            </div>
            <div class="kpi-val font-mono" style="color: var(--text-primary);">
                <fmt:formatNumber value="${not empty stat_grossProfitToday ? stat_grossProfitToday : 0}" type="number" groupingUsed="true"/> đ
            </div>
            <div class="kpi-desc">Doanh thu thuần trừ tổng giá vốn hàng bán</div>
        </div>

        <!-- 3. Doanh thu năm filterYear -->
        <div class="kpi-card-luxury kpi-accent-gold">
            <div class="kpi-header">
                <span class="kpi-label">Doanh thu năm ${filterYear}</span>
                <span class="kpi-tag" style="background: #fef3c7; color: #b45309;">Năm ${filterYear}</span>
            </div>
            <div class="kpi-val font-mono" style="color: #b45309;">
                <fmt:formatNumber value="${not empty stat_totalRevenueYear ? stat_totalRevenueYear : 0}" type="number" groupingUsed="true"/> đ
            </div>
            <div class="kpi-desc">Doanh thu thuần lũy kế cả năm đã trừ ưu đãi</div>
        </div>

        <!-- 4. Lợi nhuận gộp năm filterYear -->
        <div class="kpi-card-luxury kpi-accent-emerald">
            <div class="kpi-header">
                <span class="kpi-label">Lợi nhuận năm ${filterYear}</span>
                <span class="kpi-tag" style="background: #dcfce7; color: #15803d;">Biên ${stat_profitMarginYear}%</span>
            </div>
            <div class="kpi-val font-mono" style="color: #15803d;">
                <fmt:formatNumber value="${not empty stat_grossProfitYear ? stat_grossProfitYear : 0}" type="number" groupingUsed="true"/> đ
            </div>
            <div class="kpi-desc">Tỷ suất biên lợi nhuận ròng: ${stat_profitMarginYear}%</div>
        </div>

        <!-- 5. Tổng doanh thu toàn thời gian -->
        <div class="kpi-card-luxury kpi-accent-slate">
            <div class="kpi-header">
                <span class="kpi-label">Doanh thu toàn thời gian</span>
                <span class="kpi-tag">Toàn hệ thống</span>
            </div>
            <div class="kpi-val font-mono" style="color: var(--text-primary);">
                <fmt:formatNumber value="${not empty stat_totalRevenueAllTime ? stat_totalRevenueAllTime : 0}" type="number" groupingUsed="true"/> đ
            </div>
            <div class="kpi-desc">Tích lũy từ tất cả đơn hàng đã thanh toán</div>
        </div>

        <!-- 6. Tổng sản phẩm đã bán trong năm filterYear -->
        <div class="kpi-card-luxury kpi-accent-navy">
            <div class="kpi-header">
                <span class="kpi-label">Sản phẩm bán năm ${filterYear}</span>
                <span class="kpi-tag">Năm ${filterYear}</span>
            </div>
            <div class="kpi-val font-mono" style="color: var(--text-primary);">
                <fmt:formatNumber value="${not empty stat_totalSoldYear ? stat_totalSoldYear : 0}" type="number" groupingUsed="true"/> SP
            </div>
            <div class="kpi-desc">Số lượng mặt hàng thời trang đã xuất kho</div>
        </div>

        <!-- 7. Tổng sản phẩm đã bán toàn thời gian -->
        <div class="kpi-card-luxury kpi-accent-slate">
            <div class="kpi-header">
                <span class="kpi-label">Sản phẩm bán toàn bộ</span>
                <span class="kpi-tag">Toàn thời gian</span>
            </div>
            <div class="kpi-val font-mono" style="color: var(--text-primary);">
                <fmt:formatNumber value="${not empty stat_totalSold ? stat_totalSold : 0}" type="number" groupingUsed="true"/> SP
            </div>
            <div class="kpi-desc">Tổng quy mô sản phẩm bán ra toàn hệ thống</div>
        </div>

        <!-- 8. Tổng tồn kho hiện tại -->
        <div class="kpi-card-luxury kpi-accent-gold">
            <div class="kpi-header">
                <span class="kpi-label">Tổng tồn kho hiện tại</span>
                <span class="kpi-tag" style="background: #f1f5f9; color: #334155;">Kho DUNA</span>
            </div>
            <div class="kpi-val font-mono" style="color: #0f172a;">
                <fmt:formatNumber value="${not empty stat_totalInventory ? stat_totalInventory : 0}" type="number" groupingUsed="true"/> SP
            </div>
            <div class="kpi-desc">Tổng số biến thể màu/size sẵn sàng bán lẻ</div>
        </div>
    </div>

    <!-- BIỂU ĐỒ DOANH THU & LỢI NHUẬN GỘP -->
    <div class="report-chart-card">
        <div class="report-chart-header">
            <div class="chart-title-area">
                <h3>Biểu đồ tăng trưởng doanh thu & lợi nhuận (Năm ${filterYear})</h3>
                <p>Cột màu xanh đen: Doanh thu thuần thực nhận | Đường nét hổ phách: Lợi nhuận gộp sau khi trừ giá vốn</p>
            </div>
            <div class="chart-control-cluster">
                <button type="button" class="btn-explain-formula" onclick="openModal('modalExplainFormula')" title="Bấm để xem công thức và cách tính lợi nhuận gộp chi tiết">
                    <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"></path><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>
                    <span>Cách tính doanh thu & lợi nhuận</span>
                </button>
                <span class="badge-unit">Đơn vị: VNĐ</span>
            </div>
        </div>
        <div style="height: 380px; position: relative;">
            <canvas id="revenueChart"></canvas>
        </div>
    </div>

    <!-- HÀNG CHỨA 2 BẢNG (TOP 10 SẢN PHẨM & TỒN KHO THEO DANH MỤC) -->
    <div class="report-tables-grid">
        <!-- TOP 10 SẢN PHẨM BÁN CHẠY -->
        <div class="report-section-card">
            <div class="section-card-header">
                <div style="display: flex; align-items: center; gap: 8px;">
                    <h3 class="section-card-title">Top 10 sản phẩm bán chạy</h3>
                    <span class="badge" style="background: #0f172a; color: #ffffff; font-size: 11px; padding: 3px 8px; border-radius: 6px;">Tháng ${thangTop5}/${filterYearTop5}</span>
                </div>
                <div style="display: flex; align-items: center; gap: 8px;">
                    <select name="thangTop5" id="thangTop5" form="filterReportForm" class="table-filter-select" onchange="submitReportFilter()">
                        <c:forEach var="m" begin="1" end="12">
                            <option value="${m}" ${thangTop5 == m ? 'selected' : ''}>Tháng ${m}</option>
                        </c:forEach>
                    </select>
                    <select name="namTop5" id="namTop5" form="filterReportForm" class="table-filter-select" onchange="submitReportFilter()">
                        <c:forEach var="y" begin="2020" end="2120">
                            <option value="${y}" ${filterYearTop5 == y ? 'selected' : ''}>Năm ${y}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="table-wrap">
                <table class="report-table">
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
                                        <td style="text-align: center; color: var(--muted); font-size: 12px;" class="font-mono">${item.thang}/${item.nam}</td>
                                        <td style="text-align: center; font-weight: 700;" class="font-mono">${item.daBan}</td>
                                        <td class="money font-mono th-money" style="font-weight: 700; color: var(--text-primary);">
                                            <fmt:formatNumber value="${item.doanhThu}" type="number" groupingUsed="true"/> đ
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="5" style="text-align: center; color: var(--muted); padding: 40px 16px;">Không có dữ liệu trong khoảng thời gian này</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- TỒN KHO THEO PHÂN LOẠI DANH MỤC -->
        <div class="report-section-card">
            <div class="section-card-header">
                <h3 class="section-card-title">Tồn kho theo Danh mục DUNA</h3>
                <span class="badge" style="background: #f1f5f9; color: #475569; font-size: 11px; padding: 3px 8px; border-radius: 6px; font-weight: 600;">Kho hàng</span>
            </div>
            <div class="table-wrap">
                <table class="report-table">
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
                                            <span class="badge" style="background: #f1f5f9; color: #1e293b; border: 1px solid #cbd5e1; font-size: 11.5px; padding: 3px 8px; border-radius: 6px; font-weight: 600;">${item.danhMuc}</span>
                                        </td>
                                        <td class="money font-mono th-money" style="font-weight: 700; font-size: 14px; color: var(--text-primary);">
                                            <fmt:formatNumber value="${item.soLuongTon}" type="number" groupingUsed="true"/> SP
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="2" style="text-align: center; color: var(--muted); padding: 40px 16px;">Không có dữ liệu tồn kho</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- DANH SÁCH SẢN PHẨM ĐÃ BÁN -->
    <div class="report-section-card" style="margin-bottom: 28px;">
        <div class="section-card-header">
            <div style="display: flex; align-items: center; gap: 8px;">
                <h3 class="section-card-title">Sổ kê chi tiết sản phẩm đã bán</h3>
                <span class="badge" style="background: #0f172a; color: #ffffff; font-size: 11px; padding: 3px 8px; border-radius: 6px;">Tháng ${thangDaBan}/${filterYearDaBan}</span>
            </div>
            <div style="display: flex; align-items: center; gap: 8px;">
                <label for="thangDaBan" style="font-size: 12.5px; color: var(--muted); font-weight: 600;">Lọc theo:</label>
                <select name="thangDaBan" id="thangDaBan" form="filterReportForm" class="table-filter-select" onchange="submitReportFilter()">
                    <c:forEach var="m" begin="1" end="12">
                        <option value="${m}" ${thangDaBan == m ? 'selected' : ''}>Tháng ${m}</option>
                    </c:forEach>
                </select>
                <select name="namDaBan" id="namDaBan" form="filterReportForm" class="table-filter-select" onchange="submitReportFilter()">
                    <c:forEach var="y" begin="2020" end="2120">
                        <option value="${y}" ${filterYearDaBan == y ? 'selected' : ''}>Năm ${y}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="table-wrap" style="max-height: 520px; overflow-y: auto;">
            <table class="report-table">
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
                                    <td style="text-align: center;">
                                        <span style="font-family: 'JetBrains Mono', monospace; font-size: 12px; font-weight: 700; color: #0f172a; background: #f1f5f9; border: 1px solid #e2e8f0; padding: 2px 7px; border-radius: 5px;">${item.maSP}</span>
                                    </td>
                                    <td style="font-weight: 600;">${item.tenSP}</td>
                                    <td style="text-align: center;">
                                        <span class="badge" style="background: #f1f5f9; color: #334155; border: 1px solid #cbd5e1; font-size: 11.5px; padding: 2px 8px; border-radius: 6px;">${not empty item.phanLoai ? item.phanLoai : 'Thời trang nam'}</span>
                                    </td>
                                    <td style="text-align: center; color: var(--muted); font-size: 12px;" class="font-mono">${item.thoiGian}</td>
                                    <td style="text-align: center; font-weight: 700;" class="font-mono">${item.soLuong}</td>
                                    <td class="money font-mono th-money" style="font-weight: 700; color: #15803d; font-size: 14px;">
                                        <fmt:formatNumber value="${item.thanhTien}" type="number" groupingUsed="true"/> đ
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="7" style="text-align: center; color: var(--muted); padding: 48px 16px;">Không có dữ liệu bán hàng trong khoảng thời gian này</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</main>

<!-- MODAL GIẢI THÍCH CÔNG THỨC DOANH THU & LỢI NHUẬN GỘP DUNA -->
<div class="modal-bg" id="modalExplainFormula">
    <div class="modal-box" style="max-width: 720px; width: 95%;">
        <button class="close-btn" type="button" onclick="closeModal('modalExplainFormula')">&times;</button>
        <div style="display: flex; align-items: center; gap: 14px; margin-bottom: 20px; border-bottom: 1px solid var(--border-subtle); padding-bottom: 16px;">
            <div style="width: 42px; height: 42px; border-radius: 10px; background: rgba(197, 160, 89, 0.15); color: #c5a059; display: flex; align-items: center; justify-content: center; flex-shrink: 0;">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"></path><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>
            </div>
            <div>
                <h3 style="margin: 0; font-size: 18px; font-weight: 800; color: var(--text-primary); letter-spacing: -0.01em;">Quy Tắc Tài Chính: Cách Tính Doanh Thu & Lợi Nhuận</h3>
                <div style="font-size: 13px; color: var(--muted); margin-top: 3px;">Phương pháp hạch toán sổ cái chuẩn chuỗi bán lẻ thời trang nam cao cấp DUNA</div>
            </div>
        </div>

        <div class="formula-grid">
            <!-- 1. Doanh thu thuần -->
            <div class="formula-card formula-revenue">
                <span class="formula-badge" style="color: #0f172a;">1. Doanh Thu Thuần (Net Revenue)</span>
                <div class="formula-math-box">
                    Doanh Thu = &Sigma; Tiền thực thu đơn "Đã thanh toán"
                </div>
                <div class="formula-desc">
                    Là tổng tiền khách hàng thực tế trả sau khi đã trừ mọi mã giảm giá Voucher và chiết khấu thành viên VIP. Các đơn hủy hoặc đơn chờ (chưa thanh toán) tuyệt đối không được ghi nhận vào doanh thu.
                </div>
            </div>

            <!-- 2. Giá vốn hàng bán -->
            <div class="formula-card formula-cogs">
                <span class="formula-badge" style="color: #475569;">2. Giá Vốn Hàng Bán (COGS)</span>
                <div class="formula-math-box">
                    Giá Vốn = &Sigma; (Giá nhập lúc bán &times; Số lượng bán)
                </div>
                <div class="formula-desc">
                    Hệ thống lưu vết chính xác giá vốn nhập kho của từng biến thể sản phẩm (Áo sơ mi, Blazer, Polo, Quần âu...) tại thời điểm xuất kho giao dịch, giúp kiểm soát chi phí nhập chính xác.
                </div>
            </div>

            <!-- 3. Lợi nhuận gộp -->
            <div class="formula-card formula-profit">
                <span class="formula-badge" style="color: #b45309;">3. Lợi Nhuận Gộp (Gross Profit)</span>
                <div class="formula-math-box" style="color: #b45309;">
                    Lợi Nhuận Gộp = Doanh Thu Thuần &minus; Giá Vốn Hàng Bán
                </div>
                <div class="formula-desc">
                    Khoản tiền thặng dư thực tế cửa hàng giữ lại sau khi đã bù đắp tiền vốn mua sản phẩm. Đây là nguồn ngân sách để trang trải chi phí nhân sự, mặt bằng, điện nước và lợi nhuận ròng.
                </div>
            </div>

            <!-- 4. Tỷ suất biên lợi nhuận -->
            <div class="formula-card formula-margin">
                <span class="formula-badge" style="color: #15803d;">4. Tỷ Suất Biên Lợi Nhuận (Margin %)</span>
                <div class="formula-math-box" style="color: #15803d;">
                    Biên Lợi Nhuận = (Lợi Nhuận Gộp / Doanh Thu) &times; 100%
                </div>
                <div class="formula-desc">
                    Đo lường hiệu quả sinh lời trên mỗi đồng doanh thu. Ví dụ: Biên lợi nhuận năm ${filterYear} đạt <b>${stat_profitMarginYear}%</b> nghĩa là cứ 100.000 đ doanh thu, cửa hàng DUNA thu về <b><fmt:formatNumber value="${not empty stat_totalRevenueYear and stat_totalRevenueYear > 0 ? (stat_grossProfitYear * 100000 / stat_totalRevenueYear) : 53400}" maxFractionDigits="0"/> đ</b> tiền lãi gộp.
                </div>
            </div>
        </div>

        <!-- Khung ví dụ số liệu thực tế năm đang xem -->
        <div style="background: var(--surface); border: 1px solid var(--border); border-radius: 8px; padding: 14px 18px; margin-top: 14px; font-size: 13px; line-height: 1.6;">
            <div style="font-weight: 700; color: var(--text-primary); margin-bottom: 4px; display: flex; align-items: center; gap: 6px;">
                <span style="display: inline-block; width: 8px; height: 8px; border-radius: 50%; background: #c5a059;"></span>
                Minh họa số liệu thực tế Năm ${filterYear} trên hệ thống DUNA:
            </div>
            <div style="color: var(--muted); display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; margin-top: 8px; font-family: 'JetBrains Mono', monospace; font-size: 12.5px;">
                <div>Doanh thu: <b style="color: var(--text-primary);"><fmt:formatNumber value="${not empty stat_totalRevenueYear ? stat_totalRevenueYear : 0}" type="number" groupingUsed="true"/> đ</b></div>
                <div>Lợi nhuận gộp: <b style="color: #15803d;"><fmt:formatNumber value="${not empty stat_grossProfitYear ? stat_grossProfitYear : 0}" type="number" groupingUsed="true"/> đ</b></div>
                <div>Tỷ suất biên: <b style="color: #c5a059;">${stat_profitMarginYear}%</b></div>
            </div>
        </div>

        <div class="modal-footer" style="margin-top: 20px; display: flex; justify-content: flex-end;">
            <button type="button" class="btn btn-primary" onclick="closeModal('modalExplainFormula')" style="padding: 8px 24px;">Đã hiểu</button>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" />

<script>
    function openModal(id) {
        var m = document.getElementById(id);
        if (m) m.classList.add('open');
    }
    function closeModal(id) {
        var m = document.getElementById(id);
        if (m) m.classList.remove('open');
    }

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

        document.querySelectorAll('.modal-bg').forEach(function(modal) {
            modal.addEventListener('click', function(event) {
                if (event.target === modal) modal.classList.remove('open');
            });
        });
    });
</script>
</body>
</html>
