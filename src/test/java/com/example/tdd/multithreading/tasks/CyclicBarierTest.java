package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarierTest {
    private final static int NUM_THREADS = 5;
    private final static CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_THREADS);

    @Test
    void name() throws InterruptedException {

        var threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(new Worker());
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("Thread " + Thread.currentThread().getName() + " is waiting for the barrier");
                cyclicBarrier.await();
                System.out.println("Thread " + Thread.currentThread().getName() + " has passed the barrier");
            }catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class BarrierAction implements Runnable {
        @Override
        public void run() {
            System.out.println("Barrier action performed by thread " + Thread.currentThread().getName() + " at " + System.currentTimeMillis() + " ms");
        }
    }
}
