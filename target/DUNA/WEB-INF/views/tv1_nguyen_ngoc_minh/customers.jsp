<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Quản lý Khách hàng — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="customers" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M02 • Hồ sơ 600 khách hàng, hạng thành viên &amp; số đo tư vấn size (TV1 • UC028–UC043)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/customers?uc=UC028" class="btn btn-outline btn-sm">UC028 DS Khách hàng</a>
            <a href="${pageContext.request.contextPath}/customers?uc=UC035" class="btn btn-outline btn-sm">UC035 Hạng VIP</a>
            <a href="${pageContext.request.contextPath}/customers?uc=UC039" class="btn btn-outline btn-sm">UC039 Nhóm KH</a>
            <form method="post" action="${pageContext.request.contextPath}/customers" style="display:inline;">
                <input type="hidden" name="action" value="autoUpgrade">
                <button type="submit" class="btn btn-secondary btn-sm">UC038 Nâng hạng tự động</button>
            </form>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="get" action="${pageContext.request.contextPath}/customers" class="filter-form" style="width:100%;">
            <input type="hidden" name="uc" value="UC029">
            <div class="search-box-wrap">
                <input type="text" name="keyword" class="search-box" placeholder="Tìm khách hàng theo mã KH, SĐT hoặc họ tên..." value="${param.keyword}">
            </div>
            <button type="submit" class="btn btn-primary btn-sm">Tìm kiếm</button>
            <a href="${pageContext.request.contextPath}/customers" class="btn btn-outline btn-sm">Làm mới</a>
        </form>
    </div>

    <div class="toolbar-section" style="margin-top:12px;">
        <form method="post" action="${pageContext.request.contextPath}/customers" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="addCustomer">
            <input type="text" name="fullName" class="form-control" style="max-width:220px;" placeholder="Họ tên khách vãng lai" required>
            <input type="tel" name="phone" class="form-control" style="max-width:170px;" placeholder="0912345678" maxlength="10" pattern="0[35789][0-9]{8}" title="Số điện thoại bắt đầu bằng số 0, gồm đúng 10 chữ số (03, 05, 07, 08, 09)" oninput="this.value=this.value.replace(/[^0-9]/g,'').slice(0,10)" required>
            <select name="gender" class="form-control" style="max-width:120px;">
                <option value="MALE">Nam</option>
                <option value="FEMALE">Nữ</option>
            </select>
            <button type="submit" class="btn btn-primary btn-sm">+ Thêm khách hàng (UC031)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
