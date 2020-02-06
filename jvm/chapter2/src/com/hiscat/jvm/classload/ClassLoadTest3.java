package com.hiscat.jvm.classload;

/**
 * @author Administrator
 */
public class ClassLoadTest3 {
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(Class.forName("java.lang.String").getClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader().getParent());
    }
}
