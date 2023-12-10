package org.ulpgc.indexer.controller.indexers;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.multimap.MultiMap;
import org.ulpgc.indexer.controller.InvertedIndexWriter;
import org.ulpgc.indexer.model.FileEvent;

public class HazelCastWriter implements InvertedIndexWriter {
    private final MultiMap<Object, Object> invertedIndex;

    public HazelCastWriter() {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        HazelcastInstance client = HazelcastClient.newHazelcastClient();
        invertedIndex = client.getMultiMap("invertedIndex");
    }

    @Override
    public void saveWordDocument(String word, String fileName) {
        invertedIndex.put(word, fileName);
    }

    @Override
    public void saveDocumentEvent(FileEvent event) {}
}
