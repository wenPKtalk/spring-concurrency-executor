package com.topsion.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConcurrencyTask {
    int maxCount() default 5;      // 最大并发线程数
    int waitQueue() default 20;   // 等待队列大小
    String maxCountKey() default "";  // 配置项的 key
    String waitQueueKey() default ""; // 配置项的 key
}
