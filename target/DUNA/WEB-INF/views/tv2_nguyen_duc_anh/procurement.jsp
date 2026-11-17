<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Nhà cung cấp & Nhập hàng — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="inventory" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M04 • Nhà cung cấp, Đơn đặt hàng PO, Phiếu nhập &amp; Kiểm kê (TV2 • UC100–UC113)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/procurement?uc=UC100" class="btn btn-outline btn-sm">UC100 Nhà cung cấp</a>
            <a href="${pageContext.request.contextPath}/procurement?uc=UC105" class="btn btn-outline btn-sm">UC105 Đơn nhập PO</a>
            <a href="${pageContext.request.contextPath}/inventory" class="btn btn-secondary btn-sm">Quay lại Tồn kho</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="post" action="${pageContext.request.contextPath}/procurement" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="createSupplier">
            <input type="text" name="supplierCode" class="form-control" style="max-width:120px;" placeholder="Mã NCC" required>
            <input type="text" name="supplierName" class="form-control" style="max-width:220px;" placeholder="Tên xưởng may / NCC" required>
            <input type="text" name="contactPerson" class="form-control" style="max-width:160px;" placeholder="Người liên hệ">
            <input type="text" name="phone" class="form-control" style="max-width:140px;" placeholder="SĐT">
            <button type="submit" class="btn btn-primary btn-sm">+ Thêm NCC (UC101)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
    <%@ include file="/WEB-INF/views/common/uc-workbench.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
