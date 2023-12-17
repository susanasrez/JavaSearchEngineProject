package org.ulpgc.dataloader.controller.loader;

import com.google.gson.Gson;
import org.ulpgc.dataloader.Main;
import org.ulpgc.dataloader.controller.message.Publisher;
import org.ulpgc.dataloader.controller.message.broker.EventPublisher;
import org.ulpgc.dataloader.controller.readers.ContentReader;

import javax.jms.JMSException;

public class ContentEnqueue {
    private final Publisher eventPublisher;
    private final String apiUrl;

    public ContentEnqueue(String port, String apiUrl) throws JMSException {
        this.eventPublisher = new EventPublisher(port, "cleanerEvents", apiUrl);
        this.apiUrl = apiUrl;
    }

    public void execute() {
        String[] previousBooks = processedBooks();
        System.out.println("Books to add: " + previousBooks.length);
        for (String book: previousBooks) {
            try {
                System.out.println("Published book " + book);
                eventPublisher.publish(book);

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
}
