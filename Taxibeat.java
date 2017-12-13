import java.io.*;
import java.util.*;

public class Taxibeat {
    public static void main(String[] args) {
        World myWorld = World.getInstance();
        ArrayList<Taxi> fleet = Taxi.parse();
        Client clientPosition = Client.parse();

        ArrayList<Node> nodes = myWorld.parseNodes();
        HashMap<String, ArrayList<GraphConnection>> searchSpace = myWorld.generateSearchSpace(nodes, clientPosition);

        Position driverNode = myWorld.closestNode(nodes, fleet.get(0));
        solve(searchSpace, driverNode);

        for (Taxi taxi : fleet) {
            //taxi.printTaxi();
        }

        String[] test = {"test"};
        //XMLFile.getInstance().write(test);
    }

    private static void solve(HashMap<String, ArrayList<GraphConnection>> searchSpace, Position startPosition) {
        Comparator<GraphConnection> comparator = new GraphConnectionComparator();
        SortedSet<GraphConnection> queue = new TreeSet<GraphConnection>(comparator);
        Set<String> visited = new TreeSet<String>();

        for (GraphConnection neighbor : searchSpace.get(startPosition.stringify())) {
            queue.add(neighbor);
        }
        visited.add(startPosition.stringify());

        System.out.println("SOLVING");
        GraphConnection top = null;
        int maxFrontier = 5;
        while (queue.size() > 0) {
            top = queue.first();

            System.out.println("At ");
            top.print();
            visited.add(top.getNode());

            if (top.isGoal()) {
                System.out.println("FOUND THE CLIENT!!!");
                break;
            }

            for (GraphConnection neighbor : searchSpace.get(top.getNode())) {
                if (!visited.contains(neighbor.getNode())) {
                    System.out.println("Adding");
                    neighbor.print();
                    queue.add(neighbor);
                }
            }

            queue.remove(top);

            while (queue.size() > maxFrontier) {
                System.out.println("Removing");
                queue.last().print();
                queue.remove(queue.last());
            }
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
