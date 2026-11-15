package com.duna.common.util;

/**
 * Lớp tiện ích tư vấn Size thông minh cho thời trang nam DUNA (UC 16)
 * Dựa trên ma trận Chiều cao (cm) và Cân nặng (kg) chuẩn người Việt Nam
 */
public class SizeGuideUtil {

    /**
     * Gợi ý Size phù hợp (S, M, L, XL, XXL)
     */
    public static String recommendSize(int heightCm, int weightKg) {
        if (heightCm < 140 || weightKg < 35) {
            return "S";
        }
        if (weightKg >= 85 || heightCm >= 185) {
            return "XXL";
        }
        if (weightKg >= 75 || heightCm >= 180) {
            return "XL";
        }
        if (weightKg >= 65 || heightCm >= 172) {
            return "L";
        }
        if (weightKg >= 55 || heightCm >= 165) {
            return "M";
        }
        return "S";
    }
}
