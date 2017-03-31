package com.github.jeffgbutler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Step3 is use streams everywhere.  If you think about it a but differently, you can see that
 * the second getStatements method is actually mapping the Stream of enums into a stream of statements.
 * 
 * Changes:
 * 
 * 1. Second getStatements method now returns a stream
 * 2. First getStatements method uses a flatMap and Collector
 * 
 * See how the getStatements methods are so much smaller, and we are not ever creating our
 * own ArrayList.
 * 
 * Next step is to remove the if statements with optionals
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep3 implements Generator {

    @Override
    public List<String> generate(Sheet sheet) {
        return Utils.stream(sheet)
                .filter(this::hasUserId)
                .flatMap(this::getStatements)
                .collect(Collectors.toList());
    }
    
    private boolean hasUserId(Row row) {
        return getUserId(row) != null;
    }

    private Stream<String> getStatements(Row row) {
        String userId = getUserId(row);

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
        if (cell != null) {
            String cellValue = cell.getStringCellValue();
            if (hasAuthority(cellValue)) {
                return true;
            }
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
