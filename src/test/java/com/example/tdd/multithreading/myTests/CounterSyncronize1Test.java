package com.example.tdd.multithreading.myTests;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;

public class CounterSyncronize1Test {

    private static int counter = 0;
    private static volatile int volatileCounter = 0;

    @SneakyThrows
    @Test
    void shouldCountSuccess() throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                counter++;
            }
        };

        var threads = new ArrayList<Thread>();
        for (int i=0; i<1000; i++) {
            Thread thread=new Thread(task);
           thread.start();
            threads.add(thread);

        }

        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).join();
        }

        assertEquals(1000000, counter);
    }

    @SneakyThrows
    @Test
    void setVolatileCounter() throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                volatileCounter++;
            }
        };

        var threads = new ArrayList<Thread>();
        for (int i=0; i<1000; i++) {
            Thread thread=new Thread(task);
            thread.start();
            threads.add(thread);
        }

        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).join();
        }

        assertEquals(1000000, volatileCounter);
    }

    @SneakyThrows
    @Test
    void setVolatileCounterSync() throws InterruptedException {
        Runnable task = () -> {
            synchronized (CounterSyncronize1Test.class) {
                for (int i = 0; i < 1000; i++) {
                    volatileCounter++;
                }
            }
        };

        var threads = new ArrayList<Thread>();
        for (int i=0; i<1000; i++) {
            Thread thread=new Thread(task);
            thread.start();
            threads.add(thread);

        }

        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).join();
        }

        Assertions.assertEquals(1000000, volatileCounter);
    }


    @SneakyThrows
    @Test
    void setVolatileCounterWithLock() throws InterruptedException {
        var lock = new ReentrantLock();
        Runnable task = () -> {
            try{
                lock.lock();
                for (int i = 0; i < 1000; i++) {
                    volatileCounter++;
                }
            } finally{
                lock.unlock();
            }
        };

        var threads = new ArrayList<Thread>();
        for (int i=0; i<10000; i++) {
            Thread thread=new Thread(task);
            thread.start();
            threads.add(thread);

        }

        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).join();
        }

        Assertions.assertEquals(10000000, volatileCounter);
    }

    @SneakyThrows
    @Test
    void setVolatileCounterWithLockCondition() throws InterruptedException {
        var lock = new ReentrantLock();

        Runnable task = () -> {
            while (lock.isLocked()){
                System.out.println(Thread.currentThread().getName());
            }
            try{
                lock.lock();
                for (int i = 0; i < 1000; i++) {
                    try {
                        Thread.sleep(10);
                    }catch (InterruptedException e) {}
                    volatileCounter++;
                }
            } finally{
                lock.unlock();
            }
        };

        var threads = new ArrayList<Thread>();
        for (int i=0; i<10000; i++) {
            Thread thread=new Thread(task);
            thread.start();
            threads.add(thread);

        }

        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).join();
        }

        Assertions.assertEquals(10000000, volatileCounter);
    }
}
