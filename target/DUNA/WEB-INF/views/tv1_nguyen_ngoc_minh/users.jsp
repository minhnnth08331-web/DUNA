<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Quản lý Tài khoản &amp; Bảo mật — DUNA Men's Wear" scope="request"/>
<c:set var="activeNav" value="uchub" scope="request"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/navbar.jspf" %>

<div class="page-container">
    <%@ include file="/WEB-INF/views/common/alerts.jspf" %>
    <c:if test="${param.msg == 'created'}"><div class="alert alert-success">Đã tạo mới tài khoản &amp; hồ sơ khách hàng liên kết 1:1 thành công.</div></c:if>
    <c:if test="${param.msg == 'weak_password'}"><div class="alert alert-danger">Mật khẩu phải tối thiểu 6 ký tự và chứa cả chữ cái lẫn chữ số!</div></c:if>
    <c:if test="${param.msg == 'duplicate_user'}"><div class="alert alert-danger">Email hoặc số điện thoại đã tồn tại trên hệ thống.</div></c:if>
    <c:if test="${param.msg == 'has_orders'}"><div class="alert alert-warning">Không thể xóa tài khoản đã phát sinh đơn hàng hoặc hồ sơ nhân sự. Hãy dùng nút Khóa TK để giữ toàn vẹn dữ liệu!</div></c:if>
    <c:if test="${param.msg == 'token'}"><div class="alert alert-success">Đã tạo Token đặt lại mật khẩu (UC019): <code>${param.token}</code> (hiệu lực 30 phút).</div></c:if>

    <div class="page-header">
        <div>
            <h1 class="page-title">${resultTitle}</h1>
            <p class="page-subtitle">M01 • Trung tâm định danh đăng nhập hợp nhất (<code>UC011–UC027</code>), bảo mật tài khoản, sổ địa chỉ &amp; nhật ký kiểm toán</p>
        </div>
        <div class="action-buttons">
            <button type="button" class="btn btn-primary btn-sm" onclick="openModal('modalCreateUser')">+ Tạo Tài khoản (UC014)</button>
            <a href="${pageContext.request.contextPath}/CustomerServlet" class="btn btn-outline btn-sm">Hồ sơ Khách hàng (M02)</a>
            <a href="${pageContext.request.contextPath}/EmployeeManagementServlet" class="btn btn-outline btn-sm">Hồ sơ Nhân sự (M15)</a>
        </div>
    </div>

    <!-- KPI tổng quan tài khoản -->
    <div class="kpi-grid" style="margin-bottom: 16px;">
        <div class="stat-card">
            <div class="stat-label">Tổng tài khoản đăng nhập</div>
            <div class="stat-value">${not empty userStats.totalUsers ? userStats.totalUsers : 0}</div>
            <div class="stat-sub">Admin, Quản lý, Nhân viên &amp; Khách hàng</div>
        </div>
        <div class="stat-card">
            <div class="stat-label">Quản trị &amp; Nhân sự nội bộ</div>
            <div class="stat-value">${(not empty userStats.adminMgrCount ? userStats.adminMgrCount : 0) + (not empty userStats.staffCount ? userStats.staffCount : 0)}</div>
            <div class="stat-sub">${userStats.adminMgrCount} Quản lý/Admin • ${userStats.staffCount} Thu ngân POS</div>
        </div>
        <div class="stat-card">
            <div class="stat-label">Tài khoản Khách hàng</div>
            <div class="stat-value">${not empty userStats.customerCount ? userStats.customerCount : 0}</div>
            <div class="stat-sub">Liên kết 1:1 với 600 hồ sơ CRM</div>
        </div>
        <div class="stat-card">
            <div class="stat-label">Tài khoản đang khóa</div>
            <div class="stat-value" style="color:var(--negative);">${not empty userStats.lockedCount ? userStats.lockedCount : 0}</div>
            <div class="stat-sub">Tự khóa khi sai MK 5 lần (UC016) hoặc khóa tay</div>
        </div>
    </div>

    <!-- Thanh chuyển Tab UC011 - UC027 -->
    <div class="action-buttons" style="margin-bottom: 14px; flex-wrap: wrap;">
        <a href="${pageContext.request.contextPath}/users?uc=UC011"
           class="btn ${activeUc == 'UC011' || activeUc == 'UC012' || activeUc == 'UC013' ? 'btn-primary' : 'btn-outline'} btn-sm">
            1. Danh sách Tài khoản (UC011–022)
        </a>
        <a href="${pageContext.request.contextPath}/users?uc=UC023"
           class="btn ${activeUc == 'UC023' ? 'btn-primary' : 'btn-outline'} btn-sm">
            2. Sổ địa chỉ nhận hàng (UC023–026)
        </a>
        <a href="${pageContext.request.contextPath}/users?uc=UC015"
           class="btn ${activeUc == 'UC015' ? 'btn-primary' : 'btn-outline'} btn-sm">
            3. Lịch sử Đăng nhập (UC015)
        </a>
        <a href="${pageContext.request.contextPath}/users?uc=UC016&onlyFailed=1"
           class="btn ${activeUc == 'UC016' ? 'btn-primary' : 'btn-outline'} btn-sm">
            4. Cảnh báo Đăng nhập sai (UC016)
        </a>
        <a href="${pageContext.request.contextPath}/users?uc=UC027"
           class="btn ${activeUc == 'UC027' ? 'btn-primary' : 'btn-outline'} btn-sm">
            5. Nhật ký Kiểm toán Audit Logs (UC027)
        </a>
    </div>

    <!-- Thanh lọc riêng theo từng Tab -->
    <c:choose>
        <c:when test="${activeUc == 'UC023'}">
            <div class="toolbar-section" style="margin-bottom:14px;">
                <form method="get" action="${pageContext.request.contextPath}/users" class="filter-form" style="width:100%;">
                    <input type="hidden" name="uc" value="UC023">
                    <label style="font-size:12.5px;font-weight:600;color:var(--muted);">Lọc theo Mã TK:</label>
                    <input type="number" name="userId" class="form-control" style="max-width:140px;" value="${selectedUserId > 0 ? selectedUserId : ''}" placeholder="Tất cả TK">
                    <button type="submit" class="btn btn-primary btn-sm">Xem Sổ địa chỉ (UC023)</button>
                    <button type="button" class="btn btn-secondary btn-sm" onclick="openAddAddress(${selectedUserId > 0 ? selectedUserId : 4})">+ Thêm địa chỉ mới (UC024)</button>
                </form>
            </div>
        </c:when>
        <c:when test="${activeUc == 'UC015' || activeUc == 'UC016'}">
            <div class="toolbar-section" style="margin-bottom:14px;">
                <form method="get" action="${pageContext.request.contextPath}/users" class="filter-form" style="width:100%;">
                    <input type="hidden" name="uc" value="${activeUc}">
                    <div class="search-box-wrap">
                        <input type="text" name="keyword" class="search-box" placeholder="Tìm theo email đăng nhập hoặc họ tên..." value="${keyword}">
                    </div>
                    <button type="submit" class="btn btn-primary btn-sm">Tra cứu lịch sử</button>
                </form>
            </div>
        </c:when>
        <c:when test="${activeUc == 'UC027'}">
            <div class="toolbar-section" style="margin-bottom:14px;">
                <form method="get" action="${pageContext.request.contextPath}/users" class="filter-form" style="width:100%;">
                    <input type="hidden" name="uc" value="UC027">
                    <div class="search-box-wrap">
                        <input type="text" name="keyword" class="search-box" placeholder="Lọc hành động (HR_LOCK, RESET_PASSWORD...) hoặc tên người thao tác..." value="${keyword}">
                    </div>
                    <input type="date" name="fromDate" class="form-control" style="max-width:155px;" value="${fromDate}">
                    <input type="date" name="toDate" class="form-control" style="max-width:155px;" value="${toDate}">
                    <button type="submit" class="btn btn-primary btn-sm">Lọc Audit Logs (UC027)</button>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div class="toolbar-section" style="margin-bottom:14px;">
                <form method="get" action="${pageContext.request.contextPath}/users" class="filter-form" style="width:100%;">
                    <input type="hidden" name="uc" value="UC013">
                    <div class="search-box-wrap">
                        <input type="text" name="keyword" class="search-box" placeholder="Tìm Email, SĐT, Họ tên hoặc Mã KH/NV (VD: admin, QL001, KH000600)..." value="${keyword}">
                    </div>
                    <select name="roleCode" class="form-control" style="width:auto;min-width:170px;">
                        <option value="" ${empty roleCode ? 'selected' : ''}>— Tất cả vai trò —</option>
                        <option value="ADMIN" ${roleCode == 'ADMIN' ? 'selected' : ''}>ADMIN (Quản trị)</option>
                        <option value="MANAGER" ${roleCode == 'MANAGER' ? 'selected' : ''}>MANAGER (Quản lý)</option>
                        <option value="STAFF" ${roleCode == 'STAFF' ? 'selected' : ''}>STAFF (Nhân viên POS)</option>
                        <option value="CUSTOMER" ${roleCode == 'CUSTOMER' ? 'selected' : ''}>CUSTOMER (Khách hàng)</option>
                    </select>
                    <select name="status" class="form-control" style="width:auto;min-width:155px;">
                        <option value="" ${empty statusFilter ? 'selected' : ''}>— Tất cả trạng thái —</option>
                        <option value="ACTIVE" ${statusFilter == 'ACTIVE' ? 'selected' : ''}>Đang hoạt động (ACTIVE)</option>
                        <option value="LOCKED" ${statusFilter == 'LOCKED' ? 'selected' : ''}>Đã khóa (LOCKED)</option>
                    </select>
                    <button type="submit" class="btn btn-primary btn-sm">Lọc (UC012–013)</button>
                    <a href="${pageContext.request.contextPath}/users?uc=UC011" class="btn btn-outline btn-sm">Làm mới</a>
                </form>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- Nội dung chính từng Tab -->
    <c:choose>
        <c:when test="${activeUc == 'UC023'}">
            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Chủ tài khoản</th>
                        <th>Người nhận</th>
                        <th>SĐT nhận</th>
                        <th>Địa chỉ chi tiết</th>
                        <th>Phường/Xã — Tỉnh/TP</th>
                        <th>Loại</th>
                        <th style="text-align:center;">Thao tác (UC025–026)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${resultRows}" var="a">
                        <tr>
                            <td>#${a.address_id}</td>
                            <td><code>#${a.user_id}</code> ${a.full_name}</td>
                            <td style="font-weight:600;">${a.receiver_name}</td>
                            <td>${a.receiver_phone}</td>
                            <td>${a.address_line}</td>
                            <td>${not empty a.ward_name ? a.ward_name : ''} ${not empty a.province_name ? '— '.concat(a.province_name) : ''}</td>
                            <td><span class="badge ${a.is_default == 'Mặc định' ? 'badge-success' : 'badge-neutral'}">${a.is_default}</span></td>
                            <td style="text-align:center;white-space:nowrap;">
                                <c:if test="${a.is_default != 'Mặc định'}">
                                    <form method="post" action="${pageContext.request.contextPath}/users" style="display:inline;">
                                        <input type="hidden" name="action" value="setDefaultAddress">
                                        <input type="hidden" name="userId" value="${a.user_id}">
                                        <input type="hidden" name="addressId" value="${a.address_id}">
                                        <button type="submit" class="btn btn-outline btn-sm" style="height:26px;padding:0 8px;">Đặt mặc định</button>
                                    </form>
                                    <form method="post" action="${pageContext.request.contextPath}/users" style="display:inline;" onsubmit="return confirm('Xóa địa chỉ này?');">
                                        <input type="hidden" name="action" value="deleteAddress">
                                        <input type="hidden" name="userId" value="${a.user_id}">
                                        <input type="hidden" name="addressId" value="${a.address_id}">
                                        <button type="submit" class="btn btn-ghost btn-sm" style="height:26px;padding:0 8px;color:var(--negative)!important;">Xóa</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty resultRows}">
                        <tr><td colspan="8" style="text-align:center;padding:28px;color:var(--muted);">Chưa có địa chỉ nào được lưu.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </c:when>

        <c:when test="${activeUc == 'UC015' || activeUc == 'UC016' || activeUc == 'UC027'}">
            <%@ include file="/WEB-INF/views/common/result-table.jspf" %>
        </c:when>

        <c:otherwise>
            <div class="table-wrap">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>Mã TK</th>
                        <th>Hồ sơ liên kết</th>
                        <th>Email đăng nhập</th>
                        <th>SĐT</th>
                        <th>Họ và tên</th>
                        <th>Vai trò</th>
                        <th>Hạng / Chức danh</th>
                        <th>Đăng nhập cuối</th>
                        <th>Trạng thái</th>
                        <th style="text-align:center;">Thao tác (UC017–026)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${resultRows}" var="u">
                        <tr>
                            <td style="font-weight:700;">#${u.user_id}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.role_code == 'CUSTOMER' && u.linked_code != '-'}">
                                        <a href="${pageContext.request.contextPath}/CustomerServlet?search=${u.linked_code}" class="badge badge-neutral"><code>${u.linked_code}</code></a>
                                    </c:when>
                                    <c:when test="${u.linked_code != '-' and not empty u.linked_emp_id}">
                                        <a href="${pageContext.request.contextPath}/EmployeeDetailServlet?id=${u.linked_emp_id}&mode=view" class="badge badge-info" title="Mở hồ sơ nhân sự ${u.linked_code}"><code>${u.linked_code}</code></a>
                                    </c:when>
                                    <c:when test="${u.linked_code != '-'}">
                                        <a href="${pageContext.request.contextPath}/EmployeeManagementServlet?searchQuery=${u.linked_code}" class="badge badge-info"><code>${u.linked_code}</code></a>
                                    </c:when>
                                    <c:otherwise>—</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${u.email}</td>
                            <td>${not empty u.phone ? u.phone : '—'}</td>
                            <td style="font-weight:600;">${u.full_name}</td>
                            <td><span class="badge ${u.role_code == 'ADMIN' || u.role_code == 'MANAGER' ? 'badge-warning' : 'badge-neutral'}">${u.role_code}</span></td>
                            <td>${u.linked_info}</td>
                            <td style="font-size:12px;color:var(--muted);">${not empty u.last_login_at ? u.last_login_at : 'Chưa đăng nhập'}</td>
                            <td>
                                <span class="badge ${u.status == 'ACTIVE' ? 'badge-success' : 'badge-danger'}">${u.status}</span>
                            </td>
                            <td style="text-align:center; white-space:nowrap;">
                                <div style="display:inline-flex; gap:4px; flex-wrap:nowrap;">
                                    <button type="button" class="btn btn-outline btn-sm" style="height:26px;padding:0 7px;"
                                            onclick="openEditUser('${u.user_id}','${u.full_name}','${u.phone}','${u.email}')">Sửa / MK</button>
                                    <a href="${pageContext.request.contextPath}/users?uc=UC023&userId=${u.user_id}" class="btn btn-outline btn-sm" style="height:26px;padding:0 7px;">Địa chỉ</a>
                                    <a href="${pageContext.request.contextPath}/users?uc=UC015&keyword=${u.email}" class="btn btn-outline btn-sm" style="height:26px;padding:0 7px;">Log ĐN</a>
                                    <form method="post" action="${pageContext.request.contextPath}/users" style="display:inline;margin:0;">
                                        <input type="hidden" name="action" value="toggleStatus">
                                        <input type="hidden" name="userId" value="${u.user_id}">
                                        <button type="submit" class="btn btn-outline btn-sm" style="height:26px;padding:0 7px;">
                                            ${u.status == 'ACTIVE' ? 'Khóa' : 'Mở'}
                                        </button>
                                    </form>
                                    <form method="post" action="${pageContext.request.contextPath}/users" style="display:inline;margin:0;" onsubmit="return confirm('Xóa tài khoản #${u.user_id} (chỉ xóa được khi chưa có đơn hàng)?');">
                                        <input type="hidden" name="action" value="deleteUser">
                                        <input type="hidden" name="userId" value="${u.user_id}">
                                        <button type="submit" class="btn btn-ghost btn-sm" style="height:26px;padding:0 6px;color:var(--negative)!important;">Xóa</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty resultRows}">
                        <tr><td colspan="10" style="text-align:center;padding:28px;color:var(--muted);">Chưa có tài khoản phù hợp bộ lọc.</td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>

            <c:if test="${totalPages > 1}">
                <c:set var="uParams" value="&uc=UC013${not empty roleCode ? '&roleCode='.concat(roleCode) : ''}${not empty statusFilter ? '&status='.concat(statusFilter) : ''}${not empty keyword ? '&keyword='.concat(keyword) : ''}" />
                <div class="pagination-bar" style="margin-top:16px;">
                    <c:if test="${currentPage > 1}">
                        <a href="?page=${currentPage - 1}${uParams}">&#10094;</a>
                    </c:if>
                    <c:forEach begin="${currentPage - 3 > 1 ? currentPage - 3 : 1}" end="${currentPage + 3 < totalPages ? currentPage + 3 : totalPages}" var="i">
                        <a href="?page=${i}${uParams}" class="${currentPage == i ? 'active' : ''}">${i}</a>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <a href="?page=${currentPage + 1}${uParams}">&#10095;</a>
                    </c:if>
                    <span style="margin-left:10px; font-size:12.5px; color:var(--muted);">Trang ${currentPage} / ${totalPages}</span>
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>

<!-- MODAL TẠO TÀI KHOẢN MỚI (UC014) -->
<div class="modal-bg" id="modalCreateUser">
    <div class="modal-box" style="max-width:520px;">
        <button type="button" class="close-btn" onclick="closeModal('modalCreateUser')">&times;</button>
        <div class="modal-header"><h3 class="modal-title">Tạo Tài Khoản Khách Hàng Mới (UC014)</h3></div>
        <form method="post" action="${pageContext.request.contextPath}/users">
            <input type="hidden" name="action" value="register">
            <div class="modal-body">
                <div class="form-group"><label>Họ và tên *</label><input type="text" name="fullName" class="form-control" required placeholder="Nguyễn Văn A"></div>
                <div class="form-group"><label>Email đăng nhập *</label><input type="email" name="email" class="form-control" required placeholder="email@duna-member.vn"></div>
                <div class="form-group"><label>Số điện thoại *</label><input type="text" name="phone" class="form-control" required placeholder="09xxxxxxxx"></div>
                <div class="form-group"><label>Mật khẩu (>= 6 ký tự, có chữ &amp; số) *</label><input type="password" name="password" class="form-control" required placeholder="VD: Duna123"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="closeModal('modalCreateUser')">Hủy</button>
                <button type="submit" class="btn btn-primary">Tạo Tài Khoản</button>
            </div>
        </form>
    </div>
</div>

<!-- MODAL SỬA HỒ SƠ & ĐẶT LẠI MẬT KHẨU (UC017, UC018, UC019, UC020) -->
<div class="modal-bg" id="modalEditUser">
    <div class="modal-box" style="max-width:540px;">
        <button type="button" class="close-btn" onclick="closeModal('modalEditUser')">&times;</button>
        <div class="modal-header"><h3 class="modal-title">Cập nhật Tài khoản &amp; Mật khẩu (UC017–UC020)</h3></div>
        <form method="post" action="${pageContext.request.contextPath}/users">
            <input type="hidden" name="action" value="updateProfile">
            <input type="hidden" name="userId" id="euId">
            <div class="modal-body">
                <div class="form-group"><label>Họ và tên *</label><input type="text" name="fullName" id="euName" class="form-control" required></div>
                <div class="form-group"><label>Email *</label><input type="email" name="email" id="euEmail" class="form-control" required></div>
                <div class="form-group"><label>Số điện thoại</label><input type="text" name="phone" id="euPhone" class="form-control"></div>
                <div class="form-group"><label>Đặt lại mật khẩu mới (bỏ trống nếu giữ nguyên)</label><input type="password" name="newPassword" class="form-control" placeholder=">= 6 ký tự gồm chữ và số"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="closeModal('modalEditUser')">Đóng</button>
                <button type="submit" class="btn btn-primary">Lưu &amp; Đồng bộ</button>
            </div>
        </form>
        <form method="post" action="${pageContext.request.contextPath}/users" style="padding:0 20px 16px;">
            <input type="hidden" name="action" value="createResetToken">
            <input type="hidden" name="email" id="euTokenEmail">
            <button type="submit" class="btn btn-outline btn-sm" style="width:100%;">Sinh Token Quên mật khẩu (UC019)</button>
        </form>
    </div>
</div>

<!-- MODAL THÊM ĐỊA CHỈ (UC024) -->
<div class="modal-bg" id="modalAddAddress">
    <div class="modal-box" style="max-width:540px;">
        <button type="button" class="close-btn" onclick="closeModal('modalAddAddress')">&times;</button>
        <div class="modal-header"><h3 class="modal-title">Thêm Địa Chỉ Nhận Hàng Mới (UC024)</h3></div>
        <form method="post" action="${pageContext.request.contextPath}/users">
            <input type="hidden" name="action" value="addAddress">
            <div class="modal-body">
                <div class="form-group"><label>Mã Tài khoản (userId) *</label><input type="number" name="userId" id="addrUserId" class="form-control" required></div>
                <div class="form-group"><label>Tên người nhận *</label><input type="text" name="receiverName" class="form-control" required></div>
                <div class="form-group"><label>Số điện thoại nhận *</label><input type="text" name="receiverPhone" class="form-control" required></div>
                <div class="form-group">
                    <label>Tỉnh / Thành phố *</label>
                    <select name="provinceId" id="addrProv" class="form-control" onchange="loadWardsForProv(this.value)">
                        <c:forEach items="${provinces}" var="p"><option value="${p.province_id}">${p.province_name}</option></c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Phường / Xã</label>
                    <select name="wardId" id="addrWard" class="form-control">
                        <c:forEach items="${wards}" var="w"><option value="${w.ward_id}">${w.ward_name}</option></c:forEach>
                    </select>
                </div>
                <div class="form-group"><label>Số nhà / Đường / Thôn *</label><input type="text" name="addressLine" class="form-control" required placeholder="Số 12 Nguyễn Trãi..."></div>
                <label style="display:flex;align-items:center;gap:8px;cursor:pointer;"><input type="checkbox" name="isDefault" value="1" checked> Đặt làm địa chỉ mặc định</label>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline" onclick="closeModal('modalAddAddress')">Hủy</button>
                <button type="submit" class="btn btn-primary">Lưu Địa Chỉ</button>
            </div>
        </form>
    </div>
</div>

<script>
function openModal(id) { var el = document.getElementById(id); if (el) el.classList.add('open'); }
function closeModal(id) { var el = document.getElementById(id); if (el) el.classList.remove('open'); }
function openEditUser(id, name, phone, email) {
    document.getElementById('euId').value = id;
    document.getElementById('euName').value = name || '';
    document.getElementById('euPhone').value = phone || '';
    document.getElementById('euEmail').value = email || '';
    document.getElementById('euTokenEmail').value = email || '';
    openModal('modalEditUser');
}
function openAddAddress(uid) {
    document.getElementById('addrUserId').value = uid || 4;
    openModal('modalAddAddress');
}
function loadWardsForProv(pid) {
    var sel = document.getElementById('addrWard');
    if (!sel) return;
    fetch('${pageContext.request.contextPath}/users?ajax=wards&provinceId=' + encodeURIComponent(pid))
        .then(function(r){ return r.json(); })
        .then(function(list){
            sel.innerHTML = (list || []).map(function(w){ return '<option value="' + w.ward_id + '">' + w.ward_name + '</option>'; }).join('');
        });
}
</script>
<jsp:include page="/footer.jsp" />
</body>
</html>
