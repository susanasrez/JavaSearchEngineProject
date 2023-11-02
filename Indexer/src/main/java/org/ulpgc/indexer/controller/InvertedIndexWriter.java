package org.ulpgc.indexer.controller;

import org.ulpgc.indexer.model.FileEvent;

import java.io.IOException;

public interface InvertedIndexWriter {
    void save_document_event(FileEvent event) throws IOException;
    void save_word_document(String word, String fileName) throws IOException;
}
