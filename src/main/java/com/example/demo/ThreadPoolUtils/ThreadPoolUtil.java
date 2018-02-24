package com.example.demo.ThreadPoolUtils;


import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

    //    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    private static ThreadPoolExecutor fixedThreadPool = null;// new ThreadPoolExecutor(4, 16, 5, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    public static void init(){
        fixedThreadPool = new ThreadPoolExecutor(20, 200, 5, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }
    public static void doExcute(Thread thread) {
        try {
            fixedThreadPool.execute(thread);

        } catch (Exception e) {
            System.out.println("Thread->" + thread.getName() + " 队列阻塞中！！！！！");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            doExcute(thread);
        }
    }

    public static void shutdown() {
        fixedThreadPool.shutdown();
    }

}
