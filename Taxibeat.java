import java.io.*;
import java.util.*;

public class Taxibeat {
    public static Position driverPosition(HashMap<String, Set<Integer>> Nodes, Taxi driver) {
        Position driverPosition = driver.position;
        Position nodeDriverPosition = null;

        double minDriverDistance = -1, driverDistance, x, y;
        String[] parts;

        for(String currentNode : Nodes.keySet()) {
            parts = currentNode.split(" ");
            x = Double.valueOf(parts[0].trim());
            y = Double.valueOf(parts[1].trim());

            driverDistance = Math.sqrt(Math.pow(driverPosition.x - x, 2) + Math.pow(driverPosition.y - y, 2));

            if (minDriverDistance < 0 || driverDistance < minDriverDistance) {
                minDriverDistance = driverDistance;
                nodeDriverPosition = new Position(x, y);
            }
        }

        return nodeDriverPosition;
    }

    public static Position clientPosition(HashMap<String, Set<Integer>> Nodes, Position clientPosition) {
        Position nodeClientPosition = null;

        double minClientDistane = -1, clientDistance, x, y;
        String[] parts;

        for(String currentNode : Nodes.keySet()) {
            parts = currentNode.split(" ");
            x = Double.valueOf(parts[0].trim());
            y = Double.valueOf(parts[1].trim());

            clientDistance = Math.sqrt(Math.pow(clientPosition.x - x, 2) + Math.pow(clientPosition.y - y, 2));

            if (minClientDistane < 0 || clientDistance < minClientDistane) {
                minClientDistane = clientDistance;
                nodeClientPosition = new Position(x, y);
            }
        }

        return nodeClientPosition;
    }

    public static void main(String[] args) {
        ArrayList<Taxi> fleet = ParseTaxis.parse();
        Position mapClientPosition = ParseClient.parse();

        HashMap<String, Set<Integer>> Nodes = ParseNodes.parse();

        Position driverPosition = Taxibeat.driverPosition(Nodes, fleet.get(0));
        Position clientPosition = Taxibeat.clientPosition(Nodes, mapClientPosition);

        for (Taxi taxi : fleet) {
            //taxi.printTaxi();
        }

        System.out.println("Client: ");
        clientPosition.print();
        System.out.println("Taxi: ");
        driverPosition.print();
    }
}
