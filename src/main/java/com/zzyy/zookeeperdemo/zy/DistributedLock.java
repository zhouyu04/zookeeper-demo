package com.zzyy.zookeeperdemo.zy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Auther: zhouyu
 * @Date: 2021/2/1 18:03
 * @Description:
 */
public class DistributedLock implements Lock, Watcher {

    private static Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    /**
     * 定义根节点
     */
    private String ROOT_LOCK = "/locks";

    /**
     * 表示等待前一个锁
     */
    private String WAIT_LOCK;

    /**
     * 表示当前锁
     */
    private String CURRENT_LOCK;

    /**
     * 主要用作控制
     */
    private CountDownLatch countDownLatch;


    ZooKeeper zk_client;

    public DistributedLock() {
        try {
            zk_client = new ZooKeeper("121.4.27.171:2181", 4000, this);
            //为false就不去注册当前的事件
            Stat stat = zk_client.exists(ROOT_LOCK, false);
            //判断当前根节点是否存在
            if (stat == null) {
                //创建持久化节点
                zk_client.create(ROOT_LOCK, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            logger.error("初始化分布式锁异常！！", e);
        }
    }

    @Override
    public void lock() {

        if (tryLock()) {
            logger.info(Thread.currentThread().getName() + "-->" + CURRENT_LOCK + "|获得锁成功！恭喜！");
            System.out.println(Thread.currentThread().getName() + "-->" + CURRENT_LOCK + "|获得锁成功！恭喜！");
            return;
        }
        //如果没有获得锁，那么就继续监听，等待获得锁
        try {
            waitForLock(WAIT_LOCK);
        } catch (Exception e) {
            logger.error("异常", e);
        }

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {

        try {
            //创建临时有序节点（节点会自动递增）-当前锁
            CURRENT_LOCK = zk_client.create(ROOT_LOCK + "/", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.info(Thread.currentThread().getName() + "-->" + CURRENT_LOCK + "|尝试竞争锁！");
            System.out.println(Thread.currentThread().getName() + "-->" + CURRENT_LOCK + "|尝试竞争锁！");

            //获取根节点下所有的子节点，不注册监听
            List<String> children = zk_client.getChildren(ROOT_LOCK, false);
            //排序
            SortedSet<String> sortedSet = new TreeSet<>();
            children.forEach(child -> {
                sortedSet.add(ROOT_LOCK + "/" + child);
            });

            //获取当前子节点中最小的节点
            String firstNode = sortedSet.first();
            if (StringUtils.equals(firstNode, CURRENT_LOCK)) {
                //将当前节点和最小节点进行比较，如果相等，则获得锁成功
                return true;
            }


            //获取当前节点中所有比自己更小的节点
            SortedSet<String> lessThenMe = sortedSet.headSet(CURRENT_LOCK);
            //如果当前所有节点中有比自己更小的节点
            if (CollectionUtils.isNotEmpty(lessThenMe)) {
                //获取比自己小的节点中的最后一个节点，设置为等待锁
                WAIT_LOCK = lessThenMe.last();
            }
            System.out.println("当前最小节点:" + firstNode + ",当前节点:" + CURRENT_LOCK + ",当前等待节点:" + WAIT_LOCK);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        logger.info(Thread.currentThread().getName() + "-->释放锁 " + CURRENT_LOCK);
        System.out.println(Thread.currentThread().getName() + "-->释放锁 " + CURRENT_LOCK);
        try {
            //-1强制删除
            zk_client.delete(CURRENT_LOCK, -1);
            CURRENT_LOCK = null;
            zk_client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        if (this.countDownLatch != null) {
            System.out.println("返回监听:" + watchedEvent.getPath() + "_" + watchedEvent.getState()+"_"+watchedEvent.getType());
            logger.info("返回监听:" + watchedEvent.getPath() + "_" + watchedEvent.getState());
            //如果不为null说明存在这样的监听
            this.countDownLatch.countDown();
        }
    }


    /**
     * 持续阻塞获得锁的过程
     *
     * @param prev 当前节点的前一个等待节点
     * @return
     */
    private boolean waitForLock(String prev) throws KeeperException, InterruptedException {
        //等待锁需要监听上一个节点，设置Watcher为true，即每一个有序节点都去监听它的上一个节点
        Stat stat = zk_client.exists(prev, true);
        if (stat != null) {
            //即如果上一个节点依然存在的话
            System.out.println(Thread.currentThread().getName() + "-->等待锁 " + ROOT_LOCK + "/" + prev + "释放。");
            logger.info(Thread.currentThread().getName() + "-->等待锁 " + ROOT_LOCK + "/" + prev + "释放。");
            countDownLatch = new CountDownLatch(1);
            countDownLatch.await();
            System.out.println(Thread.currentThread().getName() + "-->" + "等待后获得锁成功！");
            logger.info(Thread.currentThread().getName() + "-->" + "等待后获得锁成功！");
        }
        return true;
    }

}
