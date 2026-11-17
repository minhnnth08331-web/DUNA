<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Vai trò & Phân quyền — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M01 • Quản lý vai trò, quyền hạn và ma trận phân quyền RBAC (TV1 • UC001–UC010)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/roles?uc=UC001" class="btn btn-outline btn-sm">UC001 Vai trò</a>
            <a href="${pageContext.request.contextPath}/roles?uc=UC005" class="btn btn-outline btn-sm">UC005 Quyền hạn</a>
            <a href="${pageContext.request.contextPath}/roles?uc=UC009&roleId=1" class="btn btn-secondary btn-sm">UC009 Ma trận quyền</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="get" action="${pageContext.request.contextPath}/roles" class="filter-form" style="width:100%;">
            <input type="hidden" name="uc" value="${activeUc}">
            <div class="search-box-wrap">
                <input type="text" name="keyword" class="search-box" placeholder="Tìm vai trò hoặc quyền hạn..." value="${param.keyword}">
            </div>
            <button type="submit" class="btn btn-primary btn-sm">Tìm kiếm</button>
        </form>
    </div>

    <div class="toolbar-section" style="margin-top:12px;">
        <form method="post" action="${pageContext.request.contextPath}/roles" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="createRole">
            <input type="text" name="roleCode" class="form-control" style="max-width:160px;" placeholder="Mã vai trò (VD: WAREHOUSE)" required>
            <input type="text" name="roleName" class="form-control" style="max-width:220px;" placeholder="Tên vai trò" required>
            <input type="text" name="description" class="form-control" style="max-width:280px;" placeholder="Mô tả">
            <button type="submit" class="btn btn-primary btn-sm">+ Thêm vai trò (UC002)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
