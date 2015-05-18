package com.infusionsoft.threads;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by chad on 5/8/15.
 */
public interface RunNowScheduledExecutorService extends ScheduledExecutorService {

    public void runNow();

}
