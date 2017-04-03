package com.github.jeffgbutler;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * This is the worst way to address the problem.
 * 
 * @author Jeff Butler
 *
 */
public class CrappyScriptGenerator implements Generator {

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
            Cell cell = row.getCell(0);
            if (cell != null) {
                String userId = cell.getStringCellValue();
                if (".".equals(userId.substring(1, 2))) {
                    for (int[] columnToApplicationMapping : columnToApplicationMappings) {
                        cell = row.getCell(columnToApplicationMapping[0]);
                        if (cell != null) {
                            String cellValue = cell.getStringCellValue();
                            if ("X".equals(cellValue)) {
                                lines.add(getInsertStatement(userId, columnToApplicationMapping[1]));
                            }
                        }
                    }
                }
            }
        }

        return lines;
    }
}
