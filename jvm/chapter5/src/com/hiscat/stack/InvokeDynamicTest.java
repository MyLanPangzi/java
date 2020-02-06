package com.hiscat.stack;

import java.util.function.Consumer;

/**
 * @author Administrator
 */
public class InvokeDynamicTest {
    public static void main(String[] args) {
        Consumer<String > consumer = s -> {};
        consumer.accept("hello");
    }
}
