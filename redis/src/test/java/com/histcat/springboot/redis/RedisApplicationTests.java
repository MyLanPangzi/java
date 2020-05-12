package com.histcat.springboot.redis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApplicationTests {

    @Autowired
    StringRedisTemplate st;

    @Test
    public void contextLoads() {
        assertNotNull(st);
    }

    @Before
    public void clean() {
        Objects.requireNonNull(st.getConnectionFactory()).getConnection().flushAll();
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testString() throws InterruptedException {
        ValueOperations<String, String> ops = st.opsForValue();
        ops.set("hello", "world");
        assertEquals(ops.get("hello"), "world");
        ops.append("hello", "world");
        assertEquals(ops.get("hello"), "worldworld");
        assertEquals(Objects.requireNonNull(ops.increment("counter")).toString(), ops.get("counter"));
        assertEquals(ops.increment("k", 2).toString(), ops.get("k"));
//        assertEquals(ops.increment("d", 2.0).toString(), ops.get("d"));
        ops.set("k1", "v1", 1000);
        Thread.sleep(1000);
        assertNull(ops.get("k1"));
        assertTrue(ops.setIfAbsent("kk", "vv"));
        ops.set("k", "v", 1, TimeUnit.SECONDS);
        ops.set("k", "v", Duration.ofSeconds(1));
        ops.decrement("n");
        ops.decrement("n", 1);
        ops.getAndSet("k", "new");
        ops.multiGet(Arrays.asList("1", "k"));
        ops.multiSet(Collections.singletonMap("k", "v"));
        ops.setIfAbsent("k", "v");
        ops.setIfAbsent("k", "v", Duration.ofSeconds(1));
        ops.setIfPresent("k", "v");
        ops.setIfPresent("k", "v", 1, TimeUnit.SECONDS);
    }

    @Test
    public void testSet() {
        final SetOperations<String, String> ops = st.opsForSet();
        ops.add("set", "1", "2", "3");
        ops.members("set");
        ops.size("set");
        ops.isMember("set", "1");
        ops.remove("k", "1");
        ops.pop("set");
        ops.pop("set", 1);
        ops.randomMember("set");
        ops.randomMembers("set", 2);
        ops.move("set", "1", "set1");
        ops.difference("set", "set1");
        ops.union("set", "set1");
        ops.intersect("set", "set1");
        ops.differenceAndStore("set", "set1", "set2");
    }

    @Test
    public void testList() {
        final ListOperations<String, String> ops = st.opsForList();
        ops.leftPush("list", "1");
        ops.leftPop("list");
        ops.rightPush("list", "2");
        ops.rightPop("list");
        ops.size("list");
        ops.index("list", 0);
        ops.remove("list", 1, "0");
        ops.set("list", 0, "a");
        ops.rightPopAndLeftPush("list", "list1");
        ops.trim("list", 1, 1);
    }

    @Test
    public void testMap() {
        final HashOperations<String, Object, Object> ops = st.opsForHash();
        ops.put("h", "name", "hello");
        ops.putAll("h", Collections.singletonMap("age", "20"));
        ops.entries("h");
        ops.keys("h");
        ops.values("h");
        ops.delete("h", "name");
        ops.get("h", "name");
        ops.increment("h", "age", 1);
        ops.size("h");
        ops.lengthOfValue("h", "name");
        ops.multiGet("h", Arrays.asList("name", "age"));
    }

    @Test
    public void testZset() {
        final ZSetOperations<String, String> ops = st.opsForZSet();
        ops.add("zset", "a", 10);
        ops.count("zet", 10, 20);
        ops.incrementScore("zset", "a", 10);
        ops.range("zset", 0, 10);
        ops.rangeWithScores("zset", 0, 2);
        ops.rangeByScore("zset", 10, 20);
        ops.size("zset");
        ops.zCard("zset");
        ops.remove("zset", "a");
        ops.reverseRange("zset", 0, 1);
        ops.reverseRangeWithScores("zset", 0, 1);
        ops.reverseRangeByScoreWithScores("zset", 20, 10);
    }

    @Test
    public void testTx() {
        st.setEnableTransactionSupport(true);
        st.multi();
        st.opsForValue().set("hello", "world");
        st.opsForValue().set("k1", "v1");
        st.exec().forEach(System.out::println);

        //        stringRedisTemplate.discard();
    }


}
