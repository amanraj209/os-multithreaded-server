package com.server.java;

import com.server.java.utils.ServerLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class WebServer {

    private static final int DEFAULT_PORT = 8080;
    private static final int DEFAULT_PRIORITY = 0;
    private static final int DEFAULT_TIME_QUANTUM = 30;

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
                System.out.println("3: Round Robin Scheduling");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();

                if (choice == 1) {
                    firstInFirstOut(requestCounter);
                } else if (choice == 2) {
                    shortestJobFirstPriority(requestCounter);
                } else if (choice == 3) {
                    roundRobinScheduling(requestCounter);
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
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 2000, TimeUnit.MILLISECONDS, blockingQueue);
        executor.prestartAllCoreThreads();

        while (true) {
            requestCounter++;
            try {
                RequestHandler handler = new RequestHandler(requestCounter, serverSocket.accept());
                System.out.println("Request: " + requestCounter);
                executor.execute(handler);
            } catch (IOException e) {
                Log.error("Runtime error: " + e.getMessage(), e);
            }
        }
    }

    private static void shortestJobFirstPriority(int requestCounter) {
        System.out.println("Shortest Job First Priority Scheduling");
        PriorityBlockingQueue<Runnable> blockingQueue = new PriorityBlockingQueue<Runnable>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 2000, TimeUnit.MILLISECONDS, blockingQueue);
        executor.prestartAllCoreThreads();

        Random random = new Random(501);

        while (true) {
            requestCounter++;
            int priority = random.nextInt(10);
            try {
                RequestHandler handler = new RequestHandler(requestCounter, priority, serverSocket.accept());
                System.out.println("Request: " + requestCounter + " Priority: " + priority);
                executor.execute(handler);
            } catch (IOException e) {
                Log.error("Runtime error: " + e.getMessage(), e);
            }
        }
    }

    private static void roundRobinScheduling(int requestCounter) {
        System.out.println("Round Robin Scheduling");
        PriorityBlockingQueue<Runnable> blockingQueue = new PriorityBlockingQueue<Runnable>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 2, 2000, TimeUnit.MILLISECONDS, blockingQueue);
        executor.prestartAllCoreThreads();

        Random random = new Random(501);

        while (true) {
            requestCounter++;
            try {
                int executionTime = random.nextInt(10) * 10;
                RequestHandler handler = new RequestHandler(requestCounter, DEFAULT_PRIORITY, DEFAULT_TIME_QUANTUM, executionTime, serverSocket.accept());
                handler.setBlockingQueue(blockingQueue);
                System.out.println("Request: " + requestCounter + " Time Quantum: " + DEFAULT_TIME_QUANTUM);
                executor.execute(handler);
            } catch (IOException e) {
                Log.error("Runtime error: " + e.getMessage(), e);
            }
        }
    }
}
