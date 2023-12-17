package org.ulpgc.indexer;

import org.ulpgc.indexer.controller.Controller;

public class Main {
    public static int SERVER_MQ_PORT = 443;
    public static int SERVER_CLEANER_PORT = 80;
    public static int INDEXED_BOOKS = 0;

    public static void main(String[] args) {
        try {
            Controller.run(args[0], args[1]);
            new API().run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
