package com.server.java.executor;

import java.util.Random;
import java.util.concurrent.*;

public class Driver {

    public static void main(String[] args) {
        PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>(1024);
        PriorityExecutor executor = new PriorityExecutor(1024, 2048, 1000L, TimeUnit.MILLISECONDS, queue);

        executor.prestartAllCoreThreads();

        Random random = new Random();

        executor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });

//        for (int i = 0; i < 5; i++) {
//            int ran = random.nextInt(5);
//            System.out.println("Priority submit: " + ran);
//            Future<Long> result = executor.submit(new Callable<Long>() {
//                @Override
//                public Long call() throws Exception {
//                    return 1L;
//                }
//            }, ran);
//
//            while (!result.isDone()) {
//                System.out.println("Executing executor: " + ran);
//            }
//            try {
//                System.out.println(result.get() + " with executor " + ran);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//
//        executor.shutdown();
    }
}
