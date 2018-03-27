package com.server.java;

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

    public static ServerLogger getLogger(Class cls) {
        return new ServerLogger(cls.getName());
    }

    public void error(String message, Throwable throwable) {
        this.logger.log(Level.ERROR, message, throwable);
    }

    public void info(String message) {
        this.logger.log(Level.INFO, message);
    }
}
