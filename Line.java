import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Line {
    public static void parse() {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/" + Taxibeat.linesFile))));

            int id, lanes, maxspeed;
            String line, highway, name, oneway, boundary, access, natural, barrier, incline, waterway, busway;
            boolean lit, railway, tunnel, bridge, toll;
            String[] parts = null;

            reader.readLine(); // skip the first line with the captions
            while ((line = reader.readLine()) != null) {
                line = line.replace(",", " , ");
                parts = line.split(",");

                for (int i = 0; i < parts.length; ++i) {
                    parts[i] = parts[i].trim();
                    if (parts[i].length() == 0) {
                        parts[i] = "no";
                    }
                }

                id = Integer.valueOf(parts[0]);
                highway = parts[1];
                name = parts[2];
                oneway = parts[3];
                lit = parts[4].equals("yes") ? true : false;
                lanes = !parts[5].equals("no") ? Integer.valueOf(parts[5]) : 0;
                maxspeed = !parts[6].equals("no") ? Integer.valueOf(parts[6]) : 0;
                railway = parts[7].equals("yes") ? true : false;
                boundary = parts[8];
                access = parts[9];
                natural = parts[10];
                barrier = parts[11];
                tunnel = parts[12].equals("yes") ? true : false;
                bridge = parts[13].equals("yes") ? true : false;
                incline = parts[14];
                waterway = parts[15];
                busway = parts[16];
                toll = parts[17].equals("yes") ? true : false;

                String predicate = "lineSpecs(" + id + "," + highway + "," + name + "," + oneway + "," + lit + "," + lanes + "," + maxspeed + "," + railway + "," + boundary + "," + access + "," + natural + "," + barrier + "," + tunnel + "," + bridge + "," + incline + "," + waterway + "," + busway + "," + toll + ")";

                System.out.println(predicate);
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
