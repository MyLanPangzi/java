package com.hiscat.jedis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JedisTest {

    Jedis jedis;

    @BeforeEach
    void setup() {
        jedis = new Jedis("192.168.11.128", 6379);
    }

    @Test
    public void testJedis() {
        assertNotNull(jedis.ping());
    }

    @Test
    public void testSet() {
        String s = jedis.set("k1", "v1");
        assertEquals(s, "OK");
    }
}
