import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author hiscat
 */
public class Server {
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws Exception {
        connect();

        register();

        serve();
    }

    private static void connect() throws IOException {
        String url = "hadoop100:2181,hadoop101:2181,hadoop102:2181,hadoop103:2181,hadoop104:2181,hadoop105:2181,hadoop106:2181";
        int timeout = 2000;
        zooKeeper = new ZooKeeper(url, timeout, e -> {
        });

    }

    private static void register() throws KeeperException, InterruptedException {
        zooKeeper.create("/servers/hadoop", "hadoop100".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    private static void serve() throws InterruptedException {
        TimeUnit.DAYS.sleep(1);
    }
}
