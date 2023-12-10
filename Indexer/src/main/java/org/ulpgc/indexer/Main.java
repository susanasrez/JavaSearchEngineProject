package org.ulpgc.indexer;

import org.ulpgc.indexer.controller.Controller;

public class Main {

    public static void main(String[] args) {
        try {
            Controller.run(args[0], args[1]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
