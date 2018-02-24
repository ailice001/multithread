package com.example.demo.Tests;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class Tests {
    @Test
    public void test(){
        for (int i = 0; i < 20 ; i++) {
            // java 8 lambda method use
            Thread thread=new Thread(()-> System.out.println((new Random().nextInt(30)*100)));
            thread.start();
        }
    }
}
