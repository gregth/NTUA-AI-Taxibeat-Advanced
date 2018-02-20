import java.io.*;
import java.util.*;
import static java.lang.System.out;

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


    public void parseNodes() {
        BufferedReader reader = null;

        String line = null, streetName;
        String[] parts = null;
        double x, y;
        int streetId;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/" + Taxibeat.nodesFile))));

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

    public SearchSpace generateSearchSpace(Client clientPosition) {
        HashMap<String, SearchNode> searchSpace = new HashMap<String, SearchNode>();

        double nodesDistance;
        int previousStreetId = -1;
        String streetName = null;
        Node previousNode = null;

        // Find the closet node to client Position
        Node targetNode = closestNode(clientPosition);

        for (Node currentNode : nodes) {
            boolean isGoal = targetNode.stringify().equals(currentNode.stringify());
            if (!searchSpace.containsKey(currentNode.stringify())) {
                // Insert new node in search space
                searchSpace.put(currentNode.stringify(), new SearchNode(currentNode, isGoal, currentNode.distanceTo(targetNode)));
            }

            // Check if we are parsing a new street
            if (previousStreetId != currentNode.getStreetID()) {
                // New street
                previousStreetId = currentNode.getStreetID();
            } else {
                // Same street, calculate distance to neighbors

                if (previousNode != null) {
                    nodesDistance = previousNode.distanceTo(currentNode);

                    // Let A = previousNode, B = currentNode, then add the edge AB
                    // in the neighbors set of both A and B.

                    // Include A in the neighbors of B
                    searchSpace.get(currentNode.stringify()).addNeighbor(new GraphEdge(
                        searchSpace.get(previousNode.stringify()),
                        nodesDistance,
                        previousNode.distanceTo(targetNode)
                    ));

                    // Include B in the neighbors of A
                    searchSpace.get(previousNode.stringify()).addNeighbor(new GraphEdge(
                        searchSpace.get(currentNode.stringify()),
                        nodesDistance,
                        currentNode.distanceTo(targetNode)
                    ));
                }
            }

            previousNode = currentNode;
        }

        //printSearchSpace(searchSpace);
        return new SearchSpace(searchSpace);
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
