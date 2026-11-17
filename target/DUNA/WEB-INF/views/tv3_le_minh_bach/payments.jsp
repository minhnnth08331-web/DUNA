<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Thanh toán & Đối soát COD — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M07 • Phương thức thanh toán (COD, VNPay, MoMo), hoàn tiền &amp; đối soát COD (TV3 • UC152–UC170)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/payments?uc=UC159" class="btn btn-outline btn-sm">UC159 Phương thức TT</a>
            <a href="${pageContext.request.contextPath}/payments?uc=UC167" class="btn btn-outline btn-sm">UC167 Giao dịch</a>
            <a href="${pageContext.request.contextPath}/payments?uc=UC157" class="btn btn-secondary btn-sm">UC157 Thống kê đơn</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="post" action="${pageContext.request.contextPath}/payments" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="addMethod">
            <input type="text" name="methodCode" class="form-control" style="max-width:140px;" placeholder="Mã PT (ZALOPAY)" required>
            <input type="text" name="methodName" class="form-control" style="max-width:220px;" placeholder="Tên phương thức" required>
            <label style="display:flex;align-items:center;gap:6px;"><input type="checkbox" name="isOnline" value="1"> Online</label>
            <button type="submit" class="btn btn-primary btn-sm">+ Thêm phương thức (UC160)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
