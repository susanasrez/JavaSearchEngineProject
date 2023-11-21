package org.ulpgc.queryengine.view;

import com.google.gson.Gson;
import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.DatamartDataInterpreter;
import org.ulpgc.queryengine.controller.readDatamart.ReadDatamartStats;
import org.ulpgc.queryengine.model.*;

import java.util.List;

import static spark.Spark.*;
import static spark.Spark.port;

public class API {

    public static void runAPI(){
        port(8080);
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
        get("stats/total", (req, res) -> ReadDatamartStats.totalWords());
    }

    public static void getLen(){
        get("stats/length/:number", (req, res) -> {
            String number = req.params("number");
            return ReadDatamartStats.wordLength(number);
        });
    }

    private static void getWordDocuments() {
        get("/datamart/:word", (req, res) -> {
            String word = req.params("word");
            List<WordDocuments> documents = DatamartDataInterpreter.getDocumentsWord(word);
            return (new Gson()).toJson(documents);
        });
    }

    private static void getPhrase() {
        get("datamart-search/:phrase", (req, res) -> {
            String phrase = req.params("phrase");
            List<WordDocuments> documents = DatamartDataInterpreter.getDocumentsWord(phrase);
            return (new Gson()).toJson(documents);
        });
    }

    private static void getRecommendBook(){
        get("datamart-recommend/:phrase", (req, res) -> {
            String phrase = req.params("phrase");
            List<RecommendBook> book = DatamartDataInterpreter.getRecommendBook(phrase);
            return (new Gson()).toJson(book);
        });
    }

    private static void getFrequencyWord(){
        get("/stats/datamart-frequency/:word", (req, res) -> {
            String word = req.params("word");
            WordFrequency frequency = ReadDatamartStats.getFrequency(word);
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
