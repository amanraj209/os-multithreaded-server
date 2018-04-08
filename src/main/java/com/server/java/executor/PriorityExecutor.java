package com.server.java.executor;

import com.server.java.utils.ServerLogger;

import java.util.concurrent.*;

public class PriorityExecutor extends ThreadPoolExecutor {

    private static ServerLogger Log = ServerLogger.getLogger(PriorityExecutor.class.getSimpleName());

    public PriorityExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public PriorityExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public PriorityExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public PriorityExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        System.out.println("Perform beforeExecute() logic");
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t != null) {
            System.out.println("Perform exception handler logic");
        }
        System.out.println("Perform afterExecute() logic");
    }

    public <T> Future<T> submit(Callable<T> task, int priority) {
        if (task == null) {
            throw new NullPointerException();
        }
        RunnableFuture<T> futureTask = newTaskFor(task, priority);
        execute(futureTask);
        return futureTask;
    }

    public <T> Future<T> submit(Runnable task, T result, int priority) throws InterruptedException {
        if (task == null) {
            throw new NullPointerException();
        }
        RunnableFuture<T> futureTask = newTaskFor(task, result, priority);
        execute(futureTask);
        return futureTask;
    }

    protected <T> ComparableFutureTask<T> newTaskFor(Callable<T> callable, int priority) {
        return new ComparableFutureTask<T>(callable, priority);
    }

    protected <T> ComparableFutureTask<T> newTaskFor(Runnable runnable, T value, int priority) {
        return new ComparableFutureTask<T>(runnable, value, priority);
    }
}
