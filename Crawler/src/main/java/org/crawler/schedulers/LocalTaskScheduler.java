package org.crawler.schedulers;

import org.crawler.schedulerTasks.GutenbergTask;

import java.util.Timer;
import java.util.TimerTask;

public class LocalTaskScheduler {
    public void schedule(TimerTask task) {
        Timer timer = new Timer();
        long delay = 0;
        long period = 10 * 60 * 1000;
        timer.schedule(task, delay, period);
    }
}
