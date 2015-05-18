package com.infusionsoft.threads;

import java.text.MessageFormat;
import java.util.concurrent.*;

/**
 * Created by chad on 5/14/15.
 */
public class RunNowScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor implements RunNowScheduledExecutorService{

    public RunNowScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public RunNowScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public RunNowScheduledThreadPoolExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public RunNowScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    public void runNow() {

        BlockingQueue<Runnable> tasks = getQueue();

        for (Runnable task : tasks) {
            if (task instanceof ScheduledFuture) {
                ScheduledFuture future = (ScheduledFuture) task;
                if (!future.isDone() && !future.isCancelled()) {
                    System.out.println(MessageFormat.format("Executing scheduled task now ({0}).", task.toString()));
                    execute(task); //doesn't work...queue keeps growing...need to use RunNow2 implementation
                } else {
                    System.out.println(MessageFormat.format("Unable to run scheduled task now because it is either done or cancelled ({0}).", task.toString()));
                }
            }
        }
    }

}