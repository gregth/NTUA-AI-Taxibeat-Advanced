import java.io.*;
import java.util.*;

public class ParseNodes {
    public static HashMap<Integer, ArrayList<Position>> parse() {
        BufferedReader reader = null;
        HashMap<Integer, ArrayList<Position>> Streets = new HashMap<Integer, ArrayList<Position>>();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/nodes.csv"))));

            double x, y;
            int streetId;
            String streetName = null; 
            String line = null;
            String[] parts = null;
            int previousStreetId = -1;
            double streetLength = 0;
            double nodesDistance;
            Position previousNode = null;
            Position currentPosition = null;
            ArrayList<Position> nodes = null;

            reader.readLine(); // skip the first line with the captions
            while ((line = reader.readLine()) != null) {
                parts = line.split(",");

                x = Double.valueOf(parts[0].trim());
                y = Double.valueOf(parts[1].trim());
                streetId = Integer.valueOf(parts[2].trim());
                streetName = "";
                if (parts.length == 4) {
                    streetName = String.valueOf(parts[3].trim());
                }

                previousNode = currentPosition;
                currentPosition = new Position(x, y);
            
                if (previousStreetId != streetId) { // new Street
                    if (previousStreetId != -1) {
                        Streets.put(previousStreetId, nodes);
                    }

                    nodes = new ArrayList<Position>();
                    streetLength = 0;
                    previousStreetId = streetId;
                } else { // same street, calculate distance
                    if (previousNode != null) {
                        nodesDistance = Math.sqrt(Math.pow(x - previousNode.x, 2) + Math.pow(y - previousNode.y, 2));
                        streetLength += nodesDistance;
                    }
                }

                nodes.add(currentPosition);

                //System.out.println(streetName + " " + id + " " + x + " " + y);
            }

            Streets.put(previousStreetId, nodes);
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

        return Streets;
    }
}
