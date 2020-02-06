package com.hiscat.stack;

/**
 * @author Administrator
 */
public class StackFrameTest {

    private int count = 0;

    public static void main(String[] args) {
        long l = 100;
        StackFrameTest test = new StackFrameTest();
        try {
            test.m1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int m1() {
        System.out.println("m1 start");
        int i = (int) m2();
        System.out.println("m1 end" + this.count);
        return i;
    }

    public double m2() {
        System.out.println("m2 start");
        int i = (int) m3();
        System.out.println("m2 end");
        return i;
    }

    public long m3() {
        System.out.println("m3 start");
        int i = 30;
        System.out.println("m3 end");
        return i;
    }

    public void m4() {
        int a = 0;
        {
            int b = 10;
            b = a + 1;
        }
        int c = 20;
    }
}
