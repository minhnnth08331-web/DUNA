<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Quản lý Đơn hàng & Hóa đơn — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="orders" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M06 • Danh sách đơn hàng đa kênh, xử lý trạng thái, hóa đơn VAT &amp; đổi trả (TV2 • UC126–UC151)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/orders?uc=UC126" class="btn btn-outline btn-sm">UC126 Đơn hàng</a>
            <a href="${pageContext.request.contextPath}/orders?uc=UC145" class="btn btn-outline btn-sm">UC145 Hóa đơn VAT</a>
            <a href="${pageContext.request.contextPath}/returns" class="btn btn-secondary btn-sm">Đổi trả hàng</a>
            <a href="${pageContext.request.contextPath}/order-export" class="btn btn-primary btn-sm">Xuất Excel (UC158)</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="get" action="${pageContext.request.contextPath}/orders" class="filter-form" style="width:100%;">
            <input type="hidden" name="uc" value="UC127">
            <div class="search-box-wrap">
                <input type="text" name="keyword" class="search-box" placeholder="Tìm đơn theo mã đơn, SĐT hoặc tên người nhận..." value="${param.keyword}">
            </div>
            <button type="submit" class="btn btn-primary btn-sm">Tìm đơn (UC127)</button>
        </form>
    </div>

    <div class="toolbar-section" style="margin-top:12px;">
        <form method="post" action="${pageContext.request.contextPath}/orders" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="createPhoneOrder">
            <input type="text" name="receiverName" class="form-control" style="max-width:200px;" placeholder="Tên người nhận" required>
            <input type="text" name="receiverPhone" class="form-control" style="max-width:140px;" placeholder="SĐT nhận" required>
            <input type="text" name="shippingAddress" class="form-control" style="max-width:240px;" placeholder="Địa chỉ giao hàng" required>
            <input type="number" name="subtotal" class="form-control" style="max-width:130px;" value="390000">
            <button type="submit" class="btn btn-primary btn-sm">+ Tạo đơn qua điện thoại (UC133)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
