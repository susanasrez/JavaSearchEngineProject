package org.ulpgc.cleaner.controller.reader;

import java.io.*;
import java.nio.file.Path;

public class Reader {
    public String readFile(Path filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        try (FileReader fileReader = new FileReader("datalake/" + filePath.toString());
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
}
