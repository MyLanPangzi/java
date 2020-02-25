package com.hiscat.zk;

import org.apache.zookeeper.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author hiscat
 */
public class ZkTest {

    private static ZooKeeper zooKeeper;

    @BeforeAll
    static void testConnect() throws IOException {
        String url = "hadoop100:2181,hadoop101:2181,hadoop102:2181,hadoop103:2181,hadoop104:2181,hadoop105:2181,hadoop106:2181";
        int timeout = 2000;
        zooKeeper = new ZooKeeper(url, timeout, e -> {
            try {
                if (e.getType().equals(Watcher.Event.EventType.None)) {
                    return;
                }
                System.out.println(e.getType());
                zooKeeper.getChildren(e.getPath(), false).forEach(System.out::println);
            } catch (KeeperException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

    }

    @Test
    void testCreateChild() throws KeeperException, InterruptedException {
        zooKeeper.create("/hello", "world".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        zooKeeper.getChildren("/", false).forEach(System.out::println);
    }

    @Test
    void testGetAndWatch() throws KeeperException, InterruptedException {
        zooKeeper.getChildren("/", true);
        zooKeeper.create("/hiscat", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    void testExists() throws KeeperException, InterruptedException {
        System.out.println(zooKeeper.exists("/", false));
    }
}
