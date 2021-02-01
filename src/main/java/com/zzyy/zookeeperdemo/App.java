package com.zzyy.zookeeperdemo;

import org.apache.log4j.Logger;
import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 */
public class App {

    private static Logger logger = Logger.getLogger(App.class);
    ZooKeeper zooKeeper;

    {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 4000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {

                    if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                        System.out.println("conecttioned ...");

                        countDownLatch.countDown();
                    }

                }
            });
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("静态方法连接zookeeper");
    }


    public static void main(String[] args) throws Exception {

        logger.info("djashkdhsakj");
        System.out.println("Hello World!");

        App app = new App();

        ZooKeeper.States state = app.zooKeeper.getState();
        System.out.println(state);


        String s = app.zooKeeper.create("/zzyy", "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(s);


    }
}
