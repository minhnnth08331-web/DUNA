<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Tài chính & Báo cáo thống kê — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="finance" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M16 • Doanh thu, giá vốn, chi phí vận hành &amp; tổng quan điều hành (TV4 • UC279–UC296)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/finance?uc=UC296" class="btn btn-outline btn-sm">UC296 Top VIP</a>
            <a href="${pageContext.request.contextPath}/finance?uc=UC279" class="btn btn-outline btn-sm">UC279 Chi phí</a>
            <a href="${pageContext.request.contextPath}/finance?uc=UC286" class="btn btn-outline btn-sm">UC286 Theo tháng</a>
            <a href="${pageContext.request.contextPath}/finance?uc=UC291" class="btn btn-outline btn-sm">UC291 Giá trị kho</a>
            <a href="${pageContext.request.contextPath}/finance?action=export" class="btn btn-primary btn-sm">Xuất Excel</a>
        </div>
    </div>

    <c:if test="${not empty dashboard}">
        <div class="kpi-grid">
            <div class="stat-card"><div class="stat-label">Đơn hàng hôm nay</div><div class="stat-value">${dashboard.orders_today}</div></div>
            <div class="stat-card"><div class="stat-label">Doanh thu hôm nay (VNĐ)</div><div class="stat-value">${dashboard.revenue_today}</div></div>
            <div class="stat-card"><div class="stat-label">SKU sắp hết hàng</div><div class="stat-value">${dashboard.low_stock_items}</div></div>
            <div class="stat-card"><div class="stat-label">Ticket mở / Đổi trả chờ</div><div class="stat-value">${dashboard.open_tickets} / ${dashboard.pending_returns}</div></div>
        </div>
    </c:if>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
