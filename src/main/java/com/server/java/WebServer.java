package com.server.java;

import com.server.java.executor.PriorityExecutor;
import com.server.java.utils.ServerLogger;
import org.bouncycastle.ocsp.Req;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class WebServer extends Thread implements RejectedExecutionHandler {

    private static final int DEFAULT_PORT = 8080;

    private static ServerLogger Log = ServerLogger.getLogger(WebServer.class.getSimpleName());

    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("WebServer listening on http://localhost:" + DEFAULT_PORT);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                int requestCounter = 0;
                System.out.println("1: First In First Out Scheduling");
                System.out.println("2: Shortest Job First Priority Scheduling");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();

                if (choice == 1) {
                    firstInFirstOut(requestCounter);
                } else if (choice == 2) {
                    shortestJobFirstPriority(requestCounter);
                } else {
                    System.out.println("Invalid Choice.");
                }
            }

        } catch (IOException e) {
            Log.error("Runtime error: " + e.getMessage(), e);
        }
    }

    private static void firstInFirstOut(int requestCounter) {
        System.out.println("First In First Out Scheduling");
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<Runnable>();
        PriorityExecutor executor = new PriorityExecutor(1, 2, 2000, TimeUnit.MILLISECONDS, blockingQueue);
        executor.prestartAllCoreThreads();

        while (true) {
            requestCounter++;
            try {
                int priority = 0;
                RequestHandler handler = new RequestHandler(requestCounter, priority, serverSocket.accept());
                System.out.println("Request: " + requestCounter + " Priority: " + priority);
                executor.execute(handler);
            } catch (IOException e) {
                Log.error("Runtime error: " + e.getMessage(), e);
            }
        }
    }

    private static void shortestJobFirstPriority(int requestCounter) {
        System.out.println("Shortest Job First Priority Scheduling");
        PriorityBlockingQueue<Runnable> blockingQueue = new PriorityBlockingQueue<Runnable>();
        PriorityExecutor executor = new PriorityExecutor(1, 2, 2000, TimeUnit.MILLISECONDS, blockingQueue);
        executor.prestartAllCoreThreads();

        Random random = new Random(501);

        while (true) {
            requestCounter++;
            int priority = random.nextInt(10);
            try {
                final RequestHandler handler = new RequestHandler(requestCounter, priority, serverSocket.accept());
                System.out.println("Request: " + requestCounter + " Priority: " + priority);
                Future<RequestHandler> task = executor.submit(new Callable<RequestHandler>() {
                    @Override
                    public RequestHandler call() throws Exception {
                        handler.start();
                        return handler;
                    }
                }, priority);
            } catch (IOException e) {
                Log.error("Runtime error: " + e.getMessage(), e);
            }
        }
    }

    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("ThreadTask Rejected: " + ((RequestHandler) r).getRequestNo());
        System.out.println("Waiting for a second");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Let's add another time: " + ((RequestHandler) r).getRequestNo());
        executor.execute(r);
    }
}
