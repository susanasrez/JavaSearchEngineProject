package org.ulpgc.indexer.model;

import java.nio.file.Path;
import java.util.Date;

public class FileEvent {
    String date;
    String fileName;

    public FileEvent(Date date, Path fileName) {
        this.date = date.toString();
        this.fileName = fileName.toString();
    }

    public String getDate() {
        return date;
    }

    public String getFileName() {
        return fileName;
    }
}
