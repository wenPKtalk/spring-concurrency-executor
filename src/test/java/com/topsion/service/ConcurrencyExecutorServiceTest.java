package com.topsion.service;

import com.topsion.aop.ConcurrencyTaskExecutorAspect;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConcurrencyExecutorServiceTest {

    @Autowired
    ConcurrencyExecutorService concurrencyExecutorService;

    @Autowired
    ConcurrencyTaskExecutorAspect concurrencyTaskExecutorAspect;

    @Test
    void should_max_3_execute_run_when_call() throws NoSuchMethodException, InterruptedException {
        // Given
        int threadCount = 5;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch executionLatch = new CountDownLatch(threadCount);
        AtomicInteger concurrentExecutions = new AtomicInteger(0);
        AtomicInteger maxConcurrentExecutions = new AtomicInteger(0);
        String projectId = "p1";

        // When
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(() -> {
                try {
                    startLatch.await(); // 等待所有线程就绪
                    int current = concurrentExecutions.incrementAndGet();
                    maxConcurrentExecutions.updateAndGet(max -> Math.max(max, current));
                    concurrencyExecutorService.execute();
                    concurrentExecutions.decrementAndGet();
                    executionLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            threads.add(thread);
            thread.start();
        }

        // Then
        startLatch.countDown(); // 启动所有线程

        // 等待一小段时间让线程池达到稳定状态
        Thread.sleep(1000);

        // 获取线程池状态
        Method executeMethod = ConcurrencyExecutorService.class.getMethod("execute");
        ThreadPoolExecutor executor = concurrencyTaskExecutorAspect.getExecutorCache().get(executeMethod);

        assertNotNull(executor, "ThreadPoolExecutor should not be null");
        assertTrue(executor.getActiveCount() <= 3, "Active thread count should not exceed 3.");
        assertTrue(executor.getQueue().size() <= 2, "Wait queue max is 2 because this is snapshot maybe lose.");
        assertEquals(5, maxConcurrentExecutions.get(), "Maximum concurrent executions should be 3.");

        // 等待所有任务完成
        executionLatch.await(7, TimeUnit.SECONDS);

        // 验证所有任务都已完成
        assertEquals(0, concurrentExecutions.get(), "All executions should be completed");

    }

}