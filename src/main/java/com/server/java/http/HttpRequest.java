package com.server.java.http;

import com.server.java.utils.ServerLogger;
import com.server.java.utils.Method;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {

    private static ServerLogger Log = ServerLogger.getLogger(HttpRequest.class.getSimpleName());

    private String version;
    private List<String> headers = new ArrayList<String>();

    Method method;
    String uri;

    public HttpRequest(InputStream inputStream) {
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
}
