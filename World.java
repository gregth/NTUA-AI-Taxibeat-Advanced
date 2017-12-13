import java.io.*;
import java.util.*;

public class World {
    // Make Singleton Instance of the World Class
    private static final World instance = new World();
    private World() {};
    public static World getInstance() {
        return instance;
    }

    private void printSearchSpaceAlt(HashMap<String, ArrayList<GraphConnection>> searchSpace) {
        ArrayList<GraphConnection> neighbors;
        for(String currentNode : searchSpace.keySet()) {
            System.out.println("Neighbors of " + currentNode);

            neighbors = searchSpace.get(currentNode);
            for (GraphConnection neighbor : neighbors) {
                neighbor.print();
            }
        }
    }

    public ArrayList<Node> parseNodes() {
        BufferedReader reader = null;

        String line = null, streetName;
        String[] parts = null;
        double x, y;
        int streetId;
        ArrayList<Node> nodes = new ArrayList<Node>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/nodes.csv"))));

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

                nodes.add(new Node(x, y, streetName, streetId));
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

        return nodes;
    }

    public HashMap<String, ArrayList<GraphConnection>> generateSearchSpace(ArrayList<Node> nodes, Client clientPosition) {
        HashMap<String, ArrayList<GraphConnection>> newSearchSpace = new HashMap<String, ArrayList<GraphConnection>>();

        double nodesDistance, distanceToClient, minDistanceToClient = -1;
        int previousStreetId = -1;
        String streetName = null;
        Node previousNode = null, targetNode = null;

        ArrayList<GraphConnection> neighbors;
        for (Node currentNode : nodes) {
            distanceToClient = currentNode.distanceTo(clientPosition);
            if (distanceToClient < minDistanceToClient || minDistanceToClient == -1) {
                minDistanceToClient = distanceToClient;
                targetNode = currentNode;
            }

            if (!newSearchSpace.containsKey(currentNode.stringify())) {
                //System.out.println("New node " + currentPosition.stringify());
                newSearchSpace.put(currentNode.stringify(), new ArrayList<GraphConnection>());
            }

            // Check if we are parsing a new street
            if (previousStreetId != currentNode.getStreetID()) { // new street
                previousStreetId = currentNode.getStreetID();
            } else { // same street, calculate distance to neighbors
                neighbors = newSearchSpace.get(currentNode.stringify());

                if (previousNode != null) {
                    nodesDistance = previousNode.distanceTo(currentNode);

                    neighbors.add(new GraphConnection(
                        previousNode.stringify(),
                        nodesDistance,
                        0
                    ));
                    
                    neighbors = newSearchSpace.get(previousNode.stringify());
                    neighbors.add(new GraphConnection(
                        currentNode.stringify(),
                        nodesDistance,
                        0
                    ));
                }
            }

            previousNode = currentNode;
        }

        fixHeuristics(newSearchSpace, targetNode);

        //printSearchSpaceAlt(newSearchSpace);
        return newSearchSpace;
    }

    private void fixHeuristics(HashMap<String, ArrayList<GraphConnection>> searchSpace, Position targetNode) {
        ArrayList<GraphConnection> neighbors;
        Position nodePosition;
        for (String node : searchSpace.keySet()) {
            neighbors = searchSpace.get(node);
            for (GraphConnection neighbor : neighbors){
                nodePosition = Position.parsePosition(neighbor.getNode());
                neighbor.setHeuristic(nodePosition.distanceTo(targetNode));
            }
        }
    }

    public Position closestNode(ArrayList<Node> nodes, Position position) {
        double minDistance = -1, distance;
        Position closest = null;
        for (Node currentNode : nodes) { 
            distance = currentNode.distanceTo(position);
            if (minDistance == -1 || distance < minDistance) {
                minDistance = distance;
                closest = currentNode;
            }
        }

        return closest;
    }
}
