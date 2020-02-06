package com.hiscat.jvm.classload;

public class CIinit4 {
    static {
        A = 20;
//        System.out.println(A);//非法前向引用
    }

    private static int A = 10;

    public static void main(String[] args) {
        System.out.println(A);
    }
}
