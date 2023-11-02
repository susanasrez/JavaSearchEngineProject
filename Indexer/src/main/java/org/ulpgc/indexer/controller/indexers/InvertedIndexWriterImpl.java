package org.ulpgc.indexer.controller.indexers;

import com.google.gson.Gson;
import org.ulpgc.indexer.controller.InvertedIndexWriter;
import org.ulpgc.indexer.model.FileEvent;

import javax.sound.midi.Soundbank;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class InvertedIndexWriterImpl implements InvertedIndexWriter {
    private final String indexPath;

    public InvertedIndexWriterImpl() {
        this.indexPath = "invertedIndexDatamart";
    }


    @Override
    public void save_document_event(FileEvent event) {
        try {
            FileWriter fileWriter = new FileWriter(indexPath + "/indexEvents/" + event.getFileName());
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write((new Gson()).toJson(event));

            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save_word_document(String word, String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(indexPath + "/invertedIndex/" + word, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write(fileName + "\n");

            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error found when indexing word: " + word);
        }
    }
}
