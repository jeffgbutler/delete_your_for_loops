package com.github.jeffgbutler;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Step4a introduces method references in Java.
 * 
 * Changes:
 * 
 * 1. generate function changed to use method references
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep4a implements Generator {

    private static Pair[] columnToApplicationMappings = {
            Pair.of(1, getInsertBuilderForApplication(2237)),
            Pair.of(2, getInsertBuilderForApplication(4352)),
            Pair.of(3, getInsertBuilderForApplication(3657)),
            Pair.of(4, getInsertBuilderForApplication(5565))
    };

    private static Function<String, String> getInsertBuilderForApplication(int appId) {
        return userId -> getInsertStatement(userId, appId);
    }
    
    private static String getInsertStatement(String userId, int appId) {
        return "insert into ApplicationPermission(user_id, application_id) values('"
                + userId
                + "', "
                + appId
                + ");";
    }

    @Override
    public List<String> generate(Sheet sheet) {
        return Utils.stream(sheet)
                .filter(this::hasValidUserId)
                .flatMap(this::getStatementsFromRow)
                .collect(Collectors.toList());
    }

    private Stream<String> getStatementsFromRow(Row row) {
        String userId = row.getCell(0).getStringCellValue();
        return Arrays.stream(columnToApplicationMappings)
                .filter(mapping -> hasAuthority(row, mapping.columnNumber()))
                .map(mapping -> mapping.insertBuilder().apply(userId));
    }
    
    private boolean hasValidUserId(Row row) {
        Cell cell = row.getCell(0);
        if (cell != null) {
            String userId = cell.getStringCellValue();
            return isValidUserId(userId);
        }
        return false;
    }
    
    private boolean hasAuthority(Row row, int columnNumber) {
        Cell cell = row.getCell(columnNumber);
        if (cell != null) {
            String cellValue = cell.getStringCellValue();
            return hasAuthority(cellValue);
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
