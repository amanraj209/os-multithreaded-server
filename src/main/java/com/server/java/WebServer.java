package com.server.java;

import com.server.java.http.HttpRequest;
import com.server.java.http.HttpResponse;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.*;

public class WebServer extends Thread implements RejectedExecutionHandler {

    private static final int DEFAULT_PORT = 8080;

    private static ServerLogger Log = ServerLogger.getLogger(WebServer.class.getSimpleName());

    public static void main(String[] args) {
        Integer threadCounter = 0;

        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<Runnable>();
        RequestThreadPoolExecutor requestExecutor = new RequestThreadPoolExecutor(500, 1000, 2000, TimeUnit.MILLISECONDS, blockingQueue);
        RequestThreadPoolExecutor responseExecutor = new RequestThreadPoolExecutor(500, 1000, 2000, TimeUnit.MILLISECONDS, blockingQueue);

        requestExecutor.prestartAllCoreThreads();
        responseExecutor.prestartAllCoreThreads();

        try {
            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("WebServer listening on port " + DEFAULT_PORT);

            while (true) {
                threadCounter++;
                System.out.println("Adding ThreadTask: " + threadCounter);
                RequestHandler handler = new RequestHandler(threadCounter.toString(), serverSocket.accept());
                handler.start(requestExecutor, responseExecutor);
            }
        } catch (IOException e) {
            Log.error("Runtime error: " + e.getMessage(), e);
        }
    }

    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        r = (r instanceof HttpRequest) ? ((HttpRequest) r) : ((HttpResponse) r);
        System.out.println("ThreadTask Rejected");
        executor.execute(r);
    }
}
