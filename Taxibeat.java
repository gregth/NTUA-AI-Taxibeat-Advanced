import java.io.*;
import java.util.*;

public class Taxibeat {
    public static void main(String[] args) {
        World myWorld = World.getInstance();
        ArrayList<Taxi> fleet = Taxi.parse();
        Client clientPosition = Client.parse();

        myWorld.parse();
        HashMap<GraphNode, ArrayList<GraphConnection>> searchSpace = myWorld.generateSearchSpace(clientPosition, fleet.get(0));

        System.out.println("here");
        for (GraphConnection conn : searchSpace.get(new GraphNode(new Position(6.0, 3.0)))) {
            conn.print();
        }

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

    private static void solve(HashMap<GraphNode, ArrayList<GraphConnection>> searchSpace, Position startPosition) {
        Comparator<GraphConnection> comparator = new GraphConnectionComparator();
        SortedSet<GraphConnection> queue = new TreeSet<GraphConnection>(comparator);

        GraphNode startNode = new GraphNode(startPosition);
        for (GraphConnection neighbor : searchSpace.get(startNode)) {
            queue.add(neighbor);
        }

        GraphConnection top = null;
        int maxFrontier = 3;
        while (queue.size() > 0) {
            top = queue.first();

            top.getNode().setVisited();

            if (top.getNode().isGoal()) {
                System.out.println("FOUND THE CLIENT!!!");
                break;
            }

            top.print();
            System.out.println(top.getTotalWeight());

            for (GraphConnection neighbor : searchSpace.get(top.getNode())) {
                queue.add(neighbor);
            }

            queue.remove(top);

            /*
            while (queue.size() > maxFrontier) {
                System.out.println("Removing");
                queue.last().print();
                queue.remove(queue.last());
            }
            */
        }

        if (top != null) {
            // found the client
            top.print();
        }
    }
}

class GraphConnectionComparator implements Comparator<GraphConnection> {
    @Override
    public int compare(GraphConnection a, GraphConnection b) {
        if (a.getTotalWeight() < b.getTotalWeight()) {
            return -1;
        }

        if (a.getTotalWeight() > b.getTotalWeight()) {
            return 1;
        }

        return 0;
    }
}
