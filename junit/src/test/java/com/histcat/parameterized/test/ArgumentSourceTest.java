package com.histcat.parameterized.test;

import com.histcat.MyArgumentProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ArgumentSourceTest {
    @ParameterizedTest
    @ArgumentsSource(MyArgumentProvider.class)
    public void testWithArgumentSource(String arg) throws Exception {
        assertNotNull(arg);
    }
}
