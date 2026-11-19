package com.duna;

import com.duna.common.uc.UcRegistry;
import com.duna.common.util.DBConnection;
import com.duna.common.util.PasswordUtil;
import com.duna.tv1_nguyen_ngoc_minh.dao.CustomerDAO;
import com.duna.tv1_nguyen_ngoc_minh.dao.ProductDAO;
import com.duna.tv1_nguyen_ngoc_minh.dao.RolePermissionDAO;
import com.duna.tv1_nguyen_ngoc_minh.dao.UserDAO;
import com.duna.tv1_nguyen_ngoc_minh.service.AuthAccountService;
import com.duna.tv2_nguyen_duc_anh.dao.InventoryDAO;
import com.duna.tv2_nguyen_duc_anh.dao.OrderDAO;
import com.duna.tv3_le_minh_bach.dao.PromotionVoucherDAO;
import com.duna.tv4_nguyen_kieu_an.dao.FinanceReportDAO;
import com.duna.tv4_nguyen_kieu_an.dao.HumanResourceDAO;
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
                     "SELECT (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_NAME <> 'sysdiagrams'), "
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

    public void testClassicUiAdaptersAgainstDunaMensWear() {
        ProductDAO pdao = new ProductDAO();
        assertFalse(pdao.listClassicProducts("", "", "", 1, 10).isEmpty());

        CustomerDAO cdao = new CustomerDAO();
        assertFalse(cdao.listClassicCustomers("", "", "", 0, 10).isEmpty());

        OrderDAO odao = new OrderDAO();
        assertNotNull(odao.buildClassicReportData(2026, 11, 2026, 11, 2026));

        PromotionVoucherDAO vdao = new PromotionVoucherDAO();
        assertNotNull(vdao.listClassicVouchers("", "", ""));
        assertNotNull(vdao.getClassicVoucherStats());

        HumanResourceDAO hrDao = new HumanResourceDAO();
        assertNotNull(hrDao.listClassicEmployees("", "", "MANAGER"));
        assertNotNull(hrDao.getClassicEmployeeDetail(1));

        // Verify variant barcode & order print chiTiet keys + 1:1 customer-user linkage
        List<Map<String, Object>> prods = pdao.listClassicProducts("SP099", "", "", 0, 5);
        assertFalse(prods.isEmpty());
        int sp099Id = ((Number) prods.get(0).get("id")).intValue();
        List<Map<String, Object>> vars = pdao.listClassicVariants(sp099Id);
        assertTrue(vars.size() >= 2);
        assertNotNull(vars.get(0).get("barcode"));

        List<Map<String, Object>> orders = odao.listClassicOrders("DN20261106", "", "", "", "", 0, 5);
        assertFalse(orders.isEmpty());
        long oid = ((Number) orders.get(0).get("id")).longValue();
        Map<String, Object> printDetail = odao.getClassicOrderDetailJson(oid);
        assertNotNull(printDetail.get("chiTiet"));
        assertFalse(((List<?>) printDetail.get("chiTiet")).isEmpty());
    }

    public void testDatabaseIntegrityCustomerUserLinkedOneToOne() throws Exception {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT (SELECT COUNT(*) FROM customers WHERE user_id IS NULL), "
                             + "(SELECT COUNT(*) FROM customers c WHERE NOT EXISTS (SELECT 1 FROM loyalty_accounts la WHERE la.customer_id = c.customer_id)), "
                             + "(SELECT COUNT(*) FROM membership_tiers), "
                             + "(SELECT COUNT(*) FROM customers c WHERE c.total_spent <> ISNULL((SELECT SUM(o.total_amount) FROM orders o WHERE o.customer_id = c.customer_id AND o.status IN ('COMPLETED','DELIVERED')),0)), "
                             + "(SELECT COUNT(*) FROM customers c WHERE c.tier_id <> (SELECT TOP 1 t.tier_id FROM membership_tiers t WHERE t.min_spend <= c.total_spent ORDER BY t.min_spend DESC)), "
                             + "(SELECT COUNT(*) FROM employees WHERE user_id IS NULL OR department_id IS NULL OR position_id IS NULL)");
             ResultSet rs = ps.executeQuery()) {
            assertTrue(rs.next());
            assertEquals("Every customer must link to a user account", 0, rs.getInt(1));
            assertEquals("Every customer must have a loyalty account", 0, rs.getInt(2));
            assertEquals("membership_tiers must have exactly 4 clean tiers", 4, rs.getInt(3));
            assertEquals("Every customer total_spent must match real completed orders", 0, rs.getInt(4));
            assertEquals("Every customer tier_id must match membership_tiers threshold", 0, rs.getInt(5));
            assertEquals("Every employee must link to user, department, and position", 0, rs.getInt(6));
        }
        CustomerDAO cdao = new CustomerDAO();
        List<Map<String, Object>> kh600 = cdao.listClassicCustomers("KH000600", "", "", 0, 5);
        assertFalse(kh600.isEmpty());
        assertTrue(((Number) kh600.get(0).get("userId")).intValue() > 0);
        assertTrue(((Number) kh600.get(0).get("orderCount")).intValue() > 0);
        assertTrue(((Number) kh600.get(0).get("tongSP")).intValue() > 0);
        assertTrue(((Number) kh600.get(0).get("discountPct")).intValue() > 0);
    }

    public void testUcRegistry300CoverageAndUserM01Dao() {
        assertEquals("UcRegistry must cover all 300 Use Cases", 300, UcRegistry.findAll().size());
        for (int i = 1; i <= 300; i++) {
            String code = String.format("UC%03d", i);
            UcRegistry.UcMeta meta = UcRegistry.findByCode(code);
            assertNotNull("Missing UC metadata: " + code, meta);
            assertFalse("Empty title for " + code, meta.getTitle().isEmpty());
            assertFalse("Empty route for " + code, meta.getRoute().isEmpty());
            assertFalse("Empty jspView for " + code, meta.getJspView().isEmpty());
        }
        UserDAO udao = new UserDAO();
        Map<String, Object> stats = udao.getUserStats();
        assertTrue(((Number) stats.get("totalUsers")).intValue() >= 603);
        assertTrue(udao.countUsersFiltered("", "", "") >= 603);
        assertFalse(udao.listAllProvinces().isEmpty());
    }
}
