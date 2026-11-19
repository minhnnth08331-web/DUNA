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
        ensureStaffUsersSyncedToEmployees();
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

    // ==================== CLASSIC UI ADAPTERS (employee-management.jsp / employee-detail.jsp) ====================

    public int updatePasswordForEmployee(int empOrUserId, String passwordHash) {
        Integer targetUserId = queryOne("SELECT user_id FROM employees WHERE employee_id = ?", rs -> rs.getInt(1), empOrUserId);
        if (targetUserId == null) {
            targetUserId = queryOne("SELECT user_id FROM users WHERE user_id = ?", rs -> rs.getInt(1), empOrUserId);
        }
        if (targetUserId == null || targetUserId <= 0) {
            return 0;
        }
        return executeUpdate("UPDATE users SET password_hash = ?, updated_at = GETDATE() WHERE user_id = ?",
                passwordHash, targetUserId);
    }

    public boolean verifyUserPassword(int userId, String passwordHash) {
        return queryInt("SELECT COUNT(*) FROM users WHERE user_id = ? AND password_hash = ? AND status = 'ACTIVE'",
                userId, passwordHash) > 0;
    }

    public List<Map<String, Object>> listClassicEmployees(String searchQuery, String statusFilter, String roleScope) {
        ensureStaffUsersSyncedToEmployees();
        String sq = searchQuery == null ? "" : searchQuery.trim();
        String sf = statusFilter == null ? "" : statusFilter.trim();
        String pat = likePattern(sq);

        StringBuilder sql = new StringBuilder(
                "SELECT e.employee_id AS id, e.employee_code AS maNV, e.full_name AS ten, "
                + "CONVERT(VARCHAR(10), e.birth_date, 23) AS ngaySinh, "
                + "CASE WHEN e.status = 'WORKING' AND (u.status IS NULL OR u.status = 'ACTIVE') THEN N'Hoạt động' ELSE N'Đã khóa' END AS trangThai, "
                + "ISNULL((SELECT TOP 1 al.new_value FROM audit_logs al WHERE al.entity_name = 'employees' "
                + " AND al.entity_id = CAST(e.employee_id AS VARCHAR(40)) AND al.action = 'HR_LOCK' ORDER BY al.created_at DESC), '') AS lyDo, "
                + "ISNULL(r.role_code, 'STAFF') AS roleCode "
                + "FROM employees e "
                + "LEFT JOIN users u ON e.user_id = u.user_id "
                + "LEFT JOIN roles r ON u.role_id = r.role_id "
                + "WHERE (e.full_name LIKE ? OR e.employee_code LIKE ?) "
        );
        if ("MANAGER".equalsIgnoreCase(roleScope)) {
            sql.append("AND r.role_code IN ('ADMIN', 'MANAGER') ");
        } else {
            sql.append("AND (r.role_code IS NULL OR r.role_code NOT IN ('ADMIN', 'MANAGER')) ");
        }
        if ("1".equals(sf)) {
            sql.append("AND e.status = 'WORKING' AND (u.status IS NULL OR u.status = 'ACTIVE') ");
        } else if ("2".equals(sf)) {
            sql.append("AND (e.status <> 'WORKING' OR u.status = 'LOCKED') ");
        }
        sql.append("ORDER BY e.employee_id ASC");
        return queryRows(sql.toString(), pat, pat);
    }

    public Map<String, Object> getClassicEmployeeDetail(int idOrUserId) {
        ensureStaffUsersSyncedToEmployees();
        String baseSelect = "SELECT TOP 1 e.employee_id AS id, e.employee_code AS maNV, e.full_name AS hoVaTen, "
                + "ISNULL(u.email, '') AS email, ISNULL(u.phone, e.phone) AS soDienThoai, "
                + "ISNULL(u.email, e.employee_code) AS username, "
                + "CONVERT(VARCHAR(10), e.birth_date, 23) AS ngaySinh, "
                + "CONVERT(VARCHAR(10), e.created_at, 23) AS ngayTao, "
                + "CASE WHEN e.status = 'WORKING' AND (u.status IS NULL OR u.status = 'ACTIVE') THEN N'Hoạt động' ELSE N'Đã khóa' END AS trangThai, "
                + "CASE WHEN r.role_code IN ('ADMIN', 'MANAGER') THEN 'true' ELSE 'false' END AS isAdmin, "
                + "ISNULL(s.address, N'Hà Nội') AS diaChiStore, "
                + "e.user_id "
                + "FROM employees e "
                + "LEFT JOIN users u ON e.user_id = u.user_id "
                + "LEFT JOIN roles r ON u.role_id = r.role_id "
                + "LEFT JOIN stores s ON e.store_id = s.store_id ";
        Map<String, Object> row = queryFirstRow(baseSelect + "WHERE e.employee_id = ?", idOrUserId);
        if (row == null) {
            row = queryFirstRow(baseSelect + "WHERE e.user_id = ?", idOrUserId);
        }
        if (row == null) {
            return null;
        }
        row.put("password", "********");
        row.put("cccd", "");
        row.put("ngayCapCccd", "");
        row.put("ngayHetHanCccd", "");
        row.put("noiCapCccd", "");
        row.put("queQuan", "Hà Nội");
        row.put("noiThuongTru", "Hà Nội");
        row.put("diaChiHienTai", row.get("diaChiStore"));

        Map<String, Object> metaRow = queryFirstRow(
                "SELECT TOP 1 new_value FROM audit_logs WHERE entity_name = 'employees' AND entity_id = ? AND action = 'HR_PROFILE' ORDER BY audit_id DESC",
                String.valueOf(row.get("id")));
        if (metaRow != null && metaRow.get("new_value") != null) {
            parseHrProfileMeta(String.valueOf(metaRow.get("new_value")), row);
        }
        return row;
    }

    public int addClassicEmployee(String username, String passwordHash, String fullName, String phone, String email,
                                  String birthDate, String cccd, String ngayCapCccd, String ngayHetHanCccd,
                                  String noiCapCccd, String queQuan, String noiThuongTru, String diaChiHienTai,
                                  int actorUserId) {
        if (queryInt("SELECT COUNT(*) FROM users WHERE email = ? OR (phone IS NOT NULL AND phone = ?)", email, phone) > 0) {
            return -1;
        }
        String insertUser = "INSERT INTO users (email, phone, password_hash, full_name, role_id, status, email_verified) "
                + "SELECT ?, ?, ?, ?, role_id, 'ACTIVE', 1 FROM roles WHERE role_code = 'STAFF'";
        long newUserId = executeInsertGetId(insertUser, email, phone, passwordHash, fullName);
        if (newUserId <= 0) {
            return 0;
        }
        String code = (username != null && !username.trim().isEmpty())
                ? username.trim().toUpperCase().replaceAll("[^A-Z0-9_]", "")
                : String.format("NV%04d", newUserId);
        if (code.length() > 18) code = code.substring(0, 18);
        if (queryInt("SELECT COUNT(*) FROM employees WHERE employee_code = ?", code) > 0) {
            code = String.format("NV%04d", newUserId);
        }
        long empId = executeInsertGetId(
                "INSERT INTO employees (employee_code, user_id, full_name, phone, email, birth_date, hire_date, department_id, position_id, store_id, status) "
                + "VALUES (?, ?, ?, ?, ?, NULLIF(?, ''), GETDATE(), "
                + "(SELECT TOP 1 department_id FROM departments ORDER BY department_id), "
                + "(SELECT TOP 1 position_id FROM positions ORDER BY base_salary ASC), 1, 'WORKING')",
                code, newUserId, fullName, phone, email, birthDate == null ? "" : birthDate);
        if (empId > 0) {
            saveHrProfileMeta((int) empId, actorUserId, username, cccd, ngayCapCccd, ngayHetHanCccd,
                    noiCapCccd, queQuan, noiThuongTru, diaChiHienTai);
        }
        return (int) empId;
    }

    public int updateClassicEmployee(int empId, String fullName, String phone, String email, String birthDate,
                                     String cccd, String ngayCapCccd, String ngayHetHanCccd, String noiCapCccd,
                                     String queQuan, String noiThuongTru, String diaChiHienTai,
                                     String reason, int actorUserId) {
        String cleanEmail = email == null ? "" : email.trim();
        String cleanPhone = phone == null ? "" : phone.trim();
        Integer linkedUserId = queryOne("SELECT user_id FROM employees WHERE employee_id = ?", rs -> rs.getInt(1), empId);
        if (linkedUserId != null && linkedUserId > 0) {
            if (!cleanEmail.isEmpty() && queryInt("SELECT COUNT(*) FROM users WHERE email = ? AND user_id <> ?", cleanEmail, linkedUserId) > 0) {
                return -1;
            }
            if (!cleanPhone.isEmpty() && queryInt("SELECT COUNT(*) FROM users WHERE phone = ? AND user_id <> ?", cleanPhone, linkedUserId) > 0) {
                return -1;
            }
        }
        int res = executeUpdate(
                "UPDATE employees SET full_name = ?, phone = ?, email = ?, birth_date = NULLIF(?, ''), updated_at = GETDATE() WHERE employee_id = ?",
                fullName, cleanPhone, cleanEmail, birthDate == null ? "" : birthDate, empId);
        if (linkedUserId != null && linkedUserId > 0) {
            executeUpdate(
                    "UPDATE users SET full_name = ?, phone = NULLIF(?, ''), email = CASE WHEN ? <> '' THEN ? ELSE email END, updated_at = GETDATE() WHERE user_id = ?",
                    fullName, cleanPhone, cleanEmail, cleanEmail, linkedUserId);
        }
        saveHrProfileMeta(empId, actorUserId, "", cccd, ngayCapCccd, ngayHetHanCccd,
                noiCapCccd, queQuan, noiThuongTru, diaChiHienTai);
        if (reason != null && !reason.trim().isEmpty()) {
            executeUpdate("INSERT INTO audit_logs (user_id, action, entity_name, entity_id, new_value) VALUES (?, 'HR_UPDATE', 'employees', ?, ?)",
                    actorUserId > 0 ? actorUserId : null, String.valueOf(empId), reason.trim());
        }
        return res;
    }

    public int toggleClassicEmployeeStatus(int empId, boolean currentlyActive, String reason, int actorUserId) {
        String newEmpStatus = currentlyActive ? "RESIGNED" : "WORKING";
        String newUserStatus = currentlyActive ? "LOCKED" : "ACTIVE";
        int res = executeUpdate("UPDATE employees SET status = ?, updated_at = GETDATE() WHERE employee_id = ?",
                newEmpStatus, empId);
        executeUpdate("UPDATE users SET status = ?, updated_at = GETDATE() WHERE user_id = (SELECT user_id FROM employees WHERE employee_id = ?)",
                newUserStatus, empId);
        String lockNote = currentlyActive ? (reason == null ? "" : reason.trim()) : "";
        executeUpdate("INSERT INTO audit_logs (user_id, action, entity_name, entity_id, new_value) VALUES (?, 'HR_LOCK', 'employees', ?, ?)",
                actorUserId > 0 ? actorUserId : null, String.valueOf(empId), lockNote);
        return res;
    }

    private void saveHrProfileMeta(int empId, int actorUserId, String username, String cccd, String ngayCap,
                                   String ngayHetHan, String noiCap, String queQuan, String noiThuongTru, String diaChiHienTai) {
        String payload = "username=" + safeMeta(username)
                + "\ncccd=" + safeMeta(cccd)
                + "\nngayCapCccd=" + safeMeta(ngayCap)
                + "\nngayHetHanCccd=" + safeMeta(ngayHetHan)
                + "\nnoiCapCccd=" + safeMeta(noiCap)
                + "\nqueQuan=" + safeMeta(queQuan)
                + "\nnoiThuongTru=" + safeMeta(noiThuongTru)
                + "\ndiaChiHienTai=" + safeMeta(diaChiHienTai);
        executeUpdate("INSERT INTO audit_logs (user_id, action, entity_name, entity_id, new_value) VALUES (?, 'HR_PROFILE', 'employees', ?, ?)",
                actorUserId > 0 ? actorUserId : null, String.valueOf(empId), payload);
    }

    private String safeMeta(String s) {
        return s == null ? "" : s.replace("\r", " ").replace("\n", " ").trim();
    }

    private void parseHrProfileMeta(String payload, Map<String, Object> row) {
        if (payload == null) return;
        for (String line : payload.split("\n")) {
            int idx = line.indexOf('=');
            if (idx > 0) {
                String k = line.substring(0, idx).trim();
                String v = line.substring(idx + 1).trim();
                if (!v.isEmpty()) {
                    row.put(k, v);
                }
            }
        }
    }

    public void ensureStaffUsersSyncedToEmployees() {
        String sql = "INSERT INTO employees (employee_code, user_id, full_name, phone, email, birth_date, hire_date, department_id, position_id, store_id, status) "
                + "SELECT CASE WHEN r.role_code IN ('ADMIN', 'MANAGER') THEN 'QL' ELSE 'NV' END "
                + "+ RIGHT('000' + CAST(u.user_id AS VARCHAR(10)), 3), "
                + "u.user_id, u.full_name, u.phone, u.email, DATEADD(YEAR, -24 - (u.user_id % 7), '2026-01-01'), CAST(u.created_at AS DATE), "
                + "(SELECT TOP 1 department_id FROM departments ORDER BY department_id), "
                + "(SELECT TOP 1 position_id FROM positions ORDER BY base_salary ASC), 1, "
                + "CASE WHEN u.status = 'ACTIVE' THEN 'WORKING' ELSE 'RESIGNED' END "
                + "FROM users u JOIN roles r ON u.role_id = r.role_id "
                + "WHERE r.role_code <> 'CUSTOMER' "
                + "AND NOT EXISTS (SELECT 1 FROM employees e WHERE e.user_id = u.user_id) "
                + "AND NOT EXISTS (SELECT 1 FROM employees e2 WHERE e2.employee_code = "
                + "CASE WHEN r.role_code IN ('ADMIN', 'MANAGER') THEN 'QL' ELSE 'NV' END + RIGHT('000' + CAST(u.user_id AS VARCHAR(10)), 3))";
        executeUpdate(sql);
    }
}
