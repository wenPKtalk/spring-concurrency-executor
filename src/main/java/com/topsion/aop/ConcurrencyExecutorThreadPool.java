package com.topsion.aop;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        int taskNumber = taskNo.incrementAndGet();
        log.info("ConcurrencyExecutorPool--[{}] start execute {} task (active task: {}, wait queue: {})", poolName, taskNumber, super.getActiveCount(), super.getQueue().size());
    }
}
