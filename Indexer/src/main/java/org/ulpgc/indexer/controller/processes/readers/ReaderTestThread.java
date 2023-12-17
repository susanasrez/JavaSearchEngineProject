package org.ulpgc.indexer.controller.processes.readers;

import com.google.gson.Gson;
import org.ulpgc.indexer.Main;
import org.ulpgc.indexer.controller.message.Consumer;
import org.ulpgc.indexer.controller.message.Publisher;
import org.ulpgc.indexer.controller.message.broker.EventConsumer;
import org.ulpgc.indexer.controller.message.broker.EventPublisher;
import org.ulpgc.indexer.controller.readers.ContentReader;
import org.ulpgc.indexer.model.FileEvent;

import javax.jms.JMSException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;

public class ReaderTestThread extends Thread {
    private final String contentPath;
    private final String eventPath;
    private final Consumer eventConsumer;
    private final Publisher eventPublisher;
    private final String apiUrl;

    private int[] nbooks = {1, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100};

    public ReaderTestThread(String contentPath, String eventPath, String apiUrl, String indexerId) throws JMSException {
        this.contentPath = contentPath;
        this.eventPath = eventPath;
        this.eventConsumer = new EventConsumer(Integer.toString(Main.SERVER_MQ_PORT), "cleanerEvents", apiUrl);
        this.eventPublisher = new EventPublisher(Integer.toString(Main.SERVER_MQ_PORT), "readEvents" + indexerId, apiUrl);
        this.apiUrl = apiUrl;
    }

    public void run() {
        readIncomingContent();
    }

    private void readIncomingContent() {
        int count = 0;
        int pos = 0;

        long[] times = new long[nbooks.length];

        while (true) {
            if (pos == nbooks.length) break;
            System.out.println("Reading to " + nbooks[pos]);

            long start = System.currentTimeMillis();
            while (count < nbooks[pos]) {
                String file = eventConsumer.getMessage();
                try {
                    addToEvents(Path.of(file));
                    addToContent(Path.of(file));
                    eventPublisher.publish(file);
                    Main.INDEXED_BOOKS += 1;
                    Thread.sleep(1);

                    count++;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            long end = System.currentTimeMillis();

            times[pos] = end - start;

            System.out.println("Time took to read (" + nbooks[pos] + "): " + times[pos]);

            count = 0;
            pos += 1;
        }

        System.out.println("Reader.:" + Arrays.toString(times));
    }

    private void addToEvents(Path filePath) throws IOException {
        FileWriter file = new FileWriter(eventPath + "/" + filePath.getFileName());
        BufferedWriter writer = new BufferedWriter(file);

        FileEvent event = new FileEvent(new Date(), filePath.getFileName());
        writer.write((new Gson()).toJson(event));

        writer.close();
        file.close();
    }

    private void addToContent(Path filePath) throws Exception {
        FileWriter file = new FileWriter(contentPath + "/" + filePath.getFileName());
        BufferedWriter writer = new BufferedWriter(file);

        String fileId = filePath.getFileName().toString().split(".txt")[0];
        String content = ContentReader.readContentFromAPI(apiUrl + ":" + Main.SERVER_CLEANER_PORT + "/datalake/content/" + fileId);
        writer.write(content);

        writer.close();
        file.close();
    }
}
