/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
 */
package com.topsion.service;

import com.topsion.aop.ConcurrencyTask;
import org.springframework.stereotype.Service;

@Service
public class ConcurrencyExecutorService {

    @ConcurrencyTask(maxCount = 3, waitQueue = 5)
    public void execute() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("This is concurrency method.");
    }
}
