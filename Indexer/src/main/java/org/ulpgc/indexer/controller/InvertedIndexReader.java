package org.ulpgc.indexer.controller;

import org.ulpgc.indexer.model.FileEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface InvertedIndexReader {
    Map<String, List<String>> getInvertedIndex() throws IOException;
    List<FileEvent> getSavedEvents() throws IOException;
}
