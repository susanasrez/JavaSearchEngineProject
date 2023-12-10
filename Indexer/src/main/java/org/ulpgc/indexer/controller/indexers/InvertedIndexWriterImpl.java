package org.ulpgc.indexer.controller.indexers;

import com.google.gson.Gson;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.multimap.MultiMap;
import org.ulpgc.indexer.model.FileEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class InvertedIndexWriterImpl implements InvertedIndexWriter {
    private final String indexPath;
    private final MultiMap<Object, Object> invertedIndex;

    public InvertedIndexWriterImpl() {
        this.indexPath = "../invertedIndexDatamart";
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        HazelcastInstance client = HazelcastClient.newHazelcastClient();
        invertedIndex = client.getMultiMap("invertedIndex");
    }


    @Override
    public void saveDocumentEvent(FileEvent event) {
        try {
            FileWriter fileWriter = new FileWriter(indexPath + "/indexEvents/" + event.getFileName());
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write((new Gson()).toJson(event));

            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveWordDocument(String word, String fileName) {
        invertedIndex.put(word, fileName);
        System.out.println(invertedIndex.get(word));

        try {
            FileWriter fileWriter = new FileWriter(indexPath + "/invertedIndex/" + word, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write(fileName + "\n");

            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error found when indexing word: " + word);
        }
    }
}
