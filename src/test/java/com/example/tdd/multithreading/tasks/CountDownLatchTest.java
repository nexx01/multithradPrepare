package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
    private static final int NUM_THREADS = 5;
    private static final CountDownLatch startLatch = new CountDownLatch(1);
    private static final CountDownLatch finishLatch = new CountDownLatch(NUM_THREADS);

    @Test
    void name() {
        var threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(new Worker());
            threads[i].start();
        }
        //Allow threads start simultaneously
        startLatch.countDown();

        try {
            finishLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            try {
                startLatch.await();
                System.out.println("Thread " + Thread.currentThread().getName() + " started");
            }catch (InterruptedException e) {

            } finally {
                finishLatch.countDown();
            }
        }
    }
}
