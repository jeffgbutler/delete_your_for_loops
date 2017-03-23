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

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Step5 is Optional everywhere.  Changes:
 * 
 * 1. New getCell function that returns Optional
 * 2. New hasAuthority function that just checks the string value 
 * 3. hasAuthority uses the new getCell and hasAuthority methods and map/orElse 
 * 4. getUserId uses the new getCell method and then map/filter/map/orElse and method references
 * 
 * Streams everywhere, no for loops, no if statements
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep5 implements Generator {

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
        return getUserId(row)
                .map(userId -> getStatements(row, userId))
                .orElse(Stream.empty());
    }
    
    private Stream<String> getStatements(XSSFRow row, String userId) {
        return Arrays.stream(AppInfo.values())
                .filter(ai -> hasAuthority(row, ai))
                .map(ai -> ai.getInsertStatement(userId));
    }

    private Optional<String> getUserId(XSSFRow row) {
        return getCell(row, 0)
                .map(XSSFCell::getStringCellValue)
                .filter(this::isValidUserId)
                .map(Optional::of)
                .orElse(Optional.empty());
    }
    
    private boolean hasAuthority(XSSFRow row, AppInfo appInfo) {
        return getCell(row, appInfo.columnNumber())
                .map(XSSFCell::getStringCellValue)
                .map(this::hasAuthority)
                .orElse(false);
    }
    
    private boolean hasAuthority(String value) {
        return "X".equals(value);
    }

    private boolean isValidUserId(String value) {
        return ".".equals(value.substring(1, 2));
    }
    
    private Optional<XSSFCell> getCell(XSSFRow row, int columnNumber) {
        return Optional.ofNullable(row.getCell(columnNumber));
    }
}
