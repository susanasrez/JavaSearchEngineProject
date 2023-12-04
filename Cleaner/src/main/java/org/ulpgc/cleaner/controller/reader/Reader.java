package org.ulpgc.cleaner.controller.reader;

import java.io.*;
import java.nio.file.Path;
import org.ulpgc.cleaner.Main; // Importa la clase Main para usar la ruta del datalake

public class Reader {

    public String readFile(Path filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        try (FileReader fileReader = new FileReader(filePath.toString());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    public String getMetadata(String idBook) {
        Path filePath = Path.of(Main.datalakePath + "/Metadata/" + idBook);
        try {
            return readFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getContent(String idBook) {
        Path filePath = Path.of(Main.datalakePath + "/Content/" + idBook);
        try {
            return readFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRawBook(String idBook) {
        Path filePath = Path.of(Main.datalakePath + "/RawBooks/" + idBook);
        try {
            return readFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}