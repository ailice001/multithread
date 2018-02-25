package com.example.demo.thrends.volatile_test;

/**
 * JMM决定一个线程对共享变量的写入何时对另一个线程可见
 * volatile修饰的变量值直接存在main memory里面，子线程对该变量的读写直接写入main memory  多个线程一个变量信息 拥有的是即全局共享变量
 * volatile能保证所修饰的变量对于多个线程可见性，即只要被修改，其它线程读到的一定是最新的值。
 *
 **/
public class VolatileDemo{

    public static void main(String[] args) {

        MultiThreadingExample volatileExample = new MultiThreadingExample();

        Thread t1 = new Thread(volatileExample, "T1");
        Thread t2 = new Thread(volatileExample, "T2");

        t1.start();
        t2.start();
    }
}
class MultiThreadingExample implements Runnable {

    private volatile int testValue;

    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                System.out.println(Thread.currentThread().getName() + ": " + i);

                if (Thread.currentThread().getName().equalsIgnoreCase("T1")) {
                    testValue = 10+i;
                }
                if (Thread.currentThread().getName().equalsIgnoreCase("T2")) {
                    System.out.println("Test value: " + testValue);
                }

                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
