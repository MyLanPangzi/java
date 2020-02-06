package com.hiscat.jvm.classload;

import sun.misc.Launcher;
import sun.net.spi.nameservice.dns.DNSNameService;
import sun.security.ssl.Debug;

import java.util.Arrays;

/**
 * @author Administrator
 */
public class ClassLoadTest2 {
    public static void main(String[] args) {
        System.out.println(Debug.class.getClassLoader());
        Arrays.stream(Launcher.getBootstrapClassPath().getURLs()).forEach(System.out::println);

        System.out.println();
        System.out.println(DNSNameService.class.getClassLoader());
        String ext = System.getProperty("java.ext.dirs");
        Arrays.stream(ext.split(";")).forEach(System.out::println);


        System.out.println();
        String classpath = System.getProperty("java.class.path");
        Arrays.stream(classpath.split(";")).forEach(System.out::println);


    }
}
