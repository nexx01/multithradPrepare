package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

public class MyReadWriteLockTest1 {

    private static final int NUM_READERS = 5;
    private static final int NUM_WRITERS = 1;
    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM_READERS + NUM_WRITERS);
    @Test
    void name() {
         var lock = new ReentrantReadWriteLock();
         var sharedResourse = new SharedResourse();
        Stream.generate(() -> new Thread(new Writer(lock, sharedResourse)))
                .limit(NUM_WRITERS)
                .forEach(Thread::start);

        Stream.generate(() -> new Thread(new Reader(lock, sharedResourse)))
                .limit(NUM_READERS)
                .peek(Thread::start)
                .toList().forEach(thread -> {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });


    }

    static class Reader implements Runnable {
        private final ReadWriteLock lock;
        private final SharedResourse sharedResourse;

        public Reader(ReadWriteLock lock, SharedResourse sharedResourse) {
            this.lock = lock;
            this.sharedResourse = sharedResourse;
        }

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            while(true){
                lock.readLock().lock();


                System.out.println("Thread " + Thread.currentThread().getName() + " is reading. read: "+sharedResourse.read());

                lock.readLock().unlock();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Writer implements Runnable {
        private final ReadWriteLock lock;
        private final SharedResourse sharedResourse;
        private int counter = 0;

        public Writer(ReadWriteLock lock, SharedResourse sharedResourse) {
            this.lock = lock;
            this.sharedResourse = sharedResourse;
        }

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            while (true){
                lock.writeLock().lock();

                System.out.println("Thread " + Thread.currentThread().getName() + " is writing. write: " + sharedResourse.write(++counter));

                lock.writeLock().unlock();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class SharedResourse {
        private int data;

        public int read(){
            return data;
        }

        public int write(int data){
            this.data = data;
            return data;
        }
    }
}
