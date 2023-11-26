package org.ulpgc.queryengine.controller.readDatamart.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.ulpgc.queryengine.controller.exceptions.ObjectNotFoundException;
import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReaderFiles;
import org.ulpgc.queryengine.model.MetadataBook;
import org.ulpgc.queryengine.model.RecommendBook;
import org.ulpgc.queryengine.model.WordDocuments;
import org.ulpgc.queryengine.model.WordFrequency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadHazelcastWords implements DatamartReaderFiles {

    private HazelcastInstance hazelcastInstance = initialize();
    private final IMap<String, List<String>> hazelcastMap;

    public ReadHazelcastWords(){
        this.hazelcastMap = hazelcastInstance.getMap("datamart");
    }

    public HazelcastInstance initialize(){
        Config config = new Config();
        config.setInstanceName("instance");
        this.hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        return null;
    }

    @Override
    public List<String> get_documents(String word) {
        word = word.toLowerCase();
        List<String> documents = hazelcastMap.get(word);

        if (documents == null) {
            return new ArrayList<>();
        }

        return documents;
    }

    @Override
    public List<WordDocuments> getDocumentsWord(String param) throws ObjectNotFoundException {
        List<WordDocuments> documents = new ArrayList<>();

        String[] words= param.split("\\+");

        for (String word : words){
            List<String> idDocuments = get_documents(word);
            WordDocuments wordDocuments = new WordDocuments(word, idDocuments);
            documents.add(wordDocuments);

        }
        return documents;
    }

    @Override
    public List<RecommendBook> getRecommendBook(String phrase) throws ObjectNotFoundException {
        List<WordDocuments> wordDocumentsList = getDocumentsWord(phrase);
        Map<String, Integer> idCountMap = new HashMap<>();
        Map<String, String> idTitleMap = new HashMap<>();

        for (WordDocuments wordDocuments : wordDocumentsList) {
            for (String id : wordDocuments.documentsId()) {
                idCountMap.put(id, idCountMap.getOrDefault(id, 0) + 1);
                String title = getTitleForId(id);
                idTitleMap.put(id, title);
            }
        }

        int maxCount = 0;
        List<RecommendBook> mostRecommendedBooks = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : idCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostRecommendedBooks.clear();
                mostRecommendedBooks.add(new RecommendBook(idTitleMap.get(entry.getKey()), entry.getKey()));
            } else if (entry.getValue() == maxCount) {
                mostRecommendedBooks.add(new RecommendBook(idTitleMap.get(entry.getKey()), entry.getKey()));
            }
        }

        return mostRecommendedBooks;
    }

    @Override
    public WordFrequency getFrequency(String word) throws ObjectNotFoundException {
        List<WordDocuments> wordDocumentsList = getDocumentsWord(word);

        int frequency = 0;
        for (WordDocuments wordDocuments : wordDocumentsList) {
            frequency += wordDocuments.documentsId().size();
        }

        return new WordFrequency(word, frequency);
    }

    private static String getTitleForId(String id) {
        try {
            MetadataBook metadataBook = DatalakeReaderOneDrive.readMetadata(id);
            return metadataBook.title();
        } catch (Exception e) {
            e.printStackTrace();
            return "Title not found";
        }
    }
}
