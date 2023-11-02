package org.ulpgc.indexer;

import org.ulpgc.indexer.controller.Controller;
import org.ulpgc.indexer.controller.indexers.IndexerThread;
import org.ulpgc.indexer.controller.message.broker.EventPublisher;
import org.ulpgc.indexer.controller.readers.ReaderThread;
import org.ulpgc.indexer.view.API;

import javax.jms.JMSException;
import java.io.IOException;

public class Main {
    public static int indexedFiles = 0;
    public static double takenTimeToIndex = 0;

    public static void main(String[] args) {
        try {
            Controller.run(args);
            API.runAPI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
