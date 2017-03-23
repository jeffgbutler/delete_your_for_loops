package com.github.jeffgbutler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Step1 is pure functions.  Changes:
 * 
 * 1. Decomposed the deeply nested structure into pure functions
 * 
 * All methods could be static at this point, and all arguments are read only.  No mutable state.
 * 
 * The generate function is not pure because it depends on an external file.  All other functions are pure.
 * 
 * First tradeoff is that this method creates a new ArrayList for each row in the table, then that List
 * is merged into the main list.  Does this sound like reduction?
 * 
 * This is now 100% thread safe at the expense of a bit more memory used.
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep1 implements Generator {

    @Override
    public List<String> generate() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/Users.xlsx");
             XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            return getStatements(workbook.getSheetAt(0));
        }
    }

    private List<String> getStatements(XSSFSheet sheet) {
        List<String> lines = new ArrayList<>();
        
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            
            if (row != null) {
                lines.addAll(getStatements(row));
            }
        }
        
        return lines;
    }

    private List<String> getStatements(XSSFRow row) {
        String userId = getUserId(row);
        if (userId == null) {
            return Collections.emptyList();
        }
        
        return getStatements(row, userId);
    }
    
    private List<String> getStatements(XSSFRow row, String userId) {
        List<String> lines = new ArrayList<>();
        for(AppInfo appInfo : AppInfo.values()) {
            if (hasAuthority(row, appInfo)) {
                lines.add(appInfo.getInsertStatement(userId));
            }
        }
        return lines;
    }
    
    private String getUserId(XSSFRow row) {
        XSSFCell cell = row.getCell(0);
        if (cell != null) {
            String value = cell.getStringCellValue();
            if (isValidUserId(value)) {
                return value;
            }
        }
        
        return null;
    }
    
    private boolean hasAuthority(XSSFRow row, AppInfo appInfo) {
        XSSFCell cell = row.getCell(appInfo.columnNumber());
        if (cell != null && "X".equals(cell.getStringCellValue())) {
            return true;
        }
        return false;
    }
    
    private boolean isValidUserId(String value) {
        return ".".equals(value.substring(1, 2));
    }
}
