package org.crawler.fileHandlers;

import org.crawler.message.broker.EventPublisher;
import org.crawler.message.broker.Publisher;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class LocalFileHandler implements FileHandler{

    private static final String DOCUMENT_REPOSITORY_PATH = "C:\\Users\\carde\\OneDrive - Universidad de Las Palmas de Gran Canaria\\Documents\\DocumentsRepository\\RawBooks\\";
    private final Publisher filePublisher;

    public LocalFileHandler() throws Exception {
        this.filePublisher = new EventPublisher("61616", "datalakeEvents");
    }

    @Override
    public void saveDocument(Document bookDocument, int bookID) throws IOException {
        createFolder();
        createFile(bookDocument, bookID);
        publishFileAddition(bookID);
    }

    private void publishFileAddition(int bookID) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        filePublisher.publish(currentDate + '/' + bookID + ".txt");
    }

    public int getLastFileIdInLastDirectory() {
        File directory = new File(DOCUMENT_REPOSITORY_PATH);

        if (directory.exists() && directory.isDirectory()) {
            File[] directories = directory.listFiles(File::isDirectory);

            if (directories != null && directories.length > 0) {
                Arrays.sort(directories, (dir1, dir2) -> Long.compare(dir2.lastModified(), dir1.lastModified()));

                File lastDirectory = directories[0];
                File[] filesInLastDirectory = lastDirectory.listFiles();

                if (filesInLastDirectory != null && filesInLastDirectory.length > 0) {
                    Arrays.sort(filesInLastDirectory, (file1, file2) -> Long.compare(file2.lastModified(), file1.lastModified()));
                    return Integer.parseInt(filesInLastDirectory[0].getName().replace(".txt", ""));
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    private void createFile(Document bookDocument, int bookID) throws IOException{
        String filePath = getFolderPath() + bookID + ".txt";
        FileWriter writer = new FileWriter(filePath);
        writer.write(bookDocument.text());
        writer.close();
    }

    private void createFolder(){
        File folder = new File(getFolderPath());
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private String getFolderPath(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        return DOCUMENT_REPOSITORY_PATH + currentDate + "/";
    }


}
