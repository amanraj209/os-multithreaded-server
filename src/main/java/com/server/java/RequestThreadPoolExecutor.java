package com.server.java;

import com.server.java.http.HttpRequest;
import com.server.java.http.HttpResponse;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RequestThreadPoolExecutor extends ThreadPoolExecutor {

    private static ServerLogger Log = ServerLogger.getLogger(RequestThreadPoolExecutor.class.getSimpleName());

    public RequestThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
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

        if (r instanceof HttpRequest) {
            HttpRequest request = ((HttpRequest) r);
            HttpResponse response = new HttpResponse(request);
            response.setSocket(request.getSocket());
            request.getResponseExecutor().execute(response);
        } else if (r instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) r;
            try {
                response.write(response.getSocket().getOutputStream());
                response.getSocket().close();
            } catch (Exception e) {
                Log.error("Runtime error: " + response, e);

            }
        }
    }

}
