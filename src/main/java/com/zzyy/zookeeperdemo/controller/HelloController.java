package com.zzyy.zookeeperdemo.controller;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: zhouyu
 * @Date: 2021/2/1 17:34
 * @Description:
 */
@RestController
public class HelloController {


    @Autowired
    ZooKeeper zk_client;


    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }


    @RequestMapping("/hello/zk")
    public String zk() {

        String name = zk_client.getState().name();
        System.out.println(name);
        return name;
    }


}
