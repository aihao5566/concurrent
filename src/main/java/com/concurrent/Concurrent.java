package com.concurrent;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.ArrayUtil;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Concurrent {
    private ReentrantLock reentrantLock = new ReentrantLock();

    public Concurrent() {
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable() {
            int x = 1;

            @Override
            public void run() {
                try {
                    Thread t1 = Thread.currentThread();
                    System.out.println("t1.getStackTrace() " + ArrayUtil.toString(t1.getStackTrace()));
                    System.out.println(DateUtil.now() + " " + String.format(t1.getName(), this.x));
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

            }
        };
        // 线程池
        ThreadFactory namedThreadFactory = (new ThreadFactoryBuilder()).setNamePrefix("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(
                1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(runnable);
        singleThreadPool.shutdown();

        Thread t1 = new Thread(runnable, "t1");
        t1.start();
        t1.join();
        Thread main = Thread.currentThread();
        System.out.println("main " + main);
        System.out.println(DateUtil.now() + " main");
    }
}
