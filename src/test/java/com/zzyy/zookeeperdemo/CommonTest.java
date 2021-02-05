package com.zzyy.zookeeperdemo;

import com.zzyy.zookeeperdemo.config.RedisUtils;
import com.zzyy.zookeeperdemo.utils.User;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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


        long lzy = redisUtils.listSize("lzy");
        System.out.println("列表长度:" + lzy);

        redisUtils.leftPush("lzy", "000");
        redisUtils.leftPushAll("lzy", new String[]{"222", "333"});

        redisUtils.rightPush("lzy", "999");
        redisUtils.rightPushAll("lzy", new String[]{"888", "777"});

        redisUtils.listSet("lzy", "555", 5);

        Object lzy1 = redisUtils.getIndex("lzy", 5);
        System.out.println(lzy1);

        Object o = redisUtils.leftPop("lzy");
        System.out.println(o);
        Object o1 = redisUtils.rightPop("lzy");
        System.out.println(o1);

        redisUtils.remove("lzy", "555", 0);

    }


    @Test
    public void testHash() {

        redisUtils.put("zyhash", "name", "周渔");

        Object o = redisUtils.get("zyhash", "name");
        System.out.println("获取name:" + o);

        boolean b = redisUtils.hasKey("zyhash", "name");
        System.out.println("是否存在:" + b);

        redisUtils.deleteHash("zyhash", "name");

        boolean b2 = redisUtils.hasKey("zhash", "name");
        System.out.println("是否存在b2:" + b2);


        Map<String, Object> map = new HashMap<>();
        map.put("name", "周渔");
        map.put("age", "123");
        map.put("user", new User().toString());

        redisUtils.putAll("zhash", map);

        Set<Object> zhash = redisUtils.keys("zhash");
        System.out.println(zhash);
        List<Object> zhash1 = redisUtils.values("zhash");
        System.out.println(zhash1);
        Map<String, Object> zhash2 = redisUtils.entries("zhash");
        System.out.println(zhash2);
//
        Cursor<Map.Entry<String, Object>> zhash3 = redisUtils.scan("zhash");

        while (zhash3.hasNext()) {
            Map.Entry<String, Object> next = zhash3.next();
            System.out.println(next.getKey() + ":" + next.getValue());
        }

        long zhash4 = redisUtils.hashSize("zhash");
        System.out.println(zhash4);

    }


    @Test
    public void testSet() {

        redisUtils.add("zySet", "123", "456","000");
        redisUtils.add("zzyySet", "aaa", "bbb");

        redisUtils.remove("zySet", "123");


        redisUtils.add("zySet", "789");

        Object zySet = redisUtils.pop("zySet");
        System.out.println("获取值:" + zySet);

        boolean move = redisUtils.move("zySet", "789", "zzyySet");
        System.out.println(move);


        long zySet1 = redisUtils.setSize("zySet");
        System.out.println(zySet1);

        Set<Object> zySet2 = redisUtils.members("zySet");
        System.out.println(zySet2);


        Cursor<Object> zySet3 = redisUtils.setScan("zzyySet", ScanOptions.NONE);
        while (zySet3.hasNext()) {
            System.out.println(zySet3.next());
        }

    }


}
