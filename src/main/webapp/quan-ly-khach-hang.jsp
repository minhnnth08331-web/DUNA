<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% if (request.getAttribute("listKH") == null) {
    response.sendRedirect(request.getContextPath() + "/CustomerServlet"); return;
} %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý khách hàng — DUNA Men's Wear</title>
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
                <a href="${pageContext.request.contextPath}/OrderServlet">Quản lý hóa đơn</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/ProductServlet">Quản lý sản phẩm</a>
                <a href="${pageContext.request.contextPath}/EmployeeManagementServlet">Quản lý nhân sự</a>
                <a href="${pageContext.request.contextPath}/OrderServlet?view=report">Báo cáo thống kê</a>
                <a href="${pageContext.request.contextPath}/CustomerServlet" class="active">Quản lý khách hàng</a>
                <a href="${pageContext.request.contextPath}/OrderServlet?view=sell">Bán hàng</a>
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

<div class="page-container">
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">
            <span>${param.error}</span>
            <button type="button" onclick="this.parentElement.style.display='none'" class="close-btn" style="position:static; margin-left:auto;">&times;</button>
        </div>
    </c:if>
    <c:if test="${not empty msgSuccess}">
        <div class="alert alert-success">
            <span>${msgSuccess}</span>
            <button type="button" onclick="this.parentElement.style.display='none'" class="close-btn" style="position:static; margin-left:auto;">&times;</button>
        </div>
    </c:if>

    <div class="page-header">
        <div>
            <h1 class="page-title">Quản Lý Khách Hàng & Hội Viên VIP</h1>
            <p class="page-subtitle">Hồ sơ khách hàng, hạn mức chiết khấu hội viên DUNA Club và chu kỳ thăng hạng 24 tháng</p>
        </div>
        <c:if test="${sessionScope.role == 'MANAGER' or sessionScope.roleID == 1}">
            <div class="action-buttons">
                <button type="button" class="btn btn-primary" onclick="openAdd()">+ Thêm Khách Hàng</button>
            </div>
        </c:if>
    </div>

    <div class="toolbar-section">
        <form method="get" action="${pageContext.request.contextPath}/CustomerServlet" style="width:100%; margin:0;">
            <div class="filter-form">
                <div class="search-box-wrap">
                    <input type="text" name="search" class="search-box" placeholder="Nhập mã KH, họ tên, email hoặc số điện thoại..." value="${p_search}">
                </div>
                <select name="searchType" class="form-control" style="width:auto; min-width: 170px;" title="Chọn trường tìm kiếm">
                    <option value="">-- Tất cả các trường --</option>
                    <option value="maKH"  <c:if test="${p_searchType == 'maKH'}">selected</c:if>>Theo Mã khách hàng</option>
                    <option value="hoTen" <c:if test="${p_searchType == 'hoTen'}">selected</c:if>>Theo Họ và tên</option>
                    <option value="email" <c:if test="${p_searchType == 'email'}">selected</c:if>>Theo Địa chỉ Email</option>
                    <option value="sdt"   <c:if test="${p_searchType == 'sdt'}">selected</c:if>>Theo Số điện thoại</option>
                </select>
                <select name="rank" class="form-control" style="width:auto; min-width: 140px;">
                    <option value="">Tất cả hạng VIP</option>
                    <option value="VIP"    <c:if test="${p_rank == 'VIP'}">selected</c:if>>VIP (Giảm 15%)</option>
                    <option value="Vàng"  <c:if test="${p_rank == 'Vàng'}">selected</c:if>>Vàng (Giảm 10%)</option>
                    <option value="Bạc"   <c:if test="${p_rank == 'Bạc'}">selected</c:if>>Bạc (Giảm 5%)</option>
                    <option value="Thường" <c:if test="${p_rank == 'Thường'}">selected</c:if>>Thường (0%)</option>
                </select>
                <select name="status" class="form-control" style="width:auto; min-width: 140px;">
                    <option value="">Tất cả trạng thái</option>
                    <option value="active" <c:if test="${p_status == 'active'}">selected</c:if>>Hoạt động</option>
                    <option value="locked" <c:if test="${p_status == 'locked'}">selected</c:if>>Đã khóa</option>
                </select>
                <div class="action-buttons">
                    <button type="submit" class="btn btn-primary btn-sm">Tìm kiếm</button>
                    <a href="${pageContext.request.contextPath}/CustomerServlet" class="btn btn-outline btn-sm">Làm mới</a>
                </div>
            </div>
        </form>
    </div>

    <div id="ajax-content-area" style="transition: opacity 0.2s ease;">
        <div class="table-wrap">
            <table class="styled-table">
                <thead>
                    <tr>
                        <th style="width: 10%;">Mã KH</th>
                        <th style="width: 18%;">Họ và Tên</th>
                        <th style="width: 12%;">Số điện thoại</th>
                        <th style="width: 16%;">Email</th>
                        <th style="width: 12%; text-align: right;">Chi tiêu (24T)</th>
                        <th style="width: 8%; text-align: center;">Số đơn</th>
                        <th style="width: 12%;">Hạng hội viên</th>
                        <th style="width: 12%; text-align: center;">Hạn chu kỳ</th>
                        <th style="width: 10%;">Trạng thái</th>
                        <th style="width: 12%; text-align: center;">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listKH}" var="kh">
                        <tr>
                            <td style="font-family: var(--font-mono); font-weight: 700; color: var(--text-primary);">${kh.maKH}</td>
                            <td style="font-weight: 600;">${kh.hoTen}</td>
                            <td>${kh.sdt}</td>
                            <td>${kh.email}</td>
                            <td class="text-right" style="font-weight: 700; color: var(--positive);">${kh.tongChiTieu}</td>
                            <td class="text-center" style="font-weight: 700; font-family: var(--font-mono);">${kh.soDonDaMua}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${kh.hang == 'VIP'}">
                                        <span class="badge" style="background: var(--brand-accent-tint); color: var(--brand-accent-ink); border: 1px solid var(--brand-accent);">VIP &bull; -15%</span>
                                    </c:when>
                                    <c:when test="${kh.hang == 'Vàng'}">
                                        <span class="badge badge-warning">Vàng &bull; -10%</span>
                                    </c:when>
                                    <c:when test="${kh.hang == 'Bạc'}">
                                        <span class="badge badge-neutral">Bạc &bull; -5%</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-neutral">Thường &bull; 0%</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center" style="font-size: 12.5px; color: var(--muted); font-variant-numeric: tabular-nums;">
                                ${kh.hanReset}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${kh.trangThai == 'active'}">
                                        <span class="badge badge-success">Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-danger">Đã khóa</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td style="white-space:nowrap; text-align: center;">
                                <div style="display: inline-flex; gap: 6px;">
                                    <button type="button" class="btn btn-outline btn-sm" style="height: 28px; padding: 0 8px;"
                                        onclick="openDetail('${kh.id}','${kh.hoTen}','${kh.sdt}','${kh.email}','${kh.gioiTinh}','${kh.ngaySinh}','${kh.diaChi}')">
                                        Chi tiết
                                    </button>
                                    <c:if test="${sessionScope.role == 'MANAGER' or sessionScope.roleID == 1}">
                                        <button type="button" class="btn btn-outline btn-sm" style="height: 28px; padding: 0 8px;"
                                            onclick="openEdit('${kh.id}','${kh.hoTen}','${kh.sdt}','${kh.email}','${kh.gioiTinh}','${kh.ngaySinh}','${kh.diaChi}')">
                                            Sửa
                                        </button>
                                        <form method="post" action="${pageContext.request.contextPath}/CustomerServlet" style="display:inline">
                                            <input type="hidden" name="action" value="lock">
                                            <input type="hidden" name="id" value="${kh.id}">
                                            <button type="submit" class="btn btn-outline btn-sm" style="height: 28px; padding: 0 8px;"
                                                onclick="return confirm('Bạn có chắc muốn thay đổi trạng thái tài khoản ${kh.hoTen}?')">
                                                ${kh.trangThai == 'active' ? 'Khóa' : 'Mở'}
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty listKH}">
                        <tr><td colspan="10" style="text-align:center; color:var(--muted); padding:28px">Không có dữ liệu khách hàng nào phù hợp.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <%-- Thanh phân trang --%>
        <c:if test="${totalPages > 1}">
            <c:set var="urlParams" value="${not empty p_search ? '&search='.concat(p_search) : ''}${not empty p_searchType ? '&searchType='.concat(p_searchType) : ''}${not empty p_rank ? '&rank='.concat(p_rank) : ''}${not empty p_status ? '&status='.concat(p_status) : ''}" />
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
                    <a href="?page=${i}${urlParams}" class="${currentPage == i ? 'active' : ''}">${i}</a>
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

    <%-- Quyền lợi Hội viên DUNA Club --%>
    <div class="panel" style="margin-top: 24px;">
        <h3 style="margin: 0 0 10px 0; font-size: 15px; font-weight: 700; color: var(--text-primary);">
            Quy Định Thăng Hạng & Chu Kỳ Tích Lũy Hội Viên (24 Tháng)
        </h3>
        <p style="margin: 0 0 16px 0; color: var(--muted); font-size: 13px;">
            Hệ thống tự động xét duyệt và nâng hạng hội viên theo số lượng sản phẩm thời trang đã tích lũy trong chu kỳ hiệu lực 2 năm:
        </p>
        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 14px; margin-bottom: 16px;">
            <div class="panel" style="padding: 14px; background: var(--raised);">
                <div style="margin-bottom: 8px;"><span class="badge badge-neutral">Thường &bull; 0%</span></div>
                <div style="font-size: 12.5px; color: var(--text); line-height: 1.5;">
                    &bull; Điều kiện: <strong>0 &ndash; 1 sản phẩm</strong><br>
                    &bull; Quyền lợi: Giá niêm yết
                </div>
            </div>
            <div class="panel" style="padding: 14px; background: var(--raised);">
                <div style="margin-bottom: 8px;"><span class="badge badge-neutral">Bạc &bull; Giảm 5%</span></div>
                <div style="font-size: 12.5px; color: var(--text); line-height: 1.5;">
                    &bull; Điều kiện: Từ <strong>2 &ndash; 4 sản phẩm</strong><br>
                    &bull; Quyền lợi: Chiết khấu <strong>5%</strong> trực tiếp
                </div>
            </div>
            <div class="panel" style="padding: 14px; background: var(--raised);">
                <div style="margin-bottom: 8px;"><span class="badge badge-warning">Vàng &bull; Giảm 10%</span></div>
                <div style="font-size: 12.5px; color: var(--text); line-height: 1.5;">
                    &bull; Điều kiện: Từ <strong>5 &ndash; 9 sản phẩm</strong><br>
                    &bull; Quyền lợi: Chiết khấu <strong>10%</strong> trực tiếp
                </div>
            </div>
            <div class="panel" style="padding: 14px; background: var(--raised);">
                <div style="margin-bottom: 8px;"><span class="badge" style="background: var(--brand-accent-tint); color: var(--brand-accent-ink); border: 1px solid var(--brand-accent);">VIP &bull; Giảm 15%</span></div>
                <div style="font-size: 12.5px; color: var(--text); line-height: 1.5;">
                    &bull; Điều kiện: Từ <strong>10 sản phẩm trở lên</strong><br>
                    &bull; Quyền lợi: Chiết khấu cao cấp <strong>15%</strong>
                </div>
            </div>
        </div>
        <div style="font-size: 12px; color: var(--muted); border-top: 1px solid var(--border); padding-top: 10px;">
            * Chu kỳ 24 tháng giúp đảm bảo quyền lợi tích lũy và động lực mua sắm. Khi hết hạn chu kỳ, hệ thống sẽ tự động chuyển sang chu kỳ mới mà không làm ảnh hưởng đến lịch sử hóa đơn trong mục Quản lý hóa đơn.
        </div>
    </div>
</div>

<%-- Modal Thêm / Sửa / Xem Khách Hàng --%>
<div class="modal-bg" id="modalKH">
    <div class="modal-box" style="max-width: 680px;">
        <button type="button" class="close-btn" onclick="closeModal('modalKH')">&times;</button>
        <div class="modal-header">
            <h3 class="modal-title" id="modalTitle">Thêm khách hàng</h3>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/CustomerServlet" id="formKH">
            <input type="hidden" name="action" id="fAction" value="add">
            <input type="hidden" name="id"     id="fId">
            <div class="modal-body">
                <div class="form-grid">
                    <div>
                        <label for="fName">Họ và tên <span style="color:var(--negative)">*</span></label>
                        <input type="text" name="hoTen" id="fName" class="form-control" required placeholder="Nhập họ và tên...">
                    </div>
                    <div>
                        <label for="fSdt">Số điện thoại <span style="color:var(--negative)">*</span></label>
                        <div class="phone-wrap">
                            <span class="phone-prefix">(+84)</span>
                            <input type="tel" name="sdt" id="fSdt" class="form-control" required placeholder="912 345 678" maxlength="10">
                        </div>
                    </div>
                    <div>
                        <label for="fEmail">Địa chỉ Email</label>
                        <input type="email" name="email" id="fEmail" class="form-control" placeholder="khachhang@email.com">
                    </div>
                    <div>
                        <label for="fGender">Giới tính</label>
                        <select name="gioiTinh" id="fGender" class="form-control">
                            <option>Nam</option>
                            <option>Nữ</option>
                            <option>Khác</option>
                        </select>
                    </div>
                    <div>
                        <label for="fDob">Ngày sinh</label>
                        <input type="date" name="ngaySinh" id="fDob" class="form-control">
                    </div>
                    <div style="display:flex; flex-direction:column; justify-content:center;">
                        <span style="font-size: 11.5px; color: var(--muted); font-weight: 600; text-transform: uppercase;">Cơ chế phân hạng</span>
                        <span style="font-size: 13px; color: var(--text); font-weight: 600; margin-top: 4px;">Tự động tính theo đơn hàng</span>
                    </div>
                    <div class="full">
                        <label for="fAddr">Địa chỉ nhận hàng</label>
                        <textarea name="diaChi" id="fAddr" class="form-control" placeholder="Số nhà, đường, phường/xã, quận/huyện, tỉnh/thành phố..."></textarea>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline" id="btnCancelKH" onclick="closeModal('modalKH')">Hủy</button>
                <button type="submit" class="btn btn-primary" id="btnSubmitKH">Lưu Thông Tin</button>
            </div>
        </form>
    </div>
</div>

<script>
function openAdd(){
    document.getElementById('modalTitle').textContent='Thêm Khách Hàng Mới';
    document.getElementById('fAction').value='add'; document.getElementById('fId').value='';
    ['fName','fSdt','fEmail','fAddr'].forEach(function(i){document.getElementById(i).value=''; document.getElementById(i).readOnly=false;});
    document.getElementById('fDob').value=''; document.getElementById('fDob').readOnly=false;
    document.getElementById('fGender').value='Nam'; document.getElementById('fGender').disabled=false;
    document.getElementById('btnSubmitKH').style.display='inline-flex';
    document.getElementById('btnCancelKH').textContent='Hủy';
    openModal('modalKH');
}
function openEdit(id,ten,sdt,email,gt,dob,dc){
    document.getElementById('modalTitle').textContent='Chỉnh Sửa Hồ Sơ Khách Hàng';
    document.getElementById('fAction').value='edit'; document.getElementById('fId').value=id;
    document.getElementById('fName').value=ten; document.getElementById('fName').readOnly=false;
    document.getElementById('fSdt').value=sdt ? sdt.replace(/^0+/, '') : ''; document.getElementById('fSdt').readOnly=false;
    document.getElementById('fEmail').value=email; document.getElementById('fEmail').readOnly=false;
    document.getElementById('fGender').value=gt; document.getElementById('fGender').disabled=false;
    document.getElementById('fDob').value=dob; document.getElementById('fDob').readOnly=false;
    document.getElementById('fAddr').value=dc; document.getElementById('fAddr').readOnly=false;
    document.getElementById('btnSubmitKH').style.display='inline-flex';
    document.getElementById('btnCancelKH').textContent='Hủy';
    openModal('modalKH');
}
function openDetail(id,ten,sdt,email,gt,dob,dc){
    document.getElementById('modalTitle').textContent='Hồ Sơ Chi Tiết Khách Hàng';
    document.getElementById('fAction').value=''; document.getElementById('fId').value=id;
    document.getElementById('fName').value=ten; document.getElementById('fName').readOnly=true;
    document.getElementById('fSdt').value=sdt ? sdt.replace(/^0+/, '') : ''; document.getElementById('fSdt').readOnly=true;
    document.getElementById('fEmail').value=email; document.getElementById('fEmail').readOnly=true;
    document.getElementById('fGender').value=gt; document.getElementById('fGender').disabled=true;
    document.getElementById('fDob').value=dob; document.getElementById('fDob').readOnly=true;
    document.getElementById('fAddr').value=dc; document.getElementById('fAddr').readOnly=true;
    document.getElementById('btnSubmitKH').style.display='none';
    document.getElementById('btnCancelKH').textContent='Đóng';
    openModal('modalKH');
}
function openModal(id){ document.getElementById(id).classList.add('open'); }
function closeModal(id){ document.getElementById(id).classList.remove('open'); }
document.querySelectorAll('.modal-bg').forEach(function(el){
    el.addEventListener('click',function(e){if(e.target===el) el.classList.remove('open');});
});
</script>
<jsp:include page="footer.jsp" />
</body>
</html>
