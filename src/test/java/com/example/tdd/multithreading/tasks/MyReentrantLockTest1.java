package com.example.tdd.multithreading.tasks;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class MyReentrantLockTest1 {

    @Test
    void name() {
        var lock = new ReentrantLock();

        var sharedClass = new SharedClass();
        Runnable task = () -> {
            lock.lock();
            try {
                for (int i = 0; i < 1000; i++) {
                    sharedClass.count++;
                }
            } finally {
                lock.unlock();
            }
        };

        Stream.generate(() -> new Thread(task))
                .limit(1000)
                .peek(Thread::start)
                .toList()
                .stream()
                .forEach(thread -> {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        System.out.println("Final count: " + sharedClass.count); // should print 2000
        Assertions.assertThat(sharedClass.count).isEqualTo(1000_000);

    }

    static class SharedClass {
        int count = 0;
    }
}
