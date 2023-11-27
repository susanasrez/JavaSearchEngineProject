package org.ulpgc.queryengine.view;

import com.google.gson.Gson;
import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.DatamartCalculateStats;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReaderFiles;
import org.ulpgc.queryengine.model.*;

import java.util.List;

import static spark.Spark.*;
import static spark.Spark.port;

public class API {

    private static DatamartReaderFiles readDatamartFiles;
    private static DatamartCalculateStats readDatamartStats;

    public static void runAPI(DatamartReaderFiles obtainFiles, DatamartCalculateStats obtainStats, int port){
        readDatamartFiles = obtainFiles;
        readDatamartStats = obtainStats;
        port(port);
        getTotalWords();
        getLen();
        getWordDocuments();
        getPhrase();
        getRecommendBook();
        getFrequencyWord();
        getMetadata();
        getRawBook();
        getContent();
    }

    public static void getTotalWords(){
        get("stats/total", (req, res) -> readDatamartStats.totalWords());
    }

    public static void getLen(){
        get("stats/length/:number", (req, res) -> {
            String number = req.params("number");
            return readDatamartStats.wordLength(number);
        });
    }

    private static void getWordDocuments() {
        get("/datamart/:word", (req, res) -> {
            String word = req.params("word");
            List<WordDocuments> documents = readDatamartFiles.getDocumentsWord(word);
            return (new Gson()).toJson(documents);
        });
    }

    private static void getPhrase() {
        get("datamart-search/:phrase", (req, res) -> {
            String phrase = req.params("phrase");
            List<WordDocuments> documents = readDatamartFiles.getDocumentsWord(phrase);
            return (new Gson()).toJson(documents);
        });
    }

    private static void getRecommendBook(){
        get("datamart-recommend/:phrase", (req, res) -> {
            String phrase = req.params("phrase");
            List<RecommendBook> book = readDatamartFiles.getRecommendBook(phrase);
            return (new Gson()).toJson(book);
        });
    }

    private static void getFrequencyWord(){
        get("/stats/datamart-frequency/:word", (req, res) -> {
            String word = req.params("word");
            WordFrequency frequency = readDatamartFiles.getFrequency(word);
            return (new Gson()).toJson(frequency);
        });
    }

    private static void getMetadata(){
        get("/datalake/metadata/:idbook", (req, res) -> {
            String idbook = req.params("idbook") + ".metadata";
            MetadataBook metadata = DatalakeReaderOneDrive.readMetadata(idbook);
            return (new Gson()).toJson(metadata);
        });
    }

    private static void getRawBook(){
        get("/datalake/:idbook", (req, res) -> {
            String idbook = req.params("idbook") + ".txt";
            Book book = DatalakeReaderOneDrive.readRawBook(idbook);
            return (new Gson()).toJson(book);
        });
    }

    private static void getContent(){
        get("/datalake/content/:idbook", (req, res) -> {
            String idbook = req.params("idbook") + ".txt";
            String content = DatalakeReaderOneDrive.readContent(idbook);
            return (new Gson()).toJson(content);
        });
    }

}
