package org.ulpgc.queryengine.model;

import java.util.List;

public class WordDocuments {
    private final String word;
    private final List<String> documentsId;

    public WordDocuments(String word,List<String> documents){
        this.word = word;
        this.documentsId =documents;
    }

    public String word() {
        return word;
    }

    public List<String> documentsId() {
        return documentsId;
    }

}
