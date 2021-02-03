package com.zzyy.zookeeperdemo.zy;

/**
 * @Auther: zhouyu
 * @Date: 2021/2/2 09:49
 * @Description:
 */
public class TestLockApi {

    public static void main(String[] args) {


        //模拟50个线程抢锁
        for (int i = 0; i < 50; i++) {
            new Thread(new TestLockApi.TestThread(i, new DistributedLock())).start();
        }


    }


    static class TestThread implements Runnable {
        private Integer threadFlag;
        private DistributedLock lock;

        public TestThread(Integer threadFlag, DistributedLock lock) {
            this.threadFlag = threadFlag;
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "-线程获取到了锁");
                //等到1秒后释放锁
//                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    lock.unlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
