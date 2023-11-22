package org.ulpgc.queryengine.controller.readDatamart.google.cloud;

import com.google.cloud.storage.*;
import com.google.auth.oauth2.GoogleCredentials;
import org.ulpgc.queryengine.Main;

import java.io.IOException;
import java.util.Objects;

public class ReadCloud {

    private static Storage storage;

    public static void obtain_credentials() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                Objects.requireNonNull(Main.class.getResourceAsStream("/invertedindex-402-e2463ab080b9.json")));
        storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    public static Blob get_documents(String word){
        String bucketName = "datamart_invertedindex";
        BlobId blobId = BlobId.of(bucketName, "invertedIndex/" + word);
        return storage.get(blobId);
    }

    public static int countFilesInBucket(String bucketName) {
        Bucket bucket = storage.get(bucketName);
        if (bucket != null) {
            int count = 0;
            for (Blob ignored : bucket.list().iterateAll()) {
                count++;
            }
            return count;
        } else {
            System.out.println("Bucket " + bucketName + " not found.");
            return 0;
        }
    }

    public static Storage storage() {
        return storage;
    }
}
