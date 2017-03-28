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
             Workbook workbook = new XSSFWorkbook(is)) {
            return getStatements(workbook.getSheetAt(0));
        }
    }

    private List<String> getStatements(Sheet sheet) {
        List<String> lines = new ArrayList<>();
        
        for (Row row : sheet) {
            String userId = getUserId(row);
            
            if (userId != null) {
                lines.addAll(getStatements(row, userId));
            }
        }
        
        return lines;
    }

    private List<String> getStatements(Row row, String userId) {
        List<String> lines = new ArrayList<>();
        for(AppInfo appInfo : AppInfo.values()) {
            if (hasAuthority(row, appInfo)) {
                lines.add(appInfo.getInsertStatement(userId));
            }
        }
        return lines;
    }
    
    private String getUserId(Row row) {
        Cell cell = row.getCell(0);
        if (cell != null) {
            String value = cell.getStringCellValue();
            if (isValidUserId(value)) {
                return value;
            }
        }
        
        return null;
    }

    private boolean hasAuthority(Row row, AppInfo appInfo) {
        Cell cell = row.getCell(appInfo.columnNumber());
        if (cell != null) {
            String cellValue = cell.getStringCellValue();
            if (hasAuthority(cellValue)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasAuthority(String value) {
        return "X".equals(value);
    }

    private boolean isValidUserId(String value) {
        return ".".equals(value.substring(1, 2));
    }
}
