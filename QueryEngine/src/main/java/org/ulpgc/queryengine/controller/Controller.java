package org.ulpgc.queryengine.controller;

import org.ulpgc.queryengine.controller.exceptions.ObjectNotFoundException;
import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.DatamartCalculateStats;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReaderFiles;
import org.ulpgc.queryengine.controller.readDatamart.hazelcast.ReadHazelcastStats;
import org.ulpgc.queryengine.controller.readDatamart.hazelcast.ReadHazelcastWords;
import org.ulpgc.queryengine.view.API;

public class Controller {

    public Controller(String datalake, int port) throws ObjectNotFoundException {
        new DatalakeReaderOneDrive(datalake+"/Metadata",
                datalake+"/Content", datalake+"/RawBooks");
        DatamartReaderFiles readDatamartFiles= new ReadHazelcastWords();
        DatamartCalculateStats readDatamartStats = new ReadHazelcastStats();
        API.runAPI(readDatamartFiles, readDatamartStats, port);
    }
}
