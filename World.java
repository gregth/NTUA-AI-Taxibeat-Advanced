import java.io.*;
import java.util.*;

public class World {
    private Set<String> Intersections;
    private HashMap<String, Set<Integer>> NodesToStreets;
    private HashMap<Integer, ArrayList<Position>> StreetsToNodes;

    // Make Singleton Instance of the World Class
    private static final World instance = new World();
    private World() {
        StreetsToNodes = new HashMap<Integer, ArrayList<Position>>();
        NodesToStreets = new HashMap<String, Set<Integer>>();
        Intersections = new TreeSet<String>();
    };
    public static World getInstance() {
        return instance;
    }

    private ArrayList<GraphNode> findNeighbors(Integer streetID, String intersection) {
        ArrayList<Position> streetNodes = StreetsToNodes.get(streetID);
        Iterator<Position> streetNodesItr = streetNodes.iterator();
        Position prevIntersection = null, nextIntersection = null,
            currentNode = null, currentIntersection = null;
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

    private void printSearchSpace(HashMap<String, ArrayList<GraphNode>> searchSpace) {
        ArrayList<GraphNode> neighbors;
        for(String currentNode : searchSpace.keySet()) {
            System.out.println("Neighbors of " + currentNode);
            neighbors = searchSpace.get(currentNode);
            for (GraphNode neighbor : neighbors) {
                neighbor.print();
            }
        }
    }

    private HashMap<String, ArrayList<GraphNode>> generateSearchSpace() {
        HashMap<String, ArrayList<GraphNode>> searchSpace = new HashMap<String, ArrayList<GraphNode>>();
        Set<Integer> streetIDs = null;
        System.out.println("Found " + Intersections.size() + " intersections.");
        for (String intersection : Intersections) {
            //System.out.println(intersection);
            streetIDs = NodesToStreets.get(intersection);
            if (streetIDs != null) {
                ArrayList<GraphNode> neighbors = null;
                for (Integer streetID : streetIDs) {
                    neighbors = findNeighbors(streetID, intersection);

                    if (neighbors.size() > 0) {
                        searchSpace.put(intersection, neighbors);
                    }
                }
            }
            printSearchSpace(searchSpace);
            break;
        }

        return searchSpace;
    };

    public Position closestStreeNode(Position position) {
        Position nodePosition = null;

        double minDistance = -1, distance, x, y;
        String[] parts;

        for(String currentNode : NodesToStreets.keySet()) {
            parts = currentNode.split(" ");
            x = Double.valueOf(parts[0].trim());
            y = Double.valueOf(parts[1].trim());

            distance = Math.sqrt(Math.pow(position.x - x, 2) + Math.pow(position.y - y, 2));

            if (minDistance < 0 || distance < minDistance) {
                minDistance = distance;
                nodePosition = new Position(x, y);
            }
        }

        return nodePosition;
    }

    public HashMap<String, Set<Integer>> parse() {
        BufferedReader reader = null;

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
                    Intersections.add(currentPosition.stringify());//
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
            generateSearchSpace();
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
