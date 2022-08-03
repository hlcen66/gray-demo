package com.example.gateway.common;

public class MyThreadLocal {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();


    public static void set(String version){
        threadLocal.set(version);
    }

    public static String get(){
        return threadLocal.get();
    }
}
