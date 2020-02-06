package com.hiscat.stack;

import java.io.FileReader;
import java.io.IOException;

/**
 * @author Administrator
 */
public class ReturnAddressTest {
    public byte methodByte() {
        return Byte.MAX_VALUE;
    }

    public boolean methodBool() {
        return Boolean.FALSE;
    }

    public char methodChar() {
        return Character.MAX_VALUE;
    }

    public short methodShort() {
        return Short.MAX_VALUE;
    }

    public int methodInt() {
        return Integer.MAX_VALUE;
    }

    public long methodLong() {
        return Long.MAX_VALUE;
    }

    public float methodFloat() {
        return Float.MAX_VALUE;
    }

    public double methodDouble() {
        return Double.MAX_VALUE;
    }

    public String methodString() {
        return "Hello";
    }

    public void methodVoid() {

    }

    /**
     *  public void m1();
     *     descriptor: ()V
     *     flags: (0x0001) ACC_PUBLIC
     *     Code:
     *       stack=1, locals=2, args_size=1
     *          0: aload_0
     *          1: invokevirtual #19                 // Method m2:()V
     *          4: goto          12
     *          7: astore_1
     *          8: aload_1
     *          9: invokevirtual #21                 // Method java/lang/Exception.printStackTrace:()V
     *         12: return
     *       Exception table:
     *          from    to  target type
     *              0     4     7   Class java/lang/Exception
     *       LocalVariableTable:
     *         Start  Length  Slot  Name   Signature
     *             8       4     1     e   Ljava/lang/Exception;
     *             0      13     0  this   Lcom/hiscat/stack/ReturnAddressTest;
     */
    public void m1() {
        try {
            m2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     Code:
     stack=3, locals=4, args_size=1
     0: new           #22                 // class java/io/FileReader
     3: dup
     4: ldc           #23                 // String a.txt
     6: invokespecial #24                 // Method java/io/FileReader."<init>":(Ljava/lang/String;)V
     9: astore_1
     10: sipush        1024
     13: newarray       char
     15: astore_2
     16: aload_1
     17: aload_2
     18: invokevirtual #25                 // Method java/io/FileReader.read:([C)I
     21: istore_3
     22: aload_1
     23: invokevirtual #26                 // Method java/io/FileReader.close:()V
     26: return
     * @throws IOException
     */
    public void m2() throws IOException {
        FileReader reader = new FileReader("a.txt");
        char[] buff = new char[1024];
        int read = reader.read(buff);
        reader.close();
    }
}
