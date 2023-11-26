package googleCloud;

import org.ulpgc.queryengine.controller.exceptions.ObjectNotFoundException;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReaderFiles;
import org.ulpgc.queryengine.controller.readDatamart.google.cloud.ReadCloud;
import org.ulpgc.queryengine.controller.readDatamart.google.cloud.ReadGoogleCloudObjects;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TimeObjects {

    public static long startTime, endTime, duration;
    private static final DatamartReaderFiles datamartReaderFiles = new ReadGoogleCloudObjects();

    public static void main(String[] args) throws ObjectNotFoundException, IOException {
        List<String> words = Arrays.asList(
                "abandonedly", "actor", "boost", "cieled", "lifeas",
                "purple", "jubilant", "sapphire", "laughter", "serenity",
                "magnificent", "whisper", "enchantment", "tranquility", "harmony",
                "fascination", "blossom", "twilight", "cascade", "luminous"
        );

        ReadCloud.obtain_credentials();

        System.out.println("Time per word");
        for (String word : words) {
            measureTimeWord(word);
        }

        System.out.println("Time per phrase recommend");
        StringBuilder wordsForIteration = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            wordsForIteration.append(words.get(i));

            measureTimeWord(wordsForIteration.toString());
            measureTimeRecommend(wordsForIteration.toString());

            if (i < words.size() - 1) {
                wordsForIteration.append("+");
            }
        }

        System.out.println("Time per frequency word");
        for (String word : words) {
            measureFrequency(word);
        }
    }



    public static void measureTimeWord(String word) throws ObjectNotFoundException {
        startTime = System.currentTimeMillis();
        datamartReaderFiles.getDocumentsWord(word);
        endTime =  System.currentTimeMillis();
        duration = (endTime - startTime);
        System.out.println("Word: " + word + ", Time: " + duration + " ms");
    }

    public static void measureTimeRecommend(String phrase) throws ObjectNotFoundException {
        startTime = System.currentTimeMillis();
        datamartReaderFiles.getRecommendBook(phrase);
        endTime =  System.currentTimeMillis();
        duration = (endTime - startTime);
        System.out.println("Word: " + phrase + ", Time: " + duration + " ms");
    }

    public static void measureFrequency(String word) throws ObjectNotFoundException {
        startTime = System.currentTimeMillis();
        datamartReaderFiles.getFrequency(word);
        endTime =  System.currentTimeMillis();
        duration = (endTime - startTime);
        System.out.println("Word: " + word + ", Time: " + duration + " ms");
    }

}
