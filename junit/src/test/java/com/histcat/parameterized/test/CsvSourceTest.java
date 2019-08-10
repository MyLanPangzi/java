package com.histcat.parameterized.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CsvSourceTest {
    @ParameterizedTest
    @CsvSource(value = {
            "banana,1",
            "apple,0xF1",
    })
    public void testWithCsvSource(String fruit, int rank) throws Exception {
        assertNotNull(fruit);
        assertNotEquals(0, rank);

    }
}
