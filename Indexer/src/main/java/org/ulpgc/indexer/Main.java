package org.ulpgc.indexer;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.multimap.MultiMap;
import org.ulpgc.indexer.controller.Controller;
import org.ulpgc.indexer.view.API;

public class Main {
    public static int indexedFiles = 0;
    public static double takenTimeToIndex = 0;

    public static void main(String[] args) {
        try {
            Controller.run(args[0]);
            API.runAPI();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
