package com.github.jeffgbutler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Step4 is higher order functions.
 * 
 * Changes:
 * 
 * 1. Added the getInsertBuilderForApplication partial function
 * 2. Changed columnToApplicationMappings to a map of column numbers to functions
 * 3. Changed getStatementsFromRow to stream the map
 * 
 * Java requires calling the "apply" method in the partial function.  No compiler syntax sugar there.
 * This step illustrates higher order functions in Java, but there are better ways to do this.  An enum
 * with the embedded function would probably be better.
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep4 implements Generator {

    private static Map<Integer, Function<String, String>> columnToApplicationMappings = new HashMap<>();
    
    static {
        columnToApplicationMappings.put(1, getInsertBuilderForApplication(2237));
        columnToApplicationMappings.put(2, getInsertBuilderForApplication(4352));
        columnToApplicationMappings.put(3, getInsertBuilderForApplication(3657));
        columnToApplicationMappings.put(4, getInsertBuilderForApplication(5565));
    }

    // this is a partial...almost like currying
    private static Function<String, String> getInsertBuilderForApplication(int appId) {
        return (userId) -> getInsertStatement(userId, appId);
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
                .filter(row -> hasValidUserId(row))
                .flatMap(row -> getStatementsFromRow(row))
                .collect(Collectors.toList());
    }

    private Stream<String> getStatementsFromRow(Row row) {
        String userId = row.getCell(0).getStringCellValue();
        return columnToApplicationMappings.entrySet().stream()
                .filter(mapping -> hasAuthority(row, mapping.getKey()))
                .map(mapping -> mapping.getValue().apply(userId));
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
