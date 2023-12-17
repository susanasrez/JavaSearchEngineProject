package org.ulpgc.indexer.controller.indexers;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.multimap.MultiMap;
import org.ulpgc.indexer.model.FileEvent;

public class InvertedIndexHazelCastWriter implements InvertedIndexWriter {
    private final MultiMap<Object, Object> invertedIndex;

    public InvertedIndexHazelCastWriter() {
        //Config config = new Config();
        //config.getNetworkConfig().setPublicAddress("34.16.155.243");
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
