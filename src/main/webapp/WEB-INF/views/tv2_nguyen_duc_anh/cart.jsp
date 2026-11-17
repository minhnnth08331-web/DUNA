<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Giỏ hàng & Yêu thích — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="orders" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M05 • Giỏ hàng trực tuyến, giỏ hàng bỏ quên &amp; sản phẩm yêu thích (TV2 • UC114–UC125)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/cart?uc=UC114" class="btn btn-outline btn-sm">UC114 Giỏ hàng</a>
            <a href="${pageContext.request.contextPath}/cart?uc=UC122" class="btn btn-outline btn-sm">UC122 Giỏ bỏ quên</a>
            <a href="${pageContext.request.contextPath}/cart?uc=UC123" class="btn btn-secondary btn-sm">UC123 Yêu thích</a>
        </div>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
