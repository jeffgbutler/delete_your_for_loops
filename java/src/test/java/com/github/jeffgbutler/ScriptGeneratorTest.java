package com.github.jeffgbutler;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class ScriptGeneratorTest {

    private void testGenerator(Generator generator) throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/Users.xlsx");
                Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<String> lines = generator.generate(sheet);
            assertThat(lines.size(), is(44));
            assertThat(lines.get(0), is("insert into ApplicationPermission(user_id, application_id) values('t.wilson', 2237);"));
            assertThat(lines.get(22), is("insert into ApplicationPermission(user_id, application_id) values('b.walton', 4352);"));
            assertThat(lines.get(43), is("insert into ApplicationPermission(user_id, application_id) values('e.nash', 5565);"));
        }
    }
    
    @Test
    public void testCrappyGenerator() throws IOException {
        testGenerator(new CrappyScriptGenerator());
    }
    
    @Test
    public void testFunctionalGenerator1() throws IOException {
        testGenerator(new FunctionalScriptGeneratorStep1());
    }

    @Test
    public void testFunctionalGenerator2() throws IOException {
        testGenerator(new FunctionalScriptGeneratorStep2());
    }
    
    @Test
    public void testFunctionalGenerator3() throws IOException {
        testGenerator(new FunctionalScriptGeneratorStep3());
    }
    
    @Test
    public void testFunctionalGenerator4() throws IOException {
        testGenerator(new FunctionalScriptGeneratorStep4());
    }
    
    @Test
    public void testFunctionalGenerator4a() throws IOException {
        testGenerator(new FunctionalScriptGeneratorStep4a());
    }
    
    @Test
    public void testFunctionalGenerator5() throws IOException {
        testGenerator(new FunctionalScriptGeneratorStep5());
    }

    @Test
    public void testFunctionalGenerator6() throws IOException {
        testGenerator(new FunctionalScriptGeneratorStep6());
    }
}
