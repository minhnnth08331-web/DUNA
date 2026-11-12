<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% if (request.getAttribute("listKH") == null) {
    response.sendRedirect(request.getContextPath() + "/OrderServlet?view=sell"); return;
} %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bán hàng POS — DUNA Men's Wear</title>
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
                    <a href="${pageContext.request.contextPath}/OrderServlet?view=sell" class="active">Bán hàng</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet">Quản lý hóa đơn</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/ProductServlet">Quản lý sản phẩm</a>
                    <a href="${pageContext.request.contextPath}/EmployeeManagementServlet">Quản lý nhân sự</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet?view=report">Báo cáo thống kê</a>
                    <a href="${pageContext.request.contextPath}/CustomerServlet">Quản lý khách hàng</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet?view=sell" class="active">Bán hàng</a>
                    <a href="${pageContext.request.contextPath}/OrderServlet">Quản lý hóa đơn</a>
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

<div class="container" style="max-width: 1720px;">
    <div class="messages" style="margin-bottom: 16px;">
        <c:if test="${param.error == 'voucher_denied'}">
            <div class="alert alert-danger">Quyền truy cập bị từ chối: Tài khoản nhân viên không có quyền truy cập quản lý voucher.</div>
        </c:if>
        <c:if test="${param.success == '1'}">
            <div class="alert alert-success" style="display: flex; justify-content: space-between; align-items: center;">
                <span>Tạo hóa đơn thành công và ghi sổ giao dịch.</span>
                <c:if test="${not empty param.id}">
                    <button type="button" class="btn btn-secondary" style="padding: 4px 12px; font-size: 12px;" onclick="printInvoice('${param.id}')">In hóa đơn</button>
                </c:if>
            </div>
        </c:if>
        <c:if test="${param.success == 'hold'}">
            <div class="alert alert-warning" style="display: flex; justify-content: space-between; align-items: center;">
                <span>Đã lưu hóa đơn chờ thành công.</span>
                <c:if test="${not empty param.id}">
                    <a href="${pageContext.request.contextPath}/OrderServlet" class="btn btn-outline" style="padding: 4px 12px; font-size: 12px;">Xem danh sách hóa đơn</a>
                </c:if>
            </div>
        </c:if>
        <c:if test="${param.error == 'emptyCart'}"><div class="alert alert-warning">Giỏ hàng đang trống. Vui lòng thêm sản phẩm vào đơn.</div></c:if>
        <c:if test="${param.error == 'outOfStock'}"><div class="alert alert-danger">Sản phẩm không đủ số lượng tồn kho để xuất.</div></c:if>
        <c:if test="${param.error == 'notEnough'}"><div class="alert alert-danger">Số tiền khách đưa không đủ để thanh toán hóa đơn.</div></c:if>
        
        <c:if test="${not empty holdInfo}">
            <div class="alert alert-warning" style="display: flex; align-items: center; justify-content: space-between; gap: 16px;">
                <div>
                    <div style="font-weight: 700; font-size: 14.5px;">Đang tiếp tục thanh toán hóa đơn chờ: <span class="font-mono" style="color: var(--color-primary);">${holdInfo.maDH}</span></div>
                    <div style="font-size: 13px; margin-top: 2px;">Khách hàng: <b>${holdInfo.tenKH}</b>${not empty holdInfo.sdtKH ? ' | SĐT: '.concat(holdInfo.sdtKH) : ''} — Bổ sung thêm sản phẩm nếu có và bấm hoàn tất.</div>
                </div>
                <a href="${pageContext.request.contextPath}/OrderServlet" class="btn btn-outline" style="white-space: nowrap; font-size: 12px; padding: 6px 14px;">Quay lại sổ hóa đơn</a>
            </div>
        </c:if>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/OrderServlet" id="invoiceForm" onsubmit="return false">
        <input type="hidden" name="action" value="create">
        <input type="hidden" name="invoiceType" id="invoiceTypePost" value="paid">
        <input type="hidden" name="idKH" id="idKHPost" value="0">
        <input type="hidden" name="holdId" id="holdIdPost" value="${not empty holdId ? holdId : '0'}">
        <input type="hidden" name="discountAmount" id="discountAmountPost" value="0">
        <input type="hidden" name="vatAmount" id="vatAmountPost" value="0">
        <input type="hidden" name="vatPct" id="vatPctPost" value="${not empty currentVat ? currentVat : 8}">
        <input type="hidden" name="customerPaid" id="customerPaidPost" value="0">
        <input type="hidden" name="thanhToan" id="thanhToanPost" value="Tiền mặt">
        <input type="hidden" name="ghiChu" id="orderNotePost" value="Bán hàng tại quầy">
        <input type="hidden" name="voucherCode" id="voucherCodePost" value="">
        <input type="hidden" name="voucherAmount" id="voucherAmountPost" value="0">

        <div class="sell-layout">
            <!-- CỘT BÊN TRÁI: THANH TOÁN & THÔNG TIN KHÁCH HÀNG -->
            <aside class="side-col">
                <section class="panel summary card" style="position: sticky; top: 16px; padding: 20px;">
                    <!-- HEADER TÍNH TIỀN -->
                    <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 12px; margin-bottom: 16px;">
                        <div>
                            <div class="page-title-eyebrow">POS Terminal</div>
                            <h2 style="margin: 0; font-size: 17px; font-weight: 700; color: var(--color-text);">Thông Tin Thanh Toán</h2>
                        </div>
                        <span class="badge badge-neutral" style="font-size: 11.5px;">Thu ngân: ${not empty sessionScope.tenNV ? sessionScope.tenNV : 'Admin'}</span>
                    </div>

                    <!-- 1. KHÁCH HÀNG & THÀNH VIÊN -->
                    <div style="margin-bottom: 16px;">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;">
                            <label class="form-label" style="margin: 0;">Khách hàng / Thành viên</label>
                            <span style="font-size: 11.5px; color: var(--color-text-muted);">(Trống = Khách vãng lai)</span>
                        </div>
                        <div style="display: flex; gap: 8px; margin-bottom: 8px;">
                            <div class="kh-search-wrap" style="flex: 1; position: relative;">
                                <input type="text" class="form-control" id="khSearchInput"
                                       placeholder="Tìm SĐT, họ tên, mã..."
                                       autocomplete="off"
                                       oninput="searchCustomer(this.value);filterSelectBox(this.value)"
                                       onfocus="if(document.getElementById('khResults').childElementCount)document.getElementById('khDropdown').classList.add('open')">
                                <div class="kh-dropdown" id="khDropdown">
                                    <div id="khResults"></div>
                                </div>
                            </div>
                            <button type="button" class="btn btn-secondary" style="white-space: nowrap; padding: 0 14px;" onclick="openModal('modalAddKH')">+ Thêm KH</button>
                        </div>
                        <select id="customerSelectBox" class="form-select" onchange="onSelectBoxChange(this)">
                            <option value="">-- Chọn khách hàng thành viên --</option>
                            <c:forEach items="${listKH}" var="kh">
                                <option value="${kh.id}"
                                        data-ten="${fn:escapeXml(kh.ten)}"
                                        data-sdt="${fn:escapeXml(kh.sdt)}"
                                        data-hang="${fn:escapeXml(kh.hang)}"
                                        data-discount="${kh.discountPct}">
                                    ${kh.ten} (${kh.sdt})${not empty kh.hang && kh.hang != 'Thường' ? ' — '.concat(kh.hang) : ''}
                                </option>
                            </c:forEach>
                        </select>
                        <div id="khSelectedInfo" class="kh-selected-info" style="display: none; margin-top: 8px; background: var(--color-bg-subtle); padding: 10px 12px; border-radius: var(--radius-sm); border: 1px solid var(--color-border-subtle); font-size: 13px;"></div>
                    </div>

                    <!-- 2. VOUCHER & KHUYẾN MÃI -->
                    <div style="margin-bottom: 16px; padding-top: 14px; border-top: 1px dashed var(--color-border-subtle);">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;">
                            <label class="form-label" style="margin: 0;">Mã giảm giá (Voucher)</label>
                            <button type="button" class="btn btn-outline" style="padding: 2px 8px; font-size: 11px; height: 24px;" onclick="openVoucherLookupModal()" title="Tra cứu danh sách voucher khả dụng">Tra cứu Voucher</button>
                        </div>
                        <div style="display: flex; gap: 8px;">
                            <input type="text" id="voucherInput" class="form-control font-mono" placeholder="Nhập mã voucher (Ví dụ: YN100K, VIP...)" autocomplete="off" style="text-transform: uppercase;">
                            <button type="button" class="btn btn-secondary" onclick="applyVoucher()" style="white-space: nowrap; padding: 0 16px;">Áp dụng</button>
                            <button type="button" id="btnCancelVoucher" onclick="cancelVoucher()" class="btn btn-outline" style="color: var(--color-danger); border-color: var(--color-danger); display: none;" title="Hủy mã voucher">Hủy</button>
                        </div>
                        <div id="voucherResult" class="voucher-result" style="margin-top: 6px;"></div>
                    </div>

                    <!-- 3. TÍNH TOÁN & HÓA ĐƠN -->
                    <div style="display: flex; flex-direction: column; gap: 8px; padding-top: 14px; border-top: 1px dashed var(--color-border-subtle);">
                        <div style="display: flex; justify-content: space-between; align-items: center; font-size: 13.5px;">
                            <span style="color: var(--color-text-muted);">Tổng tiền hàng:</span>
                            <span id="subtotalText" class="font-mono" style="font-weight: 600; color: var(--color-text);">0 đ</span>
                        </div>
                        <div style="display: flex; justify-content: space-between; align-items: center; font-size: 13.5px;">
                            <span style="color: var(--color-text-muted);">Ưu đãi VIP (<span id="discountPctLabel">0</span>%):</span>
                            <span id="discountAmountText" class="font-mono" style="font-weight: 600; color: var(--color-danger);">- 0 đ</span>
                        </div>
                        <div id="voucherSummaryRow" style="display: none; justify-content: space-between; align-items: center; font-size: 13.5px;">
                            <span id="voucherSummaryLabel" style="color: var(--color-text-muted);">Mã Voucher:</span>
                            <span id="voucherAmountText" class="font-mono" style="font-weight: 600; color: var(--color-accent);">- 0 đ</span>
                        </div>
                        <div style="display: flex; justify-content: space-between; align-items: center; font-size: 13.5px;">
                            <span style="color: var(--color-text-muted); display: inline-flex; align-items: center; gap: 6px;">
                                <span>Thuế GTGT (<span id="vatPctLabel">${not empty currentVat ? currentVat : 8}</span>%):</span>
                                <c:if test="${sessionScope.role == 'MANAGER' or sessionScope.roleID == 1 or sessionScope.username == 'admin'}">
                                    <button type="button" onclick="openVatConfigModal()" title="Chỉnh sửa thuế suất VAT" class="btn btn-outline" style="padding: 1px 6px; font-size: 10px; height: 20px;">Sửa</button>
                                </c:if>
                            </span>
                            <span id="vatAmountText" class="font-mono" style="font-weight: 600; color: var(--color-text);">+ 0 đ</span>
                        </div>
                    </div>

                    <!-- TỔNG THANH TOÁN (GRAND TOTAL) -->
                    <div style="background: var(--color-surface); border: 2px solid var(--color-primary); border-radius: var(--radius-sm); padding: 12px 16px; margin: 16px 0 14px; display: flex; justify-content: space-between; align-items: center;">
                        <span style="font-size: 14px; font-weight: 700; text-transform: uppercase; letter-spacing: 0.05em; color: var(--color-primary);">Tổng Thanh Toán:</span>
                        <strong class="grand-total font-mono" id="grandTotalText" style="font-size: 22px; font-weight: 800; color: var(--color-accent);">0 đ</strong>
                    </div>

                    <!-- 4. PHƯƠNG THỨC THANH TOÁN -->
                    <div style="margin-bottom: 12px;">
                        <label class="form-label">Phương thức thanh toán:</label>
                        <div class="pay-method-row" style="display: flex; gap: 8px;">
                            <button type="button" class="pay-method-btn active" id="btnCash" onclick="selectPayMethod('cash')">
                                💵 Tiền mặt
                            </button>
                            <button type="button" class="pay-method-btn" id="btnBank" onclick="selectPayMethod('bank')">
                                🏦 Chuyển khoản QR
                            </button>
                        </div>
                    </div>

                    <!-- Tiền mặt Section -->
                    <div id="cashSection" style="background: var(--color-bg-subtle); border: 1px solid var(--color-border-subtle); padding: 12px 14px; border-radius: var(--radius-sm); margin-bottom: 16px;">
                        <div style="display: flex; align-items: center; justify-content: space-between; gap: 10px; margin-bottom: 8px;">
                            <label class="form-label" style="margin: 0; white-space: nowrap;">Khách đưa (VNĐ):</label>
                            <input class="form-control font-mono money-input" id="customerPaidInput" type="text" value="0" placeholder="0" oninput="handlePaidInput(this)" onfocus="if(this.value === '0') this.value = '';" onblur="if(this.value.trim() === '') { this.value = '0'; calculateTotals(); }" style="width: 160px; height: 36px; text-align: right; font-weight: 700; font-size: 15px;">
                        </div>
                        <div class="paid-warn" id="paidWarn" style="color: var(--color-danger); font-size: 12px; font-weight: 600; display: none; margin-bottom: 6px;">Chưa đủ số tiền thanh toán</div>
                        <div style="display: flex; align-items: center; justify-content: space-between; border-top: 1px dashed var(--color-border); padding-top: 8px;">
                            <span style="font-size: 13px; font-weight: 600; color: var(--color-text-muted);">Tiền thừa hoàn lại:</span>
                            <span class="change-money font-mono" id="changeText" style="font-size: 16px; font-weight: 700; color: var(--color-success);">0 đ</span>
                        </div>
                    </div>

                    <!-- Chuyển khoản Section (QR Code) -->
                    <div id="bankSection" class="qr-section" style="margin-bottom: 16px;">
                        <div style="font-weight: 600; font-size: 12px; color: var(--color-text-muted); margin-bottom: 8px; display: flex; justify-content: space-between;">
                            <span>Chọn Ngân hàng thụ hưởng:</span>
                            <span>Quét QR DUNA</span>
                        </div>
                        <div class="qr-bank-list" style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 6px; margin-bottom: 10px;">
                            <button type="button" class="qr-bank-btn active" onclick="selectBank(this,'VCB','Vietcombank')" data-bank="VCB">VCB</button>
                            <button type="button" class="qr-bank-btn" onclick="selectBank(this,'TCB','Techcombank')" data-bank="TCB">TCB</button>
                            <button type="button" class="qr-bank-btn" onclick="selectBank(this,'MB','MB Bank')" data-bank="MB">MB</button>
                            <button type="button" class="qr-bank-btn" onclick="selectBank(this,'BIDV','BIDV')" data-bank="BIDV">BIDV</button>
                            <button type="button" class="qr-bank-btn" onclick="selectBank(this,'VTB','VietinBank')" data-bank="VTB">Vietin</button>
                            <button type="button" class="qr-bank-btn" onclick="selectBank(this,'ACB','ACB')" data-bank="ACB">ACB</button>
                            <button type="button" class="qr-bank-btn" onclick="selectBank(this,'MOMO','MoMo')" data-bank="MOMO">MoMo</button>
                            <button type="button" class="qr-bank-btn" onclick="selectBank(this,'ZALOPAY','ZaloPay')" data-bank="ZALOPAY">ZaloPay</button>
                        </div>
                        <div style="display: flex; gap: 12px; align-items: stretch;">
                            <div class="qr-img-wrap" id="qrImgWrap" style="width: 160px; height: 160px; flex-shrink: 0; border: 1px solid var(--color-border); border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; background: #fff;">
                                <img id="qrCodeImg" src="" alt="QR Code" style="display: none; width: 100%; height: 100%; object-fit: contain;">
                                <div id="qrPlaceholder" style="color: var(--color-text-muted); font-size: 11.5px; padding: 12px; text-align: center; line-height: 1.4;">
                                    Mã QR sẽ hiển thị khi có sản phẩm.
                                </div>
                            </div>
                            <div style="flex: 1; display: flex; flex-direction: column; justify-content: space-between; gap: 8px;">
                                <div id="qrStatusContainer" style="padding: 8px 12px; background: var(--color-bg-subtle); border: 1px solid var(--color-border); border-radius: var(--radius-sm); font-size: 12px; font-weight: 600;">
                                    <span id="qrStatusText">Chờ khách quét & xác nhận...</span>
                                </div>
                                <div style="flex: 1; background: var(--color-surface); border: 1px solid var(--color-border-subtle); border-radius: var(--radius-sm); padding: 8px 10px; font-size: 11.5px; line-height: 1.5; overflow-y: auto; max-height: 105px;">
                                    <div style="font-weight: 700; color: var(--color-primary); margin-bottom: 2px;">Hướng dẫn chuyển khoản:</div>
                                    <div id="bankGuide" style="color: var(--color-text-muted);">Chọn ngân hàng để xem thông tin.</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- BUTTONS CHỐT ĐƠN -->
                    <div class="pay-btn-group" style="display: flex; gap: 10px;">
                        <c:if test="${empty holdInfo}">
                            <button type="button" class="btn btn-outline pay-btn-hold" onclick="submitInvoice('hold')" style="flex: 0 0 140px; height: 44px; font-weight: 700;" title="Lưu lại đơn hàng này để khách thanh toán sau">
                                Tạo Đơn Chờ
                            </button>
                        </c:if>
                        <button type="button" class="btn btn-primary pay-btn-main" onclick="submitInvoice('paid')" style="flex: 1; height: 44px; font-size: 14px; font-weight: 700; letter-spacing: 0.05em;">
                            THANH TOÁN NGAY
                        </button>
                    </div>
                </section>
            </aside>

            <!-- CỘT BÊN PHẢI: BARCODE INPUT & BẢNG GIỎ HÀNG -->
            <div class="main-col">
                <!-- BANNER CHẾ ĐỘ BÁN HÀNG NGOẠI TUYẾN (OFFLINE FALLBACK) -->
                <div id="networkStatusBanner" style="display: none; padding: 10px 16px; margin-bottom: 14px; border-radius: 8px; font-size: 13px; font-weight: 600; justify-content: space-between; align-items: center;">
                    <div id="networkStatusText" style="display: flex; align-items: center; gap: 8px;"></div>
                    <div id="networkStatusActions" style="display: flex; gap: 8px;"></div>
                </div>

                <!-- THANH BẮN MÃ VẠCH & PHÍM TẮT -->
                <section class="card" style="padding: 14px 18px;">
                    <div style="display: flex; gap: 10px; align-items: center; flex-wrap: wrap;">
                        <div style="flex: 1; min-width: 260px; position: relative;">
                            <input type="text" id="posBarcodeInput" class="form-control font-mono" placeholder="Bắn mã vạch sản phẩm hoặc gõ SKU... (Nhấn Enter)" autocomplete="off" autofocus onkeydown="handlePosBarcodeInput(event)" style="height: 42px; font-size: 14px; font-weight: 600; padding-left: 14px;">
                        </div>
                        <button type="button" class="btn btn-secondary no-print" onclick="openBarcodeScanner()" style="height: 42px; padding: 0 16px; white-space: nowrap;" title="Mở camera quét mã trực tiếp">Camera Barcode</button>
                        <button type="button" class="btn btn-outline no-print" onclick="openSizeConsultModal()" style="height: 42px; padding: 0 16px; white-space: nowrap;" title="Tư vấn chọn size chuẩn DUNA cho khách hàng">Tư Vấn Size</button>
                        <button type="button" class="btn btn-outline no-print" onclick="openManualProductModal()" style="height: 42px; padding: 0 16px; white-space: nowrap;" title="Tra cứu & chọn sản phẩm từ danh mục thủ công">Chọn Thủ Công</button>
                    </div>
                    <div id="posInputStatus" style="display: none; margin-top: 8px; font-size: 13px; font-weight: 600; padding: 6px 12px; border-radius: var(--radius-sm);"></div>
                </section>

                <!-- BẢNG GIỎ HÀNG CHÍNH -->
                <section class="card cart-panel" style="padding: 20px; min-height: 520px; display: flex; flex-direction: column;">
                    <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 14px; margin-bottom: 14px; flex-shrink: 0;">
                        <div>
                            <div class="page-title-eyebrow">Giỏ hàng hiện thời</div>
                            <h2 style="margin: 0; font-size: 16px; font-weight: 700; color: var(--color-text);">Danh Sách Mặt Hàng Trong Hóa Đơn</h2>
                        </div>
                        <span id="cartCountBadge" class="badge badge-primary font-mono" style="font-size: 12px;">0 sản phẩm</span>
                    </div>
                    <div id="cartContainer"></div>
                    <div id="cartDisplay" style="flex: 1; overflow-y: auto;">
                        <div class="cart-empty" style="text-align: center; padding: 90px 16px; color: var(--color-text-muted);">
                            <div style="font-size: 16px; font-weight: 600; color: var(--color-text);">Giỏ hàng đang trống</div>
                            <div style="font-size: 13.5px; margin-top: 4px;">Dùng súng quét mã vạch hoặc bấm "Chọn Thủ Công" để nạp sản phẩm vào đơn.</div>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </form>
</div>

<jsp:include page="footer.jsp" />

<!-- MODAL TRA CỨU & CHỌN HÀNG THỦ CÔNG -->
<div class="modal-bg" id="modalManualProducts">
    <div class="modal-box wide" style="max-width: 1200px;">
        <button class="close-btn" type="button" onclick="closeModal('modalManualProducts')">×</button>
        <div style="border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 14px; margin-bottom: 16px;">
            <div class="page-title-eyebrow">Dự phòng thủ công</div>
            <h3 style="margin: 0; font-size: 18px; font-weight: 700; color: var(--color-text);">Danh Mục Sản Phẩm & Chọn Hàng DUNA</h3>
        </div>
        <div class="alert alert-info" style="margin-bottom: 16px; font-size: 13px;">
            Dùng khi tem mã vạch bị mờ hoặc súng quét không nhận. Gõ mã hoặc tên để lọc, chọn Màu/Size rồi bấm <b>+ Thêm</b> vào đơn.
        </div>
        <div class="search-bar" style="margin-bottom: 16px; display: flex; gap: 8px;">
            <input type="text" id="productSearch" class="form-control" placeholder="Gõ mã SKU hoặc tên sản phẩm DUNA để tìm kiếm..." oninput="filterProducts(this.value)">
            <button type="button" class="btn btn-outline" onclick="filterProducts('');document.getElementById('productSearch').value=''">Làm mới</button>
        </div>
        <div style="overflow-y: auto; max-height: 560px; padding-right: 4px;">
            <div id="productGrid" class="product-grid" style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 14px;">
                <c:forEach items="${listSP}" var="sp">
                    <div class="product-card card" style="padding: 14px;"
                         data-masp="${fn:toLowerCase(fn:escapeXml(sp.maSP))}"
                         data-name="${fn:toLowerCase(fn:escapeXml(sp.tenSP))}">
                        <div>
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;">
                                <span class="badge badge-neutral" style="font-size: 10.5px;">${not empty sp.thuongHieu ? sp.thuongHieu : 'DUNA'}</span>
                                <c:choose>
                                    <c:when test="${sp.phanCap == 'Cao cấp'}"><span class="badge badge-primary">Cao cấp</span></c:when>
                                    <c:when test="${sp.phanCap == 'Trung cấp'}"><span class="badge badge-warning">Trung cấp</span></c:when>
                                    <c:otherwise><span class="badge badge-neutral">Tiêu chuẩn</span></c:otherwise>
                                </c:choose>
                            </div>
                            <div class="product-title" style="font-weight: 600; font-size: 13.5px; margin-bottom: 8px;" title="${sp.tenSP}">${not empty sp.maSP ? sp.maSP : 'SP'} — ${sp.tenSP}</div>
                            <div style="display: flex; justify-content: space-between; align-items: center; margin: 8px 0;">
                                <div id="price_display_${sp.id}" class="font-mono" style="font-size: 15px; font-weight: 700; color: var(--color-accent);"><fmt:formatNumber value="${sp.giaBan}" type="number" groupingUsed="true"/> đ</div>
                                <div style="display: flex; gap: 6px; align-items: center;">
                                    <button type="button" class="btn btn-outline" onclick="openProductInfoModal('${sp.id}', '${fn:escapeXml(sp.tenSP)}', '${fn:escapeXml(sp.maSP)}')" style="padding: 2px 8px; font-size: 11px; height: 24px;">Chi tiết</button>
                                    <div id="stock_display_${sp.id}" class="badge badge-success font-mono" style="font-size: 11px;">Tồn: ${sp.soLuong}</div>
                                </div>
                            </div>
                            <div style="margin: 8px 0; display: grid; grid-template-columns: 1fr 1fr; gap: 8px; background: var(--color-bg-subtle); padding: 8px; border-radius: var(--radius-sm);">
                                <div>
                                    <label class="form-label" style="font-size: 11px; margin-bottom: 2px;">Màu sắc:</label>
                                    <select id="mau_${sp.id}" class="form-select" onchange="onColorChange('${sp.id}')" style="height: 30px; font-size: 12px; padding: 2px 6px;">
                                        <c:choose>
                                            <c:when test="${not empty sp.mauSacList}">
                                                <c:forEach items="${sp.mauSacList}" var="m"><option value="${m}">${m}</option></c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <option>Đen</option><option>Trắng</option><option>Xanh navy</option><option>Xám</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </select>
                                </div>
                                <div>
                                    <label class="form-label" style="font-size: 11px; margin-bottom: 2px;">Size:</label>
                                    <select id="tl_${sp.id}" class="form-select" onchange="onWeightChange('${sp.id}')" style="height: 30px; font-size: 12px; padding: 2px 6px;">
                                        <c:choose>
                                            <c:when test="${not empty sp.trongLuongList}">
                                                <c:forEach items="${sp.trongLuongList}" var="tl"><option value="${tl}">${tl}</option></c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <option>S</option><option>M</option><option>L</option><option>XL</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </select>
                                </div>
                                <div class="barcode-display-box" style="grid-column: span 2; margin-top: 4px; padding: 6px; background: var(--color-surface); border: 1px dashed var(--color-border); border-radius: var(--radius-sm); display: flex; align-items: center; justify-content: center; gap: 10px;">
                                    <div id="qr_box_${sp.id}" style="flex-shrink: 0; background: #fff; padding: 2px; border-radius: 4px; border: 1px solid var(--color-border); line-height: 0;"></div>
                                    <div style="text-align: center; overflow: hidden; flex-grow: 1;">
                                        <svg id="bc_svg_${sp.id}" style="max-width: 100%; height: 32px; display: inline-block;"></svg>
                                        <div id="bc_txt_${sp.id}" class="font-mono" style="font-size: 11px; font-weight: 700; color: var(--color-text);"></div>
                                    </div>
                                </div>
                            </div>
                            <script id="vjson_${sp.id}" type="application/json">${sp.variantsJson}</script>
                        </div>
                        <div class="product-actions" style="display: grid; grid-template-columns: 70px 1fr; gap: 8px; margin-top: 8px;">
                            <input id="qty_${sp.id}" type="number" min="1" max="${sp.soLuong}" value="1" class="form-control font-mono" style="height: 34px; text-align: center;">
                            <button type="button" class="btn btn-primary" style="height: 34px; font-size: 13px;"
                                    data-id="${sp.id}"
                                    data-masp="${fn:escapeXml(sp.maSP)}"
                                    data-name="${fn:escapeXml(sp.tenSP)}"
                                    data-price="${sp.giaBan}"
                                    data-stock="${sp.soLuong}"
                                    onclick="if(addProductFromButton(this)){ showFeedbackToast('Đã thêm: ' + this.getAttribute('data-name')); playBeep(); }">+ Thêm</button>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div id="noProductMsg" style="display: none; color: var(--color-text-muted); text-align: center; padding: 36px; font-size: 15px;">Không tìm thấy sản phẩm phù hợp.</div>
        </div>
        <div class="modal-footer" style="margin-top: 16px;">
            <button type="button" onclick="closeModal('modalManualProducts'); setTimeout(function(){ document.getElementById('posBarcodeInput').focus(); }, 200);" class="btn btn-secondary">Đóng & Trở về</button>
        </div>
    </div>
</div>

<!-- MODAL CHI TIẾT SẢN PHẨM / BIẾN THỂ -->
<div class="modal-bg" id="modalProductInfo">
    <div class="modal-box wide">
        <button class="close-btn" type="button" onclick="closeModal('modalProductInfo')">×</button>
        <div style="border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 12px; margin-bottom: 16px;">
            <div class="page-title-eyebrow">Thông số mặt hàng</div>
            <h3 style="margin: 0; font-size: 18px; font-weight: 700; color: var(--color-text);"><span id="pInfoTitle">Thông số & Biến thể DUNA</span></h3>
        </div>
        <div style="display: flex; gap: 8px; margin-bottom: 16px; border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 8px;">
            <button type="button" class="btn btn-outline active" id="btn-tab-p-var" onclick="switchInfoTab('var')">Biến thể đang có</button>
            <button type="button" class="btn btn-outline" id="btn-tab-p-spec" onclick="switchInfoTab('spec')">Đặc tính kỹ thuật</button>
        </div>
        <div style="overflow-y: auto; max-height: 480px;">
            <div id="tab-p-var" class="info-tab-content" style="display: block;">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th style="width: 40px; text-align: center;">STT</th>
                            <th>Màu sắc</th>
                            <th>Size</th>
                            <th class="th-money">Giá bán</th>
                            <th style="text-align: center;">Tồn kho</th>
                            <th>Mã vạch Barcode</th>
                        </tr>
                    </thead>
                    <tbody id="pInfoVariantsBody">
                        <tr><td colspan="6" style="text-align: center; padding: 24px; color: var(--color-text-muted);">Đang tải dữ liệu biến thể...</td></tr>
                    </tbody>
                </table>
            </div>
            <div id="tab-p-spec" class="info-tab-content" style="display: none;">
                <table class="data-table">
                    <tbody id="pInfoSpecsBody">
                        <tr><td colspan="2" style="text-align: center; padding: 24px; color: var(--color-text-muted);">Đang tải đặc tính kỹ thuật...</td></tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" onclick="closeModal('modalProductInfo')" class="btn btn-outline">Đóng</button>
        </div>
    </div>
</div>

<!-- MODAL THÊM NHANH KHÁCH HÀNG -->
<div class="modal-bg" id="modalAddKH">
    <div class="modal-box">
        <button class="close-btn" type="button" onclick="closeModal('modalAddKH')">×</button>
        <div style="border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 12px; margin-bottom: 16px;">
            <div class="page-title-eyebrow">Hồ sơ khách hàng</div>
            <h3 style="margin: 0; font-size: 18px; font-weight: 700; color: var(--color-text);">Thêm Nhanh Khách Hàng Mới</h3>
        </div>
        <form id="formAddKH" onsubmit="return submitAddKH(event)">
            <div class="form-group">
                <label class="form-label">Họ và tên *</label>
                <input type="text" id="khName" class="form-control" required placeholder="Ví dụ: Nguyễn Văn A">
            </div>
            <div class="form-group">
                <label class="form-label">Số điện thoại *</label>
                <input type="tel" id="khSdt" class="form-control font-mono" required placeholder="0912345678" maxlength="10">
            </div>
            <div class="form-group">
                <label class="form-label">Email</label>
                <input type="email" id="khEmail" class="form-control" placeholder="khachhang@duna.vn">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="closeModal('modalAddKH')">Hủy</button>
                <button type="submit" class="btn btn-primary">Lưu & Chọn ngay</button>
            </div>
        </form>
    </div>
</div>

<!-- MODAL IN HÓA ĐƠN -->
<div class="modal-bg" id="modalPrint">
    <div class="modal-box wide" id="printContent" style="max-width: 680px;">
        <button class="close-btn no-print" type="button" onclick="closeModal('modalPrint')">×</button>
        <div class="invoice-header" style="text-align: center; border-bottom: 1px dashed var(--color-border); padding-bottom: 16px; margin-bottom: 16px;">
            <div style="font-family: 'Cinzel', serif; font-size: 20px; font-weight: 800; letter-spacing: 0.12em; color: var(--color-primary);">DUNA MEN'S WEAR</div>
            <div style="font-size: 12px; color: var(--color-text-muted); margin-top: 4px;">Sartorial Tailoring & Precision | Hotline: 1900 8888 | duna.vn</div>
            <div style="font-size: 17px; font-weight: 700; text-transform: uppercase; margin: 12px 0 6px; letter-spacing: 0.05em;">Phiếu Thanh Toán Bán Lẻ</div>
            <div class="font-mono" style="font-size: 13px; font-weight: 700; color: var(--color-primary);" id="prMaDH">Mã đơn: ...</div>
            <div class="font-mono" style="font-size: 12px; color: var(--color-text-muted);" id="prNgayTao">Ngày: ...</div>
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
                    <th style="white-space: nowrap;">Biến thể</th>
                    <th style="width: 50px; text-align: center;">SL</th>
                    <th class="th-money" style="white-space: nowrap;">Đơn giá</th>
                    <th class="th-money" style="white-space: nowrap;">Thành tiền</th>
                </tr>
            </thead>
            <tbody id="prBody"></tbody>
        </table>
        <div style="display: flex; justify-content: flex-end; margin: 16px 0; font-size: 13.5px; line-height: 1.8;">
            <div style="min-width: 320px; text-align: right;">
                <div><span style="color: var(--color-text-muted);">Thuế GTGT (<span id="prVatPctLabel">${not empty currentVat ? currentVat : 8}</span>%):</span> <span class="font-mono" id="prVAT" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
                <div><span style="color: var(--color-text-muted);">Ưu đãi VIP:</span> <span class="font-mono" id="prGiamGia" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
                <div><span style="color: var(--color-text-muted);">Voucher / Khuyến mãi:</span> <span class="font-mono" id="prVoucher" style="font-weight: 600; display: inline-block; min-width: 130px;">0 VNĐ</span></div>
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
            <button type="button" class="btn btn-primary" onclick="window.print()">In ngay</button>
        </div>
    </div>
</div>

<!-- MODAL CẤU HÌNH THUẾ VAT -->
<div class="modal-bg" id="modalVatConfig">
    <div class="modal-box" style="max-width: 450px;">
        <button class="close-btn" type="button" onclick="closeModal('modalVatConfig')">×</button>
        <div style="border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 12px; margin-bottom: 16px;">
            <div class="page-title-eyebrow">Cấu hình hệ thống</div>
            <h3 style="margin: 0; font-size: 18px; font-weight: 700; color: var(--color-text);">Cấu Hình Thuế Suất VAT</h3>
        </div>
        <div class="alert alert-info" style="margin-bottom: 16px; font-size: 13px;">
            Thay đổi mức VAT tại đây sẽ áp dụng ngay cho các hóa đơn bán ra tiếp theo trên toàn hệ thống.
        </div>
        <div style="margin-bottom: 18px;">
            <label class="form-label">Chọn mức thuế tiêu chuẩn:</label>
            <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 8px; margin-bottom: 14px;">
                <button type="button" class="btn btn-outline" onclick="selectVatPreset(0)">0%<br><small>Miễn</small></button>
                <button type="button" class="btn btn-outline" onclick="selectVatPreset(5)">5%<br><small>Ưu đãi</small></button>
                <button type="button" class="btn btn-outline" onclick="selectVatPreset(8)">8%<br><small>Chuẩn</small></button>
                <button type="button" class="btn btn-outline" onclick="selectVatPreset(10)">10%<br><small>Gốc</small></button>
            </div>
            <label class="form-label">Hoặc nhập mức VAT tùy chỉnh (%):</label>
            <div style="position: relative;">
                <input type="number" id="customVatInput" min="0" max="100" class="form-control font-mono" value="${not empty currentVat ? currentVat : 8}" style="font-size: 16px; font-weight: 700; padding-right: 36px;">
                <span style="position: absolute; right: 12px; top: 50%; transform: translateY(-50%); font-weight: 700; color: var(--color-text-muted);">%</span>
            </div>
        </div>
        <div id="vatConfigMsg" style="display: none; padding: 10px; border-radius: var(--radius-sm); font-size: 13px; font-weight: 600; margin-bottom: 14px; text-align: center;"></div>
        <div class="modal-footer">
            <button type="button" class="btn btn-outline" onclick="closeModal('modalVatConfig')">Hủy bỏ</button>
            <button type="button" class="btn btn-primary" onclick="saveVatConfig()">Lưu & Áp dụng</button>
        </div>
    </div>
</div>

<!-- MODAL TRA CỨU VOUCHER -->
<div class="modal-bg" id="modalVoucherLookup">
    <div class="modal-box wide" style="max-width: 900px;">
        <button class="close-btn" type="button" onclick="closeModal('modalVoucherLookup')">×</button>
        <div style="border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 12px; margin-bottom: 14px;">
            <div class="page-title-eyebrow">Tra cứu khuyến mãi</div>
            <h3 style="margin: 0; font-size: 18px; font-weight: 700; color: var(--color-text);">Danh Sách Voucher Trong Hệ Thống</h3>
        </div>
        <div style="display: flex; gap: 8px; margin-bottom: 12px;">
            <input type="text" id="lookupSearchInput" class="form-control" placeholder="Tìm theo mã voucher hoặc tên chương trình..." oninput="filterVoucherLookup()" style="flex: 1;">
            <select id="lookupStatusFilter" class="form-select" onchange="filterVoucherLookup()" style="width: 190px;">
                <option value="ALL">— Tất cả trạng thái —</option>
                <option value="available">Sẵn sàng</option>
                <option value="used">Đã sử dụng</option>
                <option value="locked">Đã khóa</option>
            </select>
        </div>
        <div id="voucherLookupContainer" style="overflow-y: auto; max-height: 400px; border: 1px solid var(--color-border); border-radius: var(--radius-sm);">
            <div style="text-align: center; padding: 32px; color: var(--color-text-muted);">Đang tải danh sách voucher...</div>
        </div>
        <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 14px;">
            <span id="lookupCountLabel" class="font-mono" style="font-size: 12.5px; color: var(--color-text-muted);">Đang hiển thị: 0 / 0 mã</span>
            <button type="button" onclick="closeModal('modalVoucherLookup')" class="btn btn-outline">Đóng lại</button>
        </div>
    </div>
</div>

<!-- MODAL TƯ VẤN SIZE DUNA -->
<div class="modal-bg" id="modalSizeConsult">
    <div class="modal-box" style="max-width: 620px;">
        <button class="close-btn" type="button" onclick="closeModal('modalSizeConsult')">×</button>
        <div style="border-bottom: 1px solid var(--color-border-subtle); padding-bottom: 12px; margin-bottom: 16px;">
            <div class="page-title-eyebrow">DUNA Sartorial Guide</div>
            <h3 style="margin: 0; font-size: 18px; font-weight: 700; color: var(--color-text);">Tư Vấn Chọn Size Chuẩn DUNA</h3>
        </div>
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 14px; margin-bottom: 16px;">
            <div class="form-group" style="margin: 0;">
                <label class="form-label">Chiều cao (cm):</label>
                <input type="number" id="scHeight" class="form-control font-mono" placeholder="Ví dụ: 175" oninput="calcSizeConsult()">
            </div>
            <div class="form-group" style="margin: 0;">
                <label class="form-label">Cân nặng (kg):</label>
                <input type="number" id="scWeight" class="form-control font-mono" placeholder="Ví dụ: 68" oninput="calcSizeConsult()">
            </div>
        </div>
        <div id="scResultBox" style="background: var(--color-primary); color: #fff; border-radius: var(--radius-sm); padding: 18px 24px; margin-bottom: 16px; display: flex; justify-content: space-around; align-items: center; text-align: center;">
            <div>
                <div style="font-size: 11.5px; text-transform: uppercase; letter-spacing: 0.05em; color: var(--color-text-muted); margin-bottom: 4px;">Size Áo (Sơ mi / Polo / Blazer)</div>
                <div id="scShirtSize" class="font-mono" style="font-size: 28px; font-weight: 800; color: #fff;">--</div>
            </div>
            <div style="width: 1px; height: 46px; background: rgba(255,255,255,0.2);"></div>
            <div>
                <div style="font-size: 11.5px; text-transform: uppercase; letter-spacing: 0.05em; color: var(--color-text-muted); margin-bottom: 4px;">Size Quần (Âu / Khaki)</div>
                <div id="scPantsSize" class="font-mono" style="font-size: 28px; font-weight: 800; color: var(--color-accent);">--</div>
            </div>
        </div>
        <div style="font-size: 12.5px; color: var(--color-text-muted); background: var(--color-bg-subtle); border: 1px solid var(--color-border-subtle); border-radius: var(--radius-sm); padding: 12px 14px; line-height: 1.6;">
            <b style="color: var(--color-text); display: block; margin-bottom: 4px;">Bảng Kích Cỡ Tham Chiếu DUNA:</b>
            • <b>Size S:</b> Dưới 58kg | 1m58 - 1m64 (Quần: 28-29)<br>
            • <b>Size M:</b> 59 - 65kg | 1m65 - 1m70 (Quần: 30)<br>
            • <b>Size L:</b> 66 - 72kg | 1m70 - 1m75 (Quần: 31-32)<br>
            • <b>Size XL:</b> 73 - 80kg | 1m75 - 1m80 (Quần: 33)<br>
            • <b>Size XXL:</b> Trên 80kg | Trên 1m80 (Quần: 34-36)
        </div>
        <div class="modal-footer" style="margin-top: 16px;">
            <button type="button" class="btn btn-primary" onclick="closeModal('modalSizeConsult')">Đã hiểu</button>
        </div>
    </div>
</div>

<!-- MODAL CAMERA SCANNER -->
<div class="scanner-modal-bg no-print" id="scannerModal">
    <div class="scanner-box card" style="max-width: 620px; padding: 24px;">
        <div class="scanner-title" style="font-size: 16px; font-weight: 700; margin-bottom: 12px; text-align: center;">Quét Mã Vạch Bằng Camera</div>
        <div class="scanner-viewport" style="position: relative; width: 100%; aspect-ratio: 4/3; border-radius: var(--radius-sm); overflow: hidden; background: #000; margin-bottom: 12px;">
            <div id="reader" style="width: 100%; height: 100%;"></div>
        </div>
        <div class="scan-status" id="scanStatus" style="font-size: 13px; font-weight: 600; text-align: center; margin-bottom: 12px;">Đang khởi động camera...</div>
        <div class="scan-result-card" id="scanResultCard" style="display: none; background: var(--color-bg-subtle); border: 1px solid var(--color-border); border-radius: var(--radius-sm); padding: 12px 14px; margin-bottom: 14px;">
            <div class="scan-result-name" id="scanResultName" style="font-weight: 700; color: var(--color-text);"></div>
            <div class="scan-result-meta" style="display: flex; gap: 8px; margin-top: 6px; font-size: 12px;">
                <span class="badge badge-primary font-mono" id="scanResultPrice"></span>
                <span class="badge badge-neutral" id="scanResultColor"></span>
                <span class="badge badge-neutral" id="scanResultWeight"></span>
                <span class="badge badge-success font-mono" id="scanResultStock"></span>
            </div>
        </div>
        <button type="button" class="btn btn-outline" style="width: 100%;" onclick="closeBarcodeScanner()">Đóng Camera</button>
    </div>
</div>

<div class="scan-beep-feedback no-print" id="scanFeedback"></div>

<!-- JS SCRIPTS & CLIENT LOGIC -->
<script src="https://unpkg.com/html5-qrcode@2.3.8/html5-qrcode.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.6/dist/JsBarcode.all.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/qrcode-generator/1.4.4/qrcode.min.js"></script>

<script>
function switchInfoTab(tab) {
    document.getElementById('tab-p-var').style.display = (tab === 'var') ? 'block' : 'none';
    document.getElementById('tab-p-spec').style.display = (tab === 'spec') ? 'block' : 'none';
    document.getElementById('btn-tab-p-var').classList.toggle('active', tab === 'var');
    document.getElementById('btn-tab-p-spec').classList.toggle('active', tab === 'spec');
}

function openProductInfoModal(id, ten, ma) {
    var titleEl = document.getElementById('pInfoTitle');
    if(titleEl) titleEl.innerHTML = 'Chi tiết: <span style="color:var(--color-primary);">' + (ma ? ma + ' — ' : '') + ten + '</span>';
    switchInfoTab('var');
    openModal('modalProductInfo');
    
    var tbV = document.getElementById('pInfoVariantsBody');
    var tbS = document.getElementById('pInfoSpecsBody');
    tbV.innerHTML = '<tr><td colspan="6" style="text-align:center; padding:24px; color:var(--color-text-muted);">Đang tra cứu từ hệ thống...</td></tr>';
    tbS.innerHTML = '<tr><td colspan="2" style="text-align:center; padding:24px; color:var(--color-text-muted);">Đang tải đặc tính kỹ thuật...</td></tr>';
    
    fetch('${pageContext.request.contextPath}/ProductServlet?ajaxDetailAll=1&idSP=' + id)
        .then(function(r){ return r.json(); })
        .then(function(data){
            if(!data.variants || !data.variants.length){ 
                tbV.innerHTML = '<tr><td colspan="6" style="text-align:center; padding:24px; color:var(--color-text-muted);">Chưa có biến thể nào được tạo.</td></tr>'; 
            } else {
                tbV.innerHTML = data.variants.map(function(v, i){
                    var bcId = 'bc-pinfo-svg-' + v.id;
                    var qrId = 'qr-pinfo-box-' + v.id;
                    var bcHtml = v.barcode 
                        ? '<div class="barcode-display-box" style="display:inline-flex;align-items:center;gap:8px;background:var(--color-surface);padding:4px 8px;border-radius:4px;border:1px solid var(--color-border)"><div id="'+qrId+'" style="flex-shrink:0;background:#fff;padding:2px;border:1px solid var(--color-border);border-radius:3px"></div><div><svg id="'+bcId+'" style="max-width:110px;height:32px"></svg><br><small style="color:var(--color-text);font-size:11px;font-weight:700;font-family:monospace">'+v.barcode+'</small></div></div>'
                        : '<span style="color:var(--color-text-muted); font-size:12px;">Chưa có mã vạch</span>';
                    var stockBadge = v.soLuong > 0 ? '<span class="badge badge-success font-mono">Còn: ' + v.soLuong + '</span>' : '<span class="badge badge-danger font-mono">Hết hàng</span>';
                    return '<tr>'
                        + '<td style="font-weight:700; text-align:center;" class="font-mono">' + (i+1) + '</td>'
                        + '<td style="font-weight:600;">' + (v.mauSac || '-') + '</td>'
                        + '<td style="font-weight:600;">' + (v.trongLuong || '-') + '</td>'
                        + '<td class="money font-mono" style="font-weight:700;">' + Number(v.giaBan).toLocaleString('vi-VN') + ' đ</td>'
                        + '<td style="text-align:center;">' + stockBadge + '</td>'
                        + '<td>' + bcHtml + '</td>'
                        + '</tr>';
                }).join('');
                
                setTimeout(function() {
                    data.variants.forEach(function(v) {
                        if (v.barcode) {
                            if (document.getElementById('bc-pinfo-svg-' + v.id)) {
                                try {
                                    JsBarcode('#bc-pinfo-svg-' + v.id, v.barcode, {
                                        format: 'CODE128', width: 1.5, height: 32,
                                        displayValue: false, margin: 2
                                    });
                                } catch(e) {}
                            }
                            var qrBox = document.getElementById('qr-pinfo-box-' + v.id);
                            if (window.qrcode && qrBox) {
                                try {
                                    var qr = qrcode(0, 'M');
                                    qr.addData(v.barcode);
                                    qr.make();
                                    qrBox.innerHTML = qr.createSvgTag(2.5, 0);
                                } catch(e) {}
                            }
                        }
                    });
                }, 50);
            }
            
            if(!data.specs || !data.specs.length){ 
                tbS.innerHTML = '<tr><td colspan="2" style="text-align:center; padding:24px; color:var(--color-text-muted);">Chưa có thông số kỹ thuật được cập nhật.</td></tr>'; 
            } else {
                tbS.innerHTML = data.specs.map(function(s){
                    return '<tr>'
                        + '<td style="width:35%; font-weight:700; padding:10px 14px; border-right:1px solid var(--color-border-subtle);">' + s.ten + '</td>'
                        + '<td style="font-weight:600; padding:10px 14px;">' + (s.giaTri || '<span style="color:var(--color-text-muted)">Chưa cập nhật</span>') + '</td>'
                        + '</tr>';
                }).join('');
            }
        }).catch(function(){ 
            tbV.innerHTML = '<tr><td colspan="6" style="text-align:center; padding:20px; color:var(--color-danger);">Lỗi tải kết nối dữ liệu. Vui lòng thử lại!</td></tr>'; 
            tbS.innerHTML = '<tr><td colspan="2" style="text-align:center; padding:20px; color:var(--color-danger);">Lỗi tải kết nối dữ liệu. Vui lòng thử lại!</td></tr>'; 
        });
}
</script>

<script>
var ALL_LOOKUP_VOUCHERS = [];

function openVoucherLookupModal() {
    var inp = document.getElementById('lookupSearchInput');
    if (inp) inp.value = '';
    var sel = document.getElementById('lookupStatusFilter');
    if (sel) sel.value = 'ALL';
    openModal('modalVoucherLookup');
    var container = document.getElementById('voucherLookupContainer');
    container.innerHTML = '<div style="text-align:center;padding:30px;color:var(--color-text-muted);font-weight:600;">Đang tải danh sách voucher từ CSDL...</div>';
    
    fetch('${pageContext.request.contextPath}/OrderServlet?ajaxLookupVouchers=1')
        .then(function(r){ return r.json(); })
        .then(function(data){
            if (!data.success || !data.vouchers || !data.vouchers.length) {
                container.innerHTML = '<div style="text-align:center;padding:30px;color:var(--color-danger);font-weight:600;">Hiện không có mã voucher nào khả dụng!</div>';
                return;
            }
            ALL_LOOKUP_VOUCHERS = data.vouchers;
            filterVoucherLookup();
        })
        .catch(function(){
            container.innerHTML = '<div style="text-align:center;padding:30px;color:var(--color-danger);font-weight:600;">Lỗi kết nối khi tải dữ liệu voucher!</div>';
        });
}

function filterVoucherLookup() {
    var kw = (document.getElementById('lookupSearchInput').value || '').trim().toLowerCase();
    var st = document.getElementById('lookupStatusFilter').value || 'ALL';
    var container = document.getElementById('voucherLookupContainer');
    var countLbl = document.getElementById('lookupCountLabel');
    
    var filtered = ALL_LOOKUP_VOUCHERS.filter(function(v) {
        var matchKw = (!kw || v.code.toLowerCase().indexOf(kw) > -1 || v.title.toLowerCase().indexOf(kw) > -1);
        var matchSt = (st === 'ALL' || v.status === st);
        return matchKw && matchSt;
    });
    
    if (countLbl) countLbl.textContent = 'Đang hiển thị: ' + filtered.length + ' / ' + ALL_LOOKUP_VOUCHERS.length + ' mã voucher';
    
    if (!filtered.length) {
        container.innerHTML = '<div style="text-align:center;padding:35px;color:var(--color-text-muted);font-weight:600;">Không tìm thấy mã voucher nào phù hợp bộ lọc!</div>';
        return;
    }
    
    var html = '<table class="data-table" style="margin: 0;">';
    html += '<thead>';
    html += '  <tr>';
    html += '    <th>Mã Voucher</th>';
    html += '    <th>Tên Chương Trình</th>';
    html += '    <th class="th-money">Mức Giảm</th>';
    html += '    <th>Điều Kiện Áp Dụng</th>';
    html += '    <th style="text-align:center;">Trạng Thái</th>';
    html += '  </tr>';
    html += '</thead>';
    html += '<tbody>';
    
    filtered.forEach(function(v) {
        var statusBadge = '';
        if (v.status === 'available') {
            statusBadge = '<span class="badge badge-success">Sẵn sàng</span>';
        } else if (v.status === 'used') {
            statusBadge = '<span class="badge badge-warning">Đã sử dụng</span>';
        } else {
            statusBadge = '<span class="badge badge-danger">Đã khóa</span>';
        }
        
        html += '<tr>';
        html += '  <td class="font-mono" style="font-weight: 700; color: var(--color-primary);">' + v.code + '</td>';
        html += '  <td style="font-weight: 600;">' + v.title + '</td>';
        html += '  <td class="money font-mono" style="font-weight: 700; color: var(--color-accent);">' + (v.amount <= 100 ? ('Giảm ' + v.amount + '%' + (v.maxCap ? ' (Tối đa ' + money(v.maxCap) + ')' : '')) : money(v.amount)) + '</td>';
        html += '  <td style="font-size: 12.5px; color: var(--color-text-muted);">Đơn tối thiểu ' + money(v.minOrder) + '</td>';
        html += '  <td style="text-align:center;">' + statusBadge + '</td>';
        html += '</tr>';
    });
    
    html += '</tbody></table>';
    container.innerHTML = html;
}

function openVatConfigModal() {
    document.getElementById('customVatInput').value = VAT_PCT;
    var msgEl = document.getElementById('vatConfigMsg');
    if (msgEl) msgEl.style.display = 'none';
    openModal('modalVatConfig');
}

function selectVatPreset(val) {
    document.getElementById('customVatInput').value = val;
}

function saveVatConfig() {
    var valStr = document.getElementById('customVatInput').value;
    var newVat = parseInt(valStr, 10);
    var msgEl = document.getElementById('vatConfigMsg');
    if (isNaN(newVat) || newVat < 0 || newVat > 100) {
        msgEl.className = 'alert alert-danger';
        msgEl.textContent = 'Mức thuế suất VAT phải hợp lệ (từ 0% đến 100%)!';
        msgEl.style.display = 'block';
        return;
    }
    msgEl.className = 'alert alert-warning';
    msgEl.textContent = 'Đang lưu cấu hình VAT lên hệ thống...';
    msgEl.style.display = 'block';
    
    fetch('${pageContext.request.contextPath}/OrderServlet?action=updateVAT&vatPct=' + newVat, { method: 'POST' })
        .then(function(r){ return r.json(); })
        .then(function(data){
            if (data.success) {
                VAT_PCT = data.newVat;
                var elLbl = document.getElementById('vatPctLabel');
                if (elLbl) elLbl.textContent = VAT_PCT;
                var prLbl = document.getElementById('prVatPctLabel');
                if (prLbl) prLbl.textContent = VAT_PCT;
                var postInp = document.getElementById('vatPctPost');
                if (postInp) postInp.value = VAT_PCT;
                calculateTotals();
                msgEl.className = 'alert alert-success';
                msgEl.textContent = data.message;
                setTimeout(function(){
                    closeModal('modalVatConfig');
                    msgEl.style.display = 'none';
                }, 1000);
            } else {
                msgEl.className = 'alert alert-danger';
                msgEl.textContent = data.message;
            }
        })
        .catch(function(){
            msgEl.className = 'alert alert-danger';
            msgEl.textContent = 'Lỗi kết nối máy chủ khi cập nhật VAT!';
        });
}
</script>

<!-- Store listKH as JS data -->
<script>
var ALL_KH = [
    <c:forEach items="${listKH}" var="kh" varStatus="s">
    {id:${kh.id},ten:'${fn:escapeXml(kh.ten)}',sdt:'${fn:escapeXml(kh.sdt)}',hang:'${fn:escapeXml(kh.hang)}',discountPct:${kh.discountPct},daysLeft:${not empty kh.daysLeft ? kh.daysLeft : 'null'}}${!s.last ? ',' : ''}
    </c:forEach>
];
</script>

<script>
var cart = [];
var currentDiscountPct = 0;
var VAT_PCT = ${not empty currentVat ? currentVat : 8};
var selectedKH = null;
var currentPayMethod = 'cash';
var selectedBank = 'VCB';
var appliedVoucherCode = '';
var appliedVoucherAmount = 0;
var appliedVoucherMinOrder = 0;

function money(v) { return Number(v||0).toLocaleString('vi-VN') + ' đ'; }

function searchCustomer(kw) {
    kw = kw.trim().toLowerCase();
    var dropdown = document.getElementById('khDropdown');
    var results = document.getElementById('khResults');
    if (!kw) { dropdown.classList.remove('open'); results.innerHTML=''; return; }
    var filtered = ALL_KH.filter(function(k) {
        return (k.ten||'').toLowerCase().indexOf(kw) >= 0
            || (k.sdt||'').indexOf(kw) >= 0
            || ('kh-'+k.id).indexOf(kw) >= 0
            || String(k.id).indexOf(kw) >= 0;
    }).slice(0, 12);
    if (!filtered.length) {
        results.innerHTML = '<div class="kh-option" style="color:var(--color-text-muted);cursor:default">Không tìm thấy khách hàng</div>';
    } else {
        results.innerHTML = filtered.map(function(k) {
            var badge = rankBadge(k.hang);
            return '<div class="kh-option" onclick="selectCustomerById(' + k.id + ')">' +
                '<span><b>' + escHtml(k.ten) + '</b> ' + badge + '</span>' +
                '<span class="font-mono" style="color:var(--color-text-muted);font-size:12px">' + escHtml(k.sdt||'') + '</span></div>';
        }).join('');
    }
    dropdown.classList.add('open');
}

function selectCustomerById(id) {
    var kh = ALL_KH.find(function(k) { return k.id === Number(id); });
    if (kh) selectCustomer(kh);
}

function escHtml(s) { return (s||'').replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;'); }
function rankBadge(hang) {
    var cls = 'badge-neutral';
    if ((hang||'').indexOf('VIP')!==-1) cls='badge-primary';
    else if ((hang||'').indexOf('ng')!==-1 && (hang||'').indexOf('V')===0) cls='badge-warning';
    else if ((hang||'').indexOf('c')!==-1 && (hang||'').indexOf('B')===0) cls='badge-neutral';
    return '<span class="badge '+cls+'" style="font-size:11px;margin-left:4px;">'+(hang||'Thường')+'</span>';
}

function selectCustomer(kh) {
    selectedKH = kh;
    currentDiscountPct = kh.discountPct || 0;
    document.getElementById('idKHPost').value = kh.id;
    document.getElementById('khSearchInput').value = '';
    document.getElementById('khDropdown').classList.remove('open');
    filterSelectBox('');
    var sel = document.getElementById('customerSelectBox');
    if (sel) sel.value = kh.id;
    var infoEl = document.getElementById('khSelectedInfo');
    infoEl.style.display = 'block';

    var offlineReminderHtml = '';
    if (kh.hang && kh.hang !== 'Thường' && kh.daysLeft !== null && kh.daysLeft !== undefined && kh.daysLeft <= 14) {
        offlineReminderHtml = '<div style="margin-top: 8px; padding: 8px 12px; background: rgba(245, 158, 11, 0.12); border: 1px solid rgba(245, 158, 11, 0.35); border-radius: 6px; font-size: 12.5px; color: #b45309; line-height: 1.5;">' +
            '<strong>⚠️ NHẮC TRỰC TIẾP TẠI QUẦY (Offline):</strong> Hạng <b>' + escHtml(kh.hang) + '</b> của khách chỉ còn <b>' + kh.daysLeft + ' ngày</b> nữa là hết chu kỳ 3 tháng.<br>' +
            '<span style="font-size: 11.5px; color: var(--color-text);">🗣️ <i>Gợi ý tư vấn:</i> "Dạ anh/chị ơi, hạng <b>' + escHtml(kh.hang) + '</b> (chiết khấu ' + currentDiscountPct + '%) của mình còn ' + kh.daysLeft + ' ngày nữa là đến hạn reset chu kỳ 3 tháng. Đơn hàng hôm nay của anh/chị sẽ tự động gia hạn thêm 3 tháng và tiếp tục tích lũy sản phẩm để thăng hạng mới ạ!"</span>' +
            '</div>';
    }

    infoEl.innerHTML = '<div style="display:flex;justify-content:space-between;align-items:center;">' +
        '<div><b>' + escHtml(kh.ten) + '</b> ' + rankBadge(kh.hang) +
        '<span style="color:var(--color-text-muted);margin-left:8px;">SĐT: ' + escHtml(kh.sdt||'-') + '</span>' +
        (currentDiscountPct > 0 ? ' &nbsp;|&nbsp; <span style="color:var(--color-success);font-weight:700">Ưu đãi hạng: ' + currentDiscountPct + '%</span>' : '') +
        '</div><button type="button" class="btn btn-outline" onclick="clearCustomer()" style="font-size:11px;padding:2px 8px;color:var(--color-danger);border-color:var(--color-danger);">Bỏ chọn</button></div>' +
        offlineReminderHtml;
    calculateTotals();
    if (typeof appliedVoucherCode !== 'undefined' && appliedVoucherCode && appliedVoucherCode.toUpperCase().startsWith('VIP')) {
        setTimeout(function(){ applyVoucher(); }, 100);
    }
}

function clearCustomer() {
    selectedKH = null; currentDiscountPct = 0;
    var idPost = document.getElementById('idKHPost');
    if (idPost) idPost.value = '0';
    var searchInput = document.getElementById('khSearchInput');
    if (searchInput) searchInput.value = '';
    var selectedInfo = document.getElementById('khSelectedInfo');
    if (selectedInfo) selectedInfo.style.display = 'none';
    filterSelectBox('');
    var sel = document.getElementById('customerSelectBox');
    if (sel) {
        sel.value = '';
        sel.selectedIndex = 0;
    }
    calculateTotals();
    if (typeof appliedVoucherCode !== 'undefined' && appliedVoucherCode && appliedVoucherCode.toUpperCase().startsWith('VIP')) {
        setTimeout(function(){ applyVoucher(); }, 100);
    }
}

function onSelectBoxChange(sel) {
    var id = parseInt(sel.value, 10);
    if (!id) {
        clearCustomer();
        return;
    }
    var kh = ALL_KH.find(function(k){ return k.id === id; });
    if (kh) {
        selectCustomer(kh);
    } else {
        var opt = sel.options[sel.selectedIndex];
        selectCustomer({
            id: id,
            ten: opt.getAttribute('data-ten') || opt.text,
            sdt: opt.getAttribute('data-sdt') || '',
            hang: opt.getAttribute('data-hang') || 'Thường',
            discountPct: parseInt(opt.getAttribute('data-discount')||'0', 10)
        });
    }
}

function filterSelectBox(kw) {
    kw = kw.trim().toLowerCase();
    var sel = document.getElementById('customerSelectBox');
    if (!sel) return;
    var opts = sel.options;
    for (var i = 1; i < opts.length; i++) {
        var text = opts[i].text.toLowerCase();
        var ten  = (opts[i].getAttribute('data-ten') || '').toLowerCase();
        var sdt  = (opts[i].getAttribute('data-sdt') || '').toLowerCase();
        opts[i].hidden = kw ? (text.indexOf(kw) < 0 && ten.indexOf(kw) < 0 && sdt.indexOf(kw) < 0) : false;
    }
}

document.addEventListener('click', function(e) {
    if (!e.target.closest('.kh-search-wrap')) document.getElementById('khDropdown').classList.remove('open');
});

function filterProducts(kw) {
    kw = kw.trim().toLowerCase();
    var cards = document.querySelectorAll('#productGrid .product-card');
    var found = 0;
    cards.forEach(function(c) {
        var match = !kw || (c.getAttribute('data-masp')||'').indexOf(kw)>=0 || (c.getAttribute('data-name')||'').indexOf(kw)>=0;
        c.classList.toggle('hidden', !match);
        if (match) found++;
    });
    document.getElementById('noProductMsg').style.display = found===0 ? 'block' : 'none';
}

function addProductFromButton(btn) {
    var id = btn.getAttribute('data-id');
    var mauEl = document.getElementById('mau_'+id);
    var tlEl = document.getElementById('tl_'+id);
    var tonKho = parseInt(btn.getAttribute('data-stock')||'0', 10);
    var jsonEl = document.getElementById('vjson_' + id);
    if (jsonEl) {
        try {
            var variants = JSON.parse(jsonEl.textContent || '[]');
            var selMau = mauEl ? mauEl.value.trim() : '';
            var selTl = tlEl ? tlEl.value.trim() : '';
            var found = variants.find(function(v) { return v.mau === selMau && v.tl === selTl; });
            if (!found && variants.length > 0) found = variants.find(function(v) { return v.mau === selMau; }) || variants[0];
            if (found && found.sl !== undefined) tonKho = found.sl;
        } catch(e) {}
    }
    return addToCart(id, btn.getAttribute('data-masp'), btn.getAttribute('data-name'),
              btn.getAttribute('data-price'), tonKho,
              mauEl?mauEl.value:'', tlEl?tlEl.value:'');
}

function addToCart(id, maSP, tenSP, gia, tonKho, mauSac, trongLuong) {
    var qtyInput = document.getElementById('qty_'+id);
    var parsedQty = parseInt(qtyInput.value, 10);
    if (isNaN(parsedQty) || parsedQty <= 0) {
        alert('Số lượng thêm vào không hợp lệ (phải > 0)!');
        if(qtyInput) qtyInput.value = 1;
        return false;
    }
    var qty = parsedQty;
    tonKho = Number(tonKho);
    var existing = cart.find(function(i){return i.id == id && (i.mauSac||'') === (mauSac||'') && (i.trongLuong||'') === (trongLuong||'')});
    var currentQty = existing ? existing.qty : 0;
    if (currentQty + qty > tonKho) { alert('Vượt số lượng tồn kho (' + tonKho + ')!'); return false; }
    if (existing) existing.qty += qty;
    else cart.push({id:id, maSP:maSP||'SP', tenSP:tenSP, gia:Number(gia), tonKho:tonKho, qty:qty, mauSac:mauSac||'', trongLuong:trongLuong||''});
    if(qtyInput) qtyInput.value = 1;
    renderCart();
    return true;
}

function changeQty(idx, diff) {
    var item = cart[idx];
    var next = item.qty + diff;
    if (next < 1) return;
    if (next > item.tonKho) { alert('Vượt tồn kho hiện tại!'); return; }
    item.qty = next; renderCart();
}

function removeCart(idx) { cart.splice(idx,1); renderCart(); }

function renderCart() {
    var display = document.getElementById('cartDisplay');
    var hidden = document.getElementById('cartContainer');
    var badge = document.getElementById('cartCountBadge');
    if (!cart.length) {
        display.innerHTML = '<div class="cart-empty" style="text-align: center; padding: 90px 16px; color: var(--color-text-muted);"><div style="font-size: 16px; font-weight: 600; color: var(--color-text);">Giỏ hàng đang trống</div><div style="font-size: 13.5px; margin-top: 4px;">Dùng súng quét mã vạch hoặc bấm "Chọn Thủ Công" để nạp sản phẩm vào đơn.</div></div>';
        hidden.innerHTML = '';
        if (badge) badge.textContent = '0 sản phẩm';
        calculateTotals();
        return;
    }
    if (badge) {
        var totQty = cart.reduce(function(s,i){return s+i.qty},0);
        badge.textContent = totQty + ' sản phẩm (' + cart.length + ' dòng)';
    }
    display.innerHTML = '<table class="data-table" style="margin-top: 4px;">' +
        '<thead><tr>' +
        '<th>Sản phẩm</th>' +
        '<th style="width: 100px;">Mã SKU</th>' +
        '<th style="width: 110px; text-align: center;">Số lượng</th>' +
        '<th class="th-money" style="width: 120px;">Đơn giá</th>' +
        '<th class="th-money" style="width: 130px;">Thành tiền</th>' +
        '<th style="width: 60px; text-align: right;">Xóa</th>' +
        '</tr></thead><tbody>' +
        cart.map(function(item, i) {
            var v = (item.mauSac||item.trongLuong)?'<div style="font-size:12px;color:var(--color-text-muted);margin-top:2px;">Màu: '+(item.mauSac||'—')+' | Size: '+(item.trongLuong||'—')+'</div>':'';
            return '<tr>' +
                '<td style="font-weight:600;color:var(--color-text);">'+item.tenSP+v+'</td>' +
                '<td class="font-mono" style="font-weight:600;color:var(--color-text-muted);">'+item.maSP+'</td>' +
                '<td style="text-align:center;"><div style="display:inline-flex;align-items:center;gap:4px;background:var(--color-bg-subtle);border:1px solid var(--color-border);border-radius:4px;padding:2px 4px;"><button type="button" class="btn btn-outline" style="width:24px;height:24px;padding:0;font-weight:700;" onclick="changeQty('+i+',-1)">-</button><strong class="font-mono" style="min-width:24px;text-align:center;font-size:13.5px;">'+item.qty+'</strong><button type="button" class="btn btn-outline" style="width:24px;height:24px;padding:0;font-weight:700;" onclick="changeQty('+i+',1)">+</button></div></td>' +
                '<td class="money font-mono" style="font-size:13px;color:var(--color-text-muted);">'+money(item.gia)+'</td>' +
                '<td class="money font-mono" style="font-weight:700;color:var(--color-text);">'+money(item.gia*item.qty)+'</td>' +
                '<td style="text-align:right;"><button type="button" onclick="removeCart('+i+')" class="btn btn-outline" style="padding:2px 8px;font-size:11px;color:var(--color-danger);border-color:var(--color-danger);">✕</button></td>' +
                '</tr>';
        }).join('') + '</tbody></table>';
    hidden.innerHTML = cart.map(function(item){
        return '<input type="hidden" name="idSP[]" value="'+item.id+'">'+
               '<input type="hidden" name="qty[]" value="'+item.qty+'">'+
               '<input type="hidden" name="mauSac[]" value="'+(item.mauSac||'')+'">'+
               '<input type="hidden" name="trongLuong[]" value="'+(item.trongLuong||'')+'">'+
               '<input type="hidden" name="donGia[]" value="'+item.gia+'">'; }).join('');
    calculateTotals();
}

// -------------------------------------------------------------
// NHÓM 4: CHẾ ĐỘ BÁN HÀNG NGOẠI TUYẾN (OFFLINE POS FALLBACK)
// -------------------------------------------------------------
function initOfflinePOS() {
    var banner = document.getElementById('networkStatusBanner');
    var textEl = document.getElementById('networkStatusText');
    var actionsEl = document.getElementById('networkStatusActions');
    if (!banner || !textEl || !actionsEl) return;
    
    function updateOnlineStatus() {
        var isOnline = navigator.onLine;
        var offlineOrders = JSON.parse(localStorage.getItem('duna_offline_orders') || '[]');
        if (!isOnline) {
            banner.style.display = 'flex';
            banner.style.background = '#fef3c7';
            banner.style.color = '#92400e';
            banner.style.border = '1px solid #f59e0b';
            textEl.innerHTML = '⚠️ <b>MẤT KẾT NỐI INTERNET:</b> Đang bật Chế độ POS Ngoại Tuyến. Đơn hàng sẽ được lưu tạm tại trình duyệt và không làm gián đoạn bán hàng.';
            actionsEl.innerHTML = '<span class="badge font-mono" style="background:#d97706;color:#fff;">' + offlineOrders.length + ' đơn chờ đồng bộ</span>';
        } else if (offlineOrders.length > 0) {
            banner.style.display = 'flex';
            banner.style.background = '#ecfdf5';
            banner.style.color = '#065f46';
            banner.style.border = '1px solid #10b981';
            textEl.innerHTML = '🟢 <b>ĐÃ CÓ MẠNG TRỞ LẠI:</b> Có <b>' + offlineOrders.length + '</b> đơn hàng lưu ngoại tuyến cần đồng bộ lên hệ thống.';
            actionsEl.innerHTML = '<button type="button" class="btn btn-primary" style="padding:4px 12px; font-size:12px;" onclick="syncOfflineOrders()">Đồng Bộ Ngay (' + offlineOrders.length + ')</button>';
        } else {
            banner.style.display = 'none';
        }
    }

    window.addEventListener('online', updateOnlineStatus);
    window.addEventListener('offline', updateOnlineStatus);
    updateOnlineStatus();
}

function syncOfflineOrders() {
    var offlineOrders = JSON.parse(localStorage.getItem('duna_offline_orders') || '[]');
    if (!offlineOrders.length) {
        alert('Không có đơn hàng ngoại tuyến nào cần đồng bộ!');
        return;
    }
    if (!confirm('Đồng bộ ' + offlineOrders.length + ' đơn hàng ngoại tuyến lên máy chủ DUNA ngay bây giờ?')) return;
    
    var order = offlineOrders.shift();
    localStorage.setItem('duna_offline_orders', JSON.stringify(offlineOrders));
    
    var formData = new FormData();
    for (var k in order) formData.append(k, order[k]);
    fetch('${pageContext.request.contextPath}/OrderServlet', {
        method: 'POST',
        body: new URLSearchParams(formData)
    }).then(function() {
        if (offlineOrders.length > 0) {
            syncOfflineOrders();
        } else {
            alert('✅ Đã đồng bộ tất cả đơn hàng ngoại tuyến lên hệ thống DUNA thành công!');
            location.reload();
        }
    }).catch(function(err) {
        alert('Có lỗi khi đồng bộ đơn: ' + err.message);
    });
}

function openManualProductModal() {
    openModal('modalManualProducts');
    setTimeout(function() {
        var inp = document.getElementById('productSearch');
        if (inp) inp.focus();
    }, 200);
}

function handlePosBarcodeInput(event) {
    if (event.key === 'Enter' || event.keyCode === 13) {
        event.preventDefault();
        var val = (event.target.value || '').trim().toUpperCase();
        if (!val) return;
        
        var statusEl = document.getElementById('posInputStatus');
        if (statusEl) {
            statusEl.style.display = 'block';
            statusEl.className = 'alert alert-info';
            statusEl.textContent = 'Đang kiểm tra mã vạch: ' + val + '...';
        }
        
        fetch(scannerContextPath + '/OrderServlet?ajaxScanBarcode=1&barcode=' + encodeURIComponent(val))
            .then(function(r) { return r.json(); })
            .then(function(data) {
                if (data.found) {
                    addToCartByBarcode(data);
                    playBeep();
                    showFeedbackToast('Đã thêm: ' + data.tenSP);
                    event.target.value = '';
                    if (statusEl) {
                        statusEl.className = 'alert alert-success';
                        statusEl.textContent = 'Đã thêm: ' + data.tenSP;
                        setTimeout(function() { statusEl.style.display = 'none'; }, 2500);
                    }
                } else {
                    if (statusEl) {
                        statusEl.className = 'alert alert-danger';
                        statusEl.textContent = 'Không tìm thấy sản phẩm nào khớp với mã: ' + val;
                    }
                    playErrorBeep();
                }
            })
            .catch(function() {
                if (statusEl) {
                    statusEl.className = 'alert alert-danger';
                    statusEl.textContent = 'Lỗi kết nối đến máy chủ!';
                }
            });
    }
}

function playErrorBeep() {
    try {
        var ctx = new (window.AudioContext || window.webkitAudioContext)();
        var o = ctx.createOscillator();
        var g = ctx.createGain();
        o.connect(g); g.connect(ctx.destination);
        o.frequency.value = 220;
        o.type = 'sawtooth';
        g.gain.setValueAtTime(0.4, ctx.currentTime);
        o.start(ctx.currentTime);
        o.stop(ctx.currentTime + 0.35);
    } catch(e) {}
}

function handlePaidInput(el) {
    var raw = (el.value || '').replace(/[^0-9]/g, '');
    if (raw === '') {
        el.value = '';
    } else {
        raw = parseInt(raw, 10).toString();
        el.value = raw.replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    }
    calculateTotals();
}

function calculateTotals() {
    var subtotal = cart.reduce(function(t,i){return t+i.gia*i.qty},0);
    if (!cart.length && appliedVoucherCode) {
        appliedVoucherCode = '';
        appliedVoucherAmount = 0;
        appliedVoucherMinOrder = 0;
        if(document.getElementById('voucherInput')) document.getElementById('voucherInput').value = '';
        var resEl = document.getElementById('voucherResult');
        if (resEl) { resEl.className = 'voucher-result'; resEl.textContent = ''; resEl.style.display = 'none'; }
        if (document.getElementById('btnCancelVoucher')) document.getElementById('btnCancelVoucher').style.display = 'none';
    } else if (appliedVoucherCode && appliedVoucherMinOrder > 0 && subtotal < appliedVoucherMinOrder) {
        appliedVoucherCode = '';
        appliedVoucherAmount = 0;
        var reqMin = appliedVoucherMinOrder;
        appliedVoucherMinOrder = 0;
        if(document.getElementById('voucherInput')) document.getElementById('voucherInput').value = '';
        var resEl = document.getElementById('voucherResult');
        if (resEl) {
            resEl.className = 'alert alert-warning';
            resEl.textContent = 'Đã tự động gỡ voucher do đơn hàng chưa đạt mức tối thiểu (' + money(reqMin) + ')!';
            resEl.style.display = 'block';
        }
        if (document.getElementById('btnCancelVoucher')) document.getElementById('btnCancelVoucher').style.display = 'none';
    }

    var disc = Math.round(subtotal * currentDiscountPct / 100);
    var voucherDisc = appliedVoucherAmount > 0 ? Math.min(appliedVoucherAmount, subtotal - disc) : 0;
    var taxableBase = Math.max(0, subtotal - disc - voucherDisc);
    var vat = Math.round(taxableBase * VAT_PCT / 100); 
    var grand = taxableBase + vat;

    var paidStr = (document.getElementById('customerPaidInput').value || '').replace(/[^0-9]/g, '');
    var paid = parseFloat(paidStr) || 0;
    if (paid < 0) paid = 0;
    var change = Math.max(0, paid - grand);
    document.getElementById('subtotalText').textContent = money(subtotal);
    document.getElementById('discountPctLabel').textContent = currentDiscountPct;
    document.getElementById('discountAmountText').textContent = '- ' + money(disc);
    document.getElementById('vatAmountText').textContent = '+ ' + money(vat);
    document.getElementById('grandTotalText').textContent = money(grand);
    document.getElementById('changeText').textContent = money(change);
    var warnEl = document.getElementById('paidWarn');
    warnEl.style.display = (paid > 0 && paid < grand) ? 'block' : 'none';
    document.getElementById('discountAmountPost').value = disc;
    document.getElementById('vatAmountPost').value = vat;
    document.getElementById('customerPaidPost').value = paid;
    
    var vRow = document.getElementById('voucherSummaryRow');
    if (vRow) vRow.style.display = voucherDisc > 0 ? 'flex' : 'none';
    var vText = document.getElementById('voucherAmountText');
    if (vText) vText.textContent = '- ' + money(voucherDisc);
    if (currentPayMethod === 'bank') updateQR(grand);
}

function selectPayMethod(method) {
    currentPayMethod = method;
    if (typeof stopQRPolling === 'function') stopQRPolling();
    document.getElementById('btnCash').classList.toggle('active', method==='cash');
    document.getElementById('btnBank').classList.toggle('active', method==='bank');
    document.getElementById('cashSection').style.display = method==='cash' ? '' : 'none';
    document.getElementById('bankSection').classList.toggle('show', method==='bank');
    document.getElementById('thanhToanPost').value = method==='cash' ? 'Tiền mặt' : 'Chuyển khoản';
    if (method==='bank') { calculateTotals(); }
}

var bankInfo = {
    VCB:   {name:'Vietcombank', account:'1234567890', owner:'DUNA MEN WEAR', guide:'STK: <b>1234567890</b><br>Chủ TK: <b>DUNA MEN WEAR</b><br>Ngân hàng: <b>Vietcombank</b><br>Nội dung: <b>DUNA - Tên KH</b>'},
    TCB:   {name:'Techcombank', account:'19031234567890', owner:'DUNA MEN WEAR', guide:'STK: <b>19031234567890</b><br>Chủ TK: <b>DUNA MEN WEAR</b><br>Ngân hàng: <b>Techcombank</b><br>Nội dung: <b>DUNA - Tên KH</b>'},
    MB:    {name:'MB Bank', account:'0987654321001', owner:'DUNA MEN WEAR', guide:'STK: <b>0987654321001</b><br>Chủ TK: <b>DUNA MEN WEAR</b><br>Ngân hàng: <b>MB Bank</b><br>Nội dung: <b>DUNA - Tên KH</b>'},
    BIDV:  {name:'BIDV', account:'31410001234567', owner:'DUNA MEN WEAR', guide:'STK: <b>31410001234567</b><br>Chủ TK: <b>DUNA MEN WEAR</b><br>Ngân hàng: <b>BIDV</b><br>Nội dung: <b>DUNA - Tên KH</b>'},
    VTB:   {name:'VietinBank', account:'102000123456789', owner:'DUNA MEN WEAR', guide:'STK: <b>102000123456789</b><br>Chủ TK: <b>DUNA MEN WEAR</b><br>Ngân hàng: <b>VietinBank</b><br>Nội dung: <b>DUNA - Tên KH</b>'},
    ACB:   {name:'ACB', account:'333999888777', owner:'DUNA MEN WEAR', guide:'STK: <b>333999888777</b><br>Chủ TK: <b>DUNA MEN WEAR</b><br>Ngân hàng: <b>ACB</b><br>Nội dung: <b>DUNA - Tên KH</b>'},
    MOMO:  {name:'MoMo', account:'0988888888', owner:'DUNA MEN WEAR', guide:'Số điện thoại MoMo: <b>0988.888.888</b><br>Tên TK: <b>DUNA MEN WEAR</b><br>Nội dung: <b>DUNA - Tên KH</b>'},
    ZALOPAY:{name:'ZaloPay', account:'0988888888', owner:'DUNA MEN WEAR', guide:'Số điện thoại ZaloPay: <b>0988.888.888</b><br>Tên TK: <b>DUNA MEN WEAR</b><br>Nội dung: <b>DUNA - Tên KH</b>'}
};

function selectBank(el, code, name) {
    selectedBank = code;
    document.querySelectorAll('.qr-bank-btn').forEach(function(b){b.classList.remove('active')});
    el.classList.add('active');
    var info = bankInfo[code];
    document.getElementById('bankGuide').innerHTML = info ? info.guide : '';
    var subtotal = cart.reduce(function(t,i){return t+i.gia*i.qty},0);
    var disc = Math.round(subtotal * currentDiscountPct / 100);
    var afterDisc = subtotal - disc;
    var vat = Math.round(subtotal * VAT_PCT / 100);
    var grand = afterDisc + vat;
    updateQR(grand);
}

function updateQR(amount) {
    if (typeof stopQRPolling === 'function') stopQRPolling();
    if (!amount || amount <= 0 || !cart.length) {
        document.getElementById('qrCodeImg').style.display='none';
        document.getElementById('qrPlaceholder').style.display='block';
        document.getElementById('qrPlaceholder').innerHTML = "Vui lòng chọn sản phẩm vào đơn để tạo mã QR.";
        var stEl = document.getElementById('qrStatusText');
        if (stEl) stEl.textContent = "Chờ thêm sản phẩm...";
        return;
    }
    var info = bankInfo[selectedBank];
    if (!info) return;
    
    document.getElementById('qrCodeImg').style.display='none';
    document.getElementById('qrPlaceholder').style.display='block';
    document.getElementById('qrPlaceholder').innerHTML = "Đang tạo mã QR xác nhận...";
    var stEl = document.getElementById('qrStatusText');
    if (stEl) stEl.textContent = "Chờ khách quét QR & xác nhận trên điện thoại...";

    var custName = typeof selectedCustomer !== 'undefined' && selectedCustomer ? (selectedCustomer.hoTen || "Khách hàng") : "Khách vãng lai";
    var bankName = info.name || selectedBank;
    
    fetch("${pageContext.request.contextPath}/QRConfirmServlet?action=init&amount=" + amount + "&bank=" + encodeURIComponent(bankName) + "&customer=" + encodeURIComponent(custName))
        .then(function(r) { return r.json(); })
        .then(function(data) {
            if (data && data.token && data.url) {
                var img = document.getElementById('qrCodeImg');
                img.onload = function(){ document.getElementById('qrPlaceholder').style.display='none'; img.style.display='block'; };
                img.onerror = function(){ document.getElementById('qrPlaceholder').innerHTML='Lỗi hiển thị ảnh QR.'; };
                img.src = "https://api.qrserver.com/v1/create-qr-code/?size=240x240&data=" + encodeURIComponent(data.url);
                
                var guideHtml = info.guide;
                if (data.lanIP) {
                    guideHtml += '<br><span style="color:var(--color-success);font-weight:700">Mạng nội bộ IP: ' + data.lanIP + '</span>';
                }
                document.getElementById('bankGuide').innerHTML = guideHtml;
                
                if (typeof startQRPolling === 'function') startQRPolling(data.token);
            }
        })
        .catch(function(e) {
            console.log("Lỗi tạo QR:", e);
        });
}

function submitInvoice(type) {
    calculateTotals();
    if (!cart.length) { alert('Giỏ hàng trống. Vui lòng chọn sản phẩm.'); return; }

    if (!navigator.onLine) {
        var offOrders = JSON.parse(localStorage.getItem('duna_offline_orders') || '[]');
        var offCode = 'OFF-' + (Date.now() % 1000000);
        var subtotal = cart.reduce(function(t,i){return t+i.gia*i.qty},0);
        var disc = Math.round(subtotal * currentDiscountPct / 100);
        var vat = Math.round(subtotal * VAT_PCT / 100);
        var grand = subtotal - disc + vat;
        var offOrder = {
            action: 'create',
            invoiceType: type,
            idKH: document.getElementById('idKHPost').value || 0,
            holdId: '0',
            tenKH_input: (document.getElementById('khachHangSearchInput') ? document.getElementById('khachHangSearchInput').value : 'Khách vãng lai') || 'Khách vãng lai',
            sdtKH_input: (document.getElementById('sdtKH') ? document.getElementById('sdtKH').value : '') || '',
            thanhToan: currentPayMethod === 'cash' ? 'Tiền mặt' : 'Chuyển khoản',
            customerPaid: grand,
            orderNote: 'Đơn lưu ngoại tuyến (Offline POS) #' + offCode
        };
        cart.forEach(function(item, idx) {
            offOrder['idSP[' + idx + ']'] = item.id;
            offOrder['qty[' + idx + ']'] = item.qty;
            offOrder['mauSac[' + idx + ']'] = item.mauSac || '';
            offOrder['trongLuong[' + idx + ']'] = item.trongLuong || '';
            offOrder['donGia[' + idx + ']'] = item.gia;
        });
        offOrders.push(offOrder);
        localStorage.setItem('duna_offline_orders', JSON.stringify(offOrders));
        alert('⚠️ ĐANG MẤT MẠNG INTERNET:\n\nHóa đơn đã được lưu tạm an toàn vào bộ nhớ ngoại tuyến (Mã: ' + offCode + ').\n\nHệ thống sẽ tự động đồng bộ lên máy chủ DUNA ngay khi có mạng trở lại!');
        cart = [];
        renderCart();
        initOfflinePOS();
        return;
    }

    var subtotal = cart.reduce(function(t,i){return t+i.gia*i.qty},0);
    var disc = Math.round(subtotal * currentDiscountPct / 100);
    var voucherDisc = appliedVoucherAmount > 0 ? Math.min(appliedVoucherAmount, subtotal - disc) : 0;
    var afterDisc = subtotal - disc - voucherDisc;
    var vat = Math.round(subtotal * VAT_PCT / 100);
    var grand = afterDisc + vat;

    if (type === 'paid' && currentPayMethod === 'cash') {
        var paidStr = (document.getElementById('customerPaidInput').value || '').replace(/[^0-9]/g, '');
        var paid = parseFloat(paidStr) || 0;
        if (paid < grand) { alert('Số tiền khách đưa (' + money(paid) + ') chưa đủ để thanh toán (' + money(grand) + ')!'); return; }
        document.getElementById('orderNotePost').value = 'Bán hàng tại quầy';
    } else if (type === 'paid' && currentPayMethod === 'bank') {
        alert('Vui lòng yêu cầu khách hàng quét mã QR trên màn hình và xác nhận trên điện thoại.\n\nHệ thống sẽ tự động hoàn tất hóa đơn sau khi khách xác nhận.');
        return;
    } else {
        var paidHoldStr = (document.getElementById('customerPaidInput').value || '').replace(/[^0-9]/g, '');
        var paidHold = parseFloat(paidHoldStr) || 0;
        if (paidHold > 0) {
            alert('Khi tạo hóa đơn chờ thì không được nhận tiền ở phần "Khách đưa" vì đơn chưa thanh toán!');
            return;
        }
        if (appliedVoucherCode && appliedVoucherCode.trim() !== '') {
            alert('Không thể áp dụng voucher vào các đơn hàng tạm giữ!');
            cancelVoucher();
            return;
        }
        document.getElementById('customerPaidPost').value = 0;
        document.getElementById('orderNotePost').value = 'Hóa đơn chờ - Bán hàng tại quầy';
    }

    document.getElementById('voucherCodePost').value = appliedVoucherCode || '';
    document.getElementById('voucherAmountPost').value = voucherDisc || 0;

    document.getElementById('invoiceTypePost').value = type;
    var confirmMsg = type === 'hold'
        ? 'Tạo hóa đơn chờ ' + money(grand) + '? (Trạng thái Chưa thanh toán)'
        : 'Xác nhận thanh toán ' + money(grand) + '?';
    if (!confirm(confirmMsg)) return;
    document.getElementById('invoiceForm').submit();
}

function printInvoice(id) {
    fetch('${pageContext.request.contextPath}/OrderServlet?ajaxDetail=1&id=' + id)
        .then(function(r){return r.json()})
        .then(function(data){
            document.getElementById('prMaDH').textContent = 'Mã đơn: ' + (data.maDH||'');
            document.getElementById('prNgayTao').textContent = 'Ngày: ' + (data.ngayTao||'');
            document.getElementById('prTenKH').textContent = data.tenKH||'Khách lẻ';
            document.getElementById('prSdtKH').textContent = data.sdtKH||'-';
            document.getElementById('prDiaChiKH').textContent = data.diaChiKH||'-';
            document.getElementById('prThanhToan').textContent = data.thanhToan||'Tiền mặt';
            document.getElementById('prTrangThai').textContent = data.trangThai||'';
            if (document.getElementById('prTenNV')) document.getElementById('prTenNV').textContent = data.tenNV||'Admin';

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
                    } else if (document.getElementById('prVatPctLabel')) {
                        document.getElementById('prVatPctLabel').textContent = VAT_PCT;
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
                var calculatedVAT = Math.round((data.tongTien || 0) * VAT_PCT / (100 + VAT_PCT));
                vatVal = Number(calculatedVAT).toLocaleString('vi-VN') + ' VNĐ';
                if (document.getElementById('prVatPctLabel')) document.getElementById('prVatPctLabel').textContent = VAT_PCT;
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

            document.getElementById('prTongTien').textContent = money(data.tongTien||0);
            document.getElementById('prBody').innerHTML = (data.chiTiet||[]).map(function(item,i){
                var tenSP = item.tenSP;
                var bienThe = '-';
                var match = tenSP.match(/^(.*?)\s*\(([^)]+)\)$/);
                if (match) {
                    tenSP = match[1];
                    bienThe = match[2];
                }
                return '<tr><td style="text-align:center;">'+(i+1)+'</td><td>'+tenSP+'</td><td style="white-space:nowrap;">'+bienThe+'</td><td style="text-align:center;" class="font-mono">'+item.soLuong+'</td><td class="money font-mono">'+money(item.donGia)+'</td><td class="money font-mono">'+money(item.thanhTien)+'</td></tr>';
            }).join('');
            openModal('modalPrint');
        }).catch(function(){alert('Không lấy được dữ liệu hóa đơn.');});
}

function submitAddKH(event) {
    event.preventDefault();
    var name = document.getElementById('khName').value.trim();
    var sdt = document.getElementById('khSdt').value.trim();
    var email = document.getElementById('khEmail').value.trim();
    if (!name || !sdt) { alert('Vui lòng nhập họ tên và SĐT.'); return false; }
    fetch('${pageContext.request.contextPath}/OrderServlet?ajaxAddKH=1&hoTen='+encodeURIComponent(name)+'&sdt='+encodeURIComponent(sdt)+'&email='+encodeURIComponent(email))
        .then(function(r){return r.json()})
        .then(function(data){
            if (!data.success) { alert(data.message||'Lỗi.'); return; }
            var newKH = {id:data.id, ten:data.ten, sdt:data.sdt, hang:'Thường', discountPct:0};
            if (!data.existed) {
                ALL_KH.push(newKH);
                var sel = document.getElementById('customerSelectBox');
                if (sel) {
                    var opt = document.createElement('option');
                    opt.value = newKH.id;
                    opt.text = newKH.ten + ' (' + newKH.sdt + ')';
                    opt.setAttribute('data-ten', newKH.ten);
                    opt.setAttribute('data-sdt', newKH.sdt);
                    opt.setAttribute('data-hang', 'Thường');
                    opt.setAttribute('data-discount', 0);
                    sel.appendChild(opt);
                }
            }
            selectCustomer(newKH);
            closeModal('modalAddKH');
            document.getElementById('formAddKH').reset();
        }).catch(function(){alert('Lỗi kết nối.');});
    return false;
}

function openModal(id) {
    var el = document.getElementById(id);
    if (el) { el.classList.add('open'); el.style.display = 'flex'; }
}
function closeModal(id) {
    var el = document.getElementById(id);
    if (el) { el.classList.remove('open'); el.style.display = 'none'; }
}
document.querySelectorAll('.modal-bg, .modal').forEach(function(m){
    m.addEventListener('click', function(e){ if(e.target===m) closeModal(m.id); });
});

function cancelVoucher() {
    appliedVoucherCode = '';
    appliedVoucherAmount = 0;
    appliedVoucherMinOrder = 0;
    if (document.getElementById('voucherInput')) document.getElementById('voucherInput').value = '';
    var resEl = document.getElementById('voucherResult');
    if (resEl) { resEl.className = 'voucher-result'; resEl.textContent = ''; resEl.style.display = 'none'; }
    if (document.getElementById('btnCancelVoucher')) document.getElementById('btnCancelVoucher').style.display = 'none';
    calculateTotals();
}

function applyVoucher() {
    if (!cart.length) {
        alert('Vui lòng thêm sản phẩm vào đơn hàng trước khi áp dụng voucher!');
        return;
    }
    var resultEl = document.getElementById('voucherResult');
    var code = (document.getElementById('voucherInput').value || '').trim().toUpperCase();
    if (!code) {
        resultEl.className = 'alert alert-danger';
        resultEl.textContent = 'Vui lòng nhập mã voucher vào ô.';
        return;
    }

    if (!cart.length || cart.reduce(function(t,i){return t+i.gia*i.qty},0) === 0) {
        resultEl.className = 'alert alert-warning';
        resultEl.textContent = 'Vui lòng thêm sản phẩm vào đơn trước khi áp dụng voucher.';
        resultEl.style.display = 'block';
        return;
    }

    if (appliedVoucherCode && appliedVoucherCode.toUpperCase() === code) {
        cancelVoucher();
        return;
    }

    var finalCode = code;
    resultEl.className = 'alert alert-info';
    resultEl.textContent = 'Đang kiểm tra tính hợp lệ của mã...';
    resultEl.style.display = 'block';

    var currentSubtotal = Math.round(cart.reduce(function(t,i){return t+i.gia*i.qty},0));
    var currentKhId = (typeof selectedKH !== 'undefined' && selectedKH && selectedKH.id) ? selectedKH.id : 0;
    fetch('${pageContext.request.contextPath}/OrderServlet?ajaxCheckVoucher=1&code=' + encodeURIComponent(finalCode) + '&tongTien=' + currentSubtotal + '&khId=' + currentKhId)
        .then(function(r){return r.json();})
        .then(function(data){
            if (data.valid) {
                appliedVoucherCode = finalCode;
                appliedVoucherAmount = data.amount || 0;
                appliedVoucherMinOrder = data.minOrder || 0;
                resultEl.className = 'alert alert-success';
                resultEl.textContent = (data.message || ('Áp dụng thành công mã ' + finalCode + ' (Giảm ' + money(appliedVoucherAmount) + ')'));
                if (document.getElementById('btnCancelVoucher')) document.getElementById('btnCancelVoucher').style.display = 'inline-block';
                calculateTotals();
            } else {
                appliedVoucherCode = '';
                appliedVoucherAmount = 0;
                appliedVoucherMinOrder = 0;
                resultEl.className = 'alert alert-danger';
                resultEl.textContent = data.message;
                if (document.getElementById('btnCancelVoucher')) document.getElementById('btnCancelVoucher').style.display = 'none';
                calculateTotals();
            }
        })
        .catch(function(){
            resultEl.className = 'alert alert-danger';
            resultEl.textContent = 'Lỗi kết nối. Vui lòng thử lại.';
        });
}

var currentQRToken = '';
var qrPollingInterval = null;

function startQRPolling(token) {
    stopQRPolling();
    currentQRToken = token;
    qrPollingInterval = setInterval(function() {
        if (!currentQRToken) return;
        fetch("${pageContext.request.contextPath}/QRConfirmServlet?action=check&token=" + currentQRToken)
            .then(function(r) { return r.json(); })
            .then(function(data) {
                if (data && data.status === 'CONFIRMED') {
                    stopQRPolling();
                    var stEl = document.getElementById('qrStatusText');
                    if (stEl) stEl.textContent = "Khách hàng đã xác nhận từ điện thoại. Đang tự động hoàn tất...";
                    var boxEl = document.getElementById('qrStatusContainer');
                    if (boxEl) { boxEl.style.background = "var(--color-bg-subtle)"; boxEl.style.borderColor = "var(--color-success)"; }
                    
                    setTimeout(function() {
                        submitInvoiceBank();
                    }, 800);
                }
            }).catch(function(e) { console.log('QR Check Error:', e); });
    }, 1500);
}

function stopQRPolling() {
    if (qrPollingInterval) { clearInterval(qrPollingInterval); qrPollingInterval = null; }
    currentQRToken = '';
}

function submitInvoiceBank() {
    if (!cart.length) { alert('Giỏ hàng trống!'); return; }
    var subtotal = cart.reduce(function(t,i){return t+i.gia*i.qty},0);
    var disc = Math.round(subtotal * currentDiscountPct / 100);
    var voucherDisc = appliedVoucherAmount > 0 ? Math.min(appliedVoucherAmount, subtotal - disc) : 0;
    var afterDisc = subtotal - disc - voucherDisc;
    var vat = Math.round(subtotal * VAT_PCT / 100);
    var grand = afterDisc + vat;
    document.getElementById('voucherCodePost').value = appliedVoucherCode || '';
    document.getElementById('voucherAmountPost').value = voucherDisc || 0;
    document.getElementById('discountAmountPost').value = disc;
    document.getElementById('vatAmountPost').value = vat;
    document.getElementById('customerPaidPost').value = grand;
    document.getElementById('thanhToanPost').value = 'Chuyển khoản';
    document.getElementById('orderNotePost').value = 'Bán hàng tại quầy';
    document.getElementById('invoiceTypePost').value = 'paid';
    document.getElementById('invoiceForm').submit();
}

selectPayMethod('cash');
renderCart();

<c:if test="${not empty holdInfo}">
(function() {
    var holdItems = [
        <c:forEach items="${holdInfo.items}" var="ci" varStatus="s">
        (function(){
            var fullName = '${fn:escapeXml(ci.tenSP)}';
            var baseName = fullName;
            var mau = '';
            var tl = '';
            var match = fullName.match(/^(.*?)\s*\(([^)]+)\)$/);
            if (match) {
                baseName = match[1];
                var vParts = match[2].split(' - ');
                if (vParts.length == 2) { mau = vParts[0].trim(); tl = vParts[1].trim(); }
                else if (vParts.length == 1) { mau = vParts[0].trim(); }
            }
            return {id:'${ci.idSP}',maSP:'SP${ci.idSP}',tenSP:baseName,gia:${ci.donGia},tonKho:${ci.tonKho + ci.soLuong},qty:${ci.soLuong},mauSac:mau,trongLuong:tl};
        })()${!s.last?',':''}
        </c:forEach>
    ];
    holdItems.forEach(function(item){ cart.push(item); });
    renderCart();
    <c:if test="${holdInfo.idKH > 0}">
    var hkh = ALL_KH.find(function(k){return k.id==${holdInfo.idKH}});
    if (hkh) { selectCustomer(hkh); }
    </c:if>
    document.getElementById('holdIdPost').value = '${holdId}';
    
    var holdGhiChu = '${fn:escapeXml(holdInfo.ghiChu)}';
    var rawNotes = holdGhiChu.split('|');
    rawNotes.forEach(function(noteStr) {
        var pTrim = noteStr.trim();
        if (pTrim.toLowerCase().indexOf('voucher') > -1 && pTrim.indexOf(':') > -1) {
            var vCodeMatch = pTrim.match(/\(([^)]+)\)/);
            if (vCodeMatch) {
                var input = document.getElementById('voucherInput');
                if (input) {
                    input.value = vCodeMatch[1];
                    setTimeout(function() { applyVoucher(); }, 500);
                }
            }
        }
    });
})();
</c:if>
</script>

<c:if test="${param.success == '1' && not empty param.id}">
<script>window.addEventListener('DOMContentLoaded',function(){printInvoice('${param.id}');});</script>
</c:if>

<script>
var html5QrCode = null;
var scannerActive = false;
var lastScannedBarcode = '';
var scanDebounceTimer = null;
var scannerContextPath = '${pageContext.request.contextPath}';

function openBarcodeScanner() {
    document.getElementById('scannerModal').classList.add('open');
    document.getElementById('scanStatus').textContent = 'Đang khởi động camera...';
    document.getElementById('scanStatus').className = 'scan-status';
    document.getElementById('scanResultCard').style.display = 'none';
    startScanner();
}

function closeBarcodeScanner() {
    document.getElementById('scannerModal').classList.remove('open');
    stopScanner();
}

function startScanner() {
    if (scannerActive) return;
    if (html5QrCode) {
        try { html5QrCode.stop(); } catch(e) {}
        html5QrCode = null;
    }
    html5QrCode = new Html5Qrcode("reader");
    Html5Qrcode.getCameras().then(function(cameras) {
        if (!cameras || cameras.length === 0) {
            setScanStatus('error', 'Không tìm thấy camera khả dụng.');
            return;
        }
        var camId = cameras[cameras.length - 1].id;
        var config = {
            fps: 30,
            qrbox: function(viewfinderWidth, viewfinderHeight) {
                return {
                    width: Math.floor(viewfinderWidth * 0.96),
                    height: Math.floor(viewfinderHeight * 0.85)
                };
            },
            aspectRatio: 1.3333,
            experimentalFeatures: {
                useBarCodeDetectorIfSupported: true
            },
            videoConstraints: {
                width: { min: 640, ideal: 1280 },
                height: { min: 480, ideal: 720 }
            },
            formatsToSupport: [
                Html5QrcodeSupportedFormats.CODE_128,
                Html5QrcodeSupportedFormats.CODE_39,
                Html5QrcodeSupportedFormats.CODE_93,
                Html5QrcodeSupportedFormats.EAN_13,
                Html5QrcodeSupportedFormats.EAN_8,
                Html5QrcodeSupportedFormats.UPC_A,
                Html5QrcodeSupportedFormats.UPC_E,
                Html5QrcodeSupportedFormats.QR_CODE
            ]
        };
        html5QrCode.start(camId, config, onBarcodeDetected, null).then(function() {
            scannerActive = true;
            setScanStatus('', 'Camera sẵn sàng — Hướng mã vạch vào khung quét');
        }).catch(function(err) {
            setScanStatus('error', 'Không thể truy cập camera: ' + err);
        });
    }).catch(function(err) {
        setScanStatus('error', 'Lỗi thiết bị camera: ' + err);
    });
}

function stopScanner() {
    scannerActive = false;
    if (html5QrCode) {
        try { html5QrCode.stop().catch(function(){}); } catch(e) {}
        html5QrCode = null;
    }
    clearTimeout(scanDebounceTimer);
    lastScannedBarcode = '';
}

function onBarcodeDetected(decodedText) {
    var barcode = decodedText.trim().toUpperCase();
    if (barcode === lastScannedBarcode) return;
    lastScannedBarcode = barcode;

    setScanStatus('', 'Đang tra cứu mã: <b>' + barcode + '</b>...');

    fetch(scannerContextPath + '/OrderServlet?ajaxScanBarcode=1&barcode=' + encodeURIComponent(barcode))
        .then(function(r) { return r.json(); })
        .then(function(data) {
            if (data.found) {
                setScanStatus('success', 'Tìm thấy sản phẩm. Đang đưa vào giỏ hàng...');
                showScanResult(data);
                addToCartByBarcode(data);
                playBeep();
                showFeedbackToast('Đã thêm: ' + data.tenSP);
                clearTimeout(scanDebounceTimer);
                scanDebounceTimer = setTimeout(function() {
                    lastScannedBarcode = '';
                    setScanStatus('', 'Camera sẵn sàng — Hướng mã vạch vào khung quét');
                }, 2500);
            } else {
                setScanStatus('error', data.message || 'Không tìm thấy sản phẩm');
                clearTimeout(scanDebounceTimer);
                scanDebounceTimer = setTimeout(function() {
                    lastScannedBarcode = '';
                    setScanStatus('', 'Camera sẵn sàng — Hướng mã vạch vào khung quét');
                }, 2000);
            }
        })
        .catch(function() {
            setScanStatus('error', 'Lỗi kết nối. Vui lòng thử lại.');
            clearTimeout(scanDebounceTimer);
            scanDebounceTimer = setTimeout(function() { lastScannedBarcode = ''; }, 2000);
        });
}

function addToCartByBarcode(product) {
    var existIdx = cart.findIndex(function(c) {
        return c.id == product.id && c.mauSac == product.mauSac && c.trongLuong == product.trongLuong;
    });
    if (existIdx >= 0) {
        var maxStock = product.soLuong || 1;
        if (cart[existIdx].qty < maxStock) {
            cart[existIdx].qty++;
        }
    } else {
        cart.push({
            id: product.id,
            idBT: product.idBT,
            maSP: product.maSP || ('SP' + product.id),
            tenSP: product.tenSP + (product.mauSac ? ' (' + product.mauSac + (product.trongLuong ? ', ' + product.trongLuong : '') + ')' : ''),
            gia: product.giaBan,
            tonKho: product.soLuong,
            qty: 1,
            mauSac: product.mauSac,
            trongLuong: product.trongLuong
        });
    }
    renderCart();
}

function showScanResult(product) {
    document.getElementById('scanResultName').textContent = product.tenSP;
    document.getElementById('scanResultPrice').textContent = money(product.giaBan);
    document.getElementById('scanResultColor').textContent = 'Màu: ' + (product.mauSac || '—');
    document.getElementById('scanResultWeight').textContent = 'Size: ' + (product.trongLuong || '—');
    document.getElementById('scanResultStock').textContent = 'Tồn: ' + product.soLuong;
    document.getElementById('scanResultCard').style.display = 'block';
}

function setScanStatus(type, html) {
    var el = document.getElementById('scanStatus');
    el.innerHTML = html;
    el.className = 'scan-status' + (type ? ' ' + type : '');
}

function showFeedbackToast(msg) {
    var el = document.getElementById('scanFeedback');
    el.textContent = msg;
    el.classList.add('show');
    setTimeout(function() { el.classList.remove('show'); }, 2500);
}

function playBeep() {
    try {
        var ctx = new (window.AudioContext || window.webkitAudioContext)();
        var o = ctx.createOscillator();
        var g = ctx.createGain();
        o.connect(g); g.connect(ctx.destination);
        o.frequency.value = 880;
        o.type = 'sine';
        g.gain.setValueAtTime(0.4, ctx.currentTime);
        g.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + 0.25);
        o.start(ctx.currentTime);
        o.stop(ctx.currentTime + 0.25);
    } catch(e) {}
}

document.getElementById('scannerModal').addEventListener('click', function(e) {
    if (e.target === this) closeBarcodeScanner();
});

function onColorChange(spId) {
    var jsonEl = document.getElementById('vjson_' + spId);
    var variants = [];
    if (jsonEl) {
        try { variants = JSON.parse(jsonEl.textContent || '[]'); } catch(e) {}
    }
    if (variants.length === 0) {
        updateCardBarcode(spId);
        return;
    }
    var mauEl = document.getElementById('mau_' + spId);
    var tlEl = document.getElementById('tl_' + spId);
    if (!mauEl || !tlEl) return;
    
    var selMau = mauEl.value.trim();
    var selTl = tlEl.value.trim();
    
    var validPair = variants.find(function(v) { return v.mau === selMau && v.tl === selTl; });
    if (!validPair) {
        var matchByColor = variants.find(function(v) { return v.mau === selMau; });
        if (matchByColor && matchByColor.tl) {
            tlEl.value = matchByColor.tl;
        }
    }
    updateCardBarcode(spId);
}

function onWeightChange(spId) {
    var jsonEl = document.getElementById('vjson_' + spId);
    var variants = [];
    if (jsonEl) {
        try { variants = JSON.parse(jsonEl.textContent || '[]'); } catch(e) {}
    }
    if (variants.length === 0) {
        updateCardBarcode(spId);
        return;
    }
    var mauEl = document.getElementById('mau_' + spId);
    var tlEl = document.getElementById('tl_' + spId);
    if (!mauEl || !tlEl) return;
    
    var selTl = tlEl.value.trim();
    var selMau = mauEl.value.trim();
    
    var validPair = variants.find(function(v) { return v.mau === selMau && v.tl === selTl; });
    if (!validPair) {
        var matchByWeight = variants.find(function(v) { return v.tl === selTl; });
        if (matchByWeight && matchByWeight.mau) {
            mauEl.value = matchByWeight.mau;
        }
    }
    updateCardBarcode(spId);
}

function updateCardBarcode(spId) {
    var jsonEl = document.getElementById('vjson_' + spId);
    var variants = [];
    if (jsonEl) {
        try { variants = JSON.parse(jsonEl.textContent || '[]'); } catch(e) {}
    }
    
    var mauEl = document.getElementById('mau_' + spId);
    var tlEl = document.getElementById('tl_' + spId);
    var selMau = mauEl ? mauEl.value.trim() : '';
    var selTl = tlEl ? tlEl.value.trim() : '';
    
    var found = variants.find(function(v) { return v.mau === selMau && v.tl === selTl; });
    if (!found && variants.length > 0) {
        found = variants.find(function(v) { return v.mau === selMau; }) || variants[0];
    }
    
    if (found) {
        var priceEl = document.getElementById('price_display_' + spId);
        if (priceEl && found.gia !== undefined) {
            priceEl.textContent = Number(found.gia).toLocaleString('vi-VN') + ' đ';
        }
        var stockEl = document.getElementById('stock_display_' + spId);
        if (stockEl && found.sl !== undefined) {
            stockEl.textContent = 'Tồn: ' + found.sl;
        }
        var qtyInput = document.getElementById('qty_' + spId);
        if (qtyInput && found.sl !== undefined) {
            qtyInput.setAttribute('max', found.sl);
        }
        var btn = document.querySelector('button[data-id="' + spId + '"]');
        if (btn) {
            if (found.gia !== undefined) btn.setAttribute('data-price', found.gia);
            if (found.sl !== undefined) btn.setAttribute('data-stock', found.sl);
            if (found.idBT !== undefined) btn.setAttribute('data-idbt', found.idBT);
            if (found.barcode !== undefined) btn.setAttribute('data-barcode', found.barcode);
        }
    }
    
    var bcSvg = '#bc_svg_' + spId;
    var bcTxtEl = document.getElementById('bc_txt_' + spId);
    var targetBc = '';
    if (found && found.barcode) {
        targetBc = found.barcode;
    } else {
        var btn2 = document.querySelector('button[data-id="' + spId + '"]');
        targetBc = btn2 ? (btn2.getAttribute('data-masp') || ('SP' + spId)).toUpperCase() : ('SP' + spId);
    }
    
    if (bcTxtEl) bcTxtEl.textContent = targetBc;
    try {
        if (window.JsBarcode && document.querySelector(bcSvg)) {
            JsBarcode(bcSvg, targetBc, {
                format: 'CODE128',
                width: 1.5,
                height: 32,
                displayValue: false,
                margin: 2,
                background: "transparent",
                lineColor: document.documentElement.classList.contains('dark-mode') ? "#f5f5f5" : "#0f172a"
            });
        }
    } catch(e) {}
    try {
        var qrBox = document.getElementById('qr_box_' + spId);
        if (window.qrcode && qrBox) {
            var qr = qrcode(0, 'M');
            qr.addData(targetBc);
            qr.make();
            qrBox.innerHTML = qr.createSvgTag(2.5, 0);
        }
    } catch(e) {}
}

window.addEventListener('DOMContentLoaded', function() {
    initOfflinePOS();
    document.querySelectorAll('[id^="vjson_"]').forEach(function(el) {
        var id = el.id.replace('vjson_', '');
        if (id) onColorChange(id);
    });
});

function openSizeConsultModal() {
    openModal('modalSizeConsult');
}

function calcSizeConsult() {
    var h = parseFloat(document.getElementById('scHeight').value) || 0;
    var w = parseFloat(document.getElementById('scWeight').value) || 0;
    var shirtEl = document.getElementById('scShirtSize');
    var pantsEl = document.getElementById('scPantsSize');

    if (w <= 0 && h <= 0) {
        shirtEl.textContent = '--';
        pantsEl.textContent = '--';
        return;
    }

    var shirtSize = 'L';
    var pantsSize = '31';

    if (w > 0 && w < 58 || (h > 0 && h < 165 && w <= 60)) {
        shirtSize = 'S';
        pantsSize = '29';
    } else if (w >= 58 && w <= 65 || (h >= 165 && h <= 170 && w <= 66)) {
        shirtSize = 'M';
        pantsSize = '30';
    } else if (w > 65 && w <= 72 || (h > 170 && h <= 175 && w <= 73)) {
        shirtSize = 'L';
        pantsSize = '31 - 32';
    } else if (w > 72 && w <= 80 || (h > 175 && h <= 180 && w <= 82)) {
        shirtSize = 'XL';
        pantsSize = '33';
    } else if (w > 80 || h > 180) {
        shirtSize = 'XXL';
        pantsSize = '34';
    }

    shirtEl.textContent = 'Size ' + shirtSize;
    pantsEl.textContent = 'Size ' + pantsSize;
}
</script>
</body>
</html>
