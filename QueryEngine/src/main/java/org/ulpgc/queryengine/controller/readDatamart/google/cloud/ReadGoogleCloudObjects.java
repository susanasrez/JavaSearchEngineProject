package org.ulpgc.queryengine.controller.readDatamart.google.cloud;

import com.google.cloud.storage.Blob;
import org.ulpgc.queryengine.controller.exceptions.ObjectNotFoundException;
import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReaderFiles;
import org.ulpgc.queryengine.model.MetadataBook;
import org.ulpgc.queryengine.model.RecommendBook;
import org.ulpgc.queryengine.model.WordDocuments;
import org.ulpgc.queryengine.model.WordFrequency;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class ReadGoogleCloudObjects implements DatamartReaderFiles {

    @Override
    public List<String> get_documents(String word) throws ObjectNotFoundException {
        word = word.toLowerCase();
        Blob blob = ReadCloud.get_documents(word);

        if (blob == null) {
            throw new ObjectNotFoundException();
        }

        String content = new String(blob.getContent(), StandardCharsets.UTF_8);
        String[] documentArray = content.split("\n");

        List<String> documents = new ArrayList<>();
        Collections.addAll(documents, documentArray);

        return documents;
    }

    @Override
    public List<WordDocuments> getDocumentsWord(String param) {
        List<WordDocuments> documents = new ArrayList<>();

        String[] words= param.split(" ");
        for (String word : words){
            try {
                List<String> idDocuments = get_documents(word);
                WordDocuments wordDocuments = new WordDocuments(word, idDocuments);
                documents.add(wordDocuments);
            } catch (ObjectNotFoundException e){
                continue;
            }
        }
        return documents;
    }

    @Override
    public List<RecommendBook> getRecommendBook(String phrase) {
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

    private String getTitleForId(String id) {
        try {
            MetadataBook metadataBook = DatalakeReaderOneDrive.readMetadata(id);
            return metadataBook.title();
        } catch (Exception e) {
            e.printStackTrace();
            return "Title not found";
        }
    }

    @Override
    public WordFrequency getFrequency(String word){
        List<WordDocuments> wordDocumentsList = getDocumentsWord(word);

        int frequency = 0;
        for (WordDocuments wordDocuments : wordDocumentsList) {
            frequency += wordDocuments.documentsId().size();
        }

        return new WordFrequency(word, frequency);
    }

}
