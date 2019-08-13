package com.histcat.arg;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;


public class ArgTest {
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
    @Test
    public void testParser() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new ArgParser("-d", "l:boolean:false"));
        assertThrows(IllegalArgumentException.class, () -> new ArgParser("-l", "l:boolean:false").get("d"));

        assertEquals(true, new ArgParser("-l", "l:boolean").get("l"));
        assertEquals("1", new ArgParser("-l", "l:string:1").get("l"));
        assertEquals(Integer.valueOf(1), new ArgParser("-l", "l:int:1").get("l"));
        assertEquals(Double.valueOf(100), new ArgParser("-l 100", "l:double:1").get("l"));
        assertEquals(Long.valueOf(100), new ArgParser("-l 100", "l:long:1").get("l"));

        assertIterableEquals(List.of("100"), new ArgParser("-l 100", "l:[string]:[]").get("l"));
        assertIterableEquals(List.of(100), new ArgParser("-l 100", "l:[int]:[]").get("l"));
        assertIterableEquals(List.of(100L), new ArgParser("-l 100", "l:[long]:[]").get("l"));

    }


    private class ArgParser {
        private Map<String, Schema> schemas;
        private Map<String, String> kvs;

        public ArgParser(String input, String strSchemas) {
            resolveSchemas(strSchemas);
            resolveInput(input);
        }

        private void resolveSchemas(String strSchemas) {
            this.schemas = Arrays.stream(strSchemas.split("\\s+"))
                    .map(s -> s.split(":"))
                    .map(Schema::new)
                    .collect(toMap(e -> e.name, e -> e));
        }

        private void resolveInput(String input) {
            kvs = Arrays.stream(input.split("-"))
                    .filter(Predicate.not(String::isBlank))
                    .map(s -> s.strip().split("\\s+"))
                    .peek(kvs -> {
                        if (!schemas.keySet().contains(kvs[0])) {
                            throw new IllegalArgumentException(kvs[0] + "不在参数列表内");
                        }
                    })
                    .collect(toMap(s -> s[0], s -> s.length >= 2 ? s[1] : schemas.get(s[0]).defaultValue));
        }

        private <T> T get(String name) {
            if (!schemas.keySet().contains(name)) {
                throw new IllegalArgumentException(name + "不存在列表中");
            }
            return schemas.get(name).parse(kvs.get(name));
        }

    }

    static class Schema {
        private static final Map<String, Function<String, ?>> types = new HashMap<>() {
            {
                put("int", Integer::valueOf);
                put("integer", Integer::valueOf);
                put("bool", Boolean::valueOf);
                put("boolean", Boolean::valueOf);
                put("string", String::valueOf);
                put("double", Double::valueOf);
                put("long", Long::valueOf);
                put("[string]", s -> Arrays.stream(s.split(",")).map(String::valueOf).collect(toList()));
                put("[int]", s -> Arrays.stream(s.split(",")).map(Integer::valueOf).collect(toList()));
                put("[long]", s -> Arrays.stream(s.split(",")).map(Long::valueOf).collect(toList()));
            }
        };

        private final String name;
        private final String type;
        private final String defaultValue;

        public Schema(String[] schema) {
            this.name = schema[0];
            this.type = schema[1];
            if ("bool".equals(type) || "boolean".equals(type)) {
                this.defaultValue = "true";
            } else {
                this.defaultValue = schema.length == 3 ? schema[2] : null;
            }
        }

        public <T> T parse(String val) {
            if (val == null || val.isBlank()) {
                if (defaultValue == null) {
                    return null;
                }
                return (T) types.get(type).apply(defaultValue);
            }
            if (type.matches("\\[*+\\]")) {

            }
            return (T) types.get(type).apply(val);
        }
    }

}

