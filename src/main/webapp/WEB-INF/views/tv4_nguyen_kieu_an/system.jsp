<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Cấu hình Hệ thống — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M17 • Tham số hệ thống (VAT, Freeship) &amp; Công việc chạy ngầm (TV4 • UC297–UC300)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/system?uc=UC297" class="btn btn-outline btn-sm">UC297 Cấu hình</a>
            <a href="${pageContext.request.contextPath}/system?uc=UC299" class="btn btn-secondary btn-sm">UC299 Scheduled Jobs</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="post" action="${pageContext.request.contextPath}/system" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="updateSetting">
            <input type="text" name="settingKey" class="form-control" style="max-width:220px;" value="VAT_RATE" placeholder="Setting Key" required>
            <input type="text" name="settingValue" class="form-control" style="max-width:200px;" value="8" placeholder="Giá trị" required>
            <button type="submit" class="btn btn-primary btn-sm">Cập nhật cấu hình (UC298)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
