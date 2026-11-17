<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Danh mục & Thuộc tính — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="products" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M03 • Danh mục đa cấp, Bộ sưu tập, Thương hiệu, Màu sắc, Kích cỡ &amp; Tư vấn size (TV1 • UC044–UC063)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/categories?uc=UC044" class="btn btn-outline btn-sm">UC044 Danh mục</a>
            <a href="${pageContext.request.contextPath}/categories?uc=UC048" class="btn btn-outline btn-sm">UC048 Bộ sưu tập</a>
            <a href="${pageContext.request.contextPath}/categories?uc=UC052" class="btn btn-outline btn-sm">UC052 Thương hiệu</a>
            <a href="${pageContext.request.contextPath}/categories?uc=UC054" class="btn btn-outline btn-sm">UC054 Bảng màu</a>
            <a href="${pageContext.request.contextPath}/categories?uc=UC057" class="btn btn-outline btn-sm">UC057 Bảng size</a>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-sm">Quay lại Sản phẩm</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="post" action="${pageContext.request.contextPath}/categories" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="createCategory">
            <input type="text" name="categoryName" class="form-control" style="max-width:220px;" placeholder="Tên danh mục mới" required>
            <input type="text" name="slug" class="form-control" style="max-width:180px;" placeholder="slug-seo" required>
            <input type="text" name="description" class="form-control" style="max-width:260px;" placeholder="Mô tả">
            <button type="submit" class="btn btn-primary btn-sm">+ Thêm danh mục (UC045)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
