package com.histcat.arg;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;


class ArgTest {
    /*
     *1，应该有几个标记
     *2，每个标记应该是什么类型
     *3，每个标记的缺省值是什么
     * 参数结构指定好以后，就可以把实际接收到的参数列表传给参数解析器（ArgParser Arg Parser）。
     * 解析器会首先验证参数列表是否与参数结构相匹配（Validate)。
     * 然后，程序就可以向参数解析器查询每个参数的值（根据参数的标记名）（getXXX）。
     * 返回值的类型应该与参数结构中规定的类型相一致 （getString）。
     * 如果参数结构中规定了的标记在实际的参数列表中没有出现，那么就应该返回合理的缺省值(getXXX?:defaultValue)，
     * 例如布尔型的缺省值可以是False，数值型的缺省值可以是0，字符串型的缺省值可以是空字符串。
     * 如果实际给出的参数与参数结构不匹配，需要给出良好的错误信息，解释清楚出错的原因。(print error)
     *
     * 扩展代码，支持列表类型的参数。例如下列参数中：
     * -g this,is,a,list -d 1,2,-3,5
     * g标记对应的是字符串类型的列表（[“this”, “is”, “a”, “list”]），d标记对应的是整数类型的列表（[1, 2, -3, 5]）。
     * 代码应该具有良好的可扩展性，这样在添加新的值类型时才会简单明了。
     * 输入的数据类型有 boolean short int long double float string [] [int]
     *-l,l,true
     * '-p 8080', p, 8080
     * */

    @ParameterizedTest
    @MethodSource
    void testSingleValue(String input, String schema, String name, Object value) {
        assertEquals(value, new ArgParser(input, schema).get(name));
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> testSingleValue() {
        return Stream.of(
                arguments("-b", "b:bool", "b", true),
                arguments("", "b:bool", "b", false),
                arguments("", "b:bool:true", "b", true),

                arguments("-i 10", "i:int", "i", 10),
                arguments("-i n10", "i:int", "i", -10),
                arguments("", "i:int", "i", null),
                arguments("", "i:int:10", "i", 10),

                arguments("-l 1000000000", "l:long", "l", 1000000000L),
                arguments("-l n1000000000", "l:long", "l", -1000000000L),
                arguments("", "l:long", "l", null),
                arguments("", "l:long:100", "l", 100L),

                arguments("-d 10.0", "d:double", "d", 10.0),
                arguments("-d n10.0", "d:double", "d", -10.0),
                arguments("", "d:double", "d", null),
                arguments("", "d:double:10.0", "d", 10.0),

                arguments("-s 's a b c'", "s:string", "s", "s a b c"),
                arguments("", "s:string", "s", null),
                arguments("", "s:string:'s'", "s", "s")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testIterable(String input, String schema, String name, List<?> value) {
        assertIterableEquals(value, new ArgParser(input, schema).get(name));
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> testIterable() {
        return Stream.of(
                arguments("-list 1,2", "list:[int]", "list", List.of(1, 2)),
                arguments("-list 1,2", "list:[long]", "list", List.of(1L, 2L)),
                arguments("-list 1.0,2.0", "list:[double]", "list", List.of(1.0, 2.0)),
                arguments("-list 'a b','d c'", "list:[string]", "list", List.of("a b", "d c")),
                arguments("-list false,true", "list:[bool]", "list", List.of(false, true))
        );
    }
}

