package com.example.demo.thrends.waitnotify;

import java.util.Random;

/**
 * Created by xuebinmeng on 2017/12/30.
 */
// 使用ThreadLocal 实现线程变量共享
public class ThreadLocalTestDemo {
//    private static ThreadLocal<ThreadDataMap> threadLocal = new ThreadLocal<>();
    private static ThreadLocal<Integer> x = new ThreadLocal<> ();

    public static void main(String[]args){

        for (int i = 0; i < 3 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int data = new Random().nextInt();
                    System.out.println(Thread.currentThread().getName()+" put data "+data);
                    x.set(data);
                    //   实现方法 1    生产数据
//                    ThreadDataMap threadDataMap = new ThreadDataMap("name-"+data,data);
//                    threadLocal.set(threadDataMap);

                    // ThreadDataMap 的 ThreadLocal存在于这个线程中
                    ThreadDataMap.getinstance().setAge(data);
                    ThreadDataMap.getinstance().setName("name"+data);
//                    ThreadDataMap
                    //消费数据
                    new A().get();
                    new B().get();
                }
            }).start();
        }

    }


    static class A{

        public void get(){
//            int data = x.get();
            // 实现方法1
//            ThreadDataMap threadDataMap = threadLocal.get();
            //实现方法2  更优雅
            ThreadDataMap threadDataMap = ThreadDataMap.getinstance();
            System.out.println("class A "+" from "+Thread.currentThread().getName()/*+" get data "+data*/);
            System.out.println("class A "+" from "+Thread.currentThread().getName()+" get DataMap"+threadDataMap.toString());
        }


    }
    static class B{
        public void get(){
            int data = x.get();
            // 实现方法1
//            ThreadDataMap threadDataMap = threadLocal.get();
            //实现方法2  更优雅
            ThreadDataMap threadDataMap = ThreadDataMap.getinstance();
            System.out.println(/*this.getClass().getName()*/"class B "+" from "+Thread.currentThread().getName()+" get data "+data);
            System.out.println("class B "+" from "+Thread.currentThread().getName()+" get DataMap"+threadDataMap.toString());
        }
    }

    static class ThreadDataMap{
        private ThreadDataMap(){}

        private static /*synchronized*/ ThreadDataMap getinstance(){
            ThreadDataMap instance = map.get();         // 对于新建实例 不同线程获取的对象不同
            if (instance == null){
                instance = new ThreadDataMap();
                map.set(instance);
            }
            return instance;
        }

        private static ThreadLocal<ThreadDataMap> map = new ThreadLocal<>();

        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public ThreadDataMap(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "ThreadDataMap {" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

       /* public static void  get(){
            int data = x.get();
            // 实现方法1
//            ThreadDataMap threadDataMap = threadLocal.get();
            //实现方法2  更优雅
            ThreadDataMap threadDataMap = ThreadDataMap.getinstance();
            System.out.println(*//*this.getClass().getName()*//*"ThreadLocal get"+" from "+Thread.currentThread().getName()+" get data "+data);
            System.out.println("ThreadLocal get "+" from "+Thread.currentThread().getName()+" get DataMap"+threadDataMap.toString());
        }*/
    }
}
