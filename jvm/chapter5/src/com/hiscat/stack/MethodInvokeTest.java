package com.hiscat.stack;

/**
 * @author Administrator
 */
public class MethodInvokeTest {

}

interface Interface {
    void m();
}

class Show {

    //invokespecial
    public Show(String name) {
//        this();
    }
//invokespecial

    public Show() {
        super();
    }

    public static void showStatic() {
        System.out.println("static");
    }

    public final void showFinal() {
        System.out.println("final");
    }

    private void showPrivate() {
        System.out.println("private");
    }

    public void info() {
        System.out.println("info");
    }

    public void show() {
        //非虚方法
//invokestatic
        showStatic();
//invokespecial
        showPrivate();
//invokespecial
        super.toString();
//invokevirtual
        showFinal();

//invokevirtual
        info();
//invokevirtual
        this.info();
        Interface i = null;
//invokeinterface
        i.m();
    }

}

