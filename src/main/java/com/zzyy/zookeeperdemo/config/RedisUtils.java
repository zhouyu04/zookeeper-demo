package com.zzyy.zookeeperdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: zhouyu
 * @Date: 2021/2/4 13:57
 * @Description:https://blog.csdn.net/weixin_40461281/article/details/82011670
 */
@Component
public class RedisUtils {

    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 功能描述: 删除key
     *
     * @auther: zhouyu
     * @date: 2021/2/4 13:59
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 功能描述: 删除多个key
     *
     * @auther: zhouyu
     * @date: 2021/2/4 13:59
     */
    public void deleteKeys(String... keys) {
        if (keys.length == 0) {
            throw new RuntimeException("删除KEY值不能为空");
        }

        List<String> strings = Arrays.asList(keys.clone());
        redisTemplate.delete(strings);
    }

    /**
     * 功能描述: 指定key的过期时间，默认秒
     *
     * @auther: zhouyu
     * @date: 2021/2/4 14:01
     */
    public void expire(String key, long time, TimeUnit timeUnit) {
        if (timeUnit == null) {
            timeUnit = TimeUnit.SECONDS;
        }
        redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 功能描述: 获取指定key的过期时间
     *
     * @auther: zhouyu
     * @date: 2021/2/4 14:02
     */
    public long getExpire(String key) {

        return redisTemplate.getExpire(key);
    }


    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }


    /*------------------------String类型操作--------------------------*/

    /**
     * 功能描述: 设置String
     *
     * @auther: zhouyu
     * @date: 2021/2/4 15:34
     */
    public void set(String key, String value) {

        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 功能描述: 设置带过期时间
     *
     * @auther: zhouyu
     * @date: 2021/2/4 15:36
     */
    public void setEx(String key, String value, long time, TimeUnit timeUnit) {

        if (timeUnit == null) {
            timeUnit = TimeUnit.SECONDS;
        }
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }


    /**
     * 功能描述: 覆写(overwrite)给定 key 所储存的字符串值，从偏移量 offset 开始
     *
     * @auther: zhouyu
     * @date: 2021/2/4 15:38
     */
    public void setOff(String key, String value, long offSet) {
        redisTemplate.opsForValue().set(key, value, offSet);
    }

    /**
     * 功能描述: 根据key获取值
     *
     * @auther: zhouyu
     * @date: 2021/2/4 15:40
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 功能描述:设置键的字符串值并返回其旧值
     *
     * @auther: zhouyu
     * @date: 2021/2/4 15:41
     */
    public Object getAndSet(String key, String value) {

        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 功能描述: 如果key已经存在并且是一个字符串，则该命令将该值追加到字符串的末尾。如果键不存在，则它被创建并设置为空字符串
     *
     * @auther: zhouyu
     * @date: 2021/2/4 15:42
     */
    public void append(String key, String value) {
        redisTemplate.opsForValue().append(key, value);
    }


    /**
     * 功能描述: 返回key所对应的value值得长度
     *
     * @auther: zhouyu
     * @date: 2021/2/4 15:43
     */
    public long size(String key) {
        return redisTemplate.opsForValue().size(key);
    }


    /**
     * 功能描述: 原子操作
     *
     * @auther: zhouyu
     * @date: 2021/2/4 15:48
     */
    public boolean setNx(String key, String value, Duration time) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, time);
    }


    /*----------------------------List数据操作---------------------------------*/

    /**
     * 功能描述: 获取当前KEY的长度 -1 为类型错误返回值
     *
     * @auther: zhouyu
     * @date: 2021/2/4 16:09
     */
    public long listSize(String key) {
        long size = 0;
        try {
            size = redisTemplate.opsForList().size(key);
        } catch (RedisSystemException e) {
            System.out.println("类型错误返回");
            size = -1;
        }
        return size;
    }

    /**
     * 功能描述: 将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从左边插入）
     *
     * @auther: zhouyu
     * @date: 2021/2/4 16:20
     */
    public long leftPush(String key, Object value) {

        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 功能描述: 批量把一个数组插入到列表中
     *
     * @auther: zhouyu
     * @date: 2021/2/4 16:23
     */
    public long leftPushAll(String key, Object... value) {

        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * 功能描述: 将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从右边插入）
     *
     * @auther: zhouyu
     * @date: 2021/2/4 16:38
     */
    public long rightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 功能描述: 将多个插入列表右边
     *
     * @auther: zhouyu
     * @date: 2021/2/4 16:39
     */
    public long rightPushAll(String key, Object... value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 功能描述: 在列表中index的位置设置value值
     *
     * @auther: zhouyu
     * @date: 2021/2/4 16:40
     */
    public void listSet(String key, Object value, long index) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 功能描述:
     * 从存储在键中的列表中删除等于值的元素的第一个计数事件。
     * 计数参数以下列方式影响操作：
     * count> 0：删除等于从头到尾移动的值的元素。
     * count <0：删除等于从尾到头移动的值的元素。
     * count = 0：删除等于value的所有元素。
     *
     * @auther: zhouyu
     * @date: 2021/2/4 16:43
     */
    public long remove(String key, Object value, long count) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * 功能描述: 根据下表获取列表中的值，下标是从0开始的
     *
     * @auther: zhouyu
     * @date: 2021/2/4 16:46
     */
    public Object getIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 功能描述:弹出最左边的元素，弹出之后该值在列表中将不复存在
     *
     * @auther: zhouyu
     * @date: 2021/2/4 16:47
     */
    public Object leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 功能描述: 弹出最右边的元素，弹出之后该值在列表中将不复存在
     *
     * @auther: zhouyu
     * @date: 2021/2/4 16:48
     */
    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }


    //----------------------Hash-----------------------------

    /**
     * 功能描述: 设置hash值
     *
     * @auther: zhouyu
     * @date: 2021/2/4 17:42
     */
    public void put(String key, Object hk, Object value) {
        redisTemplate.opsForHash().put(key, hk, value);
    }

    /**
     * 功能描述: 使用m中提供的多个散列字段设置到key对应的散列表中
     *
     * @auther: zhouyu
     * @date: 2021/2/5 13:45
     */
    public void putAll(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 功能描述: 返回hash的值
     *
     * @auther: zhouyu
     * @date: 2021/2/4 17:46
     */
    public Object get(String key, Object hk) {
        return redisTemplate.opsForHash().get(key, hk);
    }


    /**
     * 功能描述: 判断hsah是否存在
     *
     * @auther: zhouyu
     * @date: 2021/2/4 17:48
     */
    public boolean hasKey(String key, Object hk) {
        return redisTemplate.opsForHash().hasKey(key, hk);
    }

    /**
     * 功能描述: 删除hash
     *
     * @auther: zhouyu
     * @date: 2021/2/4 17:57
     */
    public Object deleteHash(String key, Object hk) {
        return redisTemplate.opsForHash().delete(key, hk);
    }

    /**
     * 功能描述:获取key所对应的散列表的key
     *
     * @auther: zhouyu
     * @date: 2021/2/5 13:42
     */
    public Set<Object> keys(Object hk) {
        return redisTemplate.opsForHash().keys(hk);
    }

    /**
     * 功能描述: 获取整个哈希存储的值根据密钥
     *
     * @auther: zhouyu
     * @date: 2021/2/5 13:46
     */
    public List<Object> values(Object hk) {
        return redisTemplate.opsForHash().values(hk);
    }


    /**
     * 功能描述: 获取整个哈希存储根据
     *
     * @auther: zhouyu
     * @date: 2021/2/5 13:48
     */
    public Map<String, Object> entries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 功能描述: 使用Cursor在key的hash中迭代，相当于迭代器
     *
     * @auther: zhouyu
     * @date: 2021/2/5 13:50
     */
    public Cursor<Map.Entry<String, Object>> scan(String key) {
        return redisTemplate.opsForHash().scan(key, ScanOptions.NONE);
    }


    /**
     * 功能描述:获取key所对应的散列表的大小个数
     *
     * @auther: zhouyu
     * @date: 2021/2/5 13:43
     */
    public long hashSize(Object hk) {
        return redisTemplate.opsForHash().size(hk);
    }


    //----------------------------SET数据结构----------------------------------------------

    /**
     * 功能描述: 无序集合中添加元素，返回添加个数
     *
     * @auther: zhouyu
     * @date: 2021/2/5 15:17
     */
    public long add(String key, String... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 功能描述: 无序集合中删除元素，返回添加个数
     *
     * @auther: zhouyu
     * @date: 2021/2/5 15:33
     */
    public long remove(String key, String... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }


    /**
     * 功能描述:移除并返回集合中的一个随机元素
     *
     * @auther: zhouyu
     * @date: 2021/2/5 15:58
     */
    public Object pop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 功能描述: 将 value 元素从 source 集合移动到 destination 集合
     *
     * @auther: zhouyu
     * @date: 2021/2/5 16:01
     */
    public boolean move(String source, Object value, String destination) {
        return redisTemplate.opsForSet().move(source, value, destination);
    }


    /**
     * 功能描述: 无序集合的大小长度
     *
     * @auther: zhouyu
     * @date: 2021/2/5 16:02
     */
    public long setSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }


    /**
     * 功能描述: 返回集合中的所有成员
     *
     * @auther: zhouyu
     * @date: 2021/2/5 16:03
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }


    /**
     * 功能描述: 遍历set
     *
     * @auther: zhouyu
     * @date: 2021/2/5 16:04
     */
    public Cursor<Object> setScan(String key, ScanOptions ops) {
        return redisTemplate.opsForSet().scan(key, ops);
    }

    //--------------------------ZSET数据类型-----------------------------------


}
