package org.ulpgc.cleaner;

import org.ulpgc.cleaner.controller.Controller;
import org.ulpgc.cleaner.view.APIController;

public class Main {
    public static String datalakePath;

    public static void main(String[] args) {
        APIController apiController = new APIController();
        apiController.run();

        datalakePath = args[0];
        Controller.run(args[0]);
    }
}