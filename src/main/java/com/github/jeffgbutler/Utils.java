package com.github.jeffgbutler;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static String linesToScript(List<String> lines) {
        return lines.stream()
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
