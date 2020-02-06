package com.hiscat.stack;

/**
 * @author Administrator
 */
public class OperandStackTest {
    public double m() {
        short s = Short.MAX_VALUE;
        float f = Float.MAX_VALUE;
        int i = Integer.MAX_VALUE;
//        int i2 = Integer.MAX_VALUE;
        byte b = 127;
        long l = Long.MAX_VALUE;
        double d = Double.MAX_VALUE;
        double r = d + d;
        return d;
    }

    public double m2(int i) {
        double d = m();
        d = m();
        return d;
    }

    public void m3() {
        double d = m2(1);
        int i = 10;
        i++;
        ++i;
//        12
//        iload_2
//        iinc 2 by 1
//        isotre_2
        i = i++;
        System.out.println(i);
//        13
//        iinc 2 by 1
//        iload_2
//        istore_2
        i = ++i;
        System.out.println(i);
//        13+15
//        iload_2
//        iinc 2 by 1
//        iinc 2 by 1
//        iload_2
//        iadd
//        iload_2
//        iinc 2 by 1
//        iadd
//        istore_2
        i = i++ + ++i  + i++;
        System.out.println(i);
    }

    public static void main(String[] args) {
        OperandStackTest operandStackTest = new OperandStackTest();
        operandStackTest.m3();
    }
}
