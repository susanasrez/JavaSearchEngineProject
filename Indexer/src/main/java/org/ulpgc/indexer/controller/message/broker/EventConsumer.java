package org.ulpgc.indexer.controller.message.broker;

import org.apache.activemq.ActiveMQConnectionFactory;

import org.ulpgc.indexer.controller.message.Consumer;

import javax.jms.*;
import java.util.ArrayList;

public class EventConsumer implements Consumer {
    private final Connection connection;
    private final Session session;
    private final MessageConsumer consumer;


    public EventConsumer(String port, String queue, String apiURL) throws JMSException {
        String apiIP = apiURL.substring(7);
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + apiIP + ":" + port);
        this.connection = factory.createConnection();
        this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queue);
        connection.start();
        this.consumer = session.createConsumer(destination);
        System.out.println("Connection estabilished");
    }


    @Override
    public String getMessage() {
        try {
            while (true) {
                Message message = consumer.receive();
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("Message recieved");
                    return textMessage.getText();
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }


        return null;
    }

    public void closeConnection() throws JMSException {
        consumer.close();
        session.close();
        connection.close();
    }
}