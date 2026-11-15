package com.duna.common.util;

/**
 * SizeUtil - Tiện ích tư vấn chọn kích cỡ (Size) trang phục nam DUNA
 * Dựa trên Chiều cao (cm) và Cân nặng (kg) chuẩn phom dáng nam giới Việt Nam
 */
public class SizeUtil {

    /**
     * Tư vấn Size trang phục dựa trên Chiều cao và Cân nặng
     * @param heightCm Chiều cao (cm)
     * @param weightKg Cân nặng (kg)
     * @return Kích cỡ phù hợp: S, M, L, XL, XXL
     */
    public static String recommendSize(int heightCm, int weightKg) {
        return SizeGuideUtil.recommendSize(heightCm, weightKg);
    }
}
