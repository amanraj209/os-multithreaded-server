package com.server.java.http;

import com.server.java.RequestThreadPoolExecutor;
import com.server.java.ServerLogger;
import com.server.java.http.utils.Method;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest implements Runnable {

    private static ServerLogger Log = ServerLogger.getLogger(HttpRequest.class.getSimpleName());

    private InputStream inputStream;
    private RequestThreadPoolExecutor responseExecutor;
    private Socket socket;
    private String version;
    private List<String> headers = new ArrayList<String>();

    Method method;
    String uri;

    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String str = reader.readLine();
            parseRequestLine(str);

            while (!str.equals("")) {
                str = reader.readLine();
                parseRequestHeader(str);
            }
        } catch (IOException e) {
            Log.error(e.getMessage(), e);
        }
    }

    private void parseRequestLine(String str) {
        Log.info(str);
        String[] split = str.split("\\s+");
        try {
            method = Method.valueOf(split[0]);
        } catch (Exception e) {
            method = Method.UNRECOGNIZED;
        }
        uri = split[1];
        version = split[2];
    }

    private void parseRequestHeader(String str) {
        Log.info(str);
        headers.add(str);
    }

    public HttpRequest(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public RequestThreadPoolExecutor getResponseExecutor() {
        return responseExecutor;
    }

    public void setResponseExecutor(RequestThreadPoolExecutor responseExecutor) {
        this.responseExecutor = responseExecutor;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
