package org.ulpgc.cleaner.controller;

import org.ulpgc.cleaner.controller.process.Cleaner;
import org.ulpgc.cleaner.controller.process.ParseFiles;
import org.ulpgc.cleaner.controller.reader.Reader;

import javax.jms.JMSException;

public class Controller {
    public static void run(String datalakePath) {
        Reader reader = new Reader();
        ParseFiles parseFiles = new ParseFiles();
        Cleaner cleaner = new Cleaner();

        try {
            FileProcessor fileProcessor = new FileProcessor(datalakePath, reader, parseFiles, cleaner);
            fileProcessor.run();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
