package org.ulpgc.cleaner.controller;

import org.ulpgc.cleaner.controller.process.Cleaner;
import org.ulpgc.cleaner.controller.process.ParseFiles;
import org.ulpgc.cleaner.controller.reader.Reader;

public class Controller {
    public static void main(String[] args) {
        Reader reader = new Reader();
        ParseFiles parseFiles = new ParseFiles();
        Cleaner cleaner = new Cleaner();
        EventListener eventListener = new EventListener("datalake", reader, parseFiles, cleaner);
        eventListener.run();
    }
}
