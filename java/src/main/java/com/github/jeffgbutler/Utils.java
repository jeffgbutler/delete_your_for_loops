package com.github.jeffgbutler;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Utils {

    public static String linesToScript(List<String> lines) {
        return lines.stream().collect(Collectors.joining(System.lineSeparator()));
    }

    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
