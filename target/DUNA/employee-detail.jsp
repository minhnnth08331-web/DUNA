<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String mode = (String) request.getAttribute("mode");
    if (mode == null || !mode.equals("edit")) {
        mode = "view";
    }
    boolean isEdit = "edit".equals(mode);
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= isEdit ? "Sửa thông tin nhân viên" : "Chi tiết nhân viên" %> — DUNA Men's Wear</title>
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
        <% if("MANAGER".equals(session.getAttribute("role"))) { %>
            <a href="ProductServlet">Quản lý sản phẩm</a>
            <a href="EmployeeManagementServlet" class="active">Quản lý nhân sự</a>
            <a href="OrderServlet?view=report">Báo cáo thống kê</a>
            <a href="CustomerServlet">Quản lý khách hàng</a>
            <a href="OrderServlet?view=sell">Bán hàng</a>
            <a href="OrderServlet">Quản lý hóa đơn</a>
            <a href="VoucherServlet">Quản lý Voucher</a>
        <% } else { %>
            <a href="#" class="active">Thông tin cá nhân</a>
            <a href="OrderServlet?view=sell">Bán hàng</a>
            <a href="OrderServlet">Quản lý hóa đơn</a>
        <% } %>
        </div>
        <div class="nav-right-cluster">
            <div class="nav-user">
                <span>Xin chào</span>
                <a href="EmployeeDetailServlet?id=${not empty sessionScope.nhanVienID ? sessionScope.nhanVienID : 1}&mode=view" class="nav-user-name">
                    ${not empty sessionScope.tenNV ? sessionScope.tenNV : 'Admin'}
                </a>
                <span class="nav-divider">|</span>
                <a href="LogoutServlet" class="nav-logout-btn">Đăng xuất</a>
            </div>
        </div>
    </div>

    <div class="page-container">
        <div class="page-header">
            <div>
                <h1 class="page-title">
                    <%= isEdit ? "Sửa Hồ Sơ Nhân Sự" : "Hồ Sơ Nhân Sự Chi Tiết" %>: ${nv.hoVaTen}
                </h1>
                <p class="page-subtitle">
                    <%= isEdit ? "Chế độ chỉnh sửa thông tin nhân viên trong hệ thống DUNA." : "Chế độ xem thông tin hồ sơ nhân sự, phân quyền và dữ liệu tài khoản." %>
                </p>
            </div>
            <% if (!isEdit && "MANAGER".equals(session.getAttribute("role"))) { %>
                <div class="action-buttons">
                    <a href="EmployeeDetailServlet?id=${nv.id}&mode=edit" class="btn btn-primary">
                        Chuyển Sang Chế Độ Sửa
                    </a>
                </div>
            <% } %>
        </div>

        <%
            String msg = (String) request.getAttribute("message");
            if (msg != null && !msg.trim().isEmpty()) {
                boolean isErr = msg.contains("LỖI") || msg.contains("Lỗi") || msg.startsWith("Lỗi") || msg.contains("thuoc ve nguoi khac");
        %>
            <div class="alert <%= isErr ? "alert-danger" : "alert-success" %>"><%= msg %></div>
        <% } %>

        <!-- Profile Hero Card -->
        <div class="panel" style="margin-bottom: 24px; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 16px;">
            <div style="display: flex; align-items: center; gap: 16px;">
                <div style="width: 52px; height: 52px; border-radius: var(--radius-control); background: var(--raised); border: 1px solid var(--border); display: flex; align-items: center; justify-content: center; font-size: 20px; font-weight: 800; color: var(--text-primary); font-family: var(--font-brand);">
                    ${nv.hoVaTen.substring(0, 1)}
                </div>
                <div>
                    <h2 style="font-size: 18px; font-weight: 700; color: var(--text-primary); margin: 0 0 4px 0;">${nv.hoVaTen}</h2>
                    <div style="font-size: 13px; color: var(--muted); display: flex; align-items: center; gap: 8px;">
                        <span>Mã nhân sự: <strong style="font-family: var(--font-mono); color: var(--text-primary);">${nv.maNV}</strong></span>
                        <span class="nav-divider">&bull;</span>
                        <span>Tài khoản: <strong style="color: var(--text-primary);">${nv.username}</strong></span>
                    </div>
                </div>
            </div>
            <div>
                <span class="badge ${nv.trangThai == 'Hoạt động' ? 'badge-success' : 'badge-danger'}" style="font-size: 13px; padding: 6px 14px;">
                    ${nv.trangThai}
                </span>
            </div>
        </div>

        <% if (!isEdit) { %>
            <!-- ======================= CHẾ ĐỘ XEM (READ-ONLY) ======================= -->
            
            <!-- Thông tin tài khoản -->
            <div class="detail-view-card">
                <h3>Thông Tin Tài Khoản & Phân Quyền</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <label>Tên đăng nhập (Username)</label>
                        <div class="info-value" style="font-weight: 700;">${nv.username}</div>
                    </div>
                    <div class="info-item">
                        <label>Mật khẩu đăng nhập</label>
                        <div class="info-value" style="display: flex; justify-content: space-between; align-items: center;">
                            <span id="displayPassword" style="font-weight: 700; letter-spacing: 3px; color: var(--negative);">••••••••</span>
                            <button type="button" id="btnTogglePass" onclick="onTogglePasswordClick('${nv.id}')" 
                                    class="btn btn-ghost btn-sm"
                                    title="Yêu cầu mật khẩu Admin để xem chi tiết mật khẩu">👁️ Hiện</button>
                        </div>
                    </div>
                    <div class="info-item">
                        <label>Mã nhân viên</label>
                        <div class="info-value" style="font-family: var(--font-mono);">${nv.maNV}</div>
                    </div>
                    <div class="info-item">
                        <label>Trạng thái tài khoản</label>
                        <div class="info-value">
                            <span class="badge ${nv.trangThai == 'Hoạt động' ? 'badge-success' : 'badge-danger'}">${nv.trangThai}</span>
                        </div>
                    </div>
                    <div class="info-item" style="grid-column: 1 / -1;">
                        <label>Ngày tạo tài khoản</label>
                        <div class="info-value">${not empty nv.ngayTao ? nv.ngayTao : '—'}</div>
                    </div>
                </div>
            </div>

            <!-- Thông tin cá nhân -->
            <div class="detail-view-card">
                <h3>Thông Tin Cá Nhân & Căn Cước Công Dân</h3>
                <div class="info-grid">
                    <div class="info-item" style="grid-column: 1 / -1;">
                        <label>Họ và tên</label>
                        <div class="info-value" style="font-weight: 700; font-size: 15px;">${nv.hoVaTen}</div>
                    </div>
                    <div class="info-item">
                        <label>Mã số CCCD / CMND</label>
                        <div class="info-value" style="font-family: var(--font-mono);">${not empty nv.cccd ? nv.cccd : '—'}</div>
                    </div>
                    <div class="info-item">
                        <label>Ngày sinh</label>
                        <div class="info-value">${not empty nv.ngaySinh ? nv.ngaySinh : '—'}</div>
                    </div>
                    <div class="info-item">
                        <label>Ngày cấp CCCD</label>
                        <div class="info-value">${not empty nv.ngayCapCccd ? nv.ngayCapCccd : '—'}</div>
                    </div>
                    <div class="info-item">
                        <label>Ngày hết hạn CCCD</label>
                        <div class="info-value">${not empty nv.ngayHetHanCccd ? nv.ngayHetHanCccd : '—'}</div>
                    </div>
                    <div class="info-item">
                        <label>Nơi cấp CCCD</label>
                        <div class="info-value">${not empty nv.noiCapCccd ? nv.noiCapCccd : '—'}</div>
                    </div>
                    <div class="info-item">
                        <label>Số điện thoại</label>
                        <div class="info-value">${not empty nv.soDienThoai ? nv.soDienThoai : '—'}</div>
                    </div>
                    <div class="info-item">
                        <label>Email</label>
                        <div class="info-value">${not empty nv.email ? nv.email : '—'}</div>
                    </div>
                    <div class="info-item">
                        <label>Quê quán</label>
                        <div class="info-value">${not empty nv.queQuan ? nv.queQuan : '—'}</div>
                    </div>
                    <div class="info-item">
                        <label>Địa chỉ thường trú</label>
                        <div class="info-value">${not empty nv.noiThuongTru ? nv.noiThuongTru : '—'}</div>
                    </div>
                    <div class="info-item" style="grid-column: 1 / -1;">
                        <label>Địa chỉ hiện tại (đang ở)</label>
                        <div class="info-value">${not empty nv.diaChiHienTai ? nv.diaChiHienTai : '—'}</div>
                    </div>
                </div>
            </div>

        <% } else { %>
            <!-- ======================= CHẾ ĐỘ SỬA (EDIT MODE) ======================= -->

            <form action="UpdateEmployeeServlet" method="POST" onsubmit="return validateEmployeeForm();" id="editForm">
                <input type="hidden" name="idNhanVien" value="${nv.id}">

                <!-- Layout 2 cột: Form trái, Actions phải -->
                <div style="display: grid; grid-template-columns: 1fr 300px; gap: 20px; align-items: start;">

                    <!-- CỘT TRÁI: Các card thông tin -->
                    <div>

                        <!-- Card 1: Thông tin nhận dạng -->
                        <div class="detail-view-card">
                            <h3>Thông Tin Nhận Dạng & CCCD</h3>
                            <div class="form-grid">
                                <div class="full">
                                    <label>Họ và tên <span style="color:var(--negative)">*</span></label>
                                    <input type="text" name="hoVaTen" class="form-control" value="${nv.hoVaTen}" required placeholder="Nhập họ và tên...">
                                </div>
                                <div>
                                    <label>Mã nhân viên (Không thể sửa)</label>
                                    <input type="text" name="maNV" class="form-control" value="${nv.maNV}" readonly placeholder="VD: NV001" title="Không được phép sửa Mã nhân viên">
                                </div>
                                <div>
                                    <label>Mã số CCCD <c:if test="${sessionScope.role != 'MANAGER'}"><span style="color:var(--muted); font-weight:400">(Không thể sửa)</span></c:if> <span style="color:var(--negative)">*</span></label>
                                    <input type="text" name="cccd" class="form-control" value="${nv.cccd}" ${sessionScope.role == 'MANAGER' ? '' : 'readonly'} required placeholder="12 chữ số" title="Mã số CCCD là bắt buộc">
                                </div>
                                <div>
                                    <label>Ngày cấp CCCD <span style="color:var(--negative)">*</span></label>
                                    <input type="date" name="ngayCapCccd" id="eNgayCapCccd" class="form-control" value="${nv.ngayCapCccd}" required title="Vui lòng chọn Ngày cấp CCCD">
                                </div>
                                <div>
                                    <label>Ngày hết hạn CCCD <span style="color:var(--negative)">*</span></label>
                                    <input type="date" name="ngayHetHanCccd" id="eNgayHetHanCccd" class="form-control" value="${nv.ngayHetHanCccd}" required title="Vui lòng chọn Ngày hết hạn CCCD">
                                    <div id="errEditCccdDate" style="color:var(--negative); font-size:12.5px; margin-top:4px;"></div>
                                </div>
                                <div class="full">
                                    <label>Nơi cấp CCCD <span style="color:var(--negative)">*</span></label>
                                    <select name="noiCapCccd" class="form-control" required title="Vui lòng chọn Nơi cấp CCCD">
                                        <option value="">— Chọn Nơi cấp CCCD / CMND —</option>
                                        <optgroup label="Cơ quan cấp Trung ương (Phổ biến nhất)">
                                            <option value="Cục Cảnh sát quản lý hành chính về trật tự xã hội" ${nv.noiCapCccd == 'Cục Cảnh sát quản lý hành chính về trật tự xã hội' ? 'selected' : ''}>Cục Cảnh sát quản lý hành chính về trật tự xã hội</option>
                                            <option value="Cục trưởng Cục Cảnh sát quản lý hành chính về trật tự xã hội" ${nv.noiCapCccd == 'Cục trưởng Cục Cảnh sát quản lý hành chính về trật tự xã hội' ? 'selected' : ''}>Cục trưởng Cục Cảnh sát quản lý hành chính về trật tự xã hội</option>
                                            <option value="Cục Cảnh sát Đăng ký, Quản lý Cư trú và Dữ liệu Quốc gia về Dân cư" ${nv.noiCapCccd == 'Cục Cảnh sát Đăng ký, Quản lý Cư trú và Dữ liệu Quốc gia về Dân cư' ? 'selected' : ''}>Cục Cảnh sát Đăng ký, Quản lý Cư trú và Dữ liệu Quốc gia về Dân cư</option>
                                            <option value="Cục Trưởng Cục Cảnh sát Đăng ký, quản lý cư trú và Dữ liệu quốc gia về dân cư" ${nv.noiCapCccd == 'Cục Trưởng Cục Cảnh sát Đăng ký, quản lý cư trú và Dữ liệu quốc gia về dân cư' ? 'selected' : ''}>Cục Trưởng Cục Cảnh sát Đăng ký, quản lý cư trú và Dữ liệu quốc gia về dân cư</option>
                                            <option value="Bộ Công An - Cục Cảnh sát QLHC về TTXH" ${nv.noiCapCccd == 'Bộ Công An - Cục Cảnh sát QLHC về TTXH' ? 'selected' : ''}>Bộ Công An - Cục Cảnh sát QLHC về TTXH</option>
                                        </optgroup>
                                        <optgroup label="Công an các Tỉnh / Thành phố">
                                            <option value="Công an Thành phố Hà Nội" ${nv.noiCapCccd == 'Công an Thành phố Hà Nội' ? 'selected' : ''}>Công an Thành phố Hà Nội</option>
                                            <option value="Cảnh sát QLHC về TTXH - Công an TP Hà Nội" ${nv.noiCapCccd == 'Cảnh sát QLHC về TTXH - Công an TP Hà Nội' ? 'selected' : ''}>Cảnh sát QLHC về TTXH - Công an TP Hà Nội</option>
                                            <option value="Công an Thành phố Hồ Chí Minh" ${nv.noiCapCccd == 'Công an Thành phố Hồ Chí Minh' ? 'selected' : ''}>Công an Thành phố Hồ Chí Minh</option>
                                            <option value="Cảnh sát QLHC về TTXH - Công an TP Hồ Chí Minh" ${nv.noiCapCccd == 'Cảnh sát QLHC về TTXH - Công an TP Hồ Chí Minh' ? 'selected' : ''}>Cảnh sát QLHC về TTXH - Công an TP Hồ Chí Minh</option>
                                            <option value="Công an Thành phố Đà Nẵng" ${nv.noiCapCccd == 'Công an Thành phố Đà Nẵng' ? 'selected' : ''}>Công an Thành phố Đà Nẵng</option>
                                            <option value="Công an Thành phố Hải Phòng" ${nv.noiCapCccd == 'Công an Thành phố Hải Phòng' ? 'selected' : ''}>Công an Thành phố Hải Phòng</option>
                                            <option value="Công an Thành phố Cần Thơ" ${nv.noiCapCccd == 'Công an Thành phố Cần Thơ' ? 'selected' : ''}>Công an Thành phố Cần Thơ</option>
                                            <option value="Công an Tỉnh Quảng Ninh" ${nv.noiCapCccd == 'Công an Tỉnh Quảng Ninh' ? 'selected' : ''}>Công an Tỉnh Quảng Ninh</option>
                                            <option value="Công an Tỉnh Thanh Hóa" ${nv.noiCapCccd == 'Công an Tỉnh Thanh Hóa' ? 'selected' : ''}>Công an Tỉnh Thanh Hóa</option>
                                            <option value="Công an Tỉnh Nghệ An" ${nv.noiCapCccd == 'Công an Tỉnh Nghệ An' ? 'selected' : ''}>Công an Tỉnh Nghệ An</option>
                                            <option value="Công an Tỉnh Hải Dương" ${nv.noiCapCccd == 'Công an Tỉnh Hải Dương' ? 'selected' : ''}>Công an Tỉnh Hải Dương</option>
                                            <option value="Công an Tỉnh Nam Định" ${nv.noiCapCccd == 'Công an Tỉnh Nam Định' ? 'selected' : ''}>Công an Tỉnh Nam Định</option>
                                            <option value="Công an Tỉnh Đồng Nai" ${nv.noiCapCccd == 'Công an Tỉnh Đồng Nai' ? 'selected' : ''}>Công an Tỉnh Đồng Nai</option>
                                            <option value="Công an Tỉnh Bình Dương" ${nv.noiCapCccd == 'Công an Tỉnh Bình Dương' ? 'selected' : ''}>Công an Tỉnh Bình Dương</option>
                                            <option value="Công an Tỉnh Khánh Hòa" ${nv.noiCapCccd == 'Công an Tỉnh Khánh Hòa' ? 'selected' : ''}>Công an Tỉnh Khánh Hòa</option>
                                            <option value="Công an Tỉnh Thừa Thiên Huế" ${nv.noiCapCccd == 'Công an Tỉnh Thừa Thiên Huế' ? 'selected' : ''}>Công an Tỉnh Thừa Thiên Huế</option>
                                            <option value="Công an Tỉnh Lâm Đồng" ${nv.noiCapCccd == 'Công an Tỉnh Lâm Đồng' ? 'selected' : ''}>Công an Tỉnh Lâm Đồng</option>
                                            <option value="Công an Tỉnh Quảng Nam" ${nv.noiCapCccd == 'Công an Tỉnh Quảng Nam' ? 'selected' : ''}>Công an Tỉnh Quảng Nam</option>
                                            <option value="Công an Tỉnh Vĩnh Phúc" ${nv.noiCapCccd == 'Công an Tỉnh Vĩnh Phúc' ? 'selected' : ''}>Công an Tỉnh Vĩnh Phúc</option>
                                            <option value="Công an Tỉnh Bắc Ninh" ${nv.noiCapCccd == 'Công an Tỉnh Bắc Ninh' ? 'selected' : ''}>Công an Tỉnh Bắc Ninh</option>
                                        </optgroup>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <!-- Card 2: Thông tin liên lạc & cá nhân -->
                        <div class="detail-view-card">
                            <h3>Thông Tin Liên Lạc & Địa Chỉ</h3>
                            <div class="form-grid">
                                <div>
                                    <label>Ngày sinh</label>
                                    <input type="date" name="ngaySinh" id="fDob" class="form-control" value="${nv.ngaySinh}">
                                    <div style="color: var(--negative); font-size: 12.5px; margin-top: 4px;" id="errDob"></div>
                                </div>
                                <div>
                                    <label>Số điện thoại</label>
                                    <div class="phone-wrap">
                                        <span class="phone-prefix">(+84)</span>
                                        <input type="tel" name="soDienThoai" id="fPhone" class="form-control"
                                            value="${not empty nv.soDienThoai && nv.soDienThoai.startsWith('0') ? nv.soDienThoai.substring(1) : nv.soDienThoai}"
                                            placeholder="912 345 678" maxlength="10">
                                    </div>
                                    <div style="color: var(--negative); font-size: 12.5px; margin-top: 4px;" id="errPhone"></div>
                                </div>
                                <div class="full">
                                    <label>Email</label>
                                    <input type="email" name="email" class="form-control" value="${nv.email}" placeholder="example@email.com">
                                </div>
                                <div>
                                    <label>Quê quán</label>
                                    <select name="queQuan" id="fQueQuan" class="form-control">
                                        <option value="">-- Chọn Quê quán --</option>
                                    </select>
                                </div>
                                <div>
                                    <label>Địa chỉ thường trú</label>
                                    <select name="noiThuongTru" id="fNoiThuongTru" class="form-control">
                                        <option value="">-- Chọn Địa chỉ thường trú --</option>
                                    </select>
                                </div>
                                <div class="full">
                                    <label>Địa chỉ hiện tại (đang ở) <span style="color:var(--negative)">*</span></label>
                                    <input type="text" name="diaChiHienTai" class="form-control" value="${nv.diaChiHienTai}" required placeholder="Số nhà, đường, phường/xã, quận/huyện, tỉnh/thành phố..." title="Vui lòng nhập Địa chỉ hiện tại đang ở">
                                </div>
                            </div>
                        </div>

                    </div><!-- end cột trái -->

                    <!-- CỘT PHẢI: Panel hành động -->
                    <div style="position: sticky; top: 80px; display: flex; flex-direction: column; gap: 12px;">

                        <button type="submit" class="btn btn-primary" style="width:100%; height: 44px; font-size:14px;">
                            Lưu Thay Đổi
                        </button>

                        <a href="EmployeeDetailServlet?id=${nv.id}&mode=view" class="btn btn-outline" style="width:100%; height: 40px; font-size:13px;">
                            Hủy / Quay Lại Xem
                        </a>

                        <!-- Tóm tắt nhân viên đang sửa -->
                        <div class="panel" style="padding: 16px;">
                            <div style="font-size:11.5px; color:var(--muted); font-weight:700; text-transform:uppercase; letter-spacing:0.05em; margin-bottom:8px;">Hồ sơ đang sửa</div>
                            <div style="font-size:15px; font-weight:700; color:var(--text-primary); margin-bottom:4px;">${nv.hoVaTen}</div>
                            <div style="font-size:12.5px; color:var(--muted); margin-bottom:6px;">Mã: <span style="font-family: var(--font-mono); font-weight: 600;">${nv.maNV}</span></div>
                            <span class="badge ${nv.trangThai == 'Hoạt động' ? 'badge-success' : 'badge-danger'}">
                                ${nv.trangThai}
                            </span>
                        </div>

                        <% if("MANAGER".equals(session.getAttribute("role"))) { %>
                            <!-- Card: Lý do cập nhật -->
                            <div class="panel" style="padding: 16px;">
                                <label style="font-size:12px; font-weight:700; color:var(--text-primary); margin-bottom:6px; display:block;">
                                    Lý do cập nhật <span style="color:var(--negative)">*</span>
                                </label>
                                <textarea name="lyDoThayDoi" class="form-control" rows="3" placeholder="Nhập lý do chỉnh sửa hồ sơ..." required style="min-height: 70px;" title="Vui lòng nhập lý do cập nhật"></textarea>
                                <div style="font-size:11px; color:var(--muted); margin-top:4px;">
                                    * Bắt buộc để lưu nhật ký kiểm toán hệ thống.
                                </div>
                            </div>
                        <% } %>

                    </div><!-- end cột phải -->

                </div><!-- end 2-cột layout -->
            </form>

            <% if("MANAGER".equals(session.getAttribute("role"))) { %>
                <!-- Form Đổi Mật Khẩu -->
                <div class="detail-view-card" style="margin-top: 24px;">
                    <h3>Đổi Mật Khẩu Đăng Nhập (Quyền Quản Lý)</h3>

                    <%
                        String credMsg = (String) request.getAttribute("credMessage");
                        if (credMsg != null && !credMsg.trim().isEmpty()) {
                            boolean isCredErr = credMsg.startsWith("LỖI") || credMsg.startsWith("Lỗi");
                    %>
                        <div class="alert <%= isCredErr ? "alert-danger" : "alert-success" %>"><%= credMsg %></div>
                    <% } %>

                    <form action="ChangeCredentialsServlet" method="POST" onsubmit="return validatePasswordChange();">
                        <input type="hidden" name="idNhanVien" value="${nv.id}">
                        <div class="form-grid">
                            <div class="full">
                                <label>Tên đăng nhập (Username cố định)</label>
                                <input type="text" class="form-control" value="${nv.username}" readonly title="Không được phép sửa Username">
                            </div>
                            <div>
                                <label>Mật khẩu mới <span style="color:var(--negative)">*</span></label>
                                <input type="password" name="newPassword" id="newPwd" class="form-control" required placeholder="Nhập mật khẩu mới...">
                            </div>
                            <div>
                                <label>Xác nhận mật khẩu mới <span style="color:var(--negative)">*</span></label>
                                <input type="password" name="confirmPassword" id="confirmPwd" class="form-control" required placeholder="Nhập lại mật khẩu mới...">
                            </div>
                        </div>
                        <div style="text-align: right; margin-top: 16px;">
                            <button type="submit" class="btn btn-warning"
                                onclick="return confirm('Xác nhận đặt lại mật khẩu cho tài khoản nhân viên này?');">
                                Cập Nhật Mật Khẩu
                            </button>
                        </div>
                    </form>
                </div>
                <script>
                    function validatePasswordChange() {
                        var p1 = document.getElementById('newPwd').value;
                        var p2 = document.getElementById('confirmPwd').value;
                        if (p1 === '') {
                            alert('Vui lòng nhập mật khẩu mới!');
                            return false;
                        }
                        if (p1 !== p2) {
                            alert('Mật khẩu xác nhận không khớp!');
                            return false;
                        }
                        return true;
                    }
                </script>

                <!-- Form Khóa / Mở khóa tài khoản -->
                <c:if test="${nv.isAdmin == 'false'}">
                <div class="detail-view-card" style="margin-top: 20px;">
                    <h3 style="color: var(--negative);">Thay Đổi Trạng Thái Khóa Tài Khoản</h3>
                    <form action="ToggleEmployeeStatusServlet" method="POST">
                        <input type="hidden" name="idNhanVien" value="${nv.id}">
                        <input type="hidden" name="currentStatus" value="${nv.trangThai == 'Hoạt động' ? '1' : '2'}">
                        <div style="margin-bottom: 14px;">
                            <label style="display: block; font-size: 12.5px; font-weight: 600; color: var(--muted); margin-bottom: 6px;">
                                Lý do thay đổi trạng thái ${nv.trangThai == 'Hoạt động' ? '(bắt buộc khi khóa)' : '(tùy chọn khi mở)'}:
                            </label>
                            <input type="text" name="lyDoThayDoi" class="form-control"
                                placeholder="${nv.trangThai == 'Hoạt động' ? 'Nhập lý do khóa tài khoản...' : 'Bỏ trống hoặc nhập lý do để mở khóa...'}">
                        </div>
                        <button type="submit" class="btn ${nv.trangThai == 'Hoạt động' ? 'btn-danger' : 'btn-success'}"
                            onclick="return confirm('Xác nhận thay đổi trạng thái tài khoản?');">
                            ${nv.trangThai == 'Hoạt động' ? 'Khóa Tài Khoản Nhân Viên' : 'Mở Khóa Tài Khoản Nhân Viên'}
                        </button>
                    </form>
                </div>
                </c:if>
                <c:if test="${nv.isAdmin == 'true'}">
                <div class="alert alert-warning" style="margin-top: 20px;">
                    <span>Tài khoản Quản lý gốc (Admin) không có chức năng khóa tài khoản và không áp dụng lý do khóa.</span>
                </div>
                </c:if>
            <% } %>

            <script>
                function validateEmployeeForm() {
                    var dobEl = document.getElementById('fDob');
                    var phoneEl = document.getElementById('fPhone');
                    var errDob = document.getElementById('errDob');
                    var errPhone = document.getElementById('errPhone');
                    if (errDob) errDob.textContent = '';
                    if (errPhone) errPhone.textContent = '';

                    var phone = phoneEl ? phoneEl.value.trim() : '';
                    if (phone !== '') {
                        var cleanPhone = phone.replace(/[\s\-]/g, '');
                        if (!/^(0\d{9}|[1-9]\d{8})$/.test(cleanPhone)) {
                            if (errPhone) errPhone.textContent = 'Số điện thoại hợp lệ là 10 số (bắt đầu bằng 0) hoặc 9 số.';
                            if (phoneEl) phoneEl.focus();
                            return false;
                        }
                    }

                    var dobVal = dobEl ? dobEl.value : '';
                    if (dobVal) {
                        var dob = new Date(dobVal);
                        var today = new Date();
                        var age = today.getFullYear() - dob.getFullYear();
                        var m = today.getMonth() - dob.getMonth();
                        if (m < 0 || (m === 0 && today.getDate() < dob.getDate())) {
                            age--;
                        }
                        if (age < 18) {
                            if (errDob) errDob.textContent = 'Nhân viên phải từ 18 tuổi trở lên.';
                            if (dobEl) dobEl.focus();
                            return false;
                        }
                    }
                    var errEditCccdDate = document.getElementById('errEditCccdDate');
                    if (errEditCccdDate) errEditCccdDate.textContent = '';

                    var maNVVal = document.getElementsByName('maNV')[0] ? document.getElementsByName('maNV')[0].value.trim() : '';
                    var cccdVal = document.getElementsByName('cccd')[0] ? document.getElementsByName('cccd')[0].value.trim() : '';
                    var ngayCapVal = document.getElementsByName('ngayCapCccd')[0] ? document.getElementsByName('ngayCapCccd')[0].value.trim() : '';
                    var ngayHetHanVal = document.getElementsByName('ngayHetHanCccd')[0] ? document.getElementsByName('ngayHetHanCccd')[0].value.trim() : '';
                    var noiCapVal = document.getElementsByName('noiCapCccd')[0] ? document.getElementsByName('noiCapCccd')[0].value.trim() : '';
                    var diaChiHienTaiVal = document.getElementsByName('diaChiHienTai')[0] ? document.getElementsByName('diaChiHienTai')[0].value.trim() : '';
                    
                    if (ngayCapVal && ngayHetHanVal) {
                        if (new Date(ngayHetHanVal) <= new Date(ngayCapVal)) {
                            if (errEditCccdDate) errEditCccdDate.textContent = 'Ngày hết hạn CCCD phải sau Ngày cấp CCCD!';
                            alert('Ngày hết hạn CCCD phải sau Ngày cấp CCCD!');
                            return false;
                        }
                    }

                    if (maNVVal === '' || cccdVal === '' || ngayCapVal === '' || ngayHetHanVal === '' || noiCapVal === '' || diaChiHienTaiVal === '') {
                        alert('Bắt buộc phải nhập ĐẦY ĐỦ Mã nhân viên, Mã số CCCD, Ngày cấp/Hết hạn CCCD, Nơi cấp CCCD và Địa chỉ hiện tại, không được để trống!');
                        return false;
                    }
                    return true;
                }

                (function () {
                    var districtsMap = {
                        "An Giang": ["TP Long Xuyên", "TP Châu Đốc", "TX Tân Châu", "Huyện An Phú", "Huyện Châu Phú", "Huyện Châu Thành", "Huyện Chợ Mới", "Huyện Phú Tân", "Huyện Thoại Sơn", "Huyện Tịnh Biên", "Huyện Tri Tôn"],
                        "Bà Rịa - Vũng Tàu": ["TP Vũng Tàu", "TP Bà Rịa", "TX Phú Mỹ", "Huyện Châu Đức", "Huyện Côn Đảo", "Huyện Đất Đỏ", "Huyện Long Điền", "Huyện Xuyên Mộc"],
                        "Bắc Giang": ["TP Bắc Giang", "Huyện Hiệp Hòa", "Huyện Lạng Giang", "Huyện Lục Nam", "Huyện Lục Ngạn", "Huyện Sơn Động", "Huyện Tân Yên", "Huyện Việt Yên", "Huyện Yên Dũng", "Huyện Yên Thế"],
                        "Bắc Kạn": ["TP Bắc Kạn", "Huyện Ba Bể", "Huyện Bạch Thông", "Huyện Chợ Đồn", "Huyện Chợ Mới", "Huyện Na Rì", "Huyện Ngân Sơn", "Huyện Pác Nặm"],
                        "Bạc Liêu": ["TP Bạc Liêu", "TX Giá Rai", "Huyện Đông Hải", "Huyện Hòa Bình", "Huyện Hồng Dân", "Huyện Phước Long", "Huyện Vĩnh Lợi"],
                        "Bắc Ninh": ["TP Bắc Ninh", "TP Từ Sơn", "Huyện Gia Bình", "Huyện Lương Tài", "Huyện Quế Võ", "Huyện Thuận Thành", "Huyện Tiên Du", "Huyện Yên Phong"],
                        "Bến Tre": ["TP Bến Tre", "Huyện Ba Tri", "Huyện Bình Đại", "Huyện Châu Thành", "Huyện Chợ Lách", "Huyện Giồng Trôm", "Huyện Mỏ Cày Bắc", "Huyện Mỏ Cày Nam", "Huyện Thạnh Phú"],
                        "Bình Định": ["TP Quy Nhơn", "TX An Nhơn", "TX Hoài Nhơn", "Huyện An Lão", "Huyện Hoài Ân", "Huyện Phù Cát", "Huyện Phù Mỹ", "Huyện Tây Sơn", "Huyện Tuy Phước", "Huyện Vân Canh", "Huyện Vĩnh Thạnh"],
                        "Bình Dương": ["TP Thủ Dầu Một", "TP Dĩ An", "TP Thuận An", "TX Bến Cát", "TX Tân Uyên", "Huyện Bắc Tân Uyên", "Huyện Bàu Bàng", "Huyện Dầu Tiếng", "Huyện Phú Giáo"],
                        "Bình Phước": ["TP Đồng Xoài", "TX Bình Long", "TX Phước Long", "Huyện Bù Đăng", "Huyện Bù Đốp", "Huyện Bù Gia Mập", "Huyện Chơn Thành", "Huyện Đồng Phú", "Huyện Hớn Quản", "Huyện Lộc Ninh", "Huyện Phú Riềng"],
                        "Bình Thuận": ["TP Phan Thiết", "TX La Gi", "Huyện Bắc Bình", "Huyện Đức Linh", "Huyện Hàm Tân", "Huyện Hàm Thuận Bắc", "Huyện Hàm Thuận Nam", "Huyện Phú Quý", "Huyện Tánh Linh", "Huyện Tuy Phong"],
                        "Cà Mau": ["TP Cà Mau", "Huyện Cái Nước", "Huyện Đầm Dơi", "Huyện Năm Căn", "Huyện Ngọc Hiển", "Huyện Phú Tân", "Huyện Thới Bình", "Huyện Trần Văn Thời", "Huyện U Minh"],
                        "Cần Thơ": ["Quận Bình Thủy", "Quận Cái Răng", "Quận Ninh Kiều", "Quận Ô Môn", "Quận Thốt Nốt", "Huyện Cờ Đỏ", "Huyện Phong Điền", "Huyện Thới Lai", "Huyện Vĩnh Thạnh"],
                        "Cao Bằng": ["TP Cao Bằng", "Huyện Bảo Lạc", "Huyện Bảo Lâm", "Huyện Hạ Lang", "Huyện Hà Quảng", "Huyện Hòa An", "Huyện Nguyên Bình", "Huyện Quảng Hòa", "Huyện Thạch An", "Huyện Trùng Khánh"],
                        "Đà Nẵng": ["Quận Hải Châu", "Quận Cẩm Lệ", "Quận Thanh Khê", "Quận Liên Chiểu", "Quận Ngũ Hành Sơn", "Quận Sơn Trà", "Huyện Hòa Vang", "Huyện Hoàng Sa"],
                        "Đắk Lắk": ["TP Buôn Ma Thuột", "TX Buôn Hồ", "Huyện Buôn Đôn", "Huyện Cư M'gar", "Huyện Cư Kuin", "Huyện Ea H'leo", "Huyện Ea Kar", "Huyện Ea Súp", "Huyện Krông Ana", "Huyện Krông Bông", "Huyện Krông Búk", "Huyện Krông Năng", "Huyện Krông Pắc", "Huyện Lắk", "Huyện M'Đrắk"],
                        "Đắk Nông": ["TP Gia Nghĩa", "Huyện Cư Jút", "Huyện Đắk Glong", "Huyện Đắk Mil", "Huyện Đắk R'lấp", "Huyện Đắk Song", "Huyện Krông Nô", "Huyện Tuy Đức"],
                        "Điện Biên": ["TP Điện Biên Phủ", "TX Mường Lay", "Huyện Điện Biên", "Huyện Điện Biên Đông", "Huyện Mường Ảng", "Huyện Mường Chà", "Huyện Mường Nhé", "Huyện Nậm Pồ", "Huyện Tủa Chùa", "Huyện Tuần Giáo"],
                        "Đồng Nai": ["TP Biên Hòa", "TP Long Khánh", "Huyện Cẩm Mỹ", "Huyện Định Quán", "Huyện Long Thành", "Huyện Nhơn Trạch", "Huyện Tân Phú", "Huyện Thống Nhất", "Huyện Trảng Bom", "Huyện Vĩnh Cửu", "Huyện Xuân Lộc"],
                        "Đồng Tháp": ["TP Cao Lãnh", "TP Sa Đéc", "TP Hồng Ngự", "Huyện Cao Lãnh", "Huyện Châu Thành", "Huyện Hồng Ngự", "Huyện Lai Vung", "Huyện Lấp Vò", "Huyện Tam Nông", "Huyện Tân Hồng", "Huyện Thanh Bình", "Huyện Tháp Mười"],
                        "Gia Lai": ["TP Pleiku", "TX An Khê", "TX Ayun Pa", "Huyện Chư Păh", "Huyện Chư Prông", "Huyện Chư Pưh", "Huyện Chư Sê", "Huyện Đắk Đoa", "Huyện Đắk Pơ", "Huyện Đức Cơ", "Huyện Ia Grai", "Huyện Ia Pa", "Huyện K'Bang", "Huyện Kông Chro", "Huyện Krông Pa", "Huyện Mang Yang", "Huyện Phú Thiện"],
                        "Hà Giang": ["TP Hà Giang", "Huyện Bắc Mê", "Huyện Bắc Quang", "Huyện Đồng Văn", "Huyện Hoàng Su Phì", "Huyện Mèo Vạc", "Huyện Quản Bạ", "Huyện Quang Bình", "Huyện Vị Xuyên", "Huyện Xín Mần", "Huyện Yên Minh"],
                        "Hà Nam": ["TP Phủ Lý", "TX Duy Tiên", "Huyện Bình Lục", "Huyện Kim Bảng", "Huyện Lý Nhân", "Huyện Thanh Liêm"],
                        "Hà Nội": ["Quận Ba Đình", "Quận Hoàn Kiếm", "Quận Tây Hồ", "Quận Long Biên", "Quận Cầu Giấy", "Quận Đống Đa", "Quận Hai Bà Trưng", "Quận Hoàng Mai", "Quận Thanh Xuân", "Quận Nam Từ Liêm", "Quận Bắc Từ Liêm", "Quận Hà Đông", "Thị xã Sơn Tây", "Huyện Ba Vì", "Huyện Chương Mỹ", "Huyện Đan Phượng", "Huyện Đông Anh", "Huyện Gia Lâm", "Huyện Hoài Đức", "Huyện Mê Linh", "Huyện Mỹ Đức", "Huyện Phú Xuyên", "Huyện Phúc Thọ", "Huyện Quốc Oai", "Huyện Sóc Sơn", "Huyện Thạch Thất", "Huyện Thanh Oai", "Huyện Thanh Trì", "Huyện Thường Tín", "Huyện Ứng Hòa"],
                        "Hà Tĩnh": ["TP Hà Tĩnh", "TX Hồng Lĩnh", "TX Kỳ Anh", "Huyện Cẩm Xuyên", "Huyện Can Lộc", "Huyện Đức Thọ", "Huyện Hương Khê", "Huyện Hương Sơn", "Huyện Kỳ Anh", "Huyện Lộc Hà", "Huyện Nghi Xuân", "Huyện Thạch Hà", "Huyện Vũ Quang"],
                        "Hải Dương": ["TP Hải Dương", "TP Chí Linh", "TX Kinh Môn", "Huyện Bình Giang", "Huyện Cẩm Giàng", "Huyện Gia Lộc", "Huyện Kim Thành", "Huyện Nam Sách", "Huyện Ninh Giang", "Huyện Thanh Hà", "Huyện Thanh Miện", "Huyện Tứ Kỳ"],
                        "Hải Phòng": ["Quận Hồng Bàng", "Quận Lê Chân", "Quận Ngô Quyền", "Quận Kiến An", "Quận Hải An", "Quận Đồ Sơn", "Quận Dương Kinh", "Huyện An Dương", "Huyện An Lão", "Huyện Bạch Long Vĩ", "Huyện Cát Hải", "Huyện Kiến Thụy", "Huyện Tiên Lãng", "Huyện Vĩnh Bảo", "Huyện Thủy Nguyên"],
                        "Hậu Giang": ["TP Vị Thanh", "TP Ngã Bảy", "TX Long Mỹ", "Huyện Châu Thành", "Huyện Châu Thành A", "Huyện Long Mỹ", "Huyện Phụng Hiệp", "Huyện Vị Thủy"],
                        "Hòa Bình": ["TP Hòa Bình", "Huyện Cao Phong", "Huyện Đà Bắc", "Huyện Kim Bôi", "Huyện Lạc Sơn", "Huyện Lạc Thủy", "Huyện Lương Sơn", "Huyện Mai Châu", "Huyện Tân Lạc", "Huyện Yên Thủy"],
                        "Hưng Yên": ["TP Hưng Yên", "TX Mỹ Hào", "Huyện Ân Thi", "Huyện Khoái Châu", "Huyện Kim Động", "Huyện Phù Cừ", "Huyện Tiên Lữ", "Huyện Văn Giang", "Huyện Văn Lâm", "Huyện Yên Mỹ"],
                        "Khánh Hòa": ["TP Nha Trang", "TP Cam Ranh", "TX Ninh Hòa", "Huyện Cam Lâm", "Huyện Diên Khánh", "Huyện Khánh Sơn", "Huyện Khánh Vĩnh", "Huyện Trường Sa", "Huyện Vạn Ninh"],
                        "Kiên Giang": ["TP Rạch Giá", "TP Hà Tiên", "TP Phú Quốc", "Huyện An Biên", "Huyện An Minh", "Huyện Châu Thành", "Huyện Giang Thành", "Huyện Giồng Riềng", "Huyện Gò Quao", "Huyện Hòn Đất", "Huyện Kiên Hải", "Huyện Kiên Lương", "Huyện Tân Hiệp", "Huyện U Minh Thượng", "Huyện Vĩnh Thuận"],
                        "Kon Tum": ["TP Kon Tum", "Huyện Đắk Glei", "Huyện Đắk Hà", "Huyện Đắk Tô", "Huyện Ia H'Drai", "Huyện Kon Plông", "Huyện Kon Rẫy", "Huyện Ngọc Hồi", "Huyện Sa Thầy", "Huyện Tu Mơ Rông"],
                        "Lai Châu": ["TP Lai Châu", "Huyện Mường Tè", "Huyện Nậm Nhùn", "Huyện Phong Thổ", "Huyện Sìn Hồ", "Huyện Tam Đường", "Huyện Tân Uyên", "Huyện Than Uyên"],
                        "Lâm Đồng": ["TP Đà Lạt", "TP Bảo Lộc", "Huyện Bảo Lâm", "Huyện Cát Tiên", "Huyện Di Linh", "Huyện Đạ Huoai", "Huyện Đạ Tẻh", "Huyện Đam Rông", "Huyện Đơn Dương", "Huyện Đức Trọng", "Huyện Lạc Dương", "Huyện Lâm Hà"],
                        "Lạng Sơn": ["TP Lạng Sơn", "Huyện Bắc Sơn", "Huyện Bình Gia", "Huyện Cao Lộc", "Huyện Chi Lăng", "Huyện Đình Lập", "Huyện Hữu Lũng", "Huyện Lộc Bình", "Huyện Tràng Định", "Huyện Văn Lãng", "Huyện Văn Quan"],
                        "Lào Cai": ["TP Lào Cai", "TX Sa Pa", "Huyện Bảo Thắng", "Huyện Bảo Yên", "Huyện Bát Xát", "Huyện Bắc Hà", "Huyện Mường Khương", "Huyện Si Ma Cai", "Huyện Văn Bàn"],
                        "Long An": ["TP Tân An", "TX Kiến Tường", "Huyện Bến Lức", "Huyện Cần Đước", "Huyện Cần Giuộc", "Huyện Châu Thành", "Huyện Đức Hòa", "Huyện Đức Huệ", "Huyện Mộc Hóa", "Huyện Tân Hưng", "Huyện Tân Thạnh", "Huyện Tân Trụ", "Huyện Thạnh Hóa", "Huyện Thủ Thừa", "Huyện Vĩnh Hưng"],
                        "Nam Định": ["TP Nam Định", "Huyện Giao Thủy", "Huyện Hải Hậu", "Huyện Mỹ Lộc", "Huyện Nam Trực", "Huyện Nghĩa Hưng", "Huyện Trực Ninh", "Huyện Vụ Bản", "Huyện Xuân Trường", "Huyện Ý Yên"],
                        "Nghệ An": ["TP Vinh", "TX Cửa Lò", "TX Hoàng Mai", "TX Thái Hòa", "Huyện Anh Sơn", "Huyện Con Cuông", "Huyện Diễn Châu", "Huyện Đô Lương", "Huyện Hưng Nguyên", "Huyện Kỳ Sơn", "Huyện Nam Đàn", "Huyện Nghi Lộc", "Huyện Nghĩa Đàn", "Huyện Quế Phong", "Huyện Quỳ Châu", "Huyện Quỳ Hợp", "Huyện Quỳnh Lưu", "Huyện Tân Kỳ", "Huyện Thanh Chương", "Huyện Tương Dương", "Huyện Yên Thành"],
                        "Ninh Bình": ["TP Ninh Bình", "TP Tam Điệp", "Huyện Gia Viễn", "Huyện Hoa Lư", "Huyện Kim Sơn", "Huyện Nho Quan", "Huyện Yên Khánh", "Huyện Yên Mô"],
                        "Ninh Thuận": ["TP Phan Rang - Tháp Chàm", "Huyện Bác Ái", "Huyện Ninh Hải", "Huyện Ninh Phước", "Huyện Ninh Sơn", "Huyện Thuận Bắc", "Huyện Thuận Nam"],
                        "Phú Thọ": ["TP Việt Trì", "TX Phú Thọ", "Huyện Cẩm Khê", "Huyện Đoan Hùng", "Huyện Hạ Hòa", "Huyện Lâm Thao", "Huyện Phù Ninh", "Huyện Tam Nông", "Huyện Tân Sơn", "Huyện Thanh Ba", "Huyện Thanh Sơn", "Huyện Thanh Thủy", "Huyện Yên Lập"],
                        "Phú Yên": ["TP Tuy Hòa", "TX Đông Hòa", "TX Sông Cầu", "Huyện Đồng Xuân", "Huyện Phú Hòa", "Huyện Sơn Hòa", "Huyện Sông Hinh", "Huyện Tây Hòa", "Huyện Tuy An"],
                        "Quảng Bình": ["TP Đồng Hới", "TX Ba Đồn", "Huyện Bố Trạch", "Huyện Lệ Thủy", "Huyện Minh Hóa", "Huyện Quảng Ninh", "Huyện Quảng Trạch", "Huyện Tuyên Hóa"],
                        "Quảng Nam": ["TP Tam Kỳ", "TP Hội An", "TX Điện Bàn", "Huyện Bắc Trà My", "Huyện Duy Xuyên", "Huyện Đại Lộc", "Huyện Đông Giang", "Huyện Hiệp Đức", "Huyện Nam Giang", "Huyện Nam Trà My", "Huyện Nông Sơn", "Huyện Núi Thành", "Huyện Phú Ninh", "Huyện Phước Sơn", "Huyện Quế Sơn", "Huyện Tây Giang", "Huyện Thăng Bình", "Huyện Tiên Phước"],
                        "Quảng Ngãi": ["TP Quảng Ngãi", "TX Đức Phổ", "Huyện Ba Tơ", "Huyện Bình Sơn", "Huyện Lý Sơn", "Huyện Minh Long", "Huyện Mộ Đức", "Huyện Nghĩa Hành", "Huyện Sơn Hà", "Huyện Sơn Tây", "Huyện Sơn Tịnh", "Huyện Trà Bồng", "Huyện Tư Nghĩa"],
                        "Quảng Ninh": ["TP Hạ Long", "TP Cẩm Phả", "TP Móng Cái", "TP Uông Bí", "TX Đông Triều", "TX Quảng Yên", "Huyện Ba Chẽ", "Huyện Bình Liêu", "Huyện Cô Tô", "Huyện Đầm Hà", "Huyện Hải Hà", "Huyện Tiên Yên", "Huyện Vân Đồn"],
                        "Quảng Trị": ["TP Đông Hà", "TX Quảng Trị", "Huyện Cam Lộ", "Huyện Cồn Cỏ", "Huyện Đakrông", "Huyện Gio Linh", "Huyện Hải Lăng", "Huyện Hướng Hóa", "Huyện Triệu Phong", "Huyện Vĩnh Linh"],
                        "Sóc Trăng": ["TP Sóc Trăng", "TX Ngã Năm", "TX Vĩnh Châu", "Huyện Châu Thành", "Huyện Cù Lao Dung", "Huyện Kế Sách", "Huyện Long Phú", "Huyện Mỹ Xuyên", "Huyện Mỹ Tú", "Huyện Thạnh Trị", "Huyện Trần Đề"],
                        "Sơn La": ["TP Sơn La", "Huyện Bắc Yên", "Huyện Mai Sơn", "Huyện Mường La", "Huyện Mộc Châu", "Huyện Phù Yên", "Huyện Quỳnh Nhai", "Huyện Sông Mã", "Huyện Sốp Cộp", "Huyện Thuận Châu", "Huyện Vân Hồ", "Huyện Yên Châu"],
                        "Tây Ninh": ["TP Tây Ninh", "TX Hòa Thành", "TX Trảng Bàng", "Huyện Bến Cầu", "Huyện Châu Thành", "Huyện Dương Minh Châu", "Huyện Gò Dầu", "Huyện Tân Biên", "Huyện Tân Châu"],
                        "Thái Bình": ["TP Thái Bình", "Huyện Đông Hưng", "Huyện Hưng Hà", "Huyện Kiến Xương", "Huyện Quỳnh Phụ", "Huyện Thái Thụy", "Huyện Tiền Hải", "Huyện Vũ Thư"],
                        "Thái Nguyên": ["TP Thái Nguyên", "TP Sông Công", "TP Phổ Yên", "Huyện Đại Từ", "Huyện Định Hóa", "Huyện Đồng Hỷ", "Huyện Phú Bình", "Huyện Phú Lương", "Huyện Võ Nhai"],
                        "Thanh Hóa": ["TP Thanh Hóa", "TP Sầm Sơn", "TX Bỉm Sơn", "TX Nghi Sơn", "Huyện Bá Thước", "Huyện Cẩm Thủy", "Huyện Đông Sơn", "Huyện Hà Trung", "Huyện Hậu Lộc", "Huyện Hoằng Hóa", "Huyện Lang Chánh", "Huyện Mường Lát", "Huyện Nga Sơn", "Huyện Ngọc Lặc", "Huyện Như Thanh", "Huyện Như Xuân", "Huyện Nông Cống", "Huyện Quan Hóa", "Huyện Quan Sơn", "Huyện Quảng Xương", "Huyện Thạch Thành", "Huyện Thiệu Hóa", "Huyện Thọ Xuân", "Huyện Thường Xuân", "Huyện Triệu Sơn", "Huyện Vĩnh Lộc", "Huyện Yên Định"],
                        "Thừa Thiên Huế": ["TP Huế", "TX Hương Thủy", "TX Hương Trà", "Huyện A Lưới", "Huyện Nam Đông", "Huyện Phong Điền", "Huyện Phú Lộc", "Huyện Phú Vang", "Huyện Quảng Điền"],
                        "Tiền Giang": ["TP Mỹ Tho", "TX Cai Lậy", "TX Gò Công", "Huyện Cái Bè", "Huyện Cai Lậy", "Huyện Châu Thành", "Huyện Chợ Gạo", "Huyện Gò Công Đông", "Huyện Gò Công Tây", "Huyện Tân Phú Đông", "Huyện Tân Phước"],
                        "TP Hồ Chí Minh": ["Quận 1", "Quận 3", "Quận 4", "Quận 5", "Quận 6", "Quận 7", "Quận 8", "Quận 10", "Quận 11", "Quận 12", "Quận Bình Tân", "Quận Bình Thạnh", "Quận Gò Vấp", "Quận Phú Nhuận", "Quận Tân Bình", "Quận Tân Phú", "TP Thủ Đức", "Huyện Bình Chánh", "Huyện Cần Giờ", "Huyện Củ Chi", "Huyện Hóc Môn", "Huyện Nhà Bè"],
                        "Trà Vinh": ["TP Trà Vinh", "TX Duyên Hải", "Huyện Càng Long", "Huyện Cầu Kè", "Huyện Cầu Ngang", "Huyện Châu Thành", "Huyện Duyên Hải", "Huyện Tiểu Cần", "Huyện Trà Cú"],
                        "Tuyên Quang": ["TP Tuyên Quang", "Huyện Chiêm Hóa", "Huyện Hàm Yên", "Huyện Lâm Bình", "Huyện Na Hang", "Huyện Sơn Dương", "Huyện Yên Sơn"],
                        "Vĩnh Long": ["TP Vĩnh Long", "TX Bình Minh", "Huyện Bình Tân", "Huyện Long Hồ", "Huyện Mang Thít", "Huyện Tam Bình", "Huyện Trà Ôn", "Huyện Vũng Liêm"],
                        "Vĩnh Phúc": ["TP Vĩnh Yên", "TP Phúc Yên", "Huyện Bình Xuyên", "Huyện Lập Thạch", "Huyện Sông Lô", "Huyện Tam Đảo", "Huyện Tam Dương", "Huyện Vĩnh Tường", "Huyện Yên Lạc"],
                        "Yên Bái": ["TP Yên Bái", "TX Nghĩa Lộ", "Huyện Lục Yên", "Huyện Mù Cang Chải", "Huyện Trạm Tấu", "Huyện Trấn Yên", "Huyện Văn Chấn", "Huyện Văn Yên", "Huyện Yên Bình"]
                    };

                    var provinces = Object.keys(districtsMap);

                    function updateNoiThuongTru(prov, preservedVal) {
                        var selNTT = document.getElementById("fNoiThuongTru");
                        if (!selNTT) return;
                        selNTT.innerHTML = '<option value="">-- Chọn Địa chỉ thường trú --</option>';
                        var found = false;

                        if (prov && districtsMap[prov]) {
                            var grpProv = document.createElement("optgroup");
                            grpProv.label = "Địa chỉ thường trú theo Quê quán (" + prov + ")";

                            var optP = document.createElement("option");
                            optP.value = prov;
                            optP.text = prov;
                            if (preservedVal === prov || (!preservedVal && !found)) {
                                optP.selected = true;
                                found = true;
                            }
                            grpProv.appendChild(optP);

                            var dists = districtsMap[prov];
                            for (var i = 0; i < dists.length; i++) {
                                var val = dists[i] + ", " + prov;
                                var opt = document.createElement("option");
                                opt.value = val;
                                opt.text = val;
                                if (preservedVal === val || (preservedVal && preservedVal.indexOf(dists[i]) !== -1)) {
                                    opt.selected = true;
                                    found = true;
                                }
                                grpProv.appendChild(opt);
                            }
                            selNTT.appendChild(grpProv);
                        }

                        var grpAll = document.createElement("optgroup");
                        grpAll.label = "Tỉnh / Thành phố khác (63 Tỉnh thành)";
                        for (var j = 0; j < provinces.length; j++) {
                            var optA = document.createElement("option");
                            optA.value = provinces[j];
                            optA.text = provinces[j];
                            if (!found && preservedVal === provinces[j]) {
                                optA.selected = true;
                                found = true;
                            }
                            grpAll.appendChild(optA);
                        }
                        selNTT.appendChild(grpAll);

                        if (preservedVal && !found) {
                            var customOpt = document.createElement("option");
                            customOpt.value = preservedVal;
                            customOpt.text = preservedVal;
                            customOpt.selected = true;
                            selNTT.appendChild(customOpt);
                        }
                    }

                    var selQQ = document.getElementById("fQueQuan");
                    var selectedQueQuan = "${nv.queQuan}";
                    if (selQQ) {
                        for (var i = 0; i < provinces.length; i++) {
                            var opt = document.createElement("option");
                            opt.value = provinces[i];
                            opt.text = provinces[i];
                            if (provinces[i] === selectedQueQuan) {
                                opt.selected = true;
                            }
                            selQQ.appendChild(opt);
                        }
                        if (selectedQueQuan && provinces.indexOf(selectedQueQuan) === -1 && selectedQueQuan !== "") {
                            var customOpt = document.createElement("option");
                            customOpt.value = selectedQueQuan;
                            customOpt.text = selectedQueQuan;
                            customOpt.selected = true;
                            selQQ.appendChild(customOpt);
                        }
                        selQQ.addEventListener("change", function () {
                            updateNoiThuongTru(this.value, "");
                        });
                    }

                    updateNoiThuongTru(selectedQueQuan, "${nv.noiThuongTru}");
                })();
            </script>
        <% } %>
    </div>

    <!-- ================= MODAL XÁC THỰC MẬT KHẨU ADMIN ================= -->
    <div id="adminVerifyModal" class="modal-bg">
        <div class="modal-box" style="max-width: 440px;">
            <button type="button" class="close-btn" onclick="closeAdminVerifyModal()">&times;</button>
            <div class="modal-header">
                <h3 class="modal-title">Xác Thực Quyền Quản Lý</h3>
            </div>
            <form onsubmit="event.preventDefault(); submitAdminVerify();" style="margin: 0;">
                <div class="modal-body">
                    <p style="color: var(--muted); font-size: 13.5px; margin-bottom: 16px;">
                        Nhập mật khẩu tài khoản Quản lý (Admin) để hiển thị mật khẩu của nhân sự này:
                    </p>

                    <div id="verifyErrorMsg" class="alert alert-danger" style="display: none;"></div>

                    <div class="form-group">
                        <label for="modalAdminPassword">Mật khẩu Admin <span style="color:var(--negative)">*</span></label>
                        <input type="password" id="modalAdminPassword" class="form-control" placeholder="Nhập mật khẩu Admin..." autocomplete="current-password">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeAdminVerifyModal()">Hủy bỏ</button>
                    <button type="submit" class="btn btn-primary" id="btnSubmitVerify">Xác nhận</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        var currentTargetEmpId = '';
        var isPasswordRevealed = false;
        var realPasswordValue = '${nv.password}';

        function onTogglePasswordClick(empId) {
            if (isPasswordRevealed) {
                var disp = document.getElementById('displayPassword');
                var btn  = document.getElementById('btnTogglePass');
                if(disp) {
                    disp.textContent = '••••••••';
                    disp.style.letterSpacing = '3px';
                    disp.style.color = 'var(--negative)';
                }
                if(btn) {
                    btn.innerHTML = '👁️ Hiện';
                    btn.title = 'Yêu cầu mật khẩu Admin để xem chi tiết mật khẩu';
                }
                isPasswordRevealed = false;
            } else {
                openAdminVerifyModal(empId);
            }
        }

        function openAdminVerifyModal(empId) {
            currentTargetEmpId = empId;
            var inputPass = document.getElementById('modalAdminPassword');
            var errDiv = document.getElementById('verifyErrorMsg');
            if(inputPass) inputPass.value = '';
            if(errDiv) errDiv.style.display = 'none';
            var modal = document.getElementById('adminVerifyModal');
            if(modal) {
                modal.classList.add('open');
                setTimeout(function() {
                    if(inputPass) inputPass.focus();
                }, 150);
            }
        }

        function closeAdminVerifyModal() {
            var modal = document.getElementById('adminVerifyModal');
            if(modal) modal.classList.remove('open');
        }

        function submitAdminVerify() {
            var inputPass = document.getElementById('modalAdminPassword').value;
            var errDiv = document.getElementById('verifyErrorMsg');
            var btnSubmit = document.getElementById('btnSubmitVerify');

            if (!inputPass || inputPass.trim() === '') {
                errDiv.textContent = 'Vui lòng nhập mật khẩu của Admin!';
                errDiv.style.display = 'flex';
                document.getElementById('modalAdminPassword').focus();
                return;
            }

            errDiv.style.display = 'none';
            btnSubmit.textContent = 'Đang kiểm tra...';
            btnSubmit.disabled = true;

            fetch('EmployeeDetailServlet?ajaxVerifyAdmin=1&adminPass=' + encodeURIComponent(inputPass) + '&targetId=' + encodeURIComponent(currentTargetEmpId))
                .then(response => response.json())
                .then(data => {
                    btnSubmit.textContent = 'Xác nhận';
                    btnSubmit.disabled = false;
                    if (data.success) {
                        closeAdminVerifyModal();
                        var passToShow = data.password || realPasswordValue || 'Chưa có mật khẩu';
                        var disp = document.getElementById('displayPassword');
                        var btn  = document.getElementById('btnTogglePass');
                        if(disp) {
                            disp.textContent = passToShow;
                            disp.style.letterSpacing = 'normal';
                            disp.style.color = 'var(--positive)';
                        }
                        if(btn) {
                            btn.innerHTML = 'Ẩn mật khẩu';
                            btn.title = 'Ấn để ẩn mật khẩu lại';
                        }
                        isPasswordRevealed = true;
                    } else {
                        errDiv.textContent = data.message || 'Mật khẩu Admin không chính xác!';
                        errDiv.style.display = 'flex';
                        var modalPass = document.getElementById('modalAdminPassword');
                        if(modalPass) {
                            modalPass.focus();
                            modalPass.select();
                        }
                    }
                })
                .catch(error => {
                    btnSubmit.textContent = 'Xác nhận';
                    btnSubmit.disabled = false;
                    errDiv.textContent = 'Có lỗi kết nối hệ thống. Vui lòng thử lại!';
                    errDiv.style.display = 'flex';
                    console.error('Error:', error);
                });
        }

        window.addEventListener('click', function(event) {
            var modal = document.getElementById('adminVerifyModal');
            if (event.target === modal) {
                closeAdminVerifyModal();
            }
        });
    </script>
    <jsp:include page="footer.jsp" />
</body>
</html>
