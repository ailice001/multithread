package com.example.demo.thrends.join;

import java.util.ArrayList;
import java.util.List;

public class ClassTest extends Thread {

    public ClassTest(String name) {
        this.setName(name);
    }

    @Override
    public void run() {
        System.out.println(this.getName() + " starting...");

        for (int i = 0; i < 50; i++) {
            System.out.println(this.getName() + " count to " + i);
        }

        System.out.println(this.getName() + " end...");
    }

    public static void main(String[] args) {
        System.out.println("main thread starting...");

        List<ClassTest> list = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            ClassTest my = new ClassTest("Thrad " + i);
            my.start();
            list.add(my);
        }

        try {
            for (ClassTest my : list) {
                my.join(); //主线程等待所有的子线程结束
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("main thread end...");

    }
}
