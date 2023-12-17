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
import java.util.Date;

public class ReaderFromAPIThread extends Thread {
    private final String contentPath;
    private final String eventPath;
    private final Consumer eventConsumer;
    private final Publisher eventPublisher;
    private final String apiUrl;

    public ReaderFromAPIThread(String contentPath, String eventPath, String apiUrl, String indexerId) throws JMSException {
        this.contentPath = contentPath;
        this.eventPath = eventPath;
        this.eventConsumer = new EventConsumer(Integer.toString(Main.SERVER_MQ_PORT), "cleanerEvents", apiUrl);
        this.eventPublisher = new EventPublisher(Integer.toString(Main.SERVER_MQ_PORT), "readEvents" + indexerId, apiUrl);
        this.apiUrl = apiUrl;
    }

    public void run() {
        readPreviousContent();
        readIncomingContent();
    }

    private void readIncomingContent() {
        while (true) {
            String file = eventConsumer.getMessage();
            try {
                System.out.println("Reading file " + file);
                addToEvents(Path.of(file));
                addToContent(Path.of(file));
                System.out.println("File " + file + " read");
                eventPublisher.publish(file);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void readPreviousContent() {
        for (String book: processedBooks()) {
            try {
                System.out.println("Reading file " + book);
                addToContent(Path.of(book));
                addToEvents(Path.of(book));
                System.out.println("File " + book + " read");

                eventPublisher.publish(book);
                Thread.sleep(1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String[] processedBooks() {
        return new Gson().fromJson(
                "[" + ContentReader.readContentFromAPI(
                        apiUrl + ":" + Main.SERVER_CLEANER_PORT + "/datalake/content") + "]", String[].class);
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