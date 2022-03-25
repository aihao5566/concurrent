package com.concurrent;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.ArrayUtil;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Concurrent {
    private ReentrantLock reentrantLock = new ReentrantLock();

    public Concurrent() {
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread t1 = Thread.currentThread();
                    System.out.println(DateUtil.now() + " " + t1.getName());
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

            }
        };
        // 线程池
        ThreadFactory namedThreadFactory = (new ThreadFactoryBuilder()).setNamePrefix("demo-pool-").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(
                2, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(10), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        // 15个任务 每个任务耗时1秒 线程池参数 2,5,10 总耗时->3秒
        for (int i = 0; i < 15; i++) {
            singleThreadPool.execute(runnable);
        }
        singleThreadPool.shutdown();

        // Thread t1 = new Thread(runnable, "t1");
        // t1.start();
        // t1.join();
        // Thread main = Thread.currentThread();
        // System.out.println("main " + main);
        // System.out.println(DateUtil.now() + " main");
    }
}
