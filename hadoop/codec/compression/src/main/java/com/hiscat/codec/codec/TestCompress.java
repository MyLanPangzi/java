package com.hiscat.codec.codec;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author hiscat
 */
public class TestCompress {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        final String in = "E:\\github\\java\\hadoop\\codec\\compression\\src\\main\\resources\\hello.txt";
        String out = "E:\\github\\java\\hadoop\\codec\\compression\\src\\main\\resources\\output\\hello.txt.bz2";
        String codec = "org.apache.hadoop.io.compress.BZip2Codec";
        compression(in, out, codec);
        Files.deleteIfExists(Paths.get(in));
        decompression(out, in);

        codec = "org.apache.hadoop.io.compress.GzipCodec";
        out = "E:\\github\\java\\hadoop\\codec\\compression\\src\\main\\resources\\output\\hello.txt.gz";
        compression(in, out, codec);
        Files.deleteIfExists(Paths.get(in));
        decompression(out, in);

        codec = "org.apache.hadoop.io.compress.DeflateCodec";
        out = "E:\\github\\java\\hadoop\\codec\\compression\\src\\main\\resources\\output\\hello.txt.deflate";
        compression(in, out, codec);
        Files.deleteIfExists(Paths.get(in));
        decompression(out, in);

    }

    private static void decompression(String in, String out) throws ClassNotFoundException, IOException {
        CompressionCodecFactory factory = new CompressionCodecFactory(new Configuration());
        final CompressionCodec codec = factory.getCodec(new Path(in));
        if (codec == null) {
            System.out.println("codec is null");
            return;
        }
        try (CompressionInputStream inputStream = codec.createInputStream(Files.newInputStream(Paths.get(in)))) {
            Files.copy(inputStream, Paths.get(out));
        }

    }

    private static void compression(String in, String out, String codecClassName) throws ClassNotFoundException, IOException {
        final CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(Class.forName(codecClassName), new Configuration());
        try (CompressionOutputStream outputStream = codec.createOutputStream(Files.newOutputStream(Paths.get(out)))) {
            Files.copy(Paths.get(in), outputStream);
        }
    }

}
