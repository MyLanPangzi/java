package com.hiscat.azkaban.hello;

import java.io.IOException;

/**
 * @author hiscat
 */
public class HelloTest {
    public void run() throws IOException {
//        final Path path = Paths.get("/opt/data/azkaban/hello");
//        Files.createDirectories(path.getParent());
//        Files.createFile(path);
//        Files.write(path, "hello".getBytes());
    }

    public static void main(String[] args) throws IOException {
        new HelloTest().run();
    }
}
