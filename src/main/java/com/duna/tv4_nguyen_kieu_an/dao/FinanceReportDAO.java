package com.duna.tv4_nguyen_kieu_an.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M16 - Chi phi van hanh, Tong hop doanh thu & Bao cao tai chinh (UC279 - UC296).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class FinanceReportDAO extends BaseDAO {

    // UC279: Xem danh sach chi phi van hanh theo khoang ngay
    public List<Map<String, Object>> uc279_listExpenses(String fromDate, String toDate, String categoryKeyword) {
        String sql = "SELECT e.expense_id, c.category_name, s.store_name, e.amount, e.expense_date, e.description "
                + "FROM expenses e JOIN expense_categories c ON e.expense_category_id = c.expense_category_id "
                + "LEFT JOIN stores s ON e.store_id = s.store_id "
                + "WHERE e.expense_date BETWEEN ? AND ? AND c.category_name LIKE ? ORDER BY e.expense_date DESC";
        return queryRows(sql, fromDate, toDate, likePattern(categoryKeyword));
    }

    // UC280: Them moi loai chi phi
    public int uc280_addExpenseCategory(String categoryName) {
        return executeUpdate("INSERT INTO expense_categories (category_name) VALUES (?)", categoryName);
    }

    // UC281: Ghi nhan khoan chi phi phat sinh
    public int uc281_addExpense(int categoryId, Integer storeId, double amount, String expenseDate, String desc, int createdBy) {
        return executeUpdate("INSERT INTO expenses (expense_category_id, store_id, amount, expense_date, description, created_by) "
                + "VALUES (?, ?, ?, ?, ?, ?)", categoryId, storeId, amount, expenseDate, desc, createdBy);
    }

    // UC282: Cap nhat so tien va noi dung khoan chi phi
    public int uc282_updateExpense(int expenseId, double amount, String desc) {
        return executeUpdate("UPDATE expenses SET amount = ?, description = ? WHERE expense_id = ?", amount, desc, expenseId);
    }

    // UC283: Xoa khoan chi phi nhap sai
    public int uc283_deleteExpense(int expenseId) {
        return executeUpdate("DELETE FROM expenses WHERE expense_id = ?", expenseId);
    }

    // UC284: Tu dong tong hop doanh thu ngay hom truoc vao bang bao cao
    public int uc284_summarizeYesterdayRevenue() {
        String sql = "INSERT INTO daily_sales_summaries (summary_date, store_id, channel, total_orders, total_revenue, total_discount) "
                + "SELECT CAST(o.ordered_at AS DATE), o.store_id, o.channel, COUNT(*), SUM(o.total_amount), SUM(o.discount_amount) "
                + "FROM orders o WHERE o.status IN ('DELIVERED', 'COMPLETED') "
                + "AND CAST(o.ordered_at AS DATE) = CAST(DATEADD(DAY, -1, GETDATE()) AS DATE) "
                + "GROUP BY CAST(o.ordered_at AS DATE), o.store_id, o.channel";
        return executeUpdate(sql);
    }

    // UC285: Xem bang tong hop doanh thu theo ngay va kenh ban
    public List<Map<String, Object>> uc285_listDailySalesSummaries(String fromDate, String toDate) {
        String sql = "SELECT summary_date, channel, total_orders, total_revenue, total_cost, total_discount, "
                + "(total_revenue - total_cost) AS gross_profit "
                + "FROM daily_sales_summaries WHERE summary_date BETWEEN ? AND ? ORDER BY summary_date DESC";
        return queryRows(sql, fromDate, toDate);
    }

    // UC286: Bao cao doanh thu theo thang trong nam
    public List<Map<String, Object>> uc286_monthlyRevenue(int year) {
        String sql = "SELECT MONTH(ordered_at) AS month_no, COUNT(*) AS total_orders, ISNULL(SUM(total_amount), 0) AS revenue "
                + "FROM orders WHERE status IN ('DELIVERED', 'COMPLETED') AND YEAR(ordered_at) = ? "
                + "GROUP BY MONTH(ordered_at) ORDER BY month_no";
        return queryRows(sql, year);
    }

    // UC287: Bao cao doanh thu theo danh muc san pham
    public List<Map<String, Object>> uc287_revenueByCategory() {
        String sql = "SELECT c.category_name, ISNULL(SUM(oi.quantity), 0) AS items_sold, ISNULL(SUM(oi.line_total), 0) AS revenue "
                + "FROM order_items oi JOIN orders o ON oi.order_id = o.order_id "
                + "JOIN product_variants pv ON oi.variant_id = pv.variant_id "
                + "JOIN products p ON pv.product_id = p.product_id "
                + "JOIN categories c ON p.category_id = c.category_id "
                + "WHERE o.status IN ('DELIVERED', 'COMPLETED') GROUP BY c.category_name ORDER BY revenue DESC";
        return queryRows(sql);
    }

    // UC288: Bao cao doanh thu theo kenh ban va theo cua hang
    public List<Map<String, Object>> uc288_revenueByChannelAndStore() {
        String sql = "SELECT o.channel, ISNULL(s.store_name, N'Online Web') AS store_name, COUNT(*) AS total_orders, "
                + "ISNULL(SUM(o.total_amount), 0) AS revenue "
                + "FROM orders o LEFT JOIN stores s ON o.store_id = s.store_id "
                + "WHERE o.status IN ('DELIVERED', 'COMPLETED') GROUP BY o.channel, s.store_name ORDER BY revenue DESC";
        return queryRows(sql);
    }

    // UC289: Top 10 san pham ban chay theo doanh thu trong ky
    public List<Map<String, Object>> uc289_topProductsByRevenue(String fromDate, String toDate) {
        String sql = "SELECT TOP 10 p.product_code, p.product_name, SUM(oi.quantity) AS qty_sold, SUM(oi.line_total) AS revenue "
                + "FROM order_items oi JOIN orders o ON oi.order_id = o.order_id "
                + "JOIN product_variants pv ON oi.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id "
                + "WHERE o.status IN ('DELIVERED', 'COMPLETED') AND o.ordered_at BETWEEN ? AND ? "
                + "GROUP BY p.product_code, p.product_name ORDER BY revenue DESC";
        return queryRows(sql, fromDate, toDate);
    }

    // UC290: Bao cao loi nhuan gop theo san pham
    public List<Map<String, Object>> uc290_grossProfitByProduct() {
        String sql = "SELECT p.product_code, p.product_name, ISNULL(SUM(oi.line_total), 0) AS revenue, "
                + "ISNULL(SUM(oi.quantity * p.cost_price), 0) AS cogs, "
                + "ISNULL(SUM(oi.line_total) - SUM(oi.quantity * p.cost_price), 0) AS gross_profit "
                + "FROM order_items oi JOIN orders o ON oi.order_id = o.order_id "
                + "JOIN product_variants pv ON oi.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id "
                + "WHERE o.status IN ('DELIVERED', 'COMPLETED') GROUP BY p.product_code, p.product_name ORDER BY gross_profit DESC";
        return queryRows(sql);
    }

    // UC291: Bao cao gia tri ton kho theo tung kho hang
    public List<Map<String, Object>> uc291_inventoryValuationByWarehouse() {
        String sql = "SELECT w.warehouse_name, ISNULL(SUM(inv.stock_quantity), 0) AS total_qty, "
                + "ISNULL(SUM(inv.stock_quantity * p.cost_price), 0) AS inventory_value "
                + "FROM inventories inv JOIN warehouses w ON inv.warehouse_id = w.warehouse_id "
                + "JOIN product_variants pv ON inv.variant_id = pv.variant_id JOIN products p ON pv.product_id = p.product_id "
                + "GROUP BY w.warehouse_name";
        return queryRows(sql);
    }

    // UC292: Danh sach top khach hang VIP chi tieu cao nhat
    public List<Map<String, Object>> uc292_topVipCustomers() {
        String sql = "SELECT TOP 10 c.customer_code, c.full_name, c.phone, t.tier_name, c.total_orders, c.total_spent "
                + "FROM customers c LEFT JOIN membership_tiers t ON c.tier_id = t.tier_id "
                + "WHERE c.is_active = 1 ORDER BY c.total_spent DESC";
        return queryRows(sql);
    }

    // UC293: Bao cao ty le huy don va doi tra theo thang
    public List<Map<String, Object>> uc293_cancellationAndReturnRate(int year) {
        String sql = "SELECT MONTH(o.ordered_at) AS month_no, COUNT(*) AS total_orders, "
                + "SUM(CASE WHEN o.status = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelled_orders, "
                + "(SELECT COUNT(*) FROM return_requests r WHERE MONTH(r.created_at) = MONTH(o.ordered_at) AND YEAR(r.created_at) = ?) AS return_requests "
                + "FROM orders o WHERE YEAR(o.ordered_at) = ? GROUP BY MONTH(o.ordered_at) ORDER BY month_no";
        return queryRows(sql, year, year);
    }

    // UC294: Thong ke khach hang moi theo thang
    public List<Map<String, Object>> uc294_newCustomersByMonth() {
        String sql = "SELECT YEAR(created_at) AS year_no, MONTH(created_at) AS month_no, COUNT(*) AS new_customers "
                + "FROM customers GROUP BY YEAR(created_at), MONTH(created_at) ORDER BY year_no DESC, month_no DESC";
        return queryRows(sql);
    }

    // UC295: Bao cao loi nhuan rong thang
    public Map<String, Object> uc295_monthlyNetProfit(String fromDate, String toDate) {
        String sql = "SELECT "
                + "(SELECT ISNULL(SUM(total_revenue), 0) FROM daily_sales_summaries WHERE summary_date BETWEEN ? AND ?) AS revenue, "
                + "(SELECT ISNULL(SUM(total_cost), 0) FROM daily_sales_summaries WHERE summary_date BETWEEN ? AND ?) AS cogs, "
                + "(SELECT ISNULL(SUM(amount), 0) FROM expenses WHERE expense_date BETWEEN ? AND ?) AS operating_expenses";
        return queryFirstRow(sql, fromDate, toDate, fromDate, toDate, fromDate, toDate);
    }

    // UC296: Bang dieu khien tong quan: don hom nay, doanh thu, ton thap, ho tro va doi tra
    public Map<String, Object> uc296_getExecutiveDashboard() {
        String sql = "SELECT "
                + "(SELECT COUNT(*) FROM orders WHERE CAST(ordered_at AS DATE) = CAST(GETDATE() AS DATE)) AS orders_today, "
                + "(SELECT ISNULL(SUM(total_amount), 0) FROM orders WHERE CAST(ordered_at AS DATE) = CAST(GETDATE() AS DATE) AND status <> 'CANCELLED') AS revenue_today, "
                + "(SELECT COUNT(*) FROM inventories WHERE stock_quantity <= low_stock_threshold) AS low_stock_items, "
                + "(SELECT COUNT(*) FROM support_tickets WHERE status IN ('OPEN', 'IN_PROGRESS')) AS open_tickets, "
                + "(SELECT COUNT(*) FROM return_requests WHERE status = 'PENDING') AS pending_returns";
        return queryFirstRow(sql);
    }
}
