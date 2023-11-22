package org.ulpgc.queryengine.controller.readDatamart;

import org.ulpgc.queryengine.controller.exceptions.ObjectNotFoundException;
import org.ulpgc.queryengine.model.RecommendBook;
import org.ulpgc.queryengine.model.WordDocuments;
import org.ulpgc.queryengine.model.WordFrequency;

import java.util.List;

public interface DatamartReaderFiles {
    List<String> get_documents(String word) throws ObjectNotFoundException;

    List<WordDocuments> getDocumentsWord(String param) throws ObjectNotFoundException;

    List<RecommendBook> getRecommendBook(String phrase) throws ObjectNotFoundException;

    WordFrequency getFrequency(String word) throws ObjectNotFoundException;

}
