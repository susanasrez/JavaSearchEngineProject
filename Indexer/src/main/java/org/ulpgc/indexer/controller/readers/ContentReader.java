package org.ulpgc.indexer.controller.readers;

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
}
