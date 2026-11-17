<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Kho hàng DUNA — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="inventory" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M04 • Danh sách kho hàng &amp; tổng tồn kho (TV2 • UC088–UC090)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/inventory" class="btn btn-outline btn-sm">Tồn kho chi tiết</a>
            <a href="${pageContext.request.contextPath}/procurement" class="btn btn-secondary btn-sm">NCC &amp; Nhập hàng</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="post" action="${pageContext.request.contextPath}/warehouses" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="createWarehouse">
            <input type="text" name="warehouseCode" class="form-control" style="max-width:140px;" placeholder="Mã kho (KHO-HCM01)" required>
            <input type="text" name="warehouseName" class="form-control" style="max-width:220px;" placeholder="Tên kho" required>
            <input type="text" name="address" class="form-control" style="max-width:240px;" placeholder="Địa chỉ">
            <input type="text" name="phone" class="form-control" style="max-width:140px;" placeholder="SĐT">
            <button type="submit" class="btn btn-primary btn-sm">+ Thêm kho (UC089)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
