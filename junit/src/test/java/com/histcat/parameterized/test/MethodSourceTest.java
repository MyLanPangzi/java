package com.histcat.parameterized.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class MethodSourceTest {
    @ParameterizedTest
    @MethodSource("com.histcat.StringProvider#strings")
    public void testWithExternalMethodSource(String arg) throws Exception{
        assertNotNull(arg);
    }
    @ParameterizedTest
    @MethodSource("stringProvider")
    public void testMethodSource(String arg) throws Exception {
        assertNotNull(arg);
    }

    static Stream<String> stringProvider() {
        return Stream.of("A", "B");
    }

    @ParameterizedTest
    @MethodSource
    public void testWithDefaultMethodSource(String arg) throws Exception {
        assertNotNull(arg);
    }

    static Stream<String> testWithDefaultMethodSource() {
        return Stream.of("A", "B");
    }

    @ParameterizedTest
    @MethodSource
    public void testWithRangeMethodSource(int arg) throws Exception {
        assertNotEquals(9, arg);
    }

    static IntStream testWithRangeMethodSource() {
        return IntStream.range(0, 20).skip(10);
    }

    @ParameterizedTest
    @MethodSource("multi")
    public void testWithMultiArgMethodSource(String name, int i, List<String> list) throws Exception {
        assertTrue(name.length() == 5);
        assertTrue(i > 0);
        assertEquals(2, list.size());
    }

    static Stream<Arguments> multi() {
        return Stream.of(
                arguments("12345", 1, List.of("1", "2")),
                arguments("aplle", 2, List.of("1111", "2222"))
        );
    }

}
