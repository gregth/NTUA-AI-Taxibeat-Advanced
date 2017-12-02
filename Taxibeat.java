import java.io.*;
import java.util.ArrayList;

public class Taxibeat {
    public static void main() {
        ArrayList<Taxi> fleet = ParseTaxis.parse();
        Position clientPosition = ParseClient.parse();

        for (Taxi taxi : fleet) {
            taxi.printTaxi();
        }

        System.out.println("Client: ");
        clientPosition.print();
    }
}
