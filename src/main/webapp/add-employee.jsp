<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm nhân viên mới — DUNA Men's Wear</title>
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
            <a href="EmployeeManagementServlet" class="active">&larr; Quay lại danh sách nhân sự</a>
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
                <h1 class="page-title">Thêm Nhân Sự Mới</h1>
                <p class="page-subtitle">Khởi tạo hồ sơ nhân viên, phân quyền truy cập hệ thống bán hàng và tài khoản đăng nhập</p>
            </div>
            <div class="action-buttons">
                <a href="EmployeeManagementServlet" class="btn btn-outline btn-sm">Quay Lại</a>
            </div>
        </div>

        <%
            String msg = (String) request.getAttribute("message");
            if (msg != null && !msg.trim().isEmpty()) {
                boolean isErr = msg.contains("LỖI") || msg.contains("LỖi") || msg.startsWith("Lỗi");
        %>
            <div class="alert <%= isErr ? "alert-danger" : "alert-success" %>"><%= msg %></div>
        <% } %>

        <form action="AddEmployeeServlet" method="POST" onsubmit="return validateForm()">

            <!-- 1. Thông tin đăng nhập -->
            <div class="detail-view-card">
                <h3>1. Thông Tin Tài Khoản & Đăng Nhập</h3>
                <div class="form-grid">
                    <div>
                        <label for="fUsername">Tên đăng nhập (Username) <span style="color:var(--negative)">*</span></label>
                        <input type="text" name="username" id="fUsername" class="form-control" required placeholder="Ví dụ: nv001, tran.van.a...">
                    </div>
                    <div>
                        <label>Quyền hạn phân quyền <span style="color:var(--negative)">*</span></label>
                        <input type="hidden" name="roleID" value="2">
                        <input type="text" class="form-control" value="EMPLOYEE — Nhân viên bán hàng (Mặc định)" readonly title="Tài khoản mới tạo mặc định là Nhân viên bán hàng">
                    </div>
                    <div>
                        <label for="fPwd">Mật khẩu đăng nhập <span style="color:var(--negative)">*</span></label>
                        <input type="password" name="password" id="fPwd" class="form-control" required placeholder="Nhập mật khẩu...">
                        <div id="errPwd" style="color:var(--negative); font-size:12px; margin-top:3px;"></div>
                    </div>
                    <div>
                        <label for="fPwd2">Xác nhận mật khẩu <span style="color:var(--negative)">*</span></label>
                        <input type="password" name="confirmPassword" id="fPwd2" class="form-control" required placeholder="Nhập lại mật khẩu...">
                        <div id="errPwd2" style="color:var(--negative); font-size:12px; margin-top:3px;"></div>
                    </div>
                </div>
            </div>

            <!-- 2. Thông tin cá nhân -->
            <div class="detail-view-card">
                <h3>2. Thông Tin Cá Nhân</h3>
                <div class="form-grid">
                    <div class="full">
                        <label for="fHoVaTen">Họ và tên đầy đủ <span style="color:var(--negative)">*</span></label>
                        <input type="text" name="hoVaTen" id="fHoVaTen" class="form-control" required placeholder="Ví dụ: Nguyễn Văn An">
                    </div>
                    <div>
                        <label>Giới tính <span style="color:var(--negative)">*</span></label>
                        <select name="gioiTinh" class="form-control">
                            <option value="1">Nam</option>
                            <option value="0">Nữ</option>
                        </select>
                    </div>
                    <div>
                        <label for="fDob">Ngày sinh <span style="color:var(--negative)">*</span></label>
                        <input type="date" name="ngaySinh" id="fDob" class="form-control" required title="Vui lòng chọn Ngày sinh">
                        <div id="errDob" style="color:var(--negative); font-size:12px; margin-top:3px;"></div>
                    </div>
                    <div>
                        <label for="fPhone">Số điện thoại <span style="color:var(--negative)">*</span></label>
                        <div class="phone-wrap">
                            <span class="phone-prefix">(+84)</span>
                            <input type="tel" name="soDienThoai" id="fPhone" class="form-control" required placeholder="912 345 678" maxlength="10" title="Vui lòng nhập Số điện thoại">
                        </div>
                        <div id="errPhone" style="color:var(--negative); font-size:12px; margin-top:3px;"></div>
                    </div>
                    <div>
                        <label for="fEmail">Địa chỉ Email <span style="color:var(--negative)">*</span></label>
                        <input type="email" name="email" id="fEmail" class="form-control" required placeholder="Ví dụ: nhanvien@email.com" title="Vui lòng nhập Email">
                    </div>
                </div>
            </div>

            <!-- 3. Thông tin CCCD -->
            <div class="detail-view-card">
                <h3>3. Thông Tin Căn Cước Công Dân (CCCD)</h3>
                <div class="form-grid">
                    <div class="full">
                        <label for="fCccd">Mã số CCCD / CMND <span style="color:var(--negative)">*</span></label>
                        <input type="text" name="cccd" id="fCccd" class="form-control" required title="Vui lòng nhập Mã số CCCD" placeholder="12 chữ số — ví dụ: 001204012345">
                    </div>
                    <div>
                        <label for="fNgayCapCccd">Ngày cấp CCCD <span style="color:var(--negative)">*</span></label>
                        <input type="date" name="ngayCapCccd" id="fNgayCapCccd" class="form-control" required title="Vui lòng chọn Ngày cấp CCCD">
                    </div>
                    <div>
                        <label for="fNgayHetHanCccd">Ngày hết hạn CCCD <span style="color:var(--negative)">*</span></label>
                        <input type="date" name="ngayHetHanCccd" id="fNgayHetHanCccd" class="form-control" required title="Vui lòng chọn Ngày hết hạn CCCD">
                        <div id="errCccdDate" style="color:var(--negative); font-size:12px; margin-top:3px;"></div>
                    </div>
                    <div class="full">
                        <label>Nơi cấp CCCD <span style="color:var(--negative)">*</span></label>
                        <select name="noiCapCccd" class="form-control" required title="Vui lòng chọn Nơi cấp CCCD">
                            <option value="">— Chọn Nơi cấp CCCD / CMND —</option>
                            <optgroup label="Cơ quan cấp Trung ương (Phổ biến nhất)">
                                <option value="Cục Cảnh sát quản lý hành chính về trật tự xã hội">Cục Cảnh sát quản lý hành chính về trật tự xã hội</option>
                                <option value="Cục trưởng Cục Cảnh sát quản lý hành chính về trật tự xã hội">Cục trưởng Cục Cảnh sát quản lý hành chính về trật tự xã hội</option>
                                <option value="Cục Cảnh sát Đăng ký, Quản lý Cư trú và Dữ liệu Quốc gia về Dân cư">Cục Cảnh sát Đăng ký, Quản lý Cư trú và Dữ liệu Quốc gia về Dân cư</option>
                                <option value="Cục Trưởng Cục Cảnh sát Đăng ký, quản lý cư trú và Dữ liệu quốc gia về dân cư">Cục Trưởng Cục Cảnh sát Đăng ký, quản lý cư trú và Dữ liệu quốc gia về dân cư</option>
                                <option value="Bộ Công An - Cục Cảnh sát QLHC về TTXH">Bộ Công An - Cục Cảnh sát QLHC về TTXH</option>
                            </optgroup>
                            <optgroup label="Công an các Tỉnh / Thành phố">
                                <option value="Công an Thành phố Hà Nội">Công an Thành phố Hà Nội</option>
                                <option value="Cảnh sát QLHC về TTXH - Công an TP Hà Nội">Cảnh sát QLHC về TTXH - Công an TP Hà Nội</option>
                                <option value="Công an Thành phố Hồ Chí Minh">Công an Thành phố Hồ Chí Minh</option>
                                <option value="Cảnh sát QLHC về TTXH - Công an TP Hồ Chí Minh">Cảnh sát QLHC về TTXH - Công an TP Hồ Chí Minh</option>
                                <option value="Công an Thành phố Đà Nẵng">Công an Thành phố Đà Nẵng</option>
                                <option value="Công an Thành phố Hải Phòng">Công an Thành phố Hải Phòng</option>
                                <option value="Công an Thành phố Cần Thơ">Công an Thành phố Cần Thơ</option>
                                <option value="Công an Tỉnh Quảng Ninh">Công an Tỉnh Quảng Ninh</option>
                                <option value="Công an Tỉnh Thanh Hóa">Công an Tỉnh Thanh Hóa</option>
                                <option value="Công an Tỉnh Nghệ An">Công an Tỉnh Nghệ An</option>
                                <option value="Công an Tỉnh Hải Dương">Công an Tỉnh Hải Dương</option>
                                <option value="Công an Tỉnh Nam Định">Công an Tỉnh Nam Định</option>
                                <option value="Công an Tỉnh Đồng Nai">Công an Tỉnh Đồng Nai</option>
                                <option value="Công an Tỉnh Bình Dương">Công an Tỉnh Bình Dương</option>
                                <option value="Công an Tỉnh Khánh Hòa">Công an Tỉnh Khánh Hòa</option>
                                <option value="Công an Tỉnh Thừa Thiên Huế">Công an Tỉnh Thừa Thiên Huế</option>
                                <option value="Công an Tỉnh Lâm Đồng">Công an Tỉnh Lâm Đồng</option>
                                <option value="Công an Tỉnh Quảng Nam">Công an Tỉnh Quảng Nam</option>
                                <option value="Công an Tỉnh Vĩnh Phúc">Công an Tỉnh Vĩnh Phúc</option>
                                <option value="Công an Tỉnh Bắc Ninh">Công an Tỉnh Bắc Ninh</option>
                            </optgroup>
                        </select>
                    </div>
                </div>
            </div>

            <!-- 4. Địa chỉ -->
            <div class="detail-view-card">
                <h3>4. Thông Tin Địa Chỉ & Thường Trú</h3>
                <div class="form-grid">
                    <div>
                        <label for="fQueQuan">Quê quán (Tỉnh / Thành phố) <span style="color:var(--negative)">*</span></label>
                        <select name="queQuan" id="fQueQuan" class="form-control" required title="Vui lòng chọn Quê quán">
                            <option value="">— Chọn Quê quán —</option>
                        </select>
                    </div>
                    <div>
                        <label for="fNoiThuongTru">Địa chỉ thường trú <span style="color:var(--negative)">*</span></label>
                        <select name="noiThuongTru" id="fNoiThuongTru" class="form-control" required title="Vui lòng chọn Địa chỉ thường trú">
                            <option value="">— Chọn Địa chỉ thường trú —</option>
                        </select>
                    </div>
                    <div class="full">
                        <label for="fDiaChiHienTai">Địa chỉ hiện tại (đang ở) <span style="color:var(--negative)">*</span></label>
                        <input type="text" name="diaChiHienTai" id="fDiaChiHienTai" class="form-control" required title="Vui lòng nhập Địa chỉ hiện tại đang ở" placeholder="Số nhà, tên đường, phường/xã, quận/huyện, tỉnh/thành phố...">
                    </div>
                </div>
            </div>

            <div style="display: flex; justify-content: center; margin: 28px 0 16px;">
                <button type="submit" class="btn btn-primary btn-lg" style="min-width: 280px;">
                    Tạo Tài Khoản & Lưu Hồ Sơ
                </button>
            </div>
        </form>
    </div>

    <jsp:include page="footer.jsp" />

    <script>
    function validateForm() {
        var valid = true;
        document.getElementById('errPwd').textContent = '';
        document.getElementById('errPwd2').textContent = '';
        document.getElementById('errDob').textContent = '';
        document.getElementById('errPhone').textContent = '';

        var pwd = document.getElementById('fPwd').value;
        var pwd2 = document.getElementById('fPwd2').value;
        if (pwd.trim() === '') {
            document.getElementById('errPwd').textContent = 'Mật khẩu không được để trống.';
            valid = false;
        }
        if (pwd !== pwd2) {
            document.getElementById('errPwd2').textContent = 'Mật khẩu xác nhận không khớp.';
            valid = false;
        }

        var phone = document.getElementById('fPhone').value.trim();
        if (phone === '') {
            document.getElementById('errPhone').textContent = 'Số điện thoại không được để trống.';
            valid = false;
        } else {
            var cleanPhone = phone.replace(/[\s\-]/g, '');
            if (!/^(0\d{9}|[1-9]\d{8})$/.test(cleanPhone)) {
                document.getElementById('errPhone').textContent = 'Số điện thoại hợp lệ là 10 số (bắt đầu bằng 0) hoặc 9 số.';
                valid = false;
            }
        }

        var dobVal = document.getElementById('fDob').value;
        if (!dobVal || dobVal.trim() === '') {
            document.getElementById('errDob').textContent = 'Ngày sinh không được để trống.';
            valid = false;
        } else {
            var dob = new Date(dobVal);
            var today = new Date();
            var age = today.getFullYear() - dob.getFullYear();
            var m = today.getMonth() - dob.getMonth();
            if (m < 0 || (m === 0 && today.getDate() < dob.getDate())) age--;
            if (age < 18) {
                document.getElementById('errDob').textContent = 'Nhân viên phải từ 18 tuổi trở lên.';
                valid = false;
            }
        }

        var errCccdDate = document.getElementById('errCccdDate');
        if (errCccdDate) errCccdDate.textContent = '';

        var username = document.getElementsByName('username')[0] ? document.getElementsByName('username')[0].value.trim() : '';
        var hoVaTen = document.getElementsByName('hoVaTen')[0] ? document.getElementsByName('hoVaTen')[0].value.trim() : '';
        var email = document.getElementsByName('email')[0] ? document.getElementsByName('email')[0].value.trim() : '';
        var queQuan = document.getElementById('fQueQuan') ? document.getElementById('fQueQuan').value.trim() : '';
        var noiThuongTru = document.getElementById('fNoiThuongTru') ? document.getElementById('fNoiThuongTru').value.trim() : '';
        var diaChiHienTai = document.getElementById('fDiaChiHienTai') ? document.getElementById('fDiaChiHienTai').value.trim() : '';
        var cccdVal = document.getElementsByName('cccd')[0] ? document.getElementsByName('cccd')[0].value.trim() : '';
        var ngayCapVal = document.getElementsByName('ngayCapCccd')[0] ? document.getElementsByName('ngayCapCccd')[0].value.trim() : '';
        var ngayHetHanVal = document.getElementsByName('ngayHetHanCccd')[0] ? document.getElementsByName('ngayHetHanCccd')[0].value.trim() : '';
        var noiCapVal = document.getElementsByName('noiCapCccd')[0] ? document.getElementsByName('noiCapCccd')[0].value.trim() : '';

        if (ngayCapVal && ngayHetHanVal) {
            if (new Date(ngayHetHanVal) <= new Date(ngayCapVal)) {
                if (errCccdDate) errCccdDate.textContent = 'Ngày hết hạn CCCD phải sau Ngày cấp CCCD!';
                alert('Ngày hết hạn CCCD phải sau Ngày cấp CCCD!');
                valid = false;
            }
        }

        if (username === '' || hoVaTen === '' || email === '' || queQuan === '' || noiThuongTru === '' || diaChiHienTai === '' || cccdVal === '' || ngayCapVal === '' || ngayHetHanVal === '' || noiCapVal === '' || phone === '' || !dobVal) {
            alert('Bắt buộc phải nhập ĐẦY ĐỦ TẤT CẢ các trường thông tin nhân viên, không được để trống bất kỳ trường nào!');
            valid = false;
        }

        return valid;
    }

    (function() {
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
            selNTT.innerHTML = '<option value="">— Chọn Địa chỉ thường trú —</option>';
            var found = false;

            if (prov && districtsMap[prov]) {
                var grpProv = document.createElement("optgroup");
                grpProv.label = "Địa chỉ thường trú theo Quê quán (" + prov + ")";
                var optP = document.createElement("option");
                optP.value = prov; optP.text = prov;
                if (preservedVal === prov || (!preservedVal && !found)) { optP.selected = true; found = true; }
                grpProv.appendChild(optP);
                var dists = districtsMap[prov];
                for (var i = 0; i < dists.length; i++) {
                    var val = dists[i] + ", " + prov;
                    var opt = document.createElement("option");
                    opt.value = val; opt.text = val;
                    if (preservedVal === val || (preservedVal && preservedVal.indexOf(dists[i]) !== -1)) { opt.selected = true; found = true; }
                    grpProv.appendChild(opt);
                }
                selNTT.appendChild(grpProv);
            }

            var grpAll = document.createElement("optgroup");
            grpAll.label = "Tỉnh / Thành phố khác (63 Tỉnh thành)";
            for (var j = 0; j < provinces.length; j++) {
                var optA = document.createElement("option");
                optA.value = provinces[j]; optA.text = provinces[j];
                if (!found && preservedVal === provinces[j]) { optA.selected = true; found = true; }
                grpAll.appendChild(optA);
            }
            selNTT.appendChild(grpAll);
        }

        var selQQ = document.getElementById("fQueQuan");
        if (selQQ) {
            for (var i = 0; i < provinces.length; i++) {
                var opt = document.createElement("option");
                opt.value = provinces[i]; opt.text = provinces[i];
                selQQ.appendChild(opt);
            }
            selQQ.addEventListener("change", function() { updateNoiThuongTru(this.value, ""); });
        }
        // ===== GIỮ LẠI DỮ LIỆU KHI CÓ LỖI SERVER =====
        var prevUsername    = "<%= request.getParameter("username") != null ? request.getParameter("username").replace("\"", "&quot;") : "" %>";
        var prevHoVaTen     = "<%= request.getParameter("hoVaTen") != null ? request.getParameter("hoVaTen").replace("\"", "&quot;") : "" %>";
        var prevNgaySinh    = "<%= request.getParameter("ngaySinh") != null ? request.getParameter("ngaySinh") : "" %>";
        var prevPhone       = "<%= request.getParameter("soDienThoai") != null ? request.getParameter("soDienThoai").replace("\"", "&quot;") : "" %>";
        var prevEmail       = "<%= request.getParameter("email") != null ? request.getParameter("email").replace("\"", "&quot;") : "" %>";
        var prevCccd        = "<%= request.getParameter("cccd") != null ? request.getParameter("cccd").replace("\"", "&quot;") : "" %>";
        var prevNgayCap     = "<%= request.getParameter("ngayCapCccd") != null ? request.getParameter("ngayCapCccd") : "" %>";
        var prevNgayHetHan  = "<%= request.getParameter("ngayHetHanCccd") != null ? request.getParameter("ngayHetHanCccd") : "" %>";
        var prevNoiCap      = "<%= request.getParameter("noiCapCccd") != null ? request.getParameter("noiCapCccd").replace("\"", "&quot;") : "" %>";
        var prevQueQuan     = "<%= request.getParameter("queQuan") != null ? request.getParameter("queQuan").replace("\"", "&quot;") : "" %>";
        var prevNoiThuongTru= "<%= request.getParameter("noiThuongTru") != null ? request.getParameter("noiThuongTru").replace("\"", "&quot;") : "" %>";
        var prevDiaChi      = "<%= request.getParameter("diaChiHienTai") != null ? request.getParameter("diaChiHienTai").replace("\"", "&quot;") : "" %>";
        var prevGioiTinh    = "<%= request.getParameter("gioiTinh") != null ? request.getParameter("gioiTinh") : "" %>";

        var hasError = <%= request.getAttribute("message") != null && !request.getAttribute("message").toString().trim().isEmpty() ? "true" : "false" %>;

        if (hasError && prevUsername) {
            if (document.getElementById('fUsername'))    document.getElementById('fUsername').value    = prevUsername;
            if (document.getElementById('fHoVaTen'))     document.getElementById('fHoVaTen').value     = prevHoVaTen;
            if (document.getElementById('fDob'))         document.getElementById('fDob').value         = prevNgaySinh;
            if (document.getElementById('fPhone'))       document.getElementById('fPhone').value       = prevPhone;
            if (document.getElementById('fEmail'))       document.getElementById('fEmail').value       = prevEmail;
            if (document.getElementById('fCccd'))        document.getElementById('fCccd').value        = prevCccd;
            if (document.getElementById('fNgayCapCccd')) document.getElementById('fNgayCapCccd').value = prevNgayCap;
            if (document.getElementById('fNgayHetHanCccd')) document.getElementById('fNgayHetHanCccd').value = prevNgayHetHan;
            if (document.getElementById('fDiaChiHienTai')) document.getElementById('fDiaChiHienTai').value = prevDiaChi;

            if (prevGioiTinh && document.getElementsByName('gioiTinh')[0]) {
                document.getElementsByName('gioiTinh')[0].value = prevGioiTinh;
            }

            if (prevNoiCap) {
                var selNC = document.getElementsByName('noiCapCccd')[0];
                if (selNC) selNC.value = prevNoiCap;
            }

            if (prevQueQuan) {
                updateNoiThuongTru(prevQueQuan, prevNoiThuongTru);
                if (document.getElementById('fQueQuan')) document.getElementById('fQueQuan').value = prevQueQuan;
            } else {
                updateNoiThuongTru("", "");
            }
        } else {
            updateNoiThuongTru("", "");
        }
    })();
    </script>
</body>
</html>
