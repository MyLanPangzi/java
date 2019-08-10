package com.histcat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FizzBuzzTest {
// 1 输入数字返回数字
// 2 输入3的倍数或者包含3 返回Fizz
// 3 输入5的倍数或者包含5 返回Buzz
// 4 输入15的倍数 返回FizzBuzz
//2019年8月10日13:42:43 2019年8月10日13:50:30

    @ParameterizedTest(name = "given {0} should return {1}")
    @CsvSource(value = {})
    public void testFizzBuzz() throws Exception {

    }
}
