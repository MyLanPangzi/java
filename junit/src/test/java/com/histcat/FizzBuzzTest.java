package com.histcat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FizzBuzzTest {
// 1 输入数字返回数字
// 2 输入3的倍数或者包含3 返回Fizz
// 3 输入5的倍数或者包含5 返回Buzz
// 4 输入15的倍数 返回FizzBuzz
//2019年8月10日13:42:43 2019年8月10日13:50:30
    //2019年8月10日14:02:14 2019年8月10日14:06:13
//    2019年8月10日14:07:38 2019年8月10日14:08:23

    @ParameterizedTest
    @CsvSource({
            "1,1",
            ",null",
            "3,Fizz",
            "5,Buzz",
            "15,FizzBuzz",
    })
    void testFizzBuzz(Integer input, String output) {
        assertEquals(output, FizzBuzz.valueOf(input));
        FizzBuzz.stream(100).forEach(System.out::println);
    }
}
