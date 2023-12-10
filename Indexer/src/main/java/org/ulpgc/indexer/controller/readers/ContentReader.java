package org.ulpgc.indexer.controller.readers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContentReader {

    public static Set<String> contentTokenize(Path filePath, String contentPath) throws Exception {
        List<String> content = Arrays.asList(readFileContent(filePath, contentPath).split(" "));
        return new HashSet<>(content);
    }

    public static String readFileContent(Path filePath, String dir) throws Exception {
        Path relative = Path.of(dir + "/" + filePath);
        byte[] content = Files.readAllBytes(relative);
        return new String(content);
    }

    public static String readContentFromAPI(String url) {
        try {
            URL urlObject = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Document document = Jsoup.parse(connection.getInputStream(), "UTF-8", url);
                Element body = document.body();

                connection.disconnect();
                return body.text().substring(1, body.text().length() - 2);

            } else {
                throw new RuntimeException("Impossible to access file");
            }

        } catch (Exception e) {
            throw new RuntimeException("Impossible to access file");
        }
    }
}
