package com.example.demo.thrends.atomicinteger;

/**
 * AtomicInteger是一个提供原子操作的Integer类，通过线程安全的方式操作加减。
 * AtomicInteger提供原子操作来进行Integer的使用，因此十分适合高并发情况下的使用。
 **/

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 业务
 **/
public class AtomicIntegerDemo {




    //count
    static class Counter {
        private static AtomicInteger num = new AtomicInteger(0);
        //使用CountDownLatch来等待计算线程执行完
        static CountDownLatch countDownLatch = new CountDownLatch(30);


        public static void main(String[] args) throws InterruptedException {
            //开启30个线程进行累加操作
            for (int i = 0; i < 30; i++) {
                new Thread() {
                    public void run() {
                        for (int j = 0; j < 10000; j++) {
                            num.incrementAndGet();//原子性的num++,通过循环CAS方式
                        }
                        countDownLatch.countDown();
                    }
                }.start();
            }
            //等待计算线程执行完
            countDownLatch.await();
            System.out.println(num);
        }
    }
}
