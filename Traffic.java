import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Traffic {
    public static void parse() {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/" + Taxibeat.trafficFile))));
            PrologParser prologSystem = PrologParser.getInstance();

            int id, startTime = 0, endTime = 0;
            String name, trafficInfo, trafficTimes, trafficLevel, line;
            String[] parts = null, trafficParts;

            reader.readLine(); // skip the first line with the captions
            while ((line = reader.readLine()) != null) {
                parts = line.split(",");

                id = Integer.valueOf(parts[0]);
                
                if (parts.length >= 3 && !parts[2].equals("")) {
                    name = parts[1];
                    trafficInfo = parts[2];
                    trafficParts = trafficInfo.split("\\|");
                    for (String trafficPart : trafficParts) {
                        trafficTimes = trafficPart.split("=")[0];
                        trafficLevel = trafficPart.split("=")[1];

                        startTime = Integer.valueOf(trafficTimes.split("-")[0].replace(":", ""));
                        endTime = Integer.valueOf(trafficTimes.split("-")[1].replace(":", ""));

                        String predicate = "lineTraffic(" + id + "," + startTime + "," + endTime + "," + trafficLevel + ")";
                        prologSystem.asserta(predicate);
                    }

                }
            }
        } catch (IOException e) {
            System.err.println("Exception:" + e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Exception:" + e.toString());
                }
            }
        }
    }
}
