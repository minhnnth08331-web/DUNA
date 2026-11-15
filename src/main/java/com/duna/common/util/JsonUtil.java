package com.duna.common.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * JsonUtil - Tiện ích chuyển đổi đối tượng sang chuỗi JSON thuần Java 8
 * Không phụ thuộc thư viện ngoài (loại bỏ hoàn toàn lỗi module-info Java 9 trên Tomcat 7)
 */
public class JsonUtil {

    public static String toJson(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String) {
            return "\"" + ((String) obj).replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\"";
        }
        if (obj instanceof Number || obj instanceof Boolean) {
            return String.valueOf(obj);
        }
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) sb.append(",");
                sb.append("\"").append(entry.getKey()).append("\":").append(toJson(entry.getValue()));
                first = false;
            }
            sb.append("}");
            return sb.toString();
        }
        if (obj instanceof Collection) {
            Collection<?> col = (Collection<?>) obj;
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object item : col) {
                if (!first) sb.append(",");
                sb.append(toJson(item));
                first = false;
            }
            sb.append("]");
            return sb.toString();
        }
        // Object with getters (Java Bean)
        try {
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Method m : obj.getClass().getMethods()) {
                String name = m.getName();
                if ((name.startsWith("get") || name.startsWith("is")) && !name.equals("getClass") && m.getParameterTypes().length == 0) {
                    int prefixLen = name.startsWith("get") ? 3 : 2;
                    if (name.length() > prefixLen) {
                        String prop = Character.toLowerCase(name.charAt(prefixLen)) + name.substring(prefixLen + 1);
                        Object val = m.invoke(obj);
                        if (!first) sb.append(",");
                        sb.append("\"").append(prop).append("\":").append(toJson(val));
                        first = false;
                    }
                }
            }
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            return "\"" + obj.toString() + "\"";
        }
    }
}
