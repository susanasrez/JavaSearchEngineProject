package org.ulpgc.queryengine.controller.readDatamart;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface DatamartCalculateStats {
    JsonObject totalWords();
    JsonObject wordLength(String number);
    JsonArray findWordsByLength(int length);
}
