package com.example.googleplay.manager;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by LIANGSE on 2016/5/7.
 */
public class ThreadManager {

    private static ThreadManager manager;

    public static ThreadManager instance() {

        synchronized (ThreadManager.class) {
            if (manager == null) {
                manager = new ThreadManager();
            }
            return manager;
        }
    }

    private ThreadPoolProxy longPool;

    public  ThreadPoolProxy createLongPool() {
        if (longPool == null) {
            longPool = new ThreadPoolProxy(5, 5, 5000L);
        }
        return longPool;
    }

    private ThreadPoolProxy shortPool;

    public  ThreadPoolProxy createShortPool() {
        if (shortPool == null) {
            shortPool = new ThreadPoolProxy(5, 5, 3000L);
        }
        return shortPool;
    }

    public class ThreadPoolProxy {

        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private ThreadPoolExecutor pool;

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.keepAliveTime = keepAliveTime;
            this.maximumPoolSize = maximumPoolSize;
        }

        /**
         * 执行一个任务
         *
         * @param runnable
         */
        public void execute(Runnable runnable) {
            if (pool == null) {
                /**
                 * (int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue)
                 * corePoolSize 线程池默认开启的线程数量
                 * maximumPoolSize 当任务大于线程的数量时,额外开启的线程数
                 * keepAliveTime 当线程池线程数大于corePoolSize时,并且线程处于空闲时间,线程销毁的休眠时间
                 * TimeUnit unit 是keepAliveTime的单位
                 *  BlockingQueue<Runnable> workQueue 当所有线程数出去活动状态,则新加入的任务将会进入到此队列中,进行任务排队
                 */

                pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(10));
            }
            pool.execute(runnable);
        }

        /**
         * 取消一个任务
         *
         * @param runnable
         */
        public void cancel(Runnable runnable) {
            if (pool != null && !pool.isShutdown() && !pool.isTerminating()) {
                pool.remove(runnable);
            }
        }
    }

}
