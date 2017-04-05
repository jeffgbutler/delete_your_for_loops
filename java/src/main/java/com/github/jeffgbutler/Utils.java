package com.github.jeffgbutler;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Utils {

    static <T> Stream<T> stream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
