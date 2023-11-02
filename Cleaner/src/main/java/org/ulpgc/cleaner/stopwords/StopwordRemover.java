package org.ulpgc.cleaner.stopwords;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class StopwordRemover {
    public static String processFiles(String metadata, String content) {
        String language = getLanguageFromMetadata(metadata);
        Set<String> stopwords = getStopwordsForLanguage(language);
        String filteredContent;
        filteredContent = removeStopwords(content, stopwords);
        return filteredContent;
    }

    public static String getLanguageFromMetadata(String metadata) {
        int startIndex = metadata.indexOf("Language: ");
        if (startIndex != -1) {
            int endIndex = metadata.indexOf('\n', startIndex);
            if (endIndex != -1) {
                String line = metadata.substring(startIndex, endIndex);
                return line.substring("Language: ".length()).trim();
            }
        }
        return "Unknown";
    }

    public static Set<String> getStopwordsForLanguage(String language) {
        if ("English".equalsIgnoreCase(language)) {
            return new CustomStopwordProvider("stopwords/EnglishStopwords.txt").getStopwords();
        } else if ("Spanish".equalsIgnoreCase(language)) {
            return new CustomStopwordProvider("stopwords/SpanishStopwords.txt").getStopwords();
        } else if ("Italian".equalsIgnoreCase(language)) {
            return new CustomStopwordProvider("stopwords/ItalianStopwords.txt").getStopwords();
        } else if ("French".equalsIgnoreCase(language)) {
            return new CustomStopwordProvider("stopwords/FrenchStopwords.txt").getStopwords();
        } else if ("Portuguese".equalsIgnoreCase(language)) {
            return new CustomStopwordProvider("stopwords/PortugueseStopwords.txt").getStopwords();
        } else {
            return new HashSet<>();
        }
    }

    public static String removeStopwords(String content, Set<String> stopwords) {
        StringBuilder filteredContent = new StringBuilder();
        try (Scanner scanner = new Scanner(content)) {
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();
                if (!stopwords.contains(word)) {
                    filteredContent.append(word).append(" ");
                }
            }
        }
        return filteredContent.toString();
    }
}
