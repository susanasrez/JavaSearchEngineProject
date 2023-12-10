package org.ulpgc.indexer.controller;

import org.ulpgc.indexer.controller.processes.writers.IndexerThread;
import org.ulpgc.indexer.controller.processes.readers.ReaderFromAPIThread;

import javax.jms.JMSException;
import java.io.IOException;

public class Controller {

   public static void run(String datalakePath, String contentApiUrl) throws JMSException, IOException {
       Thread reader = new ReaderFromAPIThread("./src/main/resources/content",
               "./src/main/resources/readEvents",
               datalakePath,
               contentApiUrl);
       reader.start();

        Thread indexer = new IndexerThread(
                "./src/main/resources/content",
                "/credentials.json");
        indexer.start();
   }
}
