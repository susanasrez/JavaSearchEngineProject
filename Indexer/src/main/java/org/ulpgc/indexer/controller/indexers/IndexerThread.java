package org.ulpgc.indexer.controller.indexers;

import org.ulpgc.indexer.Main;
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
    private final InvertedIndexCloud invertedIndexWriter;

    public IndexerThread(String contentPath, String credentialsJson) throws JMSException, IOException {
        this.contentPath = contentPath;
        this.invertedIndexWriter = new InvertedIndexCloud(credentialsJson);
        this.eventConsumer = new EventConsumer("61616", "readEvents");
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

    private void updateSystem(long start, long end) {
        Main.takenTimeToIndex += (double) (end - start)/1000;
        Main.indexedFiles += 1;
    }

    private void indexDocument(Path filePath) throws Exception {
        saveEventToDatamart(filePath.getFileName());
        Set<String> words = ContentReader.contentTokenize(filePath.getFileName(), contentPath);

        for (String word : words) {
            invertedIndexWriter.save_word_document(word, String.valueOf(filePath.getFileName()));
        }
    }

    private void saveEventToDatamart(Path filePath){
        invertedIndexWriter.save_document_event(new FileEvent(
                new Date(),
                filePath
        ));
    }

}
