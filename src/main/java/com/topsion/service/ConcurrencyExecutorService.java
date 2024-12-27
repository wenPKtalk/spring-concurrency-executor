/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
 */
package com.topsion.service;

import com.topsion.aop.ConcurrencyTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConcurrencyExecutorService {

    @ConcurrencyTask(maxCount = 3, waitQueue = 5)
    public void execute() throws InterruptedException {
        Thread.sleep(5000);
        log.info("This is concurrency method.");
    }
}
