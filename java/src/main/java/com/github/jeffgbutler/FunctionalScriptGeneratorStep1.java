package com.github.jeffgbutler;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Step1 is to refactor into smaller functions.
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep1 implements Generator {

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
        for (Row row : sheet) {
            if (hasValidUserId(row)) {
                getStatementsFromRow(row, lines);
            }
        }

        return lines;
    }

    private List<String> getStatementsFromRow(Row row, List<String> lines) {
        String userId = row.getCell(0).getStringCellValue();
        for (int[] columnToApplicationMapping : columnToApplicationMappings) {
            if (hasAuthority(row, columnToApplicationMapping[0])) {
                lines.add(getInsertStatement(userId, columnToApplicationMapping[1]));
            }
        }
        return lines;
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
