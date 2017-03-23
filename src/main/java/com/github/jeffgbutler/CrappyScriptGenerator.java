package com.github.jeffgbutler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This is the worst way to address the problem.  But it demonstrates the complexity of the task.
 * 
 * @author Jeff Butler
 *
 */
public class CrappyScriptGenerator implements Generator {

    @Override
    public List<String> generate() throws IOException {
        List<String> lines = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream("/Users.xlsx");
                XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null) {
                    XSSFCell cell = row.getCell(0);
                    if (cell != null) {
                        String userId = cell.getStringCellValue();
                        if (".".equals(userId.substring(1, 2))) {
                            for (AppInfo appInfo : AppInfo.values()) {
                                cell = row.getCell(appInfo.columnNumber());
                                if (cell != null && "X".equals(cell.getStringCellValue())) {
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
