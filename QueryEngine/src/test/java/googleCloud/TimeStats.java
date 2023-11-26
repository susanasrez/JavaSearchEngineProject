package googleCloud;

import org.ulpgc.queryengine.controller.readDatamart.DatamartCalculateStats;
import org.ulpgc.queryengine.controller.readDatamart.google.cloud.ReadCloud;
import org.ulpgc.queryengine.controller.readDatamart.google.cloud.ReadGoogleCloudStats;

import java.io.IOException;

public class TimeStats {
    public static double startTime, endTime, duration;
    private static final DatamartCalculateStats datamartCalculateStats = new ReadGoogleCloudStats();

    public static void main(String[] args) throws IOException {
        duration = 0.0;
        ReadCloud.obtain_credentials();
        for (int i = 1; i <= 20; i++){
            System.out.println("i = " + i);
            measureTotal();
            measureLength(String.valueOf(i));
            System.out.println();
        }

    }
    public static void measureTotal(){
        startTime = System.currentTimeMillis();
        datamartCalculateStats.totalWords();
        endTime =  System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Duration total: " + duration);
    }

    public static void measureLength(String number){
        startTime = System.currentTimeMillis();
        datamartCalculateStats.wordLength(number);
        endTime =  System.currentTimeMillis();
        duration = endTime - startTime;
        System.out.println("Duration length: " + duration);
    }

}
