package com.zzyy.zookeeperdemo.redis;

import redis.clients.jedis.Jedis;

/**
 * @Auther: zhouyu
 * @Date: 2021/2/3 18:12
 * @Description:
 */
public class TestRedis {


    public static void main(String[] args) {

        Jedis jedis = new Jedis("121.4.27.171", 6379);
        jedis.auth("zzyy!@#");
        System.out.println("连接成功");
        jedis.set("zzyy", "hello world");

        String zzyy = jedis.get("zzyy");
        System.out.println(zzyy);

    }

}
