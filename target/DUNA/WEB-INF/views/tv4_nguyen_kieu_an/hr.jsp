<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Quản lý Nhân sự — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="hr" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M15 • Hồ sơ nhân viên, phòng ban, chức vụ, phân ca &amp; chấm công (TV4 • UC267–UC278)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/hr?uc=UC267" class="btn btn-outline btn-sm">UC267 Nhân viên</a>
            <a href="${pageContext.request.contextPath}/hr?uc=UC271" class="btn btn-outline btn-sm">UC271 Phòng ban &amp; Chức vụ</a>
            <a href="${pageContext.request.contextPath}/hr?uc=UC275" class="btn btn-outline btn-sm">UC275 Lịch phân ca</a>
            <a href="${pageContext.request.contextPath}/hr?uc=UC278" class="btn btn-secondary btn-sm">UC278 Chấm công</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="post" action="${pageContext.request.contextPath}/hr" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="addEmployee">
            <input type="text" name="employeeCode" class="form-control" style="max-width:110px;" placeholder="Mã NV" required>
            <input type="text" name="fullName" class="form-control" style="max-width:200px;" placeholder="Họ tên nhân viên" required>
            <input type="text" name="phone" class="form-control" style="max-width:140px;" placeholder="SĐT" required>
            <input type="email" name="email" class="form-control" style="max-width:200px;" placeholder="Email">
            <button type="submit" class="btn btn-primary btn-sm">+ Thêm nhân viên (UC268)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
