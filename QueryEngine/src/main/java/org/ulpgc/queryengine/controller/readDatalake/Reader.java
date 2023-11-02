package org.ulpgc.queryengine.controller.readDatalake;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Reader {

    public static String readFileContent(Path filePath) throws Exception {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            return content.toString();
        } catch (Exception e) {
            throw new Exception("Error when reading the file", e);
        }
    }
}
