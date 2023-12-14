package org.ulpgc.indexer;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.multimap.MultiMap;

import static spark.Spark.get;
import static spark.Spark.port;

public class API {
    private final MultiMap<Object, Object> invertedIndex;

    public API() {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        HazelcastInstance client = HazelcastClient.newHazelcastClient();
        invertedIndex = client.getMultiMap("invertedIndex");
    }

    public void run() {
        port(8081);
        get("word/:word", (req, res) -> invertedIndex.get(req.params("word")));
    }
}
