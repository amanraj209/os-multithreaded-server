package com.server.java;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer extends Thread {

    private static ServerLogger logger = ServerLogger.getLogger(WebServer.class);

    private static final int DEFAULT_PORT = 8080;
    private static final int N_THREADS = 3;

    public static void main(String[] args) {
        try {
            new WebServer().start(getValidPort(args));
        } catch (Exception e) {
            logger.error("Startup error: " + e.getMessage(), e);
        }
    }

    private void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("WebServer listening on port " + port + " (press CTRL-C to quit)");
            ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
            while (true) {
                executor.submit(new RequestHandler(serverSocket.accept()));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    static int getValidPort(String[] args) {
        if (args.length > 0) {
            int port = Integer.parseInt(args[0]);
            if (port > 0 && port < 65535) {
                return port;
            }
        }
        return DEFAULT_PORT;
    }
}
