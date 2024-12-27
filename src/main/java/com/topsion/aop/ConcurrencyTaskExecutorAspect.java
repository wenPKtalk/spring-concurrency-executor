/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
 */
package com.topsion.aop;

import jakarta.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
@Slf4j
@Getter
@RequiredArgsConstructor
public class ConcurrencyTaskExecutorAspect {
    private final Environment environment;
    private final Map<Method, ThreadPoolExecutor> executorCache = new ConcurrentHashMap<>();

    @Around("@annotation(concurrencyTask)")
    @SuppressWarnings("DangerousThreadPoolExecutorUsage")
    public Object handleConcurrencyTask(ProceedingJoinPoint joinPoint, ConcurrencyTask concurrencyTask) throws Throwable {
        final int maxCount = getOrFromConfiguration(concurrencyTask.maxCount(), concurrencyTask.maxCountKey());
        final int waitQueue = getOrFromConfiguration(concurrencyTask.waitQueue(), concurrencyTask.waitQueueKey());

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ThreadPoolExecutor executor = executorCache.computeIfAbsent(method, _m ->
                new ConcurrencyExecutorThreadPool(
                        method.getName(),
                        maxCount,
                        maxCount,
                        5L, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(waitQueue),
                        new ThreadPoolExecutor.CallerRunsPolicy()
                )
        );
        //允许回收核心线程
        executor.allowCoreThreadTimeOut(true);
        Future<Object> future = executor.submit(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new CompletionException(throwable);
            }
        });
        try {
            // 等待任务完成并返回结果
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Task was interrupted", e);
            return null;
        } catch (ExecutionException e) {
            log.error("Task execution failed", e);
            return null;
        }
    }

    public int getOrFromConfiguration(int fromAnnotation, String fromConfigurationKey) {
        if (StringUtils.hasText(fromConfigurationKey)) {
            return environment.getProperty(fromConfigurationKey, Integer.class, fromAnnotation);
        }

        return fromAnnotation;
    }


    /**
     * bean 销毁时被回收
     */
    @PreDestroy
    public void shutdownAllExecutors() {
        executorCache.values().forEach(ThreadPoolExecutor::shutdown);
    }

}
