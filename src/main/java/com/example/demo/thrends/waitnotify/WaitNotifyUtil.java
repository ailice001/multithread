package com.example.demo.thrends.waitnotify;

import java.util.concurrent.CountDownLatch;
import java.util.Random;

// 使用一个类 实现多线程的变量共享模型
public class WaitNotify extends Thread {

    private boolean isMainRound = true;

    private CountDownLatch clusterEnd = null;  //并发线程控制  计数器

    private final Object lock = new Object(); // 用于同步代码块

    private String uuid; //线程唯一值 也可以用ThreadId

    private int data; // 线程返回值


    WaitNotify(String uuid) {
        init();
        this.uuid = uuid;
    }

    public void init(){
         // 需要初始化的线程
        clusterEnd =   new CountDownLatch(1);
    }

    @Override   // 多线程中 需要执行的方法
    public void run() {
//        this.main_CountDownLatch(uuid);
        this.main(uuid);
    }

    // method use CountDownLatch
    public void main_CountDownLatch(String uuid) {

        // 当前计数器
        System.out.println("当前计数器 :"+clusterEnd.getCount());
         final int data = new Random().nextInt();

        this.data = data;

        ThreadLocalTestBean.setKey(uuid, String.valueOf(data));

        clusterEnd.countDown();// 线程完成 计数器减1
    }

    // method use CountDownLatch
    public String sub_CountDownLatch(String uuid) {
        String result ;
       if (clusterEnd.getCount() != 0) {
           try {
               clusterEnd.await(); // 阻塞线程 直到计数器为0
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
        result = ThreadLocalTestBean.getKey(uuid);
        return result;
    }

    public void main_SynchronizedCode(String uuid) {
        // 同步代码块方法  由于有执行时序的问题  所以一开始就获取锁
        synchronized(lock) {
            ThreadLocalTestBean.setKey(uuid, String.valueOf(data));
            try {
                Thread.sleep(new Random().nextInt(10)*100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isMainRound = false;
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 同步代码块实现
    public String sub_SynchronizedCode(String uuid) {
        String result ;
        synchronized (lock) { // 等 main释放锁 再执行
            result = ThreadLocalTestBean.getKey(uuid);
        }
        return result;
    }


    // 共享变量中多线程通信模型1 wait-notify 模式
    public synchronized void main(String uuid) {
        // 共享变量实现同步
        final int data = new Random().nextInt();
        this.data = data;
        System.out.println(String.format("%s , uuid %s , method : %s", Thread.currentThread().getName(), uuid, "main"));
        ThreadLocalTestBean.setKey(uuid, String.valueOf(data));
        this.notify();
    }

    // synchronized
    public synchronized String sub(String uuid) {
        // 实现线程通信
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String   result = ThreadLocalTestBean.getKey(uuid);
        System.out.println(String.format("sub Thread->%s : uuid -> %s  data -> %s ,this data %d ", Thread.currentThread().getName(), uuid, result, this.data));
        return result;

    }

}
