package com.duna.tv3_le_minh_bach.service;

import com.duna.tv3_le_minh_bach.dao.GeographyShippingDAO;

import java.util.List;
import java.util.Map;

/**
 * Service phan he M08 - Dia ly, Phi van chuyen & Theo doi van don (UC171 - UC190).
 * Tac gia: Le Minh Bach - TH08442
 */
public class ShippingGeoService {
    private final GeographyShippingDAO shippingDAO = new GeographyShippingDAO();

    public List<Map<String, Object>> listProvinces(String kw) {
        return shippingDAO.uc171_listProvinces(kw);
    }

    public int addProvince(String code, String name, Integer zoneId) {
        return shippingDAO.uc172_addProvince(code, name, zoneId);
    }

    public int assignZone(int provinceId, int zoneId) {
        return shippingDAO.uc173_assignProvinceZone(provinceId, zoneId);
    }

    public List<Map<String, Object>> listWards(int provinceId, String kw) {
        return shippingDAO.uc174_listWardsByProvince(provinceId, kw);
    }

    public List<Map<String, Object>> searchWards(String kw) {
        return shippingDAO.uc175_searchWards(kw);
    }

    public int addWard(int provinceId, String code, String name, String type) {
        return shippingDAO.uc176_addWard(provinceId, code, name, type);
    }

    public int deleteWard(int wardId) {
        return shippingDAO.uc177_deleteWardIfUnused(wardId);
    }

    public List<Map<String, Object>> listZones(String kw) {
        return shippingDAO.uc178_listZones(kw);
    }

    public int addZone(String name, String desc) {
        return shippingDAO.uc179_addZone(name, desc);
    }

    public List<Map<String, Object>> listCarriers(String kw) {
        return shippingDAO.uc180_listCarriers(kw);
    }

    public int addCarrier(String code, String name, String endpoint, String phone) {
        return shippingDAO.uc181_addCarrier(code.toUpperCase(), name, endpoint, phone);
    }

    public List<Map<String, Object>> listMethods(String kw) {
        return shippingDAO.uc182_listShippingMethods(kw);
    }

    public int addMethod(int carrierId, String name, int minD, int maxD) {
        return shippingDAO.uc183_addShippingMethod(carrierId, name, minD, maxD);
    }

    public List<Map<String, Object>> listFees() {
        return shippingDAO.uc184_listShippingFees();
    }

    public int updateFee(int feeId, double baseFee, double freeThreshold) {
        return shippingDAO.uc185_updateShippingFee(feeId, baseFee, freeThreshold);
    }

    public double calculateFee(int provinceId, int methodId, int weightGram, double orderAmount) {
        return shippingDAO.uc186_calculateShippingFee(provinceId, methodId, weightGram, orderAmount);
    }

    public int createShipment(long orderId, int carrierId, int methodId, String trackingCode, double fee, double cod, int weight) {
        return shippingDAO.uc187_createShipment(orderId, carrierId, methodId, trackingCode, fee, cod, weight);
    }

    public int updateTracking(String trackingCode, String status, String loc, String note) {
        return shippingDAO.uc188_updateShipmentTracking(trackingCode, status, loc, note);
    }

    public List<Map<String, Object>> trackShipment(String trackingCode) {
        return shippingDAO.uc189_trackShipment(trackingCode);
    }

    public List<Map<String, Object>> listDelayedOrFailed(String kw) {
        return shippingDAO.uc190_listDelayedOrFailedShipments(kw);
    }
}
