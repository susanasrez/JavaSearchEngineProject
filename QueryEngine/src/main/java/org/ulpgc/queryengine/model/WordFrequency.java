package org.ulpgc.queryengine.model;

public class WordFrequency {
    private final String word;
    private final Integer frequency;

    public WordFrequency(String word, Integer frequency) {
        this.word = word;
        this.frequency = frequency;
    }
}
