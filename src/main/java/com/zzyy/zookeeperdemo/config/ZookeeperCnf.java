package com.zzyy.zookeeperdemo.config;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zookeeper.demo.WatchDemo;

import java.io.IOException;

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
        try {
            zooKeeper = new ZooKeeper("127.0.0.1:2181", 4000, new WatchDemo());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("连接ZK成功...");
        return zooKeeper;
    }

}
