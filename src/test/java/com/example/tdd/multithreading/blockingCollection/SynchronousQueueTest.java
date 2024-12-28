package com.example.tdd.multithreading.blockingCollection;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;

public class SynchronousQueueTest {

    @Test
    void name() throws InterruptedException {
        var synchronousQueue = new SynchronousQueue<String>();
//        var synchronousQueue = new ArrayBlockingQueue<String>(10);
        Runnable producer = () -> {
            int i = 0;
            while (true) try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1000));
                var value = String.valueOf(i++);
                synchronousQueue.put(value);
                System.out.println("Added: " + value);
            } catch (InterruptedException ignored) {

            }
        };

        Runnable consumer = () -> {
            while (true) try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(500, 2000));
                System.out.println("Read: " + synchronousQueue.take());
            } catch (InterruptedException ignored) {

            }
        };

        var thread1 = new Thread(producer);
        thread1.start();
        var thread = new Thread(consumer);
        thread.start();
        thread.join();
        thread1.join();

    }
}
