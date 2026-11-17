package com.duna.tv4_nguyen_kieu_an.service;

import com.duna.tv4_nguyen_kieu_an.dao.SystemSettingDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M17 - Cau hinh he thong & Cong viec chay ngam (UC297 - UC300).
 * Tac gia: Nguyen Kieu An - TH08490
 */
public class SystemConfigService {
    private final SystemSettingDAO systemDAO = new SystemSettingDAO();

    public List<Map<String, Object>> listSettings(String kw) {
        return systemDAO.uc297_listSettings(kw);
    }

    public int updateSetting(String key, String value) {
        return systemDAO.uc298_updateSetting(key, value);
    }

    public List<Map<String, Object>> listScheduledJobs(String kw) {
        return systemDAO.uc299_listScheduledJobs(kw);
    }

    public int updateScheduledJob(String jobName, boolean enabled, String cron) {
        return systemDAO.uc300_updateScheduledJob(jobName, enabled, cron);
    }
}
