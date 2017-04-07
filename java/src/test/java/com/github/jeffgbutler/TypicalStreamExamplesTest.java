package com.github.jeffgbutler;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

/**
 * This is what's usually shown to illustrate map/filter/reduce.  It is useful to understand
 * the basic concepts, but no one has these problems in the real world.
 */
public class TypicalStreamExamplesTest {

    @Test
    public void testIntStreamMapReduce() {
        int answer = IntStream.rangeClosed(1, 10)    // integers from 1 to 10
                .map(i -> i * 3)                     // multiply all by 3
                .filter(i -> i % 2 == 0)             // remove any that aren't even numbers
                .reduce(0, (i1, i2) -> i1 + i2);     // add them up
        
        assertThat(answer, is(90));
    }

    @Test
    public void testIntStreamBuiltIns() {
        int answer = IntStream.rangeClosed(1, 10)
                .map(i -> i * 3)
                .filter(i -> i % 2 == 0)
                .sum();
        
        assertThat(answer, is(90));
    }
    
    @Test
    public void testStrings() {
        String[] strings = {"fred", "pebbles", "wilma"};
        
        List<String> outlist = Arrays.stream(strings)
                .filter(s -> s.length() <= 5)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        
        assertThat(outlist.size(), is(2));
        assertThat(outlist.get(0), is("FRED"));
        assertThat(outlist.get(1), is("WILMA"));
    }
}
