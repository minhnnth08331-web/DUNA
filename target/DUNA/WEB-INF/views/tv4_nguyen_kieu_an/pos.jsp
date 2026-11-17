<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Bán hàng POS Quầy — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="pos" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M14 • Quầy thu ngân POS, quét mã vạch, mở/đóng ca &amp; cửa hàng bán lẻ (TV4 • UC257–UC266)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/pos?uc=UC261" class="btn btn-outline btn-sm">UC261 Quét Barcode</a>
            <a href="${pageContext.request.contextPath}/pos?uc=UC257" class="btn btn-outline btn-sm">UC257 Cửa hàng</a>
            <a href="${pageContext.request.contextPath}/pos?uc=UC265" class="btn btn-outline btn-sm">UC265 Doanh thu ca</a>
            <a href="${pageContext.request.contextPath}/pos?uc=UC266" class="btn btn-secondary btn-sm">UC266 Lịch sử ca</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="get" action="${pageContext.request.contextPath}/pos" class="filter-form" style="width:100%;">
            <input type="hidden" name="uc" value="UC261">
            <div class="search-box-wrap">
                <input type="text" name="keyword" class="search-box" placeholder="Quét barcode (8936...), SKU (SP001...) hoặc gõ tên sản phẩm..." value="${param.keyword}" autofocus>
            </div>
            <button type="submit" class="btn btn-primary btn-sm">Tìm / Quét (UC261)</button>
        </form>
    </div>

    <div class="toolbar-section" style="margin-top:12px;">
        <form method="post" action="${pageContext.request.contextPath}/pos" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="openSession">
            <select name="storeId" class="form-control" style="max-width:200px;">
                <c:forEach items="${stores}" var="st">
                    <option value="${st.store_id}">${st.store_name}</option>
                </c:forEach>
            </select>
            <input type="number" name="openingCash" class="form-control" style="max-width:160px;" value="2000000" placeholder="Tiền đầu ca">
            <button type="submit" class="btn btn-secondary btn-sm">Mở ca POS (UC260)</button>
        </form>
    </div>

    <div class="toolbar-section" style="margin-top:12px;">
        <form method="post" action="${pageContext.request.contextPath}/pos" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="createPosOrder">
            <input type="number" name="variantId" class="form-control" style="max-width:120px;" value="1" placeholder="Variant ID" required>
            <input type="number" name="quantity" class="form-control" style="max-width:100px;" value="1" min="1" required>
            <input type="number" name="customerId" class="form-control" style="max-width:120px;" value="1" placeholder="Customer ID">
            <button type="submit" class="btn btn-primary btn-sm">Thanh toán tại quầy (UC262)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
