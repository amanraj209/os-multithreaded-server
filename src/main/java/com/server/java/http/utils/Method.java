package com.server.java.http.utils;

public enum Method {

    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    OPTIONS("OPTIONS"),
    DELETE("DELETE"),
    TRACE("TRACE"),
    CONNECT("CONNECT"),
    UNRECOGNIZED(null);

    private final String method;

    Method(String method) {
        this.method = method;
    }
}
