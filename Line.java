import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Line {
    public static void parse() {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/" + Taxibeat.linesFile))));
            PrologParser prologSystem = PrologParser.getInstance();

            int id, lanes, maxspeed;
            String line, highway, name, oneway, boundary, access, natural, barrier,
                   incline, waterway, busway, lit, railway, tunnel, bridge, toll;
            String[] parts = null;

            reader.readLine(); // skip the first line with the captions
            while ((line = reader.readLine()) != null) {
                line = replaceEscapedCommas(line);
                line = line.replace(",", " , ");
                parts = line.split(",");

                for (int i = 0; i < parts.length; ++i) {
                    parts[i] = parts[i].trim();
                    if (parts[i].length() == 0) {
                        parts[i] = "no";
                    }
                }

                id = Integer.valueOf(parts[0]);
                highway = parts[1].equals("no") ? "unknown" : parts[1];
                name = parts[2];
                oneway = parts[3];
                lit = parts[4];
                lanes = !parts[5].equals("no") ? Integer.valueOf(parts[5]) : 0;
                maxspeed = !parts[6].equals("no") ? Integer.valueOf(parts[6]) : 0;
                railway = parts[7];
                boundary = parts[8];
                access = parts[9];
                natural = parts[10];
                barrier = parts[11];
                tunnel = parts[12];
                bridge = parts[13];
                incline = parts[14].replace('%', ' ');
                waterway = parts[15];
                busway = parts[16];
                toll = parts[17];

                String predicate = "lineSpecs(" + id + "," + highway + "," + "thename" + "," + oneway + "," + lit + "," + lanes + "," + maxspeed + "," + railway + "," + boundary + "," + access + "," + natural + "," + barrier + "," + tunnel + "," + bridge + "," + incline + "," + waterway + "," + busway + "," + toll + ")";
                prologSystem.asserta(predicate);
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

    // Works only if there are not escaped quotes in quotes
    private static String replaceEscapedCommas(String line) {
        if (line.contains("\"")) {
            String[] chars = line.split("");
            int index;
            boolean replaceCommas = false;
            for (index = 0; index < chars.length; index++) {
                if (chars[index].equals("\"")) {
                    replaceCommas = !replaceCommas;
                }
                if (chars[index].equals(",") && replaceCommas) {
                    chars[index] = " "; //Replace with empty space
                }
            }
            line = String.join("", chars);
        }
        return line;
    }
}
