package com.histcat;

public class FizzBuzz {
    public static String of(Integer num) {
        if (num == null) {
            return "null";
        }
        if (num % 15 == 0) {
            return "FizzBuzz";
        }
        if (num % 3 == 0 || num.toString().contains("3")) {
            return "Fizz";
        }
        if (num % 5 == 0 || num.toString().contains("5")) {
            return "Buzz";
        }
        return num.toString();
    }
}
