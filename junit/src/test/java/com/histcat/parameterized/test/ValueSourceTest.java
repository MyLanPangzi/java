package com.histcat.parameterized.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValueSourceTest {
    @ParameterizedTest(name = "{arguments} is null or blank string")
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "\n", "\t", "\f",})
    public  void testNullAndEmptySource(String arg) throws Exception {
        assertTrue(arg == null || arg.isBlank());
    }

    @ParameterizedTest(name = "{arguments} should < 6 and {arguments} should > 0")
    @ValueSource(ints = {1, 2, 3, 4, 5,})
    public  void testWithValueSource(int arg) throws Exception {
        assertTrue(arg < 6 && arg > 0);
    }

    @ParameterizedTest(name = "{arguments} is null or blank string")
    @NullSource
    @EmptySource
    @ValueSource(strings = {"", " ", "\n", "\t"})
    public  void nullEmptyAndBlankStrings(String arg) throws Exception {
        assertTrue(arg == null || arg.isBlank());
    }
}
