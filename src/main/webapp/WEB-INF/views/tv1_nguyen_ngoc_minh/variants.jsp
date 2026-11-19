<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Biến thể SKU & Barcode — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="products" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M03 • Chi tiết biến thể màu/size, SKU, Barcode &amp; Giá bán riêng (TV1 • UC076–UC080)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline btn-sm">Nghiệp vụ M03</a>
            <a href="${pageContext.request.contextPath}/ProductServlet" class="btn btn-outline btn-sm">Quay lại Danh sách SP</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="get" action="${pageContext.request.contextPath}/variants" class="filter-form" style="width:100%;">
            <label style="font-size:12.5px;font-weight:600;color:var(--muted);">Mã SP:</label>
            <input type="number" name="productId" class="form-control" style="max-width:120px;" value="${selectedProductId}" placeholder="Mã SP">
            <div class="search-box-wrap">
                <input type="text" name="keyword" class="search-box" placeholder="Lọc theo SKU hoặc Barcode..." value="${param.keyword}">
            </div>
            <button type="submit" class="btn btn-primary btn-sm">Xem biến thể</button>
        </form>
    </div>

    <div class="toolbar-section" style="margin-top:12px;">
        <form method="post" action="${pageContext.request.contextPath}/products" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="addVariant">
            <input type="number" name="productId" class="form-control" style="max-width:110px;" value="${selectedProductId}" required>
            <select name="colorId" class="form-control" style="max-width:150px;">
                <c:forEach items="${colors}" var="cl">
                    <option value="${cl.color_id}">${cl.color_name}</option>
                </c:forEach>
            </select>
            <select name="sizeId" class="form-control" style="max-width:140px;">
                <c:forEach items="${sizes}" var="sz">
                    <option value="${sz.size_id}">${sz.size_code} (${sz.category_type})</option>
                </c:forEach>
            </select>
            <input type="text" name="sku" class="form-control" style="max-width:150px;" placeholder="SKU (tự sinh nếu trống)">
            <input type="number" name="price" class="form-control" style="max-width:130px;" value="390000">
            <button type="submit" class="btn btn-primary btn-sm">+ Thêm biến thể (UC077)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
