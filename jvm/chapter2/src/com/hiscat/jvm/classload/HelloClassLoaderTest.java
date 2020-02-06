package com.hiscat.jvm.classload;

/**
 * @author Administrator
 */
public class HelloClassLoaderTest {
    public static void main(String[] args) {
        ClassLoader appClassLoader = HelloClassLoaderTest.class.getClassLoader();
        System.out.println(appClassLoader);
        ClassLoader extClassLoader = appClassLoader.getClass().getClassLoader();
        System.out.println(extClassLoader);
//        ClassLoader bootstrapClassLoader = extClassLoader.getClass().getClassLoader();
//        System.out.println(bootstrapClassLoader);
    }
}
