package com.server.java;

import com.server.java.executor.PriorityExecutor;
import com.server.java.http.HttpRequest;
import com.server.java.utils.ServerLogger;

import java.net.Socket;

public class RequestHandler {

    private static ServerLogger Log = ServerLogger.getLogger(RequestHandler.class.getSimpleName());

    private String name;
    private Socket socket;

    public RequestHandler(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }

    public void start(PriorityExecutor requestExecutor, PriorityExecutor responseExecutor) {
        System.out.println("Executing: " + name);
        try {
            HttpRequest request = new HttpRequest(socket.getInputStream());
            request.setResponseExecutor(responseExecutor);
            request.setSocket(socket);
            requestExecutor.execute(request);
        } catch (Exception e) {
            Log.error("Runtime error: " + e.getMessage(), e);
        }
    }
}
