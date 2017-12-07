import java.io.*;
import java.util.*;

public class ParseNodes {
    private static ArrayList<GraphNode> findNeighbors(
        Integer streetID,
        String intersection,
        Set<String> Intersections,
        HashMap<Integer, ArrayList<Position>> StreetsToNodes
    ) {
        ArrayList<Position> streetNodes = StreetsToNodes.get(streetID);
        Iterator<Position> streetNodesItr = streetNodes.iterator();
        Position prevIntersection = null, nextIntersection = null, currentNode = null, currentIntersection = null;
        boolean foundIntersection = false;
        double distanceToPrev, distanceToNext;
        GraphNode prevGraphNode = null, nextGraphNode = null;

        ArrayList<GraphNode> neighbors = new ArrayList<GraphNode>();
        while (streetNodesItr.hasNext()) {
            currentNode = streetNodesItr.next();
            String currentNodeString = currentNode.stringify();
            if (currentNodeString.equals(intersection)) {
                currentIntersection = currentNode;
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

        if (prevIntersection != null) {
            distanceToPrev = currentIntersection.distanceTo(prevIntersection);
            prevGraphNode = new GraphNode(prevIntersection.stringify(), distanceToPrev);
            neighbors.add(prevGraphNode);
        }
        if (nextIntersection != null) {
            distanceToNext = currentIntersection.distanceTo(nextIntersection);
            nextGraphNode = new GraphNode(nextIntersection.stringify(), distanceToNext);
            neighbors.add(nextGraphNode);
        }

        return neighbors;
    }

    private static void printSearchSpace(HashMap<String, ArrayList<GraphNode>> searchSpace) {
        ArrayList<GraphNode> neighbors;
        for(String currentNode : searchSpace.keySet()) {
            System.out.println("Neighbors of " + currentNode);
            neighbors = searchSpace.get(currentNode);
            for (GraphNode neighbor : neighbors) {
                neighbor.print();
            }
        }
    }

    private static HashMap<String, ArrayList<GraphNode>> generateSearchSpace(
            Set<String> Intersections,
            HashMap<String, Set<Integer>> NodesToStreets,
            HashMap<Integer, ArrayList<Position>> StreetsToNodes
    ) {
        HashMap<String, ArrayList<GraphNode>> searchSpace = new HashMap<String, ArrayList<GraphNode>>();
        Set<Integer> streetIDs = null;
        System.out.println("Found " + Intersections.size() + " intersections.");
        for (String intersection : Intersections) {
            //System.out.println(intersection);
            streetIDs = NodesToStreets.get(intersection);
            if (streetIDs != null) {
                ArrayList<GraphNode> neighbors = null;
                for (Integer streetID : streetIDs) {
                    neighbors = findNeighbors(streetID, intersection, Intersections, StreetsToNodes);

                    if (neighbors.size() > 0) {
                        searchSpace.put(intersection, neighbors);
                    }
                }
            }
            ParseNodes.printSearchSpace(searchSpace);
            break;
        }

        return searchSpace;
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
                }

                nodes.add(currentPosition);

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
