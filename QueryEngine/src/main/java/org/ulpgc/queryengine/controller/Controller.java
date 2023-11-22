package org.ulpgc.queryengine.controller;

import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.filesystem.ReadDatamartFiles;
import org.ulpgc.queryengine.controller.readDatamart.filesystem.ReadDatamartStats;
import org.ulpgc.queryengine.view.API;

public class Controller {

    public Controller(String datalake, String datamart, int port){
        new DatalakeReaderOneDrive(datalake+"/Metadata",
                datalake+"/Content", datalake+"/RawBooks");
        ReadDatamartFiles readDatamartFiles= new ReadDatamartFiles(datamart);
        ReadDatamartStats readDatamartStats = new ReadDatamartStats(datamart);
        API.runAPI(readDatamartFiles, readDatamartStats, port);
    }
}
