package mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Interface chuẩn cho việc ánh xạ dữ liệu (Data Mapping) giữa:
 * 1. JDBC ResultSet (từ CSDL) -> Thực thể Entity
 * 2. Thực thể Entity -> Map<String, Object> (để truyền dữ liệu tương thích JSP/JSON)
 *
 * @param <T> Kiểu của Thực thể Entity
 */
public interface RowMapper<T> {
    /**
     * Ánh xạ từ dòng ResultSet sang Thực thể Entity
     */
    T mapRow(ResultSet rs) throws SQLException;

    /**
     * Chuyển đổi Thực thể Entity thành Map<String, Object>
     */
    Map<String, Object> toMap(T entity);
}

