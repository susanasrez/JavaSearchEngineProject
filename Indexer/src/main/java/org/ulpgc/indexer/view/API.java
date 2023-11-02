package org.ulpgc.indexer.view;

import com.google.gson.Gson;
import org.ulpgc.indexer.Main;
import org.ulpgc.indexer.controller.InvertedIndexReader;
import org.ulpgc.indexer.controller.readers.InvertedIndexReaderCloud;

import static spark.Spark.*;

public class API {
    public static InvertedIndexReader invertedIndexReader = new InvertedIndexReaderCloud();

    public static void runAPI() {
        port(8080);
        getIndexedDocuments();
        getInvertedIndexOf();
        getStatus();
    }

    private static void getIndexedDocuments() {
        get("/indexed-documents", (req, res) ->
                (new Gson()).toJson(invertedIndexReader.get_saved_events())
        );
    }

    private static void getInvertedIndexOf() {
        get("/inverted-index-of", (req, res) ->
                (new Gson()).toJson(invertedIndexReader.get_inverted_index())
        );
    }

    public static void getStatus() {
        get("/",
                (req, res) -> getPageContent());
    }

    private static String getPageContent() {
        StringBuilder pageContent = new StringBuilder();
        pageContent.append("INDEXER APPLICATION STATUS: Running")
                .append("<br>")
                .append("SESSION INDEXED FILES: ")
                .append(Main.indexedFiles)
                .append("<br>").append("TAKEN TIME: ")
                .append(Main.takenTimeToIndex)
                .append(" sec");

        return pageContent.toString();
    }

}
