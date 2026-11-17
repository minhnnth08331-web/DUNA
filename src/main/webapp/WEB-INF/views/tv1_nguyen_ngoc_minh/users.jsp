<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Tài khoản & Nhật ký — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M01 • Quản lý tài khoản, sổ địa chỉ &amp; nhật ký hệ thống (TV1 • UC011–UC027)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/users?uc=UC011" class="btn btn-outline btn-sm">UC011 DS Tài khoản</a>
            <a href="${pageContext.request.contextPath}/users?uc=UC013&roleCode=STAFF&status=ACTIVE" class="btn btn-outline btn-sm">UC013 Lọc Staff</a>
            <a href="${pageContext.request.contextPath}/users?uc=UC023" class="btn btn-outline btn-sm">UC023 Địa chỉ</a>
            <a href="${pageContext.request.contextPath}/users?uc=UC027" class="btn btn-secondary btn-sm">UC027 Audit Logs</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="get" action="${pageContext.request.contextPath}/users" class="filter-form" style="width:100%;">
            <input type="hidden" name="uc" value="UC012">
            <div class="search-box-wrap">
                <input type="text" name="keyword" class="search-box" placeholder="Tìm theo email, SĐT, họ tên..." value="${param.keyword}">
            </div>
            <button type="submit" class="btn btn-primary btn-sm">Tìm (UC012)</button>
        </form>
    </div>

    <div class="toolbar-section" style="margin-top:12px;">
        <form method="post" action="${pageContext.request.contextPath}/users" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="toggleStatus">
            <input type="number" name="userId" class="form-control" style="max-width:160px;" placeholder="ID tài khoản" required>
            <button type="submit" class="btn btn-secondary btn-sm">Khóa / Mở khóa tài khoản (UC021)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
