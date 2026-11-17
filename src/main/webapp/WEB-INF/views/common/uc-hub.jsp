<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Bản đồ 17 Module & 300 Use Case — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>

    <div class="page-header">
        <div>
            <h1 class="page-title">Tổng Quan 17 Module &amp; 300 Use Case (DUNA_MensWear)</h1>
            <p class="page-subtitle">Phân công 4 thành viên theo đúng hồ sơ 300 Use Case (100 bảng SQL Server / 300 UC điều phối trực tiếp qua <code>/uc-run</code>)</p>
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

    <c:if test="${not empty resultRows}">
        <div style="margin-top:20px;">
            <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px;">
                <h2 style="font-size:16px;font-weight:700;margin:0;color:#0f172a;">Kết quả thực thi: <c:out value="${resultTitle}"/></h2>
                <c:if test="${not empty executedMeta}">
                    <a href="${pageContext.request.contextPath}${executedMeta.route}" class="btn btn-outline btn-sm">Mở trang ${executedMeta.module} (${executedMeta.route})</a>
                </c:if>
            </div>
            <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
        </div>
    </c:if>

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
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M01#explorer">M01 - Tài khoản &amp; Phân quyền</a></td><td>UC001–UC027 (27)</td><td>Nguyễn Ngọc Minh</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/roles">Vai trò &amp; Quyền</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/users">Tài khoản (603)</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M02#explorer">M02 - Khách hàng &amp; Hạng VIP</a></td><td>UC028–UC043 (16)</td><td>Nguyễn Ngọc Minh</td><td><a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/CustomerServlet">Khách hàng (600)</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/customers">Nghiệp vụ UC M02 (VIP / Số đo)</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M03#explorer">M03 - Sản phẩm, Danh mục &amp; Biến thể</a></td><td>UC044–UC086 (43)</td><td>Nguyễn Ngọc Minh</td><td><a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/ProductServlet">Sản phẩm (100)</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/categories">Danh mục &amp; Size</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/products">Nghiệp vụ UC M03 (COGS / VAT / Tag)</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/variants">Biến thể</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M04#explorer">M04 - Kho, NCC &amp; Nhập hàng</a></td><td>UC087–UC113 (27)</td><td>Nguyễn Đức Anh</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/warehouses">Kho hàng</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/inventory">Tồn kho</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/procurement">NCC &amp; Nhập PO</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M05#explorer">M05 - Giỏ hàng &amp; Yêu thích</a></td><td>UC114–UC125 (12)</td><td>Nguyễn Đức Anh</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/cart">Giỏ hàng &amp; Wishlist</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M06#explorer">M06 - Đơn hàng &amp; Đổi trả</a></td><td>UC126–UC158 (33)</td><td>Nguyễn Đức Anh &amp; Lê Minh Bách</td><td><a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/OrderServlet">Đơn hàng (1.017)</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/orders">Nghiệp vụ UC M06 (Đặt hộ / VAT)</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/returns">Đổi trả</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M07#explorer">M07 - Thanh toán &amp; Đối soát COD</a></td><td>UC159–UC170 (12)</td><td>Lê Minh Bách</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/payments">Thanh toán</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M08#explorer">M08 - Vận chuyển &amp; Địa lý</a></td><td>UC171–UC190 (20)</td><td>Lê Minh Bách</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/shipping">Vận chuyển</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M09#explorer">M09 - Voucher &amp; Khuyến mãi</a></td><td>UC191–UC210 (20)</td><td>Lê Minh Bách</td><td><a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/VoucherServlet">Voucher</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/promotions">Khuyến mãi &amp; Flash Sale</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M10#explorer">M10 - Tích điểm &amp; Đổi quà</a></td><td>UC211–UC220 (10)</td><td>Lê Minh Bách &amp; Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/loyalty">Tích điểm</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M11#explorer">M11 - Đánh giá &amp; Hỗ trợ Ticket</a></td><td>UC221–UC236 (16)</td><td>Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/reviews">Đánh giá</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/support">Ticket &amp; FAQ</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M12#explorer">M12 - Nội dung &amp; Marketing Web</a></td><td>UC237–UC246 (10)</td><td>Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/marketing">Banner &amp; Blog</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M13#explorer">M13 - Thông báo &amp; Email</a></td><td>UC247–UC256 (10)</td><td>Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/notifications">Thông báo</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M14#explorer">M14 - Cửa hàng &amp; Bán hàng POS</a></td><td>UC257–UC266 (10)</td><td>Nguyễn Kiều An</td><td><a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/OrderServlet?view=sell">Bán hàng POS 1 chạm</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/pos">Cửa hàng &amp; Ca POS</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M15#explorer">M15 - Nhân sự &amp; Chấm công</a></td><td>UC267–UC278 (12)</td><td>Nguyễn Kiều An</td><td><a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/EmployeeManagementServlet">Nhân sự</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/hr">Phòng ban &amp; Chấm công</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M16#explorer">M16 - Tài chính &amp; Báo cáo</a></td><td>UC279–UC296 (18)</td><td>Nguyễn Kiều An</td><td><a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/OrderServlet?view=report">Biểu đồ Doanh thu</a> <a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/finance">Tài chính &amp; Công nợ</a></td></tr>
            <tr><td><a href="${pageContext.request.contextPath}/uc-hub?module=M17#explorer">M17 - Cấu hình &amp; Job ngầm</a></td><td>UC297–UC300 (4)</td><td>Nguyễn Kiều An</td><td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}/system">Hệ thống</a></td></tr>
            </tbody>
        </table>
    </div>

    <div id="explorer" style="margin-top:26px;background:#fff;border:1px solid #e2e8f0;border-radius:12px;padding:18px 20px;">
        <div style="display:flex;flex-wrap:wrap;justify-content:space-between;align-items:center;gap:12px;margin-bottom:14px;">
            <div>
                <h2 style="font-size:17px;font-weight:700;margin:0;color:#0f172a;">Danh Sách &amp; Thực Thi Đầy Đủ 300 Use Case (${ucList.size()} / ${totalUcCount} UC)</h2>
                <p style="font-size:12.5px;color:#64748b;margin:3px 0 0;">Lọc theo phân hệ, thành viên hoặc từ khóa rồi nhấn Xem / Chạy để kiểm thử trực tiếp trên CSDL.</p>
            </div>
        </div>

        <form method="get" action="${pageContext.request.contextPath}/uc-hub#explorer" style="display:flex;flex-wrap:wrap;gap:8px;margin-bottom:16px;">
            <select name="module" class="form-control" style="max-width:170px;">
                <option value="">Tất cả 17 Module</option>
                <c:forTokens items="M01,M02,M03,M04,M05,M06,M07,M08,M09,M10,M11,M12,M13,M14,M15,M16,M17" delims="," var="modCode">
                    <option value="${modCode}" ${selectedModule eq modCode ? 'selected' : ''}>Phân hệ ${modCode}</option>
                </c:forTokens>
            </select>
            <select name="owner" class="form-control" style="max-width:210px;">
                <option value="">Tất cả Thành viên</option>
                <option value="TV1" ${selectedOwner eq 'TV1' ? 'selected' : ''}>TV1 • Nguyễn Ngọc Minh</option>
                <option value="TV2" ${selectedOwner eq 'TV2' ? 'selected' : ''}>TV2 • Nguyễn Đức Anh</option>
                <option value="TV3" ${selectedOwner eq 'TV3' ? 'selected' : ''}>TV3 • Lê Minh Bách</option>
                <option value="TV4" ${selectedOwner eq 'TV4' ? 'selected' : ''}>TV4 • Nguyễn Kiều An</option>
            </select>
            <input type="text" name="keyword" value="<c:out value='${keyword}'/>" class="form-control" style="max-width:280px;" placeholder="Tìm mã UC (VD: UC166) hoặc tên chức năng...">
            <button type="submit" class="btn btn-primary btn-sm">Lọc Danh Sách</button>
            <a href="${pageContext.request.contextPath}/uc-hub#explorer" class="btn btn-outline btn-sm">Xem Cả 300 UC</a>
        </form>

        <div class="table-wrap">
            <table class="data-table">
                <thead>
                <tr>
                    <th style="width:90px;">Mã UC</th>
                    <th style="width:70px;">Module</th>
                    <th style="width:175px;">Phụ trách</th>
                    <th>Tên Use Case &amp; Tham Số Chạy Trực Tiếp (/uc-run)</th>
                    <th style="width:120px;">Trang chính</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="u" items="${ucList}">
                    <tr>
                        <td><strong>${u.code}</strong><br><span style="font-size:10.5px;color:${u.mode eq 'READ' ? '#0369a1' : '#b45309'};font-weight:700;">${u.mode}</span></td>
                        <td><a href="${pageContext.request.contextPath}/uc-hub?module=${u.module}#explorer">${u.module}</a></td>
                        <td style="font-size:12.5px;"><c:out value="${u.owner}"/></td>
                        <td>
                            <div style="font-weight:600;color:#0f172a;margin-bottom:6px;"><c:out value="${u.title}"/></div>
                            <form method="${u.mode eq 'READ' ? 'get' : 'post'}" action="${pageContext.request.contextPath}/uc-run" style="display:flex;flex-wrap:wrap;gap:6px;align-items:center;">
                                <input type="hidden" name="uc" value="${u.code}">
                                <input type="hidden" name="from" value="hub">
                                <c:forEach var="p" items="${u.params}">
                                    <input type="${p.type}" name="${p.name}" value="<c:out value='${p.defaultValue}'/>"
                                           title="<c:out value='${p.label}'/>" placeholder="<c:out value='${p.label}'/>"
                                           style="height:28px;padding:2px 7px;font-size:12px;border:1px solid #cbd5e1;border-radius:5px;width:130px;">
                                </c:forEach>
                                <button type="submit" class="btn ${u.mode eq 'READ' ? 'btn-outline' : 'btn-primary'} btn-sm" style="height:28px;padding:0 10px;">
                                    ${u.mode eq 'READ' ? 'Xem' : 'Chạy'}
                                </button>
                            </form>
                        </td>
                        <td><a class="btn btn-outline btn-sm" href="${pageContext.request.contextPath}${u.route}">${u.route}</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/footer.jsp" />
</body>
</html>
