<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% if (request.getAttribute("listDH") == null) {
    response.sendRedirect(request.getContextPath() + "/OrderServlet"); return;
} %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý hóa đơn — DUNA Men's Wear</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@500;600;700;800&family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=JetBrains+Mono:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme.css?v=duna_v5">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css?v=duna_v5">
    <script>if (localStorage.getItem('duna_theme') === 'dark') document.documentElement.classList.add('dark-mode');</script>
    <style>
    /* ==========================================================================
       DUNA MEN'S WEAR — ORDERS & LEDGER HIGH PRECISION STYLES (OPERATE MODE)
       Elevated sartorial craftsmanship, generous spatial rhythm, zero slop
       ========================================================================== */
    .orders-page-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-end;
        gap: 24px;
        margin-bottom: 28px;
        padding-bottom: 20px;
        border-bottom: 1px solid var(--border);
        flex-wrap: wrap;
    }
    .orders-title-group h1 {
        font-size: 24px;
        font-weight: 800;
        letter-spacing: -0.02em;
        color: var(--text-primary);
        margin: 0 0 6px 0;
        line-height: 1.25;
    }
    .orders-title-group p {
        font-size: 13.5px;
        color: var(--muted);
        margin: 0;
        line-height: 1.5;
    }
    .btn-create-order {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        background: #0f172a;
        color: #ffffff !important;
        border: 1px solid #0f172a;
        padding: 10px 20px;
        border-radius: 8px;
        font-size: 13.5px;
        font-weight: 600;
        text-decoration: none;
        box-shadow: 0 2px 6px rgba(15, 23, 42, 0.12);
        transition: all 0.2s cubic-bezier(0.16, 1, 0.3, 1);
        white-space: nowrap;
    }
    .btn-create-order:hover {
        background: #1e293b;
        border-color: #1e293b;
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(15, 23, 42, 0.18);
        color: #ffffff !important;
    }

    /* KPI Stats Overview Cards */
    .orders-stats-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 22px;
        margin-bottom: 28px;
    }
    @media (max-width: 900px) {
        .orders-stats-grid {
            grid-template-columns: 1fr;
        }
    }
    .order-kpi-card {
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
    .order-kpi-card:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(15, 23, 42, 0.07);
    }
    .order-kpi-card.kpi-total {
        border-left: 4px solid #0f172a;
    }
    .order-kpi-card.kpi-completed {
        border-left: 4px solid #16a34a;
    }
    .order-kpi-card.kpi-cancelled {
        border-left: 4px solid #dc2626;
    }
    .order-kpi-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 8px;
    }
    .order-kpi-label {
        font-size: 11.5px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.06em;
        color: var(--muted);
    }
    .order-kpi-badge {
        font-size: 11px;
        font-weight: 600;
        padding: 2px 8px;
        border-radius: 9999px;
    }
    .kpi-total .order-kpi-badge {
        background: #f1f5f9;
        color: #475569;
    }
    .kpi-completed .order-kpi-badge {
        background: #dcfce7;
        color: #15803d;
    }
    .kpi-cancelled .order-kpi-badge {
        background: #fee2e2;
        color: #b91c1c;
    }
    .order-kpi-val {
        font-size: 32px;
        font-weight: 800;
        line-height: 1.15;
        letter-spacing: -0.02em;
        margin-bottom: 6px;
    }
    .kpi-total .order-kpi-val {
        color: var(--text-primary);
    }
    .kpi-completed .order-kpi-val {
        color: #15803d;
    }
    .kpi-cancelled .order-kpi-val {
        color: #b91c1c;
    }
    .order-kpi-desc {
        font-size: 12.5px;
        color: var(--muted);
        line-height: 1.4;
    }

    /* Filter Toolbar Card */
    .orders-filter-card {
        background: var(--surface);
        border: 1px solid var(--border);
        border-radius: 12px;
        padding: 20px 24px;
        margin-bottom: 28px;
        box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
    }
    .filter-grid-row {
        display: flex;
        align-items: flex-end;
        gap: 16px;
        flex-wrap: wrap;
    }
    .filter-field {
        display: flex;
        flex-direction: column;
        gap: 6px;
    }
    .filter-field label {
        font-size: 11.5px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        color: var(--muted);
    }
    .filter-search-wrap {
        flex: 1.4;
        min-width: 260px;
        position: relative;
    }
    .filter-input-with-icon {
        position: relative;
        display: flex;
        align-items: center;
    }
    .filter-input-with-icon svg {
        position: absolute;
        left: 12px;
        color: var(--faint);
        pointer-events: none;
    }
    .filter-input-with-icon input {
        width: 100%;
        height: 42px;
        padding-left: 36px;
        padding-right: 14px;
        border: 1px solid var(--control-border);
        border-radius: 8px;
        font-size: 13.5px;
        color: var(--text);
        background: var(--surface);
        transition: border-color 0.15s ease, box-shadow 0.15s ease;
        outline: none;
        box-sizing: border-box;
    }
    .filter-input-with-icon input:focus {
        border-color: #0f172a;
        box-shadow: 0 0 0 2px rgba(15, 23, 42, 0.08);
    }
    .filter-select {
        height: 42px;
        border: 1px solid var(--control-border);
        border-radius: 8px;
        padding: 0 14px;
        font-size: 13.5px;
        color: var(--text);
        background: var(--surface);
        min-width: 200px;
        outline: none;
        transition: border-color 0.15s ease;
        box-sizing: border-box;
    }
    .filter-select:focus {
        border-color: #0f172a;
    }
    .filter-actions {
        display: flex;
        align-items: center;
        gap: 8px;
        height: 42px;
    }
    .btn-filter-search {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        height: 42px;
        padding: 0 20px;
        background: #0f172a;
        color: #ffffff !important;
        border: 1px solid #0f172a;
        border-radius: 8px;
        font-size: 13px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.15s ease;
        box-sizing: border-box;
    }
    .btn-filter-search:hover {
        background: #1e293b;
        border-color: #1e293b;
    }
    .btn-filter-reset {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        height: 42px;
        padding: 0 16px;
        background: var(--surface);
        color: var(--text);
        border: 1px solid var(--control-border);
        border-radius: 8px;
        font-size: 13px;
        font-weight: 500;
        text-decoration: none;
        transition: all 0.15s ease;
        box-sizing: border-box;
    }
    .btn-filter-reset:hover {
        background: var(--raised);
        border-color: var(--text);
    }

    /* Orders Ledger Table Card */
    .orders-table-card {
        background: var(--surface);
        border: 1px solid var(--border);
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
        overflow: hidden;
        margin-bottom: 28px;
    }
    .orders-table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
        font-size: 13.5px;
    }
    .orders-table th {
        background: #f8fafc;
        color: #475569;
        font-size: 11.5px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        padding: 14px 16px;
        border-bottom: 1px solid var(--border);
        white-space: nowrap;
        text-align: left;
    }
    .orders-table th.th-money,
    .orders-table th.th-actions {
        text-align: right;
    }
    .orders-table td {
        padding: 14px 16px;
        border-bottom: 1px solid var(--rule);
        vertical-align: middle;
        color: var(--text);
    }
    .orders-table tr:last-child td {
        border-bottom: none;
    }
    .orders-table tbody tr {
        transition: background-color 0.15s ease;
    }
    .orders-table tbody tr:hover {
        background-color: #f8fafc;
    }

    /* Monospace Order Tag */
    .order-code-tag {
        font-family: 'JetBrains Mono', monospace;
        font-size: 12.5px;
        font-weight: 700;
        color: #0f172a;
        background: #f1f5f9;
        border: 1px solid #e2e8f0;
        padding: 3px 8px;
        border-radius: 6px;
        display: inline-block;
        letter-spacing: 0.02em;
        white-space: nowrap;
    }

    /* Product Details Row */
    .product-row-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 8px;
        padding: 4px 0;
        border-bottom: 1px dashed var(--border-subtle);
    }
    .product-row-item:last-child {
        border-bottom: none;
    }
    .btn-exchange-size {
        display: inline-flex;
        align-items: center;
        gap: 4px;
        padding: 2px 8px;
        font-size: 11px;
        font-weight: 600;
        color: #0f172a;
        background: #ffffff;
        border: 1px solid #cbd5e1;
        border-radius: 5px;
        cursor: pointer;
        transition: all 0.15s ease;
        white-space: nowrap;
        height: 24px;
    }
    .btn-exchange-size:hover {
        background: #0f172a;
        color: #ffffff;
        border-color: #0f172a;
    }

    /* Status Pills */
    .status-pill {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        padding: 4px 10px;
        border-radius: 9999px;
        font-size: 11.5px;
        font-weight: 600;
        white-space: nowrap;
    }
    .status-pill::before {
        content: '';
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 50%;
    }
    .status-pill.pill-success {
        background: #dcfce7;
        color: #15803d;
        border: 1px solid #86efac;
    }
    .status-pill.pill-success::before {
        background: #16a34a;
    }
    .status-pill.pill-warning {
        background: #fef3c7;
        color: #b45309;
        border: 1px solid #fde68a;
    }
    .status-pill.pill-warning::before {
        background: #d97706;
    }
    .status-pill.pill-danger {
        background: #fee2e2;
        color: #b91c1c;
        border: 1px solid #fca5a5;
    }
    .status-pill.pill-danger::before {
        background: #dc2626;
    }
    .status-pill.pill-neutral {
        background: #f1f5f9;
        color: #475569;
        border: 1px solid #cbd5e1;
    }
    .status-pill.pill-neutral::before {
        background: #64748b;
    }

    /* ACTION CELL - High Precision Flex Button Group (fixes vertical stacking) */
    .action-cell {
        text-align: right;
    }
    .action-btn-group {
        display: inline-flex;
        align-items: center;
        justify-content: flex-end;
        gap: 8px;
        flex-wrap: nowrap;
    }
    .form-action-cancel {
        display: inline-flex;
        margin: 0;
        padding: 0;
    }
    .btn-action-continue {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        background: #0f172a;
        color: #ffffff !important;
        border: 1px solid #0f172a;
        border-radius: 6px;
        padding: 6px 13px;
        font-size: 12px;
        font-weight: 600;
        text-decoration: none;
        white-space: nowrap;
        transition: all 0.15s ease;
        box-shadow: 0 1px 2px rgba(0,0,0,0.06);
        box-sizing: border-box;
    }
    .btn-action-continue:hover {
        background: #1e293b;
        border-color: #1e293b;
        color: #ffffff !important;
        transform: translateY(-1px);
        box-shadow: 0 2px 6px rgba(0,0,0,0.12);
    }
    .btn-action-cancel {
        display: inline-flex;
        align-items: center;
        gap: 5px;
        background: #ffffff;
        color: #dc2626;
        border: 1px solid #fecaca;
        border-radius: 6px;
        padding: 6px 12px;
        font-size: 12px;
        font-weight: 600;
        cursor: pointer;
        white-space: nowrap;
        transition: all 0.15s ease;
        box-sizing: border-box;
    }
    .btn-action-cancel:hover {
        background: #fef2f2;
        border-color: #f87171;
        color: #b91c1c;
        transform: translateY(-1px);
    }
    .btn-action-print {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        background: #ffffff;
        color: #334155;
        border: 1px solid #cbd5e1;
        border-radius: 6px;
        padding: 6px 13px;
        font-size: 12px;
        font-weight: 600;
        cursor: pointer;
        white-space: nowrap;
        transition: all 0.15s ease;
        box-sizing: border-box;
    }
    .btn-action-print:hover {
        background: #f8fafc;
        color: #0f172a;
        border-color: #94a3b8;
        transform: translateY(-1px);
    }
    .btn-action-print-cancel {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        background: #ffffff;
        color: #64748b;
        border: 1px solid #e2e8f0;
        border-radius: 6px;
        padding: 6px 13px;
        font-size: 12px;
        font-weight: 600;
        cursor: pointer;
        white-space: nowrap;
        transition: all 0.15s ease;
        box-sizing: border-box;
    }
    .btn-action-print-cancel:hover {
        background: #f1f5f9;
        color: #334155;
        border-color: #cbd5e1;
    }

    /* Dark Mode Overrides */
    html.dark-mode .orders-page-header {
        border-bottom-color: var(--border);
    }
    html.dark-mode .btn-create-order {
        background: #f8fafc;
        color: #0f172a !important;
        border-color: #f8fafc;
    }
    html.dark-mode .btn-create-order:hover {
        background: #e2e8f0;
        color: #0f172a !important;
    }
    html.dark-mode .orders-table th {
        background: #181d28;
        color: #94a3b8;
        border-bottom-color: var(--border);
    }
    html.dark-mode .orders-table tbody tr:hover {
        background-color: #1a202c;
    }
    html.dark-mode .order-code-tag {
        background: #1e293b;
        color: #f1f5f9;
        border-color: #334155;
    }
    html.dark-mode .btn-action-continue {
        background: #f8fafc;
        color: #0f172a !important;
        border-color: #f8fafc;
    }
    html.dark-mode .btn-action-continue:hover {
        background: #e2e8f0;
        color: #0f172a !important;
    }
    html.dark-mode .btn-action-cancel {
        background: rgba(248, 113, 113, 0.1);
        color: #f87171;
        border-color: rgba(248, 113, 113, 0.3);
    }
    html.dark-mode .btn-action-cancel:hover {
        background: rgba(248, 113, 113, 0.2);
        color: #fca5a5;
    }
    html.dark-mode .btn-action-print,
    html.dark-mode .btn-action-print-cancel {
        background: #181d28;
        color: #cbd5e1;
        border-color: #334155;
    }
    html.dark-mode .btn-action-print:hover,
    html.dark-mode .btn-action-print-cancel:hover {
        background: #1f2533;
        color: #ffffff;
        border-color: #475569;
    }
    html.dark-mode .btn-exchange-size {
        background: #181d28;
        color: #cbd5e1;
        border-color: #334155;
    }
    html.dark-mode .btn-exchange-size:hover {
        background: #ffffff;
        color: #0f172a;
    }
    html.dark-mode .btn-filter-search {
        background: #f8fafc;
        color: #0f172a !important;
        border-color: #f8fafc;
    }
    html.dark-mode .filter-input-with-icon input:focus {
        border-color: #c5a059;
        box-shadow: 0 0 0 2px rgba(197, 160, 89, 0.15);
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
            <c:choose>
                <c:when test="${sessionScope.role != 'MANAGER'}">
                    <a href="${pageContext.request.contextPath}/EmployeeDetailServlet?id=${not empty sessionScope.nhanVienID ? sessionScope.nhanVienID : 1}&mode=view">Thông tin cá nhân</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet?view=sell">Bán hàng</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet" class="active">Quản lý hóa đơn</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/ProductServlet">Quản lý sản phẩm</a>
                    <a href="${pageContext.request.contextPath}/EmployeeManagementServlet">Quản lý nhân sự</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet?view=report">Báo cáo thống kê</a>
                    <a href="${pageContext.request.contextPath}/CustomerServlet">Quản lý khách hàng</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet?view=sell">Bán hàng</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet" class="active">Quản lý hóa đơn</a>
                    <a href="${pageContext.request.contextPath}/VoucherServlet">Quản lý Voucher</a>
                </c:otherwise>
            </c:choose>
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

<div class="container">
    <c:if test="${param.created == '1'}">
        <div class="alert alert-success" style="margin-bottom: 24px;">Thanh toán thành công. Hóa đơn vừa tạo đã được lưu vào hệ thống sổ cái DUNA.</div>
    </c:if>

    <!-- PAGE HEADER: Flex layout, generous breathing room between Title and Action Button -->
    <div class="orders-page-header">
        <div class="orders-title-group">
            <h1>Quản Lý Hóa Đơn & Lịch Sử Giao Dịch</h1>
            <p>Theo dõi dòng tiền, đối soát giao dịch bán lẻ và hỗ trợ đổi size sản phẩm DUNA Men's Wear</p>
        </div>
        <div>
            <a class="btn-create-order" href="${pageContext.request.contextPath}/OrderServlet?view=sell">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
                <span>Lập hóa đơn mới</span>
            </a>
        </div>
    </div>

    <!-- STATS OVERVIEW CARDS: Clear metric hierarchy, distinct color accents, generous gap -->
    <div class="orders-stats-grid">
        <div class="order-kpi-card kpi-total">
            <div class="order-kpi-header">
                <span class="order-kpi-label">Tổng hóa đơn phát sinh</span>
                <span class="order-kpi-badge">Sổ cái DUNA</span>
            </div>
            <div class="order-kpi-val font-mono">${not empty stat_totalOrders ? stat_totalOrders : 0}</div>
            <div class="order-kpi-desc">Toàn bộ hồ sơ giao dịch ghi nhận trong hệ thống</div>
        </div>
        <div class="order-kpi-card kpi-completed">
            <div class="order-kpi-header">
                <span class="order-kpi-label">Đã thanh toán thành công</span>
                <span class="order-kpi-badge">Hoàn tất</span>
            </div>
            <div class="order-kpi-val font-mono">${not empty stat_completedOrders ? stat_completedOrders : 0}</div>
            <div class="order-kpi-desc">Đã nhận thanh toán đủ & xuất kho giao cho khách</div>
        </div>
        <div class="order-kpi-card kpi-cancelled">
            <div class="order-kpi-header">
                <span class="order-kpi-label">Hóa đơn đã hủy</span>
                <span class="order-kpi-badge">Đã hủy</span>
            </div>
            <div class="order-kpi-val font-mono">${not empty stat_cancelledOrders ? stat_cancelledOrders : 0}</div>
            <div class="order-kpi-desc">Đơn tạm bị hủy bởi khách hàng hoặc nhân viên thu ngân</div>
        </div>
    </div>

    <!-- FILTER TOOLBAR CARD: Clear field grouping, integrated icons, spacious rhythm -->
    <div class="orders-filter-card">
        <form method="get" action="${pageContext.request.contextPath}/OrderServlet" style="margin: 0;">
            <div class="filter-grid-row">
                <div class="filter-field filter-search-wrap">
                    <label>Tìm kiếm giao dịch</label>
                    <div class="filter-input-with-icon">
                        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
                        <input type="text" name="search" placeholder="Mã đơn hàng, tên khách hàng, SĐT..." value="${p_search}">
                    </div>
                </div>
                <div class="filter-field" style="width: 210px;">
                    <label>Trạng thái</label>
                    <select name="status" class="filter-select">
                        <option value="">Tất cả trạng thái</option>
                        <option value="Đã thanh toán" <c:if test="${p_status == 'Đã thanh toán'}">selected</c:if>>Đã thanh toán</option>
                        <option value="Chưa thanh toán" <c:if test="${p_status == 'Chưa thanh toán'}">selected</c:if>>Chưa thanh toán (Tạm giữ)</option>
                        <option value="Đã hủy" <c:if test="${p_status == 'Đã hủy'}">selected</c:if>>Đã hủy</option>
                    </select>
                </div>
                <div class="filter-field" style="width: 210px;">
                    <label>Phương thức thanh toán</label>
                    <select name="pay" class="filter-select">
                        <option value="">Tất cả phương thức</option>
                        <option value="Tiền mặt" <c:if test="${p_pay == 'Tiền mặt'}">selected</c:if>>Tiền mặt</option>
                        <option value="Chuyển khoản" <c:if test="${p_pay == 'Chuyển khoản'}">selected</c:if>>Chuyển khoản</option>
                    </select>
                </div>
                <div class="filter-actions">
                    <button type="submit" class="btn-filter-search">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
                        <span>Tra cứu</span>
                    </button>
                    <a href="${pageContext.request.contextPath}/OrderServlet" class="btn-filter-reset" title="Đặt lại bộ lọc">
                        <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="1 4 1 10 7 10"></polyline><polyline points="23 20 23 14 17 14"></polyline><path d="M20.49 9A9 9 0 0 0 5.64 5.64L1 10m22 4l-4.64 4.36A9 9 0 0 1 3.51 15"></path></svg>
                        <span>Làm mới</span>
                    </a>
                </div>
            </div>
        </form>
    </div>

    <!-- ORDERS LEDGER TABLE: Crisp typography, status pills with dot markers, flex action buttons -->
    <div id="ajax-content-area" style="transition: opacity 0.2s ease;">
        <div class="orders-table-card">
            <table class="orders-table">
                <thead>
                <tr>
                    <th style="width: 110px;">Mã HD</th>
                    <th style="width: 160px;">Khách hàng</th>
                    <th style="width: 140px;">Nhân viên</th>
                    <th>Chi tiết sản phẩm</th>
                    <th class="th-money" style="width: 140px;">Tổng tiền</th>
                    <th style="width: 120px;">Thanh toán</th>
                    <th>Ghi chú</th>
                    <th style="width: 140px;">Trạng thái</th>
                    <th style="width: 150px;">Thời điểm</th>
                    <th class="th-actions" style="width: 170px;">Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${listDH}" var="dh">
                    <c:set var="pillClass" value="pill-neutral"/>
                    <c:if test="${dh.trangThai == 'Đã thanh toán' or dh.trangThai == 'Hoàn thành' or dh.trangThai == 'Thanh toán'}"><c:set var="pillClass" value="pill-success"/></c:if>
                    <c:if test="${dh.trangThai == 'Chưa thanh toán'}"><c:set var="pillClass" value="pill-warning"/></c:if>
                    <c:if test="${dh.trangThai == 'Đã hủy'}"><c:set var="pillClass" value="pill-danger"/></c:if>
                    <tr>
                        <td>
                            <span class="order-code-tag">${dh.maDH}</span>
                        </td>
                        <td style="font-weight: 600;">${dh.tenKH}</td>
                        <td style="color: var(--muted);">${not empty dh.tenNV ? dh.tenNV : 'Admin'}</td>
                        <td class="product-cell" style="text-align: left;">
                            <c:forEach items="${dh.chiTiet}" var="ct">
                                <div class="product-row-item">
                                    <span style="font-size: 13px;">${ct.tenSP} <span class="font-mono" style="font-weight: 700; color: #0f172a;">x${ct.soLuong}</span></span>
                                    <c:if test="${dh.trangThai == 'Đã thanh toán'}">
                                        <button type="button" class="btn-exchange-size"
                                                onclick="openExchangeModal('${dh.id}','${ct.idChiTiet}','${ct.idSP}','${ct.tenSP}','${ct.donGia}')" title="Đổi sang size hoặc màu khác">
                                            <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="17 1 21 5 17 9"></polyline><path d="M3 11V9a4 4 0 0 1 4-4h14"></path><polyline points="7 23 3 19 7 15"></polyline><path d="M21 13v2a4 4 0 0 1-4 4H3"></path></svg>
                                            <span>Đổi Size</span>
                                        </button>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </td>
                        <td class="money font-mono th-money" style="font-weight: 700; font-size: 14.5px; color: var(--text-primary);"><fmt:formatNumber value="${dh.tongTien}" type="number" groupingUsed="true"/> đ</td>
                        <td>
                            <c:choose>
                                <c:when test="${dh.thanhToan == 'Chuyển khoản'}">
                                    <span class="badge" style="background: #e0f2fe; color: #0369a1; border: 1px solid #bae6fd; font-size: 11.5px; padding: 3px 8px; border-radius: 6px; font-weight: 600;">Chuyển khoản</span>
                                </c:when>
                                <c:when test="${dh.thanhToan == 'Tiền mặt'}">
                                    <span class="badge" style="background: #f1f5f9; color: #334155; border: 1px solid #cbd5e1; font-size: 11.5px; padding: 3px 8px; border-radius: 6px; font-weight: 600;">Tiền mặt</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-neutral" style="font-size: 11.5px; padding: 3px 8px; border-radius: 6px;">${dh.thanhToan}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td style="font-size: 12.5px; color: var(--muted); max-width: 220px; line-height: 1.45;">
                            <c:set var="noteArray" value="${fn:split(dh.ghiChu, '|')}"/>
                            ${not empty noteArray ? fn:trim(noteArray[0]) : 'Bán hàng tại quầy'}
                        </td>
                        <td>
                            <span class="status-pill ${pillClass} status">${dh.trangThai}</span>
                        </td>
                        <td class="font-mono" style="font-size: 12px; color: var(--muted);">${dh.ngayTao}</td>
                        <td class="action-cell">
                            <%-- Hóa đơn Đã thanh toán / Hoàn thành: hiển thị nút In phiếu --%>
                            <c:if test="${dh.trangThai == 'Đã thanh toán' or dh.trangThai == 'Hoàn thành' or dh.trangThai == 'Thanh toán'}">
                                <div class="action-btn-group">
                                    <button type="button" class="btn-action-print" onclick="printInvoice('${dh.id}')" title="In phiếu thanh toán DUNA">
                                        <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 6 2 18 2 18 9"></polyline><path d="M6 18H4a2 2 0 0 1-2-2v-5a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2"></path><rect x="6" y="14" width="12" height="8"></rect></svg>
                                        <span>In phiếu</span>
                                    </button>
                                </div>
                            </c:if>
                            <%-- Hóa đơn Chưa thanh toán (Hold): Tiếp tục + Hủy đặt ngang hàng (flex) với khoảng cách chuẩn 8px --%>
                            <c:if test="${dh.trangThai == 'Chưa thanh toán'}">
                                <div class="action-btn-group">
                                    <a class="btn-action-continue" href="${pageContext.request.contextPath}/OrderServlet?view=sell&holdId=${dh.id}" title="Mở đơn tạm tiếp tục thanh toán">
                                        <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polygon points="5 3 19 12 5 21 5 3"></polygon></svg>
                                        <span>Tiếp tục</span>
                                    </a>
                                    <form method="post" action="${pageContext.request.contextPath}/OrderServlet" onsubmit="return cancelOrder(this, '${dh.id}')" class="form-action-cancel">
                                        <input type="hidden" name="action" value="changeStatus">
                                        <input type="hidden" name="id" value="${dh.id}">
                                        <input type="hidden" name="trangThai" value="Đã hủy">
                                        <button type="submit" class="btn-action-cancel" title="Hủy đơn chờ này">
                                            <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
                                            <span>Hủy</span>
                                        </button>
                                    </form>
                                </div>
                            </c:if>
                            <%-- Hóa đơn Đã hủy: hiển thị nút in biên bản --%>
                            <c:if test="${dh.trangThai == 'Đã hủy'}">
                                <div class="action-btn-group">
                                    <button type="button" class="btn-action-print-cancel" onclick="printInvoice('${dh.id}')" title="In hồ sơ đã hủy">
                                        <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="16" y1="13" x2="8" y2="13"></line><line x1="16" y1="17" x2="8" y2="17"></line></svg>
                                        <span>In biên bản</span>
                                    </button>
                                </div>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty listDH}">
                    <tr><td colspan="10" style="text-align:center; color: var(--muted); padding: 56px 16px;">
                        <div style="font-size: 15px; font-weight: 600; margin-bottom: 6px; color: var(--text);">Không tìm thấy hóa đơn nào</div>
                        <div style="font-size: 13px; color: var(--muted);">Thử thay đổi từ khóa tìm kiếm hoặc đặt lại bộ lọc trạng thái</div>
                    </td></tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <c:if test="${totalPages > 1}">
            <c:set var="urlParams" value="${not empty p_search ? '&search='.concat(p_search) : ''}${not empty p_status ? '&status='.concat(p_status) : ''}${not empty p_pay ? '&pay='.concat(p_pay) : ''}" />
            <div class="pagination-bar">
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <a href="?page=${currentPage - 1}${urlParams}" title="Trang trước">&#10094;</a>
                    </c:when>
                    <c:otherwise>
                        <span class="page-arrow disabled">&#10094;</span>
                    </c:otherwise>
                </c:choose>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a class="${currentPage == i ? 'active' : ''}" href="?page=${i}${urlParams}">${i}</a>
                </c:forEach>
                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a href="?page=${currentPage + 1}${urlParams}" title="Trang sau">&#10095;</a>
                    </c:when>
                    <c:otherwise>
                        <span class="page-arrow disabled">&#10095;</span>
                    </c:otherwise>
                </c:choose>
                <div class="page-jump-box">
                    <span>Đến trang:</span>
                    <input type="number" min="1" max="${totalPages}" placeholder="Số" onkeydown="if(event.key==='Enter') jumpToPage(this, '${urlParams}', ${totalPages})">
                    <button type="button" onclick="jumpToPage(this.previousElementSibling, '${urlParams}', ${totalPages})">Đi &#10140;</button>
                </div>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="footer.jsp" />

<!-- MODAL IN HÓA ĐƠN DUNA -->
<div class="modal-bg" id="modalPrint">
    <div class="modal-box" id="printContent" style="max-width: 680px;">
        <button class="close-btn no-print" type="button" onclick="closeModal('modalPrint')">×</button>
        <div class="invoice-header" style="text-align: center; border-bottom: 1px dashed var(--color-border); padding-bottom: 16px; margin-bottom: 16px;">
            <div style="font-family: 'Cinzel', serif; font-size: 20px; font-weight: 800; letter-spacing: 0.12em; color: var(--color-primary);">DUNA MEN'S WEAR</div>
            <div style="font-size: 12px; color: var(--color-text-muted); margin-top: 4px;">Sartorial Tailoring & Precision | Hotline: 1900 8888 | duna.vn</div>
            <div style="font-size: 17px; font-weight: 700; text-transform: uppercase; margin: 12px 0 6px; letter-spacing: 0.05em;">Phiếu Thanh Toán Bán Lẻ</div>
            <div class="font-mono" style="font-size: 13px; font-weight: 700; color: var(--color-primary);" id="prMaDH">Mã HD: ...</div>
            <div class="font-mono" style="font-size: 12px; color: var(--color-text-muted);" id="prNgayTao">Ngày tạo: ...</div>
        </div>
        <div style="font-size: 13.5px; line-height: 1.7; margin-bottom: 16px; background: var(--color-bg-subtle); padding: 12px 16px; border-radius: var(--radius-sm);">
            <div><span style="color: var(--color-text-muted);">Khách hàng:</span> <b id="prTenKH" style="color: var(--color-text);">...</b></div>
            <div><span style="color: var(--color-text-muted);">Số điện thoại:</span> <span class="font-mono" id="prSdtKH">...</span></div>
            <div><span style="color: var(--color-text-muted);">Địa chỉ:</span> <span id="prDiaChiKH">...</span></div>
        </div>
        <table class="data-table invoice-tbl" style="margin-bottom: 16px;">
            <thead>
                <tr>
                    <th style="width: 40px; text-align: center;">STT</th>
                    <th>Tên sản phẩm</th>
                    <th style="white-space:nowrap;">Biến thể</th>
                    <th style="width: 50px; text-align: center;">SL</th>
                    <th class="th-money" style="white-space:nowrap;">Đơn giá</th>
                    <th class="th-money" style="white-space:nowrap;">Thành tiền</th>
                </tr>
            </thead>
            <tbody id="prBody"></tbody>
        </table>
        <div style="display: flex; justify-content: flex-end; margin: 16px 0; font-size: 13.5px; line-height: 1.8;">
            <div style="min-width: 320px; text-align: right;">
                <div><span style="color: var(--color-text-muted);">Thuế GTGT (<span id="prVatPctLabel">${not empty currentVat ? currentVat : 8}</span>%):</span> <span class="font-mono" id="prVAT" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
                <div><span style="color: var(--color-text-muted);">Ưu đãi VIP:</span> <span class="font-mono" id="prGiamGia" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
                <div><span style="color: var(--color-text-muted);">Mã giảm giá / Voucher:</span> <span class="font-mono" id="prVoucher" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
                <div style="font-size: 18px; font-weight: 800; border-top: 1px dashed var(--color-border); border-bottom: 1px dashed var(--color-border); padding: 8px 0; margin: 8px 0; color: var(--color-primary);">
                    Tổng thanh toán: <span class="font-mono" id="prTongTien" style="color: var(--color-accent); display: inline-block; min-width: 130px;">0 đ</span>
                </div>
                <div><span style="color: var(--color-text-muted);">Tiền khách đưa:</span> <span class="font-mono" id="prKhachDua" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
                <div><span style="color: var(--color-text-muted);">Tiền hoàn lại:</span> <span class="font-mono" id="prTienThua" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
            </div>
        </div>
        <div style="font-size: 12.5px; margin-bottom: 24px; line-height: 1.6; border-top: 1px solid var(--color-border-subtle); padding-top: 12px;">
            <div><b>Phương thức thanh toán:</b> <span id="prThanhToan">...</span></div>
            <div><b>Trạng thái:</b> <span id="prTrangThai">...</span></div>
            <div><b>Thu ngân phục vụ:</b> <span id="prTenNV">...</span></div>
            <div><b>Ghi chú:</b> <span id="prGhiChu">...</span></div>
        </div>
        <div class="modal-footer no-print">
            <button type="button" class="btn btn-outline" onclick="closeModal('modalPrint')">Đóng</button>
            <button type="button" class="btn btn-primary" id="btnDoPrintModal" onclick="window.print()">In ngay</button>
        </div>
    </div>
</div>

<!-- MODAL ĐỔI SIZE / MÀU SẮC DUNA -->
<div class="modal-bg" id="modalExchange">
    <div class="modal-box" style="width: 560px;">
        <button class="close-btn" type="button" onclick="closeModal('modalExchange')">×</button>
        <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 18px; border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 14px;">
            <div>
                <h3 style="margin: 0; font-size: 18px; font-weight: 700; color: var(--color-text);">Đổi Trả Hàng & Đổi Size Áo/Quần DUNA</h3>
                <div style="font-size: 12.5px; color: var(--color-text-muted); margin-top: 2px;">Hoàn 1 sản phẩm cũ về kho và xuất 1 sản phẩm biến thể mới</div>
            </div>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/OrderServlet" id="frmExchange" onsubmit="return validateExchange();">
            <input type="hidden" name="action" value="exchangeItem">
            <input type="hidden" name="idDH" id="exIdDH">
            <input type="hidden" name="idCT" id="exIdCT">

            <div style="background: var(--color-bg-subtle); border: 1px solid var(--color-border-subtle); border-radius: var(--radius-sm); padding: 14px 16px; margin-bottom: 16px; font-size: 13.5px; line-height: 1.6;">
                <div>Sản phẩm hiện tại: <b id="exCurrentName" style="color: var(--color-text);">--</b></div>
                <div>Đơn giá đã thanh toán: <b id="exCurrentPrice" class="font-mono" style="color: var(--color-primary);">-- đ</b></div>
            </div>

            <div class="form-group" style="margin-bottom: 16px;">
                <label class="form-label">Chọn Size / Màu mới cần đổi sang:</label>
                <select name="idBienTheMoi" id="exSelectNewVariant" class="form-select" onchange="calcExchangeDiff()">
                    <option value="">Đang tải danh sách biến thể...</option>
                </select>
            </div>

            <!-- Khung chênh lệch giá -->
            <div id="exDiffBox" style="background: var(--color-surface); border: 1px solid var(--color-border); border-radius: var(--radius-sm); padding: 14px 16px; margin-bottom: 20px; font-size: 13.5px; display: flex; justify-content: space-between; align-items: center;">
                <span style="font-weight: 600; color: var(--color-text);">Chênh lệch thanh toán:</span>
                <span id="exDiffAmount" class="font-mono" style="font-weight: 700; font-size: 15px; color: var(--color-primary);">0 đ (Đổi ngang)</span>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="closeModal('modalExchange')">Hủy bỏ</button>
                <button type="submit" class="btn btn-primary">Xác nhận đổi hàng</button>
            </div>
        </form>
    </div>
</div>

<script>
function money(value) {
    return Number(value || 0).toLocaleString('vi-VN') + ' đ';
}

function printInvoice(id) {
    fetch('${pageContext.request.contextPath}/OrderServlet?ajaxDetail=1&id=' + id)
        .then(function(response) { return response.json(); })
        .then(function(data) {
            if (data.trangThai === 'Chưa thanh toán') {
                alert('Hóa đơn đang ở trạng thái "Chưa thanh toán" nên không được phép in!\n\nVui lòng hoàn tất thanh toán cho hóa đơn trước khi in.');
                return;
            }
            var btnPrintModal = document.getElementById('btnDoPrintModal');
            if (btnPrintModal) {
                btnPrintModal.style.display = 'inline-block';
            }
            document.getElementById('prMaDH').textContent = 'Mã HD: ' + (data.maDH || '');
            document.getElementById('prNgayTao').textContent = 'Ngày tạo: ' + (data.ngayTao || '');
            document.getElementById('prTenKH').textContent = data.tenKH || 'Khách lẻ';
            document.getElementById('prSdtKH').textContent = data.sdtKH || '-';
            document.getElementById('prDiaChiKH').textContent = data.diaChiKH || '-';
            document.getElementById('prThanhToan').textContent = data.thanhToan || '-';
            document.getElementById('prTrangThai').textContent = data.trangThai || '-';
            if (document.getElementById('prTenNV')) document.getElementById('prTenNV').textContent = data.tenNV || 'Admin';

            var noteStr = data.ghiChu || '';
            var parts = noteStr.split('|');
            var cleanNotes = [];
            var vatVal = '', giamGiaVal = '', voucherVal = '', khachDuaVal = '', tienThuaVal = '';
            
            parts.forEach(function(p) {
                var pTrim = p.trim();
                if (pTrim.indexOf('VAT') > -1 && pTrim.indexOf(':') > -1) {
                    vatVal = pTrim.substring(pTrim.indexOf(':') + 1).trim();
                    var pctMatch = pTrim.match(/VAT \(([0-9]+)%\)/i);
                    if (pctMatch && document.getElementById('prVatPctLabel')) {
                        document.getElementById('prVatPctLabel').textContent = pctMatch[1];
                    }
                } else if (pTrim.toLowerCase().indexOf('voucher') > -1 && pTrim.indexOf(':') > -1) {
                    var idx = pTrim.indexOf(':');
                    var vMoney = pTrim.substring(idx + 1).trim().replace(/^-/,'');
                    voucherVal = '- ' + vMoney;
                    var vCodeMatch = pTrim.match(/\(([^)]+)\)/);
                    if (vCodeMatch && (!data.voucherCode || data.voucherCode === "")) {
                        data.voucherCode = vCodeMatch[1];
                    }
                } else if (pTrim.toLowerCase().indexOf('giảm giá') > -1 && pTrim.indexOf(':') > -1) {
                    giamGiaVal = pTrim.substring(pTrim.indexOf(':') + 1).trim();
                } else if (pTrim.toLowerCase().indexOf('khách đưa') > -1 && pTrim.indexOf(':') > -1) {
                    khachDuaVal = pTrim.substring(pTrim.indexOf(':') + 1).trim();
                } else if (pTrim.toLowerCase().indexOf('tiền thừa') > -1 && pTrim.indexOf(':') > -1) {
                    tienThuaVal = pTrim.substring(pTrim.indexOf(':') + 1).trim();
                } else if (pTrim !== '') {
                    cleanNotes.push(pTrim);
                }
            });

            if (!vatVal) {
                var curVat = ${not empty currentVat ? currentVat : 8};
                var calculatedVAT = Math.round((data.tongTien || 0) * curVat / (100 + curVat));
                vatVal = Number(calculatedVAT).toLocaleString('vi-VN') + ' VNĐ';
                if (document.getElementById('prVatPctLabel')) document.getElementById('prVatPctLabel').textContent = curVat;
            }
            if (!giamGiaVal) giamGiaVal = '0 VNĐ';
            if (!voucherVal) voucherVal = '0 VNĐ';
            if (!khachDuaVal) khachDuaVal = Number(data.tongTien || 0).toLocaleString('vi-VN') + ' VNĐ';
            if (!tienThuaVal) tienThuaVal = '0 VNĐ';
            var finalGhiChu = cleanNotes.join(' - ');
            if (!finalGhiChu) finalGhiChu = 'Bán hàng tại quầy';

            if (vatVal && vatVal.indexOf('+') === -1 && parseInt(vatVal.replace(/[^0-9]/g, ''), 10) > 0) {
                vatVal = '+ ' + vatVal.trim();
            }
            if (giamGiaVal && giamGiaVal.indexOf('-') === -1 && parseInt(giamGiaVal.replace(/[^0-9]/g, ''), 10) > 0) {
                giamGiaVal = '- ' + giamGiaVal.trim();
            }

            var elVAT = document.getElementById('prVAT');
            var elGiamGia = document.getElementById('prGiamGia');
            var elVoucher = document.getElementById('prVoucher');
            var elKhachDua = document.getElementById('prKhachDua');
            var elTienThua = document.getElementById('prTienThua');
            if (elVAT) elVAT.textContent = vatVal;
            if (elGiamGia) elGiamGia.textContent = giamGiaVal;
            if (elVoucher) {
                if (data.voucherCode && data.voucherCode !== "") {
                    var vDesc = data.voucherCode + (data.voucherName ? " - " + data.voucherName : "");
                    var isMgr = ${sessionScope.role == 'MANAGER' or sessionScope.roleID == 1};
                    var badge = isMgr ? ('<a href="${pageContext.request.contextPath}/VoucherServlet?search=' + data.voucherCode + '" target="_blank" class="no-print" style="background:var(--color-bg-subtle);color:var(--color-primary);padding:2px 7px;border-radius:4px;font-size:12px;text-decoration:none;border:1px solid var(--color-border);font-weight:600;margin-left:4px;" title="Mở sang trang Quản lý Voucher">[' + vDesc + ']</a>') : ('<span class="no-print" style="background:var(--color-bg-subtle);color:var(--color-primary);padding:2px 7px;border-radius:4px;font-size:12px;border:1px solid var(--color-border);font-weight:600;margin-left:4px;">[' + vDesc + ']</span>');
                    elVoucher.innerHTML = '<span style="color:var(--color-danger);font-weight:700;">' + voucherVal + '</span> ' + badge + '<span style="display:none;color:var(--color-text);font-size:12px;" class="print-voucher"> (' + vDesc + ')</span>';
                } else {
                    elVoucher.textContent = voucherVal;
                }
            }
            if (elKhachDua) elKhachDua.textContent = khachDuaVal;
            if (elTienThua) elTienThua.textContent = tienThuaVal;
            document.getElementById('prGhiChu').textContent = finalGhiChu;

            document.getElementById('prTongTien').textContent = money(data.tongTien || 0);
            document.getElementById('prBody').innerHTML = (data.chiTiet || []).map(function(item, index) {
                var tenSP = item.tenSP;
                var bienThe = '-';
                var match = tenSP.match(/^(.*?)\s*\(([^)]+)\)$/);
                if (match) {
                    tenSP = match[1];
                    bienThe = match[2];
                }
                return '<tr><td style="text-align:center;">' + (index + 1) + '</td><td>' + tenSP + '</td><td style="white-space:nowrap;">' + bienThe + '</td><td style="text-align:center;" class="font-mono">' + item.soLuong + '</td><td class="money font-mono">' + money(item.donGia) + '</td><td class="money font-mono">' + money(item.thanhTien) + '</td></tr>';
            }).join('');
            openModal('modalPrint');
        })
        .catch(function() { alert('Không lấy được dữ liệu hóa đơn.'); });
}

function openModal(id) { document.getElementById(id).classList.add('open'); }
function closeModal(id) { document.getElementById(id).classList.remove('open'); }
document.querySelectorAll('.modal-bg').forEach(function(modal) {
    modal.addEventListener('click', function(event) {
        if (event.target === modal) modal.classList.remove('open');
    });
});

function cancelOrder(form, orderId) {
    if (!confirm('Bạn có chắc chắn muốn hủy hóa đơn này?')) return false;
    
    var btn = form.querySelector('button');
    if (btn) {
        btn.disabled = true;
        btn.innerHTML = '<span>Đang xử lý...</span>';
    }
    
    var formData = new URLSearchParams();
    formData.append('action', 'changeStatus');
    formData.append('id', orderId);
    formData.append('trangThai', 'Đã hủy');
    
    fetch('${pageContext.request.contextPath}/OrderServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData.toString()
    }).then(function(response) {
        if (response.ok) {
            var row = form.closest('tr');
            if (row) {
                var statusSpan = row.querySelector('.status');
                if (statusSpan) {
                    statusSpan.className = 'status-pill pill-danger status';
                    statusSpan.textContent = 'Đã hủy';
                    statusSpan.style.whiteSpace = 'nowrap';
                }
                var actionCell = row.querySelector('.action-cell');
                if (actionCell) {
                    actionCell.innerHTML = '<div class="action-btn-group"><button type="button" class="btn-action-print-cancel" onclick="printInvoice(\'' + orderId + '\')" title="In hồ sơ đã hủy"><svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="16" y1="13" x2="8" y2="13"></line><line x1="16" y1="17" x2="8" y2="17"></line></svg><span>In biên bản</span></button></div>';
                }
                var cells = row.querySelectorAll('td');
                if (cells.length >= 8) {
                    cells[5].innerHTML = '<span class="badge" style="background: #f1f5f9; color: #64748b; font-size: 11.5px; padding: 3px 8px; border-radius: 6px;">Không có</span>';
                    cells[6].textContent = 'Hóa đơn chờ bị hủy do khách hàng hủy đơn hàng';
                }
            }
            var cancelStatEl = document.querySelector('.orders-stats-grid .kpi-cancelled .order-kpi-val');
            if (cancelStatEl && !isNaN(parseInt(cancelStatEl.textContent))) {
                cancelStatEl.textContent = parseInt(cancelStatEl.textContent) + 1;
            }
        } else {
            alert('Có lỗi xảy ra khi hủy hóa đơn.');
            if (btn) { btn.disabled = false; btn.innerHTML = '<svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg><span>Hủy</span>'; }
        }
    }).catch(function(e) {
        console.error(e);
        alert('Có lỗi kết nối khi hủy hóa đơn.');
        if (btn) { btn.disabled = false; btn.innerHTML = '<svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg><span>Hủy</span>'; }
    });
    return false;
}
</script>

<script>
var currentExOldPrice = 0;
var exVariantList = [];

function openExchangeModal(idDH, idCT, idSP, tenSP, donGia) {
    document.getElementById('exIdDH').value = idDH;
    document.getElementById('exIdCT').value = idCT;
    document.getElementById('exCurrentName').textContent = tenSP;
    currentExOldPrice = Number(donGia || 0);
    document.getElementById('exCurrentPrice').textContent = currentExOldPrice.toLocaleString('vi-VN') + ' đ';

    var sel = document.getElementById('exSelectNewVariant');
    sel.innerHTML = '<option value="">Đang tải danh sách biến thể còn hàng...</option>';

    fetch('${pageContext.request.contextPath}/OrderServlet?ajaxVariantsExchange=1&idSP=' + (idSP || ''))
        .then(function(r){ return r.json(); })
        .then(function(data){
            exVariantList = data || [];
            if (!data || data.length === 0) {
                sel.innerHTML = '<option value="">Không có biến thể nào còn tồn kho để đổi!</option>';
            } else {
                sel.innerHTML = data.map(function(v, idx){
                    return '<option value="' + v.idBT + '" data-price="' + v.giaBan + '" ' + (idx === 0 ? 'selected' : '') + '>'
                        + v.tenSP + ' — Màu: ' + v.mauSac + ' | Size: ' + v.size + ' (Giá: ' + Number(v.giaBan).toLocaleString('vi-VN') + ' đ | Còn: ' + v.soLuong + ')'
                        + '</option>';
                }).join('');
                calcExchangeDiff();
            }
        });

    openModal('modalExchange');
}

function calcExchangeDiff() {
    var sel = document.getElementById('exSelectNewVariant');
    var opt = sel.options[sel.selectedIndex];
    if (!opt) return;
    var newPrice = Number(opt.getAttribute('data-price') || 0);
    var diff = newPrice - currentExOldPrice;
    var diffEl = document.getElementById('exDiffAmount');

    if (diff > 0) {
        diffEl.textContent = '+ ' + diff.toLocaleString('vi-VN') + ' đ (Khách nạp thêm)';
        diffEl.style.color = 'var(--color-danger)';
    } else if (diff < 0) {
        diffEl.textContent = '- ' + Math.abs(diff).toLocaleString('vi-VN') + ' đ (Hoàn lại khách)';
        diffEl.style.color = 'var(--color-success)';
    } else {
        diffEl.textContent = '0 đ (Đổi ngang cùng giá)';
        diffEl.style.color = 'var(--color-primary)';
    }
}

function validateExchange() {
    var sel = document.getElementById('exSelectNewVariant');
    if (!sel.value) {
        alert('Vui lòng chọn một biến thể mới còn hàng để đổi!');
        return false;
    }
    return confirm('Bạn có chắc chắn muốn tiến hành đổi hàng? Hệ thống sẽ cập nhật kho và hóa đơn tức thì.');
}
</script>
</body>
</html>
