package com.duna.common.util;

import java.util.UUID;

/**
 * Tien ich chuan hoa ten tep tin khi tai anh san pham / avatar / danh gia.
 */
public final class FileUploadUtil {

    private FileUploadUtil() {
    }

    public static String safeUploadPath(String folder, String originalName) {
        String cleanName = (originalName == null || originalName.trim().isEmpty())
                ? "image.jpg"
                : originalName.trim().replaceAll("[^A-Za-z0-9._-]", "_");
        return "/uploads/" + folder + "/" + UUID.randomUUID().toString().substring(0, 8) + "_" + cleanName;
    }
}
