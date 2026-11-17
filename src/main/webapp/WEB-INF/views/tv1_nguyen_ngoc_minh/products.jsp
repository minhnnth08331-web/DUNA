<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Quản lý Sản phẩm — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="products" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M03 • Danh sách 100 sản phẩm thời trang nam tầm trung DUNA &amp; quản lý biến thể (TV1 • UC064–UC086)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/categories" class="btn btn-outline btn-sm">Danh mục &amp; Size</a>
            <a href="${pageContext.request.contextPath}/variants?productId=${selectedProductId}" class="btn btn-secondary btn-sm">UC076 Biến thể &amp; SKU</a>
            <a href="${pageContext.request.contextPath}/products?uc=UC074" class="btn btn-outline btn-sm">UC074 Top bán chạy</a>
            <a href="${pageContext.request.contextPath}/products?uc=UC084" class="btn btn-outline btn-sm">UC084 Thẻ Tag</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="get" action="${pageContext.request.contextPath}/products" class="filter-form" style="width:100%;">
            <input type="hidden" name="uc" value="UC065">
            <div class="search-box-wrap">
                <input type="text" name="keyword" class="search-box" placeholder="Tìm sản phẩm theo mã (SP001...) hoặc tên sản phẩm..." value="${param.keyword}">
            </div>
            <button type="submit" class="btn btn-primary btn-sm">Tìm kiếm</button>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline btn-sm">Làm mới</a>
        </form>
    </div>

    <div class="toolbar-section" style="margin-top:12px;">
        <form method="post" action="${pageContext.request.contextPath}/products" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="createProduct">
            <input type="text" name="productCode" class="form-control" style="max-width:110px;" placeholder="Mã SP" required>
            <input type="text" name="productName" class="form-control" style="max-width:240px;" placeholder="Tên sản phẩm nam mới" required>
            <select name="categoryId" class="form-control" style="max-width:170px;">
                <c:forEach items="${categories}" var="cat">
                    <option value="${cat.category_id}">${cat.category_name}</option>
                </c:forEach>
            </select>
            <input type="number" name="basePrice" class="form-control" style="max-width:130px;" placeholder="Giá bán" value="390000" required>
            <input type="number" name="costPrice" class="form-control" style="max-width:130px;" placeholder="Giá vốn" value="185000" required>
            <select name="formFit" class="form-control" style="max-width:130px;">
                <option value="SLIM_FIT">SLIM_FIT</option>
                <option value="REGULAR_FIT">REGULAR_FIT</option>
                <option value="OVERSIZE">OVERSIZE</option>
            </select>
            <button type="submit" class="btn btn-primary btn-sm">+ Thêm SP (UC068)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
