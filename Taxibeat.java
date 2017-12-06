import java.io.*;
import java.util.*;

public class Taxibeat {
    public static Position driverPosition(HashMap<Integer, ArrayList<Position>> Nodes, Taxi driver) {
        Position driverPosition = driver.position;
        Position nodeDriverPosition = null;

        double minDriverDistance = -1;
        double driverDistance;

        for(int currentKey : Nodes.keySet()) {
            for (Position node : Nodes.get(currentKey)) {
                driverDistance = Math.sqrt(Math.pow(driverPosition.x - node.x, 2) + Math.pow(driverPosition.y - node.y, 2));

                if (minDriverDistance < 0 || driverDistance < minDriverDistance) {
                    minDriverDistance = driverDistance;
                    nodeDriverPosition = new Position(node.x, node.y);
                }
            }
        }

        return nodeDriverPosition;
    }

    public static Position clientPosition(HashMap<Integer, ArrayList<Position>> Nodes, Position clientPosition) {
        Position nodeClientPosition = null;

        double minClientDistane = -1;
        double clientDistance;

        for(int currentKey : Nodes.keySet()) {
            for (Position node : Nodes.get(currentKey)) {
                clientDistance = Math.sqrt(Math.pow(clientPosition.x - node.x, 2) + Math.pow(clientPosition.y - node.y, 2));

                if (minClientDistane < 0 || clientDistance < minClientDistane) {
                    minClientDistane = clientDistance;
                    nodeClientPosition = new Position(node.x, node.y);
                }
            }
        }

        return nodeClientPosition;
    }

    public static void main(String[] args) {
        ArrayList<Taxi> fleet = ParseTaxis.parse();
        Position mapClientPosition = ParseClient.parse();

        HashMap<Integer, ArrayList<Position>> Nodes = ParseNodes.parse();

        Position driverPosition = Taxibeat.driverPosition(Nodes, fleet.get(0));
        Position clientPosition = Taxibeat.clientPosition(Nodes, mapClientPosition);

        for (Taxi taxi : fleet) {
            //taxi.printTaxi();
        }

        System.out.println("Client: ");
        clientPosition.print();
    }
}
