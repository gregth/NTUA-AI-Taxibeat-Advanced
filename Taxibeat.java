import java.io.*;
import java.util.*;

public class Taxibeat {
    public static void main(String[] args) {
        World myWorld = World.getInstance();
        ArrayList<Taxi> fleet = ParseTaxis.parse();
        Client mapClientPosition = ParseClient.parse();

        myWorld.parse();
        myWorld.generateSearchSpace(mapClientPosition);

        Position driverPosition = myWorld.closestStreeNode(fleet.get(0));
        Position clientPosition = myWorld.closestStreeNode(mapClientPosition);

        for (Taxi taxi : fleet) {
            //taxi.printTaxi();
        }

        System.out.println("Client: ");
        clientPosition.print();
        System.out.println("Taxi: ");
        driverPosition.print();
    }
}
