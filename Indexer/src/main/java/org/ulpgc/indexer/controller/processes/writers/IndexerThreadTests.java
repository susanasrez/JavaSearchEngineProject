package org.ulpgc.indexer.controller.processes.writers;

import org.ulpgc.indexer.Main;
import org.ulpgc.indexer.controller.indexers.InvertedIndexHazelCastWriter;
import org.ulpgc.indexer.controller.indexers.InvertedIndexWriter;
import org.ulpgc.indexer.controller.message.broker.EventConsumer;
import org.ulpgc.indexer.controller.readers.ContentReader;
import org.ulpgc.indexer.model.FileEvent;

import javax.jms.JMSException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

public class IndexerThreadTests extends Thread {
    private final String contentPath;
    private final EventConsumer eventConsumer;
    private final InvertedIndexWriter invertedIndexWriter;

    private int[] nbooks = {1, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100};

    public IndexerThreadTests(String contentPath, String credentialsJson, String apiURL, String indexerId) throws JMSException {
        this.contentPath = contentPath;
        this.invertedIndexWriter = new InvertedIndexHazelCastWriter();
        this.eventConsumer = new EventConsumer(Integer.toString(Main.SERVER_MQ_PORT), "readEvents" + indexerId, apiURL);
    }

    public void run() {
        indexIncomingContent();
    }

    private void indexIncomingContent() {
        int count = 0;
        int pos = 0;

        long[] times = new long[nbooks.length];

        while (true) {
            if (pos == nbooks.length) break;

            System.out.println("Indexing to " + nbooks[pos]);

            long start = System.currentTimeMillis();
            while (count < nbooks[pos]) {
                String file = eventConsumer.getMessage();
                try {
                    indexDocument(Path.of(file));
                    Main.INDEXED_BOOKS += 1;
                    Thread.sleep(1);

                    count++;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            long end = System.currentTimeMillis();

            times[pos] = end - start;

            System.out.println("Time took to index (" + nbooks[pos] + "): " + times[pos]);

            count = 0;
            pos += 1;
        }

        System.out.println("Index.: " + Arrays.toString(times));
    }

    private void indexDocument(Path filePath) throws Exception {
        saveEventToDatamart(filePath.getFileName());
        Set<String> words = ContentReader.contentTokenize(filePath.getFileName(), contentPath);

        for (String word : words) {
            invertedIndexWriter.saveWordDocument(word, String.valueOf(filePath.getFileName()));
            invertedIndexWriter.saveWordDocument(word, String.valueOf(filePath.getFileName()));
        }
    }

    private void saveEventToDatamart(Path filePath) throws IOException {
        invertedIndexWriter.saveDocumentEvent(new FileEvent(
                new Date(),
                filePath
        ));
    }

}
