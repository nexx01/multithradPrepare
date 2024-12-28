package com.example.tdd.multithreading.tasks;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerWaitNotifyTest {

    private static final int BUFFER_SIZE = 5;
    private static final Queue<Integer> buffer = new LinkedList<>();

    @Test
    void test() throws InterruptedException {
        Thread producerThread = new Thread(new Producer());
        Thread consumerThread = new Thread(new Consumer());

        producerThread.start();
        consumerThread.start();

        Thread.sleep(50000);
//        producerThread.join();
//        consumerThread.join();
    }

  @Data
    static class Producer implements Runnable {

        @Override
        public void run() {
            int value = 0;
            while (true) {

                synchronized (buffer) {
                    while (buffer.size() == BUFFER_SIZE) {

                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                        System.out.println("Producer produced: " + value);
                        buffer.add(value++);

                        //Notify the consumer that an item is produced
                        buffer.notify();

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                }
            }
        }
    }


    static class Consumer implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (buffer) {
                    while (buffer.isEmpty()) {
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                        int value = buffer.poll();
                        System.out.println("Consumer consumed: " + value);
                        //Notify the producer that an item is consumed
                        buffer.notify();

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                }
            }

        }
    }

}