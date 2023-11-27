package org.ulpgc.queryengine.controller.readDatamart.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.multimap.MultiMap;
import org.ulpgc.queryengine.controller.exceptions.ObjectNotFoundException;
import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReaderFiles;
import org.ulpgc.queryengine.model.MetadataBook;
import org.ulpgc.queryengine.model.RecommendBook;
import org.ulpgc.queryengine.model.WordDocuments;
import org.ulpgc.queryengine.model.WordFrequency;

import java.util.*;

public class ReadHazelcastWords implements DatamartReaderFiles {

    private final HazelcastInstance client;
    private final MultiMap<Object, Object> hazelcastMap;

    public ReadHazelcastWords(){
        this.client = HazelcastClient.newHazelcastClient();
        this.hazelcastMap = client.getMultiMap("invertedIndex");
    }

    @Override
    public List<String> get_documents(String word) {
        word = word.toLowerCase();
        Collection<Object> documents = hazelcastMap.get(word);

        List<String> documentsStrings = new ArrayList<>();

        for (Object obj : documents) {
            documentsStrings.add((String) obj);
        }

        return documentsStrings;
    }

    @Override
    public List<WordDocuments> getDocumentsWord(String param) {
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
        Map<Object, Integer> idCountMap = new HashMap<>();
        Map<Object, String> idTitleMap = new HashMap<>();

        for (WordDocuments wordDocuments : wordDocumentsList) {
            for (Object id : wordDocuments.documentsId()) {
                idCountMap.put(id, idCountMap.getOrDefault(id, 0) + 1);
                String title = getTitleForId(id);
                idTitleMap.put(id, title);
            }
        }

        int maxCount = 0;
        List<RecommendBook> mostRecommendedBooks = new ArrayList<>();

        for (Map.Entry<Object, Integer> entry : idCountMap.entrySet()) {
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

    private static String getTitleForId(Object id) {
        try {
            MetadataBook metadataBook = DatalakeReaderOneDrive.readMetadata(id);
            return metadataBook.title();
        } catch (Exception e) {
            e.printStackTrace();
            return "Title not found";
        }
    }
}
