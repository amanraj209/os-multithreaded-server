package com.server.java.http;

import com.server.java.ServerLogger;
import com.server.java.http.utils.Method;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {

    private static ServerLogger logger = ServerLogger.getLogger(HttpRequest.class);

    List<String> headers = new ArrayList<String>();

    Method method;
    String uri;
    String version;

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
            logger.error(e.getMessage(), e);
        }
    }

    private void parseRequestLine(String str) {
        logger.info(str);
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
        logger.info(str);
        headers.add(str);
    }
}
