package org.ulpgc.dataloader;

import org.ulpgc.dataloader.controller.loader.ContentEnqueue;

import javax.jms.JMSException;

public class Main {
    public static final int SERVER_CLEANER_PORT = 80;

    public static void main(String[] args) throws JMSException {
        ContentEnqueue contentEnqueue = new ContentEnqueue(args[1], args[0]);
        contentEnqueue.execute();
    }
}