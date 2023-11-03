package org.ulpgc.cleaner.stopwords;

import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CustomStopwordProvider implements StopwordProvider {
    private String stopwordsFilePath;

    public CustomStopwordProvider(String stopwordsFilePath) {
        this.stopwordsFilePath = stopwordsFilePath;
    }

    @Override
    public Set<String> getStopwords() {
        Set<String> stopwords = new HashSet<>();
        try {
            Scanner scanner = new Scanner(new File("Cleaner/src/main/resources/" + stopwordsFilePath));
            while (scanner.hasNext()) {
                stopwords.add(scanner.next().toLowerCase());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopwords;
    }
}