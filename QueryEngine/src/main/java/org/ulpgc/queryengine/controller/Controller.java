package org.ulpgc.queryengine.controller;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.ulpgc.queryengine.controller.readDatalake.DatalakeReaderOneDrive;
import org.ulpgc.queryengine.controller.readDatamart.DatamartCalculateStats;
import org.ulpgc.queryengine.controller.readDatamart.DatamartReaderFiles;
import org.ulpgc.queryengine.controller.readDatamart.hazelcast.ReadHazelcastStats;
import org.ulpgc.queryengine.controller.readDatamart.hazelcast.ReadHazelcastWords;
import org.ulpgc.queryengine.view.API;

import java.io.IOException;

public class Controller {

    public Controller(String datalake, int port) throws IOException {
        new DatalakeReaderOneDrive(datalake+"/Metadata",
                datalake+"/Content", datalake+"/RawBooks");
        HazelcastInstance hazelcastInstance = initializeHaZelcast();
        DatamartReaderFiles readDatamartFiles= new ReadHazelcastWords(hazelcastInstance);
        DatamartCalculateStats readDatamartStats = new ReadHazelcastStats(hazelcastInstance);
        API.runAPI(readDatamartFiles, readDatamartStats, port);



    }

    private static HazelcastInstance initializeHaZelcast(){
        Config config = new Config();
        config.setInstanceName("instance");
        return Hazelcast.newHazelcastInstance(config);
    }
}
