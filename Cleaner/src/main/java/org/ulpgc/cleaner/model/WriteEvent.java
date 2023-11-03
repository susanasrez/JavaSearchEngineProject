package org.ulpgc.cleaner.model;

import java.util.Date;

public class WriteEvent {
    Date data;
    String fileName;

    public WriteEvent(Date data, String fileName) {
        this.data = data;
        this.fileName = fileName;
    }

    public Date getData() {
        return data;
    }

    public String getFileName() {
        return fileName;
    }
}
