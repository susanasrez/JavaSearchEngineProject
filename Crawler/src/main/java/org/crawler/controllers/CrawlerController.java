package org.crawler.controllers;

import org.crawler.downloadHandlers.GutenbergDownloadHandler;
import org.crawler.fileHandlers.LocalFileHandler;
import org.crawler.schedulerTasks.GutenbergTask;
import org.crawler.schedulers.LocalTaskScheduler;
import org.crawler.schedulers.Scheduler;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class CrawlerController {

    public void run() {
        LocalTaskScheduler scheduler = new LocalTaskScheduler();
        scheduler.schedule(new GutenbergTask());
    }
}
