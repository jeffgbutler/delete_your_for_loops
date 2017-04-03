package com.github.jeffgbutler;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Utils {

    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
