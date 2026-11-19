package com.duna.common.uc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Danh muc trung tam 300 Use Case (UC001 - UC300) cho 17 Module & 4 Thanh vien.
 */
public final class UcRegistry {

    public static final class ParamSpec {
        private final String name;
        private final String label;
        private final String type;
        private final String defaultValue;

        public ParamSpec(String name, String label, String type, String defaultValue) {
            this.name = name;
            this.label = label;
            this.type = type;
            this.defaultValue = defaultValue;
        }

        public String getName() { return name; }
        public String getLabel() { return label; }
        public String getType() { return type; }
        public String getDefaultValue() { return defaultValue; }
    }

    public static final class UcMeta {
        private final String code;
        private final String module;
        private final String owner;
        private final String route;
        private final String jspView;
        private final String title;
        private final String mode;
        private final List<ParamSpec> params;

        public UcMeta(String code, String module, String owner, String route, String jspView,
                      String title, String mode, List<ParamSpec> params) {
            this.code = code;
            this.module = module;
            this.owner = owner;
            this.route = route;
            this.jspView = jspView;
            this.title = title;
            this.mode = mode;
            this.params = params;
        }

        public String getCode() { return code; }
        public String getModule() { return module; }
        public String getOwner() { return owner; }
        public String getRoute() { return route; }
        public String getJspView() { return jspView; }
        public String getTitle() { return title; }
        public String getMode() { return mode; }
        public List<ParamSpec> getParams() { return params; }
    }

    private static final Map<String, UcMeta> REGISTRY = new LinkedHashMap<>();

    static {
        // ===== TV1: NGUYEN NGOC MINH - TH08331 (UC001 - UC086) =====
        String vRoles = "/WEB-INF/views/tv1_nguyen_ngoc_minh/roles.jsp";
        r("UC001", "M01", "TV1", "/roles", vRoles, "Xem danh sách vai trò hệ thống (Admin, Quản lý, Nhân viên, Khách hàng)", p("keyword", "Từ khóa", "text", ""));
        w("UC002", "M01", "TV1", "/roles", vRoles, "Thêm mới vai trò và mã vai trò duy nhất", p("roleCode", "Mã vai trò", "text", "SUPERVISOR"), p("roleName", "Tên vai trò", "text", "Giám sát cửa hàng"), p("description", "Mô tả", "text", "Giám sát vận hành"));
        w("UC003", "M01", "TV1", "/roles", vRoles, "Cập nhật tên hiển thị và mô tả vai trò", p("roleId", "ID vai trò", "number", "3"), p("roleName", "Tên mới", "text", "Nhân viên bán hàng POS"), p("description", "Mô tả mới", "text", "Thu ngân & tư vấn"));
        w("UC004", "M01", "TV1", "/roles", vRoles, "Xóa vai trò và kiểm tra tài khoản đang sử dụng", p("roleId", "ID vai trò cần xóa", "number", "99"));
        r("UC005", "M01", "TV1", "/roles", vRoles, "Xem danh sách quyền hạn theo từng module chức năng", p("keyword", "Từ khóa", "text", ""));
        w("UC006", "M01", "TV1", "/roles", vRoles, "Thêm mới quyền hạn truy cập chức năng", p("permissionCode", "Mã quyền", "text", "REPORT_EXPORT"), p("permissionName", "Tên quyền", "text", "Xuất báo cáo"), p("moduleName", "Module", "text", "M16"));
        w("UC007", "M01", "TV1", "/roles", vRoles, "Gán quyền hạn cho vai trò", p("roleId", "ID vai trò", "number", "2"), p("permissionId", "ID quyền", "number", "1"));
        w("UC008", "M01", "TV1", "/roles", vRoles, "Thu hồi quyền hạn khỏi vai trò", p("roleId", "ID vai trò", "number", "2"), p("permissionId", "ID quyền", "number", "1"));
        r("UC009", "M01", "TV1", "/roles", vRoles, "Xem ma trận phân quyền chi tiết của một vai trò", p("roleId", "ID vai trò", "number", "2"));
        r("UC010", "M01", "TV1", "/roles", vRoles, "Kiểm tra quyền của người dùng khi truy cập chức năng", p("userId", "ID tài khoản", "number", "1"), p("permissionCode", "Mã quyền", "text", "USER_VIEW"));

        String vUsers = "/WEB-INF/views/tv1_nguyen_ngoc_minh/users.jsp";
        r("UC011", "M01", "TV1", "/users", vUsers, "Xem danh sách tài khoản người dùng phân trang", p("keyword", "Từ khóa", "text", ""));
        r("UC012", "M01", "TV1", "/users", vUsers, "Tìm kiếm tài khoản theo email, số điện thoại hoặc họ tên", p("keyword", "Từ khóa tìm kiếm", "text", "admin"));
        r("UC013", "M01", "TV1", "/users", vUsers, "Lọc tài khoản theo vai trò và trạng thái hoạt động", p("roleCode", "Vai trò", "text", "STAFF"), p("status", "Trạng thái", "text", "ACTIVE"), p("keyword", "Từ khóa", "text", ""));
        w("UC014", "M01", "TV1", "/users", vUsers, "Đăng ký tài khoản khách hàng mới và tự tạo hồ sơ khách hàng", p("fullName", "Họ và tên", "text", "Nguyễn Văn Khách"), p("email", "Email", "text", "khachmoi@duna.vn"), p("phone", "SĐT", "text", "0988777666"), p("password", "Mật khẩu", "text", "Duna123"));
        r("UC015", "M01", "TV1", "/users", vUsers, "Đăng nhập & xem nhật ký lịch sử đăng nhập thiết bị", p("keyword", "Email / Tên", "text", ""));
        r("UC016", "M01", "TV1", "/users", vUsers, "Kiểm tra & khóa tự động khi đăng nhập sai quá 5 lần", p("keyword", "Email cần kiểm tra", "text", ""));
        w("UC017", "M01", "TV1", "/users", vUsers, "Cập nhật hồ sơ cá nhân và ảnh đại diện tài khoản", p("userId", "ID tài khoản", "number", "4"), p("fullName", "Họ tên mới", "text", "Trần Văn Nam"), p("phone", "SĐT", "text", "0912345678"), p("email", "Email", "text", "khach01@gmail.com"));
        w("UC018", "M01", "TV1", "/users", vUsers, "Đổi mật khẩu tài khoản sau khi xác thực mật khẩu cũ", p("userId", "ID tài khoản", "number", "4"), p("oldPassword", "Mật khẩu cũ", "text", "Khach123"), p("newPassword", "Mật khẩu mới", "text", "Khach123"));
        w("UC019", "M01", "TV1", "/users", vUsers, "Yêu cầu quên mật khẩu và tạo token đặt lại gửi qua email", p("email", "Email tài khoản", "text", "khach01@gmail.com"));
        w("UC020", "M01", "TV1", "/users", vUsers, "Đặt lại mật khẩu mới cho tài khoản bằng token / quản trị", p("userId", "ID tài khoản", "number", "4"), p("newPassword", "Mật khẩu mới", "text", "Khach123"));
        w("UC021", "M01", "TV1", "/users", vUsers, "Khóa hoặc mở khóa tài khoản người dùng (đồng bộ 2 chiều)", p("userId", "ID tài khoản", "number", "4"));
        w("UC022", "M01", "TV1", "/users", vUsers, "Xóa tài khoản chưa phát sinh đơn hàng", p("userId", "ID tài khoản", "number", "9999"));
        r("UC023", "M01", "TV1", "/users", vUsers, "Xem sổ địa chỉ nhận hàng của người dùng", p("userId", "ID tài khoản (0=tất cả)", "number", "0"));
        w("UC024", "M01", "TV1", "/users", vUsers, "Thêm mới địa chỉ nhận hàng theo tỉnh và xã/phường", p("userId", "ID tài khoản", "number", "4"), p("receiverName", "Người nhận", "text", "Trần Văn Nam"), p("receiverPhone", "SĐT", "text", "0912345678"), p("provinceId", "ID Tỉnh", "number", "1"), p("wardId", "ID Phường", "number", "1"), p("addressLine", "Số nhà/Đường", "text", "Số 25 Xuân Thủy"));
        w("UC025", "M01", "TV1", "/users", vUsers, "Cập nhật địa chỉ và đặt địa chỉ nhận hàng mặc định", p("userId", "ID tài khoản", "number", "4"), p("addressId", "ID địa chỉ", "number", "1"), p("addressLine", "Địa chỉ mới", "text", "Số 25 Xuân Thủy, Cầu Giấy"));
        w("UC026", "M01", "TV1", "/users", vUsers, "Xóa địa chỉ nhận hàng khỏi sổ địa chỉ", p("addressId", "ID địa chỉ phụ", "number", "999"));
        r("UC027", "M01", "TV1", "/users", vUsers, "Xem nhật ký thao tác hệ thống theo người dùng và khoảng thời gian", p("fromDate", "Từ ngày", "date", "2026-01-01"), p("toDate", "Đến ngày", "date", "2027-12-31"), p("keyword", "Từ khóa hành động", "text", ""));

        String vCust = "/WEB-INF/views/tv1_nguyen_ngoc_minh/customers.jsp";
        r("UC028", "M02", "TV1", "/customers", vCust, "Xem danh sách khách hàng kèm hạng thành viên", p("keyword", "Từ khóa", "text", ""));
        r("UC029", "M02", "TV1", "/customers", vCust, "Tìm kiếm khách hàng theo mã, số điện thoại hoặc họ tên", p("keyword", "SĐT / Tên / Mã", "text", "0912"));
        r("UC030", "M02", "TV1", "/customers", vCust, "Xem hồ sơ chi tiết khách hàng và tổng chi tiêu tích lũy", p("customerId", "ID khách hàng", "number", "1"));
        w("UC031", "M02", "TV1", "/customers", vCust, "Thêm mới khách hàng vãng lai mua tại quầy", p("fullName", "Họ tên", "text", "Khách vãng lai mới"), p("phone", "SĐT", "text", "0909888111"), p("gender", "Giới tính", "text", "MALE"));
        w("UC032", "M02", "TV1", "/customers", vCust, "Cập nhật thông tin liên hệ và ngày sinh khách hàng", p("customerId", "ID KH", "number", "1"), p("phone", "SĐT", "text", "0912345678"), p("email", "Email", "text", "khach01@gmail.com"), p("birthDate", "Ngày sinh", "date", "1995-05-20"));
        w("UC033", "M02", "TV1", "/customers", vCust, "Vô hiệu hóa hồ sơ khách hàng vi phạm chính sách", p("customerId", "ID KH", "number", "1"), p("active", "Kích hoạt (true/false)", "text", "true"));
        w("UC034", "M02", "TV1", "/customers", vCust, "Xóa hồ sơ khách hàng chưa phát sinh đơn hàng", p("customerId", "ID KH", "number", "9999"));
        r("UC035", "M02", "TV1", "/customers", vCust, "Xem danh sách hạng thành viên và quyền lợi ưu đãi", p("keyword", "Từ khóa", "text", ""));
        w("UC036", "M02", "TV1", "/customers", vCust, "Thêm mới hạng thành viên (Bạc, Vàng, Kim cương)", p("tierName", "Tên hạng", "text", "VIP Platinum"), p("minSpend", "Chi tiêu tối thiểu", "number", "50000000"), p("discountPercent", "Giảm giá (%)", "number", "12"), p("pointMultiplier", "Hệ số điểm", "number", "2"), p("benefits", "Quyền lợi", "text", "Miễn phí đổi trả & quà sinh nhật"));
        w("UC037", "M02", "TV1", "/customers", vCust, "Cập nhật ngưỡng chi tiêu và mức giảm giá của hạng thành viên", p("tierId", "ID hạng", "number", "1"), p("minSpend", "Chi tiêu tối thiểu", "number", "0"), p("discountPercent", "Giảm giá (%)", "number", "0"));
        w("UC038", "M02", "TV1", "/customers", vCust, "Tự động nâng hạng thành viên theo tổng chi tiêu tích lũy");
        r("UC039", "M02", "TV1", "/customers", vCust, "Xem danh sách nhóm khách hàng chăm sóc", p("keyword", "Từ khóa", "text", ""));
        w("UC040", "M02", "TV1", "/customers", vCust, "Thêm mới nhóm khách hàng (Khách VIP, Khách sỉ, Khách lâu không mua)", p("groupName", "Tên nhóm", "text", "Khách công sở Q3"), p("description", "Mô tả", "text", "Nhóm mua sơ mi định kỳ"));
        w("UC041", "M02", "TV1", "/customers", vCust, "Gán khách hàng vào nhóm chăm sóc", p("customerId", "ID KH", "number", "1"), p("groupId", "ID nhóm", "number", "1"));
        w("UC042", "M02", "TV1", "/customers", vCust, "Lưu số đo cơ thể của khách hàng phục vụ tư vấn size", p("customerId", "ID KH", "number", "1"), p("heightCm", "Chiều cao (cm)", "number", "172"), p("weightKg", "Cân nặng (kg)", "number", "66"), p("chestCm", "Ngực (cm)", "number", "94"), p("waistCm", "Eo (cm)", "number", "78"), p("hipCm", "Mông (cm)", "number", "96"), p("shoulderCm", "Vai (cm)", "number", "44"));
        r("UC043", "M02", "TV1", "/customers", vCust, "Xem số đo gần nhất của khách hàng để gợi ý size", p("customerId", "ID KH", "number", "1"));

        String vCat = "/WEB-INF/views/tv1_nguyen_ngoc_minh/categories.jsp";
        r("UC044", "M03", "TV1", "/categories", vCat, "Xem cây danh mục sản phẩm đa cấp (cha - con)");
        w("UC045", "M03", "TV1", "/categories", vCat, "Thêm mới danh mục sản phẩm kèm slug chuẩn SEO", p("categoryName", "Tên danh mục", "text", "Áo Sơ Mi Dài Tay"), p("slug", "Slug SEO", "text", "ao-so-mi-dai-tay"), p("parentId", "Mã DM cha (0=gốc)", "number", "1"), p("description", "Mô tả", "text", "Dòng sơ mi công sở"), p("displayOrder", "Thứ tự", "number", "1"));
        w("UC046", "M03", "TV1", "/categories", vCat, "Cập nhật tên, mô tả và ảnh đại diện danh mục", p("categoryId", "ID danh mục", "number", "1"), p("categoryName", "Tên danh mục", "text", "Áo Sơ Mi Nam"), p("description", "Mô tả", "text", "Sơ mi Oxford, Bamboo thanh lịch"));
        w("UC047", "M03", "TV1", "/categories", vCat, "Xóa danh mục và kiểm tra sản phẩm, danh mục con đang trực thuộc", p("categoryId", "ID danh mục", "number", "999"));
        r("UC048", "M03", "TV1", "/categories", vCat, "Xem danh sách bộ sưu tập thời trang theo mùa", p("keyword", "Từ khóa", "text", ""));
        w("UC049", "M03", "TV1", "/categories", vCat, "Thêm mới bộ sưu tập Xuân Hè / Thu Đông kèm banner", p("collectionName", "Tên BST", "text", "BST Thu Đông DUNA 2026"), p("season", "Mùa (SPRING_SUMMER/FALL_WINTER/ALL_SEASON)", "text", "FALL_WINTER"), p("startDate", "Từ ngày", "date", "2026-10-01"), p("endDate", "Đến ngày", "date", "2027-02-28"));
        w("UC050", "M03", "TV1", "/categories", vCat, "Cập nhật thời gian áp dụng và banner bộ sưu tập", p("collectionId", "ID BST", "number", "1"), p("endDate", "Ngày kết thúc", "date", "2027-03-31"), p("bannerUrl", "URL Banner", "text", "img/duna-logo.png"));
        w("UC051", "M03", "TV1", "/categories", vCat, "Xóa bộ sưu tập và gỡ liên kết sản phẩm thuộc bộ sưu tập", p("collectionId", "ID BST", "number", "999"));
        r("UC052", "M03", "TV1", "/categories", vCat, "Xem danh sách thương hiệu kèm số lượng sản phẩm", p("keyword", "Từ khóa", "text", ""));
        w("UC053", "M03", "TV1", "/categories", vCat, "Thêm mới thương hiệu hoặc dòng sản phẩm", p("brandName", "Tên thương hiệu", "text", "DUNA Smart Casual"), p("description", "Mô tả", "text", "Dòng thời trang nam hiện đại"));
        r("UC054", "M03", "TV1", "/categories", vCat, "Xem bảng màu sắc chuẩn kèm mã hiển thị hex");
        w("UC055", "M03", "TV1", "/categories", vCat, "Thêm mới màu sắc và mã hex trực quan", p("colorName", "Tên màu", "text", "Xanh Olive"), p("hexCode", "Mã Hex", "text", "#556B2F"), p("colorGroup", "Nhóm màu", "text", "GREEN"));
        w("UC056", "M03", "TV1", "/categories", vCat, "Xóa màu sắc và kiểm tra biến thể đang sử dụng", p("colorId", "ID màu", "number", "999"));
        r("UC057", "M03", "TV1", "/categories", vCat, "Xem danh sách kích cỡ theo loại trang phục (áo, quần, giày)", p("keyword", "Từ khóa", "text", ""));
        w("UC058", "M03", "TV1", "/categories", vCat, "Thêm mới kích cỡ (S, M, L, XL, 2XL, 29, 30, 31)", p("sizeCode", "Mã size", "text", "3XL"), p("categoryType", "Loại (SHIRT/PANTS/SHOES)", "text", "SHIRT"), p("displayOrder", "Thứ tự", "number", "12"));
        w("UC059", "M03", "TV1", "/categories", vCat, "Xóa kích cỡ và kiểm tra ràng buộc với biến thể sản phẩm", p("sizeId", "ID size", "number", "999"));
        r("UC060", "M03", "TV1", "/categories", vCat, "Xem bảng thông số size chuẩn theo danh mục", p("categoryId", "ID danh mục", "number", "1"));
        w("UC061", "M03", "TV1", "/categories", vCat, "Cập nhật khoảng chiều cao, cân nặng, vòng ngực trong bảng size", p("chartDetailId", "ID dòng size", "number", "1"), p("minH", "Cao min", "number", "160"), p("maxH", "Cao max", "number", "168"), p("minChest", "Ngực min", "number", "88"), p("maxChest", "Ngực max", "number", "94"));
        r("UC062", "M03", "TV1", "/categories", vCat, "Tư vấn size tự động theo chiều cao và cân nặng khách nhập", p("categoryId", "ID danh mục", "number", "1"), p("formFit", "Form dáng", "text", "REGULAR_FIT"), p("heightCm", "Chiều cao (cm)", "number", "172"), p("weightKg", "Cân nặng (kg)", "number", "66"));
        w("UC063", "M03", "TV1", "/categories", vCat, "Thêm mới quy tắc tư vấn size cho form dáng Slim fit / Oversize", p("categoryId", "ID danh mục", "number", "1"), p("formFit", "Form", "text", "SLIM_FIT"), p("hMin", "Cao min", "number", "170"), p("hMax", "Cao max", "number", "176"), p("wMin", "Nặng min", "number", "62"), p("wMax", "Nặng max", "number", "68"), p("sizeId", "ID size gợi ý", "number", "1"));

        String vProd = "/WEB-INF/views/tv1_nguyen_ngoc_minh/products.jsp";
        r("UC064", "M03", "TV1", "/products", vProd, "Xem danh sách sản phẩm phân trang và sắp xếp mới nhất", p("keyword", "Từ khóa", "text", ""));
        r("UC065", "M03", "TV1", "/products", vProd, "Tìm kiếm sản phẩm theo mã sản phẩm hoặc tên", p("keyword", "Mã/Tên SP", "text", "SP001"));
        r("UC066", "M03", "TV1", "/products", vProd, "Lọc sản phẩm theo danh mục, bộ sưu tập và khoảng giá", p("categoryId", "ID danh mục (0=tất cả)", "number", "1"), p("minPrice", "Giá từ", "number", "100000"), p("maxPrice", "Giá đến", "number", "1000000"), p("keyword", "Từ khóa", "text", ""));
        r("UC067", "M03", "TV1", "/products", vProd, "Xem chi tiết sản phẩm kèm danh mục, thương hiệu và bộ sưu tập", p("productId", "ID sản phẩm", "number", "1"));
        w("UC068", "M03", "TV1", "/products", vProd, "Thêm mới sản phẩm thời trang nam với mã sản phẩm duy nhất", p("productCode", "Mã SP", "text", "SP101"), p("productName", "Tên SP", "text", "Áo Sơ Mi Bamboo DUNA"), p("categoryId", "ID DM", "number", "1"), p("basePrice", "Giá bán", "number", "420000"), p("costPrice", "Giá vốn", "number", "210000"), p("materialInfo", "Chất liệu", "text", "60% Bamboo, 40% Cotton"), p("formFit", "Form", "text", "REGULAR_FIT"));
        w("UC069", "M03", "TV1", "/products", vProd, "Cập nhật thông tin, chất liệu, form dáng và hướng dẫn giặt ủi", p("productId", "ID SP", "number", "1"), p("materialInfo", "Chất liệu", "text", "100% Cotton Oxford cao cấp"), p("formFit", "Form", "text", "REGULAR_FIT"), p("careInstructions", "Bảo quản", "text", "Giặt nhẹ 30 độ C"));
        w("UC070", "M03", "TV1", "/products", vProd, "Cập nhật giá bán niêm yết và lưu lịch sử thay đổi giá", p("productId", "ID SP", "number", "1"), p("newPrice", "Giá mới (đ)", "number", "390000"), p("reason", "Lý do", "text", "Cập nhật bảng giá niêm yết"));
        w("UC071", "M03", "TV1", "/products", vProd, "Bật tắt trạng thái kinh doanh và đánh dấu sản phẩm nổi bật", p("productId", "ID SP", "number", "1"), p("featured", "Nổi bật (true/false)", "text", "true"));
        w("UC072", "M03", "TV1", "/products", vProd, "Ngừng kinh doanh sản phẩm (xóa mềm) và ẩn toàn bộ biến thể", p("productId", "ID SP", "number", "9999"));
        w("UC073", "M03", "TV1", "/products", vProd, "Xóa vĩnh viễn sản phẩm chưa tạo biến thể", p("productId", "ID SP", "number", "9999"));
        r("UC074", "M03", "TV1", "/products", vProd, "Xem top sản phẩm bán chạy theo số lượng đã bán", p("topN", "Top N", "number", "10"));
        w("UC075", "M03", "TV1", "/products", vProd, "Tăng lượt xem sản phẩm khi khách mở trang chi tiết", p("productId", "ID SP", "number", "1"));

        String vVar = "/WEB-INF/views/tv1_nguyen_ngoc_minh/variants.jsp";
        r("UC076", "M03", "TV1", "/variants", vVar, "Xem danh sách biến thể màu và size kèm tồn kho", p("productId", "ID SP", "number", "1"), p("keyword", "SKU", "text", ""));
        w("UC077", "M03", "TV1", "/variants", vVar, "Thêm mới biến thể sản phẩm ràng buộc duy nhất màu - size", p("productId", "ID SP", "number", "1"), p("colorId", "ID Màu", "number", "1"), p("sizeId", "ID Size", "number", "1"), p("price", "Giá riêng", "number", "390000"));
        w("UC078", "M03", "TV1", "/variants", vVar, "Tự động sinh mã SKU và barcode cho biến thể mới", p("productId", "ID SP", "number", "1"), p("colorId", "ID Màu", "number", "1"), p("sizeId", "ID Size", "number", "1"));
        w("UC079", "M03", "TV1", "/variants", vVar, "Cập nhật giá bán riêng cho biến thể size lớn (2XL, 3XL)", p("variantId", "ID biến thể", "number", "1"), p("price", "Giá mới", "number", "390000"));
        w("UC080", "M03", "TV1", "/variants", vVar, "Xóa biến thể chưa có tồn kho và chưa phát sinh đơn hàng", p("variantId", "ID biến thể", "number", "9999"));
        r("UC081", "M03", "TV1", "/variants", vVar, "Xem thư viện ảnh và video của sản phẩm theo màu sắc", p("productId", "ID SP", "number", "1"));
        w("UC082", "M03", "TV1", "/variants", vVar, "Tải lên ảnh sản phẩm gắn với từng màu sắc", p("productId", "ID SP", "number", "1"), p("mediaUrl", "URL Ảnh", "text", "img/duna-logo.png"), p("displayOrder", "Thứ tự", "number", "2"));
        w("UC083", "M03", "TV1", "/variants", vVar, "Đặt ảnh đại diện chính hiển thị trên website", p("productId", "ID SP", "number", "1"), p("mediaId", "ID ảnh", "number", "1"));
        r("UC084", "M03", "TV1", "/variants", vVar, "Xem danh sách thẻ sản phẩm kèm số sản phẩm được gắn");
        w("UC085", "M03", "TV1", "/variants", vVar, "Gắn thẻ cho sản phẩm (Đi làm, Dạo phố, Mùa hè)", p("productId", "ID SP", "number", "1"), p("tagId", "ID thẻ", "number", "1"));
        w("UC086", "M03", "TV1", "/variants", vVar, "Gỡ thẻ khỏi sản phẩm", p("productId", "ID SP", "number", "1"), p("tagId", "ID thẻ", "number", "999"));

        // ===== TV2: NGUYEN DUC ANH - TH08308 (UC087 - UC151) =====
        String vWh = "/WEB-INF/views/tv2_nguyen_duc_anh/warehouses.jsp";
        r("UC087", "M04", "TV2", "/warehouses", vWh, "Xem lịch sử thay đổi giá của sản phẩm", p("productId", "ID SP (0=tất cả)", "number", "0"), p("keyword", "Từ khóa", "text", ""));
        r("UC088", "M04", "TV2", "/warehouses", vWh, "Xem danh sách kho hàng kèm tổng số lượng tồn từng kho", p("keyword", "Từ khóa", "text", ""));
        w("UC089", "M04", "TV2", "/warehouses", vWh, "Thêm mới kho hàng (kho tổng, kho cửa hàng)", p("warehouseCode", "Mã kho", "text", "KHO-HCM02"), p("warehouseName", "Tên kho", "text", "Kho Quận 1 TP.HCM"), p("address", "Địa chỉ", "text", "Quận 1, TP.HCM"), p("phone", "SĐT", "text", "02838221100"));
        w("UC090", "M04", "TV2", "/warehouses", vWh, "Cập nhật thông tin hoặc ngừng hoạt động kho hàng", p("warehouseId", "ID Kho", "number", "1"), p("address", "Địa chỉ mới", "text", "Kho Tổng DUNA Hà Nội"), p("active", "Hoạt động (true/false)", "text", "true"));

        String vInv = "/WEB-INF/views/tv2_nguyen_duc_anh/inventory.jsp";
        r("UC091", "M04", "TV2", "/inventory", vInv, "Xem tồn kho theo từng biến thể size màu và từng kho", p("warehouseId", "ID Kho (0=tất cả)", "number", "0"));
        r("UC092", "M04", "TV2", "/inventory", vInv, "Tra cứu tồn kho theo SKU hoặc mã vạch", p("keyword", "SKU / Barcode", "text", "SP001"));
        r("UC093", "M04", "TV2", "/inventory", vInv, "Cảnh báo tồn kho dưới ngưỡng an toàn");
        w("UC094", "M04", "TV2", "/inventory", vInv, "Cập nhật ngưỡng cảnh báo hết hàng cho biến thể", p("variantId", "ID biến thể", "number", "1"), p("warehouseId", "ID Kho", "number", "1"), p("threshold", "Ngưỡng tối thiểu", "number", "5"));
        w("UC095", "M04", "TV2", "/inventory", vInv, "Giữ chỗ tồn kho khi khách đặt hàng online", p("variantId", "ID biến thể", "number", "1"), p("warehouseId", "ID Kho", "number", "1"), p("qty", "Số lượng giữ", "number", "1"));
        w("UC096", "M04", "TV2", "/inventory", vInv, "Giải phóng số lượng giữ chỗ khi đơn hàng bị hủy", p("variantId", "ID biến thể", "number", "1"), p("warehouseId", "ID Kho", "number", "1"), p("qty", "Số lượng trả", "number", "1"));
        r("UC097", "M04", "TV2", "/inventory", vInv, "Xem nhật ký xuất nhập tồn kho chi tiết", p("keyword", "Từ khóa SKU", "text", ""));
        r("UC098", "M04", "TV2", "/inventory", vInv, "Lọc nhật ký tồn kho theo loại thay đổi và khoảng ngày", p("changeType", "Loại (IMPORT/SALE/ADJUST)", "text", ""), p("fromDate", "Từ ngày", "date", "2026-01-01"), p("toDate", "Đến ngày", "date", "2027-12-31"));
        w("UC099", "M04", "TV2", "/inventory", vInv, "Điều chỉnh tồn kho thủ công kèm lý do và ghi nhật ký", p("variantId", "ID biến thể", "number", "1"), p("warehouseId", "ID Kho", "number", "1"), p("newQty", "Tồn mới", "number", "50"), p("delta", "Chênh lệch", "number", "0"), p("reason", "Lý do", "text", "Kiểm kê định kỳ"));

        String vProc = "/WEB-INF/views/tv2_nguyen_duc_anh/procurement.jsp";
        r("UC100", "M04", "TV2", "/procurement", vProc, "Xem danh sách nhà cung cấp và xưởng may", p("keyword", "Từ khóa", "text", ""));
        w("UC101", "M04", "TV2", "/procurement", vProc, "Thêm mới nhà cung cấp kèm mã số thuế", p("supplierCode", "Mã NCC", "text", "NCC-HN05"), p("supplierName", "Tên NCC", "text", "Xưởng May Dệt Kim Thăng Long"), p("contactPerson", "Liên hệ", "text", "Anh Hùng"), p("phone", "SĐT", "text", "0911223344"), p("taxCode", "MST", "text", "0109988776"));
        w("UC102", "M04", "TV2", "/procurement", vProc, "Cập nhật thông tin liên hệ và điều khoản thanh toán nhà cung cấp", p("supplierId", "ID NCC", "number", "1"), p("phone", "SĐT mới", "text", "0912000111"), p("paymentTerms", "Điều khoản", "text", "Công nợ 30 ngày"));
        w("UC103", "M04", "TV2", "/procurement", vProc, "Ngừng hợp tác nhà cung cấp và kiểm tra đơn nhập đang mở", p("supplierId", "ID NCC", "number", "999"));
        w("UC104", "M04", "TV2", "/procurement", vProc, "Tạo đơn đặt hàng nhà cung cấp kèm chi tiết biến thể", p("supplierId", "ID NCC", "number", "1"), p("warehouseId", "ID Kho", "number", "1"), p("variantId", "ID biến thể", "number", "1"), p("qty", "Số lượng đặt", "number", "20"), p("costPrice", "Giá nhập", "number", "190000"));
        r("UC105", "M04", "TV2", "/procurement", vProc, "Xem danh sách đơn đặt hàng nhà cung cấp theo trạng thái", p("status", "Trạng thái", "text", ""), p("keyword", "Mã PO", "text", ""));
        w("UC106", "M04", "TV2", "/procurement", vProc, "Xác nhận đơn đặt hàng gửi nhà cung cấp", p("poId", "ID Đơn PO", "number", "1"));
        w("UC107", "M04", "TV2", "/procurement", vProc, "Hủy đơn đặt hàng nhà cung cấp chưa nhận hàng", p("poId", "ID Đơn PO", "number", "999"));
        w("UC108", "M04", "TV2", "/procurement", vProc, "Lập phiếu nhập kho theo đơn đặt hàng và cộng tồn kho", p("poId", "ID Đơn PO", "number", "1"), p("warehouseId", "ID Kho", "number", "1"), p("variantId", "ID biến thể", "number", "1"), p("receivedQty", "SL thực nhận", "number", "10"), p("costPrice", "Giá nhập", "number", "190000"));
        w("UC109", "M04", "TV2", "/procurement", vProc, "Cập nhật số lượng đã nhận và trạng thái đơn đặt hàng (nhận một phần / đủ)", p("poId", "ID Đơn PO", "number", "1"), p("variantId", "ID biến thể", "number", "1"), p("receivedQty", "SL nhận thêm", "number", "5"));
        w("UC110", "M04", "TV2", "/procurement", vProc, "Tạo phiếu điều chuyển hàng giữa các kho", p("fromWh", "Từ Kho ID", "number", "1"), p("toWh", "Đến Kho ID", "number", "2"), p("variantId", "ID biến thể", "number", "1"), p("qty", "Số lượng", "number", "2"));
        w("UC111", "M04", "TV2", "/procurement", vProc, "Hoàn tất điều chuyển kho và cập nhật tồn hai kho", p("transferId", "ID Phiếu chuyển", "number", "1"));
        w("UC112", "M04", "TV2", "/procurement", vProc, "Mở phiếu kiểm kê và ghi nhận số lượng đếm thực tế", p("warehouseId", "ID Kho", "number", "1"));
        w("UC113", "M04", "TV2", "/procurement", vProc, "Chốt kiểm kê, đối chiếu chênh lệch và điều chỉnh tồn kho", p("stocktakeId", "ID Kiểm kê", "number", "1"), p("warehouseId", "ID Kho", "number", "1"));

        String vCart = "/WEB-INF/views/tv2_nguyen_duc_anh/cart.jsp";
        r("UC114", "M05", "TV2", "/cart", vCart, "Xem giỏ hàng hiện tại kèm hình ảnh, màu sắc và size", p("userId", "ID tài khoản", "number", "4"));
        w("UC115", "M05", "TV2", "/cart", vCart, "Thêm sản phẩm vào giỏ hàng và cộng dồn số lượng nếu đã tồn tại", p("cartId", "ID Giỏ hàng", "number", "1"), p("variantId", "ID biến thể", "number", "1"), p("qty", "Số lượng", "number", "1"));
        w("UC116", "M05", "TV2", "/cart", vCart, "Cập nhật số lượng sản phẩm trong giỏ có kiểm tra tồn kho", p("cartId", "ID Giỏ hàng", "number", "1"), p("variantId", "ID biến thể", "number", "1"), p("qty", "SL mới", "number", "2"));
        w("UC117", "M05", "TV2", "/cart", vCart, "Xóa một sản phẩm khỏi giỏ hàng", p("cartId", "ID Giỏ hàng", "number", "1"), p("variantId", "ID biến thể", "number", "1"));
        w("UC118", "M05", "TV2", "/cart", vCart, "Áp dụng mã voucher vào giỏ hàng sau khi kiểm tra điều kiện", p("cartId", "ID Giỏ hàng", "number", "1"), p("voucherCode", "Mã Voucher", "text", "DUNA100"));
        r("UC119", "M05", "TV2", "/cart", vCart, "Tính tổng tiền tạm tính và số lượng sản phẩm của giỏ hàng", p("cartId", "ID Giỏ hàng", "number", "1"));
        w("UC120", "M05", "TV2", "/cart", vCart, "Hợp nhất giỏ hàng khách vãng lai vào giỏ tài khoản sau khi đăng nhập", p("userCartId", "ID Giỏ tài khoản", "number", "1"), p("sessionToken", "Token khách", "text", "GUEST_TOKEN"));
        w("UC121", "M05", "TV2", "/cart", vCart, "Đánh dấu giỏ hàng bị bỏ quên sau 24 giờ không hoạt động");
        r("UC122", "M05", "TV2", "/cart", vCart, "Lấy danh sách giỏ hàng bỏ quên để gửi email nhắc mua", p("minValue", "Giá trị tối thiểu", "number", "0"));
        r("UC123", "M05", "TV2", "/cart", vCart, "Xem danh sách sản phẩm yêu thích của khách hàng", p("userId", "ID tài khoản", "number", "4"), p("keyword", "Từ khóa", "text", ""));
        w("UC124", "M05", "TV2", "/cart", vCart, "Thêm sản phẩm vào danh sách yêu thích", p("userId", "ID tài khoản", "number", "4"), p("productId", "ID SP", "number", "1"));
        w("UC125", "M05", "TV2", "/cart", vCart, "Bỏ sản phẩm khỏi danh sách yêu thích", p("userId", "ID tài khoản", "number", "4"), p("productId", "ID SP", "number", "1"));

        String vOrd = "/WEB-INF/views/tv2_nguyen_duc_anh/orders.jsp";
        r("UC126", "M06", "TV2", "/orders", vOrd, "Xem danh sách đơn hàng phân trang dành cho quản trị", p("keyword", "Từ khóa", "text", ""));
        r("UC127", "M06", "TV2", "/orders", vOrd, "Tìm kiếm đơn hàng theo mã đơn, số điện thoại hoặc tên người nhận", p("keyword", "Mã đơn / SĐT", "text", "DN"));
        r("UC128", "M06", "TV2", "/orders", vOrd, "Lọc đơn hàng theo trạng thái, kênh bán và khoảng ngày đặt", p("status", "Trạng thái", "text", ""), p("channel", "Kênh (POS/WEB)", "text", ""), p("fromDate", "Từ ngày", "date", "2026-01-01"), p("toDate", "Đến ngày", "date", "2027-12-31"));
        r("UC129", "M06", "TV2", "/orders", vOrd, "Xem chi tiết đơn hàng kèm danh sách sản phẩm đã mua", p("orderId", "ID Đơn hàng", "number", "7"));
        r("UC130", "M06", "TV2", "/orders", vOrd, "Xem lịch sử mua hàng của một khách hàng", p("customerId", "ID Khách hàng", "number", "1"), p("keyword", "Từ khóa", "text", ""));
        r("UC131", "M06", "TV2", "/orders", vOrd, "Tạo mã đơn hàng tự động theo ngày (DNyyMMdd-số thứ tự)");
        w("UC132", "M06", "TV2", "/orders", vOrd, "Đặt hàng online từ giỏ hàng: tạo đơn, chi tiết đơn và giữ chỗ tồn kho", p("customerId", "ID KH", "number", "1"), p("cartId", "ID Giỏ hàng", "number", "1"), p("receiverName", "Người nhận", "text", "Trần Văn Nam"), p("receiverPhone", "SĐT", "text", "0912345678"), p("shippingAddress", "Địa chỉ", "text", "Hà Nội"));
        w("UC133", "M06", "TV2", "/orders", vOrd, "Nhân viên tạo đơn hàng hộ khách qua điện thoại", p("customerId", "ID KH", "number", "1"), p("receiverName", "Người nhận", "text", "Khách gọi hotline"), p("receiverPhone", "SĐT", "text", "0912345678"), p("shippingAddress", "Địa chỉ giao", "text", "12 Nguyễn Trãi, Hà Nội"), p("subtotal", "Tiền hàng", "number", "390000"), p("shipFee", "Phí ship", "number", "30000"));
        w("UC134", "M06", "TV2", "/orders", vOrd, "Xác nhận đơn hàng và ghi nhận lịch sử trạng thái", p("orderId", "ID Đơn hàng", "number", "7"));
        w("UC135", "M06", "TV2", "/orders", vOrd, "Chuyển đơn hàng sang trạng thái đóng gói", p("orderId", "ID Đơn hàng", "number", "7"));
        w("UC136", "M06", "TV2", "/orders", vOrd, "Chuyển đơn hàng sang trạng thái đang giao cho đơn vị vận chuyển", p("orderId", "ID Đơn hàng", "number", "7"));
        w("UC137", "M06", "TV2", "/orders", vOrd, "Xác nhận giao hàng thành công và cập nhật thanh toán COD", p("orderId", "ID Đơn hàng", "number", "7"));
        w("UC138", "M06", "TV2", "/orders", vOrd, "Hoàn tất đơn hàng, trừ tồn thực tế và cộng chi tiêu tích lũy cho khách", p("orderId", "ID Đơn hàng", "number", "7"));
        w("UC139", "M06", "TV2", "/orders", vOrd, "Hủy đơn hàng, ghi lý do và hoàn trả tồn kho giữ chỗ", p("orderId", "ID Đơn hàng", "number", "999"), p("reasonCode", "Mã lý do", "text", "CUSTOMER_REQUEST"), p("reasonDetail", "Chi tiết", "text", "Khách đổi ý"));
        w("UC140", "M06", "TV2", "/orders", vOrd, "Thêm ghi chú nội bộ cho đơn hàng", p("orderId", "ID Đơn hàng", "number", "7"), p("content", "Nội dung ghi chú", "text", "Giao giờ hành chính"), p("internal", "Nội bộ (true/false)", "text", "true"));
        r("UC141", "M06", "TV2", "/orders", vOrd, "Xem danh sách ghi chú của đơn hàng kèm người ghi", p("orderId", "ID Đơn hàng", "number", "7"), p("keyword", "Từ khóa", "text", ""));
        w("UC142", "M06", "TV2", "/orders", vOrd, "Cập nhật địa chỉ giao hàng của đơn chưa đóng gói", p("orderId", "ID Đơn hàng", "number", "7"), p("receiverName", "Người nhận", "text", "Trần Văn Nam"), p("phone", "SĐT", "text", "0912345678"), p("address", "Địa chỉ mới", "text", "Số 88 Cầu Giấy, Hà Nội"));
        r("UC143", "M06", "TV2", "/orders", vOrd, "Xem lịch sử thay đổi trạng thái của đơn hàng", p("orderId", "ID Đơn hàng", "number", "7"), p("statusFilter", "Lọc trạng thái", "text", ""));
        w("UC144", "M06", "TV2", "/orders", vOrd, "Xuất hóa đơn bán hàng cho đơn đã hoàn tất", p("orderId", "ID Đơn hàng", "number", "7"), p("buyerName", "Tên người mua", "text", "Trần Văn Nam"), p("companyName", "Công ty", "text", "DUNA Client"), p("taxCode", "MST", "text", "0101234567"));
        r("UC145", "M06", "TV2", "/orders", vOrd, "Tra cứu hóa đơn theo mã đơn hàng hoặc mã số thuế", p("keyword", "Mã hóa đơn / MST", "text", ""));
        w("UC146", "M06", "TV2", "/orders", vOrd, "Hủy hóa đơn đã xuất do sai thông tin", p("invoiceId", "ID Hóa đơn", "number", "999"));

        String vRet = "/WEB-INF/views/tv2_nguyen_duc_anh/returns.jsp";
        w("UC147", "M06", "TV2", "/returns", vRet, "Khách tạo yêu cầu đổi trả hàng kèm sản phẩm cần đổi trả", p("orderId", "ID Đơn hàng", "number", "7"), p("customerId", "ID KH", "number", "1"), p("requestType", "Loại (RETURN/EXCHANGE)", "text", "EXCHANGE"), p("reason", "Lý do", "text", "Chật vai, đổi sang size lớn hơn"));
        r("UC148", "M06", "TV2", "/returns", vRet, "Xem danh sách yêu cầu đổi trả đang chờ xử lý", p("status", "Trạng thái", "text", ""), p("keyword", "Từ khóa", "text", ""));
        w("UC149", "M06", "TV2", "/returns", vRet, "Duyệt yêu cầu đổi trả hàng của khách", p("returnId", "ID Yêu cầu đổi trả", "number", "1"));
        w("UC150", "M06", "TV2", "/returns", vRet, "Từ chối yêu cầu đổi trả và ghi chú lý do", p("returnId", "ID Yêu cầu đổi trả", "number", "999"), p("reasonNote", "Lý do từ chối", "text", "Sản phẩm đã qua sử dụng"));
        w("UC151", "M06", "TV2", "/returns", vRet, "Nhận hàng trả về và nhập lại kho", p("returnId", "ID Yêu cầu", "number", "1"), p("warehouseId", "ID Kho", "number", "1"), p("variantId", "ID biến thể", "number", "1"), p("qty", "SL nhập lại", "number", "1"));

        // ===== TV3: LE MINH BACH - TH08442 (UC152 - UC218) =====
        String vPay = "/WEB-INF/views/tv3_le_minh_bach/payments.jsp";
        w("UC152", "M06", "TV3", "/payments", vPay, "Tạo yêu cầu hoàn tiền sau khi nhận hàng trả", p("orderId", "ID Đơn hàng", "number", "7"), p("amount", "Số tiền hoàn", "number", "390000"), p("method", "Phương thức", "text", "BANK"));
        w("UC153", "M06", "TV3", "/payments", vPay, "Hoàn tất hoàn tiền và cập nhật trạng thái thanh toán đơn hàng", p("refundId", "ID Phiếu hoàn", "number", "1"));
        w("UC154", "M06", "TV3", "/returns", vRet, "Xử lý đổi hàng sang size khác: trừ kho size mới và cộng kho size cũ", p("returnId", "ID Yêu cầu đổi", "number", "1"), p("warehouseId", "ID Kho", "number", "1"));
        w("UC155", "M06", "TV3", "/orders", vOrd, "Tự động hủy đơn hàng quá hạn chưa thanh toán online");
        w("UC156", "M06", "TV3", "/orders", vOrd, "Tự động hoàn tất đơn hàng sau 7 ngày giao thành công");
        r("UC157", "M06", "TV3", "/payments", vPay, "Thống kê số lượng và giá trị đơn hàng theo trạng thái");
        r("UC158", "M06", "TV3", "/orders", vOrd, "Xuất danh sách đơn hàng ra file Excel để đối soát", p("fromDate", "Từ ngày", "date", "2026-01-01"), p("toDate", "Đến ngày", "date", "2027-12-31"));
        r("UC159", "M07", "TV3", "/payments", vPay, "Xem danh sách phương thức thanh toán (COD, VNPay, MoMo, chuyển khoản)", p("keyword", "Từ khóa", "text", ""));
        w("UC160", "M07", "TV3", "/payments", vPay, "Thêm mới phương thức thanh toán", p("methodCode", "Mã PT", "text", "VIETQR"), p("methodName", "Tên PT", "text", "Chuyển khoản VietQR Napas"), p("isOnline", "Online (true/false)", "text", "true"), p("displayOrder", "Thứ tự", "number", "4"));
        w("UC161", "M07", "TV3", "/payments", vPay, "Bật tắt phương thức thanh toán trên trang đặt hàng", p("methodId", "ID Phương thức", "number", "3"));
        w("UC162", "M07", "TV3", "/payments", vPay, "Tạo bản ghi thanh toán khi khách chọn thanh toán COD", p("orderId", "ID Đơn hàng", "number", "7"), p("amount", "Số tiền", "number", "500000"));
        w("UC163", "M07", "TV3", "/payments", vPay, "Tạo yêu cầu thanh toán VNPay và lưu giao dịch chờ xử lý", p("orderId", "ID Đơn hàng", "number", "7"), p("amount", "Số tiền", "number", "500000"));
        w("UC164", "M07", "TV3", "/payments", vPay, "Xử lý kết quả thanh toán VNPay thành công (IPN callback)", p("txnRef", "Mã giao dịch", "text", "TXN-DN20261106"), p("gateway", "Cổng", "text", "VNPAY"), p("gatewayTxnNo", "Mã cổng TT", "text", "VNP999888"), p("amount", "Số tiền", "number", "500000"));
        w("UC165", "M07", "TV3", "/payments", vPay, "Xử lý thanh toán thất bại hoặc hết hạn thanh toán online", p("paymentId", "ID Giao dịch", "number", "1"), p("gateway", "Cổng", "text", "VNPAY"), p("responseCode", "Mã lỗi", "text", "24"), p("amount", "Số tiền", "number", "500000"));
        w("UC166", "M07", "TV3", "/payments", vPay, "Tạo yêu cầu thanh toán qua ví MoMo", p("orderId", "ID Đơn hàng", "number", "7"), p("amount", "Số tiền", "number", "500000"));
        r("UC167", "M07", "TV3", "/payments", vPay, "Xem lịch sử các giao dịch thanh toán của một đơn hàng", p("orderId", "ID Đơn hàng (0=tất cả)", "number", "0"), p("keyword", "Từ khóa", "text", ""));
        w("UC168", "M07", "TV3", "/payments", vPay, "Tạo phiên đối soát COD với đơn vị vận chuyển theo kỳ", p("carrierId", "ID ĐVVC", "number", "1"), p("periodFrom", "Từ ngày", "date", "2026-11-01"), p("periodTo", "Đến ngày", "date", "2026-11-30"));
        w("UC169", "M07", "TV3", "/payments", vPay, "Xác nhận hoàn tất đối soát COD và ghi nhận tiền về", p("reconciliationId", "ID Phiên đối soát", "number", "1"));
        w("UC170", "M07", "TV3", "/payments", vPay, "Hoàn tiền qua cổng thanh toán cho đơn đã thanh toán online", p("paymentId", "ID Thanh toán", "number", "1"), p("gateway", "Cổng", "text", "VNPAY"), p("amount", "Số tiền hoàn", "number", "100000"));

        String vShip = "/WEB-INF/views/tv3_le_minh_bach/shipping.jsp";
        r("UC171", "M08", "TV3", "/shipping", vShip, "Xem danh sách tỉnh thành (34 đơn vị cấp tỉnh) kèm vùng giao hàng", p("keyword", "Tên tỉnh", "text", ""));
        w("UC172", "M08", "TV3", "/shipping", vShip, "Thêm mới tỉnh thành phố vào danh mục địa lý", p("provinceCode", "Mã tỉnh", "text", "99"), p("provinceName", "Tên tỉnh", "text", "Khu đô thị mở rộng"), p("zoneId", "ID Vùng", "number", "1"));
        w("UC173", "M08", "TV3", "/shipping", vShip, "Gán tỉnh thành vào vùng giao hàng để tính phí vận chuyển", p("provinceId", "ID Tỉnh", "number", "1"), p("zoneId", "ID Vùng", "number", "1"));
        r("UC174", "M08", "TV3", "/shipping", vShip, "Xem danh sách xã, phường, đặc khu theo tỉnh thành", p("provinceId", "ID Tỉnh", "number", "1"), p("keyword", "Từ khóa", "text", ""));
        r("UC175", "M08", "TV3", "/shipping", vShip, "Tìm kiếm xã phường theo tên gợi ý khi nhập địa chỉ", p("keyword", "Tên phường/xã", "text", "Láng"));
        w("UC176", "M08", "TV3", "/shipping", vShip, "Thêm mới xã phường đặc khu sau khi sắp xếp đơn vị hành chính", p("provinceId", "ID Tỉnh", "number", "1"), p("wardCode", "Mã phường", "text", "00999"), p("wardName", "Tên phường/xã", "text", "Phường Mới"), p("wardType", "Loại (WARD/COMMUNE)", "text", "WARD"));
        w("UC177", "M08", "TV3", "/shipping", vShip, "Xóa xã phường khi chưa được sử dụng trong địa chỉ và đơn hàng", p("wardId", "ID Phường/Xã", "number", "99999"));
        r("UC178", "M08", "TV3", "/shipping", vShip, "Xem danh sách vùng giao hàng kèm số tỉnh thành", p("keyword", "Từ khóa", "text", ""));
        w("UC179", "M08", "TV3", "/shipping", vShip, "Thêm mới vùng giao hàng (nội thành, liên tỉnh, vùng xa)", p("zoneName", "Tên vùng", "text", "Vùng Đặc Khu"), p("description", "Mô tả", "text", "Khu vực hải đảo & đặc khu"));
        r("UC180", "M08", "TV3", "/shipping", vShip, "Xem danh sách đơn vị vận chuyển đang hợp tác", p("keyword", "Từ khóa", "text", ""));
        w("UC181", "M08", "TV3", "/shipping", vShip, "Thêm mới đơn vị vận chuyển kèm địa chỉ API tích hợp", p("carrierCode", "Mã ĐVVC", "text", "JNT"), p("carrierName", "Tên ĐVVC", "text", "J&T Express"), p("apiEndpoint", "API URL", "text", "https://api.jtexpress.vn"), p("phone", "Hotline", "text", "19001088"));
        r("UC182", "M08", "TV3", "/shipping", vShip, "Xem danh sách dịch vụ vận chuyển theo từng đơn vị", p("keyword", "Từ khóa", "text", ""));
        w("UC183", "M08", "TV3", "/shipping", vShip, "Thêm mới dịch vụ vận chuyển (tiêu chuẩn, hỏa tốc)", p("carrierId", "ID ĐVVC", "number", "1"), p("methodName", "Tên dịch vụ", "text", "Giao hỏa tốc 4h"), p("minDays", "Min ngày", "number", "0"), p("maxDays", "Max ngày", "number", "1"));
        r("UC184", "M08", "TV3", "/shipping", vShip, "Xem bảng phí vận chuyển theo vùng và khối lượng");
        w("UC185", "M08", "TV3", "/shipping", vShip, "Cập nhật phí vận chuyển và ngưỡng miễn phí giao hàng", p("feeId", "ID Biểu phí", "number", "1"), p("baseFee", "Phí cơ bản (đ)", "number", "25000"), p("freeShipThreshold", "Ngưỡng freeship (đ)", "number", "500000"));
        r("UC186", "M08", "TV3", "/shipping", vShip, "Tính phí vận chuyển cho đơn hàng theo tỉnh nhận và khối lượng", p("provinceId", "ID Tỉnh", "number", "1"), p("shippingMethodId", "ID Dịch vụ", "number", "1"), p("weightGram", "Khối lượng (g)", "number", "500"), p("orderAmount", "Giá trị đơn (đ)", "number", "390000"));
        w("UC187", "M08", "TV3", "/shipping", vShip, "Tạo vận đơn gửi đơn vị vận chuyển cho đơn hàng", p("orderId", "ID Đơn hàng", "number", "7"), p("carrierId", "ID ĐVVC", "number", "1"), p("shippingMethodId", "ID Dịch vụ", "number", "1"), p("trackingCode", "Mã vận đơn", "text", "VD-DN202601"), p("shippingFee", "Phí ship", "number", "25000"), p("codAmount", "Tiền thu hộ COD", "number", "0"));
        w("UC188", "M08", "TV3", "/shipping", vShip, "Cập nhật trạng thái vận đơn theo webhook của đơn vị vận chuyển", p("trackingCode", "Mã vận đơn", "text", "VD-DN202601"), p("newStatus", "Trạng thái", "text", "IN_TRANSIT"), p("location", "Vị trí", "text", "Kho trung chuyển Hà Nội"), p("note", "Ghi chú", "text", "Đang giao"));
        r("UC189", "M08", "TV3", "/shipping", vShip, "Tra cứu hành trình vận đơn theo mã vận đơn", p("trackingCode", "Mã vận đơn", "text", "VD"));
        r("UC190", "M08", "TV3", "/shipping", vShip, "Xem danh sách vận đơn giao trễ hoặc giao thất bại cần xử lý", p("keyword", "Từ khóa", "text", ""));

        String vPromo = "/WEB-INF/views/tv3_le_minh_bach/promotions.jsp";
        r("UC191", "M09", "TV3", "/promotions", vPromo, "Xem danh sách voucher kèm trạng thái hiệu lực và lượt dùng", p("keyword", "Mã/Tên voucher", "text", ""));
        w("UC192", "M09", "TV3", "/promotions", vPromo, "Thêm mới voucher giảm theo phần trăm, số tiền hoặc miễn phí vận chuyển", p("voucherCode", "Mã Voucher", "text", "DUNA15"), p("voucherName", "Tên CT", "text", "Ưu đãi DUNA 15%"), p("discountType", "Loại (PERCENT/FIXED)", "text", "PERCENT"), p("discountValue", "Giá trị", "number", "15"), p("maxDiscountAmount", "Giảm tối đa", "number", "200000"), p("minOrderValue", "Đơn tối thiểu", "number", "500000"), p("usageLimit", "Số lượt", "number", "100"), p("perUserLimit", "Lượt/người", "number", "1"), p("startDate", "Từ ngày", "date", "2026-01-01"), p("endDate", "Đến ngày", "date", "2027-06-30"));
        w("UC193", "M09", "TV3", "/promotions", vPromo, "Cập nhật điều kiện áp dụng và thời gian hiệu lực voucher", p("voucherId", "ID Voucher", "number", "1"), p("minOrderValue", "Đơn tối thiểu", "number", "500000"), p("endDate", "Hết hạn", "date", "2027-06-30"), p("usageLimit", "Số lượt", "number", "100"));
        w("UC194", "M09", "TV3", "/promotions", vPromo, "Bật tắt trạng thái voucher", p("voucherId", "ID Voucher", "number", "1"));
        w("UC195", "M09", "TV3", "/promotions", vPromo, "Xóa voucher chưa có lượt sử dụng", p("voucherId", "ID Voucher", "number", "999"));
        r("UC196", "M09", "TV3", "/promotions", vPromo, "Kiểm tra mã voucher hợp lệ khi khách nhập tại bước thanh toán", p("keyword", "Mã Voucher", "text", "DUNA100"), p("orderAmount", "Giá trị đơn", "number", "600000"));
        w("UC197", "M09", "TV3", "/promotions", vPromo, "Ghi nhận lượt sử dụng voucher khi đặt hàng thành công", p("voucherId", "ID Voucher", "number", "1"), p("userId", "ID TK", "number", "4"), p("orderId", "ID Đơn", "number", "7"), p("discountAmount", "Tiền giảm", "number", "100000"));
        r("UC198", "M09", "TV3", "/promotions", vPromo, "Thống kê hiệu quả voucher theo số lượt dùng và tổng tiền giảm");
        r("UC199", "M09", "TV3", "/promotions", vPromo, "Xem danh sách chương trình khuyến mãi đang diễn ra", p("keyword", "Từ khóa", "text", ""));
        w("UC200", "M09", "TV3", "/promotions", vPromo, "Thêm mới chương trình khuyến mãi theo sự kiện (Black Friday, 11.11)", p("promotionName", "Tên CT", "text", "DUNA Smart Casual Week"), p("promoType", "Loại (PERCENT/FIXED)", "text", "PERCENT"), p("discountValue", "Mức giảm", "number", "15"), p("startDate", "Từ ngày", "date", "2026-11-01"), p("endDate", "Đến ngày", "date", "2027-01-31"));
        w("UC201", "M09", "TV3", "/promotions", vPromo, "Gán sản phẩm vào chương trình khuyến mãi", p("promotionId", "ID KM", "number", "1"), p("productId", "ID SP", "number", "1"));
        w("UC202", "M09", "TV3", "/promotions", vPromo, "Gỡ sản phẩm khỏi chương trình khuyến mãi", p("promotionId", "ID KM", "number", "1"), p("productId", "ID SP", "number", "999"));
        r("UC203", "M09", "TV3", "/promotions", vPromo, "Tính giá bán sau khuyến mãi của sản phẩm", p("productId", "ID SP", "number", "1"));
        w("UC204", "M09", "TV3", "/promotions", vPromo, "Kết thúc sớm chương trình khuyến mãi", p("promotionId", "ID KM", "number", "999"));
        r("UC205", "M09", "TV3", "/promotions", vPromo, "Xem danh sách các đợt flash sale và trạng thái", p("keyword", "Từ khóa", "text", ""));
        w("UC206", "M09", "TV3", "/promotions", vPromo, "Tạo mới đợt flash sale theo khung giờ vàng", p("title", "Tên đợt Flash Sale", "text", "Flash Sale Cuối Tuần DUNA"), p("startTime", "Bắt đầu", "text", "2026-11-27 20:00"), p("endTime", "Kết thúc", "text", "2026-11-27 22:00"));
        w("UC207", "M09", "TV3", "/promotions", vPromo, "Thêm biến thể sản phẩm vào flash sale kèm giá và giới hạn số lượng", p("flashSaleId", "ID Flash Sale", "number", "1"), p("variantId", "ID biến thể", "number", "1"), p("salePrice", "Giá sale", "number", "299000"), p("qtyLimit", "Giới hạn SL", "number", "20"));
        w("UC208", "M09", "TV3", "/promotions", vPromo, "Tự động cập nhật trạng thái flash sale theo khung giờ");
        w("UC209", "M09", "TV3", "/promotions", vPromo, "Ghi nhận bán hàng flash sale và kiểm tra giới hạn số lượng", p("flashItemId", "ID Flash Item", "number", "1"));
        r("UC210", "M09", "TV3", "/promotions", vPromo, "Xem sản phẩm flash sale đang diễn ra kèm phần trăm giảm");

        String vLoy = "/WEB-INF/views/tv3_le_minh_bach/loyalty.jsp";
        w("UC211", "M10", "TV3", "/loyalty", vLoy, "Tạo tài khoản tích điểm cho khách hàng mới", p("customerId", "ID Khách hàng", "number", "1"));
        r("UC212", "M10", "TV3", "/loyalty", vLoy, "Xem số dư điểm và hạng thành viên của khách hàng", p("customerId", "ID Khách hàng", "number", "2"));
        w("UC213", "M10", "TV3", "/loyalty", vLoy, "Cộng điểm thưởng khi đơn hàng hoàn tất theo hệ số hạng thành viên", p("accountId", "ID Ví điểm", "number", "1"), p("orderId", "ID Đơn", "number", "7"), p("points", "Số điểm cộng", "number", "50"), p("description", "Nội dung", "text", "Tích điểm đơn hoàn tất"));
        w("UC214", "M10", "TV3", "/loyalty", vLoy, "Trừ điểm khi khách dùng điểm để thanh toán đơn hàng", p("accountId", "ID Ví điểm", "number", "2"), p("orderId", "ID Đơn", "number", "7"), p("points", "Số điểm trừ", "number", "10"), p("description", "Nội dung", "text", "Đổi điểm giảm giá đơn"));
        w("UC215", "M10", "TV3", "/loyalty", vLoy, "Điều chỉnh điểm thủ công kèm lý do", p("accountId", "ID Ví điểm", "number", "1"), p("deltaPoints", "Điểm +/-", "number", "20"), p("reason", "Lý do", "text", "Thưởng sinh nhật hội viên"));
        r("UC216", "M10", "TV3", "/loyalty", vLoy, "Xem lịch sử giao dịch điểm thưởng của khách hàng", p("customerId", "ID KH (0=tất cả)", "number", "0"), p("txnType", "Loại (EARN/REDEEM/ADJUST)", "text", ""));
        w("UC217", "M10", "TV3", "/loyalty", vLoy, "Tự động hết hạn điểm thưởng quá 12 tháng");
        r("UC218", "M10", "TV3", "/loyalty", vLoy, "Xem danh sách quà đổi điểm đang áp dụng", p("keyword", "Từ khóa", "text", ""));

        // ===== TV4: NGUYEN KIEU AN - TH08490 (UC219 - UC300) =====
        w("UC219", "M10", "TV4", "/loyalty", vLoy, "Thêm mới quà đổi điểm gắn với voucher", p("rewardName", "Tên quà", "text", "Voucher DUNA 100K"), p("pointsRequired", "Điểm đổi", "number", "200"), p("stockQuantity", "Số lượng kho", "number", "50"));
        w("UC220", "M10", "TV4", "/loyalty", vLoy, "Khách hàng đổi quà bằng điểm thưởng", p("accountId", "ID Ví điểm", "number", "3"), p("rewardId", "ID Quà", "number", "1"), p("pointsCost", "Điểm trừ", "number", "100"));

        String vRev = "/WEB-INF/views/tv4_nguyen_kieu_an/reviews.jsp";
        r("UC221", "M11", "TV4", "/reviews", vRev, "Xem đánh giá đã duyệt của sản phẩm kèm ảnh khách hàng", p("productId", "ID SP", "number", "1"));
        w("UC222", "M11", "TV4", "/reviews", vRev, "Gửi đánh giá sản phẩm sau khi mua hàng thành công", p("productId", "ID SP", "number", "1"), p("rating", "Số sao (1-5)", "number", "5"), p("fitFeedback", "Form (TOO_SMALL/TRUE_TO_SIZE/TOO_LARGE)", "text", "TRUE_TO_SIZE"), p("content", "Nhận xét", "text", "Vải mát, đứng form rất đẹp"));
        w("UC223", "M11", "TV4", "/reviews", vRev, "Tải ảnh thực tế của khách lên đánh giá", p("reviewId", "ID Đánh giá", "number", "1"), p("mediaUrl", "URL Ảnh", "text", "img/duna-logo.png"));
        r("UC224", "M11", "TV4", "/reviews", vRev, "Xem danh sách đánh giá chờ kiểm duyệt", p("keyword", "Từ khóa", "text", ""));
        w("UC225", "M11", "TV4", "/reviews", vRev, "Duyệt đánh giá để hiển thị công khai", p("reviewId", "ID Đánh giá", "number", "1"));
        w("UC226", "M11", "TV4", "/reviews", vRev, "Từ chối đánh giá vi phạm nội quy", p("reviewId", "ID Đánh giá", "number", "999"));
        w("UC227", "M11", "TV4", "/reviews", vRev, "Shop phản hồi đánh giá của khách hàng", p("reviewId", "ID Đánh giá", "number", "1"), p("content", "Nội dung trả lời", "text", "DUNA cảm ơn quý khách đã tin tưởng!"));
        r("UC228", "M11", "TV4", "/reviews", vRev, "Thống kê điểm đánh giá trung bình và phản hồi về size của sản phẩm");

        String vSup = "/WEB-INF/views/tv4_nguyen_kieu_an/support.jsp";
        w("UC229", "M11", "TV4", "/support", vSup, "Khách hàng tạo yêu cầu hỗ trợ gắn với đơn hàng", p("customerId", "ID KH", "number", "1"), p("orderId", "ID Đơn", "number", "7"), p("category", "Phân loại (SIZE_ADVICE/ORDER/RETURN)", "text", "SIZE_ADVICE"), p("subject", "Tiêu đề", "text", "Hỗ trợ chọn size sơ mi"), p("priority", "Ưu tiên (LOW/NORMAL/HIGH/URGENT)", "text", "NORMAL"));
        r("UC230", "M11", "TV4", "/support", vSup, "Xem danh sách yêu cầu hỗ trợ theo trạng thái và mức ưu tiên", p("keyword", "Mã Ticket", "text", ""));
        w("UC231", "M11", "TV4", "/support", vSup, "Phân công nhân viên chăm sóc xử lý yêu cầu hỗ trợ", p("ticketId", "ID Ticket", "number", "1"), p("staffUserId", "ID Nhân viên", "number", "3"));
        w("UC232", "M11", "TV4", "/support", vSup, "Gửi tin nhắn phản hồi trong yêu cầu hỗ trợ", p("ticketId", "ID Ticket", "number", "1"), p("content", "Nội dung tin nhắn", "text", "DUNA đã tiếp nhận và tư vấn size phù hợp cho anh."));
        r("UC233", "M11", "TV4", "/support", vSup, "Xem toàn bộ hội thoại của một yêu cầu hỗ trợ", p("ticketId", "ID Ticket", "number", "1"), p("keyword", "Từ khóa", "text", ""));
        w("UC234", "M11", "TV4", "/support", vSup, "Đóng yêu cầu hỗ trợ sau khi giải quyết xong", p("ticketId", "ID Ticket", "number", "999"));
        r("UC235", "M11", "TV4", "/support", vSup, "Xem danh sách câu hỏi thường gặp theo nhóm", p("keyword", "Từ khóa", "text", ""));
        w("UC236", "M11", "TV4", "/support", vSup, "Thêm mới câu hỏi thường gặp (chính sách đổi trả, hướng dẫn chọn size)", p("faqGroup", "Nhóm FAQ", "text", "Đổi trả"), p("question", "Câu hỏi", "text", "DUNA hỗ trợ đổi size trong bao lâu?"), p("answer", "Trả lời", "text", "Hỗ trợ đổi size trong 15 ngày trên toàn hệ thống."));

        String vMkt = "/WEB-INF/views/tv4_nguyen_kieu_an/marketing.jsp";
        r("UC237", "M12", "TV4", "/marketing", vMkt, "Xem danh sách banner theo vị trí hiển thị trên website", p("keyword", "Vị trí", "text", ""));
        w("UC238", "M12", "TV4", "/marketing", vMkt, "Thêm mới banner quảng cáo kèm ảnh và liên kết", p("title", "Tiêu đề Banner", "text", "BST Công Sở DUNA 2026"), p("imageUrl", "URL Ảnh", "text", "img/duna-logo.png"), p("linkUrl", "Liên kết", "text", "/ProductServlet"), p("position", "Vị trí (HOME_HERO/SIDEBAR)", "text", "HOME_HERO"), p("startDate", "Từ ngày", "date", "2026-11-01"), p("endDate", "Đến ngày", "date", "2027-03-31"));
        w("UC239", "M12", "TV4", "/marketing", vMkt, "Cập nhật thời gian hiển thị và thứ tự banner", p("bannerId", "ID Banner", "number", "1"), p("displayOrder", "Thứ tự", "number", "1"), p("endDate", "Ngày kết thúc", "date", "2027-06-30"));
        w("UC240", "M12", "TV4", "/marketing", vMkt, "Tự động ẩn banner hết hạn hiển thị");
        r("UC241", "M12", "TV4", "/marketing", vMkt, "Xem danh sách bài viết phối đồ theo chuyên mục", p("keyword", "Từ khóa", "text", ""));
        w("UC242", "M12", "TV4", "/marketing", vMkt, "Soạn bài viết mới ở trạng thái nháp", p("title", "Tiêu đề bài", "text", "Cách phối Áo Polo & Quần Âu"), p("slug", "Slug", "text", "cach-phoi-ao-polo-quan-au"), p("summary", "Tóm tắt", "text", "Gợi ý phong cách lịch lãm mỗi ngày"), p("content", "Nội dung", "text", "Chọn màu trung tính kết hợp thắt lưng da."));
        w("UC243", "M12", "TV4", "/marketing", vMkt, "Xuất bản bài viết lên website", p("postId", "ID Bài viết", "number", "1"));
        r("UC244", "M12", "TV4", "/marketing", vMkt, "Xem bài viết theo đường dẫn và tăng lượt xem", p("slug", "Slug bài viết", "text", "cach-phoi-ao-polo-quan-au"));
        w("UC245", "M12", "TV4", "/marketing", vMkt, "Thêm mới chuyên mục bài viết", p("name", "Tên chuyên mục", "text", "Phối đồ công sở"), p("slug", "Slug", "text", "phoi-do-cong-so"));
        w("UC246", "M12", "TV4", "/marketing", vMkt, "Đăng ký nhận bản tin khuyến mãi qua email", p("email", "Email đăng ký", "text", "subscriber@duna.vn"));

        String vNot = "/WEB-INF/views/tv4_nguyen_kieu_an/notifications.jsp";
        r("UC247", "M13", "TV4", "/notifications", vNot, "Xem danh sách mẫu thông báo theo kênh gửi", p("channel", "Kênh (EMAIL/SMS/INAPP)", "text", ""), p("keyword", "Mã mẫu", "text", ""));
        w("UC248", "M13", "TV4", "/notifications", vNot, "Thêm mới mẫu email xác nhận đơn hàng", p("templateCode", "Mã mẫu", "text", "ORDER_CONFIRMED_V2"), p("channel", "Kênh", "text", "EMAIL"), p("subject", "Tiêu đề", "text", "DUNA xác nhận đơn hàng"), p("body", "Nội dung", "text", "Cảm ơn quý khách đã mua sắm tại DUNA."));
        w("UC249", "M13", "TV4", "/notifications", vNot, "Cập nhật nội dung mẫu thông báo", p("templateId", "ID Mẫu", "number", "1"), p("subject", "Tiêu đề mới", "text", "DUNA - Cập nhật đơn hàng"), p("body", "Nội dung mới", "text", "Đơn hàng của bạn đang được xử lý."));
        w("UC250", "M13", "TV4", "/notifications", vNot, "Tạo thông báo trong ứng dụng khi đơn hàng đổi trạng thái", p("orderId", "ID Đơn", "number", "7"), p("templateId", "ID Mẫu", "number", "1"), p("title", "Tiêu đề TB", "text", "Đơn hàng đã xác nhận"));
        r("UC251", "M13", "TV4", "/notifications", vNot, "Xem danh sách thông báo chưa đọc của người dùng", p("userId", "ID TK", "number", "1"), p("keyword", "Từ khóa", "text", ""));
        w("UC252", "M13", "TV4", "/notifications", vNot, "Đánh dấu thông báo đã đọc", p("notificationId", "ID Thông báo", "number", "1"));
        w("UC253", "M13", "TV4", "/notifications", vNot, "Tự động xóa thông báo đã đọc cũ hơn 90 ngày");
        w("UC254", "M13", "TV4", "/notifications", vNot, "Tạo chiến dịch email marketing gửi theo nhóm khách hàng", p("campaignName", "Tên chiến dịch", "text", "Tri ân Hội viên VIP Tháng 11"), p("templateId", "ID Mẫu", "number", "1"), p("targetGroupId", "ID Nhóm KH", "number", "1"));
        w("UC255", "M13", "TV4", "/notifications", vNot, "Lên lịch gửi chiến dịch email và cập nhật trạng thái sau khi gửi", p("campaignId", "ID Chiến dịch", "number", "1"), p("scheduledAt", "Thời gian gửi", "text", "2026-11-20 09:00"));
        r("UC256", "M13", "TV4", "/notifications", vNot, "Xem nhật ký gửi email và SMS thất bại để gửi lại", p("keyword", "Email/SĐT nhận", "text", ""));

        String vPos = "/WEB-INF/views/tv4_nguyen_kieu_an/pos.jsp";
        r("UC257", "M14", "TV4", "/pos", vPos, "Xem danh sách cửa hàng và kho liên kết", p("keyword", "Từ khóa", "text", ""));
        w("UC258", "M14", "TV4", "/pos", vPos, "Thêm mới cửa hàng bán lẻ", p("storeCode", "Mã CH", "text", "DUNA-HP01"), p("storeName", "Tên cửa hàng", "text", "DUNA Hải Phòng"), p("address", "Địa chỉ", "text", "Lê Lợi, Hải Phòng"), p("phone", "SĐT", "text", "02253888999"));
        w("UC259", "M14", "TV4", "/pos", vPos, "Cập nhật giờ mở cửa và kho xuất hàng của cửa hàng", p("storeId", "ID CH", "number", "1"), p("openTime", "Giờ mở", "text", "08:30"), p("closeTime", "Giờ đóng", "text", "22:00"), p("warehouseId", "ID Kho", "number", "1"));
        w("UC260", "M14", "TV4", "/pos", vPos, "Mở ca bán hàng tại quầy và nhập tiền đầu ca", p("storeId", "ID CH", "number", "1"), p("openingCash", "Tiền đầu ca (đ)", "number", "2000000"));
        r("UC261", "M14", "TV4", "/pos", vPos, "Tra cứu sản phẩm tại quầy bằng quét mã vạch", p("keyword", "Barcode / SKU", "text", "8936"), p("warehouseId", "ID Kho", "number", "1"));
        w("UC262", "M14", "TV4", "/pos", vPos, "Bán hàng tại quầy: tạo đơn POS, trừ tồn kho và ghi nhận tiền mặt", p("storeId", "ID CH", "number", "1"), p("warehouseId", "ID Kho", "number", "1"), p("sessionId", "ID Ca POS", "number", "1"), p("variantId", "ID Biến thể", "number", "1"), p("quantity", "SL", "number", "1"));
        w("UC263", "M14", "TV4", "/pos", vPos, "Ghi nhận thu chi phát sinh trong két tiền mặt", p("posSessionId", "ID Ca POS", "number", "1"), p("txnType", "Loại (CASH_IN/CASH_OUT)", "text", "CASH_OUT"), p("amount", "Số tiền", "number", "50000"), p("note", "Ghi chú", "text", "Mua túi giấy gói hàng"));
        w("UC264", "M14", "TV4", "/pos", vPos, "Đóng ca bán hàng và đối chiếu tiền mặt thực tế", p("sessionId", "ID Ca POS", "number", "999"), p("closingCash", "Tiền chốt két", "number", "2000000"));
        r("UC265", "M14", "TV4", "/pos", vPos, "Báo cáo doanh thu theo ca bán hàng", p("storeId", "ID CH (0=tất cả)", "number", "0"));
        r("UC266", "M14", "TV4", "/pos", vPos, "Xem lịch sử ca bán hàng theo thu ngân", p("cashierId", "ID Thu ngân (0=tất cả)", "number", "0"), p("statusFilter", "Trạng thái (OPEN/CLOSED)", "text", ""));

        String vHr = "/WEB-INF/views/tv4_nguyen_kieu_an/hr.jsp";
        r("UC267", "M15", "TV4", "/hr", vHr, "Xem danh sách nhân viên theo cửa hàng và phòng ban", p("keyword", "Tên / Mã NV", "text", ""));
        w("UC268", "M15", "TV4", "/hr", vHr, "Thêm mới nhân viên và liên kết tài khoản đăng nhập", p("employeeCode", "Mã NV", "text", "NV-TEST01"), p("fullName", "Họ tên", "text", "Phạm Minh Tuấn"), p("phone", "SĐT", "text", "0977665544"), p("email", "Email", "text", "tuan.nv@duna.vn"), p("birthDate", "Ngày sinh", "date", "1999-04-12"), p("storeId", "ID CH", "number", "1"));
        w("UC269", "M15", "TV4", "/hr", vHr, "Cập nhật chức vụ và chuyển công tác nhân viên sang cửa hàng khác", p("employeeId", "ID NV", "number", "1"), p("positionId", "ID Chức vụ", "number", "1"), p("storeId", "ID CH mới", "number", "1"));
        w("UC270", "M15", "TV4", "/hr", vHr, "Cho nhân viên nghỉ việc và khóa tài khoản đăng nhập", p("employeeId", "ID NV", "number", "999"));
        r("UC271", "M15", "TV4", "/hr", vHr, "Xem danh sách phòng ban và chức vụ kèm lương cơ bản", p("keyword", "Từ khóa", "text", ""));
        w("UC272", "M15", "TV4", "/hr", vHr, "Thêm mới chức vụ kèm mức lương cơ bản", p("positionName", "Tên chức vụ", "text", "Trưởng ca POS"), p("baseSalary", "Lương cơ bản", "number", "10500000"));
        w("UC273", "M15", "TV4", "/hr", vHr, "Thêm mới ca làm việc (ca sáng, ca chiều, ca tối)", p("shiftName", "Tên ca", "text", "Ca Tối Cuối Tuần"), p("startTime", "Bắt đầu", "text", "18:00"), p("endTime", "Kết thúc", "text", "22:00"));
        w("UC274", "M15", "TV4", "/hr", vHr, "Phân ca làm việc cho nhân viên theo ngày", p("employeeId", "ID NV", "number", "1"), p("shiftId", "ID Ca", "number", "1"), p("storeId", "ID CH", "number", "1"), p("workDate", "Ngày làm", "date", "2026-11-18"));
        r("UC275", "M15", "TV4", "/hr", vHr, "Xem lịch phân ca của cửa hàng theo tuần", p("storeId", "ID CH (0=tất cả)", "number", "0"), p("fromDate", "Từ ngày", "date", "2026-01-01"), p("toDate", "Đến ngày", "date", "2027-12-31"));
        w("UC276", "M15", "TV4", "/hr", vHr, "Chấm công vào ca và đánh dấu đi trễ", p("employeeId", "ID NV", "number", "1"), p("workDate", "Ngày làm", "date", "2026-11-17"));
        w("UC277", "M15", "TV4", "/hr", vHr, "Chấm công ra ca và ghi nhận giờ kết thúc", p("employeeId", "ID NV", "number", "1"), p("workDate", "Ngày làm", "date", "2026-11-17"));
        r("UC278", "M15", "TV4", "/hr", vHr, "Báo cáo chấm công tháng: số ngày đi làm, đi trễ và vắng mặt", p("fromDate", "Từ ngày", "date", "2026-01-01"), p("toDate", "Đến ngày", "date", "2027-12-31"));

        String vFin = "/WEB-INF/views/tv4_nguyen_kieu_an/finance.jsp";
        r("UC279", "M16", "TV4", "/finance", vFin, "Xem danh sách chi phí vận hành theo khoảng ngày", p("fromDate", "Từ ngày", "date", "2026-01-01"), p("toDate", "Đến ngày", "date", "2027-12-31"), p("keyword", "Loại chi phí", "text", ""));
        w("UC280", "M16", "TV4", "/finance", vFin, "Thêm mới loại chi phí (thuê mặt bằng, marketing, lương)", p("categoryName", "Tên loại chi phí", "text", "Chi phí quảng cáo TikTok"));
        w("UC281", "M16", "TV4", "/finance", vFin, "Ghi nhận khoản chi phí phát sinh", p("categoryId", "ID Loại CP", "number", "1"), p("storeId", "ID CH", "number", "1"), p("amount", "Số tiền", "number", "1500000"), p("expenseDate", "Ngày chi", "date", "2026-11-17"), p("description", "Nội dung", "text", "Chi phí điện nước cửa hàng"));
        w("UC282", "M16", "TV4", "/finance", vFin, "Cập nhật số tiền và nội dung khoản chi phí", p("expenseId", "ID Chi phí", "number", "1"), p("amount", "Số tiền mới", "number", "1500000"), p("description", "Nội dung mới", "text", "Đã điều chỉnh chứng từ"));
        w("UC283", "M16", "TV4", "/finance", vFin, "Xóa khoản chi phí nhập sai", p("expenseId", "ID Chi phí", "number", "9999"));
        w("UC284", "M16", "TV4", "/finance", vFin, "Tự động tổng hợp doanh thu ngày hôm trước vào bảng báo cáo");
        r("UC285", "M16", "TV4", "/finance", vFin, "Xem bảng tổng hợp doanh thu theo ngày và kênh bán", p("fromDate", "Từ ngày", "date", "2026-01-01"), p("toDate", "Đến ngày", "date", "2027-12-31"));
        r("UC286", "M16", "TV4", "/finance", vFin, "Báo cáo doanh thu theo tháng trong năm", p("year", "Năm", "number", "2026"));
        r("UC287", "M16", "TV4", "/finance", vFin, "Báo cáo doanh thu theo danh mục sản phẩm");
        r("UC288", "M16", "TV4", "/finance", vFin, "Báo cáo doanh thu theo kênh bán và theo cửa hàng");
        r("UC289", "M16", "TV4", "/finance", vFin, "Top 10 sản phẩm bán chạy theo doanh thu trong kỳ", p("fromDate", "Từ ngày", "date", "2026-01-01"), p("toDate", "Đến ngày", "date", "2027-12-31"));
        r("UC290", "M16", "TV4", "/finance", vFin, "Báo cáo lợi nhuận gộp theo sản phẩm (doanh thu trừ giá vốn)");
        r("UC291", "M16", "TV4", "/finance", vFin, "Báo cáo giá trị tồn kho theo từng kho hàng");
        r("UC292", "M16", "TV4", "/finance", vFin, "Danh sách top khách hàng VIP chi tiêu cao nhất");
        r("UC293", "M16", "TV4", "/finance", vFin, "Báo cáo tỷ lệ hủy đơn và đổi trả theo tháng", p("year", "Năm", "number", "2026"));
        r("UC294", "M16", "TV4", "/finance", vFin, "Thống kê khách hàng mới theo tháng");
        r("UC295", "M16", "TV4", "/finance", vFin, "Báo cáo lợi nhuận ròng tháng (doanh thu trừ giá vốn và chi phí vận hành)", p("fromDate", "Từ ngày", "date", "2026-01-01"), p("toDate", "Đến ngày", "date", "2027-12-31"));
        r("UC296", "M16", "TV4", "/finance", vFin, "Bảng điều khiển tổng quan: đơn hôm nay, doanh thu, tồn thấp, hỗ trợ và đổi trả chờ xử lý");

        String vSys = "/WEB-INF/views/tv4_nguyen_kieu_an/system.jsp";
        r("UC297", "M17", "TV4", "/system", vSys, "Xem danh sách cấu hình hệ thống", p("keyword", "Từ khóa", "text", ""));
        w("UC298", "M17", "TV4", "/system", vSys, "Cập nhật giá trị cấu hình (VAT, ngưỡng miễn phí vận chuyển, thời gian giữ hàng)", p("settingKey", "Khóa cấu hình", "text", "VAT_RATE"), p("settingValue", "Giá trị", "text", "8"));
        r("UC299", "M17", "TV4", "/system", vSys, "Xem danh sách công việc chạy ngầm và kết quả lần chạy gần nhất", p("keyword", "Từ khóa", "text", ""));
        w("UC300", "M17", "TV4", "/system", vSys, "Bật tắt và đổi lịch chạy của công việc chạy ngầm", p("jobName", "Tên Job", "text", "DAILY_REVENUE_JOB"), p("enabled", "Bật (true/false)", "text", "true"), p("cronExpression", "Lịch Cron", "text", "0 0 2 * * ?"));
    }

    private UcRegistry() {
    }

    private static ParamSpec p(String name, String label, String type, String def) {
        return new ParamSpec(name, label, type, def);
    }

    private static void r(String code, String mod, String ownerCode, String route, String jspView, String title, ParamSpec... params) {
        REGISTRY.put(code, new UcMeta(code, mod, ownerLabel(ownerCode), route, jspView, title, "READ",
                params == null ? Collections.<ParamSpec>emptyList() : Arrays.asList(params)));
    }

    private static void w(String code, String mod, String ownerCode, String route, String jspView, String title, ParamSpec... params) {
        REGISTRY.put(code, new UcMeta(code, mod, ownerLabel(ownerCode), route, jspView, title, "WRITE",
                params == null ? Collections.<ParamSpec>emptyList() : Arrays.asList(params)));
    }

    private static String ownerLabel(String ownerCode) {
        if ("TV1".equals(ownerCode)) return "TV1 • Nguyễn Ngọc Minh";
        if ("TV2".equals(ownerCode)) return "TV2 • Nguyễn Đức Anh";
        if ("TV3".equals(ownerCode)) return "TV3 • Lê Minh Bách";
        return "TV4 • Nguyễn Kiều An";
    }

    public static UcMeta findByCode(String code) {
        return code == null ? null : REGISTRY.get(code.toUpperCase());
    }

    public static List<UcMeta> findAll() {
        return new ArrayList<>(REGISTRY.values());
    }

    public static List<UcMeta> findByRoute(String route) {
        List<UcMeta> list = new ArrayList<>();
        if (route == null) return list;
        String normalized = "/stores".equals(route) ? "/pos" : route;
        for (UcMeta m : REGISTRY.values()) {
            if (normalized.equals(m.getRoute())) {
                list.add(m);
            }
        }
        return list;
    }

    public static List<UcMeta> filter(String module, String owner, String keyword) {
        String mod = module == null ? "" : module.trim().toUpperCase();
        String own = owner == null ? "" : owner.trim().toUpperCase();
        String kw = keyword == null ? "" : keyword.trim().toLowerCase();
        List<UcMeta> list = new ArrayList<>();
        for (UcMeta m : REGISTRY.values()) {
            if (!mod.isEmpty() && !mod.equals(m.getModule())) continue;
            if (!own.isEmpty() && !m.getOwner().toUpperCase().startsWith(own)) continue;
            if (!kw.isEmpty() && !m.getCode().toLowerCase().contains(kw) && !m.getTitle().toLowerCase().contains(kw)) continue;
            list.add(m);
        }
        return list;
    }
}
