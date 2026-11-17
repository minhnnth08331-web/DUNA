package com.duna.tv3_le_minh_bach.dao;

import com.duna.common.util.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * DAO phan he M08 - Dia ly, Don vi van chuyen, Bang phi & Van don (UC171 - UC190).
 * Tac gia: Le Minh Bach - TH08442
 */
public class GeographyShippingDAO extends BaseDAO {

    // UC171: Xem danh sach tinh thanh kem vung giao hang
    public List<Map<String, Object>> uc171_listProvinces(String keyword) {
        String sql = "SELECT p.province_id, p.province_code, p.province_name, z.zone_name "
                + "FROM provinces p LEFT JOIN shipping_zones z ON p.zone_id = z.zone_id "
                + "WHERE p.province_name LIKE ? ORDER BY p.province_name";
        return queryRows(sql, likePattern(keyword));
    }

    // UC172: Them moi tinh thanh pho
    public int uc172_addProvince(String code, String name, Integer zoneId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM provinces WHERE province_code = ?) "
                + "INSERT INTO provinces (province_code, province_name, zone_id) VALUES (?, ?, ?)";
        return executeUpdate(sql, code, code, name, zoneId);
    }

    // UC173: Gan tinh thanh vao vung giao hang
    public int uc173_assignProvinceZone(int provinceId, int zoneId) {
        return executeUpdate("UPDATE provinces SET zone_id = ? WHERE province_id = ?", zoneId, provinceId);
    }

    // UC174: Xem danh sach xa, phuong, dac khu theo tinh thanh
    public List<Map<String, Object>> uc174_listWardsByProvince(int provinceId, String keyword) {
        String sql = "SELECT w.ward_id, w.ward_code, w.ward_name, w.ward_type, p.province_name "
                + "FROM wards w JOIN provinces p ON w.province_id = p.province_id "
                + "WHERE (? <= 0 OR w.province_id = ?) AND w.ward_name LIKE ? ORDER BY w.ward_name";
        return queryRows(sql, provinceId, provinceId, likePattern(keyword));
    }

    // UC175: Tim kiem xa phuong theo ten goi y
    public List<Map<String, Object>> uc175_searchWards(String keyword) {
        String sql = "SELECT TOP 20 w.ward_id, w.ward_code, w.ward_name, p.province_name "
                + "FROM wards w JOIN provinces p ON w.province_id = p.province_id "
                + "WHERE w.ward_name LIKE ? ORDER BY w.ward_name";
        return queryRows(sql, likePattern(keyword));
    }

    // UC176: Them moi xa phuong dac khu
    public int uc176_addWard(int provinceId, String wardCode, String wardName, String wardType) {
        return executeUpdate("INSERT INTO wards (province_id, ward_code, ward_name, ward_type) VALUES (?, ?, ?, ?)",
                provinceId, wardCode, wardName, wardType);
    }

    // UC177: Xoa xa phuong khi chua duoc su dung
    public int uc177_deleteWardIfUnused(int wardId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM user_addresses WHERE ward_id = ?) "
                + "AND NOT EXISTS (SELECT 1 FROM orders WHERE ward_id = ?) DELETE FROM wards WHERE ward_id = ?";
        return executeUpdate(sql, wardId, wardId, wardId);
    }

    // UC178: Xem danh sach vung giao hang kem so tinh thanh
    public List<Map<String, Object>> uc178_listZones(String keyword) {
        String sql = "SELECT z.zone_id, z.zone_name, z.description, COUNT(p.province_id) AS total_provinces "
                + "FROM shipping_zones z LEFT JOIN provinces p ON z.zone_id = p.zone_id "
                + "WHERE z.zone_name LIKE ? GROUP BY z.zone_id, z.zone_name, z.description";
        return queryRows(sql, likePattern(keyword));
    }

    // UC179: Them moi vung giao hang
    public int uc179_addZone(String zoneName, String description) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM shipping_zones WHERE zone_name = ?) "
                + "INSERT INTO shipping_zones (zone_name, description) VALUES (?, ?)";
        return executeUpdate(sql, zoneName, zoneName, description);
    }

    // UC180: Xem danh sach don vi van chuyen
    public List<Map<String, Object>> uc180_listCarriers(String keyword) {
        String sql = "SELECT carrier_id, carrier_code, carrier_name, phone, is_active "
                + "FROM shipping_carriers WHERE carrier_name LIKE ? ORDER BY carrier_name";
        return queryRows(sql, likePattern(keyword));
    }

    // UC181: Them moi don vi van chuyen kem API
    public int uc181_addCarrier(String code, String name, String apiEndpoint, String phone) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM shipping_carriers WHERE carrier_code = ?) "
                + "INSERT INTO shipping_carriers (carrier_code, carrier_name, api_endpoint, phone) VALUES (?, ?, ?, ?)";
        return executeUpdate(sql, code, code, name, apiEndpoint, phone);
    }

    // UC182: Xem danh sach dich vu van chuyen theo tung don vi
    public List<Map<String, Object>> uc182_listShippingMethods(String keyword) {
        String sql = "SELECT m.shipping_method_id, c.carrier_name, m.method_name, m.estimated_days_min, m.estimated_days_max, m.is_active "
                + "FROM shipping_methods m JOIN shipping_carriers c ON m.carrier_id = c.carrier_id "
                + "WHERE m.method_name LIKE ? ORDER BY c.carrier_name";
        return queryRows(sql, likePattern(keyword));
    }

    // UC183: Them moi dich vu van chuyen
    public int uc183_addShippingMethod(int carrierId, String methodName, int minDays, int maxDays) {
        return executeUpdate("INSERT INTO shipping_methods (carrier_id, method_name, estimated_days_min, estimated_days_max) VALUES (?, ?, ?, ?)",
                carrierId, methodName, minDays, maxDays);
    }

    // UC184: Xem bang phi van chuyen theo vung va khoi luong
    public List<Map<String, Object>> uc184_listShippingFees() {
        String sql = "SELECT f.fee_id, c.carrier_name, m.method_name, z.zone_name, "
                + "f.min_weight_gram, f.max_weight_gram, f.base_fee, f.free_ship_threshold "
                + "FROM shipping_fees f JOIN shipping_methods m ON f.shipping_method_id = m.shipping_method_id "
                + "JOIN shipping_carriers c ON m.carrier_id = c.carrier_id "
                + "JOIN shipping_zones z ON f.zone_id = z.zone_id ORDER BY c.carrier_name, z.zone_name";
        return queryRows(sql);
    }

    // UC185: Cap nhat phi van chuyen va nguong mien phi giao hang
    public int uc185_updateShippingFee(int feeId, double baseFee, double freeShipThreshold) {
        return executeUpdate("UPDATE shipping_fees SET base_fee = ?, free_ship_threshold = ? WHERE fee_id = ?",
                baseFee, freeShipThreshold, feeId);
    }

    // UC186: Tinh phi van chuyen cho don hang theo tinh nhan va khoi luong
    public double uc186_calculateShippingFee(int provinceId, int shippingMethodId, int weightGram, double orderAmount) {
        String sql = "SELECT TOP 1 CASE WHEN ? >= f.free_ship_threshold THEN 0 ELSE f.base_fee END AS shipping_fee "
                + "FROM shipping_fees f JOIN provinces p ON f.zone_id = p.zone_id "
                + "WHERE p.province_id = ? AND f.shipping_method_id = ? AND ? BETWEEN f.min_weight_gram AND ISNULL(f.max_weight_gram, 999999)";
        return queryDouble(sql, orderAmount, provinceId, shippingMethodId, weightGram);
    }

    // UC187: Tao van don gui don vi van chuyen cho don hang
    public int uc187_createShipment(long orderId, int carrierId, int shippingMethodId, String trackingCode,
                                    double shippingFee, double codAmount, int weightGram) {
        return executeUpdate("INSERT INTO shipments (order_id, carrier_id, shipping_method_id, tracking_code, shipping_fee, cod_amount, weight_gram) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                orderId, carrierId, shippingMethodId, trackingCode, shippingFee, codAmount, weightGram);
    }

    // UC188: Cap nhat trang thai van don theo webhook
    public int uc188_updateShipmentTracking(String trackingCode, String newStatus, String location, String note) {
        int updated = executeUpdate("UPDATE shipments SET status = ?, shipped_at = ISNULL(shipped_at, GETDATE()) WHERE tracking_code = ?",
                newStatus, trackingCode);
        if (updated > 0) {
            executeUpdate("INSERT INTO shipment_tracking_events (shipment_id, event_status, event_location, event_note) "
                    + "SELECT shipment_id, ?, ?, ? FROM shipments WHERE tracking_code = ?",
                    newStatus, location, note, trackingCode);
        }
        return updated;
    }

    // UC189: Tra cuu hanh trinh van don theo ma van don
    public List<Map<String, Object>> uc189_trackShipment(String trackingCode) {
        String sql = "SELECT s.tracking_code, s.status, e.event_status, e.event_location, e.event_note, e.event_time "
                + "FROM shipments s JOIN shipment_tracking_events e ON s.shipment_id = e.shipment_id "
                + "WHERE s.tracking_code LIKE ? ORDER BY e.event_time DESC";
        return queryRows(sql, likePattern(trackingCode));
    }

    // UC190: Xem danh sach van don giao tre hoac giao that bai
    public List<Map<String, Object>> uc190_listDelayedOrFailedShipments(String orderCodeKeyword) {
        String sql = "SELECT s.shipment_id, s.tracking_code, o.order_code, c.carrier_name, s.status, s.shipped_at "
                + "FROM shipments s JOIN orders o ON s.order_id = o.order_id "
                + "JOIN shipping_carriers c ON s.carrier_id = c.carrier_id "
                + "WHERE (s.status = 'FAILED' OR (s.status = 'IN_TRANSIT' AND s.shipped_at < DATEADD(DAY, -5, GETDATE()))) "
                + "AND o.order_code LIKE ?";
        return queryRows(sql, likePattern(orderCodeKeyword));
    }
}
