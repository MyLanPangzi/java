package com.hiscat.jvm.classload;

public class CInit5 {
    public static void main(String[] args) {
        Runnable r = () -> {
            System.out.println(Thread.currentThread().getName());
            MyLock myLock = new MyLock();
            System.out.println(Thread.currentThread().getName());
        };
        Thread t1 = new Thread(r, "线程1");
        Thread t2 = new Thread(r, "线程2");
        t1.start();
        t2.start();
    }

    static class MyLock {
        static {
            System.out.println(Thread.currentThread().getName() + "coming ");
            if (true) {
                while (true) {
                }
            }
        }
    }
}
