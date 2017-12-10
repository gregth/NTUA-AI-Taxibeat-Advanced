import java.io.*;
import java.util.*;

public class Taxibeat {
    public static void main(String[] args) {
        World myWorld = World.getInstance();
        ArrayList<Taxi> fleet = Taxi.parse();
        Client clientPosition = Client.parse();

        myWorld.parse();
        myWorld.generateSearchSpace(clientPosition);
        HashMap<String, ArrayList<GraphNode>> searchSpace = myWorld.addDriverToSpace(fleet.get(0));

        Position driverPosition = myWorld.closestStreeNode(fleet.get(0));

        for (Taxi taxi : fleet) {
            //taxi.printTaxi();
        }

        System.out.println("Client: ");
        myWorld.clientPosition.print();
        System.out.println("Taxi: ");
        clientPosition.print();
    }
}
