package org.ulpgc.queryengine.controller;

import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.ReadCloud;
import org.ulpgc.queryengine.view.API;

public class Controller {

    public Controller(String datalake) throws Exception {
        new DatalakeReaderOneDrive(datalake+"/Metadata",
                datalake+"/Content", datalake+"/RawBooks");
        ReadCloud.obtain_credentials();
        API.runAPI();
    }
}
