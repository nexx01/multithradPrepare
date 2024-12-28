package com.example.tdd.multithreading.tasks;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

public class IncrementSharedCounter {

    @SneakyThrows
    @ParameterizedTest
    @CsvSource({
            "1,1,1",
            "1,1000,1000",
            "2,1000,2000",
            "5,1000,5000",
            "1000,1000,1000000",
    })
    void f(int countThreads, int countCycles, int expected) {
        var sharedClass = new SharedClass();
        Runnable task = () -> {
            for (int i = 0; i < countCycles; i++) {
                sharedClass.counter++;
            }
        };

        Stream.generate(()->new Thread(task))
                .limit(countThreads)
                .peek(Thread::start)
                .toList()
                .stream()
                .forEach(thread -> {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        Assertions.assertEquals(expected, sharedClass.counter);
    }
}

class SharedClass{
    int counter = 0;
}
