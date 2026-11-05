<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Nếu truy cập trực tiếp JSP mà chưa có data -> tự redirect sang Servlet --%>
<%
    if (request.getAttribute("listNV") == null) {
        response.sendRedirect(request.getContextPath() + "/EmployeeManagementServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý nhân sự — DUNA Men's Wear</title>
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
            <a href="${pageContext.request.contextPath}/EmployeeManagementServlet" class="active">Quản lý nhân sự</a>
            <a href="${pageContext.request.contextPath}/OrderServlet?view=report">Báo cáo thống kê</a>
            <a href="${pageContext.request.contextPath}/CustomerServlet">Quản lý khách hàng</a>
            <a href="${pageContext.request.contextPath}/OrderServlet?view=sell">Bán hàng</a>
            <a href="${pageContext.request.contextPath}/OrderServlet">Quản lý hóa đơn</a>
            <a href="${pageContext.request.contextPath}/VoucherServlet">Quản lý Voucher</a>
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
        <div class="page-header">
            <div>
                <h1 class="page-title">Quản Lý Nhân Sự & Phân Quyền</h1>
                <p class="page-subtitle">Quản lý hồ sơ nhân viên, phân quyền truy cập hệ thống và trạng thái hoạt động</p>
            </div>
        </div>

        <div class="toolbar-section">
            <div class="filter-form">
                <form action="EmployeeManagementServlet" method="POST" style="display:flex; align-items:center; width:100%; margin:0;">
                    <div class="search-box-wrap">
                        <input type="text" name="searchQuery" class="search-box" placeholder="Tìm kiếm theo Tên hoặc Mã nhân viên (VD: Admin, NV001, QL001...) [Ấn Enter]" value="${param.searchQuery}">
                    </div>
                </form>
            </div>
            <div class="action-buttons">
                <% if(("MANAGER").equals(session.getAttribute("role"))) { %>
                    <a href="EmployeeManagementServlet?statusFilter=1" class="btn btn-outline btn-sm">
                        Hoạt Động
                    </a>
                    <a href="EmployeeManagementServlet?statusFilter=2" class="btn btn-outline btn-sm">
                        Đã Khóa
                    </a>
                    <a href="EmployeeManagementServlet" class="btn btn-outline btn-sm">
                        Tất Cả
                    </a>
                    <a href="add-employee.jsp" class="btn btn-primary btn-sm">+ Thêm Nhân Viên</a>
                <% } %>
            </div>
        </div>

        <div id="ajax-content-area" style="transition: opacity 0.2s ease;">
            <!-- Danh sách Quản lý / Admin -->
            <div style="margin-top: 14px; margin-bottom: 12px; display: flex; align-items: center; justify-content: space-between;">
                <div>
                    <h2 style="font-size: 16px; font-weight: 700; color: var(--text-primary); margin: 0;">Danh Sách Quản Lý & Admin</h2>
                    <span style="font-size: 12.5px; color: var(--muted);">Toàn quyền quản trị hệ thống, nhân sự, kho hàng và báo cáo tài chính</span>
                </div>
                <span class="badge badge-neutral">Quản trị viên</span>
            </div>

            <div class="table-wrap" style="margin-bottom: 28px;">
                <table class="styled-table">
                    <thead>
                        <tr>
                            <th style="width: 14%;">Mã QL</th>
                            <th style="width: 26%;">Họ và Tên</th>
                            <th style="width: 16%;">Ngày Sinh</th>
                            <th style="width: 16%;">Trạng thái</th>
                            <th style="width: 18%;">Lý do khóa</th>
                            <th style="width: 10%; text-align: center;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${listQuanLy}" var="ql">
                            <tr>
                                <td style="font-weight: 700; font-family: var(--font-mono); color: var(--text-primary);">${ql.maNV}</td>
                                <td style="font-weight: 600;">${ql.ten}</td>
                                <td>${ql.ngaySinh}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ql.trangThai == 'Hoạt động'}">
                                            <span class="badge badge-success">${ql.trangThai}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-danger">${ql.trangThai}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="color: var(--negative); font-size: 12.5px;">${ql.lyDo}</td>
                                <td style="white-space: nowrap; text-align: center;">
                                    <div style="display: inline-flex; gap: 8px;">
                                        <a href="EmployeeDetailServlet?id=${ql.id}&mode=view" class="btn btn-outline btn-sm" title="Xem chi tiết" style="padding: 0 8px; height: 28px;">
                                            Xem
                                        </a>
                                        <a href="EmployeeDetailServlet?id=${ql.id}&mode=edit" class="btn btn-outline btn-sm" title="Sửa thông tin" style="padding: 0 8px; height: 28px;">
                                            Sửa
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listQuanLy}">
                            <tr>
                                <td colspan="6" style="text-align:center; color:var(--muted); padding:28px;">
                                    Không có tài khoản Quản lý/Admin nào phù hợp.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- Danh sách Nhân viên Bán hàng -->
            <div style="margin-top: 20px; margin-bottom: 12px; display: flex; align-items: center; justify-content: space-between;">
                <div>
                    <h2 style="font-size: 16px; font-weight: 700; color: var(--text-primary); margin: 0;">Danh Sách Nhân Viên Bán Hàng</h2>
                    <span style="font-size: 12.5px; color: var(--muted);">Thực hiện bán hàng POS tại quầy, tạo đơn và tra cứu sản phẩm</span>
                </div>
                <span class="badge badge-neutral">Nhân viên POS</span>
            </div>

            <div class="table-wrap">
                <table class="styled-table">
                    <thead>
                        <tr>
                            <th style="width: 14%;">Mã NV</th>
                            <th style="width: 26%;">Họ và Tên</th>
                            <th style="width: 16%;">Ngày Sinh</th>
                            <th style="width: 16%;">Trạng thái</th>
                            <th style="width: 18%;">Lý do khóa</th>
                            <th style="width: 10%; text-align: center;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${listNV}" var="nv">
                            <tr>
                                <td style="font-weight: 700; font-family: var(--font-mono); color: var(--text-primary);">${nv.maNV}</td>
                                <td style="font-weight: 600;">${nv.ten}</td>
                                <td>${nv.ngaySinh}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${nv.trangThai == 'Hoạt động'}">
                                            <span class="badge badge-success">${nv.trangThai}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-danger">${nv.trangThai}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="color: var(--negative); font-size: 12.5px;">${nv.lyDo}</td>
                                <td style="white-space: nowrap; text-align: center;">
                                    <div style="display: inline-flex; gap: 8px;">
                                        <a href="EmployeeDetailServlet?id=${nv.id}&mode=view" class="btn btn-outline btn-sm" title="Xem chi tiết" style="padding: 0 8px; height: 28px;">
                                            Xem
                                        </a>
                                        <a href="EmployeeDetailServlet?id=${nv.id}&mode=edit" class="btn btn-outline btn-sm" title="Sửa thông tin" style="padding: 0 8px; height: 28px;">
                                            Sửa
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listNV}">
                            <tr>
                                <td colspan="6" style="text-align:center; color:var(--muted); padding:28px;">
                                    Không có tài khoản Nhân viên nào phù hợp.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <%-- Thanh phân trang --%>
            <c:if test="${totalPages > 1}">
                <c:set var="urlParams" value="${not empty param.searchQuery ? '&searchQuery='.concat(param.searchQuery) : ''}${not empty param.statusFilter ? '&statusFilter='.concat(param.statusFilter) : ''}" />
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
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>
