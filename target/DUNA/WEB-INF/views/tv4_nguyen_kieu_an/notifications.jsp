<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Thông báo & Email — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M13 • Mẫu thông báo Email/SMS, Thông báo in-app &amp; Nhật ký gửi (TV4 • UC247–UC256)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/notifications?uc=UC247" class="btn btn-outline btn-sm">UC247 Mẫu thông báo</a>
            <a href="${pageContext.request.contextPath}/notifications?uc=UC251" class="btn btn-outline btn-sm">UC251 Chưa đọc</a>
            <a href="${pageContext.request.contextPath}/notifications?uc=UC256" class="btn btn-secondary btn-sm">UC256 Log lỗi</a>
        </div>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
