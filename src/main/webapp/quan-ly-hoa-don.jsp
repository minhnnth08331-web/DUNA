<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% if (request.getAttribute("listDH") == null) {
    response.sendRedirect(request.getContextPath() + "/OrderServlet"); return;
} %>
<!DOCTYPE html>
<html lang="vi">
<head>hi
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý hóa đơn — DUNA Men's Wear</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Cinzel:wght@500;600;700;800&family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=JetBrains+Mono:wght@400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme.css?v=duna_v4">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css?v=duna_v4">
    <script>if (localStorage.getItem('duna_theme') === 'dark') document.documentElement.classList.add('dark-mode');</script>
</head>
<body>
    <div class="navbar">
        <a href="${pageContext.request.contextPath}/OrderServlet?view=sell" class="navbar-brand">
            <img src="${pageContext.request.contextPath}/img/duna-logo.png" alt="DUNA" class="navbar-brand-logo" width="36" height="36" style="width: 36px !important; height: 36px !important; max-width: 36px !important; max-height: 36px !important; border-radius: 6px; object-fit: contain; flex-shrink: 0;">
            <div class="navbar-brand-text">
                <span class="brand-monogram">DU<span>NA</span></span>
                <span class="brand-subtext">Men's Wear</span>
            </div>
        </a>
        <div class="nav-menu">
            <c:choose>
                <c:when test="${sessionScope.role != 'MANAGER'}">
                    <a href="${pageContext.request.contextPath}/EmployeeDetailServlet?id=${not empty sessionScope.nhanVienID ? sessionScope.nhanVienID : 1}&mode=view">Thông tin cá nhân</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet?view=sell">Bán hàng</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet" class="active">Quản lý hóa đơn</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/ProductServlet">Quản lý sản phẩm</a>
                    <a href="${pageContext.request.contextPath}/EmployeeManagementServlet">Quản lý nhân sự</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet?view=report">Báo cáo thống kê</a>
                    <a href="${pageContext.request.contextPath}/CustomerServlet">Quản lý khách hàng</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet?view=sell">Bán hàng</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet" class="active">Quản lý hóa đơn</a>
                    <a href="${pageContext.request.contextPath}/VoucherServlet">Quản lý Voucher</a>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="nav-right-cluster">
            <div class="nav-user">
                <span>Xin chào</span>
                <a href="${pageContext.request.contextPath}/EmployeeDetailServlet?id=${not empty sessionScope.nhanVienID ? sessionScope.nhanVienID : 1}&mode=view" class="nav-user-name">
                    ${not empty sessionScope.tenNV ? sessionScope.tenNV : 'Admin'}
                </a>
                <span class="nav-divider">|</span>
                <a href="${pageContext.request.contextPath}/LogoutServlet" class="nav-logout-btn">Đăng xuất</a>
            </div>
        </div>
    </div>

<div class="container">
    <c:if test="${param.created == '1'}">
        <div class="alert alert-success" style="margin-bottom: 24px;">Thanh toán thành công. Hóa đơn vừa tạo đã được lưu vào hệ thống sổ cái DUNA.</div>
    </c:if>

    <div class="page-header-row">
        <div>
            <div class="page-title-eyebrow">Sổ cái giao dịch & Hóa đơn</div>
            <h2>Quản lý Hóa Đơn & Lịch Sử Giao Dịch</h2>
        </div>
        <div>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/OrderServlet?view=sell">+ Lập hóa đơn mới</a>
        </div>
    </div>

    <!-- STATS OVERVIEW -->
    <div class="overview-grid" style="grid-template-columns: repeat(3, 1fr); margin-bottom: 24px;">
        <div class="stat-card">
            <span class="stat-label">Tổng hóa đơn phát sinh</span>
            <div class="stat-val font-mono">${not empty stat_totalOrders ? stat_totalOrders : 0}</div>
            <span class="stat-sub">Toàn bộ hồ sơ giao dịch ghi nhận</span>
        </div>
        <div class="stat-card">
            <span class="stat-label">Đã thanh toán thành công</span>
            <div class="stat-val font-mono" style="color: var(--color-success);">${not empty stat_completedOrders ? stat_completedOrders : 0}</div>
            <span class="stat-sub">Đã hoàn tất thanh toán & giao hàng</span>
        </div>
        <div class="stat-card">
            <span class="stat-label">Hóa đơn đã hủy</span>
            <div class="stat-val font-mono" style="color: var(--color-danger);">${not empty stat_cancelledOrders ? stat_cancelledOrders : 0}</div>
            <span class="stat-sub">Đơn hủy bởi khách hàng hoặc nhân viên</span>
        </div>
    </div>

    <!-- FILTER TOOLBAR -->
    <div class="card" style="margin-bottom: 24px;">
        <form method="get" action="${pageContext.request.contextPath}/OrderServlet">
            <div class="filter-bar" style="margin: 0; align-items: flex-end;">
                <div class="form-group" style="flex: 1; min-width: 240px; margin: 0;">
                    <label class="form-label">Tìm kiếm giao dịch</label>
                    <input type="text" name="search" class="form-control" placeholder="Mã đơn hàng, tên khách hàng, SĐT..." value="${p_search}">
                </div>
                <div class="form-group" style="width: 200px; margin: 0;">
                    <label class="form-label">Trạng thái</label>
                    <select name="status" class="form-select">
                        <option value="">Tất cả trạng thái</option>
                        <option value="Đã thanh toán" <c:if test="${p_status == 'Đã thanh toán'}">selected</c:if>>Đã thanh toán</option>
                        <option value="Chưa thanh toán" <c:if test="${p_status == 'Chưa thanh toán'}">selected</c:if>>Chưa thanh toán (Tạm giữ)</option>
                        <option value="Đã hủy" <c:if test="${p_status == 'Đã hủy'}">selected</c:if>>Đã hủy</option>
                    </select>
                </div>
                <div class="form-group" style="width: 200px; margin: 0;">
                    <label class="form-label">Phương thức thanh toán</label>
                    <select name="pay" class="form-select">
                        <option value="">Tất cả phương thức</option>
                        <option value="Tiền mặt" <c:if test="${p_pay == 'Tiền mặt'}">selected</c:if>>Tiền mặt</option>
                        <option value="Chuyển khoản" <c:if test="${p_pay == 'Chuyển khoản'}">selected</c:if>>Chuyển khoản</option>
                    </select>
                </div>
                <div style="display: flex; gap: 8px;">
                    <button type="submit" class="btn btn-secondary">Tra cứu</button>
                    <a href="${pageContext.request.contextPath}/OrderServlet" class="btn btn-outline">Làm mới</a>
                </div>
            </div>
        </form>
    </div>

    <!-- ORDERS LEDGER TABLE -->
    <div id="ajax-content-area" style="transition: opacity 0.2s ease;">
        <div class="table-wrap">
            <table class="data-table">
                <thead>
                <tr>
                    <th style="width: 110px;">Mã HD</th>
                    <th style="width: 160px;">Khách hàng</th>
                    <th style="width: 140px;">Nhân viên</th>
                    <th>Chi tiết sản phẩm</th>
                    <th class="th-money" style="width: 140px;">Tổng tiền</th>
                    <th style="width: 120px;">Thanh toán</th>
                    <th>Ghi chú</th>
                    <th style="width: 130px;">Trạng thái</th>
                    <th style="width: 150px;">Thời điểm</th>
                    <th style="width: 130px; text-align: right;">Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${listDH}" var="dh">
                    <c:set var="statusPill" value="badge-neutral"/>
                    <c:if test="${dh.trangThai == 'Đã thanh toán' or dh.trangThai == 'Hoàn thành' or dh.trangThai == 'Thanh toán'}"><c:set var="statusPill" value="badge-success"/></c:if>
                    <c:if test="${dh.trangThai == 'Chưa thanh toán'}"><c:set var="statusPill" value="badge-warning"/></c:if>
                    <c:if test="${dh.trangThai == 'Đã hủy'}"><c:set var="statusPill" value="badge-danger"/></c:if>
                    <tr>
                        <td class="font-mono" style="font-weight: 700; color: var(--color-primary);">${dh.maDH}</td>
                        <td style="font-weight: 600;">${dh.tenKH}</td>
                        <td style="color: var(--color-text-muted);">${not empty dh.tenNV ? dh.tenNV : 'Admin'}</td>
                        <td class="product-cell" style="text-align: left;">
                            <c:forEach items="${dh.chiTiet}" var="ct">
                                <div style="display: flex; align-items: center; justify-content: space-between; gap: 8px; margin-bottom: 4px; padding: 3px 0; border-bottom: 1px dashed var(--color-border-subtle);">
                                    <span style="font-size: 13px;">${ct.tenSP} <span class="font-mono" style="font-weight: 700; color: var(--color-primary);">x${ct.soLuong}</span></span>
                                    <c:if test="${dh.trangThai == 'Đã thanh toán'}">
                                        <button type="button" class="btn btn-outline" style="padding: 2px 8px; font-size: 11px; height: 24px; white-space: nowrap;"
                                                onclick="openExchangeModal('${dh.id}','${ct.idChiTiet}','${ct.idSP}','${ct.tenSP}','${ct.donGia}')" title="Đổi sang size hoặc màu khác">
                                            Đổi Size
                                        </button>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </td>
                        <td class="money font-mono" style="font-weight: 700; font-size: 14px;"><fmt:formatNumber value="${dh.tongTien}" type="number" groupingUsed="true"/> đ</td>
                        <td>
                            <span class="badge badge-neutral" style="font-size: 11px;">${dh.thanhToan}</span>
                        </td>
                        <td style="font-size: 12.5px; color: var(--color-text-muted); max-width: 220px; line-height: 1.4;">
                            <c:set var="noteArray" value="${fn:split(dh.ghiChu, '|')}"/>
                            ${not empty noteArray ? fn:trim(noteArray[0]) : 'Bán hàng tại quầy'}
                        </td>
                        <td>
                            <span class="badge ${statusPill} status">${dh.trangThai}</span>
                        </td>
                        <td class="font-mono" style="font-size: 12px; color: var(--color-text-muted);">${dh.ngayTao}</td>
                        <td class="action-cell" style="text-align: right;">
                            <%-- Hóa đơn Đã thanh toán / Hoàn thành: hiển thị nút in --%>
                            <c:if test="${dh.trangThai == 'Đã thanh toán' or dh.trangThai == 'Hoàn thành' or dh.trangThai == 'Thanh toán'}">
                                <button type="button" class="btn btn-secondary" style="padding: 4px 10px; font-size: 12px;" onclick="printInvoice('${dh.id}')" title="In phiếu thanh toán DUNA">In phiếu</button>
                            </c:if>
                            <%-- Hóa đơn Chưa thanh toán (Hold): ẩn nút In, chỉ hiện nút Thanh toán + Hủy --%>
                            <c:if test="${dh.trangThai == 'Chưa thanh toán'}">
                                <a class="btn btn-primary" style="padding: 4px 10px; font-size: 12px; margin-right: 4px;" href="${pageContext.request.contextPath}/OrderServlet?view=sell&holdId=${dh.id}" title="Mở đơn tạm tiếp tục thanh toán">
                                    Tiếp tục
                                </a>
                                <form method="post" action="${pageContext.request.contextPath}/OrderServlet" onsubmit="return cancelOrder(this, '${dh.id}')" style="display:inline">
                                    <input type="hidden" name="action" value="changeStatus">
                                    <input type="hidden" name="id" value="${dh.id}">
                                    <input type="hidden" name="trangThai" value="Đã hủy">
                                    <button type="submit" class="btn btn-outline" style="padding: 4px 10px; font-size: 12px; color: var(--color-danger); border-color: var(--color-danger);">Hủy</button>
                                </form>
                            </c:if>
                            <%-- Hóa đơn Đã hủy: hiển thị nút in --%>
                            <c:if test="${dh.trangThai == 'Đã hủy'}">
                                <button type="button" class="btn btn-outline" style="padding: 4px 10px; font-size: 12px;" onclick="printInvoice('${dh.id}')" title="In hồ sơ đã hủy">In biên bản</button>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty listDH}">
                    <tr><td colspan="10" style="text-align:center; color: var(--color-text-muted); padding: 48px 16px;">Chưa tìm thấy hóa đơn nào trong hệ thống.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <c:if test="${totalPages > 1}">
            <c:set var="urlParams" value="${not empty p_search ? '&search='.concat(p_search) : ''}${not empty p_status ? '&status='.concat(p_status) : ''}${not empty p_pay ? '&pay='.concat(p_pay) : ''}" />
            <div class="pagination-bar">
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <a href="?page=${currentPage - 1}${urlParams}" title="Trang trước">&#10094;</a>
                    </c:when>
                    <c:otherwise>
                        <span class="page-arrow disabled">&#10094;</span>
                    </c:otherwise>
                </c:choose>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a class="${currentPage == i ? 'active' : ''}" href="?page=${i}${urlParams}">${i}</a>
                </c:forEach>
                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a href="?page=${currentPage + 1}${urlParams}" title="Trang sau">&#10095;</a>
                    </c:when>
                    <c:otherwise>
                        <span class="page-arrow disabled">&#10095;</span>
                    </c:otherwise>
                </c:choose>
                <div class="page-jump-box">
                    <span>Đến trang:</span>
                    <input type="number" min="1" max="${totalPages}" placeholder="Số" onkeydown="if(event.key==='Enter') jumpToPage(this, '${urlParams}', ${totalPages})">
                    <button type="button" onclick="jumpToPage(this.previousElementSibling, '${urlParams}', ${totalPages})">Đi &#10140;</button>
                </div>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="footer.jsp" />

<!-- MODAL IN HÓA ĐƠN DUNA -->
<div class="modal-bg" id="modalPrint">
    <div class="modal-box" id="printContent" style="max-width: 680px;">
        <button class="close-btn no-print" type="button" onclick="closeModal('modalPrint')">×</button>
        <div class="invoice-header" style="text-align: center; border-bottom: 1px dashed var(--color-border); padding-bottom: 16px; margin-bottom: 16px;">
            <div style="font-family: 'Cinzel', serif; font-size: 20px; font-weight: 800; letter-spacing: 0.12em; color: var(--color-primary);">DUNA MEN'S WEAR</div>
            <div style="font-size: 12px; color: var(--color-text-muted); margin-top: 4px;">Sartorial Tailoring & Precision | Hotline: 1900 8888 | duna.vn</div>
            <div style="font-size: 17px; font-weight: 700; text-transform: uppercase; margin: 12px 0 6px; letter-spacing: 0.05em;">Phiếu Thanh Toán Bán Lẻ</div>
            <div class="font-mono" style="font-size: 13px; font-weight: 700; color: var(--color-primary);" id="prMaDH">Mã HD: ...</div>
            <div class="font-mono" style="font-size: 12px; color: var(--color-text-muted);" id="prNgayTao">Ngày tạo: ...</div>
        </div>
        <div style="font-size: 13.5px; line-height: 1.7; margin-bottom: 16px; background: var(--color-bg-subtle); padding: 12px 16px; border-radius: var(--radius-sm);">
            <div><span style="color: var(--color-text-muted);">Khách hàng:</span> <b id="prTenKH" style="color: var(--color-text);">...</b></div>
            <div><span style="color: var(--color-text-muted);">Số điện thoại:</span> <span class="font-mono" id="prSdtKH">...</span></div>
            <div><span style="color: var(--color-text-muted);">Địa chỉ:</span> <span id="prDiaChiKH">...</span></div>
        </div>
        <table class="data-table invoice-tbl" style="margin-bottom: 16px;">
            <thead>
                <tr>
                    <th style="width: 40px; text-align: center;">STT</th>
                    <th>Tên sản phẩm</th>
                    <th style="white-space:nowrap;">Biến thể</th>
                    <th style="width: 50px; text-align: center;">SL</th>
                    <th class="th-money" style="white-space:nowrap;">Đơn giá</th>
                    <th class="th-money" style="white-space:nowrap;">Thành tiền</th>
                </tr>
            </thead>
            <tbody id="prBody"></tbody>
        </table>
        <div style="display: flex; justify-content: flex-end; margin: 16px 0; font-size: 13.5px; line-height: 1.8;">
            <div style="min-width: 320px; text-align: right;">
                <div><span style="color: var(--color-text-muted);">Thuế GTGT (<span id="prVatPctLabel">${not empty currentVat ? currentVat : 8}</span>%):</span> <span class="font-mono" id="prVAT" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
                <div><span style="color: var(--color-text-muted);">Ưu đãi VIP:</span> <span class="font-mono" id="prGiamGia" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
                <div><span style="color: var(--color-text-muted);">Mã giảm giá / Voucher:</span> <span class="font-mono" id="prVoucher" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
                <div style="font-size: 18px; font-weight: 800; border-top: 1px dashed var(--color-border); border-bottom: 1px dashed var(--color-border); padding: 8px 0; margin: 8px 0; color: var(--color-primary);">
                    Tổng thanh toán: <span class="font-mono" id="prTongTien" style="color: var(--color-accent); display: inline-block; min-width: 130px;">0 đ</span>
                </div>
                <div><span style="color: var(--color-text-muted);">Tiền khách đưa:</span> <span class="font-mono" id="prKhachDua" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
                <div><span style="color: var(--color-text-muted);">Tiền hoàn lại:</span> <span class="font-mono" id="prTienThua" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
            </div>
        </div>
        <div style="font-size: 12.5px; margin-bottom: 24px; line-height: 1.6; border-top: 1px solid var(--color-border-subtle); padding-top: 12px;">
            <div><b>Phương thức thanh toán:</b> <span id="prThanhToan">...</span></div>
            <div><b>Trạng thái:</b> <span id="prTrangThai">...</span></div>
            <div><b>Thu ngân phục vụ:</b> <span id="prTenNV">...</span></div>
            <div><b>Ghi chú:</b> <span id="prGhiChu">...</span></div>
        </div>
        <div class="modal-footer no-print">
            <button type="button" class="btn btn-outline" onclick="closeModal('modalPrint')">Đóng</button>
            <button type="button" class="btn btn-primary" id="btnDoPrintModal" onclick="window.print()">In ngay</button>
        </div>
    </div>
</div>

<!-- MODAL ĐỔI SIZE / MÀU SẮC DUNA -->
<div class="modal-bg" id="modalExchange">
    <div class="modal-box" style="width: 560px;">
        <button class="close-btn" type="button" onclick="closeModal('modalExchange')">×</button>
        <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 18px; border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 14px;">
            <div>
                <h3 style="margin: 0; font-size: 18px; font-weight: 700; color: var(--color-text);">Đổi Trả Hàng & Đổi Size Áo/Quần DUNA</h3>
                <div style="font-size: 12.5px; color: var(--color-text-muted); margin-top: 2px;">Hoàn 1 sản phẩm cũ về kho và xuất 1 sản phẩm biến thể mới</div>
            </div>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/OrderServlet" id="frmExchange" onsubmit="return validateExchange();">
            <input type="hidden" name="action" value="exchangeItem">
            <input type="hidden" name="idDH" id="exIdDH">
            <input type="hidden" name="idCT" id="exIdCT">

            <div style="background: var(--color-bg-subtle); border: 1px solid var(--color-border-subtle); border-radius: var(--radius-sm); padding: 14px 16px; margin-bottom: 16px; font-size: 13.5px; line-height: 1.6;">
                <div>Sản phẩm hiện tại: <b id="exCurrentName" style="color: var(--color-text);">--</b></div>
                <div>Đơn giá đã thanh toán: <b id="exCurrentPrice" class="font-mono" style="color: var(--color-primary);">-- đ</b></div>
            </div>

            <div class="form-group" style="margin-bottom: 16px;">
                <label class="form-label">Chọn Size / Màu mới cần đổi sang:</label>
                <select name="idBienTheMoi" id="exSelectNewVariant" class="form-select" onchange="calcExchangeDiff()">
                    <option value="">Đang tải danh sách biến thể...</option>
                </select>
            </div>

            <!-- Khung chênh lệch giá -->
            <div id="exDiffBox" style="background: var(--color-surface); border: 1px solid var(--color-border); border-radius: var(--radius-sm); padding: 14px 16px; margin-bottom: 20px; font-size: 13.5px; display: flex; justify-content: space-between; align-items: center;">
                <span style="font-weight: 600; color: var(--color-text);">Chênh lệch thanh toán:</span>
                <span id="exDiffAmount" class="font-mono" style="font-weight: 700; font-size: 15px; color: var(--color-primary);">0 đ (Đổi ngang)</span>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="closeModal('modalExchange')">Hủy bỏ</button>
                <button type="submit" class="btn btn-primary">Xác nhận đổi hàng</button>
            </div>
        </form>
    </div>
</div>

<script>
function money(value) {
    return Number(value || 0).toLocaleString('vi-VN') + ' đ';
}

function printInvoice(id) {
    fetch('${pageContext.request.contextPath}/OrderServlet?ajaxDetail=1&id=' + id)
        .then(function(response) { return response.json(); })
        .then(function(data) {
            if (data.trangThai === 'Chưa thanh toán') {
                alert('Hóa đơn đang ở trạng thái "Chưa thanh toán" nên không được phép in!\n\nVui lòng hoàn tất thanh toán cho hóa đơn trước khi in.');
                return;
            }
            var btnPrintModal = document.getElementById('btnDoPrintModal');
            if (btnPrintModal) {
                btnPrintModal.style.display = 'inline-block';
            }
            document.getElementById('prMaDH').textContent = 'Mã HD: ' + (data.maDH || '');
            document.getElementById('prNgayTao').textContent = 'Ngày tạo: ' + (data.ngayTao || '');
            document.getElementById('prTenKH').textContent = data.tenKH || 'Khách lẻ';
            document.getElementById('prSdtKH').textContent = data.sdtKH || '-';
            document.getElementById('prDiaChiKH').textContent = data.diaChiKH || '-';
            document.getElementById('prThanhToan').textContent = data.thanhToan || '-';
            document.getElementById('prTrangThai').textContent = data.trangThai || '-';
            if (document.getElementById('prTenNV')) document.getElementById('prTenNV').textContent = data.tenNV || 'Admin';

            var noteStr = data.ghiChu || '';
            var parts = noteStr.split('|');
            var cleanNotes = [];
            var vatVal = '', giamGiaVal = '', voucherVal = '', khachDuaVal = '', tienThuaVal = '';
            
            parts.forEach(function(p) {
                var pTrim = p.trim();
                if (pTrim.indexOf('VAT') > -1 && pTrim.indexOf(':') > -1) {
                    vatVal = pTrim.substring(pTrim.indexOf(':') + 1).trim();
                    var pctMatch = pTrim.match(/VAT \(([0-9]+)%\)/i);
                    if (pctMatch && document.getElementById('prVatPctLabel')) {
                        document.getElementById('prVatPctLabel').textContent = pctMatch[1];
                    }
                } else if (pTrim.toLowerCase().indexOf('voucher') > -1 && pTrim.indexOf(':') > -1) {
                    var idx = pTrim.indexOf(':');
                    var vMoney = pTrim.substring(idx + 1).trim().replace(/^-/,'');
                    voucherVal = '- ' + vMoney;
                    var vCodeMatch = pTrim.match(/\(([^)]+)\)/);
                    if (vCodeMatch && (!data.voucherCode || data.voucherCode === "")) {
                        data.voucherCode = vCodeMatch[1];
                    }
                } else if (pTrim.toLowerCase().indexOf('giảm giá') > -1 && pTrim.indexOf(':') > -1) {
                    giamGiaVal = pTrim.substring(pTrim.indexOf(':') + 1).trim();
                } else if (pTrim.toLowerCase().indexOf('khách đưa') > -1 && pTrim.indexOf(':') > -1) {
                    khachDuaVal = pTrim.substring(pTrim.indexOf(':') + 1).trim();
                } else if (pTrim.toLowerCase().indexOf('tiền thừa') > -1 && pTrim.indexOf(':') > -1) {
                    tienThuaVal = pTrim.substring(pTrim.indexOf(':') + 1).trim();
                } else if (pTrim !== '') {
                    cleanNotes.push(pTrim);
                }
            });

            if (!vatVal) {
                var curVat = ${not empty currentVat ? currentVat : 8};
                var calculatedVAT = Math.round((data.tongTien || 0) * curVat / (100 + curVat));
                vatVal = Number(calculatedVAT).toLocaleString('vi-VN') + ' VNĐ';
                if (document.getElementById('prVatPctLabel')) document.getElementById('prVatPctLabel').textContent = curVat;
            }
            if (!giamGiaVal) giamGiaVal = '0 VNĐ';
            if (!voucherVal) voucherVal = '0 VNĐ';
            if (!khachDuaVal) khachDuaVal = Number(data.tongTien || 0).toLocaleString('vi-VN') + ' VNĐ';
            if (!tienThuaVal) tienThuaVal = '0 VNĐ';
            var finalGhiChu = cleanNotes.join(' - ');
            if (!finalGhiChu) finalGhiChu = 'Bán hàng tại quầy';

            if (vatVal && vatVal.indexOf('+') === -1 && parseInt(vatVal.replace(/[^0-9]/g, ''), 10) > 0) {
                vatVal = '+ ' + vatVal.trim();
            }
            if (giamGiaVal && giamGiaVal.indexOf('-') === -1 && parseInt(giamGiaVal.replace(/[^0-9]/g, ''), 10) > 0) {
                giamGiaVal = '- ' + giamGiaVal.trim();
            }

            var elVAT = document.getElementById('prVAT');
            var elGiamGia = document.getElementById('prGiamGia');
            var elVoucher = document.getElementById('prVoucher');
            var elKhachDua = document.getElementById('prKhachDua');
            var elTienThua = document.getElementById('prTienThua');
            if (elVAT) elVAT.textContent = vatVal;
            if (elGiamGia) elGiamGia.textContent = giamGiaVal;
            if (elVoucher) {
                if (data.voucherCode && data.voucherCode !== "") {
                    var vDesc = data.voucherCode + (data.voucherName ? " - " + data.voucherName : "");
                    var isMgr = ${sessionScope.role == 'MANAGER' or sessionScope.roleID == 1};
                    var badge = isMgr ? ('<a href="${pageContext.request.contextPath}/VoucherServlet?search=' + data.voucherCode + '" target="_blank" class="no-print" style="background:var(--color-bg-subtle);color:var(--color-primary);padding:2px 7px;border-radius:4px;font-size:12px;text-decoration:none;border:1px solid var(--color-border);font-weight:600;margin-left:4px;" title="Mở sang trang Quản lý Voucher">[' + vDesc + ']</a>') : ('<span class="no-print" style="background:var(--color-bg-subtle);color:var(--color-primary);padding:2px 7px;border-radius:4px;font-size:12px;border:1px solid var(--color-border);font-weight:600;margin-left:4px;">[' + vDesc + ']</span>');
                    elVoucher.innerHTML = '<span style="color:var(--color-danger);font-weight:700;">' + voucherVal + '</span> ' + badge + '<span style="display:none;color:var(--color-text);font-size:12px;" class="print-voucher"> (' + vDesc + ')</span>';
                } else {
                    elVoucher.textContent = voucherVal;
                }
            }
            if (elKhachDua) elKhachDua.textContent = khachDuaVal;
            if (elTienThua) elTienThua.textContent = tienThuaVal;
            document.getElementById('prGhiChu').textContent = finalGhiChu;

            document.getElementById('prTongTien').textContent = money(data.tongTien || 0);
            document.getElementById('prBody').innerHTML = (data.chiTiet || []).map(function(item, index) {
                var tenSP = item.tenSP;
                var bienThe = '-';
                var match = tenSP.match(/^(.*?)\s*\(([^)]+)\)$/);
                if (match) {
                    tenSP = match[1];
                    bienThe = match[2];
                }
                return '<tr><td style="text-align:center;">' + (index + 1) + '</td><td>' + tenSP + '</td><td style="white-space:nowrap;">' + bienThe + '</td><td style="text-align:center;" class="font-mono">' + item.soLuong + '</td><td class="money font-mono">' + money(item.donGia) + '</td><td class="money font-mono">' + money(item.thanhTien) + '</td></tr>';
            }).join('');
            openModal('modalPrint');
        })
        .catch(function() { alert('Không lấy được dữ liệu hóa đơn.'); });
}

function openModal(id) { document.getElementById(id).classList.add('open'); }
function closeModal(id) { document.getElementById(id).classList.remove('open'); }
document.querySelectorAll('.modal-bg').forEach(function(modal) {
    modal.addEventListener('click', function(event) {
        if (event.target === modal) modal.classList.remove('open');
    });
});

function cancelOrder(form, orderId) {
    if (!confirm('Bạn có chắc chắn muốn hủy hóa đơn này?')) return false;
    
    var btn = form.querySelector('button');
    if (btn) {
        btn.disabled = true;
        btn.textContent = 'Đang xử lý...';
    }
    
    var formData = new URLSearchParams();
    formData.append('action', 'changeStatus');
    formData.append('id', orderId);
    formData.append('trangThai', 'Đã hủy');
    
    fetch('${pageContext.request.contextPath}/OrderServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: formData.toString()
    }).then(function(response) {
        if (response.ok) {
            var row = form.closest('tr');
            if (row) {
                var statusSpan = row.querySelector('.status');
                if (statusSpan) {
                    statusSpan.className = 'badge badge-danger status';
                    statusSpan.textContent = 'Đã hủy';
                    statusSpan.style.whiteSpace = 'nowrap';
                }
                var actionCell = row.querySelector('.action-cell');
                if (actionCell) {
                    actionCell.innerHTML = '<button type="button" class="btn btn-outline" style="padding: 4px 10px; font-size: 12px;" onclick="printInvoice(\'' + orderId + '\')" title="In hóa đơn đã hủy">In biên bản</button>';
                }
                var cells = row.querySelectorAll('td');
                if (cells.length >= 8) {
                    cells[5].innerHTML = '<span class="badge badge-neutral" style="font-size: 11px;">Không có</span>';
                    cells[6].textContent = 'Hóa đơn chờ bị hủy do khách hàng hủy đơn hàng';
                }
            }
            var cancelStatEl = document.querySelector('.overview-grid .stat-card:nth-child(3) .stat-val');
            if (cancelStatEl && !isNaN(parseInt(cancelStatEl.textContent))) {
                cancelStatEl.textContent = parseInt(cancelStatEl.textContent) + 1;
            }
        } else {
            alert('Có lỗi xảy ra khi hủy hóa đơn.');
            if (btn) { btn.disabled = false; btn.textContent = 'Hủy'; }
        }
    }).catch(function(e) {
        console.error(e);
        alert('Có lỗi kết nối khi hủy hóa đơn.');
        if (btn) { btn.disabled = false; btn.textContent = 'Hủy'; }
    });
    return false;
}
</script>

<script>
var currentExOldPrice = 0;
var exVariantList = [];

function openExchangeModal(idDH, idCT, idSP, tenSP, donGia) {
    document.getElementById('exIdDH').value = idDH;
    document.getElementById('exIdCT').value = idCT;
    document.getElementById('exCurrentName').textContent = tenSP;
    currentExOldPrice = Number(donGia || 0);
    document.getElementById('exCurrentPrice').textContent = currentExOldPrice.toLocaleString('vi-VN') + ' đ';

    var sel = document.getElementById('exSelectNewVariant');
    sel.innerHTML = '<option value="">Đang tải danh sách biến thể còn hàng...</option>';

    fetch('${pageContext.request.contextPath}/OrderServlet?ajaxVariantsExchange=1&idSP=' + (idSP || ''))
        .then(function(r){ return r.json(); })
        .then(function(data){
            exVariantList = data || [];
            if (!data || data.length === 0) {
                sel.innerHTML = '<option value="">Không có biến thể nào còn tồn kho để đổi!</option>';
            } else {
                sel.innerHTML = data.map(function(v, idx){
                    return '<option value="' + v.idBT + '" data-price="' + v.giaBan + '" ' + (idx === 0 ? 'selected' : '') + '>'
                        + v.tenSP + ' — Màu: ' + v.mauSac + ' | Size: ' + v.size + ' (Giá: ' + Number(v.giaBan).toLocaleString('vi-VN') + ' đ | Còn: ' + v.soLuong + ')'
                        + '</option>';
                }).join('');
                calcExchangeDiff();
            }
        });

    openModal('modalExchange');
}

function calcExchangeDiff() {
    var sel = document.getElementById('exSelectNewVariant');
    var opt = sel.options[sel.selectedIndex];
    if (!opt) return;
    var newPrice = Number(opt.getAttribute('data-price') || 0);
    var diff = newPrice - currentExOldPrice;
    var diffEl = document.getElementById('exDiffAmount');

    if (diff > 0) {
        diffEl.textContent = '+ ' + diff.toLocaleString('vi-VN') + ' đ (Khách nạp thêm)';
        diffEl.style.color = 'var(--color-danger)';
    } else if (diff < 0) {
        diffEl.textContent = '- ' + Math.abs(diff).toLocaleString('vi-VN') + ' đ (Hoàn lại khách)';
        diffEl.style.color = 'var(--color-success)';
    } else {
        diffEl.textContent = '0 đ (Đổi ngang cùng giá)';
        diffEl.style.color = 'var(--color-primary)';
    }
}

function validateExchange() {
    var sel = document.getElementById('exSelectNewVariant');
    if (!sel.value) {
        alert('Vui lòng chọn một biến thể mới còn hàng để đổi!');
        return false;
    }
    return confirm('Bạn có chắc chắn muốn tiến hành đổi hàng? Hệ thống sẽ cập nhật kho và hóa đơn tức thì.');
}
</script>
</body>
</html>
