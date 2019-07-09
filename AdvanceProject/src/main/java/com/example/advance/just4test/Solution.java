package com.example.advance.just4test;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Solution {

    public static ThreadLocal<Integer> threadLocalIds = new ThreadLocal<>();
    public static int userId;

    public static void testExecutor() {
//        ExecutorService executorService = Executors.newSingleThreadExecutor(); // 单线程
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        sleep(1);
                        System.out.println("Executor1: " + i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        sleep(1);
                        System.out.println("Executor2: " + i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        executorService.shutdown();
    }

    public static void testThreadLocal() {
        for (int i = 0; i < 10; i++) {
            final int finalId = i;
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        threadLocalIds.set(finalId);
                        userId = finalId;
                        sleep(1);
                        System.out.println("ThreadLocal:" + threadLocalIds.get());
                        System.out.println("haha" + userId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static int count = 0;
    public static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void testWithoutAntomic() {
        for (int i = 0; i < 10; i++) {   // 开10个线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sleep(1000);
                        for (int i = 0; i < 10; i++) {
                            count++;
                            System.out.println(count);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    public static void testAntomic() {
        for (int i = 0; i < 10; i++) {   // 开10个线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sleep(1000);
                        for (int i = 0; i < 10; i++) {
                            System.out.println(atomicInteger.incrementAndGet());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    public static void testFuture() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sleep(1000);
                return 1;
            }
        });
        service.shutdown();
        try {
            System.out.println(future.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        testThreadLocal();
//        testExecutor();
//        testWithoutAntomic();
//        testAntomic();
        testFuture();
    }
}
