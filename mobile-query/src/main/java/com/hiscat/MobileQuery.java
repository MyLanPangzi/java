package com.hiscat;

import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;

/**
 * @author hiscat
 */
public class MobileQuery {
    public static void main(String[] args) throws IOException {
        final Path path = Paths.get("E:\\github\\java\\mobile-query\\src\\main\\resources\\mobile");
        OkHttpClient client = new OkHttpClient();
        final Path result = Paths.get("E:\\github\\java\\mobile-query\\src\\main\\resources\\result.txt");
        final OutputStream out = Files.newOutputStream(result, StandardOpenOption.APPEND);
        Files.lines(path)
//                .limit(1000)
                .forEach(m -> {
                    String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=" + m;
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        final String res = response.body().string().substring(19);
                        final String province = JSONObject.parseObject(res)
                                .getString("province");
                        if (province.equals("湖北")) {
                            final String mobile = JSONObject.parseObject(res)
                                    .getString("telString");
                            System.out.println(mobile);
//                            Files.write(result, (mobile + "\n").getBytes(), StandardOpenOption.APPEND);
                            out.write((mobile + "\n").getBytes());
                        }
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        out.close();


    }
}
