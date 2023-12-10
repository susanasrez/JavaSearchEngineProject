package org.ulpgc.indexer.controller.indexers;

import org.ulpgc.indexer.model.FileEvent;

import java.io.IOException;

public interface InvertedIndexWriter {
    void saveDocumentEvent(FileEvent event) throws IOException;
    void saveWordDocument(String word, String fileName) throws IOException;
}
