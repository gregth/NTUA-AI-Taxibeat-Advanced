import java.io.*;
import java.util.*;

public class World {
    private ArrayList<Node> nodes;

    // Make Singleton Instance of the World Class
    private static final World instance = new World();
    private World() {
        nodes = new ArrayList<Node>();
    };
    public static World getInstance() {
        return instance;
    }

    private void printSearchSpaceAlt(HashMap<String, ArrayList<GraphEdge>> searchSpace) {
        ArrayList<GraphEdge> neighbors;
        for(String currentNode : searchSpace.keySet()) {
            System.out.println("Neighbors of " + currentNode);

            neighbors = searchSpace.get(currentNode);
            for (GraphEdge neighbor : neighbors) {
                neighbor.print();
            }
        }
    }

    public void parseNodes() {
        BufferedReader reader = null;

        String line = null, streetName;
        String[] parts = null;
        double x, y;
        int streetId;
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
    }

    public HashMap<String, ArrayList<GraphEdge>> generateSearchSpace(Client clientPosition) {
        HashMap<String, ArrayList<GraphEdge>> newSearchSpace = new HashMap<String, ArrayList<GraphEdge>>();

        double nodesDistance;
        int previousStreetId = -1;
        String streetName = null;
        Node previousNode = null;

        // Find the closet node to client Position
        Node targetNode = closestNode(clientPosition);

        ArrayList<GraphEdge> neighbors;
        for (Node currentNode : nodes) {
            if (!newSearchSpace.containsKey(currentNode.stringify())) {
                //System.out.println("New node " + currentPosition.stringify());
                newSearchSpace.put(currentNode.stringify(), new ArrayList<GraphEdge>());
            }

            // Check if we are parsing a new street
            if (previousStreetId != currentNode.getStreetID()) { // new street
                previousStreetId = currentNode.getStreetID();
            } else { // same street, calculate distance to neighbors
                neighbors = newSearchSpace.get(currentNode.stringify());

                if (previousNode != null) {
                    nodesDistance = previousNode.distanceTo(currentNode);

                    neighbors.add(new GraphEdge(
                        previousNode.stringify(),
                        nodesDistance,
                        previousNode.distanceTo(targetNode)
                    ));

                    neighbors = newSearchSpace.get(previousNode.stringify());
                    neighbors.add(new GraphEdge(
                        currentNode.stringify(),
                        nodesDistance,
                        currentNode.distanceTo(targetNode)
                    ));
                }
            }

            previousNode = currentNode;
        }

        //printSearchSpaceAlt(newSearchSpace);
        return newSearchSpace;
    }

    public Node closestNode(Position position) {
        double minDistance = -1, distance;
        Node closest = null;
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
