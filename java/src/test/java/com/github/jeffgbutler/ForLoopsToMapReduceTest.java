package com.github.jeffgbutler;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class ForLoopsToMapReduceTest {

    public void mapExample() {
        String[] names = {"Fred", "Wilma", "Barney", "Betty"};

        // three equivalent streams.
        // This code does nothing because there is no terminal operation
        Arrays.stream(names)
        .map(name -> name.toUpperCase());
        
        Arrays.stream(names)
        .map(String::toUpperCase);
        
        Arrays.stream(names)
        .map(name -> {
            return name.toUpperCase();
        });
    }
    
    
    public void flatMapExample() {
        String[][] names = {
                {"Fred", "Wilma", "Pebbles"},
                {"Barney", "Betty", "Bamm Bamm"}
        };

        // three equivalent streams of Strings.
        // This code does nothing because there is no terminal operation
        Arrays.stream(names)
        .flatMap(family -> Arrays.stream(family));
        
        Arrays.stream(names)
        .flatMap(Arrays::stream);
        
        Arrays.stream(names)
        .flatMap(family -> {
            return Arrays.stream(family);
        });
    }
    

    public void filterExample() {
        String[] names = {"Fred", "Wilma", "Barney", "Betty"};

        // three equivalent streams.
        // This code does nothing because there is no terminal operation
        Arrays.stream(names)
        .filter(name -> name.length() > 4);
        
        Arrays.stream(names)
        .filter(this::filterOutFred);
        
        Arrays.stream(names)
        .filter(name -> {
            return name.length() > 4;
        });
    }
    
    public boolean filterOutFred(String name) {
        return name.length() > 4;
    }
    
    @Test
    public void reduceExample() {
        String[] names = {"Fred", "Wilma", "Barney", "Betty"};

        String s = Arrays.stream(names)
        .reduce("", (s1, s2) -> s1 + s2);
        
        assertThat(s, is("FredWilmaBarneyBetty"));
    }
    
    @Test
    public void collectExample() {
        String[] names = {"Fred", "Wilma", "Barney", "Betty"};

        String s = Arrays.stream(names)
        .collect(Collectors.joining(", "));
        
        assertThat(s, is("Fred, Wilma, Barney, Betty"));
    }
    

    @Test
    public void pipeLineExample() {
        String s = IntStream.range(0, 20)
                .filter(i -> i % 2 == 0)
                .map(i -> i * i)
                .filter(i -> i % 16 == 0)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", "));
        
        assertThat(s, is("0, 16, 64, 144, 256"));
    }
    
    
    public void forLoopIsAMap() {
        String[] names = {"Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm Bamm"};
        
        // imperative...
        List<String> upperNames = new ArrayList<>();
        for (String name : names) {
            upperNames.add(name.toUpperCase());
        }
        
        // functional...
        upperNames = Arrays.stream(names)
                .map(s -> s.toUpperCase())
                .collect(Collectors.toList());
    }

    public void forLoopIsAFilter() {
        String[] names = {"Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm Bamm"};
        
        // imperative...
        List<String> longNames = new ArrayList<>();
        for (String name : names) {
            if (name.length() > 4) {
                longNames.add(name);
            }
        }
        
        // imperative...
        longNames = new ArrayList<>();
        for (String name : names) {
            if (name.length() < 5) {
                continue;
            }

            longNames.add(name);
        }
        
        // functional...
        longNames = Arrays.stream(names)
                .filter(s -> s.length() > 4)
                .collect(Collectors.toList());
    }

    public void forLoopIsReduction() {
        String[] names = {"Fred", "Wilma", "Pebbles", "Barney", "Betty", "Bamm Bamm"};
        
        // imperative...
        StringBuilder buffer = new StringBuilder();
        buffer.append("The main characters on the Flinstones are ");
        boolean first = true;
        for (String name : names) {
            if (first) {
                first = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(name);
        }
        String sentence = buffer.toString();
        
        // functional...with reduce (this isn't exactly correct - has an extra comma)
        sentence = Arrays.stream(names)
                .reduce("The main characters on the Flinstones are ", (s1, s2) -> s1 + ", " + s2);
        
        // functional...with collectors
        sentence = Arrays.stream(names)
                .collect(Collectors.joining(", ", "The main characters on the Flinstones are ", "."));
    }
}
