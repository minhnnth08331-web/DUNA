package com.duna.tv4_nguyen_kieu_an.service;

import com.duna.common.util.RequestUtil;
import com.duna.tv4_nguyen_kieu_an.dao.HumanResourceDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M15 - Nhan su, Phong ban, Phan ca & Cham cong (UC267 - UC278).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class HumanResourceService {
    private final HumanResourceDAO hrDAO = new HumanResourceDAO();

    public List<Map<String, Object>> listEmployees(String kw) {
        return hrDAO.uc267_listEmployees(kw);
    }

    public int addEmployee(String code, Integer userId, String name, String phone, String email,
                           String birth, String hire, Integer deptId, Integer posId, Integer storeId) {
        return hrDAO.uc268_addEmployee(code.toUpperCase(), userId, name, RequestUtil.normalizePhone(phone),
                email, birth, hire, deptId, posId, storeId);
    }

    public int updatePositionAndStore(int empId, int posId, int storeId) {
        return hrDAO.uc269_updateEmployeePositionAndStore(empId, posId, storeId);
    }

    public int resignEmployee(int empId) {
        return hrDAO.uc270_resignEmployeeAndLockUser(empId);
    }

    public List<Map<String, Object>> listDepartmentsAndPositions(String kw) {
        return hrDAO.uc271_listDepartmentsAndPositions(kw);
    }

    public int addPosition(String name, double salary) {
        return hrDAO.uc272_addPosition(name, salary);
    }

    public int addWorkShift(String name, String start, String end) {
        return hrDAO.uc273_addWorkShift(name, start, end);
    }

    public int assignShift(int empId, int shiftId, int storeId, String date) {
        return hrDAO.uc274_assignEmployeeShift(empId, shiftId, storeId, date);
    }

    public List<Map<String, Object>> getWeeklySchedule(int storeId, String from, String to) {
        return hrDAO.uc275_getWeeklyShiftSchedule(storeId, from, to);
    }

    public int checkIn(int empId, String date) {
        return hrDAO.uc276_checkIn(empId, date);
    }

    public int checkOut(int empId, String date) {
        return hrDAO.uc277_checkOut(empId, date);
    }

    public List<Map<String, Object>> monthlyAttendance(String from, String to) {
        return hrDAO.uc278_monthlyAttendanceReport(from, to);
    }
}
