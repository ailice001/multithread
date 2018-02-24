package com.example.demo.thrends.waitnotify;

import com.example.demo.ThreadPoolUtils.ThreadPoolUtil;

import java.util.UUID;

/**
 * Created by xuebinmeng on 2017/12/30.
 */
public class WaitNotifyTest {

    public static String getUUID() {
        //去掉“-”符号
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {

        ThreadPoolUtil.init();

        for (int i = 0; i < 1000; i++) {
            final int k = i;
            new Thread(() -> {
                long start1 = System.currentTimeMillis();
                String uuid = getUUID();
                System.out.println(String.format("master Thread->%s : uuid -> %s ", Thread.currentThread().getName(), uuid));

                // 业务控制类
                WaitNotifyUtil waitNotifyUtil = new WaitNotifyUtil(uuid);

                ThreadPoolUtil.doExcute(waitNotifyUtil);

                String getData = waitNotifyUtil.sub(uuid);

                System.out.println(String.format("this is %d , use time %s ,%s ", k, (System.currentTimeMillis() - start1),getData));
            }).start();

        }

    }

}
