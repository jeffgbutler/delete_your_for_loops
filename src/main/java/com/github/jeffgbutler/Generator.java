package com.github.jeffgbutler;

import java.io.IOException;
import java.util.List;

public interface Generator {
    List<String> generate() throws IOException;
}
