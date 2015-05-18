package com.infusionsoft.fake;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PhaserExampleTest {



    @Test
    public void testPhaser() throws InterruptedException{

        List<Runnable> tasks = new ArrayList<>();
        final List<Exception> errors = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 2; i++) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    int i = 0;
                    for (i = 0; i < 10; i++) {
                        System.out.println("Thread id: " + Thread.currentThread().getId() + " count: " + i);

                        /* try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    }
                    try {
                        assertEquals(i, 10);
                    } catch (Exception e) {
                        errors.add(e);
                    }
                }
            };

            tasks.add(runnable);

        }

        runTasks(tasks);

        Thread.sleep(10000); //give 10 seconds to complete tests

        assertTrue(errors.size() == 0);
    }



    void runTasks(List<Runnable> tasks) throws InterruptedException {

        final Phaser phaser = new Phaser(1) {
            protected boolean onAdvance(int phase, int registeredParties) {
                return phase >= 1 || registeredParties == 0;
            }
        };

        int i = 0;


        for (final Runnable task : tasks) {
            phaser.register();
            new Thread() {
                public void run() {
                    do {
                        System.out.println("Step:");
                        phaser.arriveAndAwaitAdvance();
                        System.out.println("Next:");
                        task.run();
                        System.out.println("Done");
                    } while (!phaser.isTerminated());
                }
            }.start();
            //Thread.sleep(1000);
        }

        System.out.println("here1");
        phaser.arriveAndDeregister();
        System.out.println("here2");
    }

}
