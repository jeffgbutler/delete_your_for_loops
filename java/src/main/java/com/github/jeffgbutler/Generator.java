package com.github.jeffgbutler;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

public interface Generator {
    List<String> generate(Sheet sheet);
}
