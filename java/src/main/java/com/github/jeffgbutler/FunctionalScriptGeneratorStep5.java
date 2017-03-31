package com.github.jeffgbutler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Step5 is a bit of a rethinking.  Changes:
 * 
 * 1. getUserId now returns an optional
 * 2. Added a third getStatements overload that accepts row and user id
 * 3. Change the second getStatements overload to use Optional map and orElse
 * 4. No longer need the first filter for hasUserId or that function
 * 
 * Streams everywhere, no for loops, no if statements
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep5 implements Generator {

    @Override
    public List<String> generate(Sheet sheet) {
        return Utils.stream(sheet)
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
        return getCell(row, 0)
                .map(Cell::getStringCellValue)
                .filter(this::isValidUserId);
    }

    private boolean hasAuthority(Row row, AppInfo appInfo) {
        return getCell(row, appInfo.columnNumber())
                .map(Cell::getStringCellValue)
                .map(this::hasAuthority)
                .orElse(false);
    }
    
    private boolean hasAuthority(String value) {
        return "X".equals(value);
    }

    private boolean isValidUserId(String value) {
        return ".".equals(value.substring(1, 2));
    }
    
    private Optional<Cell> getCell(Row row, int cellNumber) {
        return Optional.ofNullable(row.getCell(cellNumber));
    }
}
