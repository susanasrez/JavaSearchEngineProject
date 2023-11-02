package org.ulpgc.indexer.controller;

import org.ulpgc.indexer.controller.readers.ReaderThread;
import org.ulpgc.indexer.controller.indexers.IndexerThread;

import javax.jms.JMSException;

public class Controller {

   public static void run(String[] args) throws InterruptedException, JMSException {
       Thread reader = new ReaderThread("Indexer/src/main/resources/content",
               "Indexer/src/main/resources/readEvents",
               args[0]);
       reader.start();

        Thread indexer = new IndexerThread(
                "Indexer/src/main/resources/content",
                "invertedIndex",
                "/credentials.json");
        indexer.start();
   }
}
