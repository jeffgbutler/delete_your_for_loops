package com.github.jeffgbutler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Step6 is a higher order functions - kind of tortured in Java.  Changes:
 * 
 * 1. New function getStatementBuilderForRow returns a Functor - an object that contains a function
 * 2. The getStatements(Row) method uses the functor and a method reference
 * 
 *  This pattern could be continued in the getStatementBuilderForRow method if you feel you must use
 *  method references everywhere.
 * 
 * @author Jeff Butler
 *
 */
public class FunctionalScriptGeneratorStep6 implements Generator {

    @Override
    public List<String> generate() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/Users.xlsx");
             Workbook workbook = new XSSFWorkbook(is)) {
            return getStatements(workbook.getSheetAt(0));
        }
    }

    private List<String> getStatements(Sheet sheet) {
        return Utils.stream(sheet)
                .flatMap(this::getStatements)
                .collect(Collectors.toList());
    }
    
    private Stream<String> getStatements(Row row) {
        Function<String, Stream<String>> statementBuilder = getStatementBuilderForRow(row);
        return getUserId(row)
                .map(statementBuilder::apply)
                .orElse(Stream.empty());
    }
    
    private Function<String, Stream<String>> getStatementBuilderForRow(Row row) {
        return userId -> Arrays.stream(AppInfo.values())
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
