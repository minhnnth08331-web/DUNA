package com.duna.tv4_nguyen_kieu_an.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M17 - Cau hinh he thong & Cong viec chay ngam (UC297 - UC300).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class SystemSettingDAO extends BaseDAO {

    // UC297: Xem danh sach cau hinh he thong
    public List<Map<String, Object>> uc297_listSettings(String keyKeyword) {
        String sql = "SELECT setting_id, setting_key, setting_value, description, updated_at "
                + "FROM system_settings WHERE setting_key LIKE ? ORDER BY setting_key";
        return queryRows(sql, likePattern(keyKeyword));
    }

    // UC298: Cap nhat gia tri cau hinh
    public int uc298_updateSetting(String settingKey, String settingValue) {
        return executeUpdate("UPDATE system_settings SET setting_value = ?, updated_at = GETDATE() WHERE setting_key = ?",
                settingValue, settingKey);
    }

    // UC299: Xem danh sach cong viec chay ngam va ket qua lan chay gan nhat
    public List<Map<String, Object>> uc299_listScheduledJobs(String jobKeyword) {
        String sql = "SELECT job_id, job_name, cron_expression, is_enabled, last_run_at, next_run_at, last_status "
                + "FROM scheduled_jobs WHERE job_name LIKE ? ORDER BY job_name";
        return queryRows(sql, likePattern(jobKeyword));
    }

    // UC300: Bat tat va doi lich chay cua cong viec chay ngam
    public int uc300_updateScheduledJob(String jobName, boolean enabled, String cronExpression) {
        return executeUpdate("UPDATE scheduled_jobs SET is_enabled = ?, cron_expression = ? WHERE job_name = ?",
                enabled ? 1 : 0, cronExpression, jobName);
    }
}
