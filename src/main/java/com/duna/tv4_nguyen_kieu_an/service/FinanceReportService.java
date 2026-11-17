package com.duna.tv4_nguyen_kieu_an.service;

import com.duna.tv4_nguyen_kieu_an.dao.FinanceReportDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M16 - Chi phi, Doanh thu, Loi nhuan & Dashboard dieu hanh (UC279 - UC296).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class FinanceReportService {
    private final FinanceReportDAO financeDAO = new FinanceReportDAO();

    public List<Map<String, Object>> listExpenses(String from, String to, String kw) {
        return financeDAO.uc279_listExpenses(from, to, kw);
    }

    public int addExpenseCategory(String name) {
        return financeDAO.uc280_addExpenseCategory(name);
    }

    public int addExpense(int catId, Integer storeId, double amount, String date, String desc, int userId) {
        return financeDAO.uc281_addExpense(catId, storeId, amount, date, desc, userId);
    }

    public int updateExpense(int id, double amount, String desc) {
        return financeDAO.uc282_updateExpense(id, amount, desc);
    }

    public int deleteExpense(int id) {
        return financeDAO.uc283_deleteExpense(id);
    }

    public int summarizeYesterday() {
        return financeDAO.uc284_summarizeYesterdayRevenue();
    }

    public List<Map<String, Object>> listDailySummaries(String from, String to) {
        return financeDAO.uc285_listDailySalesSummaries(from, to);
    }

    public List<Map<String, Object>> monthlyRevenue(int year) {
        return financeDAO.uc286_monthlyRevenue(year);
    }

    public List<Map<String, Object>> revenueByCategory() {
        return financeDAO.uc287_revenueByCategory();
    }

    public List<Map<String, Object>> revenueByChannelAndStore() {
        return financeDAO.uc288_revenueByChannelAndStore();
    }

    public List<Map<String, Object>> topProductsByRevenue(String from, String to) {
        return financeDAO.uc289_topProductsByRevenue(from, to);
    }

    public List<Map<String, Object>> grossProfitByProduct() {
        return financeDAO.uc290_grossProfitByProduct();
    }

    public List<Map<String, Object>> inventoryValuation() {
        return financeDAO.uc291_inventoryValuationByWarehouse();
    }

    public List<Map<String, Object>> topVipCustomers() {
        return financeDAO.uc292_topVipCustomers();
    }

    public List<Map<String, Object>> cancellationReturnRate(int year) {
        return financeDAO.uc293_cancellationAndReturnRate(year);
    }

    public List<Map<String, Object>> newCustomersByMonth() {
        return financeDAO.uc294_newCustomersByMonth();
    }

    public Map<String, Object> monthlyNetProfit(String from, String to) {
        return financeDAO.uc295_monthlyNetProfit(from, to);
    }

    public Map<String, Object> getDashboard() {
        return financeDAO.uc296_getExecutiveDashboard();
    }
}
