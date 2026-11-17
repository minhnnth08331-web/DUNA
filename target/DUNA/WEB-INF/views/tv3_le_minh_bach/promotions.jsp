<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Voucher & Khuyến mãi — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="promotions" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M09 • Quản lý Voucher giảm giá, Chương trình khuyến mãi &amp; Flash Sale (TV3 • UC191–UC210)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/promotions?uc=UC191" class="btn btn-outline btn-sm">UC191 Voucher</a>
            <a href="${pageContext.request.contextPath}/promotions?uc=UC198" class="btn btn-outline btn-sm">UC198 Thống kê Voucher</a>
            <a href="${pageContext.request.contextPath}/promotions?uc=UC199" class="btn btn-outline btn-sm">UC199 Khuyến mãi</a>
            <a href="${pageContext.request.contextPath}/promotions?uc=UC205" class="btn btn-secondary btn-sm">UC205 Flash Sale</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="post" action="${pageContext.request.contextPath}/promotions" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="createVoucher">
            <input type="text" name="voucherCode" class="form-control" style="max-width:140px;" placeholder="Mã (DUNA20)" required>
            <input type="text" name="voucherName" class="form-control" style="max-width:220px;" placeholder="Tên chương trình" required>
            <select name="discountType" class="form-control" style="max-width:130px;">
                <option value="PERCENT">Phần trăm (%)</option>
                <option value="FIXED">Số tiền (đ)</option>
            </select>
            <input type="number" name="discountValue" class="form-control" style="max-width:110px;" value="10">
            <input type="number" name="minOrderValue" class="form-control" style="max-width:130px;" value="400000">
            <button type="submit" class="btn btn-primary btn-sm">+ Tạo Voucher (UC192)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
