package com.hiscat.jvm.classload;

/**
 * @author Administrator
 */
public class ClassLoaderTest {
    public static void main(String[] args) {
        //jdk.internal.loader.ClassLoaders$AppClassLoader@3fee733d
        System.out.println(ClassLoader.getSystemClassLoader());
        //jdk.internal.loader.ClassLoaders$PlatformClassLoader@234bef66
        System.out.println(ClassLoader.getSystemClassLoader().getParent());
        //null
        System.out.println(ClassLoader.getSystemClassLoader().getParent().getParent());
        //jdk.internal.loader.ClassLoaders$AppClassLoader@3fee733d
        System.out.println(ClassLoaderTest.class.getClassLoader());
        //null
        System.out.println(String.class.getClassLoader());
    }
}
