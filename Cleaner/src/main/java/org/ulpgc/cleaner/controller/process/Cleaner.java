package org.ulpgc.cleaner.controller.process;

import org.ulpgc.cleaner.stopwords.StopwordRemover;
import java.util.List;

public class Cleaner {
    public String processFile(List<String> contentSplit) {
        String content = contentSplit.get(0);
        String metadata = contentSplit.get(1);

        String content_without_stopwords;
        content_without_stopwords = StopwordRemover.processFiles(metadata,content);

        StringBuilder processedContent = new StringBuilder();

        for (String word : content_without_stopwords.split(" ")) {
            String cleanedWord = remove_non_alphanumeric_characters(word);
            cleanedWord = remove_spaces_oneword(cleanedWord);
            cleanedWord = remove_repeated_chars(cleanedWord);
            processedContent.append(cleanedWord).append(" ");
        }
        return processedContent.toString();
    }

    public static String remove_non_alphanumeric_characters(String text) {
        return text.replaceAll("[^A-Za-z\\s]", "");
    }

    public static String remove_spaces_oneword(String text) {
        String[] words = text.split(" ");
        StringBuilder cleanedText = new StringBuilder();

        for (String word : words) {
            if (word.length() > 1) {
                cleanedText.append(word).append(" ");
            }
        }
        return cleanedText.toString().trim();
    }

    public static String remove_repeated_chars(String text) {
        return text.replaceAll("(\\w)\\1{2,}", ""); // Remove characters repeated 2 or more times
    }
}
