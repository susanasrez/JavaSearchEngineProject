package org.ulpgc.cleaner.controller;

import org.ulpgc.cleaner.controller.process.Cleaner;
import org.ulpgc.cleaner.controller.process.ParseFiles;
import org.ulpgc.cleaner.controller.reader.Reader;
import org.ulpgc.cleaner.controller.writer.Writer;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class EventListener {
    private final String listenedPath;
    private final Reader reader;
    private final ParseFiles parseFiles;
    private final Cleaner cleaner;

    public EventListener(String listenedPath, Reader reader, ParseFiles parseFiles, Cleaner cleaner) {
        this.listenedPath = listenedPath;
        this.reader = reader;
        this.parseFiles = parseFiles;
        this.cleaner = cleaner;
    }

    public void run() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path directoryToWatch = Paths.get(listenedPath);

            directoryToWatch.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while (true) {
                try {
                    makeActionWhenListened(watchService);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeActionWhenListened(WatchService watchService) throws Exception {
        Path addedFilePath = listenReadEvents(watchService);
        executeAction(addedFilePath);
    }

    private Path listenReadEvents(WatchService watchService) throws InterruptedException {
        WatchKey key;
        key = watchService.take();

        for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind<?> kind = event.kind();

            @SuppressWarnings("unchecked")
            WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
            Path fileName = pathEvent.context();

            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                key.reset();
                return fileName;
            }
        }

        key.reset();
        return null;
    }

    protected void executeAction(Path filePath)  throws Exception {
        String book = reader.readFile(filePath);
        List<String> content = parseFiles.processFile(book, String.valueOf(filePath.getFileName()));
        String cleaned_Content = cleaner.processFile(content);
        Writer.writeToDatalake(cleaned_Content, String.valueOf(filePath));
        Writer.writeMetadataToDatalake(content.get(1), String.valueOf(filePath));
        Writer.writeEvent(String.valueOf(filePath));
    }
}

