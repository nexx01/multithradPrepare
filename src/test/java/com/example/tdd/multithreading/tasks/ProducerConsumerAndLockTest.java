package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerAndLockTest {

    private static final Lock lock = new ReentrantLock();
    private final static Condition condition = lock.newCondition();
    private final static Queue<Integer> q= new LinkedList<>();
    private final static int MAX_SIZE = 10;

    @Test
    void name() throws InterruptedException {
        var producer = new Thread(new Producer());
        var consumer = new Thread(new Consumer());
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    static class Producer implements Runnable {

        @Override
        public void run() {
            while (true){
                lock.lock();
                while (q.size()==MAX_SIZE){
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    q.offer(1);
                    System.out.println("Producer produce");
                    System.out.println(q.size());
                }finally {
                    condition.signalAll();
                    lock.unlock();
                }
            }
        }
    }

    static class Consumer implements Runnable {

        @Override
        public void run() {
            while(true){
                lock.lock();
                while(q.isEmpty()){
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    System.out.println("Consumer consume " + q.poll());

                }finally {
                    condition.signalAll();
                    lock.unlock();
                }
            }
        }
    }


}
