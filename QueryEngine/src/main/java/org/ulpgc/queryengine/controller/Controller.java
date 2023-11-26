package org.ulpgc.queryengine.controller;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.ulpgc.queryengine.controller.exceptions.ObjectNotFoundException;
import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReaderFiles;
import org.ulpgc.queryengine.controller.readDatamart.filesystem.ReadDatamartFiles;
import org.ulpgc.queryengine.controller.readDatamart.filesystem.ReadDatamartStats;
import org.ulpgc.queryengine.controller.readDatamart.hazelcast.ReadHazelcastWords;
import org.ulpgc.queryengine.view.API;

public class Controller {

    public Controller(String datalake, String datamart, int port) throws ObjectNotFoundException {
        /*new DatalakeReaderOneDrive(datalake+"/Metadata",
                datalake+"/Content", datalake+"/RawBooks");
        ReadDatamartFiles readDatamartFiles= new ReadDatamartFiles(datamart);
        ReadDatamartStats readDatamartStats = new ReadDatamartStats(datamart);
        API.runAPI(readDatamartFiles, readDatamartStats, port);*/
        Config config = new Config();
        config.setInstanceName("instance");
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        DatamartReaderFiles datamartReaderFiles = new ReadHazelcastWords(hazelcastInstance);
        datamartReaderFiles.get_documents("hello");

    }
}
