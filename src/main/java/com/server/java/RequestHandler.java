package com.server.java;

import com.server.java.http.HttpRequest;
import com.server.java.http.HttpResponse;
import com.server.java.utils.ServerLogger;

import java.net.Socket;

public class RequestHandler implements Runnable {

    private static ServerLogger Log = ServerLogger.getLogger(RequestHandler.class.getSimpleName());

    private int requestNo;
    private int priority;
    private Socket socket;

    public RequestHandler(int requestNo, int priority, Socket socket) {
        this.requestNo = requestNo;
        this.priority = priority;
        this.socket = socket;
    }

    public int getRequestNo() {
        return requestNo;
    }

    public int getPriority() {
        return priority;
    }

    public void run() {
        start();
    }

    public void start() {
        System.out.println("Executing: " + getRequestNo() + " Priority: " + getPriority());
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
