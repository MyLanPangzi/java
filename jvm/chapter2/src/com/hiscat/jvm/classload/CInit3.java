package com.hiscat.jvm.classload;

public class CInit3 {

    public static void main(String[] args) {
        System.out.println(Son.B);
    }
}

class Father {
    protected static int A = 10;
}

class Son extends Father {
    static {
        B = 20;
    }
    public static int B = A;
}