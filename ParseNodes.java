import java.io.*;
import java.util.*;

public class ParseNodes {
    private static HashMap<String, Set<GraphNode>> generateSearchSpace(
            Set<String> Intersections,
            HashMap<String, Set<Integer>> NodesToStreets,
            HashMap<Integer, ArrayList<Position>> StreetsToNodes
    ) {
        Set<Integer> streetIDs = null;
        ArrayList<Position> streetNodes = null;
        for (String intersection : Intersections) {
            System.out.println(intersection);
            streetIDs = NodesToStreets.get(intersection);
            if (streetIDs != null) {
                for (Integer streetID : streetIDs) {
                    System.out.println(streetID);
                    streetNodes = StreetsToNodes.get(streetID);
                    Iterator<Position> streeNodesItr = streeNodes.iterator();
                    Position prevIntersection = null, nextIntersection = null;
                    Position currentNode = null;
                    boolean foundIntersection = false;
                    while (streeNodesItr.hasNext()) {
                        currentNode = StreetsToNodes.next();
                        String currentNodeString = currentNode.stringify();
                        if (currentNodeString == intersection) {
                            System.out.println("Intersection is myself");
                            foundIntersection = true;
                        } else if (Intersections.contains(currentNodeString)) {
                            if (!foundIntersection) {
                                prevIntersection = currentNode;
                            } else {
                                nextIntersection = currentNode;
                                break;
                            }
                        }
                    }
                }
            }
            break;
        }
        return null;
    };

    public static HashMap<String, Set<Integer>> parse() {
        BufferedReader reader = null;
        HashMap<Integer, ArrayList<Position>> StreetsToNodes = new HashMap<Integer, ArrayList<Position>>();
        HashMap<String, Set<Integer>> NodesToStreets = new HashMap<String, Set<Integer>>();
        Set<String> Intersections = new TreeSet<String>();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/nodes.csv"))));

            double x, y, streetLength = 0, nodesDistance;
            int streetId = -1, previousStreetId = -1;
            String streetName = null, line = null;
            String[] parts = null;
            StreetNode previousNode = null, currentPosition = null;
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
                currentPosition = new StreetNode(x, y);

                // Check for intersections
                if (NodesToStreets.containsKey(currentPosition.stringify())) {
                    //System.out.println("Found intersection!");
                    Intersections.add(currentPosition.stringify());
                    if (Intersections.size() == 1) {
                        System.out.println(currentPosition.stringify());
                    }
                } else { // new node
                    NodesToStreets.put(currentPosition.stringify(), new TreeSet<Integer>());
                }
                Set<Integer> streetsFound = NodesToStreets.get(currentPosition.stringify());
                streetsFound.add(streetId); // Sets prevent duplicates

                // Check if we are parsing a new street
                if (previousStreetId != streetId) { // new street
                    if (previousStreetId != -1) {
                        StreetsToNodes.put(previousStreetId, nodes);
                    }

                    nodes = new ArrayList<Position>();
                    streetLength = 0;
                    previousStreetId = streetId;
                } else { // same street, calculate distance
                    if (previousNode != null) {
                        nodesDistance = Math.sqrt(Math.pow(x - previousNode.x, 2) + Math.pow(y - previousNode.y, 2));
                        streetLength += nodesDistance;
                    }

                    nodes.add(currentPosition);
                }

                //System.out.println(streetName + " " + id + " " + x + " " + y);
            }

            if (streetId != -1) {
                StreetsToNodes.put(streetId, nodes);
            }
            generateSearchSpace(Intersections, NodesToStreets, StreetsToNodes);
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

        return NodesToStreets;
    }
}
