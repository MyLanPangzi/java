package com.histcat.parameterized.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExplicitArgumentConversionTest {
    @ParameterizedTest
    @EnumSource(value = TimeUnit.class)
    void testWithExplicitArgumentConversion(@ConvertWith(ToStringArgumentConverter.class) String arg) {
        assertNotNull(arg);
    }

    private static class ToStringArgumentConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertEquals(String.class, targetType, "Can only to String");
            return String.valueOf(source);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"01.01.2019"})
    void testWithExplicitJavaTimeConverter(@JavaTimeConversionPattern("dd.MM.yyyy") LocalDate date) {
        assertNotNull(date);
    }
}
