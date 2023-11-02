package org.ulpgc.indexer.controller.readers;

import com.google.gson.Gson;
import org.ulpgc.indexer.controller.Consumer;
import org.ulpgc.indexer.controller.Publisher;
import org.ulpgc.indexer.controller.message.broker.EventConsumer;
import org.ulpgc.indexer.controller.message.broker.EventPublisher;
import org.ulpgc.indexer.model.FileEvent;

import javax.jms.JMSException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

public class ReaderThread extends Thread {
    private final String contentPath;
    private final String eventPath;
    private final String listenedPath;
    private final Consumer eventConsumer;
    private final Publisher eventPublisher;

    public ReaderThread(String contentPath, String eventPath, String listenedPath) throws JMSException {
        this.contentPath = contentPath;
        this.eventPath = eventPath;
        this.listenedPath = listenedPath;
        this.eventConsumer = new EventConsumer("61616", "datalakeEvents");
        this.eventPublisher = new EventPublisher("61616", "readEvents");
    }

    public void run() {
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

        String content = ContentReader.readFileContent(filePath, listenedPath);
        writer.write(content);

        writer.close();
        file.close();
    }

}
