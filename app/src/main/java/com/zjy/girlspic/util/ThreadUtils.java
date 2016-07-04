package com.zjy.girlspic.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiyoung.tsang on 16/6/18.
 */
public class ThreadUtils {
    public final static int CPU_CORE = Runtime.getRuntime()
            .availableProcessors();

    /**
     * 主线程Handler
     */
    public static final Handler MAIN_HANDLE = new Handler(
            Looper.getMainLooper());


    /**
     * 普通线程池，执行时间较短的任务
     */
    public final static ExecutorService GENERAL_THREAD_POOL = new ThreadPoolExecutor(
            CPU_CORE + 1, CPU_CORE * 2 + 1, 60L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    /**
     * 耗时线程池，执行如下载文件、高耗时处理等任务（如时耗30s、瞬间并发1~5）
     */
    public final static ExecutorService TIME_COST_THREAD_POOL = Executors
            .newCachedThreadPool();
}
