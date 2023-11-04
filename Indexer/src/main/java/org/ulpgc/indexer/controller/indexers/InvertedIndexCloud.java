package org.ulpgc.indexer.controller.indexers;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.gson.Gson;
import org.ulpgc.indexer.Main;
import org.ulpgc.indexer.controller.InvertedIndexWriter;
import org.ulpgc.indexer.model.FileEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class InvertedIndexCloud implements InvertedIndexWriter {

    private final String bucketName;
    private Storage storage;

    public InvertedIndexCloud(String credentialsJson) throws IOException {
        this.bucketName = "datamart_invertedindex";
        obtain_credentials(credentialsJson);
    }

    public void obtain_credentials(String credentialsJson) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                Main.class.getResourceAsStream(credentialsJson));
        storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    @Override
    public void save_document_event(FileEvent event){
        String blobName = "invertedIndexEvents/" + event.getFileName();
        BlobId blobId = BlobId.of(bucketName, blobName);

        try {
            Blob blob = storage.get(blobId);
            byte[] content;
            if (blob != null) {
                content = addToDatamart(blob, (new Gson()).toJson(event));
            } else {
                content = createInDatamart((new Gson()).toJson(event));
            }
            createOrUpdateBlob(blobId, content);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void save_word_document(String word, String fileName){
        String blobName = "invertedIndex/" + word;
        BlobId blobId = BlobId.of(bucketName, blobName);

        try {
            Blob blob = storage.get(blobId);
            byte[] content;
            if (blob != null) {
                content = addToDatamart(blob, fileName);
            } else {
                content = createInDatamart(fileName);
            }
            createOrUpdateBlob(blobId, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] createInDatamart(String fileName) {
        return (fileName + "\n").getBytes(StandardCharsets.UTF_8);
    }

    public byte[] addToDatamart(Blob blob, String fileName){
        byte[] existingData = blob.getContent();
        byte[] newData = (fileName + "\n").getBytes(StandardCharsets.UTF_8);
        byte[] content = new byte[existingData.length + newData.length];
        System.arraycopy(existingData, 0, content, 0, existingData.length);
        System.arraycopy(newData, 0, content, existingData.length, newData.length);
        return content;
    }

    public void createOrUpdateBlob(BlobId blobId, byte[] content){
        ByteArrayInputStream contentStream = new ByteArrayInputStream(content);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, contentStream);
    }
}
