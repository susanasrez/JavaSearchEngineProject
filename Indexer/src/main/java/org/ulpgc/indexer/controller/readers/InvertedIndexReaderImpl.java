package org.ulpgc.indexer.controller.readers;

import com.google.gson.Gson;
import org.ulpgc.indexer.controller.InvertedIndexReader;
import org.ulpgc.indexer.model.FileEvent;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndexReaderImpl implements InvertedIndexReader {
    private final String indexPath;

    public InvertedIndexReaderImpl() {
        this.indexPath = "invertedIndexDatamart";
    }

    @Override
    public Map<String, List<String>> get_inverted_index() {
        Map<String, List<String>> words = new HashMap<>();

        Path directory = Paths.get(indexPath + "/invertedIndex");
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(directory);
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    String fileName = entry.getFileName().toString();
                    List<String> lines = readLinesFromFile(entry);
                    words.put(fileName, lines);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return words;
    }

    @Override
    public List<FileEvent> get_saved_events() {
        List<FileEvent> events = new ArrayList<>();

        Path directory = Paths.get(indexPath + "/indexEvents");
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(directory);
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    String event = Files.readString(entry);
                    events.add((new Gson()).fromJson(event, FileEvent.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }

    private static List<String> readLinesFromFile(Path filePath) throws IOException {
        return Files.readAllLines(filePath);
    }
}
