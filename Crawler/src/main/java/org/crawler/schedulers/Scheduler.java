package org.crawler.schedulers;

import java.util.TimerTask;

public interface Scheduler {
    public void schedule(TimerTask task);
}
