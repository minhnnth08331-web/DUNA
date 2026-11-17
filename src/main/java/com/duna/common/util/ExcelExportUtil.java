package com.duna.common.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tien ich xuat file Excel (.xlsx) bang Apache POI 4.1.2 (UC158, Bao cao tai chinh).
 */
public final class ExcelExportUtil {

    private ExcelExportUtil() {
    }

    public static void writeExcel(HttpServletResponse resp, String fileName, String sheetName,
                                  List<Map<String, Object>> rows) throws IOException {
        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet(sheetName);
            if (rows != null && !rows.isEmpty()) {
                Set<String> headers = rows.get(0).keySet();
                Row headerRow = sheet.createRow(0);
                CellStyle headerStyle = wb.createCellStyle();
                Font font = wb.createFont();
                font.setBold(true);
                headerStyle.setFont(font);

                int colIdx = 0;
                for (String h : headers) {
                    Cell cell = headerRow.createCell(colIdx++);
                    cell.setCellValue(h);
                    cell.setCellStyle(headerStyle);
                }

                int rowIdx = 1;
                for (Map<String, Object> item : rows) {
                    Row r = sheet.createRow(rowIdx++);
                    int c = 0;
                    for (String h : headers) {
                        Object val = item.get(h);
                        r.createCell(c++).setCellValue(val == null ? "" : String.valueOf(val));
                    }
                }
                for (int i = 0; i < headers.size(); i++) {
                    sheet.autoSizeColumn(i);
                }
            }
            wb.write(resp.getOutputStream());
        }
    }
}
