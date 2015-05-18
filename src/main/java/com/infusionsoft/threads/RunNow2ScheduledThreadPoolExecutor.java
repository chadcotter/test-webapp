package com.infusionsoft.threads;

import java.util.concurrent.*;

/**
 * Created by chad on 5/8/15.
 */
public class RunNow2ScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor implements  RunNowScheduledExecutorService {

    public RunNow2ScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public RunNow2ScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public RunNow2ScheduledThreadPoolExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public RunNow2ScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable r, RunnableScheduledFuture<V> task) {
        return new CustomRunnableScheduledFuture<V>(r, task);
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> c, RunnableScheduledFuture<V> task) {
        return new CustomRunnableScheduledFuture<V>(c, task);
    }

    /***
     * CustomRunnableScheduledFuture so we can get the original Runnable or Callable directly
     *
     * @param <V> The result type returned by this Future's <tt>get</tt> method
     */
    public static class CustomRunnableScheduledFuture<V> implements RunnableScheduledFuture<V> {

        private RunnableScheduledFuture<V> task;
        private Runnable runnable;
        private Callable<V> callable;

        public CustomRunnableScheduledFuture(Runnable runnable, RunnableScheduledFuture<V> task) {
            this.runnable = runnable;
            this.task = task;
        }

        public CustomRunnableScheduledFuture(Callable<V> callable, RunnableScheduledFuture<V> task) {
            this.callable = callable;
            this.task = task;
        }

        @Override
        public boolean isPeriodic() {
            return task.isPeriodic();
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return task.getDelay(unit);
        }

        @Override
        public int compareTo(Delayed o) {
            return task.compareTo(o);
        }

        @Override
        public void run() {
            task.run();
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return task.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            return task.isCancelled();
        }

        @Override
        public boolean isDone() {
            return task.isDone();
        }

        @Override
        public V get() throws InterruptedException, ExecutionException {
            return task.get();
        }

        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return task.get(timeout, unit);
        }

        public Runnable getRunnable() {
            return runnable;
        }

        public Callable<V> getCallable() {
            return callable;
        }

        public boolean isCallable() {
            return callable != null;
        }

        public boolean isRunnable() {
            return runnable != null;
        }
    }


    @Override
    public void runNow() {

        BlockingQueue<Runnable> tasks = getQueue();

        for (Runnable task : tasks) {

            if (task instanceof CustomRunnableScheduledFuture) {

                CustomRunnableScheduledFuture customTask = (CustomRunnableScheduledFuture) task;

                if (!customTask.isDone() && !customTask.isCancelled()) {
                    executeNow(customTask);
                }
            }
        }
    }

    private void executeNow(CustomRunnableScheduledFuture customTask) {
        if (customTask.isRunnable()) {
            this.execute(customTask.getRunnable());
        } else if (customTask.isCallable()) {
            this.submit(customTask.getCallable());
        } else {
            //log error
            System.out.println("oops...this will never happen");
        }
    }

}
