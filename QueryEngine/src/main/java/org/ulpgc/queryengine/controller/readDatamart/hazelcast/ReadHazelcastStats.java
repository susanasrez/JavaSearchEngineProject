package org.ulpgc.queryengine.controller.readDatamart.hazelcast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.ulpgc.queryengine.controller.readDatamart.DatamartCalculateStats;

import java.util.ArrayList;
import java.util.List;

public class ReadHazelcastStats implements DatamartCalculateStats {

    private final HazelcastInstance hazelcastInstance;

    public ReadHazelcastStats(HazelcastInstance hazelcastInstance){
        this.hazelcastInstance = hazelcastInstance;
    }
    @Override
    public JsonObject totalWords() {
        JsonObject jsonResult = new JsonObject();
        IMap<String, List<String>> hazelcastMap = hazelcastInstance.getMap("NAME");

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
        IMap<String, List<String>> hazelcastMap = hazelcastInstance.getMap("tu_nombre_mapa_hazelcast");

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
