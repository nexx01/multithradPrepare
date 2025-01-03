package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

public class ConcurrentLinkedQueueTest {
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(4);

    @Test
    void name() throws InterruptedException {
        var queue = new ConcurrentLinkedQueue<String>();

        var pr1 = new Thread(new Producer(queue, "Producer-1"));
        var pr2 = new Thread(new Producer(queue, "Producer-2"));
        pr1.start();
        pr2.start();
        var co1 = new Thread(new Consumer(queue, "Consumer-1"));
        var co2 = new Thread(new Consumer(queue, "Consumer-2"));
        co2.start();co1.start();
        pr1.join();pr2.join();co1.join();co2.join();
    }

    static class Producer implements Runnable {

        private final ConcurrentLinkedQueue<String> queue;
        private final String name;
        private int counter=0;

        public Producer(ConcurrentLinkedQueue<String> queue, String name) {
            this.queue = queue;
            this.name = name;
            this.counter = 0;
        }

        @Override
        public void run() {
            while(true){
                try {
//                    cyclicBarrier.await();
                    String item = name + "-item-" + counter++;
                    queue.offer(item);
                    System.out.println("Producer " + name + " produced: " + item);

                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class Consumer implements Runnable{
        private final ConcurrentLinkedQueue<String> queue;
        private final String name;

        public Consumer(ConcurrentLinkedQueue<String> queue, String name){
            this.queue = queue;
            this.name = name;

        }

        @Override
        public void run(){
            while(true){
                try {
                    //cyclicBarrier.await();

                    String item =queue.poll();
                    if(item!=null){
                        System.out.println("Consumer " + name + " consumed: " + item);
                    }
                    Thread.sleep(1000);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
