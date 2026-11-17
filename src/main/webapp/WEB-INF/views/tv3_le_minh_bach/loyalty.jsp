<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Tích điểm & Đổi quà — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M10 • Tài khoản tích điểm thưởng, lịch sử điểm &amp; đổi quà (TV3 &amp; TV4 • UC211–UC220)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/loyalty?uc=UC216" class="btn btn-outline btn-sm">UC216 Lịch sử điểm</a>
            <a href="${pageContext.request.contextPath}/loyalty?uc=UC218" class="btn btn-secondary btn-sm">UC218 Quà đổi điểm</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="post" action="${pageContext.request.contextPath}/loyalty" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="addReward">
            <input type="text" name="rewardName" class="form-control" style="max-width:240px;" placeholder="Tên quà tặng / Voucher" required>
            <input type="number" name="pointsRequired" class="form-control" style="max-width:140px;" value="200" placeholder="Điểm cần">
            <input type="number" name="stockQuantity" class="form-control" style="max-width:120px;" value="100" placeholder="Số lượng">
            <button type="submit" class="btn btn-primary btn-sm">+ Thêm quà đổi điểm (UC219)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
