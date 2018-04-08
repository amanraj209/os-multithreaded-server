package com.server.java;

import com.server.java.utils.ServerLogger;

import java.util.concurrent.*;

public class PriorityExecutor extends ThreadPoolExecutor {

    private static ServerLogger Log = ServerLogger.getLogger(PriorityExecutor.class.getSimpleName());

    public PriorityExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        RequestHandler handler = (RequestHandler) r;
        System.out.println("afterExecute() Execution Time: " + handler.getExecutionTime());
    }
}
