package org.ulpgc.cleaner.controller.writer;

import com.google.gson.Gson;
import org.ulpgc.cleaner.controller.process.ParseFiles;
import org.ulpgc.cleaner.model.WriteEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

public class Writer {

    private static final Path pathContent= Path.of("datalake/content");
    private static final Path pathEvent = Path.of("Cleaner/src/main/resources/readEvents");
    private static final Path pathMetadata = Path.of("datalake/metadata");

    public static void writeToDatalake(String text, String idBook){
        try {
            String newName = ParseFiles.renameFile(idBook);
            FileWriter fileWriter = new FileWriter(pathContent + "/" + newName + ".txt");
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(text);
            writer.close();
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeMetadataToDatalake(String text, String idBook){
        try {
            String newName = ParseFiles.renameFile(idBook);
            FileWriter fileWriter = new FileWriter((pathMetadata +  "/" + newName + ".metadata"), true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(text);
            writer.close();
            fileWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeEvent(String idBook){
        FileWriter file = null;
        try {
            String newName = ParseFiles.renameFile(idBook);
            file = new FileWriter(pathEvent +  "/" + newName + ".event" );
            WriteEvent event = new WriteEvent(new Date(), newName);
            BufferedWriter writer = new BufferedWriter(file);
            writer.write((new Gson()).toJson(event));

            writer.close();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
