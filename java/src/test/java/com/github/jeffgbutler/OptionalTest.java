package com.github.jeffgbutler;

import java.util.Optional;

public class OptionalTest {

    public void syntaxSugar() {
        Optional<String> name = getName();
        name.ifPresent(n -> {
            System.out.println(n);
        });
    }

    private Optional<String> getName() {
        return Optional.ofNullable("fred");
    }


    public boolean isFred() {
        return getName()
                .map(n -> "fred".equals(n))
                .orElse(false);
    }
    
    
}
