package org.ulpgc.queryengine;

import org.ulpgc.queryengine.controller.Controller;


public class Main {

    public static void main(String[] args) {
        new Controller(args[0], args[1]);
    }
}
