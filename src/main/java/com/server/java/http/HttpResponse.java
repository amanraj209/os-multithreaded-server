package com.server.java.http;

import com.server.java.utils.ServerLogger;
import com.server.java.utils.ContentType;
import com.server.java.utils.Status;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse {

    private static ServerLogger Log = ServerLogger.getLogger(HttpResponse.class.getSimpleName());
    private static final String VERSION = "HTTP/1.0";
    private byte[] body;
    private List<String> headers = new ArrayList<String>();

    public HttpResponse(HttpRequest request) {
        switch (request.method) {
            case HEAD:
                fillHeaders(Status._200);
                break;
            case GET:
                try {
                    fillHeaders(Status._200);
                    File file = new File("." + request.uri);
                    if (file.isDirectory()) {
                        headers.add(ContentType.HTML.toString());
                        StringBuilder result = new StringBuilder("<html><head><title>Index of ");
                        result.append(request.uri);
                        result.append("</title></head><body><h1>Index of ");
                        result.append(request.uri);
                        result.append("</h1><hr><pre>");

                        File[] files = file.listFiles();
                        if (files != null) {
                            for (File subFile : files) {
                                result.append("<a href=\"")
                                        .append(subFile.getPath())
                                        .append("\">")
                                        .append(subFile.getPath())
                                        .append("</a>\n");
                            }
                        }
                        result.append("<hr></pre></body></html>");
                        fillResponse(result.toString());

                    } else if (file.exists()) {
                        setContentType(request.uri, headers);
                        fillResponse(getBytes(file));
                    } else {
                        Log.info("File not found: " + request.uri);
                        fillHeaders(Status._400);
                        fillResponse(Status._400.toString());
                    }
                } catch (Exception e) {
                    Log.error(e.getMessage(), e);
                    fillHeaders(Status._400);
                    fillResponse(Status._400.toString());
                }
                break;
            case UNRECOGNIZED:
                fillHeaders(Status._400);
                fillResponse(Status._400.toString());
                break;
            default:
                fillHeaders(Status._501);
                fillResponse(Status._501.toString());
        }
    }

    private byte[] getBytes(File file) {
        int length = (int) file.length();
        byte[] array = new byte[length];
        try {
            InputStream inputStream = new FileInputStream(file);
            int offset = 0;
            while (offset < length) {
                int count = inputStream.read(array, offset, (length - offset));
                offset += count;
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            Log.error(e.getMessage(), e);
        } catch (IOException e) {
            Log.error(e.getMessage(), e);
        }
        return array;
    }

    private void fillHeaders(Status status) {
        headers.add(HttpResponse.VERSION + " " + status.toString());
        headers.add("Connection: close");
        headers.add("Server: MultithreadedWebServer");
    }

    private void fillResponse(String response) {
        body = response.getBytes();
    }

    private void fillResponse(byte[] response) {
        body = response;
    }

    private void setContentType(String uri, List<String> list) {
        try {
            String extension = uri.substring(uri.indexOf(".") + 1);
            list.add(ContentType.valueOf(extension.toUpperCase()).toString());
        } catch (Exception e) {
            Log.error("ContentType not found: " + e.getMessage(), e);
        }
    }

    public void write(OutputStream outputStream) {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        try {
            for (String header: headers) {
                dataOutputStream.writeBytes(header + "\r\n");
            }
            dataOutputStream.writeBytes("\r\n");
            if (body != null) {
                dataOutputStream.write(body);
            }
            dataOutputStream.writeBytes("\r\n");
            dataOutputStream.flush();
        } catch (IOException e) {
            Log.error(e.getMessage(), e);
        }
    }
}
