<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Vận chuyển & Địa lý — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M08 • Tỉnh/Thành, Xã/Phường, Vùng giao hàng, Đơn vị vận chuyển &amp; Vận đơn (TV3 • UC171–UC190)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/shipping?uc=UC171" class="btn btn-outline btn-sm">UC171 Tỉnh/Thành</a>
            <a href="${pageContext.request.contextPath}/shipping?uc=UC174" class="btn btn-outline btn-sm">UC174 Xã/Phường</a>
            <a href="${pageContext.request.contextPath}/shipping?uc=UC178" class="btn btn-outline btn-sm">UC178 Vùng giao</a>
            <a href="${pageContext.request.contextPath}/shipping?uc=UC180" class="btn btn-outline btn-sm">UC180 ĐVVC</a>
            <a href="${pageContext.request.contextPath}/shipping?uc=UC184" class="btn btn-secondary btn-sm">UC184 Bảng phí</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="get" action="${pageContext.request.contextPath}/shipping" class="filter-form" style="width:100%;">
            <input type="hidden" name="uc" value="${activeUc}">
            <div class="search-box-wrap">
                <input type="text" name="keyword" class="search-box" placeholder="Tìm tỉnh thành, xã phường hoặc mã vận đơn..." value="${param.keyword}">
            </div>
            <button type="submit" class="btn btn-primary btn-sm">Tìm kiếm</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
