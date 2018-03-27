package com.server.java;

import com.server.java.http.HttpRequest;
import com.server.java.http.HttpResponse;

import java.net.Socket;

public class RequestHandler implements Runnable {

    private static ServerLogger logger = ServerLogger.getLogger(RequestHandler.class);

    private Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            HttpRequest request = new HttpRequest(socket.getInputStream());
            HttpResponse response = new HttpResponse(request);
            response.write(socket.getOutputStream());
            socket.close();
        } catch (Exception e) {
            logger.error("Runtime error: " + e.getMessage(), e);
        }
    }
}
