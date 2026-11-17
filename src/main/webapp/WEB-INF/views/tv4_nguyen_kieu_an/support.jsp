<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Hỗ trợ & FAQ — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M11 • Ticket hỗ trợ khách hàng &amp; Câu hỏi thường gặp (TV4 • UC229–UC236)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/support?uc=UC230" class="btn btn-outline btn-sm">UC230 DS Ticket</a>
            <a href="${pageContext.request.contextPath}/support?uc=UC235" class="btn btn-secondary btn-sm">UC235 FAQ</a>
        </div>
    </div>

    <div class="toolbar-section">
        <form method="post" action="${pageContext.request.contextPath}/support" class="filter-form" style="width:100%;">
            <input type="hidden" name="action" value="addFaq">
            <input type="text" name="faqGroup" class="form-control" style="max-width:150px;" value="Đổi trả" placeholder="Nhóm">
            <input type="text" name="question" class="form-control" style="max-width:260px;" placeholder="Câu hỏi thường gặp" required>
            <input type="text" name="answer" class="form-control" style="max-width:300px;" placeholder="Câu trả lời" required>
            <button type="submit" class="btn btn-primary btn-sm">+ Thêm FAQ (UC236)</button>
        </form>
    </div>

    <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
</div>
<jsp:include page="/footer.jsp" />
</body>
</html>
