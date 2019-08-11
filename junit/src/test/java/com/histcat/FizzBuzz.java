package com.histcat;

import java.util.stream.IntStream;
import java.util.stream.Stream;

class FizzBuzz {
    static String valueOf(Integer input) {
        if (input == null) {
            return "null";
        }
        if (input % 15 == 0) {
            return "FizzBuzz";
        }
        if (input % 3 == 0) {
            return "Fizz";
        }
        if (input % 5 == 0) {
            return "Buzz";
        }
        return input.toString();
    }

    static Stream<String> stream(Integer input) {
        if (input == null) {
            return Stream.of("null");
        }
        return IntStream.rangeClosed(1, input)
                .mapToObj(FizzBuzz::valueOf);
    }
}
