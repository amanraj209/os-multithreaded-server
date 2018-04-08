package com.server.java;

import com.server.java.http.HttpRequest;
import com.server.java.http.HttpResponse;
import com.server.java.utils.ServerLogger;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class RequestHandler implements Runnable, Comparable<RequestHandler> {

    private static ServerLogger Log = ServerLogger.getLogger(RequestHandler.class.getSimpleName());

    private int requestNo;
    private int priority;
    private int timeQuantum;
    private int executionTime;
    private Socket socket;
    private BlockingQueue<Runnable> blockingQueue;

    public RequestHandler(int requestNo, Socket socket) {
        this.requestNo = requestNo;
        this.socket = socket;
    }

    public RequestHandler(int requestNo, int priority, Socket socket) {
        this.requestNo = requestNo;
        this.priority = priority;
        this.socket = socket;
    }

    public RequestHandler(int requestNo, int priority, int timeQuantum, int executionTime, Socket socket) {
        this.requestNo = requestNo;
        this.priority = priority;
        this.timeQuantum = timeQuantum;
        this.executionTime = executionTime;
        this.socket = socket;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setBlockingQueue(BlockingQueue<Runnable> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public int compareTo(RequestHandler o) {
        return this.priority - o.priority;
    }

    @Override
    public void run() {
        if (executionTime <= timeQuantum) start();
        else {
            executionTime -= timeQuantum;
            System.out.println("afterExecute() Execution Time: " + executionTime);
            if (executionTime > 0) {
                try {
                    Thread.sleep(timeQuantum);
                    if (blockingQueue != null) {
                        blockingQueue.put(this);
                    }
                } catch (InterruptedException e) {
                    Log.error("Runtime error: " + e.getMessage(), e);
                }
            }
        }
    }

    public void start() {
        System.out.println("Executing: " + requestNo + " Priority: " + priority);
        try {
            HttpRequest request = new HttpRequest(socket.getInputStream());
            HttpResponse response = new HttpResponse(request);
            response.write(socket.getOutputStream());
            socket.close();
        } catch (Exception e) {
            Log.error("Runtime error: " + e.getMessage(), e);
        }
    }
}
