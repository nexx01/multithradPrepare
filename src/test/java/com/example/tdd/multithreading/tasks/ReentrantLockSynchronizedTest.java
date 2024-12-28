package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockSynchronizedTest {
    private static Integer THREAD_COUNT = 3;
    private static final int NUM_ITERATIONS = 5;


    @Test
    void name() {
        var lock = new ReentrantLock();
        SharedResource sharedResource = new SharedResource();

        var threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(new Worker(lock, sharedResource));
            threads[i].start();
        }

    }

    static class Worker implements Runnable {
        private Lock lock;
        private SharedResource sharedResource;

        public Worker(Lock lock, SharedResource sharedResource) {
            this.lock = lock;
            this.sharedResource = sharedResource;
        }

        @Override
        public void run() {
            for (int i = 0; i < NUM_ITERATIONS; i++) {
                lock.lock();
                try {
                    sharedResource.doWork();
                } finally {
                    lock.unlock();
                }
            }


        }
    }

    static class SharedResource {
        public void doWork() {
            var threadName = Thread.currentThread().getName();
            System.out.println("Thread " + threadName + " is running");
        }
    }

}


