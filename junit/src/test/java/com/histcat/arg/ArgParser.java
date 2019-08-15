package com.histcat.arg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toMap;

class ArgParser {
    private Map<String, Object> values;

    ArgParser(String input, String schema) {
        //1，解析schema->Parser
        Map<String, Parser> parsers = resolveSchema(schema);
        //2，解析input,为schema设值
        resolveInput(input, parsers);
        // 3，缓存
        values = parsers.values().stream()
                .collect(HashMap::new, (map, p) -> map.put(p.getName(), p.get()), HashMap::putAll);
    }

    private Map<String, Parser> resolveSchema(String schema) {
        return Arrays.stream(schema.split("\\s+"))
                .filter(Predicate.not(String::isBlank))
                .map(s -> s.split(":"))
                .map(Parser::new)
                .collect(toMap(Parser::getName, p -> p));
    }

    private void resolveInput(String input, Map<String, Parser> parsers) {
        Arrays.stream(input.split("-"))
                .filter(Predicate.not(String::isBlank))
                .map(s -> s.strip().split("\\s+", 2))
                .forEach(kv -> parsers.computeIfAbsent(kv[0], key -> {
                            throw new NoSuchElementException(key + "不在参数解析列表中");
                        }).set(kv.length == 2 ? kv[1] : null)
                );
    }

    @SuppressWarnings("unchecked")
    <T> T get(String name) {
        return (T) values.get(name);
    }
}
