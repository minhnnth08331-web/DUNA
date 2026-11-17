package com.duna;

import com.duna.common.util.DBConnection;
import com.duna.common.util.PasswordUtil;
import com.duna.tv1_nguyen_ngoc_minh.dao.CustomerDAO;
import com.duna.tv1_nguyen_ngoc_minh.dao.ProductDAO;
import com.duna.tv1_nguyen_ngoc_minh.dao.RolePermissionDAO;
import com.duna.tv1_nguyen_ngoc_minh.service.AuthAccountService;
import com.duna.tv2_nguyen_duc_anh.dao.InventoryDAO;
import com.duna.tv2_nguyen_duc_anh.dao.OrderDAO;
import com.duna.tv3_le_minh_bach.dao.PromotionVoucherDAO;
import com.duna.tv4_nguyen_kieu_an.dao.FinanceReportDAO;
import com.duna.tv4_nguyen_kieu_an.dao.SystemSettingDAO;
import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Bo kiem thu tich hop kien truc 4 thanh vien, 100 bang & 300 UC (JUnit 3.8.1).
 */
public class DunaArchitectureAndUcTest extends TestCase {

    public void testDatabaseHas100TablesAndSeedData() throws Exception {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'), "
                             + "(SELECT COUNT(*) FROM customers), (SELECT COUNT(*) FROM products)");
             ResultSet rs = ps.executeQuery()) {
            assertTrue(rs.next());
            assertEquals(100, rs.getInt(1));
            assertTrue(rs.getInt(2) >= 1);
            assertTrue(rs.getInt(3) >= 2);
        }
    }

    public void testPasswordHashingAndAuthLogin() {
        assertEquals("3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2",
                PasswordUtil.hashPassword("Admin123"));
        AuthAccountService auth = new AuthAccountService();
        Map<String, Object> admin = auth.login("admin@duna.vn", "Admin123", "127.0.0.1", "JUnit");
        assertNotNull("Admin login must succeed", admin);
        assertEquals("ADMIN", String.valueOf(admin.get("role_code")));
    }

    public void testMember1DaosUc001ToUc086() {
        RolePermissionDAO rp = new RolePermissionDAO();
        assertTrue(rp.uc001_listRoles("").size() >= 4);

        CustomerDAO cdao = new CustomerDAO();
        assertFalse(cdao.uc028_listCustomersPaged("", 0, 10).isEmpty());

        ProductDAO pdao = new ProductDAO();
        List<Map<String, Object>> prods = pdao.uc065_searchProducts("SP001");
        assertFalse(prods.isEmpty());
    }

    public void testMember2To4DaosUc087ToUc300() {
        InventoryDAO idao = new InventoryDAO();
        assertFalse(idao.uc091_listVariantInventories(0).isEmpty());

        OrderDAO odao = new OrderDAO();
        String nextCode = odao.uc131_generateNextOrderCode();
        assertTrue(nextCode.startsWith("DN"));

        PromotionVoucherDAO vdao = new PromotionVoucherDAO();
        assertNotNull(vdao.uc191_listVouchers(""));

        FinanceReportDAO fdao = new FinanceReportDAO();
        assertNotNull(fdao.uc296_getExecutiveDashboard());

        SystemSettingDAO sdao = new SystemSettingDAO();
        assertFalse(sdao.uc297_listSettings("").isEmpty());
    }
}
