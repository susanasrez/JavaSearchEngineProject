package org.ulpgc.indexer.controller;

import org.ulpgc.indexer.controller.readers.ReaderThread;
import org.ulpgc.indexer.controller.indexers.IndexerThread;

import javax.jms.JMSException;
import java.io.IOException;

public class Controller {

   public static void run(String[] args) throws JMSException, IOException {
       Thread reader = new ReaderThread("./src/main/resources/content",
               "./src/main/resources/readEvents",
               args[0]);
       reader.start();

        Thread indexer = new IndexerThread(
                "./src/main/resources/content",
                "/credentials.json");
        indexer.start();
   }
}
