<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- Redirect sang Servlet nếu truy cập JSP trực tiếp --%>
<% if (request.getAttribute("listSP") == null) {
    response.sendRedirect(request.getContextPath() + "/ProductServlet"); return;
} %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý sản phẩm thời trang — DUNA Men's Wear</title>
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
        <a href="${pageContext.request.contextPath}/ProductServlet" class="active">Quản lý sản phẩm</a>
        <a href="${pageContext.request.contextPath}/EmployeeManagementServlet">Quản lý nhân sự</a>
        <a href="${pageContext.request.contextPath}/OrderServlet?view=report">Báo cáo thống kê</a>
        <a href="${pageContext.request.contextPath}/CustomerServlet">Quản lý khách hàng</a>
        <a href="${pageContext.request.contextPath}/OrderServlet?view=sell">Bán hàng</a>
        <a href="${pageContext.request.contextPath}/OrderServlet">Quản lý hóa đơn</a>
        <a href="${pageContext.request.contextPath}/VoucherServlet">Quản lý voucher</a>
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

    <div class="page-header">
        <div>
            <h1 class="page-title">Quản Lý Sản Phẩm Thời Trang DUNA</h1>
            <p class="page-subtitle">Danh mục áo sơ mi, polo, quần âu, blazer, biến thể màu sắc & size, in tem mác barcode chuẩn xuất xưởng</p>
        </div>
        <div class="action-buttons">
            <button type="button" class="btn btn-primary" onclick="openAdd()">
                + Thêm Sản Phẩm Mới
            </button>
        </div>
    </div>

    <%-- Bộ lọc --%>
    <div class="toolbar-section">
        <form method="get" action="${pageContext.request.contextPath}/ProductServlet" style="width:100%; margin:0;">
            <div class="filter-form">
                <div class="search-box-wrap">
                    <input type="text" name="search" class="search-box" placeholder="Tìm theo tên SP, mã SP, chất liệu, form dáng..." value="${p_search}">
                </div>
                <select name="cat" class="form-control" style="width:auto; min-width: 170px;">
                    <option value="">Tất cả danh mục</option>
                    <option value="Áo Sơ Mi Nam" <c:if test="${p_cat == 'Áo Sơ Mi Nam'}">selected</c:if>>Áo Sơ Mi Nam</option>
                    <option value="Áo Polo Nam" <c:if test="${p_cat == 'Áo Polo Nam'}">selected</c:if>>Áo Polo Nam</option>
                    <option value="Quần Âu Nam" <c:if test="${p_cat == 'Quần Âu Nam'}">selected</c:if>>Quần Âu Nam</option>
                    <option value="Áo Blazer Nam" <c:if test="${p_cat == 'Áo Blazer Nam'}">selected</c:if>>Áo Blazer Nam</option>
                    <option value="Quần Khaki Nam" <c:if test="${p_cat == 'Quần Khaki Nam'}">selected</c:if>>Quần Khaki Nam</option>
                    <option value="Phụ Kiện Nam" <c:if test="${p_cat == 'Phụ Kiện Nam'}">selected</c:if>>Phụ Kiện Nam</option>
                </select>
                <select name="status" class="form-control" style="width:auto; min-width: 150px;">
                    <option value="">Tất cả trạng thái</option>
                    <option value="Còn hàng"  <c:if test="${p_status == 'Còn hàng'}">selected</c:if>>Còn hàng</option>
                    <option value="Hết hàng"  <c:if test="${p_status == 'Hết hàng'}">selected</c:if>>Hết hàng</option>
                    <option value="Ngừng bán" <c:if test="${p_status == 'Ngừng bán'}">selected</c:if>>Ngừng bán</option>
                </select>
                <div class="action-buttons">
                    <button type="submit" class="btn btn-primary btn-sm">Lọc</button>
                    <a href="${pageContext.request.contextPath}/ProductServlet" class="btn btn-outline btn-sm">Làm mới</a>
                </div>
            </div>
        </form>
    </div>

    <%-- Bảng sản phẩm --%>
    <div class="table-wrap">
        <table class="styled-table">
            <thead>
                <tr>
                    <th style="width: 10%;">Mã SP</th>
                    <th style="width: 20%;">Tên Sản Phẩm</th>
                    <th style="width: 12%;">Danh Mục</th>
                    <th style="width: 16%;">Chất Liệu & Form</th>
                    <th style="width: 8%; text-align: right;">Tổng Tồn</th>
                    <th style="width: 10%; text-align: right;">Giá Vốn</th>
                    <th style="width: 11%; text-align: right;">Giá Niêm Yết</th>
                    <th style="width: 9%; text-align: center;">Ngày Tạo</th>
                    <th style="width: 9%; text-align: center;">Trạng Thái</th>
                    <th style="width: 15%; text-align: center;">Thao Tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${listSP}" var="sp">
                    <tr>
                        <td style="font-family: var(--font-mono); font-weight: 700; color: var(--text-primary);">${sp.maSP}</td>
                        <td>
                            <div style="font-weight: 700; color: var(--text-primary); margin-bottom: 2px;">${sp.tenSP}</div>
                            <div style="font-size: 11.5px; color: var(--muted); text-transform: uppercase; letter-spacing: 0.04em;">${sp.thuongHieu}</div>
                        </td>
                        <td><span class="badge badge-neutral">${sp.danhMuc}</span></td>
                        <td style="font-size: 12.5px; color: var(--muted); line-height: 1.4;">
                            <c:if test="${not empty sp.chatLieu}"><div>Vải: <strong style="color:var(--text-primary);">${sp.chatLieu}</strong></div></c:if>
                            <c:if test="${not empty sp.formDang}"><div>Form: <strong style="color:var(--text-primary);">${sp.formDang}</strong></div></c:if>
                        </td>
                        <td class="text-right" style="font-weight: 800; font-variant-numeric: tabular-nums; color: ${sp.soLuong > 5 ? 'var(--text-primary)' : (sp.soLuong > 0 ? 'var(--warning)' : 'var(--negative)')};">
                            ${sp.soLuong}
                        </td>
                        <td class="text-right" style="color: var(--muted);"><fmt:formatNumber value="${sp.giaNhap}" type="number" groupingUsed="true"/> đ</td>
                        <td class="text-right" style="font-weight: 800; color: var(--text-primary);"><fmt:formatNumber value="${sp.giaBan}" type="number" groupingUsed="true"/> đ</td>
                        <td class="text-center" style="font-size: 12px; color: var(--muted);">${sp.ngayTao}</td>
                        <td class="text-center">
                            <c:choose>
                                <c:when test="${sp.trangThai == 'Còn hàng'}">
                                    <span class="badge badge-success">Còn hàng</span>
                                </c:when>
                                <c:when test="${sp.trangThai == 'Hết hàng'}">
                                    <span class="badge badge-danger">Hết hàng</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-neutral">Ngừng bán</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-center">
                            <div style="display:inline-flex; gap:4px; align-items:center;">
                                <button type="button" class="btn btn-outline btn-sm" style="height: 28px; padding: 0 8px;" onclick="openVariants('${sp.id}','${sp.tenSP}')" title="Quản lý biến thể size/màu">Biến thể</button>
                                <button type="button" class="btn btn-outline btn-sm" style="height: 28px; padding: 0 8px;" onclick="openPrintTagModal('${sp.id}','${sp.maSP}','${sp.tenSP}','${sp.giaBan}')" title="In tem mác barcode">In tem</button>
                                <button type="button" class="btn btn-outline btn-sm" style="height: 28px; padding: 0 8px;" onclick="openEdit('${sp.id}','${sp.tenSP}','${sp.danhMuc}','${sp.thuongHieu}','${sp.giaBan}','${sp.giaNhap}','${sp.soLuong}','${sp.trangThai}','${sp.chatLieu}','${sp.formDang}','${sp.hinhAnh}','${sp.moTa}')" title="Sửa thông tin">Sửa</button>
                                <form method="post" action="${pageContext.request.contextPath}/ProductServlet" style="display:inline; margin:0;" onsubmit="return confirm('Bạn có chắc chắn muốn ngừng bán / xóa sản phẩm này?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="${sp.id}">
                                    <button type="submit" class="btn btn-ghost btn-sm" style="height: 28px; padding: 0 6px; color: var(--negative) !important;" title="Ngừng bán hoặc xóa">&times;</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty listSP}">
                    <tr><td colspan="10" style="text-align:center; color:var(--muted); padding:32px">Không tìm thấy sản phẩm thời trang nào phù hợp bộ lọc.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <%-- Phân trang --%>
    <c:if test="${totalPages > 1}">
        <c:set var="urlParams" value="${not empty p_search ? '&search='.concat(p_search) : ''}${not empty p_cat ? '&cat='.concat(p_cat) : ''}${not empty p_status ? '&status='.concat(p_status) : ''}" />
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

<%-- MODAL THÊM / SỬA SẢN PHẨM --%>
<div class="modal-bg" id="modalSP">
    <div class="modal-box wide">
        <button type="button" class="close-btn" onclick="closeModal('modalSP')">&times;</button>
        <div class="modal-header">
            <h3 class="modal-title" id="modalTitle">Thêm sản phẩm thời trang DUNA</h3>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/ProductServlet" id="formSP">
            <input type="hidden" name="action" id="fAction" value="add">
            <input type="hidden" name="id"     id="fId">
            <div class="modal-body">
                <div class="form-grid">
                    <div class="full">
                        <label for="fName">Tên sản phẩm thời trang <span style="color:var(--negative)">*</span></label>
                        <input type="text" name="tenSP" id="fName" class="form-control" required placeholder="Ví dụ: Áo Sơ Mi Nam Oxford Button-Down DUNA...">
                    </div>
                    <div>
                        <label for="fCat">Danh mục thời trang <span style="color:var(--negative)">*</span></label>
                        <select name="cat" id="fCat" class="form-control" required>
                            <option value="Áo Sơ Mi Nam">Áo Sơ Mi Nam</option>
                            <option value="Áo Polo Nam">Áo Polo Nam</option>
                            <option value="Quần Âu Nam">Quần Âu Nam</option>
                            <option value="Áo Blazer Nam">Áo Blazer Nam</option>
                            <option value="Quần Khaki Nam">Quần Khaki Nam</option>
                            <option value="Phụ Kiện Nam">Phụ Kiện Nam</option>
                        </select>
                    </div>
                    <div>
                        <label for="fBrand">Thương hiệu</label>
                        <input type="text" name="thuongHieu" id="fBrand" class="form-control" value="DUNA" placeholder="DUNA">
                    </div>
                    <div>
                        <label for="fChatLieu">Chất liệu vải</label>
                        <input type="text" name="chatLieu" id="fChatLieu" class="form-control" placeholder="Ví dụ: 100% Cotton Oxford, Poly Rayon Nano...">
                    </div>
                    <div>
                        <label for="fFormDang">Form dáng</label>
                        <input type="text" name="formDang" id="fFormDang" class="form-control" placeholder="Ví dụ: Slimfit tôn dáng, Regular Fit, Tailored Fit...">
                    </div>
                    <div>
                        <label for="fCostPrice">Giá vốn / Giá nhập (VNĐ) <span style="color:var(--negative)">*</span></label>
                        <input type="number" name="giaNhap" id="fCostPrice" class="form-control" placeholder="0" required min="0">
                    </div>
                    <div>
                        <label for="fPrice">Giá bán niêm yết (VNĐ) <span style="color:var(--negative)">*</span></label>
                        <input type="number" name="giaBan" id="fPrice" class="form-control" placeholder="0" required min="0">
                    </div>
                    <div>
                        <label for="fQty" id="lblQty">Số lượng tồn kho ban đầu</label>
                        <input type="number" name="soLuong" id="fQty" class="form-control" value="10" min="0">
                    </div>
                    <div id="fStWrapper" style="display:flex; align-items:center; gap:8px; padding-top:20px;">
                        <input type="checkbox" name="trangThai" value="Ngừng bán" id="fSt">
                        <label for="fSt" style="margin-bottom:0; cursor:pointer; color:var(--negative); font-weight:700;">Ngừng kinh doanh sản phẩm này</label>
                    </div>
                    <div class="full">
                        <label for="fHinhAnh">Đường dẫn hình ảnh (URL)</label>
                        <input type="text" name="hinhAnh" id="fHinhAnh" class="form-control" placeholder="https://... (để trống nếu dùng ảnh mặc định)">
                    </div>
                    <div class="full">
                        <label for="fDesc">Mô tả sản phẩm</label>
                        <textarea name="moTa" id="fDesc" class="form-control" placeholder="Mô tả chi tiết kiểu dáng, thông số giặt ủi, chất liệu cao cấp..."></textarea>
                    </div>

                    <%-- Biến thể ban đầu khi tạo mới --%>
                    <div id="defVarBox" class="panel full" style="background:var(--raised); padding:16px;">
                        <div style="font-size:12.5px; font-weight:700; color:var(--text-primary); margin-bottom:12px; text-transform:uppercase; letter-spacing:0.04em;">
                            Khởi tạo biến thể đầu tiên (Màu sắc & Size)
                        </div>
                        <div class="form-grid">
                            <div>
                                <label for="fMauSac">Màu sắc</label>
                                <select name="mauSac" id="fMauSac" class="form-control">
                                    <option value="Trắng">Trắng</option>
                                    <option value="Đen">Đen</option>
                                    <option value="Xanh Navy">Xanh Navy</option>
                                    <option value="Ghi Xám">Ghi Xám</option>
                                    <option value="Be">Be</option>
                                    <option value="Rêu">Rêu</option>
                                    <option value="Xanh Nhạt">Xanh Nhạt</option>
                                    <option value="Nâu">Nâu</option>
                                </select>
                            </div>
                            <div>
                                <label for="fTrongLuong">Kích cỡ (Size)</label>
                                <select name="trongLuong" id="fTrongLuong" class="form-control">
                                    <option value="S">Size S (50 - 58kg)</option>
                                    <option value="M">Size M (59 - 65kg)</option>
                                    <option value="L" selected>Size L (66 - 72kg)</option>
                                    <option value="XL">Size XL (73 - 80kg)</option>
                                    <option value="XXL">Size XXL (81 - 88kg)</option>
                                    <option value="29">Size 29 (Quần)</option>
                                    <option value="30">Size 30 (Quần)</option>
                                    <option value="31">Size 31 (Quần)</option>
                                    <option value="32">Size 32 (Quần)</option>
                                    <option value="33">Size 33 (Quần)</option>
                                    <option value="34">Size 34 (Quần)</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="closeModal('modalSP')">Hủy</button>
                <button type="submit" class="btn btn-primary">Lưu Sản Phẩm</button>
            </div>
        </form>
    </div>
</div>

<%-- MODAL QUẢN LÝ BIẾN THỂ SIZE & MÀU --%>
<div class="modal-bg" id="modalVariant">
    <div class="modal-box wide">
        <button type="button" class="close-btn" onclick="closeModal('modalVariant')">&times;</button>
        <div class="modal-header">
            <h3 class="modal-title" id="variantTitle">Biến thể sản phẩm</h3>
            <button type="button" class="btn btn-outline btn-sm" onclick="toggleForm('variantForm')">+ Thêm Size/Màu mới</button>
        </div>

        <%-- Form Thêm Biến Thể --%>
        <div id="variantForm" class="panel" style="display:none; margin-bottom:18px; background:var(--raised);">
            <div style="font-weight:700; font-size:13px; color:var(--text-primary); margin-bottom:10px;">Thêm Biến Thể Mới</div>
            <form method="post" action="${pageContext.request.contextPath}/ProductServlet" onsubmit="return validateVariant('add');">
                <input type="hidden" name="action" value="addVariant">
                <input type="hidden" name="idSP" id="vfIdSP">
                <div class="form-grid" style="grid-template-columns: repeat(4, 1fr); gap: 10px;">
                    <div>
                        <label>Màu sắc</label>
                        <select name="mauSac" class="form-control">
                            <option>Trắng</option><option>Đen</option><option>Xanh Navy</option>
                            <option>Ghi Xám</option><option>Be</option><option>Rêu</option>
                            <option>Xanh Nhạt</option><option>Nâu</option>
                        </select>
                    </div>
                    <div>
                        <label>Size</label>
                        <select name="trongLuong" class="form-control">
                            <option>S</option><option>M</option><option selected>L</option>
                            <option>XL</option><option>XXL</option>
                            <option>29</option><option>30</option><option>31</option><option>32</option><option>33</option><option>34</option>
                        </select>
                    </div>
                    <div>
                        <label>Giá bán (VNĐ)</label>
                        <input type="number" name="giaBan" id="vfGiaBan" class="form-control" min="0" required>
                    </div>
                    <div>
                        <label>Số lượng kho</label>
                        <input type="number" name="soLuong" class="form-control" value="10" min="0" required>
                    </div>
                </div>
                <div style="margin-top:12px; display:flex; gap:8px;">
                    <button type="submit" class="btn btn-primary btn-sm">Lưu biến thể</button>
                    <button type="button" class="btn btn-outline btn-sm" onclick="toggleForm('variantForm')">Hủy</button>
                </div>
            </form>
        </div>

        <%-- Form Sửa Biến Thể --%>
        <div id="variantEditForm" class="panel" style="display:none; margin-bottom:18px; background:var(--raised); border-color:var(--brand-accent);">
            <div style="font-weight:700; font-size:13px; color:var(--text-primary); margin-bottom:10px;">Cập Nhật Thông Tin Biến Thể</div>
            <form method="post" action="${pageContext.request.contextPath}/ProductServlet" onsubmit="return validateVariant('edit');">
                <input type="hidden" name="action" value="editVariant">
                <input type="hidden" name="idBT" id="vefIdBT">
                <input type="hidden" name="idSP" id="vefIdSP">
                <div class="form-grid" style="grid-template-columns: repeat(4, 1fr); gap: 10px;">
                    <div>
                        <label>Màu sắc</label>
                        <select name="mauSac" id="vefMauSac" class="form-control">
                            <option>Trắng</option><option>Đen</option><option>Xanh Navy</option>
                            <option>Ghi Xám</option><option>Be</option><option>Rêu</option>
                            <option>Xanh Nhạt</option><option>Nâu</option>
                        </select>
                    </div>
                    <div>
                        <label>Size</label>
                        <select name="trongLuong" id="vefTrongLuong" class="form-control">
                            <option>S</option><option>M</option><option>L</option>
                            <option>XL</option><option>XXL</option>
                            <option>29</option><option>30</option><option>31</option><option>32</option><option>33</option><option>34</option>
                        </select>
                    </div>
                    <div>
                        <label>Giá bán (VNĐ)</label>
                        <input type="number" name="giaBan" id="vefGiaBan" class="form-control" min="0">
                    </div>
                    <div>
                        <label>Số lượng kho</label>
                        <input type="number" name="soLuong" id="vefSoLuong" class="form-control" min="0">
                    </div>
                </div>
                <div style="margin-top:12px; display:flex; gap:8px;">
                    <button type="submit" class="btn btn-primary btn-sm">Cập nhật</button>
                    <button type="button" class="btn btn-outline btn-sm" onclick="toggleForm('variantEditForm')">Hủy</button>
                </div>
            </form>
        </div>

        <div class="table-wrap">
            <table class="styled-table">
                <thead>
                    <tr>
                        <th style="width: 50px; text-align: center;">STT</th>
                        <th style="width: 18%;">Màu sắc</th>
                        <th style="width: 14%;">Size</th>
                        <th style="width: 18%; text-align: right;">Giá bán</th>
                        <th style="width: 14%; text-align: right;">Tồn kho</th>
                        <th style="width: 22%;">Barcode</th>
                        <th style="width: 14%; text-align: center;">Thao tác</th>
                    </tr>
                </thead>
                <tbody id="variantBody"></tbody>
            </table>
        </div>
    </div>
</div>

<%-- MODAL IN TEM MÁC BARCODE TREO QUẦN ÁO DUNA --%>
<div class="modal-bg" id="modalPrintTag">
    <div class="modal-box" style="max-width: 480px;">
        <button type="button" class="close-btn" onclick="closeModal('modalPrintTag')">&times;</button>
        <div class="modal-header">
            <h3 class="modal-title">In Tem Mác Barcode Treo Hàng</h3>
        </div>

        <div class="form-group" style="margin-bottom: 18px;">
            <label for="tagSelectVariant">Chọn kích cỡ / màu sắc cần in:</label>
            <select id="tagSelectVariant" class="form-control" onchange="renderTagPreview()">
                <option value="">Đang tải biến thể...</option>
            </select>
        </div>

        <%-- KHU VỰC THẺ BÀI XEM TRƯỚC VÀ IN --%>
        <div id="printableTagArea" style="display:flex; justify-content:center; margin: 10px 0;">
            <div class="tag-preview-card">
                <div class="tag-brand">DUNA</div>
                <div class="tag-sub">MEN'S WEAR &bull; PREMIUM BESPOKE</div>
                <div class="tag-name" id="tagPrevName">Áo Sơ Mi Nam Oxford DUNA</div>
                <div style="background: #f1f5f9; padding: 6px; border-radius: 4px; font-size: 12.5px; margin-bottom: 12px;">
                    MÀU: <strong id="tagPrevColor">Trắng</strong> &nbsp;|&nbsp; SIZE: <strong id="tagPrevSize" style="color:var(--brand-navy); font-size:14px;">L</strong>
                </div>
                <div style="margin: 8px 0;">
                    <svg id="tagBarcodeSvg" style="max-width:220px; height:50px;"></svg>
                    <div id="tagBarcodeText" class="tag-sku">SP001BT01</div>
                </div>
                <div class="tag-price" id="tagPrevPrice">450.000 VNĐ</div>
                <div style="font-size: 11px; color: #64748b;">(Đã bao gồm thuế GTGT)</div>
            </div>
        </div>

        <div class="modal-footer">
            <button type="button" class="btn btn-outline" onclick="closeModal('modalPrintTag')">Đóng</button>
            <button type="button" class="btn btn-primary" onclick="window.print()">In Tem Mác Ngay</button>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.6/dist/JsBarcode.all.min.js"></script>
<script>
var currentSpId = null;
var currentSpData = {};
window.currentVariants = [];

function toggleForm(id) {
    var f = document.getElementById(id);
    if (f) f.style.display = (f.style.display === 'none' ? 'block' : 'none');
}

function openModal(id) { 
    var el = document.getElementById(id);
    if (el) el.classList.add('open'); 
}
function closeModal(id) { 
    var el = document.getElementById(id);
    if (el) el.classList.remove('open'); 
}

document.querySelectorAll('.modal-bg').forEach(function(el){
    el.addEventListener('click', function(e){ if (e.target === el) el.classList.remove('open'); });
});

function openAdd() {
    document.getElementById('modalTitle').textContent = 'Thêm sản phẩm thời trang DUNA';
    document.getElementById('fAction').value = 'add';
    document.getElementById('fId').value = '';
    document.getElementById('fName').value = '';
    document.getElementById('fBrand').value = 'DUNA';
    document.getElementById('fChatLieu').value = '';
    document.getElementById('fFormDang').value = 'Slimfit tôn dáng';
    document.getElementById('fCostPrice').value = '';
    document.getElementById('fPrice').value = '';
    document.getElementById('fQty').value = '10';
    document.getElementById('fQty').readOnly = false;
    document.getElementById('fDesc').value = '';
    document.getElementById('fHinhAnh').value = '';
    document.getElementById('fStWrapper').style.display = 'none';
    document.getElementById('fSt').checked = false;
    document.getElementById('defVarBox').style.display = 'block';
    openModal('modalSP');
}

function openEdit(id, ten, cat, brand, giaBan, giaNhap, sl, tt, chatLieu, formDang, hinhAnh, moTa) {
    document.getElementById('modalTitle').textContent = 'Sửa thông tin sản phẩm';
    document.getElementById('fAction').value = 'edit';
    document.getElementById('fId').value = id;
    document.getElementById('fName').value = ten;
    document.getElementById('fCat').value = cat;
    document.getElementById('fBrand').value = brand || 'DUNA';
    document.getElementById('fChatLieu').value = chatLieu || '';
    document.getElementById('fFormDang').value = formDang || '';
    document.getElementById('fCostPrice').value = giaNhap || '';
    document.getElementById('fPrice').value = giaBan || '';
    document.getElementById('fQty').value = sl || '0';
    document.getElementById('fQty').readOnly = true;
    document.getElementById('fHinhAnh').value = hinhAnh || '';
    document.getElementById('fDesc').value = moTa || '';
    document.getElementById('fStWrapper').style.display = 'flex';
    document.getElementById('fSt').checked = (tt === 'Ngừng bán');
    document.getElementById('defVarBox').style.display = 'none';
    openModal('modalSP');
}

function openVariants(id, ten) {
    currentSpId = id;
    document.getElementById('variantTitle').textContent = 'Biến thể Size & Màu: ' + ten;
    document.getElementById('vfIdSP').value = id;
    document.getElementById('vefIdSP').value = id;
    document.getElementById('variantForm').style.display = 'none';
    document.getElementById('variantEditForm').style.display = 'none';

    fetch('${pageContext.request.contextPath}/ProductServlet?ajaxVariant=1&idSP=' + id)
        .then(function(r){ return r.json(); })
        .then(function(data){
            window.currentVariants = data || [];
            var tb = document.getElementById('variantBody');
            if (!data || data.length === 0) {
                tb.innerHTML = '<tr><td colspan="7" style="text-align:center;color:var(--muted);padding:24px">Chưa có biến thể nào.</td></tr>';
            } else {
                tb.innerHTML = data.map(function(v, idx){
                    var bcId = 'v-bc-' + v.id;
                    return '<tr>'
                        + '<td class="text-center">' + (idx + 1) + '</td>'
                        + '<td><strong>' + v.mauSac + '</strong></td>'
                        + '<td><span class="badge badge-neutral">' + v.trongLuong + '</span></td>'
                        + '<td class="text-right" style="font-weight:700;">' + v.giaBan.toLocaleString('vi-VN') + ' đ</td>'
                        + '<td class="text-right" style="font-weight:700;">' + v.soLuong + '</td>'
                        + '<td><svg id="' + bcId + '" style="height:30px;"></svg><br><small style="font-family:var(--font-mono);font-weight:700;">' + (v.barcode || '') + '</small></td>'
                        + '<td class="text-center">'
                        + '<div style="display:inline-flex;gap:4px;">'
                        + '<button type="button" class="btn btn-outline btn-sm" style="height:26px;padding:0 8px;" onclick="openEditVariant(' + v.id + ',\'' + v.mauSac + '\',\'' + v.trongLuong + '\',' + v.giaBan + ',' + v.soLuong + ')">Sửa</button>'
                        + '<form method="post" action="${pageContext.request.contextPath}/ProductServlet" style="display:inline;margin:0;" onsubmit="return confirm(\'Xóa biến thể này?\');"><input type="hidden" name="action" value="deleteVariant"><input type="hidden" name="idBT" value="' + v.id + '"><button type="submit" class="btn btn-ghost btn-sm" style="height:26px;padding:0 6px;color:var(--negative)!important;">Xóa</button></form>'
                        + '</div>'
                        + '</td>'
                        + '</tr>';
                }).join('');

                setTimeout(function(){
                    data.forEach(function(v){
                        if (v.barcode && document.getElementById('v-bc-' + v.id)) {
                            try {
                                JsBarcode('#v-bc-' + v.id, v.barcode, { format: 'CODE128', width: 1.3, height: 26, displayValue: false, margin: 2 });
                            } catch(e) {}
                        }
                    });
                }, 40);
            }
        });
    openModal('modalVariant');
}

function openEditVariant(id, mau, tl, gia, sl) {
    document.getElementById('vefIdBT').value = id;
    document.getElementById('vefMauSac').value = mau;
    document.getElementById('vefTrongLuong').value = tl;
    document.getElementById('vefGiaBan').value = gia;
    document.getElementById('vefSoLuong').value = sl;
    document.getElementById('variantEditForm').style.display = 'block';
    document.getElementById('variantForm').style.display = 'none';
}

function validateVariant(type) {
    var mau, tl, idBT;
    if (type === 'add') {
        mau = document.querySelector('#variantForm select[name="mauSac"]').value;
        tl = document.querySelector('#variantForm select[name="trongLuong"]').value;
        var exist = window.currentVariants.some(function(v){ return v.mauSac === mau && v.trongLuong === tl; });
        if (exist) {
            alert('Biến thể Màu "' + mau + '" - Size "' + tl + '" đã tồn tại cho sản phẩm này!');
            return false;
        }
    } else {
        idBT = document.getElementById('vefIdBT').value;
        mau = document.getElementById('vefMauSac').value;
        tl = document.getElementById('vefTrongLuong').value;
        var exist = window.currentVariants.some(function(v){ return String(v.id) !== String(idBT) && v.mauSac === mau && v.trongLuong === tl; });
        if (exist) {
            alert('Biến thể Màu "' + mau + '" - Size "' + tl + '" đã tồn tại cho sản phẩm này!');
            return false;
        }
    }
    return true;
}

// IN TEM MÁC BARCODE DUNA
function openPrintTagModal(id, maSP, tenSP, giaBan) {
    currentSpData = { id: id, maSP: maSP, tenSP: tenSP, giaBan: Number(giaBan) };
    document.getElementById('tagPrevName').textContent = tenSP;
    document.getElementById('tagPrevPrice').textContent = Number(giaBan).toLocaleString('vi-VN') + ' VNĐ';

    var sel = document.getElementById('tagSelectVariant');
    sel.innerHTML = '<option value="">Đang tải danh sách size/màu...</option>';

    fetch('${pageContext.request.contextPath}/ProductServlet?ajaxVariant=1&idSP=' + id)
        .then(function(r){ return r.json(); })
        .then(function(data){
            window.currentVariants = data || [];
            if (!data || data.length === 0) {
                sel.innerHTML = '<option value="">Sản phẩm chưa có biến thể size/màu cụ thể</option>';
                renderTagPreviewSingle(maSP, 'Tiêu chuẩn', 'L', Number(giaBan));
            } else {
                sel.innerHTML = data.map(function(v, idx){
                    return '<option value="' + v.id + '" ' + (idx === 0 ? 'selected' : '') + '>'
                        + 'Màu: ' + v.mauSac + ' - Size: ' + v.trongLuong + ' (' + v.giaBan.toLocaleString('vi-VN') + ' đ - Barcode: ' + v.barcode + ')'
                        + '</option>';
                }).join('');
                renderTagPreview();
            }
        });

    openModal('modalPrintTag');
}

function renderTagPreview() {
    var sel = document.getElementById('tagSelectVariant');
    var vId = sel.value;
    var v = window.currentVariants.find(function(item){ return String(item.id) === String(vId); });
    if (!v) {
        if (window.currentVariants.length > 0) v = window.currentVariants[0];
        else return;
    }

    document.getElementById('tagPrevColor').textContent = v.mauSac;
    document.getElementById('tagPrevSize').textContent = v.trongLuong;
    document.getElementById('tagPrevPrice').textContent = v.giaBan.toLocaleString('vi-VN') + ' VNĐ';
    document.getElementById('tagBarcodeText').textContent = v.barcode || (currentSpData.maSP + 'BT' + v.id);

    try {
        JsBarcode('#tagBarcodeSvg', v.barcode || (currentSpData.maSP + 'BT' + v.id), {
            format: 'CODE128',
            width: 1.8,
            height: 48,
            displayValue: false,
            margin: 4
        });
    } catch(e) {}
}

function renderTagPreviewSingle(barcode, color, size, price) {
    document.getElementById('tagPrevColor').textContent = color;
    document.getElementById('tagPrevSize').textContent = size;
    document.getElementById('tagPrevPrice').textContent = price.toLocaleString('vi-VN') + ' VNĐ';
    document.getElementById('tagBarcodeText').textContent = barcode;
    try {
        JsBarcode('#tagBarcodeSvg', barcode, {
            format: 'CODE128',
            width: 1.8,
            height: 48,
            displayValue: false,
            margin: 4
        });
    } catch(e) {}
}
</script>
<jsp:include page="footer.jsp" />
</body>
</html>
