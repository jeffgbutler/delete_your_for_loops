package com.github.jeffgbutler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Step4 introduces Optional.  Changes:
 * 
 * 1. getUserId returns an Optional
 * 2. getStatements uses the Optional.map function
 * 
 * Still has some if statements - and there is a repeated check for null.  More Optionals to come.
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep4 implements Generator {

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
        return getUserId(row)
                .map(userId -> getStatements(row, userId))
                .orElse(Stream.empty());
    }
    
    private Stream<String> getStatements(Row row, String userId) {
        return Arrays.stream(AppInfo.values())
                .filter(ai -> hasAuthority(row, ai))
                .map(ai -> ai.getInsertStatement(userId));
    }

    private Optional<String> getUserId(Row row) {
        Cell cell = row.getCell(0);
        if (cell != null) {
            String value = cell.getStringCellValue();
            if (isValidUserId(value)) {
                return Optional.of(value);
            }
        }
        
        return Optional.empty();
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
