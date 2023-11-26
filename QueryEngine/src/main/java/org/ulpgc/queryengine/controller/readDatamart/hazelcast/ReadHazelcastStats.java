package org.ulpgc.queryengine.controller.readDatamart.hazelcast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.ulpgc.queryengine.controller.readDatamart.DatamartCalculateStats;

import java.util.ArrayList;
import java.util.List;

public class ReadHazelcastStats implements DatamartCalculateStats {

    private HazelcastInstance hazelcastInstance = initialize();
    private final IMap<String, List<String>> hazelcastMap;

    public ReadHazelcastStats(){
        this.hazelcastMap = hazelcastInstance.getMap("datamart");
    }

    public HazelcastInstance initialize(){
        Config config = new Config();
        config.setInstanceName("instance");
        this.hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        return null;
    }
    @Override
    public JsonObject totalWords() {
        JsonObject jsonResult = new JsonObject();

        int total = hazelcastMap.size();
        jsonResult.addProperty("total", total);

        return jsonResult;
    }

    @Override
    public JsonObject wordLength(String number){
        JsonObject jsonResult = new JsonObject();
        JsonArray words = findWordsByLength(Integer.parseInt(number));
        jsonResult.addProperty("words-by-length", String.valueOf(words));
        jsonResult.addProperty("total", words.size());

        return jsonResult;
    }

    @Override
    public JsonArray findWordsByLength(int length) {
        List<String> words = new ArrayList<>();

        for (String word : hazelcastMap.keySet()) {
            if (word.length() == length) {
                words.add(word);
            }
        }

        JsonArray jsonArray = new JsonArray();
        for (String word : words) {
            jsonArray.add(word);
        }

        return jsonArray;
    }
}
