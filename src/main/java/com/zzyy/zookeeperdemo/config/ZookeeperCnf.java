package com.zzyy.zookeeperdemo.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Auther: zhouyu
 * @Date: 2021/2/1 17:09
 * @Description:
 */
@Configuration
public class ZookeeperCnf {


    @Bean(name = "zk_client")
    public ZooKeeper getZk() {

        ZooKeeper zooKeeper = null;
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

            countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("连接ZK成功...");
        return zooKeeper;
    }

}
