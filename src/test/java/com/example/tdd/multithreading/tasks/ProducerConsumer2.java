package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer2 {

   final static Queue<Integer> buffer = new LinkedList<Integer>();
    final static int BUFFER_SIZE = 19;

    @Test
    void name() throws InterruptedException {
        var producer = new Thread(new Producer());
        var consumer = new Thread(new Consumer());
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }


static  class Producer implements Runnable {

        @Override
        public void run() {
            int val = 1;
            while (true){
                synchronized (buffer){
                    while(buffer.size()==BUFFER_SIZE){
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("Produced: " + val + " size " + buffer.size());
                    buffer.offer(val++);
                    buffer.notify();
                }
            }
        }
    }


    static class Consumer implements Runnable {

        @Override
        public void run() {
            while(true){
                synchronized(buffer){
                    while(buffer.size()==0){
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("Consumed: " + buffer.poll() + " size " + buffer.size());
                    buffer.notify();
                }
            }
        }
    }

}
