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
    }
}
