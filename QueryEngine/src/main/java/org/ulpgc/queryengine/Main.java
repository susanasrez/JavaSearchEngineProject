package org.ulpgc.queryengine;

import org.ulpgc.queryengine.controller.Controller;
import org.ulpgc.queryengine.controller.exceptions.ObjectNotFoundException;

public class Main {

    public static void main(String[] args) throws ObjectNotFoundException {
        new Controller(args[0], args[1], Integer.parseInt(args[2]));
    }
}
