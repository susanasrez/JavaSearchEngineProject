package org.ulpgc.indexer.controller.processes.writers;

import org.ulpgc.indexer.controller.indexers.InvertedIndexWriter;
import org.ulpgc.indexer.controller.indexers.InvertedIndexHazelCastWriter;
import org.ulpgc.indexer.controller.message.broker.EventConsumer;
import org.ulpgc.indexer.controller.readers.ContentReader;
import org.ulpgc.indexer.model.FileEvent;

import javax.jms.JMSException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.Set;

public class IndexerThread extends Thread {
    private final String contentPath;
    private final EventConsumer eventConsumer;
    private final InvertedIndexWriter invertedIndexWriter;

    public IndexerThread(String contentPath, String credentialsJson, String apiURL) throws JMSException {
        this.contentPath = contentPath;
        this.invertedIndexWriter = new InvertedIndexHazelCastWriter();
        this.eventConsumer = new EventConsumer("443", "readEvents", apiURL);
    }

    public void run() {
        while (true) {
            String file = eventConsumer.getMessage();
            try {
                System.out.println("Indexing file " + file);
                indexDocument(Path.of(file));
                System.out.println("File " + file + " indexed");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
