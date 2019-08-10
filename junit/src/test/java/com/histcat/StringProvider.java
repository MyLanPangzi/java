package com.histcat;

import java.util.stream.Stream;

public class StringProvider {
    static Stream<String> strings() {
        return Stream.of("A", "B");
    }
}
