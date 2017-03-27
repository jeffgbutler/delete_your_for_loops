package com.github.jeffgbutler;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Utils {

    public static String linesToScript(List<String> lines) {
        return lines.stream().collect(Collectors.joining(System.lineSeparator()));
    }

    public static <T> Iterable<T> iterable(Iterator<T> iterator) {
        return () -> iterator;
    }

    public static <T> Stream<T> stream(Iterator<T> iterator) {
        return StreamSupport.stream(iterable(iterator).spliterator(), false);
    }
}
