package com.example.demo.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xuebinmeng on 2017/12/30.
 */

public class TimerTaskTest {
    private static int count = 0 ;
    static class MyTimerTask extends TimerTask {

        public void run() {
            count = (count +1 )%2;
            System.out.println("this is timer body!");
            new Timer().schedule(new MyTimerTask(),2000+2000*count);// 用完后再新建   // 奇数 2秒  偶数 4 秒
        }
    }

    public static void main(String[]args){


        // 定时器的使用
      /*  new Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                System.out.println("this is timer body!");
                new Timer().schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("this is timer body!");
                    }
                },2000);
            }
        },2000*//*,1000*//*);  // 第一个为延迟  第二个为每隔多少秒
*/
        //定时器启动
        new Timer().schedule(new MyTimerTask(),2000);

        //
        while (true){
            System.out.println("time:"+new Date().getSeconds());
            try {
                Thread.sleep(1000);  //线程中断
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
