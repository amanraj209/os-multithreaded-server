package com.server.java.ConcurrentTasks;

import com.server.java.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.*;

public class RequestExecutor {

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        Integer threadCounter = 0;

        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(1000);
        RequestThreadPoolExecutor executor = new RequestThreadPoolExecutor(100, 500, 2000, TimeUnit.MILLISECONDS, blockingQueue);

        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {

            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("ThreadTask Rejected: " + ((RequestHandler) r).getName());
                System.out.println("Waiting for a second");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Let's add another time: " + ((RequestHandler) r).getName());
                executor.execute(r);
            }
        });

        int count = executor.prestartAllCoreThreads();
        System.out.println("Core Threads: " + count);

        try {
            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("WebServer listening on port " + DEFAULT_PORT + " (press CTRL-C to quit)");

            while (true) {
                threadCounter++;
                System.out.println("Adding ThreadTask: " + threadCounter);

                executor.execute(new RequestHandler(threadCounter.toString(), serverSocket.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        curl -H "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/604.5.6 (KHTML, like Gecko) Version/11.0.3 Safari/604.5.6" -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8" http://127.0.0.1:8080

    }
}
