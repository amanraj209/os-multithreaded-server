package com.server.java;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer extends Thread {

    private static ServerLogger logger = ServerLogger.getLogger(WebServer.class);

    private static final int DEFAULT_PORT = 8080;
    private static final int N_THREADS = 100;

    public static void main(String[] args) {
        try {
            int numRequests = 1000;
            new WebServer().start(numRequests);
        } catch (Exception e) {
            logger.error("Startup error: " + e.getMessage(), e);
        }
    }

    private void start(int numRequests) {
        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
        for (int i = 0; i < numRequests; i++) {
            String[] params = new String[]{"curl", "http://127.0.0.1:8080"};
            final ProcessBuilder process = new ProcessBuilder(params);

            final int j = i + 1;
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        System.out.println("Request no.: " + j);
                        process.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            executor.execute(runnable);
        }
        executor.shutdown();
//        try {
//            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
//            System.out.println("WebServer listening on port " + DEFAULT_PORT);
//            ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
//            while (true) {
//                executor.submit(new RequestHandler(serverSocket.accept()));
//            }
//        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
//        }
    }
}
