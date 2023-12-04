package org.ulpgc.queryengine;

import org.ulpgc.queryengine.controller.Controller;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        new Controller(args[0], Integer.parseInt(args[1]));
    }
}
