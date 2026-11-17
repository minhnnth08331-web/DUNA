<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Bản đồ 17 Module & 300 Use Case — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <div class="page-header">
        <div>
            <h1 class="page-title">Tổng Quan 17 Module &amp; 300 Use Case (DUNA_MensWear)</h1>
            <p class="page-subtitle">Phân công 4 thành viên theo đúng hồ sơ Nhom6_300_usecase_CHUAN_HOA_DUNA.xlsx (100 bảng / 300 UC)</p>
        </div>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/finance?action=export" class="btn btn-secondary">Xuất Excel Tài Chính</a>
            <a href="${pageContext.request.contextPath}/order-export" class="btn btn-primary">Xuất Excel Đơn Hàng (UC158)</a>
        </div>
    </div>

    <div class="kpi-grid">
        <div class="stat-card">
            <div class="stat-label">TV1 • Nguyễn Ngọc Minh (TH08331)</div>
            <div class="stat-value">86 UC</div>
            <div class="stat-sub">UC001–UC086 (M01 Tài khoản, M02 Khách hàng, M03 Sản phẩm)</div>
        </div>
        <div class="stat-card">
            <div class="stat-label">TV2 • Nguyễn Đức Anh (TH08308)</div>
            <div class="stat-value">65 UC</div>
            <div class="stat-sub">UC087–UC151 (M04 Kho hàng, M05 Giỏ hàng, M06 Đơn hàng)</div>
        </div>
        <div class="stat-card">
            <div class="stat-label">TV3 • Lê Minh Bách (TH08442)</div>
            <div class="stat-value">67 UC</div>
            <div class="stat-sub">UC152–UC218 (M07 Thanh toán, M08 Vận chuyển, M09 KM, M10 Điểm)</div>
        </div>
        <div class="stat-card">
            <div class="stat-label">TV4 • Nguyễn Kiều An (TH08490)</div>
            <div class="stat-value">82 UC</div>
            <div class="stat-sub">UC219–UC300 (M11–M17 CSKH, Marketing, POS, Nhân sự, Tài chính)</div>
        </div>
    </div>

    <div class="table-wrap" style="margin-top:20px;">
        <table class="data-table">
            <thead>
            <tr>
                <th>Module</th>
                <th>Phạm vi UC</th>
                <th>Thành viên phụ trách</th>
                <th>Truy cập nhanh</th>
            </tr>
            </thead>
            <tbody>
            <tr><td>M01 - Tài khoản &amp; Phân quyền</td><td>UC001–UC027</td><td>Nguyễn Ngọc Minh</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/roles">Vai trò &amp; Quyền</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/users">Tài khoản</a></td></tr>
            <tr><td>M02 - Khách hàng &amp; Hạng VIP</td><td>UC028–UC043</td><td>Nguyễn Ngọc Minh</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/customers">Khách hàng (600)</a></td></tr>
            <tr><td>M03 - Sản phẩm, Danh mục &amp; Biến thể</td><td>UC044–UC086</td><td>Nguyễn Ngọc Minh</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/categories">Danh mục &amp; Size</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/products">Sản phẩm (100)</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/variants">Biến thể</a></td></tr>
            <tr><td>M04 - Kho, NCC &amp; Nhập hàng</td><td>UC087–UC113</td><td>Nguyễn Đức Anh</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/warehouses">Kho hàng</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/inventory">Tồn kho</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/procurement">NCC &amp; Nhập PO</a></td></tr>
            <tr><td>M05 - Giỏ hàng &amp; Yêu thích</td><td>UC114–UC125</td><td>Nguyễn Đức Anh</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/cart">Giỏ hàng &amp; Wishlist</a></td></tr>
            <tr><td>M06 - Đơn hàng &amp; Đổi trả</td><td>UC126–UC158</td><td>Nguyễn Đức Anh &amp; Lê Minh Bách</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/orders">Đơn hàng</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/returns">Đổi trả</a></td></tr>
            <tr><td>M07 - Thanh toán &amp; Đối soát COD</td><td>UC159–UC170</td><td>Lê Minh Bách</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/payments">Thanh toán</a></td></tr>
            <tr><td>M08 - Vận chuyển &amp; Địa lý</td><td>UC171–UC190</td><td>Lê Minh Bách</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/shipping">Vận chuyển</a></td></tr>
            <tr><td>M09 - Voucher &amp; Khuyến mãi</td><td>UC191–UC210</td><td>Lê Minh Bách</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/promotions">Voucher &amp; Flash Sale</a></td></tr>
            <tr><td>M10 - Tích điểm &amp; Đổi quà</td><td>UC211–UC220</td><td>Lê Minh Bách &amp; Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/loyalty">Tích điểm</a></td></tr>
            <tr><td>M11 - Đánh giá &amp; Hỗ trợ Ticket</td><td>UC221–UC236</td><td>Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/reviews">Đánh giá</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/support">Ticket &amp; FAQ</a></td></tr>
            <tr><td>M12 - Nội dung &amp; Marketing Web</td><td>UC237–UC246</td><td>Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/marketing">Banner &amp; Blog</a></td></tr>
            <tr><td>M13 - Thông báo &amp; Email</td><td>UC247–UC256</td><td>Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/notifications">Thông báo</a></td></tr>
            <tr><td>M14 - Cửa hàng &amp; Bán hàng POS</td><td>UC257–UC266</td><td>Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/pos">Bán hàng POS</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/stores">Cửa hàng</a></td></tr>
            <tr><td>M15 - Nhân sự &amp; Chấm công</td><td>UC267–UC278</td><td>Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/hr">Nhân sự</a></td></tr>
            <tr><td>M16 - Tài chính &amp; Báo cáo</td><td>UC279–UC296</td><td>Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/finance">Tài chính &amp; Báo cáo</a></td></tr>
            <tr><td>M17 - Cấu hình &amp; Job ngầm</td><td>UC297–UC300</td><td>Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/system">Hệ thống</a></td></tr>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/footer.jsp" />
</body>
</html>
