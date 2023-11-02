package org.ulpgc.queryengine.controller.readDatamart;

import com.google.cloud.storage.Storage;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.StorageOptions;
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

}
