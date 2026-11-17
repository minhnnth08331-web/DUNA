<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Quản lý Tồn kho — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="inventory" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M04 • Tồn kho biến thể, cảnh báo sắp hết hàng (${lowStockCount}) &amp; nhật ký xuất nhập (TV2 • UC087–UC099)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/inventory?uc=UC091" class="btn btn-outline btn-sm">UC091 Tồn kho</a>
            <a href="${pageContext.request.contextPath}/inventory?uc=UC093" class="btn btn-outline btn-sm">UC093 Cảnh báo thấp (${lowStockCount})</a>
            <a href="${pageContext.request.contextPath}/inventory?uc=UC097" class="btn btn-outline btn-sm">UC097 Nhật ký kho</a>
            <a href="${pageContext.request.contextPath}/warehouses" class="btn btn-secondary btn-sm">Kho hàng</a>
            <a href="${pageContext.request.contextPath}/procurement" class="btn btn-primary btn-sm">NCC &amp; Nhập PO</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="get" action="${pageContext.request.contextPath}/inventory" class="filter-form" style="width:100%;">
            <input type="hidden" name="uc" value="UC092">
            <div class="search-box-wrap">
                <input type="text" name="keyword" class="search-box" placeholder="Tra cứu tồn kho theo SKU hoặc mã vạch..." value="${param.keyword}">
            </div>
            <button type="submit" class="btn btn-primary btn-sm">Tra cứu (UC092)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
