package com.histcat.arg;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

class Parser {

    private static final Map<String, Function<String, ?>> DEFAULT_PARSERS = new HashMap<>() {
        {
            put("bool", Boolean::valueOf);
            put("int", s -> Integer.valueOf(s.replace("n", "-")));
            put("long", s -> Long.valueOf(s.replace("n", "-")));
            put("double", s -> Double.valueOf(s.replace("n", "-")));
            put("string", s -> s.substring(1, s.length() - 1));
            put("[int]", makeListParser("int"));
            put("[bool]", makeListParser("bool"));
            put("[long]", makeListParser("long"));
            put("[double]", makeListParser("double"));
            put("[string]", makeListParser("string"));
        }
    };

    private static Function<String, ?> makeListParser(String type) {
        return s -> Arrays.stream(s.split(",")).map(DEFAULT_PARSERS.get(type)).collect(toList());
    }

    private String name;
    private String value;
    private String defaultValue;
    private String type;
    private Function<String, ?> parser;

    Parser(String[] schema) {
        Objects.requireNonNull(schema);
        if (schema.length < 2) {
            throw new IllegalArgumentException(Arrays.toString(schema) + "长度不能小于2");
        }
        this.name = schema[0];
        this.type = schema[1];
        this.defaultValue = schema.length == 3 ? schema[2] : typeIsBool() ? "" : null;
        this.parser = DEFAULT_PARSERS.computeIfAbsent(type, t -> {
            throw new NoSuchElementException(t + "类型不在解析类型列表中");
        });
    }

    private boolean typeIsBool() {
        return type.equals("bool");
    }

    String getName() {
        return name;
    }

    void set(String value) {
        if (!typeIsBool() && defaultValue == null && value == null) {
            throw new IllegalArgumentException(name + "参数无默认值，需指定输入值");
        }
        this.value = typeIsBool() ? "true" : value;
    }

    Object get() {
        return value != null ? parser.apply(value)
                : defaultValue != null ? parser.apply(defaultValue)
                : null;
    }
}
