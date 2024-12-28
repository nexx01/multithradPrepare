package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurentHashMapTest {

    @Test
    void name() throws InterruptedException {
        var map = new ConcurrentHashMap<String, Integer>();

        var writer1 = new Thread(new Writer(map, "Thread-1", 1));
        var writer2 = new Thread(new Writer(map, "Thread-2", 2));
        writer2.start();
        writer1.start();

        var reader1 = new Thread(new Reader(map, "Thread-1"));
        var reader2 = new Thread(new Reader(map, "Thread-2"));
        reader2.start();
        reader1.start();

        reader1.join(); reader2.join();
        writer1.join();writer2.join();
    }

    static class Writer implements Runnable {
        private ConcurrentHashMap<String, Integer> map;
        private String threadName;
        private int value;

        public Writer(ConcurrentHashMap<String, Integer> map, String threadName, int value) {
            this.map = map;
            this.threadName = threadName;
            this.value = value;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                map.put(threadName, value);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Reader implements Runnable {
        private ConcurrentHashMap<String, Integer> map;
        private String threadName;

        public Reader(ConcurrentHashMap<String, Integer> map, String threadName) {
            this.map = map;
            this.threadName = threadName;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                var value = map.get(threadName);
                System.out.println("Thread " + threadName + " read value: " + value);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
