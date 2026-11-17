<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Quản lý Đổi trả hàng — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="orders" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M06 • Tiếp nhận đổi size/màu, duyệt yêu cầu và nhập lại kho (TV2 • UC147–UC151)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline btn-sm">Quay lại Đơn hàng</a>
            <a href="${pageContext.request.contextPath}/payments" class="btn btn-secondary btn-sm">Thanh toán &amp; Hoàn tiền</a>
        </div>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
