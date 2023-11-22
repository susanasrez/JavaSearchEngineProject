package org.ulpgc.queryengine.controller.readDatamart.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.ulpgc.queryengine.controller.readDatamart.DatamartCalculateStats;

public class ReadDatamartStats implements DatamartCalculateStats {

    private static String datamartPath;
    public ReadDatamartStats(String datamartPath){
        this.datamartPath = datamartPath;
    }

    @Override
    public JsonObject totalWords() {
        JsonObject jsonResult = new JsonObject();

        try {
            File folder = new File(datamartPath);
            File[] files = folder.listFiles();

            int fileCount = 0;
            if (files != null) {
                fileCount = files.length;
            }

            jsonResult.add("total_words", new JsonPrimitive(fileCount));

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        File folder = new File(datamartPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.length() == length) {
                        words.add(fileName);
                    }
                }
            }
        }
        JsonArray jsonArray = new JsonArray();
        for (String word : words) {
            jsonArray.add(word);
        }

        return jsonArray;
    }


}
