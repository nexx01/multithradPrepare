package com.example.tdd.multithreading.tasks;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/*
 * 1. Write a Java program to create and start
 *  multiple threads that increment
 *  a shared counter variable concurrently.
 * */
public class IncrementVariableConcurrentlyTest  {

    @Test
    void shouldWrongCount() {
        var counter = new CounterBad();
        int numThread = 6;
        IncrementThread[] threads = new IncrementThread[numThread];

        for (int i=0;i<numThread;i++) {
            threads[i] = new IncrementThread(counter, 100000);
            threads[i].start();
        }

        try {
            for(IncrementThread thread: threads){
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assertions.assertThat(counter.getCount()).isNotEqualTo(100000);
        System.out.println("Final count: " + counter.getCount());
    }


    @Test
    void shouldCountSuccess() {
        var counter = new CounterSync();
        int numThread = 6;
        IncrementThread[] threads = new IncrementThread[numThread];

        for (int i=0;i<numThread;i++) {
            threads[i] = new IncrementThread(counter, 100000);
            threads[i].start();
        }

        try {
            for(IncrementThread thread: threads){
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assertions.assertThat(counter.getCount()).isNotEqualTo(100000);
        System.out.println("Final count: " + counter.getCount());
    }
}

class IncrementThread extends Thread{
    Counter counter;
    private int incrementsPerThread;

    public IncrementThread(Counter counter, int incrementsPerThread) {
        this.counter = counter;
        this.incrementsPerThread = incrementsPerThread;
    }

    @Override
    public void run() {
        for(int i=0; i<incrementsPerThread; i++){
            counter.increment();
        }
    }
}

interface Counter{
    void increment();
    int getCount();
}

class CounterBad implements Counter{
    private int count = 0;

    public void increment(){
        count++;
    }
    public int getCount(){
        return count;
    }

}


class CounterSync implements Counter{
    private int count = 0;

    public void increment(){
        count++;
    }
    public int getCount(){
        return count;
    }

}
