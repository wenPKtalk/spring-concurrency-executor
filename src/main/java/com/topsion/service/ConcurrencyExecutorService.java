package com.topsion.service;

import com.topsion.aop.ConcurrencyTask;

@Service
public class ConcurrencyExecutorService {

    @ConcurrencyTask(maxCount = 3, waitQueue = 5)
    public void execute() {
        System.out.println("This is concurrency method.");
    }
}
