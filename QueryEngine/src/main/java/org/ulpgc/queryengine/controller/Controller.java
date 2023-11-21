package org.ulpgc.queryengine.controller;

import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.DatamartDataInterpreter;
import org.ulpgc.queryengine.controller.readDatamart.ReadDatamartStats;
import org.ulpgc.queryengine.view.API;

public class Controller {

    public Controller(String datalake, String datamart){
        new DatalakeReaderOneDrive(datalake+"/Metadata",
                datalake+"/Content", datalake+"/RawBooks");
        new DatamartDataInterpreter(datamart);
        new ReadDatamartStats(datamart);
        API.runAPI();
    }
}
