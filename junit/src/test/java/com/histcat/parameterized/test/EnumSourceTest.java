package com.histcat.parameterized.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class EnumSourceTest {
    @ParameterizedTest
    @EnumSource(value = TimeUnit.class)
    public void testEnumSource(TimeUnit timeUnit) throws Exception {
        assertNotNull(timeUnit);
    }

    @ParameterizedTest
    @EnumSource(value = TimeUnit.class, names = {"DAYS"})
    public void testEnumSourceWithInclude(TimeUnit timeUnit) throws Exception {
        assertNotNull(timeUnit);
    }

    @ParameterizedTest
    @EnumSource(value = TimeUnit.class, mode = EnumSource.Mode.EXCLUDE, names = {"DAYS", "HOURS"})
    public void testEnumSourceWithExclude(TimeUnit timeUnit) throws Exception {
        assertFalse(EnumSet.of(TimeUnit.DAYS, TimeUnit.HOURS).contains(timeUnit));
        assertTrue(timeUnit.name().length() > 5);
    }

    @ParameterizedTest
    @EnumSource(value = TimeUnit.class, mode = EnumSource.Mode.MATCH_ALL, names = "^(M|N).+SECONDS$")
    public void testEnumSourceWithReg(TimeUnit timeUnit) throws Exception {
        assertTrue(timeUnit.name().startsWith("M") || timeUnit.name().startsWith("N"));
        assertTrue(timeUnit.name().endsWith("SECONDS"));
    }

}
