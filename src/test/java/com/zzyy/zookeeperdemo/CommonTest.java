package com.zzyy.zookeeperdemo;

import com.zzyy.zookeeperdemo.config.RedisUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

/**
 * @Auther: zhouyu
 * @Date: 2021/2/4 10:35
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZookeeperDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommonTest {

    @Autowired
    RedisUtils redisUtils;

    @Test
    public void testStr() throws InterruptedException {

        redisUtils.set("str", "123");

        System.out.println("是否存在:" + redisUtils.hasKey("str"));
        System.out.println("获取值:" + redisUtils.get("str"));
        redisUtils.expire("str", 3, null);

        Thread.sleep(3000);
        System.out.println("获取值2:" + redisUtils.get("str"));

        //------------------------------
        redisUtils.setEx("s2", "5225", 5, null);
        System.out.println("获取值s2:" + redisUtils.get("s2"));
        System.out.println("获取值s2过期时间:" + redisUtils.getExpire("s2"));
        redisUtils.delete("s2");
        System.out.println("再次获取值s2:" + redisUtils.get("s2"));

        //-------------------------------
        boolean s3 = redisUtils.setNx("s3", Double.toString(RandomUtils.nextDouble()), Duration.ofSeconds(10));
        System.out.println("set nx s3:" + s3);
        System.out.println("获取值s3:" + redisUtils.get("s3"));
        boolean s31 = redisUtils.setNx("s3", Long.toString(RandomUtils.nextLong()), Duration.ofSeconds(5));
        System.out.println("set nx s31:" + s31);
        System.out.println("再次获取值s3:" + redisUtils.get("s3"));
        Thread.sleep(10000);
        System.out.println("再再获取获取值s3:" + redisUtils.get("s3"));

        boolean s32 = redisUtils.setNx("s3", "123", Duration.ofSeconds(50));
        System.out.println("set nx s32:" + s32);
        System.out.println("最后获取值s3:" + redisUtils.get("s3"));

        redisUtils.set("s4", "123");
        Object s4 = redisUtils.getAndSet("s4", "456");
        System.out.println("get and set s4:" + s4);
        System.out.println("获取值s4:" + redisUtils.get("s4"));

        redisUtils.append("s4", "789");
        System.out.println("再次获取值s4:" + redisUtils.get("s4"));

        redisUtils.setOff("s4", "000", 3);
        System.out.println("再再次获取值s4:" + redisUtils.get("s4"));
        long s41 = redisUtils.size("s4");
        System.out.println("获取s4长度:" + s41);

        redisUtils.deleteKeys(new String[]{"s3", "s4"});

    }


    @Test
    public void testList() {

        long zyStr = redisUtils.listSize("zyStr");
        System.out.println(zyStr);

    }

}
