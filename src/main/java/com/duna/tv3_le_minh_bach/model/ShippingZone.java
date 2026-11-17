package com.duna.tv3_le_minh_bach.model;

/**
 * Entity anh xa bang shipping_zones trong CSDL DUNA_MensWear.
 */
public class ShippingZone {
    private int zoneId;
    private String zoneName;
    private String description;

    public ShippingZone() {
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
