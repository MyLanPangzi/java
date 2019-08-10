package com.histcat.parameterized.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CsvFileSourceTest {
    @ParameterizedTest
    @CsvFileSource(resources = "/two-column.csv", numLinesToSkip = 1)
    public void testWithCsvFileSource(String country, int reference) throws Exception{
        assertNotNull(country);
        assertNotEquals(0, reference);
    }
}
