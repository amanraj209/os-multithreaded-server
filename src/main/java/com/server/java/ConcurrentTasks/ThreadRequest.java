package com.server.java.ConcurrentTasks;

public class ThreadRequest implements Runnable {

    private String name;

    public ThreadRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void run() {
        try {
        Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Executing: " + name);
    }
}
