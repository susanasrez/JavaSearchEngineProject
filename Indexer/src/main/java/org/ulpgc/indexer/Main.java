package org.ulpgc.indexer;

import org.ulpgc.indexer.controller.Controller;

public class Main {
    public static int SERVER_MQ_PORT = 443;
    public static int SERVER_CLEANER_PORT = 80;

    public static void main(String[] args) {
        try {
            Controller.run(args[0]);
            new API().run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
