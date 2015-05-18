package com.infusionsoft.threads;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Created by chad on 5/14/15.
 */
public class CallableWorkerThread implements Callable<String> {

    private String name;
    private int count = 1;

    public CallableWorkerThread(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        System.out.println(name + " Callable running (" + count + ")..." + new Date());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(name + " Callable stopped (" + count++ + "). " + new Date());

        return "Done!";
    }
}
