package com.github.jeffgbutler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This is the worst way to address the problem.
 * 
 * @author Jeff Butler
 *
 */
public class CrappyScriptGenerator implements Generator {

    @Override
    public List<String> generate() throws IOException {
        List<String> lines = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream("/Users.xlsx");
                Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    String userId = cell.getStringCellValue();
                    if (".".equals(userId.substring(1, 2))) {
                        for (AppInfo appInfo : AppInfo.values()) {
                            cell = row.getCell(appInfo.columnNumber());
                            if (cell != null) {
                                String cellValue = cell.getStringCellValue();
                                if ("X".equals(cellValue)) {
                                    lines.add(appInfo.getInsertStatement(userId));
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return lines;
    }
}
