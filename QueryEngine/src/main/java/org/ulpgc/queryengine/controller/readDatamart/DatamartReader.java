package org.ulpgc.queryengine.controller.readDatamart;

import org.ulpgc.queryengine.controller.exceptions.ObjectNotFoundException;
import org.ulpgc.queryengine.model.RecommendBook;
import org.ulpgc.queryengine.model.WordDocuments;
import org.ulpgc.queryengine.model.WordFrequency;

import java.util.List;

public interface DatamartReader {
    List<String> get_documents(String word) throws ObjectNotFoundException;

    List<WordDocuments> getDocumentsWord(String param);

    List<RecommendBook> getRecommendBook(String phrase);

    WordFrequency getFrequency(String word);
}
