package org.ulpgc.queryengine.controller.readDatamart.hazelcast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.multimap.MultiMap;
import org.ulpgc.queryengine.controller.readDatamart.DatamartCalculateStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReadHazelcastStats implements DatamartCalculateStats {

    private HazelcastInstance client;
    private final MultiMap<Object, Object> hazelcastMap;

    public ReadHazelcastStats(){
        this.client = HazelcastClient.newHazelcastClient();
        this.hazelcastMap = client.getMultiMap("invertedIndex");
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

        for (Object key : hazelcastMap.keySet()) {
            if (key instanceof String && ((String) key).length() == length) {
                words.add((String) key);
            }
        }

        JsonArray jsonArray = new JsonArray();
        for (String word : words) {
            jsonArray.add(word);
        }

        return jsonArray;
    }
}
