package com.concurrent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer {

    /**
     * LinkedList实现了Deque接口，Deque接口实现了Queue接口
     */
    private BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(2);


    class Producer implements Runnable{

        @Override
        public void run() {
            while (true){
                int i = new Random().nextInt(100) + 10;
                try {
                    queue.put(i);
                    System.out.println("生产者生产一条任务" + i + "，当前队列长度为" + queue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Consumer implements Runnable{

        @Override
        public void run() {
            while (true){
                try {
                    Integer take = queue.take();
                    System.out.println("消费者消费一条任务" + take + "，当前队列长度为" + queue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 使用 Lock 锁实现
     */
    private final int MAX_LEN = 2;
    private Queue<Integer> queue1 = new LinkedList<Integer>();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    class Producer1 extends Thread {
        @Override
        public void run() {
            producer();
        }
        private void producer() {
            while(true) {
                lock.lock();
                try {
                    while (queue1.size() == MAX_LEN) {
                        System.out.println("当前队列满");
                        try {
                            condition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue1.add(1);
                    condition.signal();
                    System.out.println("生产者生产一条任务，当前队列长度为" + queue1.size());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    class Consumer1 extends Thread {
        @Override
        public void run() {
            consumer();
        }
        private void consumer() {
            while (true) {
                lock.lock();
                try {
                    while (queue1.size() == 0) {
                        System.out.println("当前队列为空");
                        try {
                            condition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue1.poll();
                    condition.signal();
                    System.out.println("消费者消费一条任务，当前队列长度为" + queue1.size());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }
    public static void main(String[] args) {
        ProducerConsumer producerConsumer = new ProducerConsumer();
        Producer1 producer1 = producerConsumer.new Producer1();
        Consumer1 consumer1 = producerConsumer.new Consumer1();
        new Thread(producer1).start();
        new Thread(consumer1).start();


    }



}
