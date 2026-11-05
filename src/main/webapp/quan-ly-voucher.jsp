<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Voucher & Khuyến mãi — DUNA Men's Wear</title>
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
            <a href="${pageContext.request.contextPath}/ProductServlet">Quản lý sản phẩm</a>
            <a href="${pageContext.request.contextPath}/EmployeeManagementServlet">Quản lý nhân sự</a>
            <a href="${pageContext.request.contextPath}/OrderServlet?view=report">Báo cáo thống kê</a>
            <a href="${pageContext.request.contextPath}/CustomerServlet">Quản lý khách hàng</a>
            <a href="${pageContext.request.contextPath}/OrderServlet?view=sell">Bán hàng</a>
            <a href="${pageContext.request.contextPath}/OrderServlet">Quản lý hóa đơn</a>
            <a href="${pageContext.request.contextPath}/VoucherServlet" class="active">Quản lý Voucher</a>
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

    <main class="page-container">
        <!-- Thông báo thành công / lỗi -->
        <c:if test="${not empty param.success}">
            <div class="alert alert-success">
                <span>
                    <c:choose>
                        <c:when test="${param.success == 'added'}">Đã khởi tạo mã voucher mới thành công!</c:when>
                        <c:when test="${param.success == 'batch'}">Đã tự động phát hành hàng loạt lô voucher mới vào hệ thống!</c:when>
                        <c:otherwise>Cập nhật trạng thái voucher thành công!</c:otherwise>
                    </c:choose>
                </span>
                <button type="button" onclick="this.parentElement.style.display='none'" class="close-btn" style="position:static; margin-left:auto;">&times;</button>
            </div>
        </c:if>
        <c:if test="${not empty param.error or not empty error}">
            <div class="alert alert-danger">
                <span>Lỗi: ${not empty error ? error : (param.error == 'exist' ? 'Mã voucher đã tồn tại trong hệ thống!' : 'Vui lòng điền đầy đủ thông tin.')}</span>
                <button type="button" onclick="this.parentElement.style.display='none'" class="close-btn" style="position:static; margin-left:auto;">&times;</button>
            </div>
        </c:if>

        <!-- Header -->
        <div class="page-header">
            <div>
                <h1 class="page-title">Quản Lý Đợt Giảm Giá & Voucher DUNA</h1>
                <p class="page-subtitle">Thiết lập chương trình khuyến mãi theo chiết khấu % và trợ giá tiền mặt trực tiếp</p>
            </div>
            <div class="action-buttons">
                <button type="button" class="btn btn-primary" onclick="openModal('addModal')">Tạo Mã VIP Lẻ</button>
                <button type="button" class="btn btn-outline" onclick="openModal('batchModal')">Phát Hành Lô Mã</button>
                <button type="button" class="btn btn-outline" onclick="exportVouchersExcel()">Xuất Excel</button>
            </div>
        </div>

        <!-- 4 Thẻ Thống Kê Tổng Quan -->
        <div class="overview-grid">
            <div class="stat-card">
                <div class="stat-label">Tổng Phát Hành</div>
                <div class="stat-value">
                    <fmt:formatNumber value="${not empty stat_total ? stat_total : 0}" type="number"/> <span style="font-size:14px; font-weight:600; color:var(--muted)">mã</span>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-label">Sẵn Sàng (Chưa Dùng)</div>
                <div class="stat-value" style="color: var(--positive);">
                    <fmt:formatNumber value="${not empty stat_available ? stat_available : 0}" type="number"/> <span style="font-size:14px; font-weight:600; color:var(--muted)">mã</span>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-label">Đã Sử Dụng / Hết Lượt</div>
                <div class="stat-value" style="color: var(--warning);">
                    <fmt:formatNumber value="${not empty stat_used ? stat_used : 0}" type="number"/> <span style="font-size:14px; font-weight:600; color:var(--muted)">mã</span>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-label">Tổng Tiền Đã Trợ Giá</div>
                <div class="stat-value" style="color: var(--brand-accent);">
                    <fmt:formatNumber value="${not empty stat_discount ? stat_discount : 0}" type="number" pattern="#,##0"/> <span style="font-size:14px; font-weight:600; color:var(--muted)">VNĐ</span>
                </div>
            </div>
        </div>

        <!-- Thanh Toolbar: Bộ lọc & Hành động -->
        <div class="toolbar-section">
            <form id="voucherFilterForm" action="${pageContext.request.contextPath}/VoucherServlet" method="get" class="filter-form">
                <div class="search-box-wrap">
                    <input type="text" name="search" class="search-box" value="${search}" placeholder="Tìm mã voucher, tên chương trình, đơn hàng...">
                </div>

                <select name="giamGia" class="form-control" style="width:auto; min-width: 180px;" onchange="this.form.submit()">
                    <option value="ALL" ${giamGia == 'ALL' or empty giamGia ? 'selected' : ''}>— Tất cả mệnh giá —</option>
                    <optgroup label="Giảm theo số tiền">
                        <option value="100000" ${giamGia == '100000' ? 'selected' : ''}>Giảm 100.000 VNĐ</option>
                        <option value="200000" ${giamGia == '200000' ? 'selected' : ''}>Giảm 200.000 VNĐ</option>
                        <option value="300000" ${giamGia == '300000' ? 'selected' : ''}>Giảm 300.000 VNĐ</option>
                        <option value="400000" ${giamGia == '400000' ? 'selected' : ''}>Giảm 400.000 VNĐ</option>
                        <option value="500000" ${giamGia == '500000' ? 'selected' : ''}>Giảm 500.000 VNĐ</option>
                        <option value="600000" ${giamGia == '600000' ? 'selected' : ''}>Giảm 600.000 VNĐ</option>
                        <option value="700000" ${giamGia == '700000' ? 'selected' : ''}>Giảm 700.000 VNĐ</option>
                        <option value="800000" ${giamGia == '800000' ? 'selected' : ''}>Giảm 800.000 VNĐ</option>
                        <option value="900000" ${giamGia == '900000' ? 'selected' : ''}>Giảm 900.000 VNĐ</option>
                        <option value="1000000" ${giamGia == '1000000' ? 'selected' : ''}>Giảm 1.000.000 VNĐ</option>
                    </optgroup>
                    <optgroup label="Giảm theo %">
                        <option value="5" ${giamGia == '5' ? 'selected' : ''}>Giảm 5%</option>
                        <option value="10" ${giamGia == '10' ? 'selected' : ''}>Giảm 10%</option>
                        <option value="15" ${giamGia == '15' ? 'selected' : ''}>Giảm 15%</option>
                        <option value="20" ${giamGia == '20' ? 'selected' : ''}>Giảm 20%</option>
                        <option value="25" ${giamGia == '25' ? 'selected' : ''}>Giảm 25%</option>
                        <option value="30" ${giamGia == '30' ? 'selected' : ''}>Giảm 30%</option>
                        <option value="35" ${giamGia == '35' ? 'selected' : ''}>Giảm 35%</option>
                        <option value="40" ${giamGia == '40' ? 'selected' : ''}>Giảm 40%</option>
                        <option value="45" ${giamGia == '45' ? 'selected' : ''}>Giảm 45%</option>
                        <option value="50" ${giamGia == '50' ? 'selected' : ''}>Giảm 50%</option>
                    </optgroup>
                </select>

                <select name="trangThai" class="form-control" style="width:auto; min-width: 170px;" onchange="this.form.submit()">
                    <option value="" ${empty trangThai ? 'selected' : ''}>— Tất cả trạng thái —</option>
                    <option value="available" ${trangThai == 'available' ? 'selected' : ''}>Sẵn sàng (Chưa dùng)</option>
                    <option value="used" ${trangThai == 'used' ? 'selected' : ''}>Đã sử dụng (Hết lượt)</option>
                    <option value="locked" ${trangThai == 'locked' ? 'selected' : ''}>Bị khóa / Tạm dừng</option>
                </select>

                <div class="action-buttons">
                    <button type="submit" class="btn btn-primary btn-sm">Lọc</button>
                    <c:if test="${not empty search or not empty giamGia or not empty trangThai}">
                        <a href="${pageContext.request.contextPath}/VoucherServlet" class="btn btn-outline btn-sm">Xóa lọc</a>
                    </c:if>
                </div>
            </form>
        </div>

        <!-- Bảng Danh Sách Voucher -->
        <div class="table-wrap">
            <table class="styled-table">
                <thead>
                    <tr>
                        <th style="width: 50px; text-align: center;">STT</th>
                        <th style="width: 14%;">Mã Voucher</th>
                        <th style="width: 22%;">Tên Chương Trình</th>
                        <th style="width: 14%;">Mệnh Giá Giảm</th>
                        <th style="width: 16%;">Điều Kiện Áp Dụng</th>
                        <th style="width: 8%; text-align: center;">Tối đa</th>
                        <th style="width: 8%; text-align: center;">Đã dùng</th>
                        <th style="width: 12%; text-align: center;">Đơn hàng</th>
                        <th style="width: 10%;">Trạng Thái</th>
                        <th style="width: 10%; text-align: center;">Thao Tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty vouchers}">
                            <tr>
                                <td colspan="10" style="text-align: center; padding: 36px; color: var(--muted);">
                                    Không tìm thấy voucher nào phù hợp bộ lọc!
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="v" items="${vouchers}" varStatus="loop">
                                <tr>
                                    <td class="text-center" style="color:var(--muted); font-weight: 600;">${loop.index + 1}</td>
                                    <td>
                                        <span class="code-pill">${v.MaVoucher}</span>
                                    </td>
                                    <td style="font-weight: 600;">${v.TenChuongTrinh}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty v.GiamGia and v.GiamGia <= 100}">
                                                <span class="badge badge-warning">
                                                    Giảm ${v.GiamGia}%
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="price text-right" style="font-weight: 700; color: var(--text-primary);">
                                                    <fmt:formatNumber value="${not empty v.GiamGia ? v.GiamGia : 0}" type="number" groupingUsed="true"/> đ
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="font-size: 13px; color: var(--muted);">
                                        Đơn từ <strong style="color:var(--text-primary); font-variant-numeric: tabular-nums;"><fmt:formatNumber value="${not empty v.DonToiThieu ? v.DonToiThieu : (v.GiamGia * 5)}" type="number" groupingUsed="true"/> đ</strong>
                                    </td>
                                    <td class="text-center" style="font-weight: 700; font-variant-numeric: tabular-nums;">${v.SoLuong}</td>
                                    <td class="text-center" style="font-weight: 700; font-variant-numeric: tabular-nums; color: ${v.DaSuDung > 0 ? 'var(--warning)' : 'var(--muted)'};">${v.DaSuDung}</td>
                                    <td class="text-center">
                                        <c:if test="${not empty v.DonHangSuDung and v.DonHangSuDung != 'null'}">
                                            <a href="${pageContext.request.contextPath}/OrderServlet?search=${fn:replace(v.DonHangSuDung, 'Đơn hàng #', '')}" style="color:var(--brand-accent); font-weight:600; font-family:var(--font-mono); font-size:12.5px;" title="Mở chi tiết hóa đơn">
                                                ${v.DonHangSuDung}
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${v.TrangThai == 0}">
                                                <span class="badge badge-danger">Đã khóa</span>
                                            </c:when>
                                            <c:when test="${v.SoLuong <= 0}">
                                                <span class="badge badge-warning">Hết lượt</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-success">Sẵn sàng</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/VoucherServlet?action=toggleStatus&id=${v.ID_Voucher}" 
                                           onclick="saveScrollPos(); return confirm('Bạn có chắc chắn muốn ${v.TrangThai == 1 ? 'Khóa' : 'Mở khóa'} voucher ${v.MaVoucher}?');"
                                           class="btn ${v.TrangThai == 1 ? 'btn-outline' : 'btn-primary'} btn-sm" style="height: 28px; padding: 0 8px;">
                                            ${v.TrangThai == 1 ? 'Khóa' : 'Mở'}
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </main>

    <!-- MODAL 1: TẠO MÃ VIP LẺ -->
    <div id="addModal" class="modal-bg">
        <div class="modal-box" style="max-width: 680px;">
            <button type="button" class="close-btn" onclick="closeModal('addModal')">&times;</button>
            <div class="modal-header">
                <h3 class="modal-title">Tạo Mã Voucher VIP Lẻ</h3>
            </div>
            <form action="${pageContext.request.contextPath}/VoucherServlet" method="post">
                <input type="hidden" name="action" value="addCustom">
                
                <div class="modal-body">
                    <div class="form-group">
                        <label>Cấu trúc mã VIP (Tiền tố VIP + tối đa 5 ký tự chữ/số):</label>
                        <div style="display: flex; gap: 8px; align-items: center;">
                            <input type="text" value="VIP" readonly style="width: 65px; height: var(--control-h); background: var(--raised); color: var(--brand-accent); font-weight: 800; text-align: center; border: 1px solid var(--control-border); border-radius: var(--radius-control); cursor: not-allowed;">
                            <input type="text" name="vipNumber" id="vipNumber" class="form-control" maxlength="5" pattern="[a-zA-Z0-9]{1,5}" placeholder="Tối đa 5 ký tự (VD: ATND, 88888)" title="Vui lòng nhập chữ hoặc số (từ 1 đến 5 ký tự, không dấu)" oninput="this.value = this.value.toUpperCase().replace(/[^A-Z0-9]/g, '')" required style="font-weight: 700; letter-spacing: 1px;">
                            <button type="button" class="btn btn-outline btn-sm" onclick="generateRandomVipNumber()">5 Ký tự ngẫu nhiên</button>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="tenChuongTrinh">Tên chương trình ưu đãi / Mục đích:</label>
                        <input type="text" name="tenChuongTrinh" id="tenChuongTrinh" class="form-control" value="Voucher đặc quyền dành riêng cho Khách Hàng VIP" required>
                    </div>
                    <div class="form-group">
                        <label for="vipGiamGia">Mệnh giá giảm & Điều kiện áp dụng:</label>
                        <input type="hidden" name="donToiThieu" id="vipDonToiThieu" value="3000000">
                        <select name="giamGia" id="vipGiamGia" class="form-control" onchange="updateVipDonToiThieu(this)" required>
                            <optgroup label="Giảm theo số tiền (VNĐ)">
                                <option value="600000" data-min="2200000">Giảm 600.000 VNĐ — Đơn từ 2.200.000 VNĐ</option>
                                <option value="750000" data-min="2400000">Giảm 750.000 VNĐ — Đơn từ 2.400.000 VNĐ</option>
                                <option value="950000" data-min="2600000">Giảm 950.000 VNĐ — Đơn từ 2.600.000 VNĐ</option>
                                <option value="1200000" data-min="2800000">Giảm 1.200.000 VNĐ — Đơn từ 2.800.000 VNĐ</option>
                                <option value="1500000" data-min="3000000" selected>Giảm 1.500.000 VNĐ — Đơn từ 3.000.000 VNĐ</option>
                            </optgroup>
                            <optgroup label="Giảm theo %">
                                <option value="30" data-min="3600000">Giảm 30% — Đơn từ 3.6 Triệu (Tối đa 1.5 Triệu)</option>
                                <option value="35" data-min="4000000">Giảm 35% — Đơn từ 4 Triệu (Tối đa 1.8 Triệu)</option>
                                <option value="40" data-min="4400000">Giảm 40% — Đơn từ 4.4 Triệu (Tối đa 2.2 Triệu)</option>
                                <option value="45" data-min="4700000">Giảm 45% — Đơn từ 4.7 Triệu (Tối đa 2.6 Triệu)</option>
                                <option value="50" data-min="5000000">Giảm 50% — Đơn từ 5 Triệu (Tối đa 3.2 Triệu)</option>
                            </optgroup>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Số lượt sử dụng (Cố định 1 lượt):</label>
                        <input type="number" name="soLuong" value="1" readonly class="form-control" style="cursor: not-allowed; font-weight:700;">
                    </div>
                </div>
                
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('addModal')">Hủy bỏ</button>
                    <button type="submit" class="btn btn-primary">Xác Nhận Tạo Mã</button>
                </div>
            </form>
        </div>
    </div>

    <!-- MODAL 2: TẠO LÔ MÃ TỰ ĐỘNG (BATCH GENERATOR) -->
    <div id="batchModal" class="modal-bg">
        <div class="modal-box" style="max-width: 680px;">
            <button type="button" class="close-btn" onclick="closeModal('batchModal')">&times;</button>
            <div class="modal-header">
                <h3 class="modal-title">Phát Hành Lô Voucher Tự Động Hàng Loạt</h3>
            </div>
            <form action="${pageContext.request.contextPath}/VoucherServlet" method="post">
                <input type="hidden" name="action" value="generateBatch">
                
                <div class="modal-body">
                    <div class="alert alert-warning" style="margin-bottom: 16px;">
                        <span>Hệ thống tự động nối 5 ký tự ngẫu nhiên sau tiền tố (ví dụ: YN100X7K9A...) để bảo mật tối đa.</span>
                    </div>

                    <div class="form-group">
                        <label for="batchAmount">Chọn mệnh giá & Điều kiện cho lô mã:</label>
                        <select name="batchAmount" id="batchAmount" class="form-control" onchange="updateBatchPrefix()" required>
                            <optgroup label="Giảm theo số tiền (VNĐ)">
                                <option value="100000" data-prefix="YN100">Giảm 100.000 VNĐ — Đơn từ 500.000 VNĐ (Mã YN100...)</option>
                                <option value="200000" data-prefix="YN200">Giảm 200.000 VNĐ — Đơn từ 1 Triệu (Mã YN200...)</option>
                                <option value="300000" data-prefix="YN300">Giảm 300.000 VNĐ — Đơn từ 1.5 Triệu (Mã YN300...)</option>
                                <option value="400000" data-prefix="YN400">Giảm 400.000 VNĐ — Đơn từ 2 Triệu (Mã YN400...)</option>
                                <option value="500000" data-prefix="YN500">Giảm 500.000 VNĐ — Đơn từ 2.5 Triệu (Mã YN500...)</option>
                            </optgroup>
                            <optgroup label="Giảm theo %">
                                <option value="5" data-prefix="YN5P">Giảm 5% — Đơn từ 800.000 VNĐ (Tối đa 100k - Mã YN5P...)</option>
                                <option value="10" data-prefix="YN10P">Giảm 10% — Đơn từ 1.5 Triệu (Tối đa 300k - Mã YN10P...)</option>
                                <option value="15" data-prefix="YN15P">Giảm 15% — Đơn từ 2.2 Triệu (Tối đa 500k - Mã YN15P...)</option>
                                <option value="20" data-prefix="YN20P">Giảm 20% — Đơn từ 2.7 Triệu (Tối đa 750k - Mã YN20P...)</option>
                                <option value="25" data-prefix="YN25P">Giảm 25% — Đơn từ 3.1 Triệu (Tối đa 1 Triệu - Mã YN25P...)</option>
                            </optgroup>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Tiền tố mã (Prefix nhận diện):</label>
                        <input type="hidden" name="batchPrefix" id="batchPrefix" value="YN100">
                        <input type="text" id="displayPrefix" class="form-control" value="YN100 + [5 Ký tự ngẫu nhiên]" readonly style="cursor: not-allowed; font-weight: 700;">
                    </div>
                    <div class="form-group">
                        <label>Số lượng mã cần tạo trong lô:</label>
                        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 12px;">
                            <div>
                                <select id="batchQtySelect" class="form-control" onchange="onSelectBatchQty(this.value)">
                                    <option value="10">10 mã voucher</option>
                                    <option value="20">20 mã voucher</option>
                                    <option value="50" selected>50 mã voucher</option>
                                    <option value="100">100 mã voucher</option>
                                    <option value="200">200 mã voucher</option>
                                    <option value="500">500 mã voucher</option>
                                    <option value="1000">1.000 mã voucher</option>
                                    <option value="5000">5.000 mã voucher</option>
                                </select>
                            </div>
                            <div>
                                <input type="number" name="batchQty" id="batchQtyInput" class="form-control" min="1" max="1000000" value="50" required placeholder="Nhập số lượng..." oninput="onInputBatchQty(this.value)" style="font-weight: 700;">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="batchTen">Tên chiến dịch / Chương trình cho cả lô:</label>
                        <input type="text" name="batchTen" id="batchTen" class="form-control" value="Lô voucher ưu đãi thời trang nam DUNA" required>
                    </div>
                </div>
                
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeModal('batchModal')">Hủy bỏ</button>
                    <button type="submit" class="btn btn-primary">Phát Hành Lô Ngay</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openCreateModal() {
            openModal('addModal');
        }
        function openModal(id) { 
            var el = document.getElementById(id);
            if (el) {
                el.classList.add('open');
            }
            if (id === 'addModal' && (!document.getElementById('vipNumber').value || document.getElementById('vipNumber').value.trim() === '')) {
                generateRandomVipNumber();
            }
        }
        function closeModal(id) { 
            var el = document.getElementById(id);
            if (el) {
                el.classList.remove('open');
            }
        }
        
        function generateRandomVipNumber() {
            const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
            let result = '';
            for (let i = 0; i < 5; i++) {
                result += chars.charAt(Math.floor(Math.random() * chars.length));
            }
            document.getElementById('vipNumber').value = result;
        }
        
        function updateVipDonToiThieu(sel) {
            const min = sel.options[sel.selectedIndex].getAttribute('data-min');
            if (min && document.getElementById('vipDonToiThieu')) {
                document.getElementById('vipDonToiThieu').value = min;
            }
        }
        
        function updateBatchPrefix() {
            const sel = document.getElementById('batchAmount');
            const amount = parseInt(sel.value, 10);
            const customPrefix = sel.options[sel.selectedIndex].getAttribute('data-prefix');
            let prefix = '';
            if (customPrefix) {
                prefix = customPrefix;
            } else if (amount <= 100) {
                prefix = 'YN' + amount + 'P';
            } else {
                prefix = 'YN' + (amount / 1000);
            }
            document.getElementById('batchPrefix').value = prefix;
            document.getElementById('displayPrefix').value = prefix + ' + [5 Ký tự ngẫu nhiên]';
        }

        function onSelectBatchQty(val) {
            if (!val) return;
            document.getElementById('batchQtyInput').value = val;
        }
        
        function onInputBatchQty(val) {
            const sel = document.getElementById('batchQtySelect');
            const opts = Array.from(sel.options).map(o => o.value);
            if (opts.includes(val)) {
                sel.value = val;
            } else {
                sel.selectedIndex = -1;
            }
        }

        function exportVouchersExcel() {
            const form = document.getElementById('voucherFilterForm');
            const params = new URLSearchParams(new FormData(form));
            params.set('export', 'excel');
            window.location.href = '${pageContext.request.contextPath}/VoucherServlet?' + params.toString();
        }

        function saveScrollPos() {
            sessionStorage.setItem('voucherScrollPos', window.scrollY || document.documentElement.scrollTop);
        }

        document.addEventListener("DOMContentLoaded", function() {
            const savedScrollPos = sessionStorage.getItem('voucherScrollPos');
            if (savedScrollPos !== null) {
                const pos = parseInt(savedScrollPos, 10);
                if (!isNaN(pos)) {
                    window.scrollTo(0, pos);
                    setTimeout(() => window.scrollTo(0, pos), 20);
                    setTimeout(() => window.scrollTo(0, pos), 100);
                }
                sessionStorage.removeItem('voucherScrollPos');
            }
        });

        document.querySelectorAll('.modal-bg').forEach(function(el){
            el.addEventListener('click',function(e){if(e.target===el) el.classList.remove('open');});
        });
    </script>
    <jsp:include page="footer.jsp" />
</body>
</html>
