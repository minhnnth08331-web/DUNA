package com.duna.common.uc;

import com.duna.common.util.PasswordUtil;
import com.duna.tv1_nguyen_ngoc_minh.dao.CustomerDAO;
import com.duna.tv1_nguyen_ngoc_minh.dao.ProductDAO;
import com.duna.tv1_nguyen_ngoc_minh.dao.UserDAO;
import com.duna.tv1_nguyen_ngoc_minh.service.AuthAccountService;
import com.duna.tv1_nguyen_ngoc_minh.service.CatalogProductService;
import com.duna.tv1_nguyen_ngoc_minh.service.CustomerService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.duna.common.uc.UcDispatchSupport.*;

/**
 * Dieu phoi day du UC001 - UC086 (Thanh vien 1: Nguyen Ngoc Minh - TH08331).
 */
public final class Tv1UcDispatcher {
    private static final AuthAccountService authSvc = new AuthAccountService();
    private static final CustomerService custSvc = new CustomerService();
    private static final CatalogProductService prodSvc = new CatalogProductService();
    private static final UserDAO userDao = new UserDAO();
    private static final CustomerDAO custDao = new CustomerDAO();
    private static final ProductDAO prodDao = new ProductDAO();

    private Tv1UcDispatcher() {}

    public static List<Map<String, Object>> execute(String uc, HttpServletRequest req) {
        String kw = str(req, "keyword", "");
        int uid = sessionUser(req);
        switch (uc) {
            case "UC001": return authSvc.listRoles(kw);
            case "UC002": return wrapAffected(uc, authSvc.createRole(str(req, "roleCode", "SUPERVISOR"), str(req, "roleName", "Giám sát"), str(req, "description", "Giám sát cửa hàng")), "Đã tạo vai trò mới", "Mã vai trò đã tồn tại");
            case "UC003": return wrapAffected(uc, authSvc.updateRole(numInt(req, "roleId", 3), str(req, "roleName", "Nhân viên bán hàng"), str(req, "description", "Thu ngân & tư vấn")), "Đã cập nhật vai trò", "Không tìm thấy vai trò");
            case "UC004": return wrapAffected(uc, authSvc.deleteRole(numInt(req, "roleId", 99)), "Đã xóa vai trò", "Vai trò đang có tài khoản sử dụng hoặc không tồn tại");
            case "UC005": return authSvc.listPermissions(kw);
            case "UC006": return wrapAffected(uc, authSvc.createPermission(str(req, "permissionCode", "REPORT_EXPORT"), str(req, "permissionName", "Xuất báo cáo"), str(req, "moduleName", "M16")), "Đã thêm quyền mới", "Mã quyền đã tồn tại");
            case "UC007": return wrapAffected(uc, authSvc.grantPermission(numInt(req, "roleId", 2), numInt(req, "permissionId", 1)), "Đã gán quyền cho vai trò", "Quyền đã tồn tại ở vai trò này");
            case "UC008": return wrapAffected(uc, authSvc.revokePermission(numInt(req, "roleId", 2), numInt(req, "permissionId", 1)), "Đã thu hồi quyền", "Không có liên kết quyền cần thu hồi");
            case "UC009": return authSvc.getRoleMatrix(numInt(req, "roleId", 2));
            case "UC010": return wrapScalar(uc, authSvc.hasPermission(numInt(req, "userId", 1), str(req, "permissionCode", "USER_VIEW")), "Kiểm tra phân quyền RBAC");

            case "UC011": return authSvc.listUsers(kw, 0, 30);
            case "UC012": return authSvc.searchUsers(kw);
            case "UC013": return authSvc.filterUsersPaged(str(req, "roleCode", "STAFF"), str(req, "status", "ACTIVE"), kw, 0, 50);
            case "UC014": return wrapAffected(uc, authSvc.registerCustomer(str(req, "email", "khachmoi@duna.vn"), str(req, "phone", "0988777666"), str(req, "password", "Duna123"), str(req, "fullName", "Khách DUNA Mới")), "Đã đăng ký tài khoản & hồ sơ khách hàng", "Email/SĐT đã tồn tại hoặc mật khẩu chưa đủ mạnh");
            case "UC015": return authSvc.listLoginHistories(kw, false);
            case "UC016": return authSvc.listLoginHistories(kw, true);
            case "UC017": return wrapAffected(uc, authSvc.updateProfileAndSync(numInt(req, "userId", 4), str(req, "fullName", "Trần Văn Nam"), str(req, "phone", "0912345678"), str(req, "email", "khach01@gmail.com")), "Đã cập nhật hồ sơ & đồng bộ", "Không tìm thấy tài khoản");
            case "UC018": return wrapAffected(uc, authSvc.changePassword(numInt(req, "userId", 4), str(req, "oldPassword", "Khach123"), str(req, "newPassword", "Khach123")), "Đã đổi mật khẩu", "Mật khẩu cũ sai hoặc mật khẩu mới chưa đạt chuẩn (>=6 ký tự, có chữ & số)");
            case "UC019": {
                String tok = authSvc.createResetToken(str(req, "email", "khach01@gmail.com"));
                return wrapScalar(uc, tok != null ? tok : "Không tìm thấy email", tok != null ? "Đã sinh token đặt lại mật khẩu (30 phút)" : "Email không tồn tại");
            }
            case "UC020": return wrapAffected(uc, authSvc.adminResetPassword(numInt(req, "userId", 4), str(req, "newPassword", "Khach123"), uid), "Đã đặt lại mật khẩu", "Mật khẩu chưa đạt chuẩn hoặc sai ID");
            case "UC021": return wrapAffected(uc, authSvc.toggleUserStatus(numInt(req, "userId", 4)), "Đã đảo trạng thái khóa/mở (đồng bộ 2 chiều)", "Không tìm thấy tài khoản");
            case "UC022": return wrapAffected(uc, authSvc.deleteUser(numInt(req, "userId", 9999)), "Đã xóa tài khoản chưa phát sinh đơn", "Tài khoản đã phát sinh đơn hàng hoặc không tồn tại");
            case "UC023": return authSvc.listAddresses(numInt(req, "userId", 0));
            case "UC024": return wrapAffected(uc, authSvc.addAddress(numInt(req, "userId", 4), str(req, "receiverName", "Trần Văn Nam"), str(req, "receiverPhone", "0912345678"), numInt(req, "provinceId", 1), numInt(req, "wardId", 1), str(req, "addressLine", "Số 25 Xuân Thủy"), true), "Đã thêm địa chỉ mới", "Không thể thêm địa chỉ");
            case "UC025": return wrapAffected(uc, authSvc.setDefaultAddress(numInt(req, "userId", 4), numInt(req, "addressId", 1), str(req, "addressLine", "Số 25 Xuân Thủy, Cầu Giấy")), "Đã cập nhật & đặt địa chỉ mặc định", "Không tìm thấy địa chỉ thuộc tài khoản");
            case "UC026": return wrapAffected(uc, authSvc.deleteAddress(numInt(req, "addressId", 999)), "Đã xóa địa chỉ phụ", "Không xóa được địa chỉ mặc định hoặc không tồn tại");
            case "UC027": return authSvc.listAuditLogs(str(req, "fromDate", "2026-01-01"), str(req, "toDate", "2027-12-31"), kw);

            case "UC028": return custSvc.listCustomers(kw, 0, 50);
            case "UC029": return custSvc.searchCustomers(kw);
            case "UC030": return wrapMap(custSvc.getCustomerDetail(numInt(req, "customerId", 1)), uc, "Chi tiết khách hàng");
            case "UC031": return wrapAffected(uc, custSvc.addWalkInCustomer(str(req, "fullName", "Khách vãng lai mới"), str(req, "phone", "0909888111"), str(req, "gender", "MALE")), "Đã thêm khách vãng lai & tài khoản liên kết", "SĐT đã tồn tại");
            case "UC032": return wrapAffected(uc, custSvc.updateCustomer(numInt(req, "customerId", 1), str(req, "phone", "0912345678"), str(req, "email", "khach01@gmail.com"), str(req, "birthDate", "1995-05-20")), "Đã cập nhật thông tin khách hàng", "Không tìm thấy khách hàng");
            case "UC033": return wrapAffected(uc, custSvc.setCustomerActive(numInt(req, "customerId", 1), bool(req, "active", true)), "Đã cập nhật trạng thái hồ sơ khách hàng", "Không tìm thấy khách hàng");
            case "UC034": return wrapAffected(uc, custSvc.deleteCustomer(numInt(req, "customerId", 9999)), "Đã xóa khách hàng chưa có đơn", "Khách hàng đã có đơn hàng hoặc không tồn tại");
            case "UC035": return custSvc.listTiers(kw);
            case "UC036": return wrapAffected(uc, custSvc.addTier(str(req, "tierName", "VIP Platinum"), numDouble(req, "minSpend", 50000000), numDouble(req, "discountPercent", 12), numDouble(req, "pointMultiplier", 2), str(req, "benefits", "Ưu đãi cao cấp")), "Đã thêm hạng thành viên", "Hạng đã tồn tại");
            case "UC037": return wrapAffected(uc, custSvc.updateTier(numInt(req, "tierId", 1), numDouble(req, "minSpend", 0), numDouble(req, "discountPercent", 0)), "Đã cập nhật hạng thành viên", "Không tìm thấy hạng");
            case "UC038": return wrapAffected(uc, custSvc.autoUpgradeTiers(), "Đã chạy thăng hạng tự động toàn bộ khách hàng", "Không có bản ghi cập nhật");
            case "UC039": return custSvc.listGroups(kw);
            case "UC040": return wrapAffected(uc, custSvc.addGroup(str(req, "groupName", "Khách công sở Q3"), str(req, "description", "Nhóm mua sơ mi")), "Đã thêm nhóm khách hàng", "Nhóm đã tồn tại");
            case "UC041": return wrapAffected(uc, custSvc.assignToGroup(numInt(req, "customerId", 1), numInt(req, "groupId", 1)), "Đã gán khách hàng vào nhóm", "Không tìm thấy khách hàng/nhóm");
            case "UC042": return wrapAffected(uc, custSvc.saveMeasurement(numInt(req, "customerId", 1), numDouble(req, "heightCm", 172), numDouble(req, "weightKg", 66), numDouble(req, "chestCm", 94), numDouble(req, "waistCm", 78), numDouble(req, "hipCm", 96), numDouble(req, "shoulderCm", 44)), "Đã lưu số đo khách hàng", "Không lưu được số đo");
            case "UC043": return wrapMap(custSvc.getLatestMeasurement(numInt(req, "customerId", 1)), uc, "Số đo gần nhất");

            case "UC044": return prodSvc.listCategoryTree();
            case "UC045": return wrapAffected(uc, prodSvc.createCategory(str(req, "categoryName", "Áo Sơ Mi Dài Tay"), str(req, "slug", "ao-so-mi-dai-tay"), null, str(req, "description", "Sơ mi công sở"), numInt(req, "displayOrder", 11)), "Đã tạo danh mục mới", "Danh mục đã tồn tại");
            case "UC046": return wrapAffected(uc, prodSvc.updateCategory(numInt(req, "categoryId", 1), str(req, "categoryName", "Áo Sơ Mi Nam"), str(req, "description", "Sơ mi Oxford, Bamboo thanh lịch"), ""), "Đã cập nhật danh mục", "Không tìm thấy danh mục");
            case "UC047": return wrapAffected(uc, prodSvc.deleteCategory(numInt(req, "categoryId", 999)), "Đã xóa danh mục", "Danh mục đang có sản phẩm hoặc không tồn tại");
            case "UC048": return prodSvc.listCollections(kw);
            case "UC049": return wrapAffected(uc, prodSvc.createCollection(str(req, "collectionName", "BST Thu Đông 2026"), str(req, "season", "Thu Đông"), "", "", str(req, "startDate", "2026-10-01"), str(req, "endDate", "2027-02-28")), "Đã thêm bộ sưu tập", "Không thêm được bộ sưu tập");
            case "UC050": return wrapAffected(uc, prodSvc.updateCollection(numInt(req, "collectionId", 1), str(req, "endDate", "2027-03-31"), str(req, "bannerUrl", "img/duna-logo.png")), "Đã cập nhật bộ sưu tập", "Không tìm thấy bộ sưu tập");
            case "UC051": return wrapAffected(uc, prodSvc.deleteCollection(numInt(req, "collectionId", 999)), "Đã xóa bộ sưu tập", "Không tìm thấy bộ sưu tập");
            case "UC052": return prodSvc.listBrands(kw);
            case "UC053": return wrapAffected(uc, prodSvc.createBrand(str(req, "brandName", "DUNA Smart Casual"), "", str(req, "description", "Thời trang nam tầm trung")), "Đã thêm thương hiệu", "Thương hiệu đã tồn tại");
            case "UC054": return prodSvc.listColors();
            case "UC055": return wrapAffected(uc, prodSvc.createColor(str(req, "colorName", "Xanh Olive"), str(req, "hexCode", "#556B2F"), str(req, "colorGroup", "GREEN")), "Đã thêm màu sắc", "Màu đã tồn tại");
            case "UC056": return wrapAffected(uc, prodSvc.deleteColor(numInt(req, "colorId", 999)), "Đã xóa màu sắc", "Màu đang gắn với biến thể hoặc không tồn tại");
            case "UC057": return prodSvc.listSizes(kw);
            case "UC058": return wrapAffected(uc, prodSvc.createSize(str(req, "sizeCode", "3XL"), str(req, "categoryType", "SHIRT"), numInt(req, "displayOrder", 12)), "Đã thêm kích cỡ", "Size đã tồn tại");
            case "UC059": return wrapAffected(uc, prodSvc.deleteSize(numInt(req, "sizeId", 999)), "Đã xóa kích cỡ", "Size đang được sử dụng hoặc không tồn tại");
            case "UC060": return prodSvc.getSizeChart(numInt(req, "categoryId", 1));
            case "UC061": return wrapAffected(uc, prodSvc.updateSizeChart(numInt(req, "chartDetailId", 1), numDouble(req, "minH", 160), numDouble(req, "maxH", 168), numDouble(req, "minChest", 88), numDouble(req, "maxChest", 94)), "Đã cập nhật thông số bảng size", "Không tìm thấy dòng bảng size");
            case "UC062": return wrapMap(prodSvc.recommendSize(numInt(req, "categoryId", 1), str(req, "formFit", "REGULAR_FIT"), numDouble(req, "heightCm", 172), numDouble(req, "weightKg", 66)), uc, "Kết quả tư vấn size");
            case "UC063": return wrapAffected(uc, prodSvc.addSizeRule(numInt(req, "categoryId", 1), str(req, "formFit", "SLIM_FIT"), numDouble(req, "hMin", 170), numDouble(req, "hMax", 176), numDouble(req, "wMin", 62), numDouble(req, "wMax", 68), numInt(req, "sizeId", 1)), "Đã thêm quy tắc tư vấn size", "Không thêm được quy tắc");

            case "UC064": return prodSvc.listProducts(kw, 0, 30);
            case "UC065": return prodSvc.searchProducts(kw);
            case "UC066": {
                int cid = numInt(req, "categoryId", 0);
                return prodSvc.filterProducts(cid > 0 ? cid : null, null, numDouble(req, "minPrice", 0), numDouble(req, "maxPrice", 99999999), kw);
            }
            case "UC067": return wrapMap(prodDao.uc067_getProductDetail(numInt(req, "productId", 1)), uc, "Chi tiết sản phẩm");
            case "UC068": {
                String code = str(req, "productCode", "SP" + (System.currentTimeMillis() % 10000));
                return wrapAffected(uc, prodSvc.createProduct(code, str(req, "productName", "Áo Sơ Mi DUNA Mới"), code.toLowerCase(), numInt(req, "categoryId", 1), null, 1, numDouble(req, "basePrice", 420000), numDouble(req, "costPrice", 210000), str(req, "materialInfo", "Cotton"), str(req, "formFit", "REGULAR_FIT"), "", "Giặt máy nhẹ"), "Đã tạo sản phẩm mới", "Mã sản phẩm đã tồn tại");
            }
            case "UC069": return wrapAffected(uc, prodSvc.updateProductInfo(numInt(req, "productId", 1), str(req, "materialInfo", "100% Cotton Oxford"), str(req, "formFit", "REGULAR_FIT"), str(req, "careInstructions", "Giặt nhẹ 30 độ C")), "Đã cập nhật chất liệu & hướng dẫn bảo quản", "Không tìm thấy sản phẩm");
            case "UC070": return wrapAffected(uc, prodSvc.updatePrice(numInt(req, "productId", 1), numDouble(req, "newPrice", 390000), uid, str(req, "reason", "Cập nhật giá niêm yết")), "Đã cập nhật giá bán & lưu lịch sử", "Không tìm thấy sản phẩm");
            case "UC071": return wrapAffected(uc, prodSvc.toggleActiveAndFeature(numInt(req, "productId", 1), bool(req, "featured", true)), "Đã cập nhật trạng thái nổi bật", "Không tìm thấy sản phẩm");
            case "UC072": return wrapAffected(uc, prodSvc.softDeleteProduct(numInt(req, "productId", 9999)), "Đã ngừng kinh doanh sản phẩm", "Không tìm thấy sản phẩm");
            case "UC073": return wrapAffected(uc, prodSvc.deleteProductIfNoVariants(numInt(req, "productId", 9999)), "Đã xóa sản phẩm chưa có biến thể", "Sản phẩm đã có biến thể hoặc không tồn tại");
            case "UC074": return prodSvc.getTopSelling(numInt(req, "topN", 10));
            case "UC075": return wrapAffected(uc, prodDao.uc075_incrementViewCount(numInt(req, "productId", 1)), "Đã tăng lượt xem sản phẩm", "Không tìm thấy sản phẩm");
            case "UC076": return prodSvc.listVariants(numInt(req, "productId", 1), kw);
            case "UC077": return wrapAffected(uc, prodSvc.addUniqueVariant(numInt(req, "productId", 1), numInt(req, "colorId", 1), numInt(req, "sizeId", 1), "", "", numDouble(req, "price", 390000)), "Đã thêm biến thể duy nhất", "Biến thể màu-size đã tồn tại");
            case "UC078": return wrapScalar(uc, prodSvc.generateSkuAndBackfill(numInt(req, "productId", 1), numInt(req, "colorId", 1), numInt(req, "sizeId", 1)), "Đã chuẩn hóa SKU & Barcode");
            case "UC079": return wrapAffected(uc, prodSvc.updateVariantPrice(numInt(req, "variantId", 1), numDouble(req, "price", 390000)), "Đã cập nhật giá riêng cho biến thể", "Không tìm thấy biến thể");
            case "UC080": return wrapAffected(uc, prodSvc.deleteVariant(numInt(req, "variantId", 9999)), "Đã xóa biến thể chưa phát sinh đơn", "Biến thể đang có đơn/tồn kho hoặc không tồn tại");
            case "UC081": return prodSvc.listMedia(numInt(req, "productId", 1));
            case "UC082": return wrapAffected(uc, prodSvc.addMedia(numInt(req, "productId", 1), null, str(req, "mediaUrl", "img/duna-logo.png"), false, numInt(req, "displayOrder", 2)), "Đã thêm media cho sản phẩm", "Không thêm được media");
            case "UC083": return wrapAffected(uc, prodSvc.setPrimaryMedia(numInt(req, "productId", 1), numInt(req, "mediaId", 1)), "Đã đặt ảnh đại diện chính", "Không tìm thấy ảnh");
            case "UC084": return prodSvc.listTags(kw);
            case "UC085": return wrapAffected(uc, prodSvc.assignTag(numInt(req, "productId", 1), numInt(req, "tagId", 1)), "Đã gắn thẻ cho sản phẩm", "Thẻ đã được gắn trước đó");
            case "UC086": return wrapAffected(uc, prodSvc.removeTag(numInt(req, "productId", 1), numInt(req, "tagId", 999)), "Đã gỡ thẻ khỏi sản phẩm", "Không có liên kết thẻ để gỡ");
            default: return wrapScalar(uc, "N/A", "Chưa hỗ trợ");
        }
    }
}
