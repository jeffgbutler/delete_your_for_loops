package com.github.jeffgbutler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Step2 is remove for loops, but only go to forEach.  Changes:
 * 
 * 1. First getStatements method uses an IntStream and forEach.  This shows map and filter.
 * 2. Second getStatements method uses an array stream and forEach.  This shows filter.
 * 
 * The real challenge is the second getStatements method.  It is still too complex, and creates
 * too many ArrayLists.  Step 3 is to use streams everywhere.
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep2 implements Generator {

    @Override
    public List<String> generate() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/Users.xlsx");
             Workbook workbook = new XSSFWorkbook(is)) {
            return getStatements(workbook.getSheetAt(0));
        }
    }

    private List<String> getStatements(Sheet sheet) {
        List<String> lines = new ArrayList<>();

        IntStream.rangeClosed(0, sheet.getLastRowNum())
        .mapToObj(sheet::getRow)
        .filter(Objects::nonNull)
        .forEach(row -> lines.addAll(getStatements(row)));
        
        return lines;
    }

    private List<String> getStatements(Row row) {
        String userId = getUserId(row);
        if (userId == null) {
            return Collections.emptyList();
        }
        
        return getStatements(row, userId);
    }
    
    private List<String> getStatements(Row row, String userId) {
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
        if (cell != null && hasAuthority(cell.getStringCellValue())) {
            return true;
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
