package org.crawler.api;

import static spark.Spark.*;

public class CrawlerAPI {
    public static void runAPI() {
        port(8080);
        get("/crawler", (req, res) -> "test");
    }
}
