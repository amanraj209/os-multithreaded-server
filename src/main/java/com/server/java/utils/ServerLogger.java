package com.server.java.utils;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class ServerLogger {

    private Logger logger;

    private ServerLogger(String name) {
        this.logger = Logger.getLogger(name);
        this.logger.setLevel(Level.INFO);
        this.logger.addAppender(new ConsoleAppender());
    }

    public static ServerLogger getLogger(String name) {
        return new ServerLogger(name);
    }

    public void error(String message, Throwable throwable) {
        this.logger.log(Level.ERROR, message, throwable);
    }

    public void info(String message) {
        this.logger.log(Level.INFO, message);
    }
}
