package com.example.demo.thrends.waitnotify;

/**
 * Created by xuebinmeng on 2017/12/30.
 */
// 需求 主线程100次  再子线程10次  再主线程100次 再子线程10次  共50次如此循环
public class WaitNotifyDemo {

    public static void main(String[] args) {
        // 同一个类实现高类聚 逻辑好控制
        Bussiness buss = new Bussiness();

        // java 8 实现
        new Thread(()->{ for (int i = 0; i < 50; i++) { buss.main(i + 1); }}).start();
        new Thread(()->{ for (int i = 0; i < 50; i++) { buss.sub(i + 1); }}).start();

    }


}

// 实现线程高类聚   对与同一个业务的代码同步实现方案
class Bussiness {
    // 共享变量  也可用 ThreadLocal实现
    private boolean isMainRound = true;

    public synchronized void main(int i) {
        while(!isMainRound) {
            try {
                System.out.println(String.format("main is wait : %s", i));
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int j = 0; j < 100; j++) {
            System.out.println(String.format("main : %s ,  this is main sequence :  %s", i, j));
        }
        isMainRound = false;
        System.out.println("notify sub !");
        this.notify();
    }

    public synchronized void sub(int i) {
        // 实现线程通信
        while(isMainRound) {
            try {
                System.out.println(String.format("sub is wait : %s", i));
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int j = 0; j < 10; j++) {
            System.out.println(String.format("sub : %s ,  this is sub sequence :  %s", i, j));
        }
        isMainRound = true;
        System.out.println("notify main !");
        this.notify();
    }

}