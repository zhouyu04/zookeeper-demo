package zookeeper.demo;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

/**
 * @Auther: zhouyu
 * @Date: 2021/1/29 13:50
 * @Description:
 */
public class WatchDemo implements Watcher {

    private static Logger logger = Logger.getLogger(App.class);
    static ZooKeeper zooKeeper;

    static {
        try {
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 4000, new WatchDemo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("静态方法连接zookeeper");
    }

    @Override
    public void process(WatchedEvent event) {


        System.out.println("eventType:" + event.getType());
        if (event.getType() == Event.EventType.NodeDataChanged) {
            try {
                zooKeeper.exists(event.getPath(), true);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String path = event.getPath();
        Event.EventType type = event.getType();

        System.out.println(String.format("path:%s,type:%s", path, type));


    }


    public static void main(String[] args) throws Exception {
        String path = "/watcher";
        if (zooKeeper.exists(path, false) == null) {
            zooKeeper.create("/watcher", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        Thread.sleep(1000);
        System.out.println("-----------");
        //true表示使用zookeeper实例中配置的watcher
        Stat stat = zooKeeper.exists(path, true);
        System.in.read();
    }

}
