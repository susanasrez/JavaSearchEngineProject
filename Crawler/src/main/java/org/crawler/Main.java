package org.crawler;

import org.crawler.api.CrawlerAPI;
import org.crawler.controllers.CrawlerController;

public class Main {
    public static void main(String[] args) {
        CrawlerController controller = new CrawlerController();
        CrawlerAPI.runAPI();
        controller.run();
    }
}