package com.histcat.arg;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SchemaTest {

    @ParameterizedTest
    @MethodSource
    void testPrimitiveType(String schema, String input, Object value) {
        assertEquals(value, new Schema(schema).getValue(input));
    }

    static Stream<Arguments> testPrimitiveType() {
        return Stream.of(
                arguments("b:bool", null, null),
                arguments("b:bool:true", null, true),
                arguments("b:bool", "true", true),
                arguments("b:bool", "false", false),

                arguments("b:int", null, null),
                arguments("b:int:10", null, 10),
                arguments("b:int", "1", 1),
                arguments("b:int", "-1", -1)
        );
    }

    @ParameterizedTest
    @MethodSource("testListValue")
    void testListValue(String schema, String input, List<?> value) {
        assertIterableEquals(value, new Schema(schema).getValue(input));
    }

    static Stream<Arguments> testListValue() {
        return Stream.of(
                arguments("b:[bool]", null, null),
                arguments("b:[bool]:true,false", null, List.of(true, false)),
                arguments("b:[bool]", "false", List.of(false)),
                arguments("b:[bool]", "false,true", List.of(false, true))
        );
    }

    @SuppressWarnings("FieldCanBeLocal")
    private static class Schema {
        private static final Map<String, Function<String, ?>> DEFAULT_TYPE_PARSER = new HashMap<>() {
            {
                put("bool", Boolean::valueOf);
                put("int", Integer::valueOf);
                put("[bool]", s -> Arrays.stream(s.split(",")).map(Boolean::valueOf).collect(toList()));
            }
        };
        private String name;
        private Object defaultValue;
        private String type;
        private Function<String, ?> parser;

        Schema(String schema) {
            String[] split = schema.split(":");
            Objects.requireNonNull(split);
            this.name = split[0];
            this.type = split[1];
            this.parser = DEFAULT_TYPE_PARSER.computeIfAbsent(this.type, type -> {
                throw new NoSuchElementException(type + "不在类型解析列表中");
            });

            if (split.length == 3) {
                this.defaultValue = parser.apply(split[2]);
            }
        }

        @SuppressWarnings("unchecked")
        <T> T getValue(String input) {
            return input == null ? (T) defaultValue : (T) parser.apply(input);

        }
    }

}
