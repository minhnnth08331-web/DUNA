package com.duna.tv4_nguyen_kieu_an.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M15 - Nhan su, Phong ban, Chuc vu, Ca lam & Cham cong (UC267 - UC278).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class HumanResourceDAO extends BaseDAO {

    // UC267: Xem danh sach nhan vien theo cua hang va phong ban
    public List<Map<String, Object>> uc267_listEmployees(String keyword) {
        String sql = "SELECT e.employee_id, e.employee_code, e.full_name, e.phone, e.email, "
                + "d.department_name, po.position_name, s.store_name, e.status "
                + "FROM employees e LEFT JOIN departments d ON e.department_id = d.department_id "
                + "LEFT JOIN positions po ON e.position_id = po.position_id "
                + "LEFT JOIN stores s ON e.store_id = s.store_id "
                + "WHERE e.full_name LIKE ? OR e.employee_code LIKE ? ORDER BY e.employee_code";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat);
    }

    // UC268: Them moi nhan vien va lien ket tai khoan dang nhap
    public int uc268_addEmployee(String code, Integer userId, String fullName, String phone, String email,
                                 String birthDate, String hireDate, Integer deptId, Integer posId, Integer storeId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM employees WHERE employee_code = ?) "
                + "INSERT INTO employees (employee_code, user_id, full_name, phone, email, birth_date, hire_date, department_id, position_id, store_id) "
                + "VALUES (?, ?, ?, ?, ?, NULLIF(?, ''), ?, ?, ?, ?)";
        return executeUpdate(sql, code, code, userId, fullName, phone, email,
                birthDate == null ? "" : birthDate, hireDate, deptId, posId, storeId);
    }

    // UC269: Cap nhat chuc vu va chuyen cong tac nhan vien sang cua hang khac
    public int uc269_updateEmployeePositionAndStore(int employeeId, int positionId, int storeId) {
        return executeUpdate("UPDATE employees SET position_id = ?, store_id = ?, updated_at = GETDATE() WHERE employee_id = ?",
                positionId, storeId, employeeId);
    }

    // UC270: Cho nhan vien nghi viec va khoa tai khoan dang nhap
    public int uc270_resignEmployeeAndLockUser(int employeeId) {
        int updated = executeUpdate("UPDATE employees SET status = 'RESIGNED', updated_at = GETDATE() WHERE employee_id = ?", employeeId);
        if (updated > 0) {
            executeUpdate("UPDATE users SET status = 'LOCKED' WHERE user_id = (SELECT user_id FROM employees WHERE employee_id = ?)", employeeId);
        }
        return updated;
    }

    // UC271: Xem danh sach phong ban va chuc vu kem luong co ban
    public List<Map<String, Object>> uc271_listDepartmentsAndPositions(String keyword) {
        String sql = "SELECT d.department_id, d.department_name, po.position_id, po.position_name, po.base_salary "
                + "FROM departments d CROSS JOIN positions po "
                + "WHERE d.department_name LIKE ? OR po.position_name LIKE ? ORDER BY d.department_name, po.base_salary DESC";
        String pat = likePattern(keyword);
        return queryRows(sql, pat, pat);
    }

    // UC272: Them moi chuc vu kem muc luong co ban
    public int uc272_addPosition(String positionName, double baseSalary) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM positions WHERE position_name = ?) INSERT INTO positions (position_name, base_salary) VALUES (?, ?)";
        return executeUpdate(sql, positionName, positionName, baseSalary);
    }

    // UC273: Them moi ca lam viec
    public int uc273_addWorkShift(String shiftName, String startTime, String endTime) {
        return executeUpdate("INSERT INTO work_shifts (shift_name, start_time, end_time) VALUES (?, ?, ?)",
                shiftName, startTime, endTime);
    }

    // UC274: Phan ca lam viec cho nhan vien theo ngay
    public int uc274_assignEmployeeShift(int employeeId, int shiftId, int storeId, String workDate) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM employee_shifts WHERE employee_id = ? AND shift_id = ? AND work_date = ?) "
                + "INSERT INTO employee_shifts (employee_id, shift_id, store_id, work_date) VALUES (?, ?, ?, ?)";
        return executeUpdate(sql, employeeId, shiftId, workDate, employeeId, shiftId, storeId, workDate);
    }

    // UC275: Xem lich phan ca cua cua hang theo tuan
    public List<Map<String, Object>> uc275_getWeeklyShiftSchedule(int storeId, String fromDate, String toDate) {
        String sql = "SELECT es.work_date, e.full_name, ws.shift_name, ws.start_time, ws.end_time "
                + "FROM employee_shifts es JOIN employees e ON es.employee_id = e.employee_id "
                + "JOIN work_shifts ws ON es.shift_id = ws.shift_id "
                + "WHERE (? <= 0 OR es.store_id = ?) AND es.work_date BETWEEN ? AND ? ORDER BY es.work_date, ws.start_time";
        return queryRows(sql, storeId, storeId, fromDate, toDate);
    }

    // UC276: Cham cong vao ca va danh dau di tre
    public int uc276_checkIn(int employeeId, String workDate) {
        String sql = "INSERT INTO attendance_records (employee_id, work_date, check_in, status) "
                + "SELECT ?, ?, GETDATE(), CASE WHEN CAST(GETDATE() AS TIME) > '08:45' THEN 'LATE' ELSE 'PRESENT' END";
        return executeUpdate(sql, employeeId, workDate);
    }

    // UC277: Cham cong ra ca va ghi nhan gio ket thuc
    public int uc277_checkOut(int employeeId, String workDate) {
        return executeUpdate("UPDATE attendance_records SET check_out = GETDATE() WHERE employee_id = ? AND work_date = ? AND check_out IS NULL",
                employeeId, workDate);
    }

    // UC278: Bao cao cham cong thang
    public List<Map<String, Object>> uc278_monthlyAttendanceReport(String fromDate, String toDate) {
        String sql = "SELECT e.employee_code, e.full_name, "
                + "SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) AS present_days, "
                + "SUM(CASE WHEN a.status = 'LATE' THEN 1 ELSE 0 END) AS late_days, "
                + "SUM(CASE WHEN a.status = 'ABSENT' THEN 1 ELSE 0 END) AS absent_days "
                + "FROM employees e LEFT JOIN attendance_records a ON e.employee_id = a.employee_id AND a.work_date BETWEEN ? AND ? "
                + "GROUP BY e.employee_code, e.full_name";
        return queryRows(sql, fromDate, toDate);
    }
}
