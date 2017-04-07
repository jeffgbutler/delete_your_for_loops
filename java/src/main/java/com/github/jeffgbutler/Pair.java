package com.github.jeffgbutler;

import java.util.function.Function;

public class Pair {
    private int columnNumber;
    private Function<String, String> insertBuilder;
    
    private Pair() {
        super();
    }
    
    public int columnNumber() {
        return columnNumber;
    }
    
    public Function<String, String> insertBuilder() {
        return insertBuilder;
    }
    
    public static Pair of(int columnNumber, Function<String, String> insertBuilder) {
        Pair pair = new Pair();
        pair.columnNumber = columnNumber;
        pair.insertBuilder = insertBuilder;
        return pair;
    }
}
