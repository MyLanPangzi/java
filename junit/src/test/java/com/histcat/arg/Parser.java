package com.histcat.arg;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class Parser {

    private static final Map<String, Function<String, ?>> DEFAULT_PARSERS = new HashMap<>() {
        {
            put("bool", Boolean::valueOf);
            put("int", Integer::valueOf);
            put("long", Long::valueOf);
            put("double", Double::valueOf);
            put("string", String::valueOf);
            put("[int]", s -> Arrays.stream(s.split(",")).map(Integer::valueOf).collect(toList()));
            put("[bool]", s -> Arrays.stream(s.split(",")).map(Boolean::valueOf).collect(toList()));
            put("[long]", s -> Arrays.stream(s.split(",")).map(Long::valueOf).collect(toList()));
            put("[double]", s -> Arrays.stream(s.split(",")).map(Double::valueOf).collect(toList()));
            put("[string]", s -> Arrays.stream(s.split(",")).map(String::valueOf).collect(toList()));
        }
    };

    private String name;
    private String value;
    private String defaultValue;
    private String type;
    private Function<String, ?> parser;

    public Parser(String[] schema) {
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

    public Object get() {
        return value != null ? parser.apply(value)
                : defaultValue != null ? parser.apply(defaultValue)
                : null;
    }
}
