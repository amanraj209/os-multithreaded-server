package com.server.java;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.*;

public class WebServer extends Thread {

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
    }
}
