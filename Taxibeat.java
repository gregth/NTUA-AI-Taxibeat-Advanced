import java.io.*;
import java.util.*;

public class Taxibeat {
    public static void main(String[] args) {
        World myWorld = World.getInstance();
        ArrayList<Taxi> fleet = Taxi.parse();
        Client clientPosition = Client.parse();

        myWorld.parse();
        HashMap<String, ArrayList<GraphNode>> searchSpace = myWorld.generateSearchSpace(clientPosition, fleet.get(0));

        Position driverPosition = myWorld.closestStreeNode(fleet.get(0));

        for (Taxi taxi : fleet) {
            //taxi.printTaxi();
        }

        System.out.println("Client: ");
        myWorld.clientPosition.print();
        String[] test = {"test"};
        XMLFile.getInstance().write(test);
        solve(searchSpace, driverPosition);
    }

    private static void solve(HashMap<String, ArrayList<GraphNode>> searchSpace, Position startPosition) {
        Comparator<GraphNode> comparators = new GraphNodeComparator();
        PriorityQueue<GraphNode> queue = new PriorityQueue<GraphNode>(comparators);

        String startPositionString = startPosition.stringify();
        for (GraphNode neighbor : searchSpace.get(startPositionString)) {
            queue.add(neighbor);
        }

        GraphNode top;
        while (queue.size() > 0) {
            top = queue.poll();
            if (top.isGoal()) {
                System.out.println("FOUND THE CLIENT!!!");
                break;
            }

            for (GraphNode neighbor : searchSpace.get(top.getPosition())) {
                if (!neighbor.isVisited()) {
                    queue.add(neighbor);
                    neighbor.setVisited();
                }
            }
        }
    }
}

class GraphNodeComparator implements Comparator<GraphNode> {
    @Override
    public int compare(GraphNode a, GraphNode b) {
        if (a.getTotalWeight() < b.getTotalWeight()) {
            return -1;
        }

        if (a.getTotalWeight() > b.getTotalWeight()) {
            return 1;
        }

        return 0;
    }
}
