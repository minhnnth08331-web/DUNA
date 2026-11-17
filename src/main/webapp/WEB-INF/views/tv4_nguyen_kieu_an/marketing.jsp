<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Banner & Blog — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M12 • Quản lý Banner quảng cáo, Blog phối đồ nam &amp; Bản tin (TV4 • UC237–UC246)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/marketing?uc=UC237" class="btn btn-outline btn-sm">UC237 Banner</a>
            <a href="${pageContext.request.contextPath}/marketing?uc=UC241" class="btn btn-secondary btn-sm">UC241 Bài viết</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="post" action="${pageContext.request.contextPath}/marketing" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="addBanner">
            <input type="text" name="title" class="form-control" style="max-width:260px;" placeholder="Tiêu đề banner" required>
            <select name="bannerPosition" class="form-control" style="max-width:160px;">
                <option value="HOME_HERO">HOME_HERO</option>
                <option value="HOME_SUB">HOME_SUB</option>
                <option value="CATEGORY_TOP">CATEGORY_TOP</option>
            </select>
            <button type="submit" class="btn btn-primary btn-sm">+ Thêm Banner (UC238)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
