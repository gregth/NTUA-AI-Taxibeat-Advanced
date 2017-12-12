import java.io.*;
import java.util.*;

public class World {
    private Set<String> Intersections;
    private HashMap<String, Set<Integer>> NodesToStreets;
    private HashMap<Integer, ArrayList<Position>> StreetsToNodes;
    public Position clientPosition;

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

    private ArrayList<GraphConnection> findNeighborIntersections(Integer streetID, String position, Position goalNode) {
        ArrayList<Position> streetNodes = StreetsToNodes.get(streetID);
        Iterator<Position> streetNodesItr = streetNodes.iterator();
        Position prevIntersection = null, nextIntersection = null,
            currentNode = null, currentIntersection = null;
        boolean foundIntersection = false;
        double distanceToPrev, distanceToNext, distanceToGoal;
        GraphNode prevGraphNode = null, nextGraphNode = null;
        GraphConnection prevGraphConnection = null, nextGraphConnection = null;

        ArrayList<GraphConnection> neighbors = new ArrayList<GraphConnection>();
        while (streetNodesItr.hasNext()) {
            currentNode = streetNodesItr.next();
            String currentNodeString = currentNode.stringify();
            if (currentNodeString.equals(position)) {
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
            distanceToGoal = prevIntersection.distanceTo(goalNode);

            prevGraphNode = new GraphNode(prevIntersection);
            prevGraphConnection = new GraphConnection(
                prevGraphNode,
                streetID,
                distanceToPrev,
                distanceToGoal
            );
            if (distanceToGoal == 0) {
                prevGraphNode.setGoal();
            }
            neighbors.add(prevGraphConnection);
        }
        if (nextIntersection != null) {
            distanceToNext = currentIntersection.distanceTo(nextIntersection);
            distanceToGoal = nextIntersection.distanceTo(goalNode);

            nextGraphNode = new GraphNode(nextIntersection);
            nextGraphConnection = new GraphConnection(
                nextGraphNode,
                streetID,
                distanceToNext,
                distanceToGoal
            );
            if (distanceToGoal == 0) {
                nextGraphNode.setGoal();
            }
            neighbors.add(nextGraphConnection);
        }

        return neighbors;
    }

    private void printSearchSpace(HashMap<GraphNode, ArrayList<GraphConnection>> searchSpace) {
        ArrayList<GraphConnection> neighbors;
        for(GraphNode currentNode : searchSpace.keySet()) {
            System.out.println("Neighbors of ");
            currentNode.print();
            neighbors = searchSpace.get(currentNode);
            for (GraphConnection neighbor : neighbors) {
                neighbor.print();
            }
        }
    }

    public HashMap<GraphNode, ArrayList<GraphConnection>> generateSearchSpace(Client clientPosition, Taxi driverPosition) {
        HashMap<GraphNode, ArrayList<GraphConnection>> searchSpace = new HashMap<GraphNode, ArrayList<GraphConnection>>();
        Set<Integer> streetIDs = null;
        ArrayList<GraphConnection> neighbors = null, buffer;

        Position clientNodePosition = closestStreeNode(clientPosition);
        this.clientPosition = clientNodePosition;
        String clientNodeString = clientNodePosition.stringify();
        if (!Intersections.contains(clientNodeString)) {
            Intersections.add(clientNodeString);
        }

        Position driverNodePosition = closestStreeNode(driverPosition);
        String driverNodeString = driverNodePosition.stringify();
        if (!Intersections.contains(driverNodeString)) {
            Intersections.add(driverNodeString);
        }


        Position intersectionPosition;
        GraphNode intersectionNode;
        for (String intersection : Intersections) {
            streetIDs = NodesToStreets.get(intersection);

            intersectionPosition = Position.parsePosition(intersection);
            intersectionNode = new GraphNode(intersectionPosition);
            if (intersection.equals(clientNodeString)) {
                intersectionNode.setGoal();
            }
            if (streetIDs != null) {
                for (Integer streetID : streetIDs) {
                    neighbors = findNeighborIntersections(streetID, intersection, clientNodePosition);

                    if (neighbors.size() > 0) {
                        if (!searchSpace.containsKey(intersectionNode)) {
                            searchSpace.put(intersectionNode, new ArrayList<GraphConnection>());

                        }
                        for (GraphConnection connection : neighbors) {
                            if (!searchSpace.containsKey(connection.getNode())) {
                                searchSpace.put(connection.getNode(), new ArrayList<GraphConnection>());
                            }
                        }
                        buffer = searchSpace.get(intersectionNode);
                        buffer.addAll(neighbors);
                    }
                }
            }
        }

        printSearchSpace(searchSpace);
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

    public void parse() {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/mynodes.csv"))));

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
            }

            if (streetId != -1) {
                StreetsToNodes.put(streetId, nodes);
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
