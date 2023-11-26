package org.ulpgc.queryengine.controller.readDatamart.google.cloud;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ulpgc.queryengine.controller.readDatamart.DatamartCalculateStats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadGoogleCloudStats implements DatamartCalculateStats {
    @Override
    public JsonObject totalWords() {
        JsonObject result = new JsonObject();

        String bucketName = "datamart_invertedindex";
        String folderName = "invertedIndex";

        try {
            ReadCloud.obtain_credentials();
            int fileCount = ReadCloud.countFilesInFolder(bucketName, folderName);
            result.addProperty("total_files", fileCount);
        } catch (IOException e) {
            e.printStackTrace();
            result.addProperty("error", "Error getting the number of files in the bucket.");
        }

        return result;
    }

    @Override
    public JsonObject wordLength(String number) {
        JsonObject jsonResult = new JsonObject();
        int wordLength = Integer.parseInt(number);

        String bucketName = "datamart_invertedindex";
        String folderName = "invertedIndex";
        int wordsCount = 0;
        List<String> wordsByLength = new ArrayList<>();

        Bucket bucket = ReadCloud.storage().get(bucketName);
        if (bucket != null) {
            Page<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(folderName + "/"));
            for (Blob blob : blobs.iterateAll()) {
                String word = blob.getName();
                if (word.length() == wordLength) {
                    wordsByLength.add(word);
                    wordsCount++;
                }
            }

            jsonResult.addProperty("words-by-length", String.valueOf(wordsByLength));
            jsonResult.addProperty("total", wordsCount);
        } else {
            jsonResult.addProperty("error", "Bucket " + bucketName + " not found.");
        }

        return jsonResult;
    }

    @Override
    public JsonArray findWordsByLength(int length) {
        List<String> words = new ArrayList<>();

        String bucketName = "datamart_invertedindex";
        Storage storage = ReadCloud.storage();
        Bucket bucket = storage.get(bucketName);

        if (bucket != null) {
            Page<Blob> blobs = bucket.list();
            for (Blob blob : blobs.iterateAll()) {
                String fileName = blob.getName();
                if (fileName.length() == length) {
                    words.add(fileName);
                }
            }
        } else {
            System.out.println("Bucket " + bucketName + " not found.");
        }

        JsonArray jsonArray = new JsonArray();
        for (String word : words) {
            jsonArray.add(word);
        }

        return jsonArray;
    }

}
