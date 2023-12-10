package org.ulpgc.queryengine.controller.readDatalake;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.ulpgc.queryengine.model.Book;
import org.ulpgc.queryengine.model.MetadataBook;

import java.io.IOException;

public class CleanerAPIClient {

    private static final OkHttpClient httpClient = new OkHttpClient();
    private final String ip;
    private static String cleanerBaseUrl;

    public CleanerAPIClient(String ip){
        this.ip = ip;
        baseUrl();
    }

    public void baseUrl(){
        cleanerBaseUrl = "http://" + ip + "/datalake";
    }

    public MetadataBook getMetadata(String idBook) throws IOException {
        String url = cleanerBaseUrl + "/metadata/" + idBook;
        String text = executeRequest(url);
        return MetadataExtractor.extractMetadata(text);
    }

    public Book getContent(String idBook) throws IOException {
        String url = cleanerBaseUrl + "/content/" + idBook;
        String jsonResponse = executeRequest(url);
        return new Book(idBook, jsonResponse);
    }

    public Book getRawBook(String idBook) throws IOException {
        String url = cleanerBaseUrl + "/books/" + idBook;
        String jsonResponse = executeRequest(url);
        return new Book(idBook, jsonResponse);
    }

    private String executeRequest(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

}
