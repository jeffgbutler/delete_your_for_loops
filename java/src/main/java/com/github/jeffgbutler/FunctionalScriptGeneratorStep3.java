package com.github.jeffgbutler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Step3 is Streams all the way, and flatMap.
 * 
 * Changes are only in the generate and getStatementsFromRow functions
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep3 implements Generator {

    private static int[][] columnToApplicationMappings = {
            {1, 2237},
            {2, 4352},
            {3, 3657},
            {4, 5565}
    };

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
        return Arrays.stream(columnToApplicationMappings)
                .filter(mapping -> hasAuthority(row, mapping[0]))
                .map(mapping -> getInsertStatement(userId, mapping[1]));
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
