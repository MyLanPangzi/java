import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author hiscat
 */
public class Client {
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        connect();

        watch();

        waiting();
    }

    private static void connect() throws IOException {
        String url = "hadoop100:2181,hadoop101:2181,hadoop102:2181,hadoop103:2181,hadoop104:2181,hadoop105:2181,hadoop106:2181";
        int timeout = 2000;
        zooKeeper = new ZooKeeper(url, timeout, e -> {
            try {
                if (e.getType().equals(Watcher.Event.EventType.None)) {
                    return;
                }
                System.out.println(e.getType());
                watch();
            } catch (KeeperException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

    }

    private static void watch() throws KeeperException, InterruptedException {
        System.out.println(zooKeeper.getChildren("/servers", true));
    }

    private static void waiting() throws InterruptedException {
        TimeUnit.DAYS.sleep(1);
    }
}
