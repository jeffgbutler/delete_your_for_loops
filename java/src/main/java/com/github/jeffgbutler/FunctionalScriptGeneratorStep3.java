package com.github.jeffgbutler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Step3 is use streams everywhere.  Changes:
 * 
 * 1. Second getStatements method now returns a stream
 * 2. First getStatements method uses a flatMap and Collector
 * 
 * Next step is to remove the if statement in the second getStatement method with an optional
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep3 implements Generator {

    @Override
    public List<String> generate() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/Users.xlsx");
             Workbook workbook = new XSSFWorkbook(is)) {
            return getStatements(workbook.getSheetAt(0));
        }
    }

    private List<String> getStatements(Sheet sheet) {
        return IntStream.rangeClosed(0, sheet.getLastRowNum())
                .mapToObj(sheet::getRow)
                .filter(Objects::nonNull)
                .flatMap(this::getStatements)
                .collect(Collectors.toList());
    }

    private Stream<String> getStatements(Row row) {
        String userId = getUserId(row);
        if (userId == null) {
            return Stream.empty();
        }
        
        return getStatements(row, userId);
    }
    
    private Stream<String> getStatements(Row row, String userId) {
        return Arrays.stream(AppInfo.values())
                .filter(ai -> hasAuthority(row, ai))
                .map(ai -> ai.getInsertStatement(userId));
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
