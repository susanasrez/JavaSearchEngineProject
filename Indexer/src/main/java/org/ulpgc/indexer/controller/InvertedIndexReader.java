package org.ulpgc.indexer.controller;

import org.ulpgc.indexer.model.FileEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface InvertedIndexReader {
    Map<String, List<String>> get_inverted_index() throws IOException;
    List<FileEvent> get_saved_events() throws IOException;
}
