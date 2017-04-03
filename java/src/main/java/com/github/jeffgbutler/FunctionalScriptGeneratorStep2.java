package com.github.jeffgbutler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Step2 is to use map/filter/reduce, but keep the lists.  This introduces streams and collectors.
 * 
 * Changes are only in the generate and getStatementsFromRow functions
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep2 implements Generator {

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
        List<String> lines = new ArrayList<>();
        
        Utils.stream(sheet)
        .filter(row -> hasValidUserId(row))
        .map(row -> getStatementsFromRow(row))
        .forEach(rowLines -> lines.addAll(rowLines));
        
        return lines;
    }

    private List<String> getStatementsFromRow(Row row) {
        String userId = row.getCell(0).getStringCellValue();
        return Arrays.stream(columnToApplicationMappings)
                .filter(mapping -> hasAuthority(row, mapping[0]))
                .map(mapping -> getInsertStatement(userId, mapping[1]))
                .collect(Collectors.toList());
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
