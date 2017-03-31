package com.github.jeffgbutler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Step2 is remove for loops, but only go to forEach.  Changes:
 * 
 * 1. First getStatements method uses a utility function to make a stream from an iterable.  Then uses filter.
 * 2. Added the hasUserId function for the filter
 * 3. Second getStatements method uses an array stream and forEach.  This shows filter.
 * 
 * The real challenge is the second getStatements method.  It is still too complex, and creates
 * too many ArrayLists.  Step 3 is to use streams everywhere.
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep2 implements Generator {

    @Override
    public List<String> generate(Sheet sheet) {
        List<String> lines = new ArrayList<>();
        
        Utils.stream(sheet)
        .filter(this::hasUserId)
        .forEach(row -> lines.addAll(getStatements(row)));
        
        return lines;
    }
    
    private boolean hasUserId(Row row) {
        return getUserId(row) != null;
    }

    private List<String> getStatements(Row row) {
        String userId = getUserId(row);
        List<String> lines = new ArrayList<>();
        Arrays.stream(AppInfo.values())
        .filter(ai -> hasAuthority(row, ai))
        .forEach(ai -> lines.add(ai.getInsertStatement(userId)));
        
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
