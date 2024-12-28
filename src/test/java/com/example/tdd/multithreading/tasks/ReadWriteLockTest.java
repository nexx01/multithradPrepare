package com.example.tdd.multithreading.tasks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {

    private static final int NUM_READERS = 5;
    private static final int NUM_WRITERS = 3;

    @Test
    void name() {
        var lock = new ReentrantReadWriteLock();
        var shareResource= new SharedResource();

        for (int i = 0; i < NUM_READERS; i++) {
            var readerThread = new Thread(new Reader(lock, shareResource));
            readerThread.start();
        }

        for (int i = 0; i < NUM_WRITERS; i++) {
            var writerThread= new Thread(new Writer(lock, shareResource));
                    writerThread.start();
        }
    }

    static class Reader implements Runnable {
        private ReadWriteLock lock;
        private SharedResource sharedResource;

        public Reader(ReadWriteLock lock, SharedResource sharedResource) {
            this.lock = lock;
            this.sharedResource = sharedResource;
        }

        @Override
        public void run() {
            while (true) {
                lock.readLock().lock();

                System.out.println("Reader " + Thread.currentThread().getName() + " is reading");

                lock.readLock().unlock();

                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Writer implements Runnable {
        private ReadWriteLock lock;
        private SharedResource sharedResource;

        public Writer(ReadWriteLock lock, SharedResource sharedResource) {
            this.lock = lock;
            this.sharedResource = sharedResource;
        }

        @Override
        public void run() {
            while (true) {
                lock.writeLock().lock();

                System.out.println("Writer " + Thread.currentThread().getName() + " is writing");

                lock.writeLock().unlock();

                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class SharedResource {
      private String data;

      public String read(){
          return data;
      }// Shared resource

        public void write(String newData){
            data = newData;
        }
    }


}
