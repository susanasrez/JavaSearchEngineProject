package org.ulpgc.queryengine.controller.readDatamart.hazelcast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.ulpgc.queryengine.controller.readDatamart.DatamartCalculateStats;

import java.util.ArrayList;
import java.util.List;

public class ReadHazelcastStats implements DatamartCalculateStats {

    private final IMap<String, List<String>> hazelcastMap;

    public ReadHazelcastStats(HazelcastInstance hazelcastInstance){
        this.hazelcastMap = hazelcastInstance.getMap("datamart");
    }
    @Override
    public JsonObject totalWords() {
        JsonObject result = new JsonObject();
        int size = hazelcastMap.size();
        result.addProperty("total_files", size);
        return result;
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
