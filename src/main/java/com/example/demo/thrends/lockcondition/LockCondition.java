package com.example.demo.thrends.lockcondition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * synchronized使用Object的同步监视器对象，用wait()、notify()、notifyAll()来协调线程的运行
 * 当使用LOCK对象保持同步时，JAVA为我们提供了Condition类来协调线程的运行。
 * 假如多线程读/写同一个缓冲区：当向缓冲区中写入数据之后，唤醒"读线程"；当从缓冲区读出数据之后，唤醒"写线程"；并且当缓冲区满的时候，"写线程"需要等待；当缓冲区为空时，"读线程"需要等待。
 * Lock 和 Condition 实现细粒度控制多线程的休眠与唤醒
 * 如果采用Object类中的wait(), notify(), notifyAll()实现该缓冲区，当向缓冲区写入数据之后需要唤醒"读线程"时，不可能通过notify()或notifyAll()明确的指定唤醒"读线程"，而只能通过notifyAll唤醒所有线程(但是notifyAll无法区分唤醒的线程是读线程，还是写线程)。  但是，通过Condition，就能明确的指定唤醒读线程
 * 需要注意的是ReetrantLock支持对同一线程重加锁，但是加锁多少次，就必须解锁多少次，这样才可以成功释放锁
 * */
public class LockCondition {

    public static void main(String[] args) {
        new NumberPrint().run();
    }

}

class NumberPrint implements Runnable {
    private int state = 1;
    private int n = 1;
    // 使用lock做锁
    private ReentrantLock lock = new ReentrantLock();
    // 获得lock锁的3个分支条件
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    @Override
    public void run() {
        new Thread(()->{
                for (int i = 0; i < 5; i++) {
                    try {
                        // 线程1获得lock锁后, 其他线程将无法进入需要lock锁的代码块.
                        // 在lock.lock()和lock.unlock()之间的代码相当于使用了synchronized(lock){}
                        lock.lock();
                        while (state != 1)
                            try {
                                // 线程1竞争到了lock, 但是发现state不为1, 说明此时还未轮到线程1打印.
                                // 因此线程1将在c1上wait
                                // 与解法一不同的是, 三个线程并非在同一个对象上wait, 也不由同一个对象唤醒
                                c1.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        // 如果线程1竞争到了lock, 也通过了state判定, 将执行打印任务
                        for (int j = 0; j < 5; j++) {
                            System.out.println(Thread.currentThread().getName()
                                    + ": " + n++);
                        }
                        System.out.println();
                        // 打印完成后将state赋值为2, 表示下一次的打印任务将由线程2执行
                        state = 2;
                        // 唤醒在c2分支上wait的线程2
                        c2.signal();
                    } finally {
                        // 打印任务执行完成后需要确保锁被释放, 因此将释放锁的代码放在finally中
                        lock.unlock();
                    }
                }
            }, "线程1").start();

        new Thread(()->{
                for (int i = 0; i < 5; i++) {
                    try {
                        lock.lock();
                        while (state != 2)
                            try {
                                c2.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        for (int j = 0; j < 5; j++) {
                            System.out.println(Thread.currentThread().getName()
                                    + ": " + n++);
                        }
                        System.out.println();
                        state = 3;
                        c3.signal();
                    } finally {
                        lock.unlock();
                    }
                }
            }, "线程2").start();

        new Thread(()->{
                for (int i = 0; i < 5; i++) {
                    try {

                        lock.lock();
                        while (state != 3)
                            try {
                                c3.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        for (int j = 0; j < 5; j++) {
                            System.out.println(Thread.currentThread().getName()
                                    + ": " + n++);
                        }
                        System.out.println();
                        state = 1;
                        c1.signal();
                    } finally {
                        lock.unlock();
                    }
                }
            }, "线程3").start();
    }

}