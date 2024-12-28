package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Stream;

public class MyCyclicBarierTest {

    private final static int NUM_THREADS = 5;
    private final static CyclicBarrier cb = new CyclicBarrier(NUM_THREADS, new BarrierAction());

    @Test
    void name() throws InterruptedException {

        Stream.generate(()->new Thread(new Worker()))
                .limit(NUM_THREADS)
                .peek(Thread::start)
                .toList()
                .forEach(thread -> {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });


        Stream.generate(()->new Thread(new Worker()))
                .limit(NUM_THREADS)
                .peek(Thread::start)
                .toList()
                .forEach(thread -> {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            try{
                System.out.println("Thread " + Thread.currentThread().getName() + " has reached the barrier");
                System.out.println("cb.getNumberWaiting() " +cb.getNumberWaiting() +"  " + cb.getParties() + " parties in total   - "  + cb.isBroken()  + " barrier is broken"  );
                cb.await();

                System.out.println("Thread " + Thread.currentThread().getName() + " has passed the barrier");

            }catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    static class BarrierAction implements Runnable {
        @Override
        public void run() {
            System.out.println("=".repeat(10)+"\n");
            System.out.println("=".repeat(10)+"\n");
            System.out.println("=".repeat(10)+"\n");
            System.out.println("=".repeat(10)+"\n");
            System.out.println("=".repeat(10)+"\n");
            System.out.println("=".repeat(10)+"\n");
            System.out.println("=".repeat(10)+"\n");
            System.out.println("=".repeat(10)+"\n");
            System.out.println("=".repeat(10)+"\n");
        }
    }


}
