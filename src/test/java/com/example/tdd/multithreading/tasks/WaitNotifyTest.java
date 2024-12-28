package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WaitNotifyTest {
    final static Queue<Integer>  queue = new LinkedList<>();
    int QUEUE_CAPACITY = 5;

    @Test
    void name() throws InterruptedException {
        System.out.println("================================================================");

        Runnable produceTask = () -> {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == QUEUE_CAPACITY) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("produce " + queue.add(1));
                    System.out.println(queue.size());
                    queue.notify();
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };

        Runnable consumeTask = () -> {
            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("consume " + queue.poll());
                    queue.notify();
                }


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        Thread producerThread = new Thread(produceTask);
        Thread consumerThread = new Thread(consumeTask);

        producerThread.start();
        consumerThread.start();
        producerThread.join();
        consumerThread.join();
    }
}
