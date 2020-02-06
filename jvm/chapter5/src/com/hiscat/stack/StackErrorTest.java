package com.hiscat.stack;

/**
 * @author Administrator
 * -Xss1m 16377
 * default 5118
 */
public class StackErrorTest {
    private static int count = 0;

    public static void main(String[] args) {
        System.out.println(count++);
        main(args);
    }
}
