package com.example.demo.thrends.waitnotify;

import java.util.concurrent.ConcurrentHashMap;

public class SharedConcurrentHashMap {
    private static ConcurrentHashMap<String,String> key = new ConcurrentHashMap<>();

    public synchronized static String getKey(String uuid) {
        String result = key.get(uuid);
        removeKey(uuid);
        return result;
    }

    public static void setKey(String uuid,String value) {
        key.put(uuid,value);
    }

   private static void removeKey(String uuid){
        key.remove(uuid);
    }
}
