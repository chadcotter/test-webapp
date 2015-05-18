package com.infusionsoft.threads;

import java.util.Date;

/**
 * Created by chad on 5/8/15.
 */
public class WorkerThread implements Runnable {

    private String name;
    private int count = 1;

    public WorkerThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name + " running (" + count + ")..." + new Date());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(name + " stopped (" + count++ + "). " + new Date());
    }

    public String getName() {
        return name;
    }
}
