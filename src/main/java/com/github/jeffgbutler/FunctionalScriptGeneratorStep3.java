package com.github.jeffgbutler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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
             XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            return getStatements(workbook.getSheetAt(0));
        }
    }

    private List<String> getStatements(XSSFSheet sheet) {
        return IntStream.rangeClosed(0, sheet.getLastRowNum())
                .mapToObj(sheet::getRow)
                .filter(Objects::nonNull)
                .flatMap(this::getStatements)
                .collect(Collectors.toList());
    }

    private Stream<String> getStatements(XSSFRow row) {
        String userId = getUserId(row);
        if (userId == null) {
            return Stream.empty();
        }
        
        return getStatements(row, userId);
    }
    
    private Stream<String> getStatements(XSSFRow row, String userId) {
        return Arrays.stream(AppInfo.values())
                .filter(ai -> hasAuthority(row, ai))
                .map(ai -> ai.getInsertStatement(userId));
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
