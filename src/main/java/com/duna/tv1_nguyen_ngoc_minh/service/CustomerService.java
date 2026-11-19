package com.duna.tv1_nguyen_ngoc_minh.service;

import com.duna.common.util.RequestUtil;
import com.duna.tv1_nguyen_ngoc_minh.dao.CustomerDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M02 - Khach hang, Hang thanh vien & So do co the (UC028 - UC043).
 * Tac gia: Nguyen Ngoc Minh - TH08331
 */
public class CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public List<Map<String, Object>> listCustomers(String keyword, int offset, int limit) {
        return customerDAO.uc028_listCustomersPaged(keyword, offset, limit);
    }

    public List<Map<String, Object>> searchCustomers(String keyword) {
        return customerDAO.uc029_searchCustomers(keyword);
    }

    public Map<String, Object> getCustomerDetail(int customerId) {
        return customerDAO.uc030_getCustomerDetail(customerId);
    }

    public int addWalkInCustomer(String fullName, String phone, String gender) {
        return addCustomerFull(fullName, phone, "", gender, "", "");
    }

    public int addCustomerFull(String fullName, String phone, String email, String gender, String birthDate, String address) {
        String norm = RequestUtil.normalizePhone(phone);
        if (!RequestUtil.isValidVnPhone(norm)) {
            return -2;
        }
        String code = "KH" + String.format("%06d", Math.abs((norm + System.currentTimeMillis()).hashCode() % 900000) + 100000);
        return customerDAO.addClassicCustomerFull(code, fullName, norm, email, gender, birthDate, address);
    }

    public Map<String, Object> findByExactPhone(String phone) {
        return customerDAO.findByExactPhone(phone);
    }

    public int updateCustomer(int customerId, String phone, String email, String birthDate) {
        String norm = RequestUtil.normalizePhone(phone);
        if (!RequestUtil.isValidVnPhone(norm)) {
            return -2;
        }
        return customerDAO.uc032_updateCustomer(customerId, norm, email, birthDate);
    }

    public int setCustomerActive(int customerId, boolean active) {
        return customerDAO.uc033_toggleCustomerActive(customerId, active);
    }

    public int deleteCustomer(int customerId) {
        return customerDAO.uc034_deleteCustomerIfNoOrders(customerId);
    }

    public List<Map<String, Object>> listTiers(String keyword) {
        return customerDAO.uc035_listMembershipTiers(keyword);
    }

    public int addTier(String name, double minSpend, double discountPct, double multiplier, String benefits) {
        return customerDAO.uc036_addMembershipTier(name, minSpend, discountPct, multiplier, benefits);
    }

    public int updateTier(int tierId, double minSpend, double discountPct) {
        return customerDAO.uc037_updateMembershipTier(tierId, minSpend, discountPct);
    }

    public int autoUpgradeTiers() {
        return customerDAO.uc038_autoUpgradeAllCustomerTiers();
    }

    public List<Map<String, Object>> listGroups(String keyword) {
        return customerDAO.uc039_listCustomerGroups(keyword);
    }

    public int addGroup(String name, String desc) {
        return customerDAO.uc040_addCustomerGroup(name, desc);
    }

    public int assignToGroup(int customerId, int groupId) {
        return customerDAO.uc041_assignCustomerToGroup(customerId, groupId);
    }

    public int saveMeasurement(int customerId, double h, double w, double chest, double waist, double hip, double shoulder) {
        return customerDAO.uc042_saveMeasurement(customerId, h, w, chest, waist, hip, shoulder);
    }

    public Map<String, Object> getLatestMeasurement(int customerId) {
        return customerDAO.uc043_getLatestMeasurement(customerId);
    }
}
