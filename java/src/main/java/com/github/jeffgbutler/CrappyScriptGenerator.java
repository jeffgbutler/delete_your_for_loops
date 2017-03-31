package com.github.jeffgbutler;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * This is the worst way to address the problem.
 * 
 * @author Jeff Butler
 *
 */
public class CrappyScriptGenerator implements Generator {

    @Override
    public List<String> generate(Sheet sheet) {
        List<String> lines = new ArrayList<>();
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

        return lines;
    }
}
