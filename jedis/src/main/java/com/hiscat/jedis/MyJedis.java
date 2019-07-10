package com.hiscat.jedis;

import redis.clients.jedis.Jedis;

public class MyJedis {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.11.128", 7000);
        System.out.println(jedis.ping());
    }
}
