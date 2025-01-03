/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
 */
package com.topsion.aop;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcurrencyExecutorThreadPool extends ThreadPoolExecutor {
    private final String poolName;
    private final AtomicInteger taskNo = new AtomicInteger(0);

    public ConcurrencyExecutorThreadPool(String poolName,
                                         int corePoolSize,
                                         int maximumPoolSize,
                                         long keepAliveTime,
                                         TimeUnit unit,
                                         BlockingQueue<Runnable> workQueue,
                                         RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.poolName = poolName;
    }

    @Override
    @SuppressWarnings("Slf4jLogsafeArgs")
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        int taskNumber = taskNo.incrementAndGet();
        log.info("ConcurrencyExecutorPool--[{}] start execute {} task (active task: {}, wait queue: {})", poolName, taskNumber, super.getActiveCount(),
                Objects.nonNull(super.getQueue()) ? super.getQueue().size() : 0);
    }
}
