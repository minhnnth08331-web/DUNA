<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Đánh giá sản phẩm — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M11 • Kiểm duyệt đánh giá sản phẩm &amp; thống kê phản hồi size (TV4 • UC221–UC228)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/reviews?uc=UC224" class="btn btn-outline btn-sm">UC224 Chờ duyệt</a>
            <a href="${pageContext.request.contextPath}/reviews?uc=UC221" class="btn btn-outline btn-sm">UC221 Đã duyệt</a>
            <a href="${pageContext.request.contextPath}/reviews?uc=UC228" class="btn btn-secondary btn-sm">UC228 Thống kê Fit</a>
        </div>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
