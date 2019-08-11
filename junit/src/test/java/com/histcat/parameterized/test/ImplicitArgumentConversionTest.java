package com.histcat.parameterized.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class ImplicitArgumentConversionTest {
    @ParameterizedTest
    @ValueSource(strings = "Thinking in java")
    void testWithImplicitArgumentConversion(Book book) {
        assertNotNull(book.getTitle());
    }
}
