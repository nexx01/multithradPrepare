package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;

public class SemaphoreExercise {

    private static final int NUM_THREADS = 5;
    private static final int MAX_PERMITS = 3;

    @Test
    void name() {
        Semaphore semaphore = new Semaphore(MAX_PERMITS);
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(new Worker(semaphore));
            threads[i].start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static class Worker implements Runnable {
        private Semaphore semaphore;

        public Worker(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("Thread " + Thread.currentThread().getName() + " acquired the semaphore.");
                Thread.sleep(2000);
                System.out.println("Thread " + Thread.currentThread().getName()  + " released the semaphore.");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
