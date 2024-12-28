package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class MyCountDownLatchTest {

    private final static int NUM_THREADS = 50;
    private final static CountDownLatch startLatch = new CountDownLatch(1);
    private final static CountDownLatch doneLatch = new CountDownLatch(NUM_THREADS);

    @Test
    void name() {
        Stream.generate(() -> new Thread(new Worker()))
                .limit(NUM_THREADS)
                .forEach(Thread::start);

        startLatch.countDown();

        try {
            doneLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static final class Worker implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("Thread " + Thread.currentThread().getName() + " started");
                startLatch.await();
                System.out.println("Thread " + Thread.currentThread().getName() + " after await");

            } catch (InterruptedException e) {

            }finally {
                doneLatch.countDown();
            }

        }
    }
}
