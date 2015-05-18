package com.infusionsoft.fake;

import static org.junit.Assert.*;

import com.infusionsoft.threads.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class MyFakeClassTest {

	private static MyFakeClass myFakeClass;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		myFakeClass = new MyFakeClass();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testGetOne() {
		System.out.println("Starting test...");
		
		assertTrue(myFakeClass.getOne() == 1);
		
		System.out.println("Test finished.");
	}

    @Test
    public void testScheduledThreadPool() throws InterruptedException {
        ScheduledThreadPoolExecutor scheduledThreadPool = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(3);

        List<ScheduledFuture<?>> scheduledFutures = new ArrayList<>();

        for (int i = 0; i < 3 ; i++ ) {
            Thread.sleep(50);
            WorkerThread worker = new WorkerThread("Thread #" + i);

            ScheduledFuture<?> scheduledFuture;
            if (i < 2) {
                scheduledFuture = scheduledThreadPool.scheduleWithFixedDelay(worker, 0, 3, TimeUnit.SECONDS);
            } else {
                scheduledFuture = scheduledThreadPool.schedule(worker, 7, TimeUnit.SECONDS);
            }

            scheduledFutures.add(scheduledFuture);
        }

        Thread.sleep(9000);

        BlockingQueue<Runnable> tasks = scheduledThreadPool.getQueue();

        System.out.println("Start checks...");

        for (Runnable task : tasks) {

                ScheduledFuture<?> scheduledFuture = (ScheduledFuture<?>) task;
                if (scheduledFutures.contains(scheduledFuture)) {
                    System.out.println("IS THE SAME");
                }

                /*if (scheduledFutures.get(0).equals(scheduledFuture)) {
                    System.out.println("IS THE SAME2");
                }*/

                RunnableScheduledFuture runnableTask = (RunnableScheduledFuture) task;
                System.out.println("Is periodic: " + runnableTask.isPeriodic());

                System.out.println("Executing now...");
                scheduledThreadPool.execute(task);
            }

        //add some delay to let some threads spawn by scheduler
        Thread.sleep(25000);

        scheduledThreadPool.shutdown();
        while(!scheduledThreadPool.isTerminated()){
            Thread.sleep(200);
        }
        System.out.println("Finished all threads");
    }


    @Test
    public void testScheduledThreadPoolRunNow() throws InterruptedException, ExecutionException {

        //RunNow2ScheduledThreadPoolExecutore works...but RunNow doesn't...need to pass actual runnables and not their futures

        RunNowScheduledExecutorService scheduledThreadPool = new RunNow2ScheduledThreadPoolExecutor(2);

        List<ScheduledFuture<?>> scheduledFutures = new ArrayList<>();


        for (int i = 0; i < 10; i++) {
            Thread.sleep(50);
            WorkerThread worker = new WorkerThread("Thread #" + i);

            ScheduledFuture<?> scheduledFuture;
            if (i < 9) {
                scheduledFuture = scheduledThreadPool.scheduleWithFixedDelay(worker, 0, 20, TimeUnit.SECONDS);
            } else if (i < 10) {
                CallableWorkerThread callable = new CallableWorkerThread("Thread #" + i);
                scheduledFuture = scheduledThreadPool.schedule(callable, 20, TimeUnit.SECONDS);
            } else {
                scheduledFuture = scheduledThreadPool.schedule(worker, 21, TimeUnit.SECONDS);
                scheduledFuture.cancel(false);
            }

            scheduledFutures.add(scheduledFuture);
        }

        System.out.println("Queue size................" + ((ScheduledThreadPoolExecutor) scheduledThreadPool).getQueue().size());

        //Thread.sleep(9000);

        BlockingQueue<Runnable> tasks = ((ScheduledThreadPoolExecutor) scheduledThreadPool).getQueue();

        System.out.println("Run Now...");
        scheduledThreadPool.runNow();
        System.out.println("Queue size 2................" + ((ScheduledThreadPoolExecutor) scheduledThreadPool).getQueue().size());


        //add some delay to let some threads spawn by scheduler
        Thread.sleep(20000);

        //ScheduledFuture<String> future = (ScheduledFuture<String>) scheduledFutures.get(9);
        //String result = future.get();
        //System.out.println("Here is your result: " + result + " " + new Date());

        System.out.println("Queue size 3................" + ((ScheduledThreadPoolExecutor) scheduledThreadPool).getQueue().size());

        Thread.sleep(10000);

        System.out.println("Queue size 4................" + ((ScheduledThreadPoolExecutor) scheduledThreadPool).getQueue().size());

        scheduledThreadPool.shutdown();
        while(!scheduledThreadPool.isTerminated()){
            Thread.sleep(200);
        }

        System.out.println("Finished all threads");
    }

    @Test
    public void testAnything() {
        StringBuilder s = new StringBuilder();

        assertNotNull(s.toString());
    }

}
