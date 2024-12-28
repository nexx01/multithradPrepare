package com.example.tdd.multithreading.myTests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

public class SyncronizedTest {

    @Test
    void name2() {
        System.out.println("11111111111");
    }

    @Test
    void name() throws InterruptedException {
        var sharedClass = new SharedClass();
        Runnable taskIncrement = () -> {
            if(sharedClass.count<=0) {
                for (int i = 0; i < 1000; i++) {
                    sharedClass.increment();
                }
            } else {
                for (int i = 0; i < 1000; i++) {
                    sharedClass.decrement();
                }
            }

        };

        var list = Stream.generate(() -> new Thread(taskIncrement ))
                .limit(1000)
                .map(t -> {
                    t.start();
                    return t;
                }).<Thread>toList();

        for (Thread t : list) {
            t.join();
        }


        Assertions.assertThat(sharedClass.count).isEqualTo(0);
    }
}

class SharedClass {

    int count = 0;

synchronized     void  increment() {
//        System.out.println("increment();");
        ++count;
    }

    synchronized void decrement() {
//        System.out.println("decrement();");
        --count;
    }

}
