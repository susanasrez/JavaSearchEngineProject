package org.ulpgc.indexer.controller.readers;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import org.ulpgc.indexer.Main;
import org.ulpgc.indexer.controller.InvertedIndexReader;
import org.ulpgc.indexer.model.FileEvent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class InvertedIndexReaderCloud implements InvertedIndexReader {

    private final String bucketName;
    private Storage storage;

    public InvertedIndexReaderCloud(){
        this.bucketName = "datamart_invertedindex";
    }
    @Override
    public Map<String, List<String>> get_inverted_index() throws IOException {
        Map<String, List<String>> invertedIndex  = new HashMap<>();

        obtain_credentials();
        Page<Blob> blobs = storage.list(bucketName);

        for (Blob blob : blobs.iterateAll()) {
            String blobName = blob.getName();
            if (blobName.startsWith("invertedIndex/")) {
                String key = blobName.split("/")[1];
                String content = new String(blob.getContent(), StandardCharsets.UTF_8);
                String[] documents = content.split("\n+");

                if (!invertedIndex.containsKey(key)) {
                    invertedIndex.put(key, new ArrayList<>());
                }

                for (String document : documents) {
                    invertedIndex.get(key).add(document);
                }
            }
        }

        return invertedIndex;
    }

    public void obtain_credentials() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                Main.class.getResourceAsStream("/credentials.json"));
        storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    @Override
    public List<FileEvent> get_saved_events() throws IOException {
        List<FileEvent> events = new ArrayList<>();
        obtain_credentials();

        Page<Blob> blobs = storage.list(bucketName);

        for (Blob blob : blobs.iterateAll()) {
            String blobName = blob.getName();
            if (blobName.startsWith("invertedIndexEvents/")) {
                String eventContent = new String(blob.getContent(), StandardCharsets.UTF_8);
                FileEvent event = (new Gson()).fromJson(eventContent, FileEvent.class);
                events.add(event);
            }
        }

        return events;
    }
}
